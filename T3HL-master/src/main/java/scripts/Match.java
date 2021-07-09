package scripts;

import data.synchronization.SynchronizationWithBuddy;
import locomotion.UnableToMoveException;
import pfg.config.Configurable;
import utils.ConfigData;
import utils.HLInstance;
import utils.communication.CommunicationException;
import utils.communication.SocketClientInterface;
import utils.math.Vec2;

import java.util.Optional;

import static connection.JavaPython.executeBashCommand;
import static connection.JavaPython.readtextfile;

public class Match extends Script {
    private final ScriptManagerMaster scriptManagerMaster;
    private SynchronizationWithBuddy syncBuddy;
    int posxinit;
    int posyinit;

    String posstart = "Blue";
    String proto = "no";
    String homolog = "yes";


    public Match(HLInstance hl, ScriptManagerMaster scriptManagerMaster, SynchronizationWithBuddy syncBuddy) {
        super(hl);
        this.scriptManagerMaster = scriptManagerMaster;
        this.syncBuddy = syncBuddy;
    }

    // fonction pour introduire les coordonnées du robot initialement en fonction de la couleur de départ
    public void posInit(String posstart) {
        if (posstart == "Yellow") {
            posxinit = -1250;
            posyinit = 650;

        }
        if (posstart == "Blue") {
            posxinit = 1250;
            posyinit = 650;
        }
    }

    @Override
//    position en entrée (0,0)=(1500,0), axe x inversé, zone nord blue
//    Vec2 changer en public et enlevé abstract
    public Vec2 entryPosition(int version) {
        posInit(posstart);
        return new Vec2(posxinit, posyinit);
    }

    @Override
    public void finalize(Exception e) {

    }


