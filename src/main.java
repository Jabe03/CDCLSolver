import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class main{

    public static void main(String[] args){
        testCNFSolver();

    }

    public static void testCNFSolver(){
        ClauseSet cs = CNFReader.readFile("sqrt10201", true);
        CNFSolver solver = new CNFSolver();
        solver.setClauseSet(cs);
        long startTime = System.currentTimeMillis();
        solver.solve();
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);
        System.out.println(solver.getSolution());
    }
}
