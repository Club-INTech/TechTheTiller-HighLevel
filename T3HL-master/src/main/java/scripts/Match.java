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

    public Match(HLInstance hl, ScriptManagerMaster scriptManagerMaster, SynchronizationWithBuddy syncBuddy) {
        super(hl);
        this.scriptManagerMaster = scriptManagerMaster;
        this.syncBuddy = syncBuddy;
    }


    @Override
    public void execute(int version) {
        //TODO: Code lançant les différents scripts du principal


        try {

//          pour LL ecrire fonction pour descendre et fermer tous marteau en meme temps ou l'écrire en JAVA à voir


            turnTowards(5*(Math.PI)/4);
            /*suck(1,True)
              suck(3,True)*/
            moveLengthwise(400, false);
            turnTowards(Math.PI);
            /*suck(4,True)*/
            moveLengthwise(200, false);
            turnTowards(-Math.PI/2);
            /*suck(2,True)*/
            moveLengthwise(300, false);
            // demi tour
            turnTowards(Math.PI/2);
            moveLengthwise(-100, false);

//              1er écueil nord (EN)


            moveLengthwise(400, false);
            turnTowards(0);
            moveLengthwise(600, false);
            turnTowards(Math.PI/2);
            moveLengthwise(665, false);
//            dépot ventouses port rouge
            /*suck(1,False)
              suck(1,False)*/

            moveLengthwise(-150, false);
            turnTowards(-Math.PI/2);
            moveLengthwise(-150, false);
//            dépot rouge EN
            /*lower_hammer()*/

            moveLengthwise(600, false);
            wait(100);
//            dépot vert EN
            /*lower_hammer()*/

            moveLengthwise(150, false);
            turnTowards(Math.PI / 2);
            moveLengthwise(150, false);
//            dépot ventouses vert
            /*lower_hammer()*/

//            phare
            moveLengthwise(-530, false);
            moveLengthwise(150, false);
            turnTowards(3*Math.PI/4);
            moveLengthwise(650, false);
            turnTowards(Math.PI/2);
            moveLengthwise(500, false);
            turnTowards(Math.PI /4);
             /*suck(2,True)
              suck(3,True)
              suck(4,True)*/

            moveLengthwise(450, false);
            turnTowards(Math.PI /2);
            moveLengthwise(260, false);
            turnTowards(Math.PI);
            moveLengthwise(-300, false);
//            écueil sud (ES)

            moveLengthwise(100, false);
            turnTowards(Math.PI/2);
            moveLengthwise(-350, false);
//            dépot rouge ES
            moveLengthwise(100, false);
            turnTowards(3*Math.PI/2);
            moveLengthwise(160, false);
//            dépot ventouses rouge



        } catch (UnableToMoveException | InterruptedException e) {
            e.printStackTrace();

        }

    }

    @Override
//    position en entrée (0,0)=(1500,0), axe x inversé, zone nord blue
//    Vec2 changer en public et enlevé abstract
    public Vec2 entryPosition(int version) {

        return new Vec2(1200, 650);
    }

    @Override
    public void finalize(Exception e) {

    }
}
