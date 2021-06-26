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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.*;

public class ScriptLoisSlave extends Script {
    String posstart="Blue";
    String aruco="North";
    int dt = 500;
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
            System.out.println("_____POSSSTART_____:" + posstart);
            if (posstart == "Blue") {
                //Portes ouvertes à 135 degrés
                //wrapper.perform(ActuatorsOrders.Valve7Off);
                //Recupere gobelet devant
                turnTowards(Math.PI);
                moveLengthwise(900, false);
                //Suck(1,1)

                //Attaque eceuil adversaire
                turnTowards(Math.PI + 0.60);
                moveLengthwise(1100, false);
                turnTowards(Math.PI / 2);
                moveLengthwise(-300, true);
                //Marteaudescend
                //Marteauremonte

                //Balayage zone de haut fond
                turnTowards(0);
                //Suck(2,1)
                //Suck(3,1)
                //Suck(4,1)
                moveLengthwise(550, false);
                turnTowards(Math.PI / 2);
                // robot.recalageMeca(true,-300);
                moveLengthwise(-300, true);
                turnTowards(0);
                moveLengthwise(550, false);

                turnTowards(Math.PI / 2);
                moveLengthwise(130, false);
                turnTowards(Math.PI);
                moveLengthwise(900, false);

                //Sortie zone haut fond
                turnTowards(-0.65 + Math.PI / 2);
                moveLengthwise(600, false);
                //Lecture QRcode
                turnTowards(-Math.PI/2);
                executeBashCommand("/usr/bin/python3 /home/brunlois/IdeaProjects/Aruco_tag-master/detection.py"); // Ici il faut mettre /usr/bin/python /absolute/path/to/your/disk.py
                aruco=readtextfile("/home/brunlois/IdeaProjects/Aruco_tag-master/arucoresults.txt");
                //envoie donnee ARUCO
                //Fin sortie zone haut fond
                turnTowards(-0.65 + Math.PI / 2);
                moveLengthwise(550, false);

                //EntreePort
                moveLengthwise(200, false);
                turnTowards(Math.PI / 2);
                moveLengthwise(500, false);
                //Suck(1,0)
                //Suck(2,0)
                //Suck(3,0)
                //Suck(4,0)
                //FIN MATCH
                if (aruco.equals("North")) {
                    moveLengthwise(-500, false);
                    turnTowards(-Math.PI / 2);
                    moveLengthwise(1000, false);
                    turnTowards(0);
                    moveLengthwise(1000, false);
                }
                if (aruco.equals("South")) {
                    moveLengthwise(-500, false);
                    turnTowards(0);
                    moveLengthwise(1000, false);
                }
            } else {
                if (posstart == "Yellow") {
                    //Recupere gobelet devant
                    turnTowards(0);
                    moveLengthwise(900, false);
                    //Suck(1,1)

                    //Attaque eceuil adversaire
                    turnTowards(-0.60);
                    moveLengthwise(1100, false);
                    turnTowards(Math.PI / 2);
                    moveLengthwise(-300, true);
                    //Marteaudescend
                    //Marteauremonte

                    //Balayage zone de haut fond
                    turnTowards(Math.PI);
                    //Suck(2,1)
                    //Suck(3,1)
                    //Suck(4,1)
                    //Recalage
                    moveLengthwise(550, false);
                    turnTowards(Math.PI / 2);
                    moveLengthwise(-300, true);
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
                    aruco=readtextfile("/home/brunlois/IdeaProjects/Aruco_tag-master/arucoresults.txt");
                    turnTowards(Math.PI / 2 + 0.65);
                    //ENVOIE DONNEE ARUCO
                    //Fin sortie zone de haut fond
                    moveLengthwise(550, false);
                    //EntreePort
                    moveLengthwise(200, false);
                    turnTowards(Math.PI / 2);
                    moveLengthwise(500, false);
                    //Suck(1,0)
                    //Suck(2,0)
                    //Suck(3,0)
                    //Suck(4,0)
                    if (aruco.equals("North")) {
                        moveLengthwise(-500, false);
                        turnTowards(-Math.PI / 2);
                        moveLengthwise(1000, false);
                        turnTowards(Math.PI);
                        moveLengthwise(1000, false);
                    }
                    if (aruco.equals("South")) {
                        moveLengthwise(-500, false);
                        turnTowards(Math.PI);
                        moveLengthwise(1000, false);
                    }
                }

            }

        } catch (UnableToMoveException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        //Position en entrée (0,0)=(1500,0) , axe x inversé, zone nord bleue

    @Override
    public void finalize(Exception e) {

    }
}

