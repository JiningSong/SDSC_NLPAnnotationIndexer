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

    public Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> indexQuery()
            throws ClassNotFoundException {

        Evaluator evaluator = new Evaluator();
        // TOkenize and priorize terms and operators within query to post fix
        List<Token> tokens = Lexer.tokenize(this.query);
        List<Token> transformedTokens = Parser.transformToPostFix(tokens);

        // Evaluate the query expression
        return (Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String>) evaluator
                .evaluate(transformedTokens);
    }

    static class Test {
        private Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> value;

        Test(Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != this.getClass())
                return false;
            Test test = (Test) obj;
            return (this.value.toString().equals(test.value.toString()));
        }

        @Override
        public int hashCode() {
            Triplet<String, String, List<Triplet<String, String, Integer>>> postingList = this.value.getValue0();
            String docId = postingList.getValue0();
            String sentId = postingList.getValue1();
            List<Triplet<String, String, Integer>> tokens = postingList.getValue2();
            int tokensCount = tokens.size();
            String appendedIdString = docId + sentId;
            for (int i = 0; i < tokensCount; i++) {
                appendedIdString += String.valueOf(tokens.get(i).getValue2());
            }
            int appendedId = Integer.parseInt(appendedIdString);
            return appendedId;
        }

        public String toString() {
            return this.value.toString();
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException {

        // // TODO: Remove hardcoded test query
        // String query =
        // "(<NER:B-PERSON+I-PERSON*E-PERSON*>|<NER:B-GPE+I-GPE*E-GPE*>)|<NER:S-Cardinal+>";

        // Indexer indexer = new Indexer();
        // indexer.setQuery(query);

        // // Evaluate the query expression
        // Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>,
        // String> results = indexer
        // .indexQuery();

        // Utils.saveResultsOnDisk(query, results);

        // FIXME: TEST
        Triplet<String, String, Integer> token = new Triplet<String, String, Integer>("tokenText", "tokenAnnotation",
                1);
        List<Triplet<String, String, Integer>> tokenList = new ArrayList();
        tokenList.add(token);
        Triplet<String, String, List<Triplet<String, String, Integer>>> postingList1 = new Triplet("1", "1", tokenList);
        Triplet<String, String, List<Triplet<String, String, Integer>>> postingList2 = new Triplet("2", "1", tokenList);
        Triplet<String, String, List<Triplet<String, String, Integer>>> postingList3 = new Triplet("3", "1", tokenList);
        Triplet<String, String, List<Triplet<String, String, Integer>>> postingList4 = new Triplet("1", "1", tokenList);
        Triplet<String, String, List<Triplet<String, String, Integer>>> postingList5 = new Triplet("5", "1", tokenList);
        Triplet<String, String, List<Triplet<String, String, Integer>>> postingList6 = new Triplet("6", "1", tokenList);

        Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> testPair1 = new Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String>(
                postingList1, "test1"); 
        Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> testPair2 = new Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String>(
                postingList2, "test2");
        Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> testPair3 = new Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String>(
                postingList3, "test3");
        Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> testPair4 = new Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String>(
                postingList4, "test1");
        Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> testPair5 = new Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String>(
                postingList5, "test5");
        Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> testPair6 = new Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String>(
                postingList6, "test6");

        Test test1 = new Test(testPair1);
        Test test2 = new Test(testPair2);
        Test test3 = new Test(testPair3);
        Test test4 = new Test(testPair4);
        Test test5 = new Test(testPair5);
        Test test6 = new Test(testPair6);

        // System.out.println(testTable);
        HashSet<Test> testSet1 = new HashSet<>();
        testSet1.add(test1);
        testSet1.add(test2);
        testSet1.add(test3);

        HashSet<Test> testSet2 = new HashSet<>();
        testSet2.add(test4);
        testSet2.add(test5);
        testSet2.add(test6);

        // testSet1 or testSet2
        // testSet1.addAll(testSet2);

        // testSet1 and testSet2
        testSet1.retainAll(testSet2);
        Iterator<Test> it = testSet1.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        // it = testSet2.iterator();
        // while (it.hasNext()) {
        // System.out.println(it.next());
        // }
    }
}
