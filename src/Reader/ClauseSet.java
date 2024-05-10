package Reader;

import Solver.CNFSolution;
import Solver.LitSolution;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Data structure for keeping the contents of the clause set
 *
 * @author Joshua Bergthold
 * @author Brayden Hambright
 *
 */
public class ClauseSet {
    private final List<Integer[]> clauses;

    private final Set<List<Integer>> clauseSet;
    private final int numLiterals;
    public ClauseSet(List<Integer[]> clauses, int numLiterals){
        this.clauses = clauses;
        this.numLiterals = numLiterals;
        clauseSet = new HashSet<>();
        for(Integer[] clause: clauses){
            clauseSet.add(List.of(clause));
        }

    }

    public Integer[] getClause(Integer clause) {
        return clauses.get(clause);
    }

    public int getNumLiterals() {
        return numLiterals;
    }

    public int getNumClauses() {
        return clauses.size();
    }


    public boolean containsClause(List<Integer> list){
        return clauseSet.contains(list);
    }

    @Override
    public String toString(){
        return "ClauseSet num vars=" + numLiterals + " num clauses=" + clauses.size();
    }

    public List<Integer[]> getClauses() {
        return clauses;
    }


public void addClause(List<Integer> addedClause){
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


}
