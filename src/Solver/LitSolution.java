package Solver;

import java.util.Objects;
/**
 * Data structure for pairing a literal with its reason. A literal does not necessarily need a reason, in the case that it is a decision literal.
 *  @author Joshua Bergthold
 *  @author Brayden Hambright
 */
public class LitSolution implements Comparable<LitSolution> {

    /**
     * Value of the literal
     */
    public Integer literal;

    /**
     * Clause that is the reason this literal was added
     */
    Integer[] reason;

    /**
     * Creates a LitSolution
     * @param literal Literal value
     * @param reason Reason clause for literal
     */
    public LitSolution(Integer literal, Integer[] reason){
                this.literal = literal;
                this.reason = reason;
    }

    /**
     * Creates a LitSolution without a reason
     * @param literal Literal value
     */
    public LitSolution(Integer literal){
        this(literal,null);
    }

    /**
     * Create a reason-less literal representing the negation of this  one
     * @return Negated literal
     */
    public LitSolution negation(){
        return new LitSolution(-literal, null);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LitSolution e){
            return Objects.equals(e.literal, this.literal);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(literal);
    }

    @Override
    public String toString(){
        return literal.toString();
    }

    @Override
    public int compareTo(LitSolution o) {
            return Math.abs(this.literal) - Math.abs(o.literal);
    }
}
