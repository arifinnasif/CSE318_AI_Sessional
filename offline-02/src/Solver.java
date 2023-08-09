import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Solver {
    private int vah;
    private boolean isFC;
    private long noOfBT;
    private long noOfNodes;
    private long nanoTime;
    private LatinSquare solution;
    public Solver(int vah, boolean isFC) {
        if(vah<1 || vah>5) {
            System.err.println("wrong vah");
            System.exit(-1);
        }
        this.vah = vah;
        this.isFC = isFC;
        this.noOfBT = 0;
        this.noOfNodes = 0;
        this.nanoTime = 0;
    }
    public void solve(int[][] grid) {

        this.noOfBT = 0;
        this.noOfNodes = 0;

        LatinSquare initial = new LatinSquare(grid);
        ++noOfNodes;
//        latinSquare.printLatinSquare();
        long t1 = System.nanoTime();
        this.solution = solve(initial);
        long t2 = System.nanoTime();
        this.nanoTime = t2 - t1;

        if(this.solution == null) {
//            System.out.println("No solution");
            return;
        }

//        this.solution.print();

    }

    private LatinSquare solve(LatinSquare latinSquare) {

        if(!latinSquare.isConsistent()) {
//            System.out.println("============================================================================");
            if(!latinSquare.isComplete()) ++noOfBT;
            return null;
        }
//        latinSquare.printLatinSquare();
        if(latinSquare.isComplete()) return latinSquare;
        Coord current_var_coord = null;
        if(vah == 1) {
            current_var_coord = latinSquare.vah1();
        } else if(vah == 2) {
            current_var_coord = latinSquare.vah2();
        } else if(vah == 3) {
            current_var_coord = latinSquare.vah3();
        } else if(vah == 4) {
            current_var_coord = latinSquare.vah4();
        } else if(vah == 5) {
            current_var_coord = latinSquare.vah5();
        }

        ArrayList<Integer> orderedList = latinSquare.getLCVOrderedList(current_var_coord.x, current_var_coord.y);
        for(int val : orderedList) {


            LatinSquare child = new LatinSquare(latinSquare);
            ++noOfNodes;


            if(child.assign(current_var_coord.x, current_var_coord.y, val, isFC)) {
                LatinSquare ret = solve(child);

                if (ret != null) return ret;
            } else {
                ++noOfBT;
            }

        }


        return null;
    }

    public long getNoOfBT() {
        return noOfBT;
    }

    public long getNoOfNodes() {
        return noOfNodes;
    }

    public long getNanoTime() {
        return nanoTime;
    }

    public LatinSquare getSolution() {
        return solution;
    }
}
