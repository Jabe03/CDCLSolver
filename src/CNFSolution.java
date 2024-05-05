import java.util.*;

public class CNFSolution implements Iterable<Integer> {

    private ArrayList<ArrayList<Integer>> sol;



    String satisfiability;

    public CNFSolution(){
        sol = new ArrayList<>();
        sol.add(new ArrayList<>());
        satisfiability = "undecided";
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

    /**
     * Adds literal to the last ddecision level
     * @param e Literal to be added
     * @param reason Index of the clause that is the reason why this literal is getting addded
     */
    public void addToLastDecisionLevel(int e, int reason){ //call when propagating our decision
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
        b.delete(b.length()-3, b.length()).append("]");
        return b.toString();
    }

    public String toFormattedString(){
        StringBuilder b = new StringBuilder();
        b.append("s ");

        switch (satisfiability){
            case "SAT" -> b.append("SATISFIABLE");
            case "UNSAT" -> b.append("UNSATISFIABLE");
            case "undecided"-> b.append("UNKNOWN");
            default -> b.append("AIHJKEGBWIASEGHVBSNDFOXKLCGVBHJSNDIDGKVBHGSZDESBRDFXICVK ZHXDBFXIVKM XZSDHBFIVBKZSHDGFHXVBIZKMWHJSRBGVIZSDUKJRFDHGVBJN");
        }

        b.append("\n");
        if(satisfiability.equals("SAT")){
            b.append("v ");
            for(Integer lit: this){
                b.append(lit).append(" ");
            }
        }
        b.delete(b.length()-1, b.length());

        if(satisfiability.equals("undecided")){
            b.append("\nc Timed out in ").append(CNFSolver.TIMEOUT).append("ms");
        }
        return b.toString();
    }
    /*
    public void chronologicalBacktrack(){//backtracking without use of Explain/learn. Will implement non-chronological later.
        if(sol.size() == 1){
            setSatisfiability(false);
            return;
        }
        ArrayList<Integer> removedPropPath = sol.remove(sol.size()-1);
        addToLastDecisionLevel(-removedPropPath.get(0));
    }*/

    /**
     *
     * @param literal
     * @param backjumpLevel
     * @param reasonClauseIndex
     * @return list of literals that were removed as a result of the backjump
     */
    public List<Integer> backjump(int literal, int backjumpLevel, int reasonClauseIndex){
        List<ArrayList<Integer>> removed = sol.subList(backjumpLevel, sol.size());
        sol = (ArrayList<ArrayList<Integer>>) sol.subList(0,backjumpLevel);

        addToLastDecisionLevel(-literal, reasonClauseIndex);


        return  mergeLists(removed);
    }


    private List<Integer> mergeLists(List<ArrayList<Integer>> lists){
        List<Integer> result = new ArrayList<>();
        for(List<Integer> list : lists){
            result.addAll(list);
        }
        return result;
    }

    @Override
    public Iterator<Integer> iterator() { //flattens the solution into one array
        ArrayList<Integer> result  = new ArrayList<>();
        for(ArrayList<Integer> dl : sol){
            result.addAll(dl);
        }
        return result.iterator();
    }

    public int totalInHighestDL(List<Integer> addedClause) {
        ArrayList<Integer> highestDL = sol.get(getHighestDecisionLevel());
        int count = 0;

        for(Integer lit: addedClause){
            if(highestDL.contains(lit)){

                count++;
            }
        }
        return count;
    }
    public int getDLof(Integer literal){
        for(int i = 0; i < sol.size(); i ++){
            if(sol.get(i).contains(literal)){
                return i;
            }
        }
        return -1;
    }
    public int getHighestLiteral(List<Integer> clause){
        int highestLit = 0;
        int highestDL = -1;
        for(Integer lit: clause){
            int decisionLevel = getDLof(lit);
            if(decisionLevel> highestDL){
                highestLit = lit;
                highestDL = decisionLevel;
                if(highestDL == getHighestDecisionLevel()){
                    return highestDL;
                }
            }
        }
        return highestLit;
    }
}
