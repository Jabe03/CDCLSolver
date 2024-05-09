package FirstAttempt;

import Reader.ClauseSet;

import java.util.*;

public class CNFSolution implements Iterable<LitSolution> {

    //private final ClauseSet cs;
    private List<List<LitSolution>> sol;

    public Set<LitSolution> litsInSol;

    public String satisfiability;

    public CNFSolution(){
        sol = new ArrayList<>();
        litsInSol = new HashSet<>();
        sol.add(new ArrayList<>());
        satisfiability = "undecided";
    }
    public CNFSolution(List<LitSolution> initial){
        sol = new ArrayList<>();
        sol.add(new ArrayList<>());
        litsInSol = new HashSet<>();
        for(LitSolution lit: initial){
            this.addToLastDecisionLevel(lit);
        }
        satisfiability = "undecided";
        //System.out.println("Created new CNFSolution with: " + this);
    }

    public LitSolution getLit(Integer lit){
        for(LitSolution temp: this){
            if(Objects.equals(temp.literal, lit)){
                return temp;
            }
        }
        return null;
    }

    public List<LitSolution> getMissingLits(Integer totalLits){
        List<LitSolution> missing = new ArrayList<>(totalLits);
        for(int i  = 1; i <= totalLits; i++){
            missing.add(new LitSolution(i));
        }
        for(LitSolution lit: this){
            missing.remove(lit);
            missing.remove(lit.negation());
        }
        return missing;
    }



    public int getHighestDecisionLevel(){//returns highest decision level
        return sol.size()-1;
    }

    public LitSolution getLastOfLastDecisionLevel(){//returns the final value of the highest decision level
        List<LitSolution> highestDL = sol.get(getHighestDecisionLevel());
        return highestDL.get(highestDL.size()-1);
    }

    public void addDecisionLevel(){//when a decision is made, add decision level
        sol.add(new ArrayList<>());
    }

    public void setSatisfiability(boolean satisfiable){ //set satisfiability of our solution
        if(satisfiable){
            satisfiability = "SAT";
        } else {
            System.out.println("M: " + this);
            try{
                throw new RuntimeException("Concluded UNSAT with");
            } catch(RuntimeException e){
                e.printStackTrace();
            }
            satisfiability = "UNSAT";
        }
    }
    public void addToLastDecisionLevel(Iterable<LitSolution> lits){
        for(LitSolution lit: lits){
            addToLastDecisionLevel(lit);
        }
    }
    public void addToLastDecisionLevel(List<Integer> lits){

        for(Integer lit: lits){
            addToLastDecisionLevel(new LitSolution(lit));
        }
    }
    /**
     * Adds literal to the last decision level
     */
    public void addToLastDecisionLevel(int literal, Integer[] reason){ //call when propagating our decision
        addToLastDecisionLevel(new LitSolution(literal, reason));

        //System.out.println("M=" + sol);
    }
    public void addToLastDecisionLevel(LitSolution e){

        if(contains(e) || contains(e.negation())){
            try{
                throw new RuntimeException();
            } catch (RuntimeException f){
                //f.printStackTrace();
            }
            //System.out.println(e.toLongString());
            return;
        }
        sol.get(sol.size()-1).add(e);
        litsInSol.add(e);
        if(getHighestDecisionLevel() == 0){
            //System.out.println("Added" + e.toLongString() + "to DL0, checking if Clause Set has any fully assigned satisfied clauses");
        }
    }

    public Integer[] getReasonFor(Integer litVal){
        for(LitSolution lit: this){
            if(Objects.equals(lit.literal, litVal)){
                return lit.reason;
            }
        }
        return null;
        //throw new NoSuchElementException("No " +  litVal + " was in M:"+this);
    }



