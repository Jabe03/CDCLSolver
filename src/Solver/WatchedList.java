package Solver;

import Reader.ClauseSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure for tracking which literals we are watching in each clause (two watched literals per clause)
 * @author Joshua Bergthold
 * @author Brayden Hambright
 */
public class WatchedList {
    /**
     * Keeps track of positive watched literals
     */
    ArrayList<Integer>[] positiveWatched;
    /**
     * Keeps track of negative watched literals
     */
    ArrayList<Integer>[] negativeWatched;
    /**
     * Keeps track of which literals are currently watched in each clause
     */
    ArrayList<ArrayList<Integer>> watchedLitsInClauses;
    /**
     * ClauseSet being watched
     */
    ClauseSet cs;

    /**
     * Creates new WatchedList and calls method to assign initial literals watched in each clause
     * @param cs ClauseSet to be watched
     */
    public WatchedList(ClauseSet cs) {
        int numLits = cs.getNumLiterals();
        this.cs = cs;
        positiveWatched = new ArrayList[numLits];
        negativeWatched = new ArrayList[numLits];

        watchedLitsInClauses = new ArrayList<>();
        initPositiveAndNegativeLists(cs);
    }


    /**
     * Initializes arrays in WatchedList and assigns initial watched literals
     * The first two literals in every clause is watched, if a clause has only one literal then it is watched
     * @param cs ClauseSet to assign initial values to
     */
    private void initPositiveAndNegativeLists(ClauseSet cs) {//Put initial values into watched literals
        for (int i = 0; i < cs.getNumClauses(); i++) {
            watchedLitsInClauses.add(new ArrayList<>());
        }
        for (int i = 0; i < positiveWatched.length; i++) {
            positiveWatched[i] = new ArrayList<>();
            negativeWatched[i] = new ArrayList<>();
        }

        List<Integer[]> clauses = cs.getClauses();

        for (int i = 0; i < clauses.size(); i++) {
            Integer[] clause = clauses.get(i);
           if (clause.length == 1) {//if the clause has length 1, add only that literal to watched literals
                addWatched(i, clause[0]);
            } else {//otherwise, add the first two literals to watched lits
                addWatched(i, clause[0]);
                addWatched(i, clause[1]);
            }
        }
    }

    /**
     * @param clauseIndex Index of clause in ClauseSet we want to get lits from
     * @return Which lits are being watched at clauseIndex
     */
    public ArrayList<Integer> getWatchedLitsInClause(int clauseIndex) {
        return watchedLitsInClauses.get(clauseIndex);
    }

    /**
     *
     * @param lit Literal to find clauses watching
     * @return Clauses which currently watch literal lit
     */
    public ArrayList<Integer> getClausesWithWatchedLit(int lit) {
        if (lit > 0) {
            return positiveWatched[lit - 1];
        }
        lit = -lit;
        return negativeWatched[lit - 1];
    }

    /**
     * Adds a literal to watch in a clause. To be used in conjunction with removeWatched
     * To be used in conjunction with addWatched to keep only 2 watched literals
     * @param clauseIndex clauseIndex to add lit to
     * @param lit lit to add to clauseIndex from WatchedList
     */
    public void addWatched(int clauseIndex, int lit) {//when literal is being watched, add it to watched literals
        if (contains(clauseIndex, lit)) {             //and keep track of where it is in clause
            return;
        }
        getClausesWithWatchedLit(lit).add(clauseIndex);
        watchedLitsInClauses.get(clauseIndex).add(lit);
    }

    /**
     * Removes a watched literal in a clause
     * To be used in conjunction with addWatched to keep only 2 watched literals
     * @param clauseIndex clauseIndex to remove lit from
     * @param lit lit to remove from clauseIndex in WatchedList
     */
    public void removeWatched(int clauseIndex, int lit) {//when literal is removed, remove it from watched literals and
        getClausesWithWatchedLit(lit).remove(Integer.valueOf(clauseIndex)); //stop tracking where it is in clause
        watchedLitsInClauses.get(clauseIndex).remove(Integer.valueOf(lit));
    }

    /**
     * Returns all clauses with one watched literal for initial propagation queue
     * @return clauses which have only one watched literal
     */
    public List<LitSolution> getPureLiterals() {//Return clauses which have only one watched literal
        List<LitSolution> pureLits = new ArrayList<>();
        for (int i = 0; i < watchedLitsInClauses.size(); i++) {
            ArrayList<Integer> list = watchedLitsInClauses.get(i);
            if (list.size() == 1) {
                pureLits.add(new LitSolution(list.get(0), cs.getClause(i)));
            }
        }
        return pureLits;
    }

    /**
     * Converts our WatchedList into a string to print for help debugging code
     * @return WatchedList in readable string form
     */
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("[");
        for (int i = 0; i < positiveWatched.length; i++) {
            b.append("(").append(i + 1).append(": ");
            for (int j = 0; j < positiveWatched[i].size(); j++) {
                b.append(positiveWatched[i].get(j) + 1).append(", ");
            }
            b.delete(b.length() - 2, b.length());
            b.append(")");
        }
        b.append("  ");
        for (int i = 0; i < negativeWatched.length; i++) {
            b.append("(").append(-(i + 1)).append(": ");
            for (int j = 0; j < negativeWatched[i].size(); j++) {
                b.append(negativeWatched[i].get(j) + 1).append(", ");
            }
            b.delete(b.length() - 2, b.length());
            b.append(")");
        }
        b.append("]");
        return b.toString();
    }

    /**
     * Checks if a literal is currently being watched in a clause
     * @param clauseIndex clauseIndex of clause we want to check
     * @param watchedLit literal we want to check
     * @return true if currently watching watchedLit in clauseIndex, false if not
     */
    public boolean contains(Integer clauseIndex, Integer watchedLit) {//check if a literal is being watched in a clause

        return getClausesWithWatchedLit(watchedLit).contains(clauseIndex);
    }

    /**
     * Checks if no literals are being watched (clauseSet is empty)
     * @return True if clauseSet is empty, false if not
     */
    public boolean isEmpty() {//checks for empty case (no literals)
        // Check positive watched list
        for (ArrayList<Integer> list : positiveWatched) {
            if (!list.isEmpty()) {
                return false;
            }
        }
        // Check negative watched list
        for (ArrayList<Integer> list : negativeWatched) {
            if (!list.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * When clauses are added, initialize the two watched literals within
     * clauseIndex to add to = last clause added, that being size - 1
     * @param watchedLits Set of two literals to watch
     */
    public void addNewWatched(List<Integer> watchedLits) {
        if(watchedLits.size() > 2){
            throw new RuntimeException("Tried to add an invalid number of literals to watchedList: " + watchedLits);
        }
        if(watchedLits.size() == 0){
            return;
        }
        watchedLitsInClauses.add(new ArrayList<>());
        addWatched(watchedLitsInClauses.size() - 1, watchedLits.get(0));
        if(watchedLits.size() == 2){
            addWatched(watchedLitsInClauses.size()-1, watchedLits.get(1));
        }
    }
}
