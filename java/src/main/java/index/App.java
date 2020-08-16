package index;

import index.utils.*;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.io.IOException;
import org.javatuples.Pair;
import org.javatuples.Triplet;

public class App {

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
    private void saveConfigs(Triplet<String, Integer, Hashtable<String, String>> configs) {
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
    private void saveUserInputs(Pair<String, String> userInputs) {
        this.setAnnotationType(userInputs.getValue0());
        this.setRegex(userInputs.getValue1());
    }

    /**
     * Function Name: chooseAnnotation
     * 
     * Description: Select configs according to user input. Configs set here
     * includes token query, annotaion column, annotation transformation dictionary
     * 
     * @param annotationType the type of annotaion that users wants to perform Regex
     *                       matchings on
     */
    private static Triplet<String, Integer, Hashtable<String, String>> chooseAnnotation(String annotationType) {

        final int UPOS_INDEX = 7;
        final int XPOS_INDEX = 8;
        final int ENTITY_TYPE_INDEX = 5;
        final int DEPENDENCY_RELATION_INDEX = 9;

        switch (annotationType) {
            case "UPOS":
                return new Triplet<String, Integer, Hashtable<String, String>>(Configs.POS_QUERY, UPOS_INDEX,
                        RegexNormDict.uposDict);
            case "XPOS":
                return new Triplet<String, Integer, Hashtable<String, String>>(Configs.POS_QUERY, XPOS_INDEX,
                        RegexNormDict.xposDict);
            case "NER":
                return new Triplet<String, Integer, Hashtable<String, String>>(Configs.NER_QUERY, ENTITY_TYPE_INDEX,
                        RegexNormDict.NERDict);
            case "DEPS":
                return new Triplet<String, Integer, Hashtable<String, String>>(Configs.DEPPARSE_QUERY,
                        DEPENDENCY_RELATION_INDEX, RegexNormDict.depparseDict);
            default:
                return null;
        }

    }

    /**
     * Function Name: getUserInput
     * 
     * Description: Read user inputs from std.in
     * 
     */
    private static Pair<String, String> getUserInput() {
        // Collect user input (type and regular expression)

        /*
         * Scanner scanner = new Scanner(System.in); System.out.print(
         * "Select the Type of annotation to index on (type the number before name) \n 1) UPOS \n 2) XPOS \n 3) NER \n 4) DEPPARSE \nYour Choice: "
         * ); String annotationType = scanner.nextLine();
         * 
         * System.out.println("Type the Regular Expression");
         * System.out.print("Regular Expression: "); String regex = scanner.nextLine();
         * scanner.close();
         */

        // FIXME: Hardcode annotationType and regex for easy testing
        String annotationType = "3";
        String regex = "B-PERSON+I-PERSON*E-PERSON*";

        return new Pair<String, String>(annotationType, regex);

    }

    public static Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> findRegexMatches(
            String query) throws ClassNotFoundException {

        Pair<String, String> inputs = Utils.seperateTypeAndPattern(query);
        if (inputs != null) {

            App app = new App();
            app.saveUserInputs(inputs);

            // Initialize and start timer
            Timer mainProcessTimer = new Timer();
            mainProcessTimer.start();

            // Select configs according to user input
            Triplet<String, Integer, Hashtable<String, String>> configs = chooseAnnotation(app.getAnnotationType());

            if (configs == null) {
                System.out.println("[ERROR]: Type of Regex Index or Regex Format is Wrong\nTry again later...");
                mainProcessTimer.stop();
                mainProcessTimer.printResult("Program");
                return null;
            } else {

                // Store configs
                app.saveConfigs(configs);

                // Normalize Regex pattern according to user input and configs
                String normalizedRegexPattern = Utils.normalizeRegexPattern(app.getRegex(), app.getAnnotationType(),
                        app.getRegexNormDict());

                // Return matches
                Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> matches = Database
                        .getRegexMatches(app, normalizedRegexPattern);
                mainProcessTimer.stop();
                mainProcessTimer.printResult("Program");
                return matches;
            }
        } else {
            return null;
        }
    }
}