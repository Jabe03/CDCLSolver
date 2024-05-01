import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class CNFSolver {

    private final CNFSolution solvedLits;
    private ClauseSet cs;
    private WatchedList watchedList;

    private  ArrayList<Integer> propagateQueue;

    private boolean[] solvedClauses;

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
    public void solve(){
        if(this.cs == null){
            throw new RuntimeException("YOU SUCK YOU DIDNT USE THIS LIKE YOU SHOULD HAVE");
        }
        propagateQueue.addAll(watchedList.getPureLiterals());
        while(!solvedLits.isSolved()){
            if(!propagateQueue.isEmpty()) {
                Integer litToBePropagated = propagateQueue.remove(0);
                if(solvedLits.contains(-litToBePropagated) || propagateQueue.contains(-litToBePropagated)){
                    fail();
                    continue;
                }
                if(solvedLits.contains(litToBePropagated) || propagateQueue.contains(litToBePropagated)){
                    continue;
                }
                //System.out.println(watchedList);
                propagate(litToBePropagated);
                //System.out.println("After propagating " + litToBePropagated +":\n" + watchedList);
            } else{
                Integer decision = decide();
                if (decision == null) {
                    solvedLits.setSatisfiability(true);
                    return;
                }
                System.out.println("Deciding " + decision);
                solvedLits.addDecisionLevel();
                propagateQueue.add(decision);


                if (solvedLits.length() == cs.getNumLiterals()) {
                    solvedLits.setSatisfiability(true);
                }
            }
        }

    }

    public Integer decide(){
        Integer decision = 1;
        while(solvedLits.contains(decision) || solvedLits.contains(-decision)){
            decision++;
            if(decision > cs.getNumLiterals()){
                return null;
            }
        }
        return decision;
    }

    private void fail(){
        System.out.println("Backtracking");
        solvedLits.chronologicalBacktrack();
    }
    private void propagate(Integer litToBePropagated){
        solvedLits.addToLastDecisionLevel(litToBePropagated);

        System.out.println(solvedLits);
        //System.out.println();
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
                ){
                    changesMade = true;
                    break;
                }
                if(i == clause.length - 1){
                    ArrayList<Integer> potentialLitsToPropagate = new ArrayList<>(watchedList.getWatchedLitsInClause(clauseIndex));
                    potentialLitsToPropagate.remove(Integer.valueOf(-litToBePropagated));
                    propagateQueue.add(potentialLitsToPropagate.get(0));

                }
            }
            if(changesMade) {
                watchedList.removeWatched(clauseIndex, -litToBePropagated);
                watchedList.addWatched(clauseIndex, newLitToBeWatched);
            }
        }

    }
}

