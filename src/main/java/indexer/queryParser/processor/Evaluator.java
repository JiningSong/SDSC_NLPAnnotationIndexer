package indexer.queryParser.processor;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

import org.javatuples.Triplet;

import indexer.queryParser.base.Operator;
import indexer.queryParser.base.Symbols;
import indexer.queryParser.base.Token;
import indexer.queryParser.base.TokenType;
import indexer.queryParser.exception.ExceptionCollection;
import indexer.postingListGenerator.PostingListGenerator;
import indexer.queryParser.util.OperatorUtil;

public class Evaluator {
    public Evaluator() {
        super();
    }

    // stack-based evaluation of RPN expression
    public Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> evaluate(
            List<Token> tokens) throws ClassNotFoundException {

        // Instantiate a PostingListGenerator for generating posting lists for the given
        // queries
        PostingListGenerator postingListGenerator = new PostingListGenerator();

        // Generate list of query terms
        ArrayList<String> queryTerms = new ArrayList<String>();

        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == TokenType.TERM) {
                queryTerms.add((String) tokens.get(i).getValue());
            }
        }

        Stack<Token> evalStack = new Stack<>();

        for (Token curToken : tokens) {

            // push number to stack
            if (curToken.getType() == TokenType.TERM) {
                Token queryResult = new Token<>(postingListGenerator.generatePostingList(curToken.toString()),
                        TokenType.LIST);
                evalStack.push(queryResult);
                // operator handling
            } else {
                char operatorSymbol = (char) curToken.getValue();

                // if binary operator => 2 numbers needed, unary only needs 1 number
                if (evalStack.size() < 2 && Symbols.BI_OPERATORS.contains(operatorSymbol)
                        || evalStack.size() < 1 && Symbols.UNARY_OPERATORS.contains(operatorSymbol)) {
                    throw new ExceptionCollection.EvaluatorException("No number found to map to operator.");
                } else if (Symbols.BI_OPERATORS.contains(operatorSymbol)) {
                    // operation applies to top two elements
                    Token token1 = evalStack.pop();
                    Token token2 = evalStack.pop();

                    Operator.VarArgsFunction operation = Objects
                            .requireNonNull(OperatorUtil.getOperator((Character) curToken.getValue())).getOperation();

                    // Performing AND operation
                    if (operation == null && (Character) curToken.getValue() == '&') {

                        Map<Object, Object> commonMap = ((Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String>) token1
                                .getValue()).entrySet().stream().filter(
                                        x -> ((Hashtable<Triplet<String, String, List<Triplet<String, String, Integer>>>, String>) token2
                                                .getValue()).containsKey(x.getKey()))
                                        .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

                        Hashtable<Object, Object> commonTable = new Hashtable();
                        commonTable.putAll(commonMap);

                        // push result back to stack
                        evalStack.push(new Token<>(commonTable, TokenType.LIST));

                    }
                    // Performing OR operation
                    else if (operation == null && (Character) curToken.getValue() == '|') {

                        ((Hashtable<String, List<String>>) token1.getValue())
                                .putAll((Map<? extends String, ? extends List<String>>) token2.getValue());

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
