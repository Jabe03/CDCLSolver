import FirstAttempt.CNFSolver;
import Reader.ClauseSet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class ExplainTests {


    @Test
    public void testMerge(){
        System.out.println(CNFSolver.mergeClauses(Arrays.asList(1,2,3), Arrays.asList(-1,2,3), 1));
    }

    @Test
    public void testMerge2(){
        System.out.println(CNFSolver.mergeClauses(Arrays.asList(8,20,18), Arrays.asList(-6,-20,8), 20));

    }

    @Test
    public void containsClause(){
        ArrayList<Integer[]> clauses = new ArrayList<>();
        clauses.add(new Integer[]{1,2});

        ClauseSet cs = new ClauseSet(clauses,2);

        assertTrue(cs.containsClause(List.of(1,2)));
    }
}
