package FirstAttempt;

import Reader.ClauseSet;

import java.sql.SQLOutput;
import java.util.*;

import static java.lang.Math.max;

public class CNFSolver {
    private static final long MIN_RESTART_TIME = 25;
    private static final long MAX_RESTART_TIME = 2000;
    private long RESTART_TIME_MILLIS = MIN_RESTART_TIME;

    private int numSinceLastAddedToLowestPropAfterRestart = 0;
    private int sizeOfLastRestartsFirstDecisionLevel = 0;

    private static int ADDED_CLAUSE_MAX_SIZE = 9999;
    private long NUM_ITERS_UNTIL_RESTART = (long) 10000000;

    private List<LitSolution> lastRestartsDL0;

    private long restartNum = 0;

    private static boolean USE_ITERS = false;
    private long now;

    private long last;
    public static long TIMEOUT = 3000000L;
    private static final String[] DECISION_TYPES = new String[]{"most_positive_occurrences", "most_negative_occurrences", "lowest_num", "random_selection", "random_from_shortest_literal"};
    private static final String DECISION_TYPE = DECISION_TYPES[3];
    private int numBackjumps = 0;
    private int numBacktracks = 0;
    private int levelsBackjumped = 0;
    //private static final String DECISION_TYPE = "lowest_num";
    private CNFSolution solvedLits;
    private ClauseSet cs;
    private WatchedList watchedList;

    private Random r;

    private  ArrayList<LitSolution> propagateQueue;



    //private boolean[] solvedClauses;

    public CNFSolver(){

        propagateQueue  = new ArrayList<>();
        r = new Random();


    }

    public void setClauseSet(ClauseSet cs){
        this.cs = cs;
        this.watchedList = new WatchedList(cs);
        this.solvedLits = new CNFSolution();
    }

    public CNFSolution getSolution(){
        return solvedLits;
    }




    public void solve(){//main function to solve

        //NUM_ITERS_UNTIL_RESTART = NUM_ITERS_UNTIL_RESTART* (long)Math.pow(1.01, cs.getNumLiterals());
        //NUM_ITERS_UNTIL_RESTART = NUM_ITERS_UNTIL_RESTART * cs.getNumLiterals();
        last = System.currentTimeMillis();
        //int numPropagations = 0;
        //int numDecisions = 0;
        long numiters = 0;
        if(this.cs == null){
            throw new RuntimeException();
        }
        List<LitSolution> pureLits = watchedList.getPureLiterals();
        addAndPropagate(pureLits);
        Collections.sort(pureLits);
        System.out.println("Number of pure lits=" + pureLits.size() + ": " + pureLits);
        propagateQueue.addAll(solvedLits.getDecisionLevel(0));
        if (watchedList.isEmpty()){//checks for empty case
            solvedLits.setSatisfiability(false);
        }
        while(!solvedLits.isSolved()){//while the solution isn't found...
            numiters++;
            now = System.currentTimeMillis();
            if(
                    (USE_ITERS && (numiters >= (NUM_ITERS_UNTIL_RESTART)) || (!USE_ITERS && (now - last) > RESTART_TIME_MILLIS))){
                restart();
                return;
            }

            if(!propagateQueue.isEmpty()) {//propagate if there are literals we can propagate
                LitSolution litToBePropagated = propagateQueue.remove(propagateQueue.size()-1);//
                //System.out.println("About to propagate: " + );
                //Integer reasonIndex = reasonQueue.remove(reasonQueue.size()-1);
                if(solvedLits.contains(litToBePropagated.negation())){//if complement of literal is within solution, fail. Do not propagate
                    Integer[] wrongClause =  litToBePropagated.reason;//solvedLits.getReasonFor(litToBePropagated.literal);
                    fail(wrongClause);
                    if(solvedLits.isSolved()){
                        return;
                    }
                } else {
                        Integer[] wrongClause = propagate2(litToBePropagated);
                        if(wrongClause != null){
                            fail(wrongClause);
                        }
                        ;//propagate the literal
                        //System.out.println("After propagating " + litToBePropagated +":\n" + watchedList);

                }
            }else { //if the propagate queue is empty, make a decision
                Integer decision = decide();
                if (decision == null) {//if there is no decision to be made
                    int wrongClause = cs.assignmentSatisfiesClauseSet(solvedLits);
                    if (wrongClause == -1) {//check if the solution is solved, if yes, the cnf is satisfiable
                        solvedLits.setSatisfiability(true);
                        List<LitSolution> firstLevel = solvedLits.getDecisionLevel(0);
                        System.out.println("First level solved (after solving) lits are " + firstLevel.size() + "/" + cs.getNumLiterals() + " num unique lits=" + new HashSet<>(firstLevel).size());

                        //System.out.println("Decisions: " + numDecisions +", Propagations: " + numPropagations+ ", Propagations per decision: " + String.format("%.2f", ((double)numPropagations)/numDecisions));
                        return;
                    }
                    fail(cs.getClause(wrongClause));
                    //fail(null);

                    //System.out.println("Failing becuase of no decisions left and non sat " + solvedLits.toString());
//                    fail(null);//if there are no decisions to be made and not all clauses are satisfied, fail

                } else {
                    solvedLits.addDecisionLevel();
                    addAndPropagate(new LitSolution(decision, null));
                }
            }
        }

        //System.out.println("Decisions: " + numDecisions +", Propagations: " + numPropagations+ ", Propagations per decision: " + String.format("%.2f", ((double)numPropagations)/numDecisions));

    }

