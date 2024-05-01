import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.max;

public class main{

    public static void main(String[] args){
        testCNFSolver();

    }

    public static void testCNFSolver(){
        ClauseSet cs = CNFReader.readFile("uuf100-010", false);
        CNFSolver solver = new CNFSolver();
        solver.setClauseSet(cs);
        long startTime = System.currentTimeMillis();
        solver.solve();
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);
        System.out.println(solver.getSolution());
        System.out.println(solutionSatisfies(solver.getSolution(), cs));
    }


    public static boolean solutionSatisfies(CNFSolution sol, ClauseSet clasueSet){
        List<Integer[]> set = clasueSet.getClauses();
        for(int i  = 0; i < set.size(); i++){
            List<Integer> clist = Arrays.asList(set.get(i));
            boolean clauseIsSat = false;
            for(Integer lit: sol){
                if(clist.contains(lit)){
                    clauseIsSat = true;
                }
            }
            if(clauseIsSat == false){
                System.out.println("Clause " + (i) + " is unsatisfied: " + clist);
                return false;
            }
        }
        return true;
    }
}
