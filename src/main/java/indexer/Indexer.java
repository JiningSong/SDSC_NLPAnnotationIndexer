package indexer;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import indexer.postingListGenerator.postingList.PostingList;
import indexer.queryParser.base.Token;
import indexer.queryParser.processor.Evaluator;
import indexer.queryParser.processor.Lexer;
import indexer.queryParser.processor.Parser;
import indexer.utils.Utils;

public class Indexer {
    private String index;

    Indexer() {

    }

    Indexer(String index) {
        this.index = index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIndex() {
        return this.index;
    }

    public Boolean createIndex() throws ClassNotFoundException, IOException {

        Evaluator evaluator = new Evaluator();
        // TOkenize and priorize terms and operators within query to post fix
        List<Token> tokens = Lexer.tokenize(this.index);
        List<Token> transformedTokens = Parser.transformToPostFix(tokens);

        System.out.println(transformedTokens.toString());

        // Evaluate the query expression
        PostingList results = (PostingList) evaluator.evaluate(transformedTokens);
        System.out.println(results.toString());
        Utils.savePostingListOnDisk(index, results);

        return true;
    }

    public PostingList queryIndex(String query) {
	//TODO: WIP
        return null;
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        Scanner scan = new Scanner(System.in);
        Indexer indexer = new Indexer();

        while (true) {

            System.out.print("Please type 1 or 2 for (1: createIndex, 2: queryIndex): ");
            int userInput = scan.nextInt();

            // Create index
            if (userInput == 1) {
                // Index on token sequences representing person, GPE, or cardinal.
                // FIXME: Remove hardcoded test query
                String index = "<NER:B-PERSON+I-PERSON*E-PERSON*>|(<NER:B-GPE+I-GPE*E-GPE*>|<NER:S-Cardinal+>)";
                indexer.setIndex(index);
                // generates posting list for the given index and store it on disk
                indexer.createIndex();
            }
            // Query existing index
            else if (userInput == 2) {
                // This query finds person entities containing 'Harry' as substring
                // FIXME: Remove hardcoded test query
                String query = "<NER:B-PERSON+I-PERSON*E-PERSON*>{Harry}";
                PostingList results = indexer.queryIndex(query);

            }
            // Invalid input
            else {
                System.out.print("Invalid input");
            }
        }

        // // TODO: Remove hardcoded test query
        // String query =
        // "<NER:B-PERSON+I-PERSON*E-PERSON*>|(<NER:B-GPE+I-GPE*E-GPE*>|<NER:S-Cardinal+>)";

        // Indexer indexer = new Indexer(query);

        // // Evaluate the query expression
        // PostingList results = indexer.indexQuery();

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
