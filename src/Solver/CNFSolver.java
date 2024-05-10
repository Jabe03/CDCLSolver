package Solver;

import Reader.ClauseSet;

import java.util.*;


/**
 * CDCL-style solver for boolean satisfiability problems
 * @author Joshua Bergthold
 * @author Brayden Hambright
 */
public class CNFSolver {
    /**
     * Time, in milliseconds, that the solver is allowed to run for in a single execution of solve().
     * Times out and returns with undecided if this limit is exceeded.
     */
    public static long TIMEOUT = 60 * 1000L;


    /**
     * Start time of the solving algorithm execution
     */
    private long startTime;
    /**
     * Time, in milliseconds, that the solver is allowed to run before preforming an internal restart.
     * Cycles through MIN_RESTART_TIME and MAX_RESATRT_TIME.
     * Starts at MIN_RESTART_TIME and gets 50% each restart until hitting the MAX_RESTART_TIME, then resets back down to MIN_RESTART_TIME.
     */
    private long RESTART_TIME_MILLIS = MIN_RESTART_TIME;

    /**
     * Maximum time allotted for restarts.
     */
    private static final long MAX_RESTART_TIME = 2000;
    /**
     * Minimum time allotted for restarts (also the starting time).
     */
    private static final long MIN_RESTART_TIME = 25;
    /**
     * Last time the solver preformed a restart
     */
    private long lastRestart;
    /**
     * Different decision types that were tested in development.
     */
    private static final String[] DECISION_TYPES = new String[]{"most_positive_occurrences", "most_negative_occurrences", "lowest_num", "random_selection", "random_from_shortest_literal"};
    /**
     * The choice of which decision type to use.
     */
    private static final String DECISION_TYPE = DECISION_TYPES[3];
    /**
     * The current partial assignment M.
     */
    private CNFSolution solvedLits;

    /**
     * The current clause set that is being solved for satisfiability.
     */
    private ClauseSet cs;

    /**
     * Two-watched-literals scheme propagation queue
     */
    private final ArrayList<LitSolution> propagateQueue;
    /**
     * Two-watched-literals scheme data structure
     */
    private WatchedList watchedList;

    /**
     * Random object used for generating decisions
     */
    private final Random r;

    /**
     * Creates a CNFSolver object. Before solving, you must assign the clause set with setClauseSet() as well
     */
    public CNFSolver(){
        propagateQueue = new ArrayList<>();
        r = new Random();
        startTime = -1;
    }

    /**
     * Primes the Solver to solve a clause set
     * @param cs Clause set to be solved
     */
    public void setClauseSet(ClauseSet cs){
        this.cs = cs;

        this.solvedLits = new CNFSolution();
    }

    /**
     * @return The current solution to the clause set
     */
    public CNFSolution getSolution(){
        return solvedLits;
    }


    /**
     * Solves the clause set for its satisfiability or unsatisfiability
     * Solution is acquired from getSolution() after solve() terminates
     */
    public void solve(){//main function to solve
        if(this.cs == null){
            throw new RuntimeException("Clause set was not initialized before solving");
        }
        if(startTime == -1){ //If first call to start, this is the starting time of the algorithm
            startTime = System.currentTimeMillis();
        }
        lastRestart = System.currentTimeMillis(); // save this time for calculating the restart

        this.watchedList = new WatchedList(cs); // initialize Two-watched-literals list
        propagateQueue.addAll(solvedLits.getDecisionLevel(0)); // if M already has something in first decision level, propagate it
        List<LitSolution> pureLits = watchedList.getPureLiterals();
        addAndPropagate(pureLits); // propagate all pure literals in clause set

        if (watchedList.isEmpty()){//checks for empty clause case
            solvedLits.setSatisfiability(false);
        }

        //Runtime loop
        while(!solvedLits.isSolved()){//while the solution isn't found...
            if(checkTimeout()){
                return;
            }

            while(!propagateQueue.isEmpty()) {//propagate if there are literals we can propagate
                Integer[] wrongClause = propagateNextLitInQueue();
                if(wrongClause != null){ //if there was a wrong clause, fail because of that clause
                    fail(wrongClause);
                }
            }
            //if the propagate queue is empty, make a decision
            interpretDecision(decide());
            restartIfApplicable();
        }
    }

