import java.util.ArrayList;
import java.util.Iterator;

public class CNFSolution implements Iterable<Integer> {

    private ArrayList<ArrayList<Integer>> sol;

    String satisfiability;

    public CNFSolution(){
        sol = new ArrayList<>();
        sol.add(new ArrayList<>());
        satisfiability = "undetermined";
    }

    public int getHighestDecisionLevel(){
        return sol.size()-1;
    }

    public Integer getLastOfLastDecisionLevel(){
        ArrayList<Integer> highestDL = sol.get(getHighestDecisionLevel());
        return highestDL.get(highestDL.size()-1);
    }

    public void addDecisionLevel(){
        sol.add(new ArrayList<>());
    }

    public void setSatisfiability(boolean satisfiabile){
        if(satisfiabile){
            satisfiability = "SAT";
        } else {
            satisfiability = "UNSAT";
        }
    }

    public void addToLastDecisionLevel(int e){
        sol.get(sol.size()-1).add(e);
    }
    public int length(){
        int totalLength = 0;
        for(ArrayList<Integer> dl: sol){
            totalLength += dl.size();
        }
        return totalLength;
    }
    public ArrayList<Integer> getDecisionLevel(int i){
        return sol.get(i);
    }

    public boolean isSolved(){
        return satisfiability.equals("SAT") || satisfiability.equals("UNSAT");
    }
    public boolean contains(Integer lit){
        boolean isContained = false;
        for(ArrayList<Integer> decisionLevel: sol){
            isContained = isContained || decisionLevel.contains(lit);
        }
        return isContained;
    }
    @Override
    public String toString(){
        return satisfiability.equals("UNSAT") ? "UNSAT" : satisfiability +" " + "(DL" + getHighestDecisionLevel()+")" + sol.toString();
    }

    public void chronologicalBacktrack(){
        if(sol.size() == 1){
            setSatisfiability(false);
            return;
        }
        ArrayList<Integer> removedPropPath = sol.remove(sol.size()-1);
        addToLastDecisionLevel(-removedPropPath.get(0));
    }

    @Override
    public Iterator iterator() {
        ArrayList<Integer> result  = new ArrayList<>();
        for(ArrayList<Integer> dl : sol){
            result.addAll(dl);
        }
        return result.iterator();
    }
}