    @Override
    public void execute(int version) {

        try {
            if (proto.equals("yes")) {protomatch();}
            if (homolog.equals("yes")) {homologationphare();}
            else {
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

//
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
        }

        } catch (UnableToMoveException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void homologationphare() throws UnableToMoveException {
        /*if (posstart.equals("Blue")) {
            turnTowards(4 * Math.PI / 3);
            suck(2, 1);
            moveLengthwise(300, false);
            turnTowards(-Math.PI/2);
            moveLengthwise(300,false);
            turnTowards(Math.PI/2);
            moveLengthwise(-100, false);
            hammers(1);
            hammers(0);
            turnTowards(Math.PI/2-0.1);
            moveLengthwise(500,false);
            suck(2,0);
        } else {
          */  //HOMOLOGATION A FAIRE COTE JAUNE !!!!!!!!!!!!!!!!!!!!!!!!!!!
            turnTowards(4 * Math.PI / 3);
            moveLengthwise(400,false);
            moveLengthwise(-400,false);
            turnTowards(-Math.PI/2);
            moveLengthwise(400,false);
            turnTowards(Math.PI/2);
            moveLengthwise(-100,false);
            hammers(0);
        //}


    }
    //    Fonction pour lire l'ordre des
    //    gobelets haut fond
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

        String res = readtextfile("/usr/bin/python /home/brunlois/PycharmProjects/ColorVision/Center.py");
        if (posstart.equals("Yellow")) {
            switch (res) {
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
        } else {
            switch (res) {
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

    public void ecueilTri_slave() throws Exception {

        String ecueilS = readtextfile("/media/salembien/Elements/PROJET X/ColorVision/eceuil.txt");
        System.out.println("Ecueil Slave : " + ecueilS);


        if (posstart.equals("Yellow")) {
            switch (ecueilS) {
                case "VVRRR":

//                voir longueur bras etendu pour depose des marteaux
                    moveLengthwise(-300, false);
//                  hammers(1,1,0,0,0);
                    moveLengthwise(200, false);
                    turnTowards(0);
                    moveLengthwise(150, false);
//                    hfTri();
                    moveLengthwise(150, false);
                    turnTowards(Math.PI);
                    moveLengthwise(-150, false);
//                  hammers(0,0,1,1,1);

//                    Lois doit continuer son script après ca
//                    le robot a tout deposé mais pas pousser il et reculé
//                    de la zone rocheuse mais pas tourné

                case "VRVRR":
                    moveLengthwise(-300, false);
//                  hammers(1,0,1,0,0);
                    moveLengthwise(200, false);
                    turnTowards(0);
                    moveLengthwise(150, false);
//                    hfTri();
                    moveLengthwise(150, false);
                    turnTowards(Math.PI);
                    moveLengthwise(-150, false);
//                  hammers(0,1,0,1,1);

                case "VRRVR":
                    moveLengthwise(-300, false);
//                  hammers(1,0,0,1,0);
                    moveLengthwise(200, false);
                    turnTowards(0);
                    moveLengthwise(150, false);
//                    hfTri();
                    moveLengthwise(150, false);
                    turnTowards(Math.PI);
                    moveLengthwise(-150, false);
//                  hammers(0,1,1,0,1);

            }
        } else {
            switch (ecueilS) {
                case "VVVRR":

                    moveLengthwise(-300, false);
//                  hammers(0,0,0,1,1);
                    moveLengthwise(200, false);
                    turnTowards(Math.PI);
                    moveLengthwise(150, false);
//                    hfTri();
                    moveLengthwise(150, false);
                    turnTowards(Math.PI);
                    moveLengthwise(-150, false);
//                  hammers(1,1,1,0,0);


                case "VVRVR":

                    moveLengthwise(-300, false);
//                  hammers(0,0,1,0,1);
                    moveLengthwise(200, false);
                    turnTowards(Math.PI);
                    moveLengthwise(150, false);
//                    hfTri();
                    moveLengthwise(150, false);
                    turnTowards(Math.PI);
                    moveLengthwise(-150, false);
//                  hammers(1,1,0,1,0);


                case "VRVVR":

                    moveLengthwise(-300, false);
//                  hammers(0,1,0,0,1);
                    moveLengthwise(200, false);
                    turnTowards(Math.PI);
                    moveLengthwise(150, false);
//                    hfTri();
                    moveLengthwise(150, false);
                    turnTowards(Math.PI);
                    moveLengthwise(-150, false);
//                  hammers(1,0,1,1,0);


            }
        }
    }


//    Fonction permettant de considérer tous les cas possibles après récuperation des gobelets dans la zone de haut fond
//    Suppose que robot positionné (en x=960 y=1655 pour zone bleue) dans zone rocheuse avec ventouse vers la zone de dépot

    public void hfTri() throws Exception {


        moveLengthwise(300, false);

        String pyt_hf = readtextfile("/media/salembien/Elements/PROJET X/ColorVision/hautfond.txt");
        if (posstart.equals("Yellow")) {
            switch (pyt_hf) {

//          Cas récup 3 gobelets ou 4 gobelets avec nr=1 ou nv=1 ou cas avec 2 gobelets et nv=nr=1
                case "RRR":
                    moveLengthwise(-300, false);
//              suckall(0)

                case "---V":
                    suck(4, 0);
                    moveLengthwise(-300, false);
                    suckall(0);
                case "--V-":
                    suck(4, 0);
                    moveLengthwise(-300, false);
                    suckall(0);
                case "-V--":
                    suck(4, 0);
                    moveLengthwise(-300, false);
                    suckall(0);
                case "V---":
                    suck(4, 0);
                    moveLengthwise(-300, false);
                    suckall(0);
                case "---R":
                    suck(4, 0);
                    moveLengthwise(-300, false);
                    suckall(0);
                case "--R-":
                    suck(4, 0);
                    moveLengthwise(-300, false);
                    suckall(0);
                case "-R--":
                    suck(4, 0);
                    moveLengthwise(-300, false);
                    suckall(0);
                case "R---":
                    suck(4, 0);
                    moveLengthwise(-300, false);
                    suckall(0);
                case "VVV":
//                    suckall(0);
                    moveLengthwise(300, false);


//          Cas récup 4 gobelets avec nr=nv=2


                case "RRVV":
                    suck(4, 0);
                    suck(3, 0);
                    moveLengthwise(-300, false);
                    suck(1, 0);
                    suck(2, 0);

                case "RVRV":
                    suck(4, 0);
                    suck(2, 0);
                    moveLengthwise(-300, false);
                    suck(1, 0);
                    suck(3, 0);

                case "RVVR":
                    suck(2, 0);
                    suck(3, 0);
                    moveLengthwise(-300, false);
                    suck(1, 0);
                    suck(4, 0);

                case "VRVR":
                    suck(1, 0);
                    suck(3, 0);
                    moveLengthwise(-300, false);
                    suck(4, 0);
                    suck(2, 0);

                case "VVRR":
                    suck(1, 0);
                    suck(2, 0);
                    moveLengthwise(-300, false);
                    suck(3, 0);
                    suck(4, 0);

                case "VRRV":
                    suck(4, 0);
                    suck(1, 0);
                    moveLengthwise(-300, false);
                    suck(3, 0);
                    suck(2, 0);


//           Cas récup 1 gobelet
                case "R":
                    moveLengthwise(-300, false);
//                  suckall(0);

                case "V":
//                    suckall(0);
                    moveLengthwise(-300, false);

//          Cas récup 2 gobelets
                case "RR":
                    moveLengthwise(-300, false);
//                  suckall(0);

                case "VV":
//                    suckall(0);
                    moveLengthwise(-300, false);


//          Cas récup 0 gobelets
                default:
                    moveLengthwise(-300, false);


            }

        }


        if (posstart.equals("Blue")) {
            switch (pyt_hf) {

//          Cas récup 3 gobelets ou 4 gobelets avec nr=1 ou nv=1 ou cas avec 2 gobelets et nv=nr=1
                case "RRR":
//              suckall(0)
                    moveLengthwise(-300, false);


                case "---V":
                    suck(1, 0);
                    suck(2, 0);
                    suck(3, 0);
                    moveLengthwise(-300, false);
                    suck(4, 0);
                case "--V-":
                    suck(1, 0);
                    suck(2, 0);
                    suck(4, 0);
                    moveLengthwise(-300, false);
                    suck(3, 0);
                case "-V--":
                    suck(1, 0);
                    suck(4, 0);
                    suck(3, 0);
                    moveLengthwise(-300, false);
                    suck(2, 0);
                case "V---":
                    suck(4, 0);
                    suck(2, 0);
                    suck(3, 0);
                    moveLengthwise(-300, false);
                    suck(1, 0);
                case "---R":
                    suck(1, 0);
                    moveLengthwise(-300, false);
                    suckall(0);
                case "--R-":
                    suck(2, 0);
                    moveLengthwise(-300, false);
                    suckall(0);
                case "-R--":
                    suck(3, 0);
                    moveLengthwise(-300, false);
                    suckall(0);
                case "R---":
                    suck(4, 0);
                    moveLengthwise(-300, false);
                    suckall(0);
                case "VVV":
                    moveLengthwise(300, false);
//                    suckall(0);


//          Cas récup 4 gobelets avec nr=nv=2


                case "RRVV":
                    suck(1, 0);
                    suck(2, 0);
                    moveLengthwise(-300, false);
                    suck(4, 0);
                    suck(3, 0);

                case "RVRV":
                    suck(1, 0);
                    suck(3, 0);
                    moveLengthwise(-300, false);

                    suck(4, 0);
                    suck(2, 0);
                case "RVVR":
                    suck(1, 0);
                    suck(4, 0);
                    moveLengthwise(-300, false);

                    suck(2, 0);
                    suck(3, 0);
                case "VRVR":
                    suck(4, 0);
                    suck(2, 0);
                    moveLengthwise(-300, false);

                    suck(1, 0);
                    suck(3, 0);
                case "VVRR":
                    suck(3, 0);
                    suck(4, 0);
                    moveLengthwise(-300, false);

                    suck(1, 0);
                    suck(2, 0);
                case "VRRV":
                    suck(3, 0);
                    suck(2, 0);
                    moveLengthwise(-300, false);

                    suck(4, 0);
                    suck(1, 0);


//           Cas récup 1 gobelet
                case "R":
//                  suckall(0);
                    moveLengthwise(-300, false);


                case "V":
                    moveLengthwise(-300, false);
//                    suckall(0);

//          Cas récup 2 gobelets
                case "RR":
//                  suckall(0);
                    moveLengthwise(-300, false);


                case "VV":
                    moveLengthwise(-300, false);
//                    suckall(0);


//          Cas récup 0 gobelets
                default:
                    moveLengthwise(-300, false);


            }

        }


    }


    public void protomatch() throws UnableToMoveException, CommunicationException {
        if (posstart.equals("Blue")) {
            turnTowards(4 * Math.PI / 3);
            suck(0, 1);
            suck(4, 1);
            moveLengthwise(300, false);
            //Methode lolo
            /*turnTowards(0);
            moveLengthwise(200,false);
            turnTowards(-Math.PI/2);
            moveLengthwise(300,false);
            turnTowards(Math.PI/2);
            moveLengthwise(-100,true);
            hammers(1);
*/
            //méthode Carole
            turnTowards(-Math.PI / 2);
            moveLengthwise(300, false);
            turnTowards(Math.PI / 2);
            moveLengthwise(-100, true);
            hammers(1);
            hammers(0);
            turnTowards(Math.PI);
            suckall(1);
            moveLengthwise(550, false);
            turnTowards(Math.PI / 2);
            moveLengthwise(-10, false);
            hammers(1);
            hammers(0);
            moveLengthwise(850, false);
            turnTowards(0);
            moveLengthwise(800, false);
            //ecueilTri_master_Nord();
            //reader
            turnTowards(-Math.PI / 2);
            moveLengthwise(500, false);
            suck(0, 0);
            turnTowards(Math.PI / 2);
            moveLengthwise(550, false);
            suckall(0);
            turnTowards(-Math.PI / 2);
            moveLengthwise(100, false);
            hammers(0);
            SocketClientInterface sender = new SocketClientInterface("192.168.1.0", 3000, false);
            Optional<String> aruco = sender.read();
            if (aruco.equals("North")) {
                turnTowards(Math.PI);
                moveLengthwise(500, false);
                turnTowards(-Math.PI / 2);
                moveLengthwise(500, false);
                moveLengthwise(300, false);
                turnTowards(0);
                moveLengthwise(650, false);
            } else {
                turnTowards(Math.PI);
                moveLengthwise(500, false);
                turnTowards(Math.PI / 2);
                moveLengthwise(350, false);
                turnTowards(0);
                moveLengthwise(650, false);
            }
        } else {
            turnTowards(- Math.PI / 3);
            suck(0, 1);
            suck(4, 1);
            moveLengthwise(300, false);
            //Methode lolo
            /*turnTowards(0);
            moveLengthwise(200,false);
            turnTowards(-Math.PI/2);
            moveLengthwise(300,false);
            turnTowards(Math.PI/2);
            moveLengthwise(-100,true);
            hammers(1);
*/
            //méthode Carole
            turnTowards(-Math.PI / 2);
            moveLengthwise(300, false);
            turnTowards(Math.PI / 2);
            moveLengthwise(-50, true);
            hammers(1);
            hammers(0);
            turnTowards(0);
            suckall(1);
            moveLengthwise(550, false);
            turnTowards(Math.PI / 2);
            moveLengthwise(-10, false);
            hammers(1);
            hammers(0);
            moveLengthwise(850, false);
            turnTowards(Math.PI);
            moveLengthwise(800, false);
            turnTowards(-Math.PI / 2);
            moveLengthwise(500, false);
            suck(0, 0);
            turnTowards(Math.PI / 2);
            moveLengthwise(500, false);
            suckall(0);
            turnTowards(-Math.PI / 2);
            moveLengthwise(100, false);
            hammers(0);
            SocketClientInterface sender = new SocketClientInterface("192.168.1.0", 3000, false);
            Optional<String> aruco = sender.read();
            if (aruco.equals("North")) {
                turnTowards(0);
                moveLengthwise(500, false);
                turnTowards(-Math.PI / 2);
                moveLengthwise(500, false);
                moveLengthwise(300, false);
                turnTowards(Math.PI);
                moveLengthwise(650, false);
            } else {
                turnTowards(0);
                moveLengthwise(500, false);
                turnTowards(Math.PI / 2);
                moveLengthwise(250, false);
                turnTowards(Math.PI);
                moveLengthwise(650, false);
            }

        }

    }
}

















