import Reader.CNFReader;
import Reader.ClauseSet;
import Solver.CNFSolution;
import Solver.CNFSolver;
import Solver.LitSolution;

import java.util.Arrays;
import java.util.List;

/**
 * @author Joshua Bergthold
 * @author Brayden Hambright
 */
public class Main {

    public static void main(String[] args)  {
        if(args.length == 1){

            testCNFSolver(args[0]);
        } else{
            String[] samples = new String[]{
                    "prime121"
            };

            for(String fileName: samples){
                testCNFSolver(fileName);
            }
        }

    }

    public static void testCNFSolver(String name){ //main function
        ClauseSet cs = CNFReader.readFile(name);
        CNFSolver solver = new CNFSolver();
        solver.setClauseSet(cs);
        System.out.println("Solving " + name);
        long startTime = System.currentTimeMillis();
        solver.solve();
        long endTime = System.currentTimeMillis();
        System.out.println(solver.getSolution().toFormattedString());
        System.out.println("Solved in " + (endTime-startTime) + "ms\n");
    }


    public static boolean solutionSatisfies(CNFSolution sol, ClauseSet clauseSet){ //checks to make sure solution provided satisfies all clauses
        List<Integer[]> set = clauseSet.getClauses();
        boolean satisfied = true;
        for(int i  = 0; i < set.size(); i++){
            List<Integer> clist = Arrays.asList(set.get(i));
            boolean clauseIsSat = false;
            for(LitSolution lit: sol){
                if (clist.contains(lit.literal)) {
                    clauseIsSat = true;
                    break;
                }
            }
            if(!clauseIsSat){
                System.out.println("Clause " + (i) + " is unsatisfied: " + clist);
                satisfied = false;
            }
        }
        return satisfied;
    }
}
