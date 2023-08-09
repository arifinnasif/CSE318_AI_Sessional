import java.util.ArrayList;

public class Variable {
    int n;
    public ArrayList<Integer> domain;
    public Variable(Variable variable) {
        this.n = variable.n;
        domain = new ArrayList<>();

        for (int i: variable.domain) {
            this.domain.add(i);
        }
    }
    public Variable(int n) {
        this.n = n;
        domain = new ArrayList<>();

        for(int i=1; i<=n; i++) {
            domain.add(i);
        }

    }
    public boolean setVal(int val) {
        if(!domain.contains(val)) {
//            System.out.println("out of domain assignment");
            return false;
        }
        domain.clear();
        domain.add(val);
        return true;
    }


}
