import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main{
    public static void main(String[] args){
        //System.out.println(CNFReader.readFile("block0", true));
        ClauseSet clauseset = CNFReader.readFile("elimredundant", true);
        int numlits = clauseset.getNumLiterals();
        int numclauses = clauseset.getNumClauses();
        List<Integer[]> clauses = clauseset.getClauses();
        System.out.println("Number of literals: " + numlits);
        System.out.println("Number of clauses: " + numclauses);
        ArrayList<Integer>[] positiveWatched = new ArrayList[numlits+1];
        ArrayList<Integer>[] negativeWatched = new ArrayList[numlits+1];
        ArrayList<Integer> propagateQueue = new ArrayList();
        for (int i = 0; i < positiveWatched.length; i++) {
            positiveWatched[i] = new ArrayList<>();
        }
        for (int i = 0; i < negativeWatched.length; i++) {
            negativeWatched[i] = new ArrayList<>();
        }
        for(int i=0; i < numclauses; i++){
            if (clauses.get(i).length == 1){
                propagateQueue.add(clauses.get(i)[0]);
            }
            else {
                Integer firstLit = clauses.get(i)[0];
                Integer secondLit = clauses.get(i)[1];
                if(firstLit > 0){
                    positiveWatched[firstLit].add(i+1);
                }
                else {
                    negativeWatched[-1*firstLit].add(i+1);
                }
                if(secondLit > 0){
                    positiveWatched[secondLit].add(i+1);
                }
                else {
                    negativeWatched[-1*secondLit].add(i+1);
                }
            }

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
        for (int i = 0; i < propagateQueue.size(); i++){
            System.out.print("Queue: " + propagateQueue);
        }
    }

}