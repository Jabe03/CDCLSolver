import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class main{

    public static void main(String[] args){
        testCNFSolver();

    }

    public static void testCNFSolver(){
        ClauseSet cs = CNFReader.readFile("test_decision_and_fail", false);
        CNFSolver solver = new CNFSolver();
        solver.setClauseSet(cs);
        long startTime = System.currentTimeMillis();
        solver.solve();
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);
        System.out.println(solver.getSolution());
    }
}
