import java.util.Hashtable;
import java.util.List;

import org.javatuples.Triplet;

import queryParser.base.Token;
import queryParser.processor.Evaluator;
import queryParser.processor.Lexer;
import queryParser.processor.Parser;

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

    public static void main(String[] args) throws ClassNotFoundException {

        // TODO: Remove hardcoded test query
        String query = "(<NER:B-PERSON+I-PERSON*E-PERSON*>|<NER:B-GPE+I-GPE*E-GPE*>)|<NER:S-Cardinal+>";

        Indexer indexer = new Indexer();
        indexer.setQuery(query);

        // Evaluate the query expression
        Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> result = indexer
                .indexQuery();

        System.out.println(result.toString());
    }
}
