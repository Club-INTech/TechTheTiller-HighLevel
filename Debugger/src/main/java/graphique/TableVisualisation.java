package graphique;

import data.CouleurVerre;
import data.XYO;
import data.table.Obstacle;
import simulator.IRobot;
import utils.RobotSide;
import utils.math.Vec2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.awt.geom.Area;

import graphique.FenetreDemarrage;

public class TableVisualisation extends JPanel {


    /**
     * La taille de l'image est 893x573
     * Le bord de la table en haut à gauche est aux coordonnées (43,21)
     * La surface de jeu fait 982x690
     *
     * Une légende concernant la numérotation des gobelets est fournis dans le dossier ressources
     * (TableLegendeGobeletsNumerotation.png)
     **/

    private final int CoinHautGaucheX = 43;
    private final int CoinHautGaucheY = 21;
    private final int TABLE_PIXEL_WIDTH = 982; //en pixels
    private final int TABLE_PIXEL_HEIGHT = 690;//en pixels
    private final int TABLEGAME_PIXEL_WIDTH = 893; // largeur de la table de jeu en pixels
    private final int TABLEGAME_PIXEL_HEIGHT = 573; //hauteur de la table de jeu en pixels
    private final int WIDTH_TABLEGAME = 3000;      // vrai largeur de la table en millimetre
    private final int HEIGHT_TABLEGAME = 2000;     // vrai hauteur de la table en millimetre
    private final int GobeletRay = 27; // rayon d'un gobelet en  millimetre
    private final int PhareRay = 55; // rayon du phare en  millimetre
    private final int MANCHE_WIDTH = (int) transformTableDistanceToInterfaceDistance(200);
    private final int MANCHE_HEIGHT = (int) transformTableDistanceToInterfaceDistance(40);
    private final int GOBELET_NORMALISE = (int) transformTableDistanceToInterfaceDistance(36);
    private final int ROBOT_NORMALISE_WIDTH = (int) transformTableDistanceToInterfaceDistance(350);
    private final int ROBOT_NORMALISE_HEIGHT = (int) transformTableDistanceToInterfaceDistance(220);

    final int PrincipalWidth = 350;
    final int PrincipalHeigh = 220;


    /* ============ Affichage de la table et des robots  ============= */


    String FileTableImage = "Debugger/src/main/java/graphique/ressources/tableComplete2020Fond.png";
    String FilePrincipalImage = "Debugger/src/main/java/graphique/ressources/PrincipalVuDessusInterfaceSize.png";
    String FileSecondaireImage = "Debugger/src/main/java/graphique/ressources/SecondaireVuDessusInterfaceSize.png";


    public static BufferedImage Principal;       //Robot séléctionné (ie. qui joue le match)
    public static BufferedImage RobotPrincipal;  //Robot séléctionné orienté
    public static BufferedImage Secondaire;      // Robot ami
    public static BufferedImage RobotSecondaire; //Robot ami orienté
    private Image Table;

    private static Scrollbar fenetreLog = new Scrollbar();

    private int posX;
    private int posY;
    private int SposX; //Abscisse du secondaire (robot ami)
    private int SposY; //ordonnée du secondaire (robot ami)

    private double orientation;
    private double orientationS; //orientation du robotAmi (secondaire)

    private boolean principal = true; //true = les logs correspondent au robot principal

    public boolean etatPrincipal = false; //false = il n'y a jamais  eu de logs concernant le buddy jusqu'à présent
    public boolean etatSecondaire = false; //false = il n'y a jamais  eu de logs concernant le buddy jusqu'à présent

    private int i = 1;


    public int getPosX() {
        return posX;
    }
    public int getSPosX() {
        return SposX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }
    public void setSPosX(int SposX) {
        this.SposX = SposX;
    }