    public int length(){ //return total number of literals in all decision levels
//        int totalLength = 0;
//        for(ArrayList<Integer> dl: sol){
//            totalLength += dl.size();
//        }
//        return totalLength;
        return litsInSol.size();
    }
    public List<LitSolution> getDecisionLevel(int i){ //return current decision level
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
        return litsInSol.contains(new LitSolution(lit, null));
    }
    public boolean contains(LitSolution lit){
        return litsInSol.contains(lit);
    }
    public String toStringWithReasons() {
        return toString(true);
    }

    private String toString(boolean reasons){
        if(satisfiability.equals("UNSAT")){
            return "UNSAT";
        }
        StringBuilder b = new StringBuilder();
        b.append("[");
        for(List<LitSolution> dl: sol){
            for(LitSolution lit: dl){
                b.append(reasons ? lit.toLongString() : lit).append(" ");
            }
            b.append("********** ");
        }
        b.delete(b.length()-3, b.length()).append("]");
        return b.toString();
    }

    @Override
    public String toString(){//converts solution into something that can be printed
        return toString(false);
    }

    public void clear(){
        sol = sol.subList(0,1);
        List<LitSolution> removed = mergeLists(sol.subList(1,getHighestDecisionLevel()+1));
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
            for(LitSolution lit: this){
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
            //sol = new ArrayList<>();
            return;
        }
        List<LitSolution> removedLits = sol.get(getHighestDecisionLevel());
        removedLits.forEach(litsInSol::remove);
        List<LitSolution> removedPropPath = sol.remove(sol.size()-1);
        addToLastDecisionLevel(removedPropPath.get(0).negation());
    }

    /**
     *
     * @param literal
     * @param backjumpLevel
     * @return list of literals that were removed as a result of the backjump
     */
    public List<LitSolution> backjump(int literal, Integer[] reason, int backjumpLevel){
        //System.out.println("backjumping " + this);

        List<List<LitSolution>> removed = sol.subList(backjumpLevel+1, sol.size());
        List<LitSolution> removedLits = mergeLists(removed);
        removedLits.forEach(litsInSol::remove);
        sol =  sol.subList(0,backjumpLevel+1);
//        if(literal == 59){
//            System.out.println("Added 59 with reason " + Arrays.toString(reason));
//        }
        addToLastDecisionLevel(new LitSolution(-literal, reason));

        return removedLits;
    }
    public Set<LitSolution> getLitsInSol(){
        return litsInSol;
    }
    public static <E> List<E> mergeLists(List<List<E>> lists){
        List<E> result = new ArrayList<>();
        for(List<E> list : lists){
            result.addAll(list);
        }
        return result;
    }
    public List<LitSolution> getMergedSol(){
        return mergeLists(sol);
    }

    @Override
    public Iterator<LitSolution> iterator() { //flattens the solution into one array
        ArrayList<LitSolution> result  = new ArrayList<>();
        for(List<LitSolution> dl : sol){
            result.addAll(dl);
        }
        return result.iterator();
    }
    public int totalInHighestDL(List<LitSolution> addedClause) {
        List<LitSolution> highestDL = sol.get(getHighestDecisionLevel());
        int count = 0;

        for(LitSolution lit: addedClause){
            if(highestDL.contains(lit) || highestDL.contains(lit.negation())){
                count++;
            }
        }
        return count;
    }

    public int getDLof(LitSolution literal){

        for(int i = 0; i < sol.size(); i ++){
            if(sol.get(i).contains(literal) || sol.get(i).contains(literal.negation())){
                //System.out.println("DL of " + literal + " is " + i);
                return i;
            }
        }
        //System.out.println("DL of " + literal + " is non existent");
        //System.out.println(this.toString());
        return -1;
    }
    public LitSolution getLastDecision(){
        return sol.get(getHighestDecisionLevel()).get(0);
    }

    public int getHighestLiteral(List<Integer> clause){
        int highestLit = 0;
        int highestDL = -1;
        for(Integer lit: clause){
            int decisionLevel = getDLof(new LitSolution(lit));
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
            int decisionLevel = getDLof(new LitSolution(lit));
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
            if(litsInSol.contains(new LitSolution(lit))){
                return true;
            }
        }
        return false;
    }
}
