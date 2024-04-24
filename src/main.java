import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main{
    public static void main(String[] args){
        System.out.println(toStringArrayListWithArrays(CNFReader.readFile("block0", true)));
    }

    public static <E> String toStringArrayListWithArrays(List<E[]> list){
        if(list == null) {
            return "null";
        }
        StringBuilder b = new StringBuilder();
        b.append("[");
        for(Object[] objects: list){
            b.append(Arrays.toString(objects)).append(", ");
        }
        b.delete(b.length() - 2, b.length());
        b.append("]");

        return b.toString();
    }
}