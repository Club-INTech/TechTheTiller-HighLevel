package scripts;

import data.synchronization.SynchronizationWithBuddy;
import locomotion.UnableToMoveException;
import pfg.config.Configurable;
import utils.ConfigData;
import utils.HLInstance;
import utils.math.Vec2;
import static connection.JavaPython.executeBashCommand;
import static connection.JavaPython.readtextfile;

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
    public void finalize(Exception e) {

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
                suck(2, 1);
                moveLengthwise(300, false);

//                Analyse de l'ordre des gobelets, il faut placer la distance exacte => à tester

//                readColors("/usr/bin/python /media/salembien/Elements/PROJET X/ColorVision/Center.py");

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
                ecueilTri_master_Nord();
                moveLengthwise(600, false);


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
                    turnTowards(3 * Math.PI / 4);
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
//            dépot ventouses rouge à finir + maches a air à faire



//                Test pour la zone de Haut Fond
//                Placer le robot devant les gobelets à analyser après les avoir recupéré dans la zone de haut fond
                    readColors("/usr/bin/python /media/salembien/Elements/PROJET X/ColorVision/HautFond.py");


                }
            }


        } catch (UnableToMoveException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    Fonction pour lire l'ordre des gobelets haut fond
    public void readColors(String pathEx) throws Exception {
        executeBashCommand(pathEx);
        Thread.sleep(3000);
    }


//        hfTri(hautfond);

    /*Lien pour HF:
     *Exec:"/usr/bin/python /media/salembien/Elements/PROJET X/ColorVision/HautFond.py"
     * "/media/salembien/Elements/PROJET X/ColorVision/hautfond.txt"
    *
    * Liens pour ecueil :
     * Exec: "/usr/bin/python /media/salembien/Elements/PROJET X/ColorVision/Center.py"
     * Txt:"/media/salembien/Elements/PROJET X/ColorVision/ecueilNord.txt"

    */



    //    Fonction permettant de considérer 3 cas
    //    après récuperation des gobelets dans l'écueil Nord ami
    public void ecueilTri_master_Nord() throws Exception {

        String res = readtextfile("/media/salembien/Elements/PROJET X/ColorVision/ecueilNord.txt");
        if (posstart.equals("Yellow")){
            switch(res){
                case "VVRRR":

//                  hammer(1,1,0,0,0);
                    moveLengthwise(600, false);
//                  hammer(0,0,1,1,1);

                case "VRVRR":
//                  hammer(1,0,1,0,0);
                    moveLengthwise(600, false);
//                  hammer(0,1,0,1,1);

                case "VRRVR":
//                  hammer(1,0,0,1,0);
                    moveLengthwise(600, false);
//                  hammer(0,1,1,0,1);

            }
        }else{
            switch (res){
                case "VVVRR":
//                  hammer(0,0,0,1,1);
                    moveLengthwise(600, false);
//                  hammer(1,1,1,0,0);


                case "VVRVR":
//                  hammer(0,0,1,0,1);
                    moveLengthwise(600, false);
//                  hammer(1,1,0,1,0);

                case "VRVVR":

//                  hammer(0,1,0,0,1);
                    moveLengthwise(600, false);
//                  hammer(1,0,1,1,0);

            }
        }
    }


    //    Fonction permettant de considérer les 3 cas après récuperation
    //    des gobelets dans l'écueil adverse avant la zone de haut fond
    public void ecueilTri_slave() throws Exception{

        String ecueilS = readtextfile("/media/salembien/Elements/PROJET X/ColorVision/hautfond.txt");
        System.out.println("Ecueil Slave : "  + ecueilS);


        if (posstart.equals("Yellow")){
            switch(ecueilS){
                case "VVRRR":

//                voir longueur bras etendu pour depose des marteaux
                    moveLengthwise(-300,false);
//                  hammers(1,1,0,0,0);
                    moveLengthwise(200,false);
                    turnTowards(0);
                    moveLengthwise(150,false);
//                    hfTri();
                    moveLengthwise(150,false);
                    turnTowards(Math.PI);
                    moveLengthwise(-150,false);
//                  hammers(0,0,1,1,1);

//                    Lois doit continuer son script après ca
//                    le robot a tout deposé mais pas pousser il et reculé
//                    de la zone rocheuse mais pas tourné

                case "VRVRR":
                    moveLengthwise(-300,false);
//                  hammers(1,0,1,0,0);
                    moveLengthwise(200,false);
                    turnTowards(0);
                    moveLengthwise(150,false);
//                    hfTri();
                    moveLengthwise(150,false);
                    turnTowards(Math.PI);
                    moveLengthwise(-150,false);
//                  hammers(0,1,0,1,1);

                case "VRRVR":
                    moveLengthwise(-300,false);
//                  hammers(1,0,0,1,0);
                    moveLengthwise(200,false);
                    turnTowards(0);
                    moveLengthwise(150,false);
//                    hfTri();
                    moveLengthwise(150,false);
                    turnTowards(Math.PI);
                    moveLengthwise(-150,false);
//                  hammers(0,1,1,0,1);

            }
        }else{
            switch (ecueilS){
                case "VVVRR":

                    moveLengthwise(-300,false);
//                  hammers(0,0,0,1,1);
                    moveLengthwise(200,false);
                    turnTowards(Math.PI);
                    moveLengthwise(150,false);
//                    hfTri();
                    moveLengthwise(150,false);
                    turnTowards(Math.PI);
                    moveLengthwise(-150,false);
//                  hammers(1,1,1,0,0);


                case "VVRVR":

                    moveLengthwise(-300,false);
//                  hammers(0,0,1,0,1);
                    moveLengthwise(200,false);
                    turnTowards(Math.PI);
                    moveLengthwise(150,false);
//                    hfTri();
                    moveLengthwise(150,false);
                    turnTowards(Math.PI);
                    moveLengthwise(-150,false);
//                  hammers(1,1,0,1,0);


                case "VRVVR":

                    moveLengthwise(-300,false);
//                  hammers(0,1,0,0,1);
                    moveLengthwise(200,false);
                    turnTowards(Math.PI);
                    moveLengthwise(150,false);
//                    hfTri();
                    moveLengthwise(150,false);
                    turnTowards(Math.PI);
                    moveLengthwise(-150,false);
//                  hammers(1,0,1,1,0);


            }
        }
    }


//    Fonction permettant de considérer tous les cas possibles après récuperation des gobelets dans la zone de haut fond
//    Suppose que robot positionné en x=960 y=1650 dans zone rocheuse BLEUE avec ventouse vers zone dépot

    public void hfTri() throws Exception {

        String pyt_hf=readtextfile("/media/salembien/Elements/PROJET X/ColorVision/hautfond.txt");
        if (posstart.equals("Yellow")) {
            switch (pyt_hf){

//          Cas récup 3 gobelets
                case "RRR":
                    moveLengthwise(300,false);
//              suckall(0)

                case "RRV":
                case "RVR":
                case "VRR":
                case "RVV":
                case "VVR":
                case "VRV":
                case "VVV":


//          Cas récup 4 gobelets
                case "RRRV":
                    suck(4,0);
                    moveLengthwise(-250,false);
                    suck(1,0);
                    suck(2,0);
                    suck(3,0);

                case "RRVR":
                    suck(3,0);
                    moveLengthwise(-250,false);
                    suck(1,0);
                    suck(2,0);
                    suck(4,0);

                case "RVRR":
                    suck(2,0);
                    moveLengthwise(-250,false);
                    suck(1,0);
                    suck(4,0);
                    suck(3,0);

                case "VRRR":
                    suck(1,0);
                    moveLengthwise(-250,false);
                    suck(4,0);
                    suck(2,0);
                    suck(3,0);



                case "RRVV":
                    suck(4,0);
                    suck(3,0);
                    moveLengthwise(-250,false);
                    suck(1,0);
                    suck(2,0);

                case "RVRV":
                    suck(4,0);
                    suck(2,0);
                    moveLengthwise(-250,false);
                    suck(1,0);
                    suck(3,0);

                case "RVVR":
                    suck(2,0);
                    suck(3,0);
                    moveLengthwise(-250,false);
                    suck(1,0);
                    suck(4,0);

                case "VRVR":
                    suck(1,0);
                    suck(3,0);
                    moveLengthwise(-250,false);
                    suck(4,0);
                    suck(2,0);

                case "VVRR":
                    suck(1,0);
                    suck(2,0);
                    moveLengthwise(-250,false);
                    suck(3,0);
                    suck(4,0);

                case "VRRV":
                    suck(4,0);
                    suck(1,0);
                    moveLengthwise(-250,false);
                    suck(3,0);
                    suck(2,0);



                case "VVVR":
                    suck(3,0);
                    suck(1,0);
                    suck(2,0);
                    moveLengthwise(-250,false);
                    suck(4,0);

                case "VVRV":
                    suck(4,0);
                    suck(1,0);
                    suck(2,0);
                    moveLengthwise(-250,false);
                    suck(3,0);

                case "VRVV":
                    suck(3,0);
                    suck(1,0);
                    suck(4,0);
                    moveLengthwise(-250,false);
                    suck(2,0);

                case "RVVV":
                    suck(3,0);
                    suck(4,0);
                    suck(2,0);
                    moveLengthwise(-250,false);
                    suck(1,0);



//           Cas récup 1 gobelet
                case "R":

                    moveLengthwise(-250,false);
//                  suckall(0);

                case "V":
//                    suckall(0);
                    moveLengthwise(-250,false);

//          Cas récup 2 gobelets
                case "RR":
                case "RV":
                case "VR":
                case "VV":


//          Cas récup 0 gobelets


            }

        }


//        Blue à faire !!!

        if (posstart.equals("Blue")) {
            switch (pyt_hf){

//          Cas récup 3 gobelets
                case "RRR":
                    moveLengthwise(300,false);
//              suckall(0)

                case "RRV":
                case "RVR":
                case "VRR":
                case "RVV":
                case "VVR":
                case "VRV":
                case "VVV":


//          Cas récup 4 gobelets
                case "RRRV":
                    suck(1,0);
                    suck(3,0);
                    suck(2,0);
                    moveLengthwise(-250,false);
                    suck(4,0);

                case "RRVR":
                    suck(1,0);
                    suck(4,0);
                    suck(2,0);
                    moveLengthwise(-250,false);
                    suck(3,0);

                case "RVRR":
                    suck(1,0);
                    suck(3,0);
                    suck(4,0);
                    moveLengthwise(-250,false);
                    suck(2,0);

                case "VRRR":
                    suck(2,0);
                    suck(3,0);
                    suck(4,0);
                    moveLengthwise(-250,false);
                    suck(1,0);


                case "RRVV":
                    suck(1,0);
                    suck(2,0);
                    moveLengthwise(-250,false);
                    suck(3,0);
                    suck(4,0);
                case "RVRV":
                    suck(1,0);
                    suck(3,0);
                    moveLengthwise(-250,false);
                    suck(2,0);
                    suck(4,0);
                case "RVVR":
                    suck(1,0);
                    suck(4,0);
                    moveLengthwise(-250,false);
                    suck(3,0);
                    suck(2,0);
                case "VRVR":
                    suck(4,0);
                    suck(2,0);
                    moveLengthwise(-250,false);
                    suck(3,0);
                    suck(1,0);
                case "VVRR":
                    suck(3,0);
                    suck(4,0);
                    moveLengthwise(-250,false);
                    suck(1,0);
                    suck(2,0);
                case "VRRV":
                    suck(3,0);
                    suck(2,0);
                    moveLengthwise(-250,false);
                    suck(1,0);
                    suck(4,0);


                case "VVVR":
                    suck(4,0);
                    moveLengthwise(-250,false);
                    suck(2,0);
                    suck(3,0);
                    suck(1,0);
                case "VVRV":
                    suck(3,0);
                    moveLengthwise(-250,false);
                    suck(2,0);
                    suck(4,0);
                    suck(1,0);
                case "VRVV":
                    suck(2,0);
                    moveLengthwise(-250,false);
                    suck(4,0);
                    suck(3,0);
                    suck(1,0);
                case "RVVV":
                    suck(1,0);
                    moveLengthwise(-250,false);
                    suck(2,0);
                    suck(3,0);
                    suck(4,0);


//           Cas récup 1 gobelet
                case "R":
//                  suckall(0);
                    moveLengthwise(-250,false);

                case "V":
                    moveLengthwise(-250,false);
//                  suckall(0);

//          Cas récup 2 gobelets
                case "RR":
                case "RV":
                case "VR":
                case "VV":


//          Cas récup 0 gobelets



            }

        }


    }

}

