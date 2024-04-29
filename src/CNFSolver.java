import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class CNFSolver {

    public static CNFSolution solve(ClauseSet clauseset){
        //System.out.println(CNFReader.readFile("block0", true));
        int numlits = clauseset.getNumLiterals();
        int numclauses = clauseset.getNumClauses();
        List<Integer[]> clauses = clauseset.getClauses();
        System.out.println("Number of literals: " + numlits);
        System.out.println("Number of clauses: " + numclauses);
        ArrayList<Integer>[] positiveWatched = new ArrayList[numlits+1];
        ArrayList<Integer>[] negativeWatched = new ArrayList[numlits+1];
        ArrayList<Integer> propagateQueue = new ArrayList();
        ArrayList<Integer> correctLits = new ArrayList<>();
        for (int i = 0; i < positiveWatched.length; i++) {
            positiveWatched[i] = new ArrayList<>();
        }
        for (int i = 0; i < negativeWatched.length; i++) {
            negativeWatched[i] = new ArrayList<>();
        }
        for(int i = 0; i < numclauses; i++){
            if (clauses.get(i).length == 3){
                propagateQueue.add(clauses.get(i)[0]);
            }
            else {
                Integer[] currentClause = clauses.get(i);
                Integer firstLit = currentClause[0];
                Integer secondLit = currentClause[1];
                if(firstLit > 0){
                    positiveWatched[firstLit].add(i+1);
                }
                else {
                    negativeWatched[-1*firstLit].add(i+1);
                }
                currentClause[currentClause.length-2] = 0;
                if(secondLit > 0){
                    positiveWatched[secondLit].add(i+1);
                }
                else {
                    negativeWatched[-1*secondLit].add(i+1);
                }
                currentClause[currentClause.length-1] = 1;
            }

        }
        while (propagateQueue.isEmpty() == false) {
            int propLit = propagateQueue.get(0);
            int removeFromWatched = propLit*-1;
            ArrayList<Integer> propClauses;
            boolean watchedArray;
            if (removeFromWatched < 0) {
                propClauses = negativeWatched[propLit];
                watchedArray = false;
            }
            else {
                propClauses = positiveWatched[propLit*-1];
                watchedArray = true;
            }
            for (int i = 0; i < propClauses.size(); i++){
                int clauseIndex = propClauses.get(i);
                Integer[] currentClause = clauses.get(propClauses.get(i)-1);
                int firstWatched = currentClause.length-2; //index for index of first watched literal
                int secondWatched = currentClause.length-1; //index for index of second watched literal
                int firstIndex = currentClause[firstWatched]; //index for first watched literal
                int secondIndex = currentClause[secondWatched]; //index for second watched literal
                int nextWatched = max(firstIndex, secondIndex) + 1;
                int toChange;
                int noChange;
                if (currentClause[firstIndex] == removeFromWatched){
                    toChange = firstWatched;
                    noChange = secondIndex;
                }
                else if (currentClause[secondIndex] == removeFromWatched){
                    toChange = secondWatched;
                    noChange = firstIndex;
                }
                else {
                    toChange = 123123;
                    noChange = 12984931;
                }
                if (nextWatched >= currentClause.length-2){
                    if(!propagateQueue.contains(currentClause[noChange]) && !correctLits.contains(currentClause[noChange] )
                            && !correctLits.contains(-currentClause[noChange]) && !propagateQueue.contains(-currentClause[noChange])){
                        propagateQueue.add(currentClause[noChange]);
                    }
                }
                else {
                    currentClause[toChange] = nextWatched;
                    if(watchedArray == true){
                        positiveWatched[removeFromWatched].remove(i);
                    }
                    else{
                        negativeWatched[removeFromWatched*-1].remove(i);
                    }
                    i--;
                    if(currentClause[nextWatched] < 0){
                        negativeWatched[currentClause[nextWatched]*-1].add(clauseIndex);
                    }
                    else{
                        positiveWatched[currentClause[nextWatched]].add(clauseIndex);
                    }
                }
            }
            correctLits.add(propagateQueue.get(0));
            propagateQueue.remove(0);
        }

        for (int i = 1; i< positiveWatched.length; i++){
            System.out.print(i + ": ");
            for (int j = 0; j<positiveWatched[i].size(); j++){
                System.out.print(positiveWatched[i].get(j) + ", ");
            }
            System.out.println();
        }
        for (int i = 1; i< negativeWatched.length; i++){
            System.out.print(-i + ": ");
            for (int j = 0; j<negativeWatched[i].size(); j++){
                System.out.print(negativeWatched[i].get(j) + ", ");
            }
            System.out.println();
        }
        System.out.println("correct: " + correctLits);
        System.out.println("Second clause: " + clauses.get(1)[0] + ", " + clauses.get(1)[1] + ", " + clauses.get(1)[2] + ", " + clauses.get(1)[3]);
        System.out.println("Third clause: " + clauses.get(2)[0] + ", " + clauses.get(2)[1] + ", " + clauses.get(2)[2] + ", " + clauses.get(2)[3] + ", " + clauses.get(2)[4]);
        return null;
    }
}
