package index;

import java.util.Hashtable;

import org.javatuples.Pair;
import org.javatuples.Triplet;

public class Indexer {

    /* Member variables */
    private String annotationType;
    private String regex;
    private String queryString;
    private String tokenQuery;
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

}