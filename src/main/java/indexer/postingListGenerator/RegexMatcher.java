package indexer.postingListGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.util.regex.*;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.apache.commons.lang3.StringUtils;

import indexer.postingListGenerator.postingList.*;

public class RegexMatcher {

    /**
     * Function Name: findMatches
     * 
     * Description: findMatches outputs the substrings that match the defined
     * regular expression rule
     * 
     * @param theRegex       Regex rule in String format
     * @param str2Check      The input string to match with the regex format
     * @param annotationType Type of annotation (UPOS, XPOS, NER, or DEPPARSE)
     * 
     * @return findMatches returns a hashtable containing the match results. The
     *         keys for teh hashtable are matched texts, values for each key are
     *         token data for tokens within matched text
     */
    public static PostingList findMatches(String normalizedRegexPattern, String str2Check,
            List<Triplet<String, String, Integer>> tokens, String docID, String sentID) {

        PostingList postinglist = new PostingList();
        Pattern checkRegex = Pattern.compile(normalizedRegexPattern);
        Matcher regexMatcher = checkRegex.matcher(str2Check);

        while (regexMatcher.find()) {
            if (regexMatcher.group().length() != 0) {
                int start_char = regexMatcher.start();
                int end_char = regexMatcher.end();
                int start_token = 0;
                int end_token = 0;
                int counter = 0;
                for (int i = 0; i < end_char; i++) {
                    if (i == start_char) {
                        start_token = counter;

                    }
                    if (i == end_char - 1) {
                        end_token = counter;
                    }
                    if (str2Check.charAt(i) == ' ') {
                        counter++;
                    }
                }
                List<Triplet<String, String, Integer>> matchedTokens = new ArrayList<Triplet<String, String, Integer>>();
                String tokensText = "";
                for (int i = start_token; i < end_token + 1; i++) {
                    matchedTokens.add(tokens.get(i));
                    tokensText += tokens.get(i).getValue(0) + " ";
                }
                StringUtils.substring(tokensText, 0, tokensText.length() - 1);

                if (tokensText.length() != 0) {
                    
                    // Create postingListItem with tokenslist and tokensText
                    Triplet<String, String, List<Triplet<String, String, Integer>>> tokensList = new Triplet<String, String, List<Triplet<String, String, Integer>>>(
                            docID, sentID, matchedTokens);
                    Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> tokenListAndText = new Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String>(
                            tokensList, tokensText);
                    
                    // Store new postingListItem in postingList
                    PostingListItem postingListItem = new PostingListItem(tokenListAndText);
                    postinglist.appendItem(postingListItem);
                }
            }
        }
        return postinglist;
    }
}