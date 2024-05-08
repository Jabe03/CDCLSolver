import FirstAttempt.CNFSolution;
import FirstAttempt.CNFSolver;
import FirstAttempt.LitSolution;
import Reader.ClauseSet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
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

    @Test
    public void testGetADuplicate(){
        CNFSolution solvedLits = new CNFSolution();
        solvedLits.addToLastDecisionLevel(new LitSolution(1));
        solvedLits.addDecisionLevel();
        solvedLits.addToLastDecisionLevel(new LitSolution(1));
        //System.out.println(CNFSolver.getADuplicate(new ArrayList<Integer>(solvedLits.getMergedSol())));
    }

    @Test
    public void solContainsLit(){
        CNFSolution solvedLits = new CNFSolution();
        solvedLits.addToLastDecisionLevel(new LitSolution(1));
        solvedLits.addToLastDecisionLevel(new LitSolution(1));
        assertEquals(new LitSolution(1), new LitSolution(1));
        System.out.println(solvedLits.litsInSol);
        assertTrue(solvedLits.contains(new LitSolution(1)));
    }
}
