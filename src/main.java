import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class main{

    public static void main(String[] args){
        CNFSolver.solve(CNFReader.readFile("propagate_test", true));
    }
}
