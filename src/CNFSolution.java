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

    public int getHighestDecisionLevel(){//returns highest decision level
        return sol.size()-1;
    }

    public Integer getLastOfLastDecisionLevel(){//returns the final value of the highest decision level
        ArrayList<Integer> highestDL = sol.get(getHighestDecisionLevel());
        return highestDL.get(highestDL.size()-1);
    }

    public void addDecisionLevel(){//when a decision is made, add decision level
        sol.add(new ArrayList<>());
    }

    public void setSatisfiability(boolean satisfiabile){ //set satisfiability of our solution
        if(satisfiabile){
            satisfiability = "SAT";
        } else {
            satisfiability = "UNSAT";
        }
    }

    public void addToLastDecisionLevel(int e){ //call when propagating our decision
        sol.get(sol.size()-1).add(e);
    }
    public int length(){ //return total number of literals in all decision levels
        int totalLength = 0;
        for(ArrayList<Integer> dl: sol){
            totalLength += dl.size();
        }
        return totalLength;
    }
    public ArrayList<Integer> getDecisionLevel(int i){ //return current decision level
        return sol.get(i);
    }

    public boolean isSolved(){ //checks if solution is SAT or UNSAT and returns that value
        return satisfiability.equals("SAT") || satisfiability.equals("UNSAT");
    }
    public boolean contains(Integer lit){//checks if a literal exists in any decision level
        boolean isContained = false;
        for(ArrayList<Integer> decisionLevel: sol){
            isContained = isContained || decisionLevel.contains(lit);
        }
        return isContained;
    }
    @Override
    public String toString(){//converts solution into something that can be printed
        return satisfiability.equals("UNSAT") ? "UNSAT" : satisfiability +" " + "(DL" + getHighestDecisionLevel()+")" + sol.toString();
    }

    public String toFormattedString(){
        if(satisfiability.equals("UNSAT")){
            return "UNSAT";
        }
        StringBuilder b = new StringBuilder();
        b.append("[");
        for(ArrayList<Integer> dl: sol){
            for(Integer lit: dl){
                b.append(lit).append(" ");
            }
            b.append("* ");
        }
        b.delete(b.length()-2, b.length()).append("]");
        return b.toString();
    }

    public void chronologicalBacktrack(){//backtracking without use of Explain/learn. Will implement non-chronological later.
        if(sol.size() == 1){
            setSatisfiability(false);
            return;
        }
        ArrayList<Integer> removedPropPath = sol.remove(sol.size()-1);
        addToLastDecisionLevel(-removedPropPath.get(0));
    }

    @Override
    public Iterator iterator() { //flattens the solution into one array
        ArrayList<Integer> result  = new ArrayList<>();
        for(ArrayList<Integer> dl : sol){
            result.addAll(dl);
        }
        return result.iterator();
    }
}