    public int getPosY() {
        return posY;
    }
    public int getSPosY() {
        return SposY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
    public void setSPosY(int SposY) {
        this.SposY = SposY;
    }

    public void setOrientation( double t) { orientation = t; }
    public void setOrientationS( double t) { orientationS = t; }

    public void setPrincipal(boolean bool) { principal = bool; }



        /**DESSIN DE LA TABLE DE JEU**/

    public double getOrientation() { return orientation; }
    public double getOrientationS() { return orientationS; }


    public TableVisualisation() {

        //permet de modifier les positions des boutons de FenêtreTable (cf. setBound())
        setLayout(null);

        try {
            Table = ImageIO.read(new File(FileTableImage));

//            System.out.println("robot n°" + principal);

            if (FenetreDemarrage.getChoixRobot()) {
                Principal = ImageIO.read(new File(FilePrincipalImage));
                Secondaire = ImageIO.read(new File(FileSecondaireImage));
            }else{
                Principal = ImageIO.read(new File(FileSecondaireImage));
                Secondaire = ImageIO.read(new File(FilePrincipalImage));
            }

            RobotPrincipal = rotate(Principal, orientation);
            RobotSecondaire = rotate(Secondaire, orientationS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        /**DESSIN DE LA TABLE DE JEU**/

        g.drawImage(Table, 0, 0, this.getWidth(), this.getHeight(), this);


        /**Affichage des ennemis**/

        drawEnnemi(g);

        /** Visualisation des gobelets sur la table**/

        initGobeletsRouges();
        initGobeletsVerts();

        drawGobelets(g, GobeletsRouge, Color.RED.darker());
        drawGobelets(g, GobeletsVert, Color.GREEN.darker().darker());

        /**Visualisation du phare et des manches à air sur la table**/
        ArrayList<String> allume = new ArrayList<>();
        allume.add("allume");

        if (!Phare.isEmpty()) {
            if (EtatPhare.equals(allume)) {
                drawPhare(g, Phare, Color.GREEN);
            } else {
                drawPhare(g, Phare, Color.BLACK);
            }
        }
        ArrayList<String> aa = new ArrayList<>();
        aa.add("active");
        aa.add("active");
        //System.out.print("aa ="+aa);

        ArrayList<String> ad = new ArrayList<>();
        ad.add("active");
        ad.add("desactive");

        ArrayList<String> da = new ArrayList<>();
        da.add("desactive");
        da.add("active");

        ArrayList<String> dd = new ArrayList<>();
        dd.add("desactive");
        dd.add("desactive");

        if (!Manche.isEmpty()) {
            if (EtatManche.equals(aa)) {
                drawManche(g, Manche, Color.GREEN, Color.GREEN);
            } else if (EtatManche.equals(ad)) {
                drawManche(g, Manche, Color.GREEN, Color.RED);
            } else if (EtatManche.equals(da)) {
                drawManche(g, Manche, Color.RED, Color.GREEN);
            } else if (EtatManche.equals(dd)) {
                drawManche(g, Manche, Color.RED, Color.RED);
            } else {
                drawManche(g, Manche, Color.RED, Color.RED);
            }
        }

        /**VISUALISATION DE NOTRE ROBOT (celui qui joue) **/

        if (RobotPrincipal != null && etatPrincipal==true) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(RobotPrincipal, posX, posY, this);
            g2d.dispose();
        }


        /**VISUALISATION DU DEUXIÈME ROBOT (le buddy)**/
        if (RobotSecondaire != null && etatSecondaire==true){
            Graphics2D g2d2 = (Graphics2D) g.create();
            g2d2.drawImage(RobotSecondaire, SposX, SposY, this);
            g2d2.dispose();
        }


    }
    /* ================================= Traitement du phare sur la table ======================================= */
    private ArrayList<Point> Phare = new ArrayList<>();
    private ArrayList<String> EtatPhare = new ArrayList<>();

    private void addPhare(Point phare) {
        synchronized (Phare) {
            Phare.add(phare);
        }
    }
    private void addEtat(String etatPhare) {
        synchronized (EtatPhare) {
            EtatPhare.clear();
            EtatPhare.add(etatPhare);
        }
    }

    private void drawPhare(Graphics g, ArrayList<Point> Phare, Color CouleurPhare) {
        for (Point point : Phare) {
            Point PhareCenter = transformTableCoordonateToInterfaceCoordonate(point);
            g.setColor(CouleurPhare);
            drawCenteredCircle(g, PhareCenter.x, PhareCenter.y, 2 * (int) transformTableDistanceToInterfaceDistance(PhareRay));
        }
    }
    public void PhareJaune() {
        Point PhareJaune = new Point(3000 - 230, -130);
        addPhare(PhareJaune);
    }
    public void PhareBleu() {
        Point PhareBleu = new Point(230, -130);
        addPhare(PhareBleu);
    }
    public void PhareAllume() {
        addEtat("allume");
    }
    public void PhareEteint() {
    }

    /* ================================= Traitement des manches à air sur la table ======================================= */
    private ArrayList<Point> Manche = new ArrayList<>();
    private ArrayList<String> EtatManche = new ArrayList<>();

    private void addManche(Point manche) {
        synchronized (Manche) {
            Manche.add(manche);
        }
    }
    private void addEtatManche(String etatManche1, String etatManche2) {
        synchronized (EtatManche) {
            EtatManche.clear();
            EtatManche.add(etatManche1);
            EtatManche.add(etatManche2);
        }
    }
    private void drawManche(Graphics g, ArrayList<Point> Manche, Color CouleurManche1, Color CouleurManche2) {
            Point MancheDebut1 = transformTableCoordonateToInterfaceCoordonate(Manche.get(0));
            Point MancheDebut2 = transformTableCoordonateToInterfaceCoordonate(Manche.get(1));
            g.drawRect(MancheDebut1.x, MancheDebut1.y, MANCHE_WIDTH, MANCHE_HEIGHT);
            g.drawRect(MancheDebut2.x, MancheDebut2.y, MANCHE_WIDTH, MANCHE_HEIGHT);
            g.setColor(CouleurManche1);
            //System.out.print ("couleurManche1 ="+ CouleurManche1);
            g.fillRect(MancheDebut1.x, MancheDebut1.y, MANCHE_WIDTH, MANCHE_HEIGHT);
            g.setColor(CouleurManche2);
            //System.out.print ("couleurManche2 ="+ CouleurManche2);
            g.fillRect(MancheDebut2.x, MancheDebut2.y, MANCHE_WIDTH, MANCHE_HEIGHT);
    }
    public void MancheJaune() {
        Point MancheJaune1 = new Point(3000 - 30, 2022);
        addManche(MancheJaune1);
        Point MancheJaune2 = new Point(3000 - 500, 2022);
        addManche(MancheJaune2);
    }
    public void MancheBleu() {
        Point MancheBleu1 = new Point(30, 2022);
        addManche(MancheBleu1);
        Point MancheBleu2 = new Point(500, 2022);
        addManche(MancheBleu2);
    }
    public void MancheDD() {
        addEtatManche("desactive", "desactive");
        //System.out.print("MancheDD ="+EtatManche);
    }
    public void MancheAD() {
        addEtatManche("active", "desactive");
        //System.out.print("MancheAD="+EtatManche);
    }
    public void MancheDA() {
        addEtatManche("desactive", "active");
        //System.out.print("MancheDA="+EtatManche);
    }
    public void MancheAA() {
        addEtatManche("active", "active");
        //System.out.print("MancheAA ="+EtatManche);
    }

    /* ================================= Traitement des gobelets sur la table ======================================= */


    private final ArrayList<Point> GobeletsRouge = new ArrayList<>();
    private final ArrayList<Point> GobeletsVert = new ArrayList<>();

    private void addGobeletsRouges(Point gobelet) {
        synchronized (GobeletsRouge) {
            GobeletsRouge.add(gobelet);
        }
    }

    private void addGobeletsVerts(Point gobelet) {
        synchronized (GobeletsVert) {
            GobeletsVert.add(gobelet);
        }
    }

    /**Enleve les gobelets de la table**/
    private void removeGobeletsRouges(Point gobelet) {
        synchronized (GobeletsRouge) {
            GobeletsRouge.remove(gobelet);
        }
    }
    private void removeGobeletsVerts(Point gobelet) {
        synchronized (GobeletsVert) {
            GobeletsVert.remove(gobelet);
        }
    }

    private void drawCenteredCircle(Graphics g, int x, int y, int r) {
        x = x - (r / 2);
        y = y - (r / 2);
        g.fillOval(x, y, r, r);
    }


    private void drawGobelets(Graphics g, ArrayList<Point> Gob, Color couleur) {
        for (Point point : Gob) {
            Point GobCenter = transformTableCoordonateToInterfaceCoordonate(point);

            g.setColor(couleur);
            drawCenteredCircle(g, GobCenter.x, GobCenter.y, 2 * (int) transformTableDistanceToInterfaceDistance(GobeletRay));
        }

    }

    public void initGobeletsVerts() {
        /**zone de haut **/
        Random rand = new Random();
        int[] vx_random = new int[3];
        int[] vy_random = new int[3];
        for (int i = 0; i < vx_random.length; i++) {
            vx_random[i]= 1000 + rand.nextInt(1000);
            vy_random[i]= rand.nextInt(500);
            while (Math.sqrt(Math.pow(vx_random[i]-1500, vx_random[i]-1500) + Math.pow(vy_random[i],vy_random[i])) > 500) {
                vx_random[i]= 1000 + rand.nextInt(1000);
                vy_random[i]= rand.nextInt(500);
            }
        }
        /**définition des points verts**/

        Point Vert1 = new Point(300, 1200);
        addGobeletsVerts(Vert1);

        Point Vert2 = new Point(450, 510);
        addGobeletsVerts(Vert2);

        Point Vert3 = new Point(950, 400);
        addGobeletsVerts(Vert3);

        Point Vert4 = new Point(1065, 1650);
        addGobeletsVerts(Vert4);

        Point Vert5 = new Point(1270, 1200);
        addGobeletsVerts(Vert5);

        Point Vert6 = new Point(1395, 1955);
        addGobeletsVerts(Vert6);

        Point Vert7 = new Point(1665, 1650);
        addGobeletsVerts(Vert7);

        Point Vert8 = new Point(1900, 800);
        addGobeletsVerts(Vert8);

        Point Vert9 = new Point(1995, 1955);
        addGobeletsVerts(Vert9);

        Point Vert10 = new Point(2330, 100);
        addGobeletsVerts(Vert10);

        Point Vert11 = new Point(2550, 1080);
        addGobeletsVerts(Vert11);

        Point Vert12 = new Point(2700, 400);
        addGobeletsVerts(Vert12);

        Point Vert13=new Point(1500,440);
        addGobeletsVerts(Vert13);

        Point Vert14=new Point(0,0);
        addGobeletsVerts(Vert14);

        Point Vert15=new Point(vx_random[2],vy_random[2]);
        addGobeletsRouges(Vert15);

        //Les gobelets dans les éceuils sont numérotés de haut en bas

        Point EcueilJauneVert1 = new Point(3067,1750);
        addGobeletsVerts(EcueilJauneVert1);

        Point EcueilJauneVert2 = new Point (3067,1600);
        addGobeletsVerts(EcueilJauneVert2);

      /*  Point EcueilJauneVert3 = new Point (3067, 1450);
        addGobeletsVerts(EcueilJauneVert3);

        Point EceuilBleuVert1 = new Point (-67, 1675);
        addGobeletsVerts(EceuilBleuVert1);

        Point EceuilBleuVert2 = new  Point (-67, 1525);
        addGobeletsVerts(EceuilBleuVert2);*/
    }


    public void initGobeletsRouges() {
        /**zone de haut fond **/
        Random rand = new Random();
        int[] rx_random = new int[3];
        int[] ry_random = new int[3];
        for (int i = 0; i <rx_random.length; i++) {
            rx_random[i]= 1000 + rand.nextInt(1000);
            ry_random[i]= rand.nextInt(500);
            while (Math.sqrt(Math.pow(rx_random[i]-1500, rx_random[i]-1500) + Math.pow(ry_random[i],ry_random[i])) > 500) {
                rx_random[i]= 1000 + rand.nextInt(1000);
                ry_random[i]= rand.nextInt(500);
            }
        }
        /**définition des gobelets**/

        Point Rouge1 = new Point(300, 400);
        addGobeletsRouges(Rouge1);

        Point Rouge2 = new Point(450, 1080);
        addGobeletsRouges(Rouge2);

        Point Rouge3 = new Point(670, 100);
        addGobeletsRouges(Rouge3);

        Point Rouge4 = new Point(1005, 1955);
        addGobeletsRouges(Rouge4);

        Point Rouge5 = new Point(1100, 800);
        addGobeletsRouges(Rouge5);

        Point Rouge6 = new Point(1335, 1650);
        addGobeletsRouges(Rouge6);

        Point Rouge7 = new Point(1605, 1955);
        addGobeletsRouges(Rouge7);

        Point Rouge8 = new Point(1730, 1200);
        addGobeletsRouges(Rouge8);

        Point Rouge9 = new Point(1935, 1650);
        addGobeletsRouges(Rouge9);

        Point Rouge10 = new Point(2050, 400);
        addGobeletsRouges(Rouge10);

        Point Rouge11 = new Point(2550, 510);
        addGobeletsRouges(Rouge11);

        Point Rouge12 = new Point(2700, 1200);
        addGobeletsRouges(Rouge12);

        Point Rouge13=new Point(rx_random[0],ry_random[0]);
        addGobeletsRouges(Rouge13);

        Point Rouge14=new Point(rx_random[1],ry_random[1]);
        addGobeletsRouges(Rouge14);

        Point Rouge15=new Point(rx_random[2],ry_random[2]);
        addGobeletsRouges(Rouge15);

        //Les gobelets dans les écueils sont numérotés de haut en bas

        Point EcueilJauneRouge1 = new Point(3067,1675);
        addGobeletsRouges(EcueilJauneRouge1);

        Point EcueilJauneRouge2 = new Point (3067,1525);
        addGobeletsRouges(EcueilJauneRouge2);

        Point EceuilBleuRouge1 = new Point (-67, 1750);
        addGobeletsRouges(EceuilBleuRouge1);

        Point EceuilBleuRouge2 = new  Point (-67, 1600);
        addGobeletsRouges(EceuilBleuRouge2);

        Point EcueilBleuRouge3 = new Point (-67, 1450);
        addGobeletsRouges(EcueilBleuRouge3);
    }


    /* =======================Différentes configuration des gobelets dans les éceuils communs=========================*/
    //les gobelets dans les éceuils communs sont numérotés de gauche à droite


    /*configurations des éceuils communs si le robot démarre dans la zone jaune*/
    public void RVRVV_J(){

        Point EceuilCommun1 = new Point(2300, -67);
        addGobeletsVerts(EceuilCommun1);

        Point EceuilCommun2 = new Point(2225, -67);
        addGobeletsVerts(EceuilCommun2);

        Point EceuilCommun3 = new Point(2150, -67);
        addGobeletsRouges(EceuilCommun3);

        Point EceuilCommun4 = new Point(2075, -67);
        addGobeletsVerts(EceuilCommun4);

        Point EceuilCommun5 = new Point(2000, -67);
        addGobeletsRouges(EceuilCommun5);

        Point EceuilCommun6 = new Point(1000, -67);
        addGobeletsRouges(EceuilCommun6);

        Point EceuilCommun7 = new Point(925, -67);
        addGobeletsVerts(EceuilCommun7);

        Point EceuilCommun8 = new Point(850, -67);
        addGobeletsRouges(EceuilCommun8);

        Point EceuilCommun9 = new Point(775, -67);
        addGobeletsVerts(EceuilCommun9);

        Point EceuilCommun10 = new Point(700, -67);
        addGobeletsVerts(EceuilCommun10);

    }

    public void RVVRV_J(){

        Point EceuilCommun1 = new Point(2300, -67);
        addGobeletsVerts(EceuilCommun1);

        Point EceuilCommun2 = new Point(2225, -67);
        addGobeletsRouges(EceuilCommun2);

        Point EceuilCommun3 = new Point(2150, -67);
        addGobeletsVerts(EceuilCommun3);

        Point EceuilCommun4 = new Point(2075, -67);
        addGobeletsVerts(EceuilCommun4);

        Point EceuilCommun5 = new Point(2000, -67);
        addGobeletsRouges(EceuilCommun5);

        Point EceuilCommun6 = new Point(1000, -67);
        addGobeletsRouges(EceuilCommun6);

        Point EceuilCommun7 = new Point(925, -67);
        addGobeletsVerts(EceuilCommun7);

        Point EceuilCommun8 = new Point(850, -67);
        addGobeletsVerts(EceuilCommun8);

        Point EceuilCommun9 = new Point(775, -67);
        addGobeletsRouges(EceuilCommun9);

        Point EceuilCommun10 = new Point(700, -67);
        addGobeletsVerts(EceuilCommun10);

    }

    public void RRVVV_J(){

        Point EceuilCommun1 = new Point(2300, -67);
        addGobeletsVerts(EceuilCommun1);

        Point EceuilCommun2 = new Point(2225, -67);
        addGobeletsVerts(EceuilCommun2);

        Point EceuilCommun3 = new Point(2150, -67);
        addGobeletsVerts(EceuilCommun3);

        Point EceuilCommun4 = new Point(2075, -67);
        addGobeletsRouges(EceuilCommun4);

        Point EceuilCommun5 = new Point(2000, -67);
        addGobeletsRouges(EceuilCommun5);

        Point EceuilCommun6 = new Point(1000, -67);
        addGobeletsRouges(EceuilCommun6);

        Point EceuilCommun7 = new Point(925, -67);
        addGobeletsRouges(EceuilCommun7);

        Point EceuilCommun8 = new Point(850, -67);
        addGobeletsVerts(EceuilCommun8);

        Point EceuilCommun9 = new Point(775, -67);
        addGobeletsVerts(EceuilCommun9);

        Point EceuilCommun10 = new Point(700, -67);
        addGobeletsVerts(EceuilCommun10);

    }

    public void VRRVR_J(){

        Point EceuilCommun1 = new Point(2300, -67);
        addGobeletsRouges(EceuilCommun1);

        Point EceuilCommun2 = new Point(2225, -67);
        addGobeletsVerts(EceuilCommun2);

        Point EceuilCommun3 = new Point(2150, -67);
        addGobeletsRouges(EceuilCommun3);

        Point EceuilCommun4 = new Point(2075, -67);
        addGobeletsRouges(EceuilCommun4);

        Point EceuilCommun5 = new Point(2000, -67);
        addGobeletsVerts(EceuilCommun5);

        Point EceuilCommun6 = new Point(1000, -67);
        addGobeletsVerts(EceuilCommun6);

        Point EceuilCommun7 = new Point(925, -67);
        addGobeletsRouges(EceuilCommun7);

        Point EceuilCommun8 = new Point(850, -67);
        addGobeletsRouges(EceuilCommun8);

        Point EceuilCommun9 = new Point(775, -67);
        addGobeletsVerts(EceuilCommun9);

        Point EceuilCommun10 = new Point(700, -67);
        addGobeletsRouges(EceuilCommun10);

    }
    public void VRVRR_J(){

        Point EceuilCommun1 = new Point(2300, -67);
        addGobeletsRouges(EceuilCommun1);

        Point EceuilCommun2 = new Point(2225, -67);
        addGobeletsRouges(EceuilCommun2);

        Point EceuilCommun3 = new Point(2150, -67);
        addGobeletsVerts(EceuilCommun3);

        Point EceuilCommun4 = new Point(2075, -67);
        addGobeletsRouges(EceuilCommun4);

        Point EceuilCommun5 = new Point(2000, -67);
        addGobeletsVerts(EceuilCommun5);

        Point EceuilCommun6 = new Point(1000, -67);
        addGobeletsVerts(EceuilCommun6);

        Point EceuilCommun7 = new Point(925, -67);
        addGobeletsRouges(EceuilCommun7);

        Point EceuilCommun8 = new Point(850, -67);
        addGobeletsVerts(EceuilCommun8);

        Point EceuilCommun9 = new Point(775, -67);
        addGobeletsRouges(EceuilCommun9);

        Point EceuilCommun10 = new Point(700, -67);
        addGobeletsRouges(EceuilCommun10);

    }
    public void VVRRR_J(){

        Point EceuilCommun1 = new Point(2300, -67);
        addGobeletsRouges(EceuilCommun1);

        Point EceuilCommun2 = new Point(2225, -67);
        addGobeletsRouges(EceuilCommun2);

        Point EceuilCommun3 = new Point(2150, -67);
        addGobeletsRouges(EceuilCommun3);

        Point EceuilCommun4 = new Point(2075, -67);
        addGobeletsVerts(EceuilCommun4);

        Point EceuilCommun5 = new Point(2000, -67);
        addGobeletsVerts(EceuilCommun5);

        Point EceuilCommun6 = new Point(1000, -67);
        addGobeletsVerts(EceuilCommun6);

        Point EceuilCommun7 = new Point(925, -67);
        addGobeletsVerts(EceuilCommun7);

        Point EceuilCommun8 = new Point(850, -67);
        addGobeletsRouges(EceuilCommun8);

        Point EceuilCommun9 = new Point(775, -67);
        addGobeletsRouges(EceuilCommun9);

        Point EceuilCommun10 = new Point(700, -67);
        addGobeletsRouges(EceuilCommun10);

    }

    /*configurations des écueils communs si le robot démarre dans la zone jaune*/

    public void RVRVV_B(){

        Point EceuilCommun1 = new Point(2300, -67);
        addGobeletsVerts(EceuilCommun1);

        Point EceuilCommun2 = new Point(2225, -67);
        addGobeletsVerts(EceuilCommun2);

        Point EceuilCommun3 = new Point(2150, -67);
        addGobeletsRouges(EceuilCommun3);

        Point EceuilCommun4 = new Point(2075, -67);
        addGobeletsVerts(EceuilCommun4);

        Point EceuilCommun5 = new Point(2000, -67);
        addGobeletsRouges(EceuilCommun5);

        Point EceuilCommun6 = new Point(1000, -67);
        addGobeletsRouges(EceuilCommun6);

        Point EceuilCommun7 = new Point(925, -67);
        addGobeletsVerts(EceuilCommun7);

        Point EceuilCommun8 = new Point(850, -67);
        addGobeletsRouges(EceuilCommun8);

        Point EceuilCommun9 = new Point(775, -67);
        addGobeletsVerts(EceuilCommun9);

        Point EceuilCommun10 = new Point(700, -67);
        addGobeletsVerts(EceuilCommun10);

    }

    public void RVVRV_B(){

        Point EceuilCommun1 = new Point(2300, -67);
        addGobeletsVerts(EceuilCommun1);

        Point EceuilCommun2 = new Point(2225, -67);
        addGobeletsRouges(EceuilCommun2);

        Point EceuilCommun3 = new Point(2150, -67);
        addGobeletsVerts(EceuilCommun3);

        Point EceuilCommun4 = new Point(2075, -67);
        addGobeletsVerts(EceuilCommun4);

        Point EceuilCommun5 = new Point(2000, -67);
        addGobeletsRouges(EceuilCommun5);

        Point EceuilCommun6 = new Point(1000, -67);
        addGobeletsRouges(EceuilCommun6);

        Point EceuilCommun7 = new Point(925, -67);
        addGobeletsVerts(EceuilCommun7);

        Point EceuilCommun8 = new Point(850, -67);
        addGobeletsVerts(EceuilCommun8);

        Point EceuilCommun9 = new Point(775, -67);
        addGobeletsRouges(EceuilCommun9);

        Point EceuilCommun10 = new Point(700, -67);
        addGobeletsVerts(EceuilCommun10);

    }

    public void RRVVV_B(){

        Point EceuilCommun1 = new Point(2300, -67);
        addGobeletsVerts(EceuilCommun1);

        Point EceuilCommun2 = new Point(2225, -67);
        addGobeletsVerts(EceuilCommun2);

        Point EceuilCommun3 = new Point(2150, -67);
        addGobeletsVerts(EceuilCommun3);

        Point EceuilCommun4 = new Point(2075, -67);
        addGobeletsRouges(EceuilCommun4);

        Point EceuilCommun5 = new Point(2000, -67);
        addGobeletsRouges(EceuilCommun5);

        Point EceuilCommun6 = new Point(1000, -67);
        addGobeletsRouges(EceuilCommun6);

        Point EceuilCommun7 = new Point(925, -67);
        addGobeletsRouges(EceuilCommun7);

        Point EceuilCommun8 = new Point(850, -67);
        addGobeletsVerts(EceuilCommun8);

        Point EceuilCommun9 = new Point(775, -67);
        addGobeletsVerts(EceuilCommun9);

        Point EceuilCommun10 = new Point(700, -67);
        addGobeletsVerts(EceuilCommun10);

        }

        public void VRRVR_B(){

        Point EceuilCommun1 = new Point(2300, -67);
        addGobeletsRouges(EceuilCommun1);

        Point EceuilCommun2 = new Point(2225, -67);
        addGobeletsVerts(EceuilCommun2);

        Point EceuilCommun3 = new Point(2150, -67);
        addGobeletsRouges(EceuilCommun3);

        Point EceuilCommun4 = new Point(2075, -67);
        addGobeletsRouges(EceuilCommun4);

        Point EceuilCommun5 = new Point(2000, -67);
        addGobeletsVerts(EceuilCommun5);

        Point EceuilCommun6 = new Point(1000, -67);
        addGobeletsVerts(EceuilCommun6);

        Point EceuilCommun7 = new Point(925, -67);
        addGobeletsRouges(EceuilCommun7);

        Point EceuilCommun8 = new Point(850, -67);
        addGobeletsRouges(EceuilCommun8);

        Point EceuilCommun9 = new Point(775, -67);
        addGobeletsVerts(EceuilCommun9);

        Point EceuilCommun10 = new Point(700, -67);
        addGobeletsRouges(EceuilCommun10);

    }
    public void VRVRR_B(){

        Point EceuilCommun1 = new Point(2300, -67);
        addGobeletsRouges(EceuilCommun1);

        Point EceuilCommun2 = new Point(2225, -67);
        addGobeletsRouges(EceuilCommun2);

        Point EceuilCommun3 = new Point(2150, -67);
        addGobeletsVerts(EceuilCommun3);

        Point EceuilCommun4 = new Point(2075, -67);
        addGobeletsRouges(EceuilCommun4);

        Point EceuilCommun5 = new Point(2000, -67);
        addGobeletsVerts(EceuilCommun5);

        Point EceuilCommun6 = new Point(1000, -67);
        addGobeletsVerts(EceuilCommun6);

        Point EceuilCommun7 = new Point(925, -67);
        addGobeletsRouges(EceuilCommun7);

        Point EceuilCommun8 = new Point(850, -67);
        addGobeletsVerts(EceuilCommun8);

        Point EceuilCommun9 = new Point(775, -67);
        addGobeletsRouges(EceuilCommun9);

        Point EceuilCommun10 = new Point(700, -67);
        addGobeletsRouges(EceuilCommun10);

    }
    public void VVRRR_B(){

        Point EceuilCommun1 = new Point(2300, -67);
        addGobeletsRouges(EceuilCommun1);

        Point EceuilCommun2 = new Point(2225, -67);
        addGobeletsRouges(EceuilCommun2);

        Point EceuilCommun3 = new Point(2150, -67);
        addGobeletsRouges(EceuilCommun3);

        Point EceuilCommun4 = new Point(2075, -67);
        addGobeletsVerts(EceuilCommun4);

        Point EceuilCommun5 = new Point(2000, -67);
        addGobeletsVerts(EceuilCommun5);

        Point EceuilCommun6 = new Point(1000, -67);
        addGobeletsVerts(EceuilCommun6);

        Point EceuilCommun7 = new Point(925, -67);
        addGobeletsVerts(EceuilCommun7);

        Point EceuilCommun8 = new Point(850, -67);
        addGobeletsRouges(EceuilCommun8);

        Point EceuilCommun9 = new Point(775, -67);
        addGobeletsRouges(EceuilCommun9);

        Point EceuilCommun10 = new Point(700, -67);
        addGobeletsRouges(EceuilCommun10);

    }

    /* ================================= Affichage des Enemmis sur la table ======================================= */

    private Ennemi ennemi = new Ennemi(0,0);

    public void setEnnemiPos(int posX, int posY){
        ennemi.setPosX(posX);
        ennemi.setPosY(posY);
    }

    public void setAffichageEnnemi(Boolean affichageEnnemi){
        ennemi.setAffichageEnnemi(affichageEnnemi);
    }

    public void drawEnnemi(Graphics g){
        if(ennemi.getAffichageEnnemi()){
            g.setColor(Color.MAGENTA);
            Point ennemiPixel = new Point(ennemi.getPosX(), ennemi.getPosY());
            ennemiPixel = transformLidarCoordonateToInterfaceCoordonate(ennemiPixel);
            g.fillOval(ennemiPixel.x,ennemiPixel.y, 25, 25);
        }
    }

    /* ============ Méthodes de transformation des distances entre la table et la fenêtre graphique ============= */

    private float transformTableDistanceToInterfaceDistance(float distanceOnTable) {
        return distanceOnTable * (TABLEGAME_PIXEL_WIDTH / (float) WIDTH_TABLEGAME);

    }

    private Point transformTableCoordonateToInterfaceCoordonate(Point point) {
        Point newPoint = new Point();
        newPoint.x  = (int) ((WIDTH_TABLEGAME - point.x) * (TABLEGAME_PIXEL_WIDTH / (float) WIDTH_TABLEGAME) + CoinHautGaucheX);
        newPoint.y = (int) ((-point.y) * ((TABLEGAME_PIXEL_HEIGHT) / (float) HEIGHT_TABLEGAME) + CoinHautGaucheY + TABLEGAME_PIXEL_HEIGHT);
        return newPoint;
    }

    //TODO: j'ai mis en  public( est-ce que je peux)
    public Point transformLidarCoordonateToInterfaceCoordonate(Point point){
        Point newPoint = new Point();
        newPoint.x = (int) ((point.x + WIDTH_TABLEGAME/2) * (TABLEGAME_PIXEL_WIDTH / (float) WIDTH_TABLEGAME) + CoinHautGaucheX);
        newPoint.y = (int) ((HEIGHT_TABLEGAME/2 - point.y) * ((TABLEGAME_PIXEL_HEIGHT) / (float) HEIGHT_TABLEGAME) + CoinHautGaucheY);
        return newPoint;
    }

    public BufferedImage rotate(BufferedImage img, double angle) {

        double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2f, (newHeight - h) / 2f);

        int x = w / 2;
        int y = h / 2;

        at.rotate(angle, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, this);
        g2d.setColor(Color.RED);
        g2d.drawRect(0, 0, newWidth - 1, newHeight - 1);
        g2d.dispose();

        return rotated;
    }


}