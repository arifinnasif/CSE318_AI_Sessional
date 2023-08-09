import java.util.ArrayList;
import java.util.Collections;

public class ValueOrdering {
    ArrayList<Value> values;
    ValueOrdering(int n) {
        values = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            values.add(new Value(i));
        }
    }

    ValueOrdering(ValueOrdering other) {
        values = new ArrayList<>();
        for (Value value: other.values) {
            this.values.add(new Value(value));
        }
    }

    void incUsage(int val) {
        values.get(val-1).usage++;
    }

    ArrayList<Integer> getOrderedList(ArrayList<Integer> domain) {
        ArrayList<Value> copyValues = new ArrayList<>();
        ArrayList<Integer> ret = new ArrayList<>();
        for(Value i : values) copyValues.add(i);
//        Collections.sort(copyValues);
        for(Value value : copyValues) {
//            System.out.println(value.val);
            if(domain.contains(value.val)) {
//                System.out.println("contains");
                ret.add(value.val);
            }
        }
//        System.out.println();

        return ret;
    }

//    public static void main(String[] args) {
//        ValueOrdering v = new ValueOrdering(10);
//        ArrayList<Integer> d = new ArrayList<>();
//        d.add(1);
//        d.add(10);
//        d.add(4);
//        d.add(2);
//        v.incUsage(4);
//        v.incUsage(4);
//        v.incUsage(4);
//        v.incUsage(4);
//        v.incUsage(2);
//        v.incUsage(2);
//        v.incUsage(10);
//        v.incUsage(10);
//        v.incUsage(10);
//        v.incUsage(7);
//        v.incUsage(7);
//        v.incUsage(7);
//        ArrayList<Integer> t = v.getOrderedList(d);
//        for(int i : t) System.out.println(i);
//
//    }
}
