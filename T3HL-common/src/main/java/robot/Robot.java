

        package robot;

        import com.panneau.Panneau;
        import connection.Connection;
        import data.*;
        import data.controlers.DataController;
        import data.controlers.PanneauModule;
        import data.synchronization.SynchronizationWithBuddy;
        import locomotion.Locomotion;
        import locomotion.UnableToMoveException;
        import locomotion.UnableToMoveReason;
        import lowlevel.order.Order;
        import orders.OrderWrapper;
        import orders.Speed;
        import orders.hooks.HookFactory;
        import orders.hooks.HookNames;
        import orders.order.MontlheryOrders;
        import pfg.config.Configurable;
        import utils.HLInstance;
        import utils.Log;
        import utils.RobotSide;
        import utils.TimeoutError;
        import utils.communication.CommunicationException;
        import utils.communication.SimulatorDebug;
        import utils.container.ContainerException;
        import utils.container.Module;
        import utils.math.Vec2;
        import utils.math.VectCartesian;

        import java.io.IOException;
        import java.util.Objects;
        import java.util.Stack;
        import java.util.concurrent.TimeUnit;
        import java.util.function.Consumer;

/**
 * Classe regroupant tout les services et fonctionnalitées de base du robot
 *
 * @author rem, jglrxavpok
 */
public abstract class Robot implements Module {

    //FIXME: Il est malheuresement impossible d'avoir une autre couleur que jaune ou bleu sur ca panneau
    private static final Panneau.LedColor BLACK = Panneau.LedColor.NOIR;
    //private static final Panneau.LedColor START_FOLLOW_PATH = Panneau.LedColor.ROUGE;
    private static final Panneau.LedColor START_FOLLOW_PATH = Panneau.LedColor.NOIR;
    //private static final Panneau.LedColor FOLLOW_PATH_REATTEMPT_EVEN = Panneau.LedColor.BLEU;
    private static final Panneau.LedColor FOLLOW_PATH_REATTEMPT_EVEN = Panneau.LedColor.NOIR;
    //private static final Panneau.LedColor FOLLOW_PATH_REATTEMPT_ODD = Panneau.LedColor.VERT;
    private static final Panneau.LedColor FOLLOW_PATH_REATTEMPT_ODD = Panneau.LedColor.NOIR;

    /**
     * Module qui permet de communiquer avec le panneau de score et l'interrupteur pour la sélection de la couleur de jeu
     */
    private final PanneauModule panneauService;

    private HLInstance hl;
    /**
     * Permet d'envoyer des infos de debug
     */
    private SimulatorDebug simulatorDebug;

    public int score;

    /**
     * Module qui permet au robot de bouger
     */
    protected Locomotion locomotion;

    /**
     * Module qui permet au robot d'envoyer des ordres au LL
     */
    protected OrderWrapper orderWrapper;

    /**
     * Module de gestion des hooks
     */
    protected HookFactory hookFactory;

    /**
     * Position et Orientation du robot
     */
    protected XYO xyo;
    @Configurable
    private long locomotionLoopDelay;

    private Stack<CouleurPalet> leftElevator;
    private Stack<CouleurPalet> rightElevator;

    private Stack<CouleurVerre> rightCouloir = new Stack<CouleurVerre>();
    private Stack<CouleurVerre> leftCouloir = new Stack<CouleurVerre>();

    private boolean lighthouse;

    private int windsocks;


    /**
     * Est-ce qu'on est en mode simulation?
     */
    @Configurable
    private boolean simulation;

    @Configurable
    private boolean master;

    @Configurable
    private boolean symetry;
    private boolean forceInversion;
    @Configurable
    private boolean usingPanel;

    /**
     * @param locomotion   service de mouvement du robot
     * @param orderWrapper service d'envoie d'ordre vers le LL
     */
    protected Robot(HLInstance hl, Locomotion locomotion, OrderWrapper orderWrapper, HookFactory hookFactory, SimulatorDebug simulatorDebug, PanneauModule panneauService) {
        this.hl = hl;
        this.simulatorDebug = simulatorDebug;
        this.locomotion = locomotion;
        this.orderWrapper = orderWrapper;
        this.hookFactory = hookFactory;
        this.panneauService = panneauService;
        try {
            hl.module(DataController.class).setRobotClass(getClass());
        } catch (ContainerException e) {
            e.printStackTrace();
        }
    }

