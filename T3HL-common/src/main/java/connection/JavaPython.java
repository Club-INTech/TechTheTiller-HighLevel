package connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

public class JavaPython {
    public static boolean executeBashCommand(String command) {
        boolean success = false;
        System.out.println("Executing BASH command:\n   " + command);
        Runtime r = Runtime.getRuntime();
        try {
//          Initialise la connexion entre java et le CLI
            Process p = r.exec(command);
            p.waitFor();
            BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";

            while ((line = b.readLine()) != null) {
                System.out.println(line);
            }
            b.close();
            success = true;

        } catch (Exception e) {
            System.err.println("Failed " + command);
            e.printStackTrace();
        }
        return true;
    }
    public static String readtextfile(String path)throws Exception
    {
        File file = new File(path);//"C:\\Users\\pankaj\\Desktop\\test.txt"

        BufferedReader br = new BufferedReader(new FileReader(file));

        String s;
        String result="";
        while ((s = br.readLine()) != null){
            result = s ;
        }
        return result;
    }
}
