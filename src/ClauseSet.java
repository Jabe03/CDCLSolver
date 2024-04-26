import java.util.Arrays;
import java.util.List;

public class ClauseSet {

    private List<Integer[]> clauses;
    private final int numVariables;
    public ClauseSet(List<Integer[]> clauses, int numVariables){

        this.clauses = clauses;
        this.numVariables = numVariables;
    }

    public int getNumVariables() {
        return numVariables;
    }


    public static <E> String toStringArrayListWithArrays(List<E[]> list){
        if(list == null) {
            return "null";
        }
        StringBuilder b = new StringBuilder();
        b.append("[");
        for(Object[] objects: list){
            b.append(Arrays.toString(objects)).append(", ");
        }
        b.delete(b.length() - 2, b.length());
        b.append("]");

        return b.toString();
    }

    public String toLongString(){
        return "ClauseSet numvars=" + numVariables +"; clauses=" + toStringArrayListWithArrays(clauses);
    }
    @Override
    public String toString(){
        return "ClauseSet numvars=" + numVariables + " numclauses=" + clauses.size();
    }

    public List<Integer[]> getClauses() {
        return clauses;
    }
}
