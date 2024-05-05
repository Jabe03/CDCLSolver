package Reader;

import java.util.*;

public class ClauseSet {

    private List<Integer[]> clauses;
    private final int numLiterals;
    public ClauseSet(List<Integer[]> clauses, int numLiterals){

        this.clauses = clauses;
        this.numLiterals = numLiterals;
    }


    public Integer[] getClause(Integer clause) {
        return clauses.get(clause);
    }

    public Integer[] getLastClause() {
        return clauses.get(clauses.size()-1);
    }

    public int getNumLiterals() {
        return numLiterals;
    }

    public int getNumClauses() {
        return clauses.size();
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
    public boolean containsClause(List<Integer> list){
        Set<Integer> set1 = new HashSet<>(list);
        for(Integer[] clause: clauses){
            HashSet<Integer> set2 = new HashSet<>(List.of(clause));
            if(set2.equals(set1)){
                return true;
            }
        }
        return false;
    }
    public String toLongString(){
        return "Reader.ClauseSet numvars=" + numLiterals +"; clauses=" + toStringArrayListWithArrays(clauses);
    }
    @Override
    public String toString(){
        return "Reader.ClauseSet numvars=" + numLiterals + " numclauses=" + clauses.size();
    }

    public List<Integer[]> getClauses() {
        return clauses;
    }

    public void addClause(List<Integer> addedClause){
        clauses.add(addedClause.toArray(new Integer[addedClause.size()]));
    }
}
