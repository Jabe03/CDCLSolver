import java.util.ArrayList;
import java.util.List;

public class WatchedList {

    ArrayList<Integer>[] positiveWatched ;
    ArrayList<Integer>[] negativeWatched ;

    ArrayList<Integer>[] watchedLitsInClauses;

    public WatchedList(ClauseSet cs){
       int numLits = cs.getNumLiterals();
       positiveWatched = new ArrayList[numLits];
       negativeWatched = new ArrayList[numLits];

       watchedLitsInClauses = new ArrayList[cs.getNumClauses()];
       initPositiveAndNegativeLists(cs);
    }

    private void initPositiveAndNegativeLists(ClauseSet cs){
        for(int i = 0; i < cs.getNumClauses(); i++){
            watchedLitsInClauses[i] = new ArrayList<>();
        }
        for(int i = 0; i <  positiveWatched.length; i++){
            positiveWatched[i] = new ArrayList<>();
            negativeWatched[i] = new ArrayList<>();
        }

        List<Integer[]> clauses = cs.getClauses();

        for(int i = 0; i < clauses.size(); i++){
            Integer[] clause = clauses.get(i);
            if(clause.length == 0){
                throw new RuntimeException("NAUIUUURRRRR");
            } else if(clause.length == 1){
                //positiveWatched[clause[0]].add(i);
                addWatched(i, clause[0]);
            } else {
                //positiveWatched[clause[0]].add(i);
                addWatched(i, clause[0]);
                addWatched(i, clause[1]);
                //positiveWatched[clause[1]].add(i);
            }
        }
    }
    public ArrayList<Integer> getWatchedLitsInClause(int clauseIndex){
        return watchedLitsInClauses[clauseIndex];
    }
    public ArrayList<Integer> getClausesWithWatchedLit(int lit){
        if(lit > 0){
            return positiveWatched[lit-1];
        }
        lit = -lit;
        return negativeWatched[lit-1];
    }

    public void addWatched(int clauseindex, int lit){
        if(contains(clauseindex, lit)){
            return;
        }
        getClausesWithWatchedLit(lit).add(clauseindex);
        watchedLitsInClauses[clauseindex].add(lit);
    }

    public void removeWatched(int clauseindex, int lit){
        getClausesWithWatchedLit(lit).remove(Integer.valueOf(clauseindex));
        watchedLitsInClauses[clauseindex].remove(Integer.valueOf(lit));
    }

    public ArrayList<Integer> getPureLiterals(){
        ArrayList<Integer> pureLits = new ArrayList<>();
        for(ArrayList<Integer> list: watchedLitsInClauses){
            if(list.size() == 1){
                pureLits.addAll(list);
            }
        }
        return pureLits;
    }

    public String toString(){
        StringBuilder b = new StringBuilder();

        for (int i = 0; i < positiveWatched.length; i++) {
            b.append("(").append(i+1).append(": ");
            for (int j = 0; j < positiveWatched[i].size(); j++) {
                b.append(positiveWatched[i].get(j) + 1).append(", ");
            }
            b.delete(b.length()-2, b.length());
            b.append(")");
        }
        b.append("\n");
        for (int i = 0; i < negativeWatched.length; i++) {
            b.append("(").append(-(i+1)).append(": ");
            for (int j = 0; j < negativeWatched[i].size(); j++) {
                 b.append(negativeWatched[i].get(j) + 1).append(", ");
            }
            b.delete(b.length()-2, b.length());
            b.append(")");
        }
        return b.toString();
    }

    public boolean contains(Integer clauseIndex, Integer watchedLit) {

        return getClausesWithWatchedLit(watchedLit).contains(clauseIndex);
    }

    public boolean isEmpty() {//checks for empty case (no literals)
        // Check positive watched list
        for (ArrayList<Integer> list : positiveWatched) {
            if (!list.isEmpty()) {
                return false;
            }
        }

        // Check negative watched list
        for (ArrayList<Integer> list : negativeWatched) {
            if (!list.isEmpty()) {
                return false;
            }
        }
        return true;
    }


//    public void removeFromIndex(int index, Integer element){
//        get(index).re
//    }
}
