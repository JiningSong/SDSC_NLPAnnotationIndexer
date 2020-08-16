package index.utils;

import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.Iterator;
import java.util.Set;
import java.util.Hashtable;

import org.javatuples.Triplet;

import org.javatuples.Pair;

public class Utils {

    // Comparator that compares two triplets storing token data. The two data
    // triplets are compared acording to the alphabetical order of token_text
    public static Comparator<Triplet<String, String, Integer>> comparator = new Comparator<Triplet<String, String, Integer>>() {
        public int compare(Triplet<String, String, Integer> tripletA, Triplet<String, String, Integer> tripletB) {
            return Integer.compare((int) tripletA.getValue(2), (int) tripletB.getValue(2));
        }

    };

    /**
     * Function Name: createFileWriter
     * 
     * Description: Construct a FileWriter object for 'fileName' in 'append' mode
     * 
     * @param fileName name of the file that FW opens
     * @param append   mode (r or w)
     * 
     * @return FileWriter for the according file or null if exception occurs
     */
    public static FileWriter createFileWriter(String fileName, boolean append) {
        try {
            File myObj = new File(fileName);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter fw = new FileWriter(myObj, append);
            if (fw != null) {
                return fw;
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Function Name: generateAnnotationPattern
     * 
     * Description: Concateante annotations of each tokens to form a string of
     * annotation pattern
     * 
     * @param tokenList token list
     * 
     * @return annotationPattern is a string that contains the concatenated sequence
     *         of annotations from tokenList
     */
    public static String generateAnnotationPattern(List<Triplet<String, String, Integer>> tokenList) {
        String annotationPattern = "";

        for (int i = 0; i < tokenList.size(); i++) {
            annotationPattern += tokenList.get(i).getValue1() + " ";
        }
        return annotationPattern;
    }

    /**
     * Function Name: normalizeRegexPattern
     * 
     * Description: Normalize regex pattern by replacing '<,>' bounded regex pattern
     * with round parantheses and spaces which are more acceptable by Java's Regex
     * 
     * @param regexPattern   unormalized regex pattern
     * @param annotationType type of annotation within regex pattern
     * @param normTable      hashtable used to normalize the pattern
     * 
     * @return normalized regex pattern
     */
    public static String normalizeRegexPattern(String regexPattern, String annotationType,
            Hashtable<String, String> normTable) {

        String normalizedRegex;

        // unify letter cases
        if (annotationType.equals("4")) {
            normalizedRegex = regexPattern.toLowerCase();
        } else {
            normalizedRegex = regexPattern.toUpperCase();
        }

        // replace strings in regex pattern which are equal to hashtable keys with the
        // according hashtable values
        Set<String> keys = normTable.keySet();
        Iterator<String> itr = keys.iterator();
        String key, value;
        while (itr.hasNext()) {
            key = itr.next();
            value = normTable.get(key);
            normalizedRegex = normalizedRegex.replace(key, value);
        }
        return normalizedRegex;
    }

    /**
     * Function Name: outputResult
     * 
     * Description: Output regex results to disk through FileWriters
     * 
     * @param regexResults regex results containing token meta data
     * @param docID        doc_id for the regex result
     * @param sentID       sent_id for the regex result
     * @param fwDataset    FW for dataset
     * @param fwDatabase   FW for database
     */
    public static void outputResult(Hashtable<String, List<Triplet<String, String, Integer>>> regexResults,
            String docID, String sentID, FileWriter fwDataset, FileWriter fwDatabase) throws IOException {

        Set<String> keys = regexResults.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String nextKey = it.next();
            fwDataset.write(String.format("%s\n", nextKey));
            fwDatabase.write(String.format("%s\n%s\n", docID, sentID));

            List<Triplet<String, String, Integer>> tokenList = regexResults.get(nextKey);
            fwDatabase.write(String.format("%d\n", tokenList.size()));

            for (int i = 0; i < tokenList.size(); i++) {
                fwDatabase.write(String.format("%s\n%s\n%d\n", tokenList.get(i).getValue0(),
                        tokenList.get(i).getValue1(), tokenList.get(i).getValue2()));
            }

            fwDatabase.write("\n");
        }
    }

    public static Pair<String, String> seperateTypeAndPattern(String query) {
        if (query.charAt(3) == ':' || query.charAt(4) == ':') {
            String[] parts = query.split(":", 2);
            return new Pair<String, String>(parts[0], parts[1]);

        } else {
            return null;
        }
    }
}