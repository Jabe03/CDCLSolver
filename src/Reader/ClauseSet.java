package Reader;

import FirstAttempt.CNFSolution;
import FirstAttempt.LitSolution;

import java.sql.Array;
import java.util.*;

public class ClauseSet {
    public static ClauseSet set; //TODO: delete
    private List<Integer[]> clauses;

    private Set<List<Integer>> clauseSet;
    private final int numLiterals;
    public ClauseSet(List<Integer[]> clauses, int numLiterals){
        set = this; //TODO:  delete
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


//    public void removeAllClausesWithLiteral(Integer lit){
//        for(int i = clauseSet.size()-1; i >= 0; i--){
//            if(List.of(clauses.get(i)).contains(lit)){
//                removeClause(i);
//            }
//        }
//    }
//
//    public void removeClause(int index){
//        Integer[] clauseToBeRemoved = clauses.get(index);
//        clauses.remove(index);
//        clauseSet.remove(new ArrayList<>(List.of(clauseToBeRemoved)));
//    }
    public void addClause(List<Integer> addedClause){
        if(addedClause.size() ==1){
            //System.out.println("adding " + addedClause + " to the set");
        }
        Integer[] clause = addedClause.toArray(new Integer[0]);
        clauseSet.add(addedClause);
        clauses.add(clause);
    }

    public int assignmentSatisfiesClauseSet(CNFSolution sol){//returns true if the solution has been found , false otherwise

        for (int i = 0; i < clauses.size(); i++) {
            Integer[] clause = clauses.get(i);
            List<Integer> listClause = Arrays.asList(clause);

            boolean isSatisfied = false;
            for (LitSolution solvedLit : sol) {
                if (listClause.contains(solvedLit.literal)) {
                    isSatisfied = true;
                    break;
                }
            }
            if (!isSatisfied) {
                return i;
            }
        }
        return -1;
    }


    public Integer[] hasUnsatisfiableClausesWith(CNFSolution  sol){
        //boolean result = false;
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
                return clause.toArray(new Integer[0]);
                //result = true;
            }
        }
        return null;
    }
}
