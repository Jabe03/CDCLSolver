import java.util.ArrayList;

public class CNFSolution {

    private ArrayList<ArrayList<Integer>> sol;

    public CNFSolution(){
        sol = new ArrayList<>();
        sol.add(new ArrayList<>());
    }

    public int getHighestDecisionLevel(){
        return sol.size()-1;
    }

    public void addDecisionLevel(){
        sol.add(new ArrayList<>());
    }

    public void addToLastDecisionLevel(int e){
        sol.get(sol.size()-1).add(e);
    }

    public ArrayList<Integer> getDecisionLevel(int i){
        return sol.get(i);
    }

}
