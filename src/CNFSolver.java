import java.util.*;

import static java.lang.Math.max;

public class CNFSolver {

    public static long TIMEOUT = 5000L;
    private static final String[] DECISION_TYPES = new String[]{"most_positive_occurrences", "most_negative_occurrences", "lowest_num"};
    private static final String DECISION_TYPE = DECISION_TYPES[2];
    //private static final String DECISION_TYPE = "lowest_num";
    private final CNFSolution solvedLits;
    private ClauseSet cs;
    private WatchedList watchedList;

    private  ArrayList<Integer> propagateQueue;



    //private boolean[] solvedClauses;

    public CNFSolver(){
        this.solvedLits = new CNFSolution();
        propagateQueue  = new ArrayList<>();
    }

    public void setClauseSet(ClauseSet cs){
        this.cs = cs;
        this.watchedList = new WatchedList(cs);
    }

    public CNFSolution getSolution(){
        return solvedLits;
    }

    private int assignmentSatisfiesClauseSet(){//returns true if the solution has been found , false otherwise
        List<Integer[]> clauses = cs.getClauses();
        for (int i = 0; i < clauses.size(); i++) {
            Integer[] clause = clauses.get(i);
            List<Integer> listClause = Arrays.asList(clause);

            boolean isSatisfied = false;
            for (Integer solvedLit : solvedLits) {
                if (listClause.contains(solvedLit)) {
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

    private Map<Integer, Integer> reasonsForLiterals;

    public void solve(){//main function to solve
        reasonsForLiterals = new HashMap<>();
        long last = System.currentTimeMillis();
        long start = last;
        int numPropagations = 0;
        int numDecisions = 0;
        if(this.cs == null){
            throw new RuntimeException("YOU SUCK YOU DIDNT USE THIS LIKE YOU SHOULD HAVE");
        }
        propagateQueue.addAll(watchedList.getPureLiterals());
        if (watchedList.isEmpty()){//checks for empty case

            solvedLits.setSatisfiability(false);
        }
        while(!solvedLits.isSolved()){//while the solution isn't found...

            long now = System.currentTimeMillis();
            if(now -start > TIMEOUT ){
                return;
            }
            if(now - last > 2500){
                //System.out.println("Decisions: " + numDecisions +", Propagations: " + numPropagations+ ", Propagations per decision: " + String.format("%.2f", ((double)numPropagations)/numDecisions));
                last = now;
            }
            if(!propagateQueue.isEmpty()) {//propagate if there are literals we can propagate
                Integer litToBePropagated = propagateQueue.remove(propagateQueue.size()-1);//

                if(solvedLits.contains(-litToBePropagated)){//if complement of literal is within solution, fail. Do not propagate
                    System.out.println("Failing becuase propagating literal whose negation appears in M");
                    fail(reasonsForLiterals.get(litToBePropagated)); //TODO: this is a challenge (???)
                    continue;
                }
                if(solvedLits.contains(litToBePropagated)){//if literal is already in solution, do not propagate
                    continue;
                }
                //System.out.println(watchedList);
                propagate(litToBePropagated);//propagate the literal
                numPropagations++;
                //System.out.println("After propagating " + litToBePropagated +":\n" + watchedList);
            } else {//if the propagate queue is empty, make a decision
                Integer decision = decide();
                if (decision == null) {//if there is no decision to be made
                    int wrongClause  =assignmentSatisfiesClauseSet();
                    if(wrongClause == -1){//check if the solution is solved, if yes, the cnf is satisfiable
                        solvedLits.setSatisfiability(true);
                        //System.out.println("Decisions: " + numDecisions +", Propagations: " + numPropagations+ ", Propagations per decision: " + String.format("%.2f", ((double)numPropagations)/numDecisions));
                        return;
                    }
                    System.out.println("Failing becuase of no decisions left and non sat " + solvedLits.toString());
                    fail(wrongClause);//if there are no decisions to be made and not all clauses are satisfied, fail
                    continue;
                }
                numDecisions++;
                solvedLits.addDecisionLevel();
                propagateQueue.add(decision);
            }
        }

        //System.out.println("Decisions: " + numDecisions +", Propagations: " + numPropagations+ ", Propagations per decision: " + String.format("%.2f", ((double)numPropagations)/numDecisions));

    }
    public Integer decide(){//Pick which value it is we want to guess/decide
        Integer decision = 0;
        switch (DECISION_TYPE) {
            case "lowest_num" -> {//decide the lowest number first
                decision = 1;
                while (solvedLits.contains(decision) || solvedLits.contains(-decision)) {
                    decision++;
                    if (decision > cs.getNumLiterals()) {
                        return null;
                    }
                }
                return decision;
            }
            case "most_negative_occurrences" -> {//decide the complement of the value which occurs the most in current watched literals

                int highestOccurrence = 0;
                for (int i = -cs.getNumLiterals(); i < cs.getNumLiterals(); i++) {
                    if (i == 0) {
                        continue;
                    }
                    int occurrences = watchedList.getClausesWithWatchedLit(i).size();
                    if (occurrences > highestOccurrence && !solvedLits.contains(i) && !solvedLits.contains(-i)) {
                        highestOccurrence = occurrences;
                        decision = i;
                    }
                }
                return decision == 0 ? null : -decision;
            }
            case "most_positive_occurrences" -> {//decide the value which occurs the most in current watched literals

                int highestOccurrence = 0;
                for (int i = -cs.getNumLiterals(); i < cs.getNumLiterals(); i++) {
                    if (i == 0) {
                        continue;
                    }
                    int occurrences = watchedList.getClausesWithWatchedLit(i).size();
                    if (occurrences > highestOccurrence && !solvedLits.contains(i) && !solvedLits.contains(-i)) {
                        highestOccurrence = occurrences;
                        decision = i;
                    }
                }
                return decision == 0 ? null : decision;
            }
            default -> throw new RuntimeException("Unsupported decision procedure: " + DECISION_TYPE);
        }

    }

    private void fail(Integer wrongClause){//Clear propagate queue if failed and backtrack
        //System.out.println("Backtracking")
        if(wrongClause == null){
            solvedLits.chronologicalBacktrack();
            propagateQueue.clear();
            propagateQueue.add(solvedLits.getLastOfLastDecisionLevel());
        } else {
            List<Integer> conflictClause = explain(Arrays.asList(cs.getClause(wrongClause)));
            int backJumpLevel = solvedLits.getSecondHighestDLinClause(conflictClause);
            cs.addClause(conflictClause);
            //int reasonClauseIndex = cs.getNumClauses();
            int literal = solvedLits.getHighestLiteral(conflictClause);
            System.out.println("Conflict clause=" + conflictClause + " backjumplevel=" + backJumpLevel + " literal=" + literal + " " + solvedLits);
            List<Integer> removed = solvedLits.backjump(-literal, backJumpLevel);
            for (Integer lit : removed) {
                reasonsForLiterals.remove(lit);
            }

            //
            propagateQueue.clear();
            propagateQueue.add(-literal);
        }
        System.out.println("after backjump: " + solvedLits);
    }
    private void propagate(Integer litToBePropagated){//propagate a literal
        solvedLits.addToLastDecisionLevel(litToBePropagated);
        for(Integer clauseIndex: new ArrayList<>(watchedList.getClausesWithWatchedLit(-litToBePropagated))){
            Integer newLitToBeWatched = 0;
            Integer[] clause = cs.getClause(clauseIndex);
            boolean changesMade = false;
            for(int i = 0; i < clause.length; i++){
                newLitToBeWatched = clause[i];
                if(
                        !watchedList.contains(clauseIndex,  newLitToBeWatched) &&
                                !propagateQueue.contains(-newLitToBeWatched) &&
                                !solvedLits.contains(-newLitToBeWatched)
                ){//if the literal isn't already watched in this claues and the complement isn't in the queue or solution, shift watched lit.
                    changesMade = true;
                    break;
                }
                if(i == clause.length - 1){//if no literals are available to be watched, add the other variable watched to the propagate queue
                    ArrayList<Integer> potentialLitsToPropagate = new ArrayList<>(watchedList.getWatchedLitsInClause(clauseIndex));
                    potentialLitsToPropagate.remove(Integer.valueOf(-litToBePropagated));
                   if(potentialLitsToPropagate.size() !=0){
                       int lit = potentialLitsToPropagate.get(0);
                       reasonsForLiterals.put(lit, clauseIndex);
                       propagateQueue.add(lit);
                   }

                }
            }
            if(changesMade) {//if a watched literal switches, changed watchedlist to represent that
                watchedList.removeWatched(clauseIndex, -litToBePropagated);
                watchedList.addWatched(clauseIndex, newLitToBeWatched);
            }
        }

    }
    private List<Integer> explain(List<Integer> falsifiedClause){
        Integer falsifiedLiteral = solvedLits.getHighestLiteral(falsifiedClause);
        List<Integer> newConflict = falsifiedClause;
        while(solvedLits.totalInHighestDL(newConflict) > 1){
            List<Integer> antiClause = Arrays.asList(cs.getClause(reasonsForLiterals.get(-falsifiedLiteral)));
            newConflict = mergeClauses(falsifiedClause, antiClause, falsifiedLiteral);
            falsifiedLiteral = solvedLits.getHighestLiteral(newConflict);
        }
        System.out.println("done explaining with result " + newConflict);
        return newConflict;
    }

    /**
     *
     * @param clauseOne contains lit
     * @param clauseTwo contains -lit
     * @param lit literal to be resolved around
     * @return
     */
    public static ArrayList<Integer> mergeClauses(List<Integer> clauseOne, List<Integer> clauseTwo, int lit){
        System.out.println("merging" + clauseOne + " " + clauseTwo + " resolving literal=" + lit);
        Set<Integer> result = new HashSet<>();
        ArrayList<Integer> clause1 = new ArrayList<>(clauseOne);
        ArrayList<Integer> clause2 = new ArrayList<>(clauseTwo);

        clause1.remove(Integer.valueOf(lit));
        clause2.remove(Integer.valueOf(-lit));

        result.addAll(clause1);
        result.addAll(clause2);

        return new ArrayList<>(result);

        /*
        ArrayList<Integer> finalClause = new ArrayList<>();
        for(int i = 0; i < clauseOne.size(); i++){
            if(!clauseTwo.contains(clauseOne.get(i)) || !clauseTwo.contains(-clauseOne.get(i))){
                finalClause.add(clauseOne.get(i));
            }
        }
        for (int i = 0; i < clauseTwo.size(); i++){
            if (!clauseOne.contains(clauseOne.get(i))){
                finalClause.add(clauseTwo.get(i));
            }
        }
        return finalClause;*/
    }
}

