package scripts;

import data.synchronization.SynchronizationWithBuddy;
import locomotion.UnableToMoveException;
import pfg.config.Configurable;
import utils.ConfigData;
import utils.HLInstance;
import utils.container.ContainerException;
import utils.math.Vec2;

public class Match extends Script {
    private final ScriptManagerMaster scriptManagerMaster;
    private SynchronizationWithBuddy syncBuddy;
    int posxinit;
    int posyinit;
    String posstart="Blue";


    public Match(HLInstance hl, ScriptManagerMaster scriptManagerMaster, SynchronizationWithBuddy syncBuddy) {
        super(hl);
        this.scriptManagerMaster = scriptManagerMaster;
        this.syncBuddy = syncBuddy;
    }

    // fonction pour introduire les coordonnées du robot initialement en fonction de la couleur de départ
    public void posInit(String posstart) {
        if (posstart=="Yellow") {
            posxinit=-1200;
            posyinit=650;

        }
        if (posstart== "Blue"){
            posxinit=1200;
            posyinit=650;
        }
    }
    @Override
//    position en entrée (0,0)=(1500,0), axe x inversé, zone nord blue
//    Vec2 changer en public et enlevé abstract
    public Vec2 entryPosition(int version) {
        posInit(posstart);
        return new Vec2(posxinit,posyinit);
    }



    @Override
    public void execute(int version) {

        try {
            if (posstart == "Blue") {

//          pour LL ecrire fonction pour descendre et fermer tous marteau en meme temps ou l'écrire en JAVA à voir

                turnTowards(5 * (Math.PI) / 4);
            /*suck(1,1)
              suck(3,1)*/
                moveLengthwise(400, false);
                turnTowards(Math.PI);
                /*suck(4,1)*/
                moveLengthwise(200, false);
                turnTowards(-Math.PI / 2);
                suck(2,1);
                moveLengthwise(300, false);
                // demi tour
                turnTowards(Math.PI / 2);
                moveLengthwise(-100, false);

//              1er écueil nord (EN)


                moveLengthwise(400, false);
                turnTowards(0);
                moveLengthwise(600, false);
                turnTowards(Math.PI / 2);
                moveLengthwise(665, false);
//            dépot ventouses port rouge
            /*suck(1,0)
              suck(1,0)*/

                moveLengthwise(-150, false);
                turnTowards(-Math.PI / 2);
                moveLengthwise(-150, false);
//            dépot rouge EN
                /*hammers()*/

                moveLengthwise(600, false);
//            wait(100);
//            dépot vert EN
                /*hammers()*/

                moveLengthwise(150, false);
                turnTowards(Math.PI / 2);
                moveLengthwise(150, false);
//            dépot ventouses vert
                /*hammers()*/

//            phare
                moveLengthwise(-530, false);
                moveLengthwise(150, false);
                turnTowards(3 * Math.PI / 4);
                moveLengthwise(650, false);
                turnTowards(Math.PI / 2);
                moveLengthwise(500, false);
                turnTowards(Math.PI / 4);
             /*suck(2,1)
              suck(3,1)
              suck(4,1)*/

                moveLengthwise(450, false);
                turnTowards(Math.PI / 2);
                moveLengthwise(260, false);
                turnTowards(Math.PI);
                moveLengthwise(-300, false);
//            écueil sud (ES)

                moveLengthwise(100, false);
                turnTowards(Math.PI / 2);
                moveLengthwise(-350, false);
//            dépot rouge ES
                moveLengthwise(100, false);
                turnTowards(3 * Math.PI / 2);
                moveLengthwise(160, false);
//            dépot ventouses rouge
            } else {
                if (posstart == "Yellow") {

                    turnTowards(-Math.PI / 4);
            /*suck(1,1)
              suck(3,1)*/
                    moveLengthwise(400, false);
                    turnTowards(0);
                    /*suck(4,1)*/
                    moveLengthwise(200, false);
                    turnTowards(-Math.PI / 2);
                    /*suck(2,1)*/
                    moveLengthwise(300, false);
                    // demi tour
                    turnTowards(Math.PI / 2);
                    moveLengthwise(-100, false);

//              1er écueil nord (EN)


                    moveLengthwise(400, false);
                    turnTowards(Math.PI);
                    moveLengthwise(600, false);
                    turnTowards(Math.PI / 2);
                    moveLengthwise(665, false);
//            dépot ventouses port rouge
            /*suck(1,0)
              suck(1,0)*/

                    moveLengthwise(-150, false);
                    turnTowards(-Math.PI / 2);
                    moveLengthwise(-150, false);
//            dépot rouge EN
                    /*hammers()*/

                    moveLengthwise(600, false);
//            wait(100);
//            dépot vert EN
                    /*hammers()*/

                    moveLengthwise(150, false);
                    turnTowards(Math.PI / 2);
                    moveLengthwise(150, false);
//            dépot ventouses vert
                    /*hammers()*/

//            phare
                    moveLengthwise(-530, false);
                    moveLengthwise(150, false);
                    turnTowards(Math.PI / 4);
                    moveLengthwise(650, false);
                    turnTowards(Math.PI / 2);
                    moveLengthwise(500, false);
                    turnTowards(3*Math.PI / 4);
             /*suck(2,1)
              suck(3,1)
              suck(4,1)*/

                    moveLengthwise(450, false);
                    turnTowards(Math.PI / 2);
                    moveLengthwise(260, false);
                    turnTowards(0);
                    moveLengthwise(-300, false);
//            écueil sud (ES)

                    moveLengthwise(100, false);
                    turnTowards(Math.PI / 2);
                    moveLengthwise(-350, false);
//            dépot rouge ES
                    moveLengthwise(100, false);
                    turnTowards(3 * Math.PI / 2);
                    moveLengthwise(160, false);
//            dépot ventouses rouge
                }
            }


        } catch (UnableToMoveException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void finalize(Exception e) {

    }
}
