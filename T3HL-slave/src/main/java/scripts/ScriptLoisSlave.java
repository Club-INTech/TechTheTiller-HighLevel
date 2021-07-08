package scripts;

import data.XYO;
import locomotion.UnableToMoveException;
import lowlevel.order.Order;
import orders.OrderWrapper;
import orders.order.ActuatorsOrders;
import pfg.config.Configurable;
import utils.HLInstance;
import utils.communication.SocketClientInterface;
import utils.math.InternalVectCartesian;
import utils.math.Vec2;
import utils.math.VectCartesian;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.*;
import utils.communication.SocketServerInterface;
public class ScriptLoisSlave extends Script {
    String posstart="Yellow";
    String aruco="North";
    int dt = 500;
    int posxinit;
    int posyinit;
    String proto = "no";
    String homoflag = "no";
    String homomov = "yes";

    public Vec2 entryPosition(int version) {
        if (posstart=="Yellow") {
            posxinit=-1400;
            posyinit=820;

        }
        if (posstart== "Blue"){
            posxinit=1400;
            posyinit=820;
        }
        if (homomov.equals("yes")){
            posxinit=-1100;
            posyinit=1050;
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
    /**
     * Execute a bash command. We can handle complex bash commands including
     * multiple executions (; | && ||), quotes, expansions ($), escapes (\), e.g.:
     *     "cd /abc/def; mv ghi 'older ghi '$(whoami)"
     * @param command
     * @return true if bash got started, but your command may have failed.
     */
    public static boolean executeBashCommand(String command) {
        boolean success = false;
        System.out.println("Executing BASH command:\n   " + command);
        Runtime r = Runtime.getRuntime();
        // Use bash -c so we can handle things like multi commands separated by ; and
        // things like quotes, $, |, and \. My tests show that command comes as
        // one argument to bash, so we do not need to quote it to make it one thing.
        // Also, exec may object if it does not have an executable file as the first thing,
        // so having bash here makes it happy provided bash is installed and in path.
        String[] commands = {"bash", "-c", command};
        try {
            Process p = r.exec(commands);

            p.waitFor();
            BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";

            while ((line = b.readLine()) != null) {
                System.out.println(line);
            }

            b.close();
            success = true;
        } catch (Exception e) {
            System.err.println("Failed to execute bash with command: " + command);
            e.printStackTrace();
        }
        return true;
    }
    //    Fonction pour lire l'ordre des gobelets haut fond
    public void readColors(String pathEx) throws Exception {
        executeBashCommand(pathEx);
        Thread.sleep(3000);
    }
    public static String readtextfile(String path)throws Exception
    {
        // We need to provide file path as the parameter:
        // double backquote is to avoid compiler interpret words
        // like \test as \t (ie. as a escape sequence)
        File file = new File(path);//"C:\\Users\\pankaj\\Desktop\\test.txt"

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        String result="";
        while ((st = br.readLine()) != null)
        {result = st ;}
        return result;
    }


    @Override
    public void execute(int version) {
        try {
            if (proto.equals("yes")){ protomatch();}
            if (homoflag.equals("yes")){homologationflag();}
            if (homomov.equals("yes")){homologationmov();}
            else {
                System.out.println("_____POSSSTART_____:" + posstart);
                Thread one = new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(9500);
                            flag(1);
                        } catch (InterruptedException v) {
                        }
                    }
                };

                one.start();

                if (posstart == "Blue") {
                    //Portes ouvertes à 135 degrés
                    //wrapper.perform(ActuatorsOrders.Valve7Off);
                    //Recupere gobelet devant
  /*              turnTowards(Math.PI/2);
                moveLengthwise(1100,false);
                turnTowards(Math.PI);
                wrapper.arm(0,0);
                wrapper.arm(1,0);
                moveLengthwise(670,false);
                wrapper.arm(0,1);
                wrapper.arm(1,1);
                turnTowards(-Math.PI/2);
                moveLengthwise(400,false);
                turnTowards(-2*Math.PI/3-0.2);
                moveLengthwise(500,false);
                moveLengthwise( 500,false);
                moveLengthwise(500,false);
                moveLengthwise(300,false);
*/


                    turnTowards(Math.PI);
                    moveLengthwise(900, false);
                    suck(1, 1);

                    //Attaque eceuil adversaire
                    turnTowards(Math.PI + 0.60);
                    moveLengthwise(1100, false);
                    turnTowards(-Math.PI / 2);

                    //PERIMETRE TROP GRAND
                /*
                readColors("/usr/bin/python /home/brunlois/PycharmProjects/ColorVision/Centre2.py");
                turnTowards(Math.PI / 2);
                moveLengthwise(-300, true);*/
                    //Marteaudescend
                    //Marteauremonte

                    //Balayage zone de haut fond
                    turnTowards(0);
                    suck(2, 1);
                    suck(3, 1);
                    suck(4, 1);
                    moveLengthwise(550, false);
                    turnTowards(Math.PI / 2);
                    // robot.recalageMeca(true,-300);
                    moveLengthwise(-50, true);
                    turnTowards(0);
                    moveLengthwise(550, false);

                    turnTowards(Math.PI / 2);
                    moveLengthwise(130, false);
                    turnTowards(Math.PI);
                    moveLengthwise(1100, false);

                    //Sortie zone haut fond
                    turnTowards(-0.65 + Math.PI / 2);
                    moveLengthwise(600, false);
                    //Lecture QRcode
                    turnTowards(-Math.PI / 2);
                    executeBashCommand("/usr/bin/python3 /home/brunlois/IdeaProjects/Aruco_tag-master/detection.py"); // Ici il faut mettre /usr/bin/python /absolute/path/to/your/disk.py
                    aruco = readtextfile("/home/brunlois/IdeaProjects/Aruco_tag-master/arucoresults.txt");
                    //envoie donnee ARUCO
                    SocketClientInterface sender = new SocketClientInterface("192.168.1.0", 3000, false);
                    sender.send(aruco);
                    //Fin sortie zone haut fond
                    moveLengthwise(210, false);
                    //hammers
                    moveLengthwise(-210, false);
                    readColors("/usr/bin/python /home/brunlois/PycharmProjects/ColorVision/HautFond.py");
                    turnTowards(-0.70 + Math.PI / 2);
                    moveLengthwise(650, false);

                    //EntreePort
                    moveLengthwise(320, false);
                    turnTowards(Math.PI);
                    try {
                        ecueilTri_slave();
                    } catch (Exception e) {
                        moveLengthwise(300, false);
                        turnTowards(Math.PI / 2);
                        suckall(0);
                        moveLengthwise(500, false);
                        moveLengthwise(-500, false);
                    }
                    turnTowards(0);
                    moveLengthwise(1000, false);
                    turnTowards(Math.PI / 2);
                    moveLengthwise(500, false);
                    turnTowards(Math.PI);
                    wrapper.arm(0, 1);
                    wrapper.arm(1, 1);
                    moveLengthwise(100, false);
                    wrapper.arm(0, 2);
                    wrapper.arm(1, 2);
                    moveLengthwise(50, false);
                    wrapper.arm(0, 1);
                    wrapper.arm(1, 1);
                    moveLengthwise(250, false);
                    wrapper.arm(0, 2);
                    wrapper.arm(1, 2);
                    moveLengthwise(50, false);
                    wrapper.arm(0, 0);
                    wrapper.arm(1, 0);
                    turnTowards(-Math.PI / 2);
                    moveLengthwise(400, false);
                    //FIN MATCH

                    if (aruco.equals("North")) {
                        turnTowards(-Math.PI / 2);
                        moveLengthwise(1200, false);
                        turnTowards(0);
                        moveLengthwise(1000, false);
                    }
                    if (aruco.equals("South")) {
                        turnTowards(0);
                        moveLengthwise(1000, false);
                    }
                } else {
                    if (posstart == "Yellow") {
                        //Recupere gobelet devant
                        turnTowards(0);
                        moveLengthwise(900, false);
                        suck(1, 1);

                        //Attaque eceuil adversaire
                        turnTowards(-0.60);
                        moveLengthwise(1100, false);

                        //PERIMETRE TROP GRAND
                    /*turnTowards(-Math.PI / 2);
                    readColors("/usr/bin/python /home/brunlois/PycharmProjects/ColorVision/Centre2.py");
                    turnTowards(Math.PI / 2);
                    moveLengthwise(-50, true);
                    //Marteaudescend
                    //Marteauremonte
                    */
                        //Balayage zone de haut fond
                        turnTowards(Math.PI);
                        suck(2, 1);
                        suck(3, 1);
                        suck(4, 1);
                        //Recalage
                        moveLengthwise(550, false);
                        turnTowards(Math.PI / 2);
                        moveLengthwise(-50, true);
                        turnTowards(Math.PI);

                        moveLengthwise(550, false);
                        turnTowards(Math.PI / 2);
                        moveLengthwise(130, false);
                        turnTowards(0);
                        moveLengthwise(900, false);

                        //Sortie zone haut fond
                        turnTowards(Math.PI / 2 + 0.65);
                        moveLengthwise(600, false);
                        //Lecture QRcode
                        turnTowards(-Math.PI / 2);
                        executeBashCommand("/usr/bin/python3 /home/brunlois/IdeaProjects/Aruco_tag-master/detection.py"); // Ici il faut mettre /usr/bin/python /absolute/path/to/your/disk.py
                        aruco = readtextfile("/home/brunlois/IdeaProjects/Aruco_tag-master/arucoresults.txt");
                        //envoie donnee ARUCO
                        SocketClientInterface sender = new SocketClientInterface("192.168.1.0", 3000, false);
                        sender.send(aruco);
                        //retour au jeu
                        moveLengthwise(210, false);
                        //hammers
                        moveLengthwise(-210, false);
                        readColors("/usr/bin/python /home/brunlois/PycharmProjects/ColorVision/HautFond.py");
                        turnTowards(Math.PI / 2 + 0.70);
                        moveLengthwise(650, false);
                        //EntreePort
                        moveLengthwise(370, false);
                        turnTowards(0);
                        try {
                            ecueilTri_slave();
                        } catch (Exception e) {
                            moveLengthwise(300, false);
                            turnTowards(Math.PI / 2);
                            suckall(0);
                            moveLengthwise(500, false);
                            moveLengthwise(-500, false);

                        }

                        turnTowards(Math.PI);
                        moveLengthwise(1000, false);
                        turnTowards(Math.PI / 2);
                        moveLengthwise(500, false);
                        turnTowards(0);
                        wrapper.arm(0, 0);
                        wrapper.arm(1, 0);
                        moveLengthwise(400, false);
                        wrapper.arm(0, 1);
                        wrapper.arm(1, 1);
                        turnTowards(-Math.PI / 2);
                        moveLengthwise(400, false);

                        if (aruco.equals("North")) {
                            moveLengthwise(-500, false);
                            turnTowards(-Math.PI / 2);
                            moveLengthwise(1200, false);
                            turnTowards(Math.PI);
                            moveLengthwise(1100, false);
                        }
                        if (aruco.equals("South")) {
                            moveLengthwise(-500, false);
                            turnTowards(Math.PI);
                            moveLengthwise(1100, false);
                        }
                    }

                }
            }

        } catch (UnableToMoveException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    Fonction permettant de considérer les 3 cas après récuperation
    //    des gobelets dans l'écueil adverse avant la zone de haut fond
    public void ecueilTri_slave() throws Exception{

        String ecueilS = readtextfile("/usr/bin/python /home/brunlois/PycharmProjects/ColorVision/hautfond.txt");
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
                    hfTri();
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
                    hfTri();
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
                    hfTri();
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
                    hfTri();
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
                    hfTri();
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
                    hfTri();
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
                suckall(0);

                case "RRV":
                case "RVR":
                case "VRR":
                case "RVV":
                case "VVR":
                case "VRV":
                case "VVV":


//          Cas récup 4 gobelets
                case "---V":
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

                case "V---":
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



                case "---R":
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

                case "R---":
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


    public void protomatch() throws Exception {
        if (posstart.equals("Blue")) {
            turnTowards(Math.PI);
            suckall(1);
            moveLengthwise(1100,false);
            turnTowards(-Math.PI / 2);
            executeBashCommand("/usr/bin/python3 /home/brunlois/IdeaProjects/Aruco_tag-master/detection.py"); // Ici il faut mettre /usr/bin/python /absolute/path/to/your/disk.py
            aruco=readtextfile("/home/brunlois/IdeaProjects/Aruco_tag-master/arucoresults.txt");
            //envoie donnee ARUCO
            SocketClientInterface sender = new SocketClientInterface("192.168.1.0", 3000,false);
            sender.send(aruco);
            //retour au jeu
            turnTowards(Math.PI/2);
            moveLengthwise(1000,false);
            suckall(0);
            moveLengthwise(-100,false);
            turnTowards(-Math.PI/2);
            moveLengthwise(100,false);
            if (aruco.equals("North")) {
                moveLengthwise(1200, false);
                turnTowards(0);
                moveLengthwise(1000, false);
            }
            if (aruco.equals("South")) {
                turnTowards(0);
                moveLengthwise(1000, false);
            }}

        else {
            turnTowards(0);
            suckall(1);
            moveLengthwise(1100,false);
            turnTowards(-Math.PI / 2);
            executeBashCommand("/usr/bin/python3 /home/brunlois/IdeaProjects/Aruco_tag-master/detection.py"); // Ici il faut mettre /usr/bin/python /absolute/path/to/your/disk.py
            aruco=readtextfile("/home/brunlois/IdeaProjects/Aruco_tag-master/arucoresults.txt");
            //envoie donnee ARUCO
            SocketClientInterface sender = new SocketClientInterface("192.168.1.0", 3000,false);
            sender.send(aruco);
            //retour au jeu
            turnTowards(Math.PI/2);
            moveLengthwise(1000,false);
            suckall(0);
            moveLengthwise(-100,false);
            turnTowards(-Math.PI/2);
            moveLengthwise(100,false);
            if (aruco.equals("North")) {
                moveLengthwise(1200, false);
                turnTowards(Math.PI);
                moveLengthwise(1000, false);
            }
            if (aruco.equals("South")) {
                turnTowards(Math.PI);
                moveLengthwise(1000, false);
            }
            }
    }
    //Position en entrée (0,0)=(1500,0) , axe x inversé, zone nord bleue

    public void homologationflag () throws UnableToMoveException {
        moveLengthwise(400,false);

        Thread one = new Thread() {
            public void run() {
                try {
                    sleep(9500);
                    flag(1);
                } catch (InterruptedException v) {
                }
            }
        };
        one.start();
    }
    public void homologationmov () throws UnableToMoveException {
        turnTowards(1.5*Math.PI/6);
        moveLengthwise(500,false);
        moveLengthwise(500,false);
        moveLengthwise(300,false);

    }
    @Override
    public void finalize(Exception e) {

    }
}

