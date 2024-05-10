package Solver;

import java.util.*;

/**
 * Data structure for keeping our partial assignment M as Solver.java is running.
 * Also includes many helpful transformations and helper methods related to the partial assignment.
 * A key feature is that the literals contained in this data structure also carry the reason they were added, since the elements are LitSolutions.
 * @author Joshua Bergthold
 * @author Brayden Hambright
 */
public class CNFSolution implements Iterable<LitSolution> {

    /**
     * Contains our partial assignment M, each sublist of LitSolutions denotes a decision level, with the first sublist being DL0.
     */
    private List<List<LitSolution>> sol;

    /**
     * A copy of the references of sol. This allows for on average constant-time contains checking.
     */
    public Set<LitSolution> litsInSol;

    /**
     * A String representing the satisfiability of the  current assignment with respect to some clause set.
     * Has three possible values: "SAT", "UNSAT", or "undecided"
     */
    public String satisfiability;

    /**
     * Creates a CNFSolution object with an empty assignment
     */
    public CNFSolution(){
        sol = new ArrayList<>();
        litsInSol = new HashSet<>();
        sol.add(new ArrayList<>());
        satisfiability = "undecided";
    }

    /**
     * Creates a CNFSolution object with an initial assignment that populates DL0.
     */
    public CNFSolution(List<LitSolution> initial){
        sol = new ArrayList<>();
        sol.add(new ArrayList<>());
        litsInSol = new HashSet<>();
        satisfiability = "undecided";
        for(LitSolution lit: initial){
            this.addToLastDecisionLevel(lit);
        }
    }

    /**
     * @return highest decision level present in this partial assignment. A freshly-constructed CNFSolution has a decision level of 0
     */
    public int getHighestDecisionLevel(){//returns highest decision level
        return sol.size()-1;
    }

    /**
     * @return The most recent literal added to M. Which is the last literal of the last decision level.
     */
    public LitSolution getLastOfLastDecisionLevel(){//returns the final value of the highest decision level
        List<LitSolution> highestDL = sol.get(getHighestDecisionLevel());
        return highestDL.get(highestDL.size()-1);
    }

    /**
     * Adds a decision level to M.
     */
    public void addDecisionLevel(){//when a decision is made, add decision level
        sol.add(new ArrayList<>());
    }

    /**
     * Marks this solution as either SAT or UNSAT.
     * @param satisfiable true if this solution satisfies the clause set, false if the clause set is  unsatisfiable
     */
    public void setSatisfiability(boolean satisfiable){ //set satisfiability of our solution
        if(satisfiable){
            satisfiability = "SAT";
        } else {
            satisfiability = "UNSAT";
        }
    }

    /**
     * Adds a collection of literals to M
     * @param lits Collection of lits to be added
     */
    public void addToLastDecisionLevel(Collection<Integer> lits){

        for(Integer lit: lits){
            addToLastDecisionLevel(new LitSolution(lit));
        }
    }

    /**
     * Adds a literal to M, in the last decision level
     * @param e Lit to be added
     */
    public void addToLastDecisionLevel(LitSolution e){

        if(contains(e) || contains(e.negation())){
            return;
        }
        sol.get(sol.size()-1).add(e);
        litsInSol.add(e);


    }

    /**
     * Gets the reason that a particular literal was added to M
     * @param litVal Literal to be queried
     * @return An array of literal values that is the reason litVal was added to M.
     */
    public Integer[] getReasonFor(Integer litVal){
        for(LitSolution lit: this){
            if(Objects.equals(lit.literal, litVal)){
                return lit.reason;
            }
        }
        return null;
    }


    /**
     *
     * @return The number of literals in this partial assignment.
     */
    public int length(){ //return total number of literals in all decision levels
        return litsInSol.size();
    }

    /**
     *
     * @param i Decision level to be queried
     * @return List of literals in decision level i
     */
    public List<LitSolution> getDecisionLevel(int i){ //return current decision level
        return sol.get(i);
    }

    /**
     *
     * @return True if this assignment has been marked as SAT or UNSAT
     */
    public boolean isSolved(){ //checks if solution is SAT or UNSAT and returns that value
        return satisfiability.equals("SAT") || satisfiability.equals("UNSAT");
    }

