import FirstAttempt.CNFSolver;
import Reader.CNFReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;


public class SATTests {
    @Rule
    public Timeout timeout = new Timeout(15000, TimeUnit.MILLISECONDS);
    @Test
    public void testblock0() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("block0", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("block0 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testslide_example() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("slide_example", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("slide_example assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testcnfgen_php_10_10() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("cnfgen-php-10-10", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("cnfgen-php-10-10 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void teste() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("e", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("e assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testelimredundant() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("elimredundant", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("elimredundant assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testfactor2708413neg() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("factor2708413neg", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("factor2708413neg assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testfactor2708413pos() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("factor2708413pos", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("factor2708413pos assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testprime121() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("prime121", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("prime121 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testprime1369() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("prime1369", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("prime1369 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testprime1681() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("prime1681", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("prime1681 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testprime169() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("prime169", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("prime169 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testprime1849() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("prime1849", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("prime1849 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testprime841() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("prime841", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("prime841 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testprime961() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("prime961", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("prime961 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testpropagate_test() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("propagate_test", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("propagate_test assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testpropagate_test_2() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("propagate_test_2", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("propagate_test_2 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testpropagate_test_3() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("propagate_test_3", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("propagate_test_3 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testpropagate_test_4() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("propagate_test_4", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("propagate_test_4 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testqg1_07() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("qg1-07", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("qg1-07 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testqg1_08() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("qg1-08", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("qg1-08 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testqg2_07() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("qg2-07", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("qg2-07 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testqg2_08() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("qg2-08", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("qg2-08 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testqg3_08() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("qg3-08", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("qg3-08 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testqg4_09() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("qg4-09", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("qg4-09 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testqg5_11() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("qg5-11", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("qg5-11 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testqg6_09() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("qg6-09", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("qg6-09 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testsat10() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("sat10", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("sat10 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testsat12() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("sat12", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("sat12 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testsqrt10201() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("sqrt10201", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("sqrt10201 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testsqrt1042441() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("sqrt1042441", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("sqrt1042441 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testsqrt10609() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("sqrt10609", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("sqrt10609 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testsqrt11449() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("sqrt11449", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("sqrt11449 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testuf20_01000() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("uf20-01000", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("uf20-01000 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testuf20_0101() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("uf20-0101", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("uf20-0101 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testuf20_0102() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("uf20-0102", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("uf20-0102 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testuf20_0103() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("uf20-0103", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("uf20-0103 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testuf20_0104() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("uf20-0104", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("uf20-0104 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testuf20_0105() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("uf20-0105", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("uf20-0105 assignment" + s.getSolution().toFormattedString());
    }

    @Test
    public void testuf20_0106() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("uf20-0106", true));
        s.solve();
        assertEquals(s.getSolution().satisfiability, "SAT");
        System.out.println("uf20-0106 assignment" + s.getSolution().toFormattedString());
    }




}