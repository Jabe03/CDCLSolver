import FirstAttempt.CNFSolver;
import Reader.CNFReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;


public class UNSATTests {
    @Rule
    public Timeout timeout = new Timeout(600000, TimeUnit.MILLISECONDS);
    @Test
    public void testadd128() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("add128", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("add128 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testadd16() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("add16", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("add16 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testadd32() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("add32", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("add32 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testadd4() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("add4", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("add4 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testadd64() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("add64", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("add64 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testadd8() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("add8", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("add8 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testcnfgen_parity_9() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("cnfgen-parity-9", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("cnfgen-parity-9 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testcnfgen_peb_pyramid_20() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("cnfgen-peb-pyramid-20", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("cnfgen-peb-pyramid-20 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testcnfgen_php_5_4() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("cnfgen-php-5-4", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("cnfgen-php-5-4 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testcnfgen_ram_4_3_10() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("cnfgen-ram-4-3-10", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("cnfgen-ram-4-3-10 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testcnfgen_tseitin_10_4() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("cnfgen-tseitin-10-4", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("cnfgen-tseitin-10-4 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testelimclash() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("elimclash", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("elimclash assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testfalse() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("false", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("false assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testfull1() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("full1", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("full1 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testfull3() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("full3", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("full3 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testfull5() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("full5", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("full5 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testfull7() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("full7", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("full7 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testph6() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("ph6", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("ph6 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testprime65537() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("prime65537", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("prime65537 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testtest_decision_and_fail() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("test_decision_and_fail", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("test_decision_and_fail assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testunit7() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("unit7", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("unit7 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testuuf100_010() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("uuf100-010", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("uuf100-010 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testuuf100_0117() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("uuf100-0117", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("uuf100-0117 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testuuf100_012() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("uuf100-012", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("uuf100-012 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testuuf100_0120() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("uuf100-0120", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("uuf100-0120 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testuuf100_0130() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("uuf100-0130", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("uuf100-0130 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testuuf100_0147() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("uuf100-0147", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("uuf100-0147 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testuuf100_0151() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("uuf100-0151", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("uuf100-0151 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testuuf100_0161() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("uuf100-0161", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("uuf100-0161 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testuuf100_0175() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("uuf100-0175", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("uuf100-0175 assignment" + s.getSolution().toFormattedString());
    }
    @Test
    public void testuuf100_0182() {
        CNFSolver s = new CNFSolver();
        s.setClauseSet(CNFReader.readFile("uuf100-0182", false));
        s.solve();
        assertEquals("Concluded incorrectly with assignment: " + s.getSolution().toFormattedString(), s.getSolution().satisfiability, "UNSAT");
        System.out.println("uuf100-0182 assignment" + s.getSolution().toFormattedString());
    }


}