package FirstAttempt;

import Reader.ClauseSet;

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
    private ArrayList<Integer> reasonQueue;



    //private boolean[] solvedClauses;

    public CNFSolver(){
        this.solvedLits = new CNFSolution();
        reasonQueue = new ArrayList<>();
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
        for(Integer lit: watchedList.getPureLiterals() ){
            propagateQueue.add(lit);
            reasonQueue.add(-1);
        }
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
                Integer reasonIndex = reasonQueue.remove(reasonQueue.size()-1);
                if(solvedLits.contains(-litToBePropagated)){//if complement of literal is within solution, fail. Do not propagate
                    System.out.println("Failing becuase trying to prpagate " + litToBePropagated + " but it appears in M:" + solvedLits);
                    System.out.println(reasonsForLiterals);
                    System.out.println(propagateQueue);
                    fail(reasonsForLiterals.get(litToBePropagated)); //TODO: this is a challenge (???)
                    continue;
                }
                if(solvedLits.contains(litToBePropagated)){//if literal is already in solution, do not propagate
                    continue;
                }
                //System.out.println(watchedList);
                propagate(litToBePropagated, reasonIndex);//propagate the literal
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
                reasonQueue.add(-1);
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
            clearQueue();
            propagateQueue.add(solvedLits.getLastOfLastDecisionLevel());
            reasonQueue.add(-1);
        } else {
            List<Integer> originalConflict = Arrays.asList(cs.getClause(wrongClause));
            System.out.println("Found conflict with clause " + wrongClause + ": " + originalConflict);
            List<Integer> conflictClause = explain(originalConflict);
            System.out.println("Explained clause " + originalConflict + "-->" + conflictClause);
            int backJumpLevel = solvedLits.getSecondHighestDLinClause(conflictClause);
            //int reasonClauseIndex = cs.getNumClauses();
            int literal = solvedLits.getHighestLiteral(conflictClause);
            System.out.println("Conflict clause=" + conflictClause + " backjumplevel=" + backJumpLevel + " literal=" + literal + " " + solvedLits);
            List<Integer> removed = solvedLits.backjump(-literal, backJumpLevel);

            for (Integer lit : removed) {
                Integer litRemoved = reasonsForLiterals.remove(lit);
                if(litRemoved != null){
                }
            }
            reasonsForLiterals.put(literal, cs.getNumClauses());
            cs.addClause(conflictClause);

            //
            clearQueue();
            propagateQueue.add(solvedLits.getLastOfLastDecisionLevel());
            reasonQueue.add(-1);
        }
        System.out.println("after backtrack: " + solvedLits + " with propagation queue: " + propagateQueue);
    }
    private void propagate(Integer litToBePropagated, int reasonIndex){//propagate a literal
        solvedLits.addToLastDecisionLevel(litToBePropagated);
        if(reasonIndex != -1) {
            reasonsForLiterals.put(litToBePropagated, reasonIndex);
        }
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
                   if(potentialLitsToPropagate.size() !=0 && !solvedLits.contains(potentialLitsToPropagate.get(0))){
                       int lit = potentialLitsToPropagate.get(0);


                       propagateQueue.add(lit);
                       reasonQueue.add(clauseIndex);
                   }

                }
            }
            if(changesMade) {//if a watched literal switches, changed watchedlist to represent that
                watchedList.removeWatched(clauseIndex, -litToBePropagated);
                watchedList.addWatched(clauseIndex, newLitToBeWatched);
            }
        }

    }

    private void clearQueue(){
        for(Integer lit: propagateQueue){
            reasonsForLiterals.remove(lit);
        }

        propagateQueue.clear();
    }

    private List<Integer> explain(List<Integer> conflictClause){
        List<Integer> newConflict = new ArrayList<>(conflictClause);
        List<Integer> highestDLList = solvedLits.getDecisionLevel(solvedLits.getHighestDecisionLevel());
        while(countNumSimilarities(newConflict, highestDLList) >1){
            Integer resolveLiteral = getLitInHighestDL(newConflict);
            List<Integer> reasonClause =Arrays.asList(cs.getClause(reasonsForLiterals.get(-resolveLiteral)));
            newConflict = mergeClauses(newConflict, reasonClause, resolveLiteral);
        }
        return newConflict;
    }

    public Integer getLitInHighestDL(List<Integer> clause){
        List<Integer> highestDLList = new ArrayList<>(solvedLits.getDecisionLevel(solvedLits.getHighestDecisionLevel()));
        highestDLList.remove(0);
        for(Integer lit: clause){
            if(highestDLList.contains(lit) || highestDLList.contains(-lit)){
                return lit;
            }
        }
        throw new RuntimeException("no lit found in highest DL that wasn't the decision literal");
    }
    public static Integer countNumSimilarities(List<Integer> list1, List<Integer> list2){
        List<Integer> result = new ArrayList<>(list1);
        result.retainAll(list2);
        return result.size();
    }
    /*
    private List<Integer> explain(List<Integer> falsifiedClause){
        List<Integer> newConflict = falsifiedClause;
        if(solvedLits.totalInHighestDL(newConflict) == 1){
            return newConflict;
        }
        Integer falsifiedLiteral = getHighestLiteralWithReason(newConflict);
        int count = 0;
        while(solvedLits.totalInHighestDL(newConflict) > 1){
            if(count > 200){
                return newConflict;
            }
            Integer lit = reasonsForLiterals.get(-falsifiedLiteral);
            List<Integer> antiClause = Arrays.asList(cs.getClause(lit));
            newConflict = mergeClauses(newConflict, antiClause, falsifiedLiteral);

            falsifiedLiteral = getHighestLiteralWithReason(newConflict);
            count++;

        }
        System.out.println("done explaining with result " + newConflict + "\n");
        return newConflict;
    }*/

    private Integer getHighestLiteralWithReason(List<Integer> clause) {
        List<Integer> temp = new ArrayList<>(clause);


        Integer falsifiedLiteral = 0;

            falsifiedLiteral = solvedLits.getHighestLiteral(temp);
            Integer lastDecision = solvedLits.getLastDecision();
        if(Objects.equals(-lastDecision, falsifiedLiteral)){
            temp.remove(Integer.valueOf(-lastDecision));
            return solvedLits.getHighestLiteral(temp);
        }

        return falsifiedLiteral;
    }

    /**
     *
     * @param clauseOne contains lit
     * @param clauseTwo contains -lit
     * @param lit literal to be resolved around
     * @return
     */
    public static ArrayList<Integer> mergeClauses(List<Integer> clauseOne, List<Integer> clauseTwo, int lit){
        //System.out.println("merging" + clauseOne + " " + clauseTwo + " resolving literal=" + lit);
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

