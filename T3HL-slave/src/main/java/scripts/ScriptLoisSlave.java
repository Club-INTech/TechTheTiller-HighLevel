package scripts;

import data.XYO;
import locomotion.UnableToMoveException;
import lowlevel.order.Order;
import orders.OrderWrapper;
import orders.order.ActuatorsOrders;
import pfg.config.Configurable;
import utils.HLInstance;
import utils.math.InternalVectCartesian;
import utils.math.Vec2;
import utils.math.VectCartesian;

public class ScriptLoisSlave extends Script {
    String posstart="Yellow";
    int posxinit;
    int posyinit;


    public Vec2 entryPosition(int version) {
        if (posstart=="Yellow") {
            posxinit=-1200;
            posyinit=820;

        }
        if (posstart== "Blue"){
            posxinit=1200;
            posyinit=820;
        }
        return new Vec2(posxinit,posyinit); }

    @Configurable
    private int robotRay;
    /**
     * Construit un script
     *
     * @param hl le container
     */
    protected ScriptLoisSlave(HLInstance hl) {
        super(hl);
    }

    @Override
    public void execute(int version) {


        try {
            if (posstart=="Blue") {
            //Portes ouvertes à 135 degrés
  /*          wrapper.perform(ActuatorsOrders.Valve7Off);*/
            //Recupere gobelet devant
            turnTowards(Math.PI);
            moveLengthwise(900,false);
            //Suck(1,1)

            //Attaque eceuil adversaire
            turnTowards(Math.PI+0.60);
            moveLengthwise(1100,false);
            turnTowards(Math.PI/2);
            moveLengthwise(-300,true);
            //Marteaudescend
            //Marteauremonte

            //Balayage zone de haut fond
            turnTowards(0);
            //Suck(2,1)
            //Suck(3,1)
            //Suck(4,1)
            moveLengthwise(1000,false);
            turnTowards(Math.PI/2);
            moveLengthwise(130,false);
            turnTowards(Math.PI);
            moveLengthwise(900,false);

            //Sortie zone haut fond
            turnTowards(-0.65+Math.PI/2 );
            moveLengthwise(1150,false);

            //EntreePort
            moveLengthwise(200,false);
            turnTowards(Math.PI/2);
            moveLengthwise(500,false);
            //Suck(1,0)
            //Suck(2,0)
            //Suck(3,0)
            //Suck(4,0)
                }
            else {
                if (posstart == "Yellow") {
                    //Recupere gobelet devant
                    turnTowards(0);
                    moveLengthwise(900,false);
                    //Suck(1,1)

                    //Attaque eceuil adversaire
                    turnTowards(-0.60);
                    moveLengthwise(1100,false);
                    turnTowards(Math.PI/2);
                    moveLengthwise(-300,true);
                    //Marteaudescend
                    //Marteauremonte

                    //Balayage zone de haut fond
                    turnTowards(Math.PI);
                    //Suck(2,1)
                    //Suck(3,1)
                    //Suck(4,1)
                    moveLengthwise(1000,false);
                    turnTowards(Math.PI/2);
                    moveLengthwise(130,false);
                    turnTowards(0);
                    moveLengthwise(900,false);

                    //Sortie zone haut fond
                    turnTowards(Math.PI/2+0.65);
                    moveLengthwise(1150,false);

                    //EntreePort
                    moveLengthwise(200,false);
                    turnTowards(Math.PI/2);
                    moveLengthwise(500,false);
                    //Suck(1,0)
                    //Suck(2,0)
                    //Suck(3,0)
                    //Suck(4,0)
                }
            }

/*
            table.removeAnyIntersectedMobileObstacle(XYO.getRobotInstance().getPosition(),robotRay);
*/
            //Portes ouvertes à 90 degrés
  /*          turnTowards(-Math.PI/2);
            moveLengthwise(260,false);
            /*wrapper.perform(ActuatorsOrders.LiftDown);
            wrapper.perform(ActuatorsOrders.Pump7On);
            wrapper.perform(ActuatorsOrders.LiftUp);*/
    /*        turnTowards(0);
            moveLengthwise(350,false);
            turnTowards(0.88);
            //Portes ouvertes à 135 degrés
            moveLengthwise(426,false);
            turnTowards(Math.PI/2);
            moveLengthwise(430,false);
            turnTowards(0.96);
            moveLengthwise(488,false);
            turnTowards(0);
            moveLengthwise(450,false);
            turnTowards(Math.PI/2);
            //Portes ouvertes à 180 degrés
            moveLengthwise(400,false);
            moveLengthwise(-90,false);
            /*wrapper.perform(ActuatorsOrders.LiftDown);
            wrapper.perform(ActuatorsOrders.Pump7On);
            wrapper.perform(ActuatorsOrders.Valve7On); */
      /*      moveLengthwise(-310,false);
            /*wrapper.perform(ActuatorsOrders.GateClose); */
    /*        turnTowards(Math.PI);
            moveLengthwise(1800-762,false);
            turnTowards(Math.PI/2);
            moveLengthwise(500,false);
            turnTowards(Math.PI);
            moveLengthwise(400,false);
            /*wrapper.perform(ActuatorsOrders.RightArmOut);
            wrapper.perform(ActuatorsOrders.RightArmIn);*/
      /*      moveLengthwise(-400,false);
            turnTowards(-Math.PI/2);
            moveLengthwise(400,false);
            moveLengthwise(200,false);
            turnTowards(0);
            moveLengthwise(1500-762,false);
            turnTowards(-Math.PI/2);
            //Lire girouette


*/
        } catch (UnableToMoveException e) {
            e.printStackTrace();
        }
    }
    //Position en entrée (0,0)=(1500,0) , axe x inversé, zone nord bleue

    @Override
    public void finalize(Exception e) {

    }
}
