import org.junit.Test;

import java.util.Arrays;

public class ExplainTests {


    @Test
    public void testMerge(){
        System.out.println(CNFSolver.mergeClauses(Arrays.asList(1,2,3), Arrays.asList(-1,2,3), 1));
    }

    @Test
    public void testMerge2(){
        System.out.println(CNFSolver.mergeClauses(Arrays.asList(8,20,18), Arrays.asList(-6,-20,8), 20));

    }
}
