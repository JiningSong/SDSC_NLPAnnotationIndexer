package queryParser.base;

import queryParser.util.MathUtil;

import static queryParser.base.Operator.Associativity.LEFT;
import static queryParser.base.Operator.Associativity.RIGHT;

public enum Operator {
    AND(1, LEFT, '&', null), OR(1, LEFT, '|', null), PARLEFT(4, null, '(', null), PARRIGHT(4, null, ')', null);

    private int precedence;
    private Associativity associativity;
    private char symbol;
    private VarArgsFunction operation;

    Operator(int precedence, Associativity associativity, char symbol, VarArgsFunction operation) {
        this.operation = operation;
        this.precedence = precedence;
        this.associativity = associativity;
        this.symbol = symbol;
    }

    public int getPrecedence() {
        return precedence;
    }

    public Associativity getAssociativity() {
        return associativity;
    }

    public char getSymbol() {
        return symbol;
    }

    public VarArgsFunction getOperation() {
        return operation;
    }

    public enum Associativity {
        LEFT, RIGHT
    }

    @FunctionalInterface
    public interface VarArgsFunction {
        Double apply(Double... args);
    }
}
