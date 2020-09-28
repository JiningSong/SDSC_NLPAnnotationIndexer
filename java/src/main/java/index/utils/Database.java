package index.utils;

import index.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.io.*;

import org.javatuples.Triplet;
import me.tongfei.progressbar.*;

public class Database {

    /**
     * Function name: connect()
     * 
     * Description: Connect to the PostgresSQL database
     * 
     * @return a Connection object
     * @throws ClassNotFoundException
     */
    public static Connection connect() throws ClassNotFoundException {
        Connection conn = null;
        try {
            Class.forName(Configs.DB_DRIVER);
            conn = DriverManager.getConnection(Configs.DB_URL, Configs.DB_USERNAME, Configs.DB_PASSWORD);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Function Name: getTokenList
     * 
     * Description: This function queries the database using token_query to get all
     * tokens in the table, specified in the token_query, whose sent_id is the same
     * as sent_id in rs. Then the
     * 
     * @param conn            db conn object
     * @param rs              sent query result
     * @param token_query     query string to get all tokens having the same sent_id
     * @param annotationIndex col index where annotaion is stored in token table
     */
    public static List<Triplet<String, String, Integer>> getTokenList(Connection conn, BigDecimal sent_id,
            String token_query, int annotationIndex) throws SQLException {

        List<Triplet<String, String, Integer>> tokenList = new ArrayList<Triplet<String, String, Integer>>();

        // formulate and execute query
        PreparedStatement ps = conn.prepareStatement(token_query);
        ps.setBigDecimal(1, sent_id);
        ResultSet tokenRS = ps.executeQuery();

        // addedTokens is used to prevent repetitive tokens
        Set<Integer> addedTokens = new HashSet<Integer>();

        // populate tokens list
        while (tokenRS.next()) {

            Triplet<String, String, Integer> token = new Triplet<String, String, Integer>(tokenRS.getString(4),
                    tokenRS.getString(annotationIndex), Integer.parseInt(tokenRS.getString(3)));
            if (addedTokens.add(token.getValue2()))
                tokenList.add(token);
        }

        ps.close();
        tokenRS.close();

        // Sort tokens list by tokenid
        Collections.sort(tokenList, Utils.comparator);

        return tokenList;
    }

    /**
     * Function Name: populateRegexMatches
     * 
     * Description: Output regex results to disk through FileWriters
     * 
     * @param indexer      Indexer object that stores configs
     * @param regexResults regex results containing token meta data
     * @param docID        doc_id for the regex result
     * @param sentID       sent_id for the regex result
     * @param fwDataset    FW for dataset
     * @param fwDatabase   FW for database
     */
    public static void populateRegexMatches(Indexer index, FileWriter fwDataset, FileWriter fwDatabase,
            String normalizedRegexPattern) throws ClassNotFoundException, IOException {

        // Database connection
        Connection conn = Database.connect();

        // Query sentence_segmentation table
        try (Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, 0);
                ResultSet rs = stmt.executeQuery(Configs.SENT_QUERY);) {

            // count number of sentences
            int sentCount = 0;
            if (rs.last()) {
                sentCount = rs.getRow();
                rs.beforeFirst();
            }

            Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> matchingResults = new Hashtable();

            ProgressBar pb = new ProgressBar("Performing REGEX Matching on sentences", sentCount);
            // Process each sentence stored in sentence_segmentation table
            while (rs.next()) {

                String docID = rs.getBigDecimal("doc_id").toString();
                String sentID = rs.getBigDecimal("sent_id").toString();

                // populate tokens list with all tokens from the current sentence
                List<Triplet<String, String, Integer>> tokenList = Database.getTokenList(conn,
                        rs.getBigDecimal("sent_id"), index.getTokenQuery(), index.getAnnotationIndex());

                // Stores annotation patterns
                String annotationPattern = Utils.generateAnnotationPattern(tokenList);

                Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> regexResults = RegexMatcher
                        .findMatches(normalizedRegexPattern, annotationPattern, tokenList, docID, sentID);

                if (regexResults.size() != 0) {
                    matchingResults.putAll(regexResults);
                }

                // Output results to disk
                // Utils.outputResult(regexResults, docID, sentID, fwDataset, fwDatabase);
                pb.step();
            }
            System.out.println(matchingResults.toString());

            pb.close();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> getRegexMatches(
            Indexer indexer, String normalizedRegexPattern) throws ClassNotFoundException {
        // Database connection
        Connection conn = Database.connect();

        // Query sentence_segmentation table
        try (Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, 0);
                ResultSet rs = stmt.executeQuery(Configs.SENT_QUERY);) {

            // count number of sentences
            int sentCount = 0;
            if (rs.last()) {
                sentCount = rs.getRow();
                rs.beforeFirst();
            }

            Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> matchingResults = new Hashtable();

            ProgressBar pb = new ProgressBar("Performing REGEX Matching on sentences", sentCount);
            // Process each sentence stored in sentence_segmentation table
            while (rs.next()) {

                String docID = rs.getBigDecimal("doc_id").toString();
                String sentID = rs.getBigDecimal("sent_id").toString();

                // populate tokens list with all tokens from the current sentence
                List<Triplet<String, String, Integer>> tokenList = Database.getTokenList(conn,
                        rs.getBigDecimal("sent_id"), indexer.getTokenQuery(), indexer.getAnnotationIndex());

                // Stores annotation patterns
                String annotationPattern = Utils.generateAnnotationPattern(tokenList);

                Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> regexResults = RegexMatcher
                        .findMatches(normalizedRegexPattern, annotationPattern, tokenList, docID, sentID);

                if (regexResults.size() != 0) {
                    matchingResults.putAll(regexResults);
                }

                // Output results to disk
                // Utils.outputResult(regexResults, docID, sentID, fwDataset, fwDatabase);
                pb.step();
            }
            pb.close();
            return matchingResults;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
