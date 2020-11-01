package indexer;

import indexer.utils.Utils;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.io.FileWriter;
import java.io.IOException;

import org.javatuples.Triplet;

import indexer.queryParser.base.Token;
import indexer.queryParser.processor.Evaluator;
import indexer.queryParser.processor.Lexer;
import indexer.queryParser.processor.Parser;
import indexer.utils.Utils;

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

    public static void main(String[] args) throws ClassNotFoundException, IOException {

        // TODO: Remove hardcoded test query
        String query = "(<NER:B-PERSON+I-PERSON*E-PERSON*>|<NER:B-GPE+I-GPE*E-GPE*>)|<NER:S-Cardinal+>";

        Indexer indexer = new Indexer();
        indexer.setQuery(query);

        // Evaluate the query expression
        Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> results = indexer
                .indexQuery();

        Utils.saveResultsOnDisk(query, results);
    }
}
