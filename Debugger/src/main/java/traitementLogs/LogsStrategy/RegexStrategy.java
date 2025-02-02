package traitementLogs.LogsStrategy;

import graphique.FenetreLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*obtention de la couleur de la zone de départ des robots*/
public class RegexStrategy {
    static public String regexStrategy (String log) {
        Matcher CouleurDeDepart = Pattern.compile("Couleur").matcher(log);
        if (CouleurDeDepart.find()) {
            String couleur = log.substring(114);
            FenetreLog.addLogTextln("couleurZone = " + couleur);
            return couleur;
        }
        return null;
    }
}