    public void addAndPropagate(Collection<LitSolution> lits){
        for (LitSolution lit : lits) {
            addAndPropagate(lit);
        }
    }
    public void addAndPropagate(LitSolution lit){
        solvedLits.addToLastDecisionLevel(lit);
        if(!propagateQueue.contains(lit)) {
            //System.out.println("Added " + lit + " to the propagation queue");
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
                    if (i == 0) {
                        continue;
                    } else {
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
                    if (i == 0) {
                        continue;
                    } else {
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
                    //System.out.println("Decided null with |M|=" + solvedLits.length() + "and numLits= " + cs.getNumLiterals());
                    if(solvedLits.length() != cs.getNumLiterals()){
                        LitSolution duplicate = getADuplicate(new ArrayList<>(solvedLits.getMergedSol()));
                        assert(duplicate != null);
                        //System.out.println( duplicate + " is duplicate in M");
                        //System.out.println("With reason " + cs.indexOf(duplicate.reason) + " -> " + Arrays.toString(duplicate.reason));
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
                //System.out.println("Shortest unsatisfied clause is " + Arrays.toString(shortestClause));
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
//                do{
//                    decision = shortestClause[(int)(Math.random()*shortestLength)];
//                } while (!solvedLits.contains(decision) && !solvedLits.contains(-decision));
//                return decision;
            }
            default -> throw new RuntimeException("Unsupported decision procedure: " + DECISION_TYPE);
        }

    }
    //private List
    public Integer decide(){//Pick which value it is we want to guess/decide

//        if(RESTART_TIME_MILLIS == MAX_RESTART_TIME){
//            //System.out.println("Using shortest clause decision");
//            return decide(DECISION_TYPES[4]);
//        }else{
            return decide(DECISION_TYPE);
//        }

    }



    private void fail(Integer[] wrongClause){//Clear propagate queue if failed and backtrack
        //System.out.println("failing");
        if(wrongClause == null){
            //System.out.println("Null wrong clause" + solvedLits.toStringWithReasons());
            //System.out.println("Cronological backtracking...");
            solvedLits.chronologicalBacktrack();
            clearQueue();
            addAndPropagate(solvedLits.getLastOfLastDecisionLevel());
            numBacktracks++;
            levelsBackjumped++;
        } else {

            //System.out.println("failing: " + Arrays.toString(Thread.currentThread().getStackTrace()));
            List<Integer> originalConflict = Arrays.asList(wrongClause);
            //System.out.println("Found conflict with clause " + wrongClause + ": " + originalConflict);

            //int beforeDL = solvedLits.getHighestDecisionLevel();
            //System.out.println("Solution so  far with reasons: " + solvedLits.toStringWithReasons());
            //System.out.println("Original conflict " + originalConflict);
//            if(allDDLsEqual(originalConflict)){
//                solvedLits.chronologicalBacktrack();
//                System.out.println(solvedLits.toStringWithReasons());
//                pause();
//                return;
//            }
            List<Integer> conflictClause = explain(originalConflict);
            //System.out.println("Result of explaining= " + conflictClause);
            if(conflictClause == null){
                //System.out.println("Conflict is empty, concluding UNSAT");
                solvedLits.setSatisfiability(false);
                return;
            }


            //System.out.println("Explained clause " + originalConflict + "-->" + conflictClause);
            int backJumpLevel = solvedLits.getSecondHighestDLinClause(conflictClause);
            if(backJumpLevel == -1){
                //System.out.println("Original conflict" + originalConflict + " Resolved conflict:" + conflictClause);
                solvedLits.setSatisfiability(false);
                return;
            }
            //int reasonClauseIndex = cs.getNumClauses();
            int literal = solvedLits.getHighestLiteral(conflictClause);

            //System.out.println("Conflict clause=" + conflictClause + " backjumplevel=" + backJumpLevel + " literal=" + literal + " " + solvedLits);
            List<LitSolution> removed = solvedLits.backjump(-literal, conflictClause.toArray(new Integer[0]), backJumpLevel);
            //System.out.println("DL after = " + );
            int afterDL = solvedLits.getHighestDecisionLevel();
            //levelsBackjumped += beforeDL - afterDL;


            clearQueue();
            addAndPropagate(solvedLits.getLastOfLastDecisionLevel());

//            }
            addClause(conflictClause);
            //System.out.println(solvedLits.satisfiability);
            numBackjumps++;
        }
        //System.out.println("Chronological Backtracks=" + numBacktracks + " Backjumps=" + numBackjumps + " avg levels jumped per backtrack=" + (levelsBackjumped/(double)(numBackjumps+numBacktracks)));
    }

    private boolean allDDLsEqual(List<Integer> originalConflict) {
        int firstDL = solvedLits.getDLof(new LitSolution(originalConflict.get(0)));
        for(int i = 1; i < originalConflict.size(); i++){
            if(!(solvedLits.getDLof(new LitSolution(originalConflict.get(i))) == (firstDL))){
                return false;
            }
        }
        return true;
    }

    private boolean addClause(List<Integer> clause){
        //System.out.println("Adding new clause to set...");
        if(cs.containsClause(clause)){
            return false;
        }

        if(clause.size() > ADDED_CLAUSE_MAX_SIZE){
            System.out.println("Skipped " + clause +" because it was too long");
            return false;
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
        if(watchedLits.isEmpty()){
            //solvedLits.setSatisfiability(false);
            //System.out.println("satisfiability is now false");
        }
        watchedList.addNewWatched(watchedLits);
        return true;
        //System.out.println("Added new clause to the set " + (clause.size() >= 10? clause.subList(0,10): clause));
    }

    private Integer[] propagate2(LitSolution litToBePropagated){
        //System.out.println("\nPropagating " + litToBePropagated + ". Watched=" + watchedList);
        //System.out.println("Queue" + propagateQueue + " M:" + solvedLits);
        Integer[] wrongClause = null;
        for(Integer clauseIndex: new ArrayList<>(watchedList.getClausesWithWatchedLit(-litToBePropagated.literal))){
            Integer[] clauseToReselect = cs.getClause(clauseIndex);
            int newLitToBeWatched = -1;
            boolean wasReselected = false;
            for(int i = 0; i < clauseToReselect.length; i++){
                Integer lit = clauseToReselect[i];
                if(!watchedList.contains(clauseIndex, lit) && !solvedLits.contains(-lit)){
                    newLitToBeWatched = lit;
                    wasReselected = true;
                    break;
                }
            }
            //System.out.println("In clause " + Arrays.toString(clauseToReselect) + " reselected was " + wasReselected + "\n");
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
                        //System.out.println(-lit1 + " and " + -lit2 + "were  both in M: " + solvedLits + "This clause must be wrong");
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
        //System.out.println(watchedList);
        //System.out.println("Wrong clause found after propagation is " + Arrays.toString(wrongClause));
        return wrongClause;
    }
    private Integer[] propagate(LitSolution litToBePropagated){//propagate a literal
        if(solvedLits.contains(litToBePropagated)){
            return null;
        }


        for(Integer clauseIndex: new ArrayList<>(watchedList.getClausesWithWatchedLit(-litToBePropagated.literal))){
            Integer newLitToBeWatched = 0;
            Integer[] clause = cs.getClause(clauseIndex);
            boolean changesMade = false;
            for(int i = 0; i < clause.length; i++){
                newLitToBeWatched = clause[i];
                if(
                        !watchedList.contains(clauseIndex,  newLitToBeWatched) &&
                                !propagateQueue.contains(new LitSolution(-newLitToBeWatched)) &&
                                !solvedLits.contains(-newLitToBeWatched)
                ){//if the literal isn't already watched in this claues and the complement isn't in the queue or solution, shift watched lit.
                    changesMade = true;
                    break;
                }
                if(i == clause.length - 1){//if no literals are available to be watched, add the other variable watched to the propagate queue
                    Integer[] wrongClause = cs.hasUnsatisfiableClausesWith(solvedLits);
                    if(wrongClause != null){
                        return wrongClause;
                    }
                    ArrayList<Integer> potentialLitsToPropagate = new ArrayList<>(watchedList.getWatchedLitsInClause(clauseIndex));
                    potentialLitsToPropagate.remove(Integer.valueOf(-litToBePropagated.literal));
                   if(potentialLitsToPropagate.size() !=0 && !solvedLits.contains(potentialLitsToPropagate.get(0))){
                       int lit = potentialLitsToPropagate.get(0);
                       propagateQueue.add(new LitSolution(lit, cs.getClause(clauseIndex)));
                   }

                }
            }
            if(changesMade) {//if a watched literal switches, changed watchedlist to represent that
                watchedList.removeWatched(clauseIndex, -litToBePropagated.literal);
                watchedList.addWatched(clauseIndex, newLitToBeWatched);
            }
        }
        return null;
    }

    private void clearQueue(){
        propagateQueue.clear();
    }

    private List<Integer> explain(List<Integer> conflictClause){
        //System.out.println("prepareing to explain" + conflictClause);

        List<Integer> newConflict = new ArrayList<>(conflictClause);
        int highestDLInConflict = solvedLits.getDLof(new LitSolution(solvedLits.getHighestLiteral(newConflict)));
        //System.out.println("Highest DL in conflict is " + highestDLInConflict);
        List<Integer> highestDLList = new ArrayList<>();
        for(LitSolution lit: solvedLits.getDecisionLevel(highestDLInConflict)){
            highestDLList.add(lit.literal);
        }
        int count = 0;
        while(countNumSimilarities(newConflict, highestDLList) >1 /*&& count < 50*/){
            Integer resolveLiteral = getLitFromClauseInDL(newConflict, highestDLInConflict);
            //System.out.println(resolveLiteral);
            //System.out.println();
            Integer[] reason = solvedLits.getReasonFor(-resolveLiteral);
            //System.out.println("Explaining... " );
            if(reason == null){
                //System.out.println("No reason for " + -resolveLiteral + ", returning " + newConflict);
                return newConflict;
            }
            List<Integer> reasonClause =Arrays.asList(reason);
            newConflict = mergeClauses(newConflict, reasonClause, resolveLiteral);
            count++;

        }
        if(conflictClause.equals(List.of(-1, 848, -451, 453, 854, 783))){

            System.out.println("Result= " + newConflict + " M:" + solvedLits);
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

    public Integer getLitInHighestDL(List<Integer> clause){
        List<LitSolution> highestDLList = new ArrayList<>(solvedLits.getDecisionLevel(solvedLits.getHighestDecisionLevel()));
        highestDLList.remove(0);
        for(Integer lit: clause){
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

    private LitSolution getHighestLiteralWithReason(List<Integer> clause) {
        List<Integer> temp = new ArrayList<>(clause);


        LitSolution falsifiedLiteral =new LitSolution( solvedLits.getHighestLiteral(temp));
        LitSolution lastDecision = solvedLits.getLastDecision();
        if(Objects.equals(lastDecision.negation(), falsifiedLiteral)){
            temp.remove(lastDecision.negation().literal);
            return new LitSolution(solvedLits.getHighestLiteral(temp));
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
    private void restart(){
        restartNum++;
        clearQueue();
        //reasonsForLiterals = new HashMap<>();
        List<LitSolution> firstLevel = solvedLits.getDecisionLevel(0);

        System.out.println("After restart, First level solved lits are " + firstLevel.size() + "/" + cs.getNumLiterals() + " num unique lits=" + new HashSet<>(firstLevel).size() + " With " + new CNFSolution(solvedLits.getDecisionLevel(0)).getMissingLits(cs.getNumLiterals()) + " missing");
        List<LitSolution> sol = new ArrayList<>(solvedLits.getMergedSol());
        Collections.sort(sol);
        System.out.println(sol);
        System.out.println("Duplicates: " + getADuplicate(solvedLits.getMergedSol()));


        sizeOfLastRestartsFirstDecisionLevel = firstLevel.size();
        //System.out.println("Attempting to add these lits to M after restart " + firstLevel);
        boolean clauseAdded  = false;
        //System.out.println();
//        for(LitSolution lit: firstLevel){
//            if(addClause(List.of(lit.literal))){
//                if(lit.literal == -987 ||lit.literal == -986 ||lit.literal == -985 ||lit.literal == -984 ||lit.literal == -987 ||lit.literal == -987 ){
//
//                    System.out.println("Added clause: [" + lit + "] with reason" + Arrays.toString(lit.reason));
//                }
//                clauseAdded = true;
//            }
//
//        }

//        if(true /*numAdded == 0*/){
//            RESTART_TIME_MILLIS += MIN_RESTART_TIME;
//        } else {
//            RESTART_TIME_MILLIS = MIN_RESTART_TIME;
//        }
//        if(RESTART_TIME_MILLIS > MAX_RESTART_TIME){
//            RESTART_TIME_MILLIS = MAX_RESTART_TIME;
//        }
        RESTART_TIME_MILLIS *= 1.5;
        if(RESTART_TIME_MILLIS >= MAX_RESTART_TIME || clauseAdded){
            RESTART_TIME_MILLIS = MIN_RESTART_TIME;
        }
        solvedLits = new CNFSolution(new ArrayList<LitSolution>(firstLevel));
//        List<LitSolution> newLits = solvedLits.getDecisionLevel(0);
//        if(lastRestartsDL0 != null){
//            newLits.retainAll(lastRestartsDL0);
//        }
//        if()
//        for(LitSolution lit: newLits){
//            cs.removeAllClausesWithLiteral(lit.literal);
//        }
        //solvedLits.clear();
        solvedLits = new CNFSolution(firstLevel);
        //solvedLits = new CNFSolution();
        this.watchedList = new WatchedList(cs);
        //lastRestartsDL0 = solvedLits.getDecisionLevel(0);
        solve();
    }

    public static void pause(){
        System.out.println("Paused,  press enter to continue");
        new Scanner(System.in).nextLine();
    }
}

