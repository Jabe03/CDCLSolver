import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CNFReader {
    public static List<Integer[]> readFile(String name, boolean satisfiable){
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

    public static List<Integer[]> readFile(File f){
        Scanner tsm;
        try{
            tsm = new Scanner(f);
        } catch (FileNotFoundException e){
            System.out.println(f.getPath() + " was not recognized as a file");
            return null;
        }

        return getClauses(tsm);
    }



    public static List<Integer[]> readSATFile(String name){
        return readFile(name, true);
    }

    public static List<Integer[]> readUNSATFile(String name){
        return readFile(name, false);
    }

    private static List<Integer[]> getClauses(Scanner tsm){
        ArrayList<Integer[]> clauses = new ArrayList<>();
        while(tsm.hasNextLine()){

            String line = tsm.nextLine();

            if(line.startsWith("p")){
                //String[] args = line.split(" ");

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
        return clauses;
    }
}
