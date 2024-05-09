import FirstAttempt.CNFSolution;
import FirstAttempt.CNFSolver;
import FirstAttempt.LitSolution;
import Reader.CNFReader;
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
        CNFSolution solvedLits = new CNFSolution(null);
        solvedLits.addToLastDecisionLevel(new LitSolution(1));
        solvedLits.addToLastDecisionLevel(new LitSolution(1));
        assertEquals(new LitSolution(1), new LitSolution(1));
        System.out.println(solvedLits.litsInSol);
        assertTrue(solvedLits.contains(new LitSolution(1)));
    }
    @Test
    public void testComparingLitSolutions(){
        LitSolution lit = new LitSolution(1);
        LitSolution lit1 = new LitSolution(1);
        LitSolution lit2 = new LitSolution(2);
        LitSolution lit3 = new LitSolution(3);
        LitSolution lit4 = new LitSolution(4);
        LitSolution lit5 = new LitSolution(-5);
        LitSolution lit6 = new LitSolution(-6);
        LitSolution lit7 = new LitSolution(7);
        System.out.println(lit1.compareTo(lit2));
        assertTrue(lit1.compareTo(lit2) < 0);
    }

    @Test
    public void satisfiesTest(){

        ClauseSet cs  = CNFReader.readFile("prime121", true);
        CNFSolution sol = new CNFSolution();
        List<Integer> sols = List.of(1, 2, -3, 4, 5, 6, 7, 8, -9, -10, -11, -12, -13, -14, -15, -16, -17, 18, 19, 20, -21, -22, 23, 24, 25, 26, 27,
                -28, -29, -30, -31, 32, -33, -34, 35, -36, 37, -38, -39, 40, -41, 42, -43, -44, -45, 46, -47, -48, -49, 50, -51, -52, -53, 54, -55, -56, -57,
                58, -59, -60, -61, -62, -63, 64, 65, -66, -67, 68, -69, -70, 71, -72, -73, -74, -75, -76, 77, -78, -79, -80, -81, 82, -83, -84, 85, -86, -87,
                88, 89, 90, -91, -92, 93, -94, -95, -96, -97, 98, 99, -100, -101, 102, -103, -104, 105, -106, -107, 108, -109, -110, 111, -112, -113, -114,
                -115, 116, -117, -118, 119, -120, -121, 122, -123, -124, -125, 126, -127, -128, 129, -130, -131, -132, 133, 134, 135, -136, -137, 138, -139,
                -140, -141, -142, 143, -144, 145, -146, -147, 148, -149, 150, -151, -152, -153, -154, -155, 156, -157, -158, 159, -160, 161, -162, -163, -164,
                165, -166, -167, -168, 169, -170, -171, -172, 173, -174, -175, -176, 177, -178, -179, 180, -181, 182, -183, -184, 185, 186, -187, -188, -189,
                -190, 191, -192, 193, -194, -195, 196, -197, -198, 199, -200, 201, -202, -203, -204, 205, -206, 207, -208, -209, 210, -211, 212, -213, -214,
                -215, 216, -217, -218, -219, -220, 221, -222, 223, -224, -225, -226, -227, -228, 229, -230, -231, 232, -233, 234, -235, 236, -237, -238, 239,
                -240, 241, -242, -243, 244, -245, -246, 247, -248, 249, -250, -251, 252, -253, -254, 255, -256, -257, 258, -259, 260, -261, -262, 263, -264,
                -265, 266, -267, -268, 269, -270, 271, -272, 273, -274, -275, 276, -277, 278, -279, -280, 281, -282, -283, 284, -285, 286, -287, -288, -290,
                -291, 292, -293, -294, 295, -296, 297, -298, 299, -300, -301, 302, -303, 304, -305, -306, 307, 308, -309, -310, 311, -312, -313, 314, -315,
                316, -317, 318, 319, 320, 321, 322, 323, 324, 325, 326, 327, 328, 329
                , 289);
        sol.addToLastDecisionLevel(sols);
        assertTrue(main.solutionSatisfies(sol, cs));

    }

    @Test
    public void get2ndHighestDLTest(){
        CNFSolution sol = new CNFSolution();
        sol.addToLastDecisionLevel(new LitSolution(1));
        sol.addDecisionLevel();
        sol.addToLastDecisionLevel(new LitSolution(2));
        sol.addToLastDecisionLevel(new LitSolution(3));
        sol.addToLastDecisionLevel(new LitSolution(4));
        sol.addDecisionLevel();
        sol.addToLastDecisionLevel(new LitSolution(5));
        sol.addDecisionLevel();
        sol.addToLastDecisionLevel(new LitSolution(6));
        System.out.println(sol.getSecondHighestDLinClause(List.of(1,2,5)));
    }
}
