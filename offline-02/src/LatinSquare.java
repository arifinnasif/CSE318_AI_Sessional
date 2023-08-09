import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class LatinSquare {
    Variable[][] grid;
    private int n;
    ArrayList<Coord> unassigned;
    boolean[][] assigned; // redundant, but makes life easy
    int[] col_unassign_count;
    int[] row_unassign_count;
    ValueOrdering valueOrdering;


    LatinSquare(LatinSquare source) {
        this.n = source.n;

        grid = new Variable[n][n];
        unassigned = new ArrayList<>();
        assigned = new boolean[n][n];
        col_unassign_count = new int[n];
        row_unassign_count = new int[n];
        valueOrdering = new ValueOrdering(source.valueOrdering);

        for(Coord v: source.unassigned) this.unassigned.add(v);
        for(int i = 0; i < n; i++) {
            this.col_unassign_count[i] = source.col_unassign_count[i];
            this.row_unassign_count[i] = source.row_unassign_count[i];
            for(int j = 0; j < n; j++) {
                this.grid[i][j] = new Variable(source.grid[i][j]);
                this.assigned[i][j] = source.assigned[i][j];
            }
        }

    }


    LatinSquare(int[][] arr) {
        this.n = arr.length;

        grid = new Variable[n][n];
        unassigned = new ArrayList<>();
        assigned = new boolean[n][n];
        col_unassign_count = new int[n];
        row_unassign_count = new int[n];
        valueOrdering = new ValueOrdering(n);

        for(int i=0; i<n; i++) {
            row_unassign_count[i] = n;
            col_unassign_count[i] = n;
        }

        for(int i = 0; i < n; i++){
            for(int j= 0; j <n;j++) {
                grid[i][j] = new Variable(n);
                if(arr[i][j] != 0) {
                    assigned[i][j] = true;
                    row_unassign_count[i]--;
                    col_unassign_count[j]--;
                    grid[i][j].setVal(arr[i][j]);
                    valueOrdering.incUsage(arr[i][j]);

                } else {
                    assigned[i][j] = false;
                    Coord temp = new Coord(i,j);
                    unassigned.add(temp);
                }
            }
        }

        for(Coord v : unassigned) {
//            System.out.println(""+v.x+" , "+v.y);
            for(int k = 0; k < n; k++) {
                if(assigned[v.x][k]) {
                    if(k==v.y) continue;
                    Variable u = grid[v.x][v.y];
                    Variable a = grid[v.x][k];
                    if(u.domain.contains(a.domain.get(0))) {
//                        System.out.println("unass "+v.x+" , "+v.y+" contains "+ v.x+" , "+k+" 's val");
                        u.domain.remove(Integer.valueOf(a.domain.get(0)));
                    }
                }
            }

            for(int k = 0; k < n; k++) {
                if(assigned[k][v.y]) {
                    Variable u = grid[v.x][v.y];
                    Variable a = grid[k][v.y];
                    if(u.domain.contains(a.domain.get(0))) {
//                        System.out.println("unass "+v.x+" , "+v.y+" contains "+ k +" , "+ v.y +" 's val");
                        u.domain.remove(Integer.valueOf(a.domain.get(0)));
                    }
                }
            }
        }

//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
////                System.out.print(""+i+","+j+" -> ");
////                for(int v: grid[i][j].domain) System.out.print(""+v+" ");
////                System.out.println();
//
//            }
//
//        }
//        System.out.println();


    }


    Coord vah1() { // smallest domain
        int min = Integer.MAX_VALUE;
        Coord min_coord = null;
        for(Coord coord: unassigned) {
            int temp;
            if((temp = grid[coord.x][coord.y].domain.size()) < min) {
                min = temp;
                min_coord = coord;
            }
        }
//        System.out.println("from vah1: "+min);
//        for(int v: grid[min_coord.x][min_coord.y].domain) System.out.print(""+v+" ");
        return min_coord;
    }


    Coord vah2() { // most link to unassigned
        int max = Integer.MIN_VALUE;
        Coord max_coord = null;
        for(Coord coord: unassigned) {
            int temp;
            if((temp = row_unassign_count[coord.x] + col_unassign_count[coord.y] - 2) > max) {
                max = Math.max(temp, 0);
                max_coord = coord;
            }
        }
        return max_coord;
    }


    Coord vah3() { // smallest domain, tie break most link to unassigned
        int min = Integer.MAX_VALUE;
        ArrayList<Coord> min_var_coord_list = new ArrayList<>();
        Coord min_coord = null;
        for(Coord coord: unassigned) {
            int temp = grid[coord.x][coord.y].domain.size();
            if(temp < min) {
                min = temp;
                min_coord = coord;
                min_var_coord_list.clear();
                min_var_coord_list.add(coord);
            } else if(temp == min) {
                min_var_coord_list.add(coord);
            }
        }
        if(min_var_coord_list.size() == 1) return min_coord;

        int max = Integer.MIN_VALUE;
        Coord max_coord = null;
        for(Coord coord: min_var_coord_list) {
            int temp;
            if((temp = row_unassign_count[coord.x] + col_unassign_count[coord.y] - 2) > max) {
                max = Math.max(temp, 0);
                max_coord = coord;
            }
        }
        return max_coord;

    }


    Coord vah4() { // vah1/vah2
        double min = Double.MAX_VALUE;
        Coord min_coord = null;
        for(Coord coord: unassigned) {
            double denom = Math.max(row_unassign_count[coord.x] + col_unassign_count[coord.y] - 2, 0.001);
            double temp = ((double)grid[coord.x][coord.y].domain.size())/denom;
            if(temp < min) {
                min = temp;
                min_coord = coord;
            }
        }
        return min_coord;
    }


    Coord vah5() {
        Random r = new Random();
        return unassigned.get(r.nextInt(unassigned.size()));
    }


    boolean isRowConsistent(int row) {
        boolean[] marker = new boolean[n];
        for(int i = 0; i < n; i++) marker[i] = false;
        for(int j = 0; j < n; j++) {
            if(assigned[row][j]) {
                int assigned_val = grid[row][j].domain.get(0);
                if(marker[assigned_val - 1]) return false;
                marker[assigned_val - 1] = true;
            }
        }
        return true;
    }


    boolean isColConsistent(int col) {
        boolean[] marker = new boolean[n];
        for(int i = 0; i < n; i++) marker[i] = false;
        for(int i = 0; i < n; i++) {
            if(assigned[i][col]) {
                int assigned_val = grid[i][col].domain.get(0);
                if(marker[assigned_val - 1]) return false;
                marker[assigned_val - 1] = true;
            }
        }
        return true;
    }


    boolean isConsistent() {
        for (int i = 0; i < n; i++) {
            if(!isRowConsistent(i)) return false;
        }
        for (int i = 0; i < n; i++) {
            if(!isColConsistent(i)) return false;
        }
        return true;
    }


    boolean isComplete() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(!assigned[i][j]) return false;
            }
        }
        return true;
    }


    private boolean assign(int i, int j, int val) {
        if(assigned[i][j]) {
            return false;
        }
        assigned[i][j] = true;
        unassigned.remove(new Coord(i,j));
        col_unassign_count[j]--;
        row_unassign_count[i]--;
//        System.out.println("Setting "+i+" , "+j+" value to "+val);
        grid[i][j].setVal(val);
        valueOrdering.incUsage(val);
//        System.out.println("++");
//        this.printLatinSquare();
//        System.out.println("--");
        return true;
    }


    boolean assign(int i, int j, int val, boolean isFC) {
        if(!isFC) {
            return assign(i, j, val);
        }
        for(int k = 0; k < n; k++) {
            if(k==j || assigned[i][k] || !grid[i][k].domain.contains(val)) continue;
            if(grid[i][k].domain.size() == 1) return false;
            grid[i][k].domain.remove(Integer.valueOf(val));
        }

        for(int k = 0; k < n; k++) {
            if(k==i || assigned[k][j] || !grid[k][j].domain.contains(val)) continue;
            if(grid[k][j].domain.size() == 1) return false;
            grid[k][j].domain.remove(Integer.valueOf(val));
        }

        if(!grid[i][j].domain.contains(val)) return false;

        return assign(i,j,val);
    }

    public void print() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(assigned[i][j]) {
                    System.out.print(""+grid[i][j].domain.get(0)+"\t");
                } else {
                    System.out.print("*\t");
                }

            }
            System.out.println();
        }
    }

    public int[][] toArray() {
        int[][] ret = new int[n][n];
        for (int i = 0; i < n; i++) {
            ret[i] = new int[n];
            for (int j = 0; j < n; j++) {
                if(assigned[i][j]) {
                    ret[i][j] = grid[i][j].domain.get(0);
                } else {
                    ret[i][j] = 0;
                }

            }
        }
        return ret;
    }

    ArrayList<Integer> getLCVOrderedList(int row, int col) {
        ValueOrdering v = new ValueOrdering(n);
        for (int i = 0; i < n; i++) {
            if(assigned[i][col] || i==row) continue;
            for(int val : grid[i][col].domain) {
                v.incUsage(val);
            }

        }

        for (int i = 0; i < n; i++) {
            if(assigned[row][i] || i==col) continue;
            for(int val : grid[row][i].domain) {
                v.incUsage(val);
            }

        }

        return v.getOrderedList(grid[row][col].domain);
    }

}
