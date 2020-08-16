package processor;

import base.Operator;
import base.Symbols;
import base.Token;
import base.TokenType;
import exception.ExceptionCollection;
import util.OperatorUtil;
import index.App;
import index.App.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

import org.javatuples.Triplet;

public class Evaluator {
    public static void main(String[] args) throws ClassNotFoundException {
        String qeury = "(<NER:B-PERSON+I-PERSON*E-PERSON*>|<UPOS:NOUN+>)";
        List<Token> tokens = Lexer.tokenize(qeury);
        System.err.println(String.format("[Evaluator:tokens]: %s", tokens));
        // tokens = Lexer.tokenize("*3(*-2!3)");
        List<Token> transformedTokens = Parser.transformToPostFix(tokens);
        System.err.println(String.format("[Evaluator:transformedTokens]: %s", transformedTokens));
        Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> result = (Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String>) evaluate(
                transformedTokens);
        System.out.println(result.toString());
    }

    // stack-based evaluation of RPN expression
    public static Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> evaluate(
            List<Token> tokens) throws ClassNotFoundException {

        // Generate list of query terms
        ArrayList<String> queryTerms = new ArrayList<String>();

        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == TokenType.TERM) {
                queryTerms.add((String) tokens.get(i).getValue());
            }
        }

        System.err.println(String.format("[Evaluator:queryTerms]: %s", queryTerms.toString()));

        for (int i = 0; i < queryTerms.size(); i++) {
            queryTerms.get(i);
        }

        // Fake term_dict (TODO: replace this by search result from InvertedSearch)
        HashMap<String, List<String>> term_dict = new HashMap<String, List<String>>();
        HashMap<String, Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String>> _term_dict = new HashMap();

        Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> dict1 = new Hashtable();
        Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> dict2 = new Hashtable();
        Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> dict3 = new Hashtable();
        Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> dict4 = new Hashtable();
        Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> dict5 = new Hashtable();

        Triplet<String, String, Integer> _token1 = new Triplet<String, String, Integer>("Selena", "B-PERSON", 21);
        Triplet<String, String, Integer> _token2 = new Triplet<String, String, Integer>("Montgomery", "E-PERSON", 22);
        Triplet<String, String, Integer> _token3 = new Triplet<String, String, Integer>("Andres", "B-PERSON", 2);
        Triplet<String, String, Integer> _token4 = new Triplet<String, String, Integer>("Manuel", "I-PERSON", 3);
        Triplet<String, String, Integer> _token5 = new Triplet<String, String, Integer>("Lopez", "I-PERSON", 4);
        Triplet<String, String, Integer> _token6 = new Triplet<String, String, Integer>("Obrador", "E-PERSON", 5);
        Triplet<String, String, Integer> _token7 = new Triplet<String, String, Integer>("Edgardo", "B-PERSON", 44);
        Triplet<String, String, Integer> _token8 = new Triplet<String, String, Integer>("Cruz", "E-PERSON", 45);

        List<Triplet<String, String, Integer>> tokenList1 = new ArrayList<Triplet<String, String, Integer>>();
        tokenList1.add(_token1);
        tokenList1.add(_token2);

        List<Triplet<String, String, Integer>> tokenList2 = new ArrayList<Triplet<String, String, Integer>>();
        tokenList2.add(_token3);
        tokenList2.add(_token4);
        tokenList2.add(_token5);
        tokenList2.add(_token6);

        List<Triplet<String, String, Integer>> tokenList3 = new ArrayList<Triplet<String, String, Integer>>();
        tokenList3.add(_token7);
        tokenList3.add(_token8);

        Triplet<String, String, List<Triplet<String, String, Integer>>> tokenData1 = new Triplet<String, String, List<Triplet<String, String, Integer>>>(
                "28075", "581058839635433", tokenList1);

        Triplet<String, String, List<Triplet<String, String, Integer>>> tokenData2 = new Triplet<String, String, List<Triplet<String, String, Integer>>>(
                "29210", "6974254007483150", tokenList2);

        Triplet<String, String, List<Triplet<String, String, Integer>>> tokenData3 = new Triplet<String, String, List<Triplet<String, String, Integer>>>(
                "28767", "9725472468251629", tokenList3);

        dict1.put(tokenData1, "Selena Montgomery");
        dict1.put(tokenData2, "Andres Manuel Lopez Obrador");
        dict2.put(tokenData2, "Andres Manuel Lopez Obrador");
        dict2.put(tokenData3, "Edgardo Cruz");
        dict3.put(tokenData2, "Andres Manuel Lopez Obrador");
        dict4.put(tokenData3, "Edgardo Cruz");
        dict5.put(tokenData1, "Selena Montgomery");

        term_dict.put("NOUN*VERB?", new ArrayList<String>(Arrays.asList("id_0", "id_1", "id_2")));
        term_dict.put("B-GPE?I-GPE*E-GPE?", new ArrayList<String>(Arrays.asList("id_3", "id_4", "id_5")));
        term_dict.put("term2", new ArrayList<String>(Arrays.asList("id_0", "id_1", "id_6")));
        System.err.println(String.format("[Evaluator:term dict]: %s", term_dict.toString()));

        _term_dict.put("term1", dict1);
        _term_dict.put("term2", dict2);
        _term_dict.put("term3", dict3);
        _term_dict.put("term4", dict4);
        _term_dict.put("term5", dict5);
        System.err.println(String.format("[Evaluator:term dict]: %s", _term_dict.toString()));

        Stack<Token> evalStack = new Stack<>();

        for (Token curToken : tokens) {

            System.err.println(String.format("[Evaluator:currToken]: %s", curToken.toString()));

            // push number to stack
            if (curToken.getType() == TokenType.TERM) {
                // TODO: Replace this line with the actual Regex result
                Token queryResult = new Token<>(App.findRegexMatches(curToken.toString()), TokenType.LIST);
                evalStack.push(queryResult);
                // operator handling
            } else {
                char operatorSymbol = (char) curToken.getValue();
                System.err.println(String.format("[Evaluator:operatorSymbol]: %c", operatorSymbol));

                // if binary operator => 2 numbers needed, unary only needs 1 number
                if (evalStack.size() < 2 && Symbols.BI_OPERATORS.contains(operatorSymbol)
                        || evalStack.size() < 1 && Symbols.UNARY_OPERATORS.contains(operatorSymbol)) {
                    throw new ExceptionCollection.EvaluatorException("No number found to map to operator.");
                } else if (Symbols.BI_OPERATORS.contains(operatorSymbol)) {
                    // operation applies to top two elements
                    Token token1 = evalStack.pop();
                    Token token2 = evalStack.pop();

                    System.err.println(String.format("[Evaluator:token 1 (list1)]: %s", token1.toString()));
                    System.err.println(String.format("[Evaluator:token 2 (list2)]: %s", token2.toString()));

                    Operator.VarArgsFunction operation = Objects
                            .requireNonNull(OperatorUtil.getOperator((Character) curToken.getValue())).getOperation();

                    System.err.println(String.format("[Evaluator:operation==null]: %b", operation == null));

                    // Performing AND operation
                    if (operation == null && (Character) curToken.getValue() == '&') {

                        Map<Object, Object> commonMap = ((Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String>) token1
                                .getValue()).entrySet().stream().filter(
                                        x -> ((Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String>) token2
                                                .getValue()).containsKey(x.getKey()))
                                        .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

                        Hashtable<Object, Object> commonTable = new Hashtable();
                        commonTable.putAll(commonMap);

                        System.err.println(String.format("[Evaluator:intersection]: %s", commonTable.toString()));

                        // push result back to stack
                        evalStack.push(new Token<>(commonTable, TokenType.LIST));

                    }
                    // Performing OR operation
                    else if (operation == null && (Character) curToken.getValue() == '|') {

                        ((Hashtable<String, List<String>>) token1.getValue())
                                .putAll((Map<? extends String, ? extends List<String>>) token2.getValue());

                        System.err.println(String.format("[Evaluator:union]: %s", token1.getValue().toString()));

                        // push result back to stack
                        evalStack.push(new Token<>(token1.getValue(), TokenType.LIST));
                    }

                } else if (Symbols.UNARY_OPERATORS.contains(operatorSymbol)) {
                    // operation applies to top element
                    Token number = evalStack.pop();
                    Operator.VarArgsFunction operation = Objects
                            .requireNonNull(OperatorUtil.getOperator((Character) curToken.getValue())).getOperation();
                    // push result back to stack
                    evalStack.push(new Token<>(operation.apply((Double) number.getValue()), TokenType.NUMBER));
                }
            }
        }
        // numbers left over
        if (evalStack.size() > 1) {
            throw new ExceptionCollection.EvaluatorException("No operator found to map to number.");
        }
        // only value in stack is result, round it to given digit number
        return (Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String>) evalStack.pop()
                .getValue();
    }
}
