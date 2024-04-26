import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class CNFReader {
    public static ClauseSet readFile(String name, boolean satisfiable){
        String path = "inputs\\";
        if(satisfiable){
            path += "sat\\";
        } else {
            path += "unsat\\";
        }

        path += name + ".cnf";
        File f = new File(path);

        return readFile(f);

    }

    public static ClauseSet readFile(File f){
        Scanner tsm;
        try{
            tsm = new Scanner(f);
        } catch (FileNotFoundException e){
            System.out.println(f.getPath() + " was not recognized as a file");
            return null;
        }

        return getClauses(tsm);
    }



    public static ClauseSet readSATFile(String name){
        return readFile(name, true);
    }

    public static ClauseSet readUNSATFile(String name){
        return readFile(name, false);
    }

    private static ClauseSet getClauses(Scanner tsm){
        ArrayList<Integer[]> clauses = new ArrayList<>();
        int numVars = 0;
        while(tsm.hasNextLine()){

            String line = tsm.nextLine();

            if(line.startsWith("p")){
                String[] args = line.split(" ");
                numVars = Integer.parseInt(args[2]);

            } else if(line.startsWith("c")){
                //do nothing
            } else {
                Scanner minitsm = new Scanner(line);
                LinkedList<Integer> literals = new LinkedList<>();
                while(minitsm.hasNextInt()){
                    int val = minitsm.nextInt();
                    if(val == 0){
                        break;
                    }
                    literals.add(val);
                }

                clauses.add(literals.toArray(new Integer[0]));
            }

        }
        return new ClauseSet(clauses, numVars);
    }
}
