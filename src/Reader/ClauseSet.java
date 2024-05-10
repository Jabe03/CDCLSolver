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
    /**
     * Stores clauses within ClauseSet
     */
    private final List<Integer[]> clauses;
    /**
     * same as clauses, used for constant-time contains
     */
    private final Set<List<Integer>> clauseSet;
    /**
     * Contains number of literals within ClauseSet
     */
    private final int numLiterals;

    /**
     * Initialize ClauseSet
     * @param clauses clauses to add to ClauseSet
     * @param numLiterals number of literals in ClauseSet
     */
    public ClauseSet(List<Integer[]> clauses, int numLiterals){
        this.clauses = clauses;
        this.numLiterals = numLiterals;
        clauseSet = new HashSet<>();
        for(Integer[] clause: clauses){
            clauseSet.add(List.of(clause));
        }

    }

    /**
     * @param clauseIndex clauseIndex to get clause from
     * @return clause located at clauseIndex
     */
    public Integer[] getClause(Integer clauseIndex) {
        return clauses.get(clauseIndex);
    }

    /**
     * @return number of literals in ClauseSet
     */
    public int getNumLiterals() {
        return numLiterals;
    }
    /**
     * @return number of clauses in ClauseSet
     */
    public int getNumClauses() {
        return clauses.size();
    }

    /**
     * Checks if ClauseSet contains a specific clause
     * @param clause clause to check ClauseSet for
     * @return True if clause already exists in ClauseSet, false if not
     */
    public boolean containsClause(List<Integer> clause){
        return clauseSet.contains(clause);
    }

    /**
     * @return Clauses in ClauseSet
     */
    public List<Integer[]> getClauses() {
        return clauses;
    }

    /**
     * Adds a clause to ClauseSet
     * @param addedClause Clause to add
     */
    public void addClause(List<Integer> addedClause){
        Integer[] clause = addedClause.toArray(new Integer[0]);
        clauseSet.add(addedClause);
        clauses.add(clause);
    }

    /**
     * Checks if the ClauseSet is satisfied by a solution.
     * @param sol CNFSolution to check
     * @return Index of clause which is falsified by current sol. Return -1 if no such clause exists
     */
    public int assignmentSatisfiesClauseSet(CNFSolution sol){

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