    private boolean checkTimeout(){
        long now = System.currentTimeMillis();

        //see if we have timed out
        return now - startTime > TIMEOUT;
    }
    private void interpretDecision(Integer decision){
        if (decision == null) {//if there is no decision to be made (there is a full assignment
            int wrongClause = cs.assignmentSatisfiesClauseSet(solvedLits);
            if (wrongClause == -1) {//if no clause was wrong in clause set, then it is satisfied
                solvedLits.setSatisfiability(true);
                return;
            }
            fail(cs.getClause(wrongClause));

        } else {
            solvedLits.addDecisionLevel();
            addAndPropagate(new LitSolution(decision, null));
        }
    }
    public void addAndPropagate(Collection<LitSolution> lits){
        for (LitSolution lit : lits) {
            addAndPropagate(lit);
        }
    }
    public void addAndPropagate(LitSolution lit){
        solvedLits.addToLastDecisionLevel(lit);
        if(!propagateQueue.contains(lit)) {
            propagateQueue.add(lit);
        }
    }
    public static <E> E getADuplicate(List<E> stuff){
        for(int i = 0; i < stuff.size(); i++){
            for(int j = i + 1; j < stuff.size(); j++){
                if(stuff.get(i).equals(stuff.get(j))){
                    return stuff.get(i);
                }
            }
        }
        return null;
    }
    public Integer decide(String decisionProcedure){
        Integer decision = 0;
        switch (decisionProcedure) {
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
                    if (i != 0) {
                        int occurrences = watchedList.getClausesWithWatchedLit(i).size();
                        if (occurrences > highestOccurrence && !solvedLits.contains(i) && !solvedLits.contains(-i)) {
                            highestOccurrence = occurrences;
                            decision = i;
                        }
                    }
                }
                return decision == 0 ? null : -decision;
            }
            case "most_positive_occurrences" -> {//decide the value which occurs the most in current watched literals
                int highestOccurrence = 0;
                for (int i = -cs.getNumLiterals(); i < cs.getNumLiterals(); i++) {
                    if (i != 0){
                        int occurrences = watchedList.getClausesWithWatchedLit(i).size();
                        if (occurrences > highestOccurrence && !solvedLits.contains(i) && !solvedLits.contains(-i)) {
                            highestOccurrence = occurrences;
                            decision = i;
                        }
                    }
                }
                return decision == 0 ? null : decision;
            }
            case "random_selection" ->{
                Set<LitSolution> litsInSol = solvedLits.getLitsInSol();
                ArrayList<Integer> allLits = new ArrayList<>(2*cs.getNumLiterals());
                for(int i = 1; i <= cs.getNumLiterals(); i++){
                    LitSolution lit = new LitSolution(i);
                    if(!litsInSol.contains(lit) && !litsInSol.contains(lit.negation())){
                        allLits.add(i);
                        allLits.add(-i);
                    }
                }
                if(allLits.size() == 0){
                    if(solvedLits.length() != cs.getNumLiterals()){
                        LitSolution duplicate = getADuplicate(new ArrayList<>(solvedLits.getMergedSol()));
                        assert(duplicate != null);
                    }
                    return null;
                }
                decision = allLits.get((int)(r.nextDouble()*allLits.size()));

                return decision;
            }
            case "random_from_shortest_literal" ->{
                int shortestLength = Integer.MAX_VALUE;
                Integer[] shortestClause = null;
                for(Integer[] clause : cs.getClauses()){
                    if(clause.length < shortestLength && !solvedLits.satisfies(clause)){
                        shortestLength = clause.length;
                        shortestClause = clause;
                    }
                }
                if(shortestClause == null){
                    return null;
                }
                List<Integer> shortest = new ArrayList<>(Arrays.asList(shortestClause));
                for(int i = shortest.size()-1; i >= 0; i--){
                    Integer lit = shortest.get(i);
                    if(solvedLits.contains(lit) || solvedLits.contains(-lit)){
                        shortest.remove(i);
                    }
                }
                if(shortest.isEmpty()){
                    return null;
                }
                return shortest.get((int)(r.nextDouble()*shortest.size()));
            }
            default -> throw new RuntimeException("Unsupported decision procedure: " + DECISION_TYPE);
        }

    }
    public Integer decide(){//Pick which value it is we want to guess/decide
            return decide(DECISION_TYPE);
    }



    private void fail(Integer[] wrongClause){//Clear propagate queue if failed and backtrack
            List<Integer> originalConflict = Arrays.asList(wrongClause);
            List<Integer> conflictClause = explain(originalConflict);
            if(conflictClause == null){
                solvedLits.setSatisfiability(false);
                return;
            }
            int backJumpLevel = solvedLits.getSecondHighestDLinClause(conflictClause);
            if(backJumpLevel == -1){
                solvedLits.setSatisfiability(false);
                return;
            }
            int literal = solvedLits.getHighestLiteralOf(conflictClause);
            solvedLits.backjump(-literal, conflictClause.toArray(new Integer[0]), backJumpLevel);
            clearQueue();
            addAndPropagate(solvedLits.getLastOfLastDecisionLevel());
            addClause(conflictClause);
    }

    private void addClause(List<Integer> clause){
        if(cs.containsClause(clause)){
            return;
        }

        int ADDED_CLAUSE_MAX_SIZE = 9999;
        if(clause.size() > ADDED_CLAUSE_MAX_SIZE){
            return;
        }
        cs.addClause(clause);
        List<Integer> watchedLits = new ArrayList<>();
        int i = 0;
        while(watchedLits.size() < 2){
            Integer lit = clause.get(i);
            if(!solvedLits.contains(-lit)){
                watchedLits.add(lit);
            }
            i++;
            if(i >= clause.size()){
                break;
            }
        }
        watchedList.addNewWatched(watchedLits);
    }

    private Integer[] propagateNextLitInQueue(){
        LitSolution litToBePropagated = propagateQueue.remove(propagateQueue.size()-1);// pop form the queue
        Integer[] wrongClause = null;
        if(solvedLits.contains(litToBePropagated.negation())) {//if complement of literal is within solution, fail. Do not propagate
            return litToBePropagated.reason;
        }
        for(Integer clauseIndex: new ArrayList<>(watchedList.getClausesWithWatchedLit(-litToBePropagated.literal))){
            Integer[] clauseToReselect = cs.getClause(clauseIndex);
            int newLitToBeWatched = -1;
            boolean wasReselected = false;
            for (Integer lit : clauseToReselect) {
                if (!watchedList.contains(clauseIndex, lit) && !solvedLits.contains(-lit)) {
                    newLitToBeWatched = lit;
                    wasReselected = true;
                    break;
                }
            }
            if(!wasReselected){
                List<Integer> lits = new ArrayList<>(watchedList.getWatchedLitsInClause(clauseIndex));

                if(lits.size() == 2){
                    Integer lit1 = lits.get(0);
                    Integer lit2 = lits.get(1);
                    if(!solvedLits.contains(lit1) && !solvedLits.contains(-lit1)){ // if lit1 was unassigned, then this clause is unit
                        addAndPropagate(new LitSolution(lit1, clauseToReselect));
                    } else if(!solvedLits.contains(lit2) && !solvedLits.contains(-lit2)){
                        addAndPropagate(new LitSolution(lit2, clauseToReselect));
                    } else if(solvedLits.contains(-lit1) && solvedLits.contains(-lit2)){
                        wrongClause = clauseToReselect;
                    }
                } else {
                    if(lits.size() == 1){
                        if(solvedLits.contains(-lits.get(0))){
                            wrongClause = clauseToReselect;
                        }
                    }else {
                        throw new RuntimeException("wtf");
                    }
                }
            } else {
                watchedList.removeWatched(clauseIndex, -litToBePropagated.literal);
                watchedList.addWatched(clauseIndex, newLitToBeWatched);
            }
        }
        return wrongClause;
    }

    private void clearQueue(){
        propagateQueue.clear();
    }

    private List<Integer> explain(List<Integer> conflictClause){
        List<Integer> newConflict = new ArrayList<>(conflictClause);
        int highestDLInConflict = solvedLits.getDLof(new LitSolution(solvedLits.getHighestLiteralOf(newConflict)));
        List<Integer> highestDLList = new ArrayList<>();
        for(LitSolution lit: solvedLits.getDecisionLevel(highestDLInConflict)){
            highestDLList.add(lit.literal);
        }
        while(countNumSimilarities(newConflict, highestDLList) >1 /*&& count < 50*/){
            Integer resolveLiteral = getLitFromClauseInDL(newConflict, highestDLInConflict);
            Integer[] reason = solvedLits.getReasonFor(-resolveLiteral);
            if(reason == null){
                return newConflict;
            }
            List<Integer> reasonClause =Arrays.asList(reason);
            newConflict = mergeClauses(newConflict, reasonClause, resolveLiteral);

        }
        return newConflict;
    }

    private Integer getLitFromClauseInDL(List<Integer> newConflict, int dl) {
        List<LitSolution> highestDLList = new ArrayList<>(solvedLits.getDecisionLevel(dl));
        highestDLList.remove(0);
        for(Integer lit: newConflict){
            if(highestDLList.contains(new LitSolution(-lit))){
                return lit;
            }
        }
        throw new RuntimeException("no lit found in highest DL that wasn't the decision literal");
    }

    public static Integer countNumSimilarities(List<Integer> list1, List<Integer> list2) {
        int count = 0;
        for (Integer num : list1) {
            if (list2.contains(-num)) {
                count++;
            }
        }
        return count;
    }

    /**
     *
     * @param clauseOne contains lit
     * @param clauseTwo contains -lit
     * @param lit literal to be resolved around
     */
    public static ArrayList<Integer> mergeClauses(List<Integer> clauseOne, List<Integer> clauseTwo, int lit){
        Set<Integer> result = new HashSet<>();
        ArrayList<Integer> clause1 = new ArrayList<>(clauseOne);
        ArrayList<Integer> clause2 = new ArrayList<>(clauseTwo);
        clause1.remove(Integer.valueOf(lit));
        clause2.remove(Integer.valueOf(-lit));
        result.addAll(clause1);
        result.addAll(clause2);
        return new ArrayList<>(result);

    }
    private void restartIfApplicable(){
        if(System.currentTimeMillis() - lastRestart <= RESTART_TIME_MILLIS){ //see if we meet restart requirements
            return;
        }
        clearQueue();
        List<LitSolution> firstLevel = solvedLits.getDecisionLevel(0);
        RESTART_TIME_MILLIS *= 1.5;
        if(RESTART_TIME_MILLIS >= MAX_RESTART_TIME){
            RESTART_TIME_MILLIS = MIN_RESTART_TIME;
        }
        solvedLits = new CNFSolution(new ArrayList<>(firstLevel));
        solvedLits = new CNFSolution(firstLevel);
        this.watchedList = new WatchedList(cs);
        solve();
    }

}