    /**
     * @param lit Literal to be checked
     * @return true if the lit is a literal in M
     */
    public boolean contains(Integer lit){//checks if a literal exists in any decision level
        return litsInSol.contains(new LitSolution(lit, null));
    }
    /**
     * @param lit Literal to be checked
     * @return true if the lit is a literal in M
     */
    public boolean contains(LitSolution lit){
        return litsInSol.contains(lit);
    }

    /**
     * Deletes all decision levels to the right of the level specified.
     * Adds the specified literal's negation, with the specified reason, to the last decision level left after deletion.
     * @param literal literal that is currently present in M to backjump about
     * @param reason Reason that the backjump is happening (which is necessarily relevant to the specified literal)
     * @param backjumpLevel Level to backjump to
     */
    public void backjump(int literal, Integer[] reason, int backjumpLevel){

        List<List<LitSolution>> removed = sol.subList(backjumpLevel+1, sol.size());
        List<LitSolution> removedLits = mergeLists(removed);
        removedLits.forEach(litsInSol::remove);
        sol =  sol.subList(0,backjumpLevel+1);
        addToLastDecisionLevel(new LitSolution(-literal, reason));
    }

    /**
     * @return The set of literals that appear in M.
     */
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

    /**
     * Merges the decision levels of this M into one continuous list.
     * @return Merged list of literals.
     */
    public List<LitSolution> getMergedSol(){
        return mergeLists(sol);
    }


    /**
     * Gets the decision level index that a specified literal is contained in.
     * @param literal Literal whose decision level is returned.
     * @return Decision level of the specified literal. -1 if the literal does not appear in M.
     */
    public int getDLof(LitSolution literal){

        for(int i = 0; i < sol.size(); i ++){
            if(sol.get(i).contains(literal) || sol.get(i).contains(literal.negation())){
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the literal whose decision level is highest, with respect to this partial assignment M, in the specified list of literals.
     * @param clause List of literals whose highest literal will be extracted.
     * @return Literal in clause that has the highest decision level in this M.
     */
    public int getHighestLiteralOf(List<Integer> clause){
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

    /**
     * Returns the second-highest decision level  literal with respect to this M
     * @param clause List of literals whose second-highest decision level will be determined
     * @return Index of second-highest decision level in M. Or, if the size of the list of literals is 1, returns the decision level immediately before that literal.
     */
    public int getSecondHighestDLinClause(List<Integer> clause){
        if(clause.size() == 1){
            return getDLof(new LitSolution(clause.get(0))) -1;
        }
        int highestDL = -2;
        int secondHighestDL = -1;
        for(Integer lit: clause){
            int currentDL = getDLof(new LitSolution(lit));
            if(currentDL > secondHighestDL){
                secondHighestDL = currentDL;
            }
            if(secondHighestDL > highestDL){
                int temp = secondHighestDL;
                secondHighestDL = highestDL;
                highestDL = temp;
            }

        }
        return secondHighestDL;
    }

    /**
     *
     * @param clause Clause to be checked for satisfiability
     * @return true if this clause is satisfied by the current assignment M. False otherwise.
     */
    public boolean satisfies(Integer[] clause) {
        for(Integer lit: clause){
            if(litsInSol.contains(new LitSolution(lit))){
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<LitSolution> iterator() { //flattens the solution into one array
        ArrayList<LitSolution> result  = new ArrayList<>();
        for(List<LitSolution> dl : sol){
            result.addAll(dl);
        }
        return result.iterator();
    }
    @Override
    public String toString(){
        if(satisfiability.equals("UNSAT")){
            return "UNSAT";
        }
        StringBuilder b = new StringBuilder();
        b.append("[");
        for(List<LitSolution> dl: sol){
            for(LitSolution lit: dl){
                b.append(lit).append(" ");
            }
            b.append("* ");
        }
        b.append("]");
        return b.toString();
    }


    /**
     * Translates this object into a competition-formatted String
     * @return formatted String
     */
    public String toFormattedString(){
        StringBuilder b = new StringBuilder();
        b.append("s ");

        switch (satisfiability){
            case "SAT" -> b.append("SATISFIABLE");
            case "UNSAT" -> b.append("UNSATISFIABLE");
            case "undecided"-> b.append("UNKNOWN");
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
            b.append("\nc Timed out");
        }
        return b.toString();
    }
}
