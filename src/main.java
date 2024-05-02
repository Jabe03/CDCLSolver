import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.max;

public class main{

    public static void main(String[] args){
        testCNFSolver();

    }

    public static void testCNFSolver(){ //main function
        ClauseSet cs = CNFReader.readFile("false", false);
        CNFSolver solver = new CNFSolver();
        solver.setClauseSet(cs);
        long startTime = System.currentTimeMillis();
        solver.solve();
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);
        System.out.println(solver.getSolution());
        System.out.println("Formatted output: " + solver.getSolution().toFormattedString());

    }


    public static boolean solutionSatisfies(CNFSolution sol, ClauseSet clauseSet){ //checks to make sure solution provided satisfies all clauses
        List<Integer[]> set = clauseSet.getClauses();
        for(int i  = 0; i < set.size(); i++){
            List<Integer> clist = Arrays.asList(set.get(i));
            boolean clauseIsSat = false;
            for(Integer lit: sol){
                if (clist.contains(lit)) {
                    clauseIsSat = true;
                    break;
                }
            }
            if(!clauseIsSat){
                System.out.println("Clause " + (i) + " is unsatisfied: " + clist);
                return false;
            }
        }
        return true;
    }
}
