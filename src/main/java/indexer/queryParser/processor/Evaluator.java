package indexer.queryParser.processor;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

import org.javatuples.Triplet;
import org.javatuples.Pair;

import indexer.queryParser.base.Operator;
import indexer.queryParser.base.Symbols;
import indexer.queryParser.base.Token;
import indexer.queryParser.base.TokenType;
import indexer.queryParser.exception.ExceptionCollection;
import indexer.postingListGenerator.PostingListGenerator;
import indexer.postingListGenerator.postingList.PostingList;
import indexer.queryParser.util.OperatorUtil;

public class Evaluator {
    public Evaluator() {
        super();
    }

    // stack-based evaluation of RPN expression
    public PostingList evaluate(List<Token> tokens) throws ClassNotFoundException {

        // Instantiate a PostingListGenerator for generating posting lists for the given
        // queries
        PostingListGenerator postingListGenerator = new PostingListGenerator();

        Stack<Token> evalStack = new Stack<>();

        for (Token curToken : tokens) {

            // generate posting list and push LIST Token containing <String, posting list>
            // pair to stack
            if (curToken.getType() == TokenType.TERM) {

                PostingList postingList = postingListGenerator.generatePostingList(curToken.toString());
                Pair<String, PostingList> queryAndPostingList = new Pair<String, PostingList>(curToken.toString(),
                        postingList);
                Token resultToken = new Token<>(queryAndPostingList, TokenType.LIST);
                evalStack.push(resultToken);
                // operator handling
            } else {
                char operatorSymbol = (char) curToken.getValue();

                // if binary operator => 2 numbers needed, unary only needs 1 number
                if (evalStack.size() < 2 && Symbols.BI_OPERATORS.contains(operatorSymbol)
                        || evalStack.size() < 1 && Symbols.UNARY_OPERATORS.contains(operatorSymbol)) {
                    throw new ExceptionCollection.EvaluatorException("No number found to map to operator.");
                } else if (Symbols.BI_OPERATORS.contains(operatorSymbol)) {

                    // operation applies to top two elements
                    Pair<String, PostingList> queryAndToken1 = (Pair<String, PostingList>) evalStack.pop().getValue();
                    Pair<String, PostingList> queryAndToken2 = (Pair<String, PostingList>) evalStack.pop().getValue();
                    String concatenatedQuery = queryAndToken1.getValue0() + operatorSymbol + queryAndToken2.getValue0();

                    Operator.VarArgsFunction operation = Objects
                            .requireNonNull(OperatorUtil.getOperator(operatorSymbol)).getOperation();

                    // Performing AND operation
                    if (operation == null && (Character) curToken.getValue() == '&') {

                        PostingList resultAfterAndOperation = PostingList.intersection(queryAndToken1.getValue1(),
                                queryAndToken2.getValue1());

                        // push result back to stack
                        evalStack.push(
                                new Token<>(new Pair<String, PostingList>(concatenatedQuery, resultAfterAndOperation),
                                        TokenType.LIST));
                    }
                    // Performing OR operation
                    else if (operation == null && (Character) curToken.getValue() == '|') {
                        // TODO:
                        // ((Hashtable<String, List<String>>) queryAndToken1.getValue1())
                        // .putAll((Map<? extends String, ? extends List<String>>)
                        // queryAndToken2.getValue1());

                        // push result back to stack
                        PostingList resultAfterOrOperation = PostingList.union(queryAndToken1.getValue1(),
                                queryAndToken2.getValue1());
                        evalStack.push(
                                new Token<>(new Pair<String, PostingList>(concatenatedQuery, resultAfterOrOperation),
                                        TokenType.LIST));
                    }

                } else if (Symbols.UNARY_OPERATORS.contains(operatorSymbol)) {
                    /*
                    // TODO: unary operator (!) is not defined
                    // operation applies to top element
                    Token number = evalStack.pop();
                    Operator.VarArgsFunction operation = Objects
                            .requireNonNull(OperatorUtil.getOperator((Character) curToken.getValue())).getOperation();
                    // push result back to stack
                    evalStack.push(new Token<>(operation.apply((Double) number.getValue()), TokenType.NUMBER));
                    */ 
                    continue;

                }
            }
        }
        // sub queries left over
        if (evalStack.size() > 1) {
            throw new ExceptionCollection.EvaluatorException("No operator found to map to number.");
        }
        // Cast result to PostinList and return 
        return (PostingList) ((Pair<String, PostingList>) (evalStack.pop().getValue())).getValue1();
    }
}
