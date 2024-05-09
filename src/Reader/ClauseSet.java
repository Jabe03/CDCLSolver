package Reader;

import FirstAttempt.CNFSolution;

import java.util.*;

public class ClauseSet {

    private List<Integer[]> clauses;

    private Set<List<Integer>> clauseSet;
    private final int numLiterals;
    public ClauseSet(List<Integer[]> clauses, int numLiterals){

        this.clauses = clauses;
        this.numLiterals = numLiterals;
        clauseSet = new HashSet<>();
        for(Integer[] clause: clauses){
            clauseSet.add(List.of(clause));
        }

    }

    public Integer indexOf(Integer[] clause){
        return clauses.indexOf(clause);
    }

    public Integer[] getClause(Integer clause) {
        Integer[] result = clauses.get(clause);
        return result;
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
//        for(List<Integer> clause: clauseSet){
//            System.out.println(clause);
//        }
//        System.out.println("Contains " + Arrays.toString(list.toArray(new Integer[0])) + "?");
        return clauseSet.contains(list);
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
        if(addedClause.size() ==1){
            //System.out.println("adding " + addedClause + " to the set");
        }
        Integer[] clause = addedClause.toArray(new Integer[0]);
        clauseSet.add(addedClause);
        clauses.add(clause);
    }

    public boolean hasUnsatisfiableClausesWith(CNFSolution  sol){
        boolean result = false;
        for(List<Integer> clause: clauseSet){
            boolean clauseIsSat = false;
            boolean clauseHasFullAssignment = true;
            for(Integer lit: clause){
                if(sol.contains(lit)){
                    clauseIsSat = true;
                    break;
                } else if(!sol.contains(-lit)){
                    clauseHasFullAssignment = false;
                    break;
                }
            }
            if(clauseHasFullAssignment && !clauseIsSat){
                System.out.println("Clause is is unsatisfiable with current assignment: " + clause);
                result = true;
            }
        }
        return result;
    }
}
