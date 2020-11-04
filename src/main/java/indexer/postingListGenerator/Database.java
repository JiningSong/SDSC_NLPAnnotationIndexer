package indexer.postingListGenerator;

import indexer.postingListGenerator.postingList.PostingList;
import indexer.postingListGenerator.utils.*;

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

import org.javatuples.Triplet;
import me.tongfei.progressbar.*;

public class Database {

    Connection conn;

    public Database() throws ClassNotFoundException {
        Connection conn = null;
        try {
            Class.forName(DatabaseConfigs.DB_DRIVER);
            conn = DriverManager.getConnection(DatabaseConfigs.DB_URL, DatabaseConfigs.DB_USERNAME,
                    DatabaseConfigs.DB_PASSWORD);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        this.conn = conn;
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
    public List<Triplet<String, String, Integer>> getTokenList(BigDecimal sent_id, String token_query,
            int annotationIndex) throws SQLException {

        List<Triplet<String, String, Integer>> tokenList = new ArrayList<Triplet<String, String, Integer>>();

        // formulate and execute query
        PreparedStatement ps = this.conn.prepareStatement(token_query);
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
     * Function Name: getRegexMatches
     * 
     * Description: Output regex results to disk through FileWriters
     * 
     * @param indexer                Indexer object that stores configs
     * @param normalizedRegexPattern Normalized query pattern
     */
    public PostingList getRegexMatches(PostingListGenerator postingListGenerator) throws ClassNotFoundException {
        // Database connection

        // Query sentence_segmentation table
        try (Statement stmt = this.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, 0);
                ResultSet rs = stmt.executeQuery(DatabaseConfigs.SENT_QUERY);) {

            // count number of sentences
            int sentCount = 0;
            if (rs.last()) {
                sentCount = rs.getRow();
                rs.beforeFirst();
            }

            PostingList matchingResults = new PostingList();

            ProgressBar pb = new ProgressBar("Performing REGEX Matching on sentences", sentCount);
            // Process each sentence stored in sentence_segmentation table
            while (rs.next()) {

                String docID = rs.getBigDecimal("doc_id").toString();
                String sentID = rs.getBigDecimal("sent_id").toString();

                // populate tokens list with all tokens from the current sentence
                List<Triplet<String, String, Integer>> tokenList = this.getTokenList(rs.getBigDecimal("sent_id"),
                        postingListGenerator.getTokenQuery(), postingListGenerator.getAnnotationIndex());

                // Stores annotation patterns
                String annotationPattern = Utils.generateAnnotationPattern(tokenList);

                PostingList regexResults = RegexMatcher.findMatches(postingListGenerator.getNormalizedRegexPattern(),
                        annotationPattern, tokenList, docID, sentID);

                if (regexResults.size() != 0) {
                    matchingResults = PostingList.union(matchingResults, regexResults);
                }

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
