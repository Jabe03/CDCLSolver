package FirstAttempt;

import java.util.Objects;

public class LitSolution {

    Integer literal;
    Integer[] reason;

    public LitSolution(Integer literal, Integer[] reason){
                this.literal = literal;
                this.reason = reason;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LitSolution e){
            return Objects.equals(e.literal, this.literal);
        }
        return false;
    }

    @Override
    public String toString(){
        return literal.toString();
    }
}
