package FirstAttempt;

import java.util.Arrays;
import java.util.Objects;

public class LitSolution implements Comparable<LitSolution> {

    public Integer literal;
    Integer[] reason;

    public LitSolution(Integer literal, Integer[] reason){
                this.literal = literal;
                this.reason = reason;
    }
    public LitSolution(Integer literal){
        this(literal,null);
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

    public String toLongString(){
        return literal.toString() + Arrays.toString(reason);
    }

    public LitSolution negation(){
        return new LitSolution(-literal, null);
    }

    @Override
    public int compareTo(LitSolution o) {
            return Math.abs(this.literal) - Math.abs(o.literal);
    }
}
