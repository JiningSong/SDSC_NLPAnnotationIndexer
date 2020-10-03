package index;

import java.util.Hashtable;
import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import index.utils.Database;
import index.utils.Timer;
import index.utils.Utils;

public class Index {
    public static Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> processQuery(
            String query) throws ClassNotFoundException {

        Pair<String, String> typeAndPattern = Utils.parseTypeAndPattern(query);
        if (typeAndPattern != null) {

            /********************* Start timer *********************/
            Timer mainProcessTimer = new Timer();
            mainProcessTimer.start();
            /*******************************************************/

            Indexer indexer = new Indexer();

            // Store type and pattern in indexer, and get configs accoring to the regex type
            // and pattern specified in query
            indexer.setRegexTypeAndPattern(typeAndPattern);
            Triplet<String, Integer, Hashtable<String, String>> configs = Utils
                    .buildConfigs(indexer.getAnnotationType());

            if (configs == null) {
                System.out.println("[ERROR]: Type of Regex Index or Regex Format is Wrong\nTry again later...");

                /********************* Stop timer *********************/
                mainProcessTimer.stop();
                mainProcessTimer.printResult("Program");
                /*****************************************************/

                return null;
            } else {

                indexer.setConfigs(configs);

                // Normalize Regex pattern according to user input and configs
                String normalizedRegexPattern = Utils.normalizeRegexPattern(indexer);

                // Find regex matches
                Database database = new Database();
                Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> matches = database
                        .getRegexMatches(indexer, normalizedRegexPattern);

                /********************* Stop timer *********************/
                mainProcessTimer.stop();
                mainProcessTimer.printResult("Program");
                /*****************************************************/

                return matches;
            }
        } else {
            return null;
        }
    }
}
