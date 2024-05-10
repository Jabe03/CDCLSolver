package Solver;

import Reader.ClauseSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joshua Bergthold
 * @author Brayden Hambright
 */
public class WatchedList {//class to keep track of which literals are being watched in each clause (two watched literals)

    ArrayList<Integer>[] positiveWatched;//keeps track of positive watched literals
    ArrayList<Integer>[] negativeWatched;//keeps track of negative watched literals
    ArrayList<ArrayList<Integer>> watchedLitsInClauses;//keeps track of which literals are currently watched in each clause

    ClauseSet cs;

    public WatchedList(ClauseSet cs) {//initializes Watched List
        int numLits = cs.getNumLiterals();
        this.cs = cs;
        positiveWatched = new ArrayList[numLits];
        negativeWatched = new ArrayList[numLits];

        watchedLitsInClauses = new ArrayList<>();
        initPositiveAndNegativeLists(cs);
    }



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

    public ArrayList<Integer> getWatchedLitsInClause(int clauseIndex) {
        return watchedLitsInClauses.get(clauseIndex);
    }

    public ArrayList<Integer> getClausesWithWatchedLit(int lit) {
        if (lit > 0) {
            return positiveWatched[lit - 1];
        }
        lit = -lit;
        return negativeWatched[lit - 1];
    }


    public void addWatched(int clauseindex, int lit) {//when literal is being watched, add it to watched literals
        if (contains(clauseindex, lit)) {             //and keep track of where it is in clause
            return;
        }
        getClausesWithWatchedLit(lit).add(clauseindex);
        watchedLitsInClauses.get(clauseindex).add(lit);
    }

    public void removeWatched(int clauseindex, int lit) {//when literal is removed, remove it from watched literals and
        getClausesWithWatchedLit(lit).remove(Integer.valueOf(clauseindex)); //stop tracking where it is in clause
        watchedLitsInClauses.get(clauseindex).remove(Integer.valueOf(lit));
    }

    public List<LitSolution> getPureLiterals() {//Return ll clauses which have only one watched literal
        List<LitSolution> pureLits = new ArrayList<>();
        for (int i = 0; i < watchedLitsInClauses.size(); i++) {
            ArrayList<Integer> list = watchedLitsInClauses.get(i);
            if (list.size() == 1) {
                pureLits.add(new LitSolution(list.get(0), cs.getClause(i)));
            }
        }
        return pureLits;
    }

    public String toString() {//convert FirstAttempt.WatchedList into something printable for testing
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

    public boolean contains(Integer clauseIndex, Integer watchedLit) {//check if a literal is being watched in a clause

        return getClausesWithWatchedLit(watchedLit).contains(clauseIndex);
    }

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
