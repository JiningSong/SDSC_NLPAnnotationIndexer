package indexer.postingListGenerator;

import indexer.postingListGenerator.postingList.PostingList;
import indexer.postingListGenerator.utils.Utils;
import indexer.utils.Timer;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.javatuples.Pair;
import org.javatuples.Triplet;

public class PostingListGenerator {

    /* Member variables */
    private String annotationType;
    private String regex;
    private String queryString;
    private String tokenQuery;
    private String normalizedRegexPattern;
    private int annotationIndex;
    private Hashtable<String, String> regexNormDict;

    /* Getters and Setters */
    public String getAnnotationType() {
        return this.annotationType;
    }

    public String getRegex() {
        return this.regex;
    }

    public String getQueryString() {
        return this.queryString;
    }

    public String getTokenQuery() {
        return this.tokenQuery;
    }

    public int getAnnotationIndex() {
        return this.annotationIndex;
    }

    public Hashtable<String, String> getRegexNormDict() {
        return this.regexNormDict;
    }

    public String getNormalizedRegexPattern() {
        return this.normalizedRegexPattern;
    }

    public void setAnnotationType(String annotationType) {
        this.annotationType = annotationType;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public void setTokenQuery(String tokenQuery) {
        this.tokenQuery = tokenQuery;
    }

    public void setAnnotationIndex(int annotationIndex) {
        this.annotationIndex = annotationIndex;
    }

    public void setRegexNormDict(Hashtable<String, String> regexNormDict) {
        this.regexNormDict = regexNormDict;
    }

    public void setNormalizedRegexPattern() {
        String regexPattern = this.getRegex();
        String annotationType = this.getAnnotationType();
        Hashtable<String, String> normTable = this.getRegexNormDict();
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
        this.normalizedRegexPattern = normalizedRegex;
    }

    /**
     * Function name: saveConfigs
     * 
     * Description: Save configrations including db query variabls and regex
     * normalization dictionary
     * 
     * @param configs Triplet containing: tokenQuery, annotationIndex, and
     *                regexNormDict
     */
    public void setConfigs(Triplet<String, Integer, Hashtable<String, String>> configs) {
        // TODO: modify setTokenQuery and setAnnotationIndex to match new DB schema
        this.setTokenQuery((String) configs.getValue0());
        this.setAnnotationIndex((int) configs.getValue1());
        this.setRegexNormDict((Hashtable<String, String>) configs.getValue2());
        this.setNormalizedRegexPattern();
    }

    /**
     * Function Name: saveUserInputs
     * 
     * Description: Save User inputs as App member variables
     * 
     * @param userInputs Triplet containing: annotationType, regex patter, and
     *                   queryString
     */
    public void setRegexTypeAndPattern(Pair<String, String> userInputs) {
        this.setAnnotationType(userInputs.getValue0());
        this.setRegex(userInputs.getValue1());
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

    public PostingList generatePostingList(
            String query) throws ClassNotFoundException {

        Pair<String, String> typeAndPattern = Utils.parseQuery(query);
        if (typeAndPattern != null) {

            /********************* Start timer *********************/
            Timer mainProcessTimer = new Timer();
            mainProcessTimer.start();
            /*******************************************************/

            // Store type and pattern in indexer, and get configs accoring to the regex type
            // and pattern specified in query
            this.setRegexTypeAndPattern(typeAndPattern);
            Triplet<String, Integer, Hashtable<String, String>> configs = Utils.buildConfigs(this.getAnnotationType());

            if (configs == null) {
                System.out.println("[ERROR]: Type of Regex Index or Regex Format is Wrong\nTry again later...");

                /********************* Stop timer *********************/
                mainProcessTimer.stop();
                mainProcessTimer.printResult("Program");
                /*****************************************************/

                return null;
            } else {

                // save annotation index, normalized annotationt labels, and normalized pattern
                this.setConfigs(configs);

                // Find regex matches
                Database database = new Database();
                PostingList matches = database
                        .getRegexMatches(this);

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