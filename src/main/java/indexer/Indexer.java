package indexer;

import indexer.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.text.InternationalFormatter;

import java.io.FileWriter;
import java.io.IOException;

import org.javatuples.Triplet;

import indexer.queryParser.base.Token;
import indexer.queryParser.processor.Evaluator;
import indexer.queryParser.processor.Lexer;
import indexer.queryParser.processor.Parser;
import indexer.utils.Utils;
import indexer.postingListGenerator.postingList.*;

import org.javatuples.Pair;

public class Indexer {
    private String query;

    Indexer() {

    }

    Indexer(String query) {
        this.query = query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQeury() {
        return this.query;
    }

    public PostingList indexQuery() throws ClassNotFoundException {

        Evaluator evaluator = new Evaluator();
        // TOkenize and priorize terms and operators within query to post fix
        List<Token> tokens = Lexer.tokenize(this.query);
        List<Token> transformedTokens = Parser.transformToPostFix(tokens);

        // Evaluate the query expression
        return (PostingList) evaluator.evaluate(transformedTokens);
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException {

        // TODO: Remove hardcoded test query
        String query = "(<NER:B-PERSON+I-PERSON*E-PERSON*>|<NER:B-GPE+I-GPE*E-GPE*>)|<NER:S-Cardinal+>";

        Indexer indexer = new Indexer();
        indexer.setQuery(query);

        // Evaluate the query expression
        PostingList results = indexer.indexQuery();

        System.out.println(results);
        // Utils.saveResultsOnDisk(query, results);

        /*
         * // FIXME: TEST PostList and PostListItem Triplet<String, String, Integer>
         * token = new Triplet<String, String, Integer>("tokenText", "tokenAnnotation",
         * 1); List<Triplet<String, String, Integer>> tokenList = new ArrayList();
         * tokenList.add(token); Triplet<String, String, List<Triplet<String, String,
         * Integer>>> postingList1 = new Triplet("1", "1", tokenList); Triplet<String,
         * String, List<Triplet<String, String, Integer>>> postingList2 = new
         * Triplet("2", "1", tokenList); Triplet<String, String, List<Triplet<String,
         * String, Integer>>> postingList3 = new Triplet("3", "1", tokenList);
         * Triplet<String, String, List<Triplet<String, String, Integer>>> postingList4
         * = new Triplet("1", "1", tokenList); Triplet<String, String,
         * List<Triplet<String, String, Integer>>> postingList5 = new Triplet("5", "1",
         * tokenList); Triplet<String, String, List<Triplet<String, String, Integer>>>
         * postingList6 = new Triplet("6", "1", tokenList);
         * 
         * Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String>
         * testPair1 = new Pair<Triplet<String, String, List<Triplet<String, String,
         * Integer>>>, String>( postingList1, "test1"); Pair<Triplet<String, String,
         * List<Triplet<String, String, Integer>>>, String> testPair2 = new
         * Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>,
         * String>( postingList2, "test2"); Pair<Triplet<String, String,
         * List<Triplet<String, String, Integer>>>, String> testPair3 = new
         * Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>,
         * String>( postingList3, "test3"); Pair<Triplet<String, String,
         * List<Triplet<String, String, Integer>>>, String> testPair4 = new
         * Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>,
         * String>( postingList4, "test1"); Pair<Triplet<String, String,
         * List<Triplet<String, String, Integer>>>, String> testPair5 = new
         * Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>,
         * String>( postingList5, "test5"); Pair<Triplet<String, String,
         * List<Triplet<String, String, Integer>>>, String> testPair6 = new
         * Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>,
         * String>( postingList6, "test6");
         * 
         * PostingListItem testItem1 = new PostingListItem(testPair1); PostingListItem
         * testItem2 = new PostingListItem(testPair2); PostingListItem testItem3 = new
         * PostingListItem(testPair3); PostingListItem testItem4 = new
         * PostingListItem(testPair4); PostingListItem testItem5 = new
         * PostingListItem(testPair5); PostingListItem testItem6 = new
         * PostingListItem(testPair6);
         * 
         * // System.out.println(testTable); PostingList testList1 = new PostingList();
         * testList1.appendItem(testItem1); testList1.appendItem(testItem2);
         * testList1.appendItem(testItem3);
         * 
         * PostingList testList2 = new PostingList(); testList2.appendItem(testItem4);
         * testList2.appendItem(testItem5); testList2.appendItem(testItem6);
         * 
         * System.out.println("-------testList1-------");
         * System.out.println(testList1.toString());
         * 
         * System.out.println("-------testList2-------");
         * System.out.println(testList2.toString());
         * 
         * System.out.println("-------intersection (and)-------"); PostingList
         * intersectionList = PostingList.intersection(testList1, testList2);
         * System.out.println(intersectionList.toString());
         * 
         * System.out.println("-------union (or)-------"); PostingList unionList =
         * PostingList.union(testList1, testList2);
         * System.out.println(unionList.toString());
         */
    }
}
