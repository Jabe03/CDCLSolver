package FirstAttempt;

import java.util.*;

public class CNFSolution implements Iterable<Integer> {

    private List<List<Integer>> sol;

    private Set<Integer> litsInSol;

    public String satisfiability;

    public CNFSolution(){
        sol = new ArrayList<>();
        litsInSol = new HashSet<>();
        sol.add(new ArrayList<>());
        satisfiability = "undecided";
    }
    public CNFSolution(List<Integer> initial){
        sol = new ArrayList<>();
        litsInSol = new HashSet<>();
        sol.add(initial);
        satisfiability = "undecided";
    }



    public int getHighestDecisionLevel(){//returns highest decision level
        return sol.size()-1;
    }

    public Integer getLastOfLastDecisionLevel(){//returns the final value of the highest decision level
        List<Integer> highestDL = sol.get(getHighestDecisionLevel());
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
     */
    public void addToLastDecisionLevel(int e){ //call when propagating our decision
        sol.get(sol.size()-1).add(e);
        litsInSol.add(e);
        //System.out.println("M=" + sol);
    }
    public int length(){ //return total number of literals in all decision levels
//        int totalLength = 0;
//        for(ArrayList<Integer> dl: sol){
//            totalLength += dl.size();
//        }
//        return totalLength;
        return litsInSol.size();
    }
    public List<Integer> getDecisionLevel(int i){ //return current decision level
        return sol.get(i);
    }

    public boolean isSolved(){ //checks if solution is SAT or UNSAT and returns that value
        return satisfiability.equals("SAT") || satisfiability.equals("UNSAT");
    }
    public boolean contains(Integer lit){//checks if a literal exists in any decision level
//        boolean isContained = false;
//        for(ArrayList<Integer> decisionLevel: sol){
//            isContained = isContained || decisionLevel.contains(lit);
//        }
//        return isContained;
        return litsInSol.contains(lit);
    }
    @Override
    public String toString(){//converts solution into something that can be printed
        if(satisfiability.equals("UNSAT")){
            return "UNSAT";
        }
        StringBuilder b = new StringBuilder();
        b.append("[");
        for(List<Integer> dl: sol){
            for(Integer lit: dl){
                b.append(lit).append(" ");
            }
            b.append("* ");
        }
        b.delete(b.length()-3, b.length()).append("]");
        return b.toString();
    }

    public void clear(){
        sol = sol.subList(0,1);
        List<Integer> removed = mergeLists(sol.subList(1,getHighestDecisionLevel()+1));
        removed.forEach(litsInSol::remove);
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

    public void chronologicalBacktrack(){//backtracking without use of Explain/learn. Will implement non-chronological later.
        //System.out.println("Chronological backtracking " + this);
        if(sol.size() == 1){
            setSatisfiability(false);
            return;
        }
        List<Integer> removedLits = sol.get(getHighestDecisionLevel());
        removedLits.forEach(litsInSol::remove);
        List<Integer> removedPropPath = sol.remove(sol.size()-1);
        addToLastDecisionLevel(-removedPropPath.get(0));
    }

    /**
     *
     * @param literal
     * @param backjumpLevel
     * @return list of literals that were removed as a result of the backjump
     */
    public List<Integer> backjump(int literal, int backjumpLevel){
        //System.out.println("backjumping " + this);

        List<List<Integer>> removed = sol.subList(backjumpLevel+1, sol.size());
        List<Integer> removedLits = mergeLists(removed);
        removedLits.forEach(litsInSol::remove);
        sol =  sol.subList(0,backjumpLevel+1);

        addToLastDecisionLevel(-literal);

        return removedLits;
    }

    public Set<Integer> getLitsInSol(){
        return litsInSol;
    }
    public static List<Integer> mergeLists(List<List<Integer>> lists){
        List<Integer> result = new ArrayList<>();
        for(List<Integer> list : lists){
            result.addAll(list);
        }
        return result;
    }

    @Override
    public Iterator<Integer> iterator() { //flattens the solution into one array
        ArrayList<Integer> result  = new ArrayList<>();
        for(List<Integer> dl : sol){
            result.addAll(dl);
        }
        return result.iterator();
    }

    public int totalInHighestDL(List<Integer> addedClause) {
        List<Integer> highestDL = sol.get(getHighestDecisionLevel());
        int count = 0;

        for(Integer lit: addedClause){
            if(highestDL.contains(lit) || highestDL.contains(-lit)){
                count++;
            }
        }
        return count;
    }
    public int getDLof(Integer literal){

        for(int i = 0; i < sol.size(); i ++){
            if(sol.get(i).contains(literal) || sol.get(i).contains(-literal)){
                //System.out.println("DL of " + literal + " is " + i);
                return i;
            }
        }
        //System.out.println("DL of " + literal + " is non existent");
        //System.out.println(this.toString());
        return -1;
    }

    public Integer getLastDecision(){
        return sol.get(getHighestDecisionLevel()).get(0);
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
                    return lit;
                }
            }
        }
        return highestLit;
    }


    public int getSecondHighestDLinClause(List<Integer> clause){

        int highestDL = 0;
        for(Integer lit: clause){
            int decisionLevel = getDLof(lit);
            if(decisionLevel> highestDL){
                if(decisionLevel != getHighestDecisionLevel()){
                    highestDL = decisionLevel;
                }
            }
        }
        //System.out.println("getting secondd highest DL in " + clause + " is " + highestDL + " total DL is " + getHighestDecisionLevel());
        return highestDL;
    }

    public boolean isEmpty() {
        return sol.get(0).isEmpty();
    }

    public boolean satisfies(Integer[] clause) {
        for(Integer lit: clause){
            if(litsInSol.contains(lit)){
                return true;
            }
        }
        return false;
    }
}