    public void increaseScore(int points) {
        this.score = this.score + points;
        if (master) {
            if (usingPanel && panneauService.getPanneau() != null) {
                this.panneauService.getPanneau().printScore(score);
            }
        } else {
            try {
                hl.module(SynchronizationWithBuddy.class).increaseScore(points);
            } catch (ContainerException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Ordonnes un 'goto' vers le LL. ATTENTION! Cette méthode ne prend PAS en compte le pathfinding! Si ça va dans le mur c'est votre faute
     *
     * @param point le point vers lequel aller
     * @throws UnableToMoveException en cas de problème de blocage/adversaire
     */
    public void gotoPoint(Vec2 point) throws UnableToMoveException {
        this.locomotion.gotoPoint(point);
    }

    /**
     * Cf {@link #followPathTo(Vec2, int)} (valeur de maxRetries à -1)
     *
     * @see #followPathTo(Vec2, int)
     */
    public void followPathTo(Vec2 point) throws UnableToMoveException {
        followPathTo(point, -1);
    }

    /**
     * Permet au robot d'aller jusqu'à un point donné. Actives les leds du robot en fonction de son état.<br/>
     * <ul>
     *     <li>Rouge: Première tentative de followpath</li>
     *     <li>Bleu: Nouvelle tentative de followpath (indice pair)</li>
     *     <li>Vert: Nouvelle tentative de followpath (indice impair)</li>
     * </ul>
     *
     * @param point      le point visé
     * @param maxRetries le nombre de réessais à faire, 0 throw un UnableToMoveException dès le premier échec, -1 signifie de tester à l'infini
     * @throws UnableToMoveException en cas de problème de blocage/adversaire
     */
    public void followPathTo(Vec2 point, int maxRetries) throws UnableToMoveException, TimeoutError {
        try {
            debugLeds(START_FOLLOW_PATH);
            if (maxRetries == -1) {
                int counter = 0;
                while (true) {
                    UnableToMoveException e = attemptToGoto(point);
                    if (e == null) { // on a réussi
                        break;
                    }
                    if (counter % 2 == 0) {
                        debugLeds(FOLLOW_PATH_REATTEMPT_EVEN);
                    } else {
                        debugLeds(FOLLOW_PATH_REATTEMPT_ODD);
                    }
                    counter++;
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e1) {
                        return;
                    }
                }
            } else {
                UnableToMoveException lastException = null;
                for (int i = 0; i < maxRetries + 1; i++) {
                    lastException = attemptToGoto(point);
                    if (lastException == null) { // on a réussi
                        return;
                    }
                    if (i % 2 == 0) {
                        debugLeds(FOLLOW_PATH_REATTEMPT_EVEN);
                    } else {
                        debugLeds(FOLLOW_PATH_REATTEMPT_ODD);
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                throw lastException;
            }
        } finally {
            debugLeds(BLACK);
        }
    }

    private void debugLeds(Panneau.LedColor color) {
        if (usingPanel) {
            if (panneauService.getPanneau() != null) {
                panneauService.getPanneau().setLeds(color);
            }
        }
    }

    private UnableToMoveException attemptToGoto(Vec2 point) {
        try {
            this.locomotion.followPathTo(point);
            return null;
        } catch (UnableToMoveException e) {
            e.printStackTrace();
            return e;
        } catch (TimeoutError e) {
            e.printStackTrace();
            return new UnableToMoveException("TIMEOUT", new XYO(point, XYO.getRobotInstance().getOrientation()), UnableToMoveReason.TRAJECTORY_OBSTRUCTED);
        }
    }


    /**
     * Permet au robot de se tourner vers un point
     *
     * @param point point vers lequel se tourner
     */
    public void turnToPoint(Vec2 point) throws UnableToMoveException {
        this.turnTowards(point.minusVector(XYO.getRobotInstance().getPosition()).getA());
    }

    /**
     * Permet au robot de faire un GoTo en utilisant les ordres de base
     *
     * @param point               point vers lequel le robot se rend
     * @param expectingWallImpact true si l'on s'attend à un blocage mécanique (lorsque l'on veut se caler contre le mur par exemple)
     * @throws UnableToMoveException
     */
    public void softGoTo(Vec2 point, boolean expectingWallImpact) throws UnableToMoveException {
        this.turnToPoint(point);
        this.moveLengthwise((int) Math.round(point.distanceTo(XYO.getRobotInstance().getPosition())), expectingWallImpact);
    }


    /**
     * Permet au robot d'avancer/recluer en ligne droite
     *
     * @param distance           la distance à parcourir, négative si l'on veut aller en arrière
     * @param expectedWallImpact true si l'on s'attend à un blocage mécanique (lorsque l'on veut se caler contre le mur par exemple)
     * @throws UnableToMoveException en cas de problèmes de blocage/adversaire
     */
    public void moveLengthwise(int distance, boolean expectedWallImpact, Runnable... runnables) throws UnableToMoveException {
        this.locomotion.moveLengthwise(distance, expectedWallImpact, runnables);
    }

    /**
     * Permet au robot d'activer/arreter la ième ventouse
     *
     * @param posventouse le numéro de la ventouse (compris entre 1 et 4)
     * @param succion     1 si l'on souhaite activer succion
     */
    public void suck(int posventouse, int succion, Runnable... runnables) {
        this.orderWrapper.suck(posventouse, succion, runnables);
    }

    public void suckall(int succion, Runnable... runnables) {
        this.orderWrapper.suckall(succion, runnables);
    }

    public void hammers(int level, Runnable... runnables) {
        this.orderWrapper.hammers(level, runnables);
    }

    public void hammer(int level1, int level2, int level3, int level4, int level5, Runnable[] runnables) {
        this.orderWrapper.hammer(level1, level2, level3, level4, level5, runnables);
    }

    /**
     * Permet au robot de lever/baisser le bras pour le drapeau
     *
     * @param level vaut 1 si bras levé et 0 sinon
     */
    public void flag(int level, Runnable[] runnables){
        this.orderWrapper.flag(level,runnables);
    }
    
    
    /**
     * Permet au robot de tourner sur lui-même
     *
     * @param point point vers lequel on veut se tourner
     * @throws UnableToMoveException en cas de problème de blocage/adversaire
     */
    public void turnTowards(Vec2 point) throws UnableToMoveException {
        this.locomotion.turnTowards(point);
    }

    /**
     * Permet au robot de tourner sur lui-même
     *
     * @param angle angle absolue vers lequel on veut se tourner
     * @throws UnableToMoveException en cas de problème de blocage/adversaire
     */
    public void turnTowards(double angle) throws UnableToMoveException {
        this.locomotion.turnTowards(angle);
    }

    public void perform(Order order) {
        this.orderWrapper.perform(order);
    }

    public void perform(Order order, boolean waitForConfirmation) {
        this.orderWrapper.perform(order, waitForConfirmation);
    }

    /**
     * Permet au robot de désactiver l'asserv en rotation
     */
    public void disableRotation() {
        this.orderWrapper.noRotationControl();
    }

    /**
     * Permet au robot d'activiter l'asserv en rotation
     */
    public void ableRotation() {
        this.orderWrapper.rotationControl();
    }

    /**
     * MECA >>>>>>>>>>> SICKS (recalage mecanique sur une distance à spécifier)
     */
    public void recalageMeca(boolean avant, int distReculeAbsolu) {
        this.disableRotation();
        distReculeAbsolu = Math.abs(distReculeAbsolu);
        try {
            this.setTranslationSpeed(Speed.SLOW_ALL);
            if (avant) {
                this.moveLengthwise(1000, true);
            } else {
                this.moveLengthwise(-1000, true);
            }
            this.setTranslationSpeed(Speed.DEFAULT_SPEED);
            this.ableRotation();
            if (avant) {
                this.moveLengthwise(-distReculeAbsolu, false);
            } else {
                this.moveLengthwise(distReculeAbsolu, false);
            }

        } catch (UnableToMoveException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recalage LiDAR (par rapport aux 3 balises)
     */
    public void recalageLidar() {
        SensorState.RECALAGE_LIDAR_EN_COURS.setData(true);
        try {
            Connection.LIDAR_DATA.send(""); //Envoie d'une requête pour avoir des données brutes
        } catch (CommunicationException e) {
            Log.STDOUT.critical("Envoi impossible de la requête de données brutes au processus Lidar");
            e.printStackTrace();
            SensorState.RECALAGE_LIDAR_EN_COURS.setData(false);
            return;
        }
        Module.waitWhileTrue(SensorState.RECALAGE_LIDAR_EN_COURS);
    }


    /**
     * Change la vitesse du LL
     *
     * @param speed la vitesse souhaitée
     */
    public void setSpeed(Speed speed) {
        this.orderWrapper.setBothSpeed(speed);
    }

    /**
     * Change la vitesse de translation du LL
     *
     * @param speed la vitesse souhaitée
     */
    public void setTranslationSpeed(Speed speed) {
        this.orderWrapper.setTranslationnalSpeed(speed.getTranslationSpeed());
    }

    /**
     * Change la vitesse de rotation du LL
     *
     * @param speed la vitesse souhaitée
     */
    public void setRotationSpeed(Speed speed) {
        this.orderWrapper.setRotationnalSpeed(speed.getRotationSpeed());
    }

    /**
     * Change la position du LL
     *
     * @param pos         position souhaitée
     * @param orientation orientation souhaitée
     */
    public void setPositionAndOrientation(Vec2 pos, double orientation) {
        this.orderWrapper.setPositionAndOrientation(pos, orientation);
    }

    public void setOrientation(double orientation) {
        this.orderWrapper.setOrientation(orientation);
    }

    /**
     * Configure des hooks
     *
     * @param hooks les hooks à configurer
     */
    public void configureHook(HookNames... hooks) {
        //TODO
    }

    /**
     * Méthode qui permet le recalage avec les sicks
     */
    public void computeNewPositionAndOrientation(Sick[] significantSicks) {
        if (simulation) {
            return;
        }
        System.out.println(" JE CROIS ETRE ICI : " + " Position =" + XYO.getRobotInstance().getPosition() + " orientation =" + XYO.getRobotInstance().getOrientation());

        Sick.resetNewXYO();
        Sick.setSignificantSicks(significantSicks);
        this.orderWrapper.getSickData();
        XYO newXYO = Sick.getNewXYO();
        if (significantSicks == Sick.NOTHING) {
            return;
        }

        // remplacement de la position dans le HL
        XYO.getRobotInstance().update(newXYO.getPosition().getX(), newXYO.getPosition().getY(), newXYO.getOrientation());

        Log.LOCOMOTION.debug("New position with SICKs: " + newXYO);
        // remplacement de la position dans le LL
        this.orderWrapper.setPositionAndOrientation(newXYO.getPosition(), newXYO.getOrientation());
    }

    /**
     * Actives le mode montlhery
     */
    public void switchToMontlheryMode() {
        this.orderWrapper.sendString(MontlheryOrders.Montlhery.toLL());
        this.orderWrapper.sendString(MontlheryOrders.MaxRotationSpeed.toLL() + " " + Math.PI / 4f);
        this.orderWrapper.sendString(MontlheryOrders.MaxTranslationSpeed.toLL() + " 180");
    }

    // Gestion des ascenseurs

    /**
     * Initialises l'ascenseur de droite
     */
    protected void createRightElevator() {
        this.rightElevator = new Stack<>();
    }

    /**
     * Initialises l'ascenseur de gauche
     */
    protected void createLeftElevator() {
        this.leftElevator = new Stack<>();
    }

    /**
     * Envoie une mise à jour de la liste de palets au simulateur si jamais il est connecté
     */
    private void sendElevatorUpdate() {
        if (leftElevator != null)
            simulatorDebug.sendElevatorContents(RobotSide.LEFT, leftElevator);
        if (rightElevator != null)
            simulatorDebug.sendElevatorContents(RobotSide.RIGHT, rightElevator);
    }

    /**
     * Envoie une mise à jour de l'état des couloirs au simulateur si jamais il est connecté
     */

    private void sendCouloirUpdate() {
        simulatorDebug.sendCouloirContents(RobotSide.LEFT, leftCouloir);
        simulatorDebug.sendCouloirContents(RobotSide.RIGHT, rightCouloir);
    }


    public boolean getLighthouse() {
        return lighthouse;
    }

    /**
     * Valide les manches à air
     */
    public void validateWindsocks(int state) {
        windsocks = state;
        if (simulatorDebug != null) {
            simulatorDebug.sendWindsocksContents(windsocks);
        }
    }

    public int getWindsocks() {
        return windsocks;
    }


    /**
     * Ajoute un palet dans l'ascenseur de gauche
     *
     * @throws NullPointerException si l'ascenseur n'existe pas
     */
    public void pushPaletGauche(CouleurPalet palet) {
        if (master && symetry()) { // le secondaire ne fait pas de symétrie ici
            pushPaletDroitNoSymetry(palet);
        } else {
            pushPaletGaucheNoSymetry(palet);
        }
    }

    /**
     * Ajoute un palet dans l'ascenseur de droite
     *
     * @throws NullPointerException si l'ascenseur n'existe pas
     */
    public void pushPaletDroitNoSymetry(CouleurPalet palet) {
        Objects.requireNonNull(rightElevator, "Tentative d'insérer un palet dans l'ascenseur de droite alors qu'il n'y a pas d'ascenseur à droite dans ce robot!");
        rightElevator.push(palet);
        sendElevatorUpdate();
    }

    /**
     * Ajoute un palet dans l'ascenseur de gauche
     *
     * @throws NullPointerException si l'ascenseur n'existe pas
     */
    public void pushPaletGaucheNoSymetry(CouleurPalet palet) {
        Objects.requireNonNull(leftElevator, "Tentative d'insérer un palet dans l'ascenseur de gauche alors qu'il n'y a pas d'ascenseur à droite dans ce robot!");
        leftElevator.push(palet);
        sendElevatorUpdate();
    }

    /**
     * (Re-)Symétrise les actions qu'on passe en argument, c'est utile quand on veut faire les mêmes actions des deux côtés du robot
     *
     * @param actions
     */
    public void invertOrders(Consumer<Robot> actions) {
        try {
            forceInversion = true;
            orderWrapper.setInverted(true);
            actions.accept(this);
        } finally { // on s'assure de pas laisser le robot dans un état indésirable
            forceInversion = false;
            orderWrapper.setInverted(false);
        }
    }

    public boolean symetry() {
        return master && (symetry ^ forceInversion);
    }

    /**
     * Renvoies l'ascenseur de gauche, ou 'null' s'il n'existe pas
     */
    public Stack<CouleurPalet> getLeftElevatorOrNull() {
        if (master && symetry()) {
            return rightElevator;
        } else {
            return leftElevator;
        }
    }

    /**
     * Renvoies l'ascenseur de droite, ou 'null' s'il n'existe pas
     */
    public Stack<CouleurPalet> getRightElevatorOrNull() {
        if (master && symetry()) {
            return leftElevator;
        } else {
            return rightElevator;
        }
    }

    /**
     * Renvoies le couloir de gauche
     */
    public Stack<CouleurVerre> getLeftCouloir() {
        if (master && symetry()) {
            return rightCouloir;
        } else {
            return leftCouloir;
        }
    }

    /**
     * Renvoies le couloir de droite
     */
    public Stack<CouleurVerre> getRightCouloir() {
        if (master && symetry()) {
            return leftCouloir;
        } else {
            return rightCouloir;
        }
    }

    public XYO getXyo() {
        return this.xyo;
    }


}


