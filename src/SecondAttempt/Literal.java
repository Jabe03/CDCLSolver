package SecondAttempt;

import java.util.Objects;

public class Literal {
    Integer value;

    public Literal(Integer value){
        this.value = value;
    }

    public Literal getNegation(){
        return new Literal(-value);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return Objects.equals(value, literal.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
