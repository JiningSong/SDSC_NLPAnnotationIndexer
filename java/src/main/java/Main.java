import java.util.Hashtable;
import java.util.List;

import org.javatuples.Triplet;

import base.Token;
import processor.Evaluator;
import processor.Lexer;
import processor.Parser;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {

        // TODO: Remove hardcoded test query
        String qeury = "(<NER:B-PERSON+I-PERSON*E-PERSON*>|<NER:B-GPE+I-GPE*E-GPE*>)|<NER:S-Cardinal+>";

        Evaluator evaluator = new Evaluator();

        // TOkenize and priorize terms and operators within query to post fix
        List<Token> tokens = Lexer.tokenize(qeury);
        List<Token> transformedTokens = Parser.transformToPostFix(tokens);

        // Evaluate the query expression
        Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> result = (Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String>) evaluator.evaluate(
                transformedTokens);
        System.out.println(result.toString());
    }
}
