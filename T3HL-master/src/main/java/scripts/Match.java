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
            moveLengthwise(1000, false);
            turnTowards(1.6);
            moveLengthwise(1000, false);



        } catch (UnableToMoveException e) {
            e.printStackTrace();

        }


        // TODO: A vous de jouer les 1As!
    }

    @Override
    public Vec2 entryPosition(int version) {
        //TODO: remplacer par la position d'entrée d'un script
        return new Vec2() {
            @Override
            public int dot(Vec2 vecteur) {
                return super.dot(vecteur);
            }
        };
    }

    @Override
    public void finalize(Exception e) {

    }
}
