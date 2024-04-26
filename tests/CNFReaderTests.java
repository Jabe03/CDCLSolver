import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;

public class CNFReaderTests {


    @Test
    public void readsAllSATFilesWithoutThrowingException(){
        String SATPath = "inputs\\sat\\";
        testReadFilesFromDirectory(SATPath);
    }
    @Test
    public void readsAllUNSATFilesWithoutThrowingException(){
        String UNSATPath = "inputs\\unsat\\";
        testReadFilesFromDirectory(UNSATPath);
    }

    private void testReadFilesFromDirectory(String dirstr){
        File dir = new File(dirstr);
        File[] files = dir.listFiles();
        assertNotNull(files);
        for(File f: files){
            long start = System.currentTimeMillis();
            ClauseSet cs = CNFReader.readFile(f);
            assertNotNull(cs);
            List<Integer[]> clauses = cs.getClauses();
            assertNotNull(clauses);
            long elapsed = (System.currentTimeMillis() - start);
            System.out.println("Read " + f.getName() + "(" + clauses.size() + "clauses) in" + elapsed + "ms. " + cs);
            /*if(elapsed == 0){
                fail("Something is wrong with the following clause set generated:\n" + main.toStringArrayListWithArrays(clauses));
            }*/
        }
    }



}
