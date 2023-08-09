import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        File file = new File("in.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            line = br.readLine();
            int n = Integer.parseInt(line);
            int[][] arr = new int[n][n];
            for (int i = 0; i < n; i++) {
                line = br.readLine();
                String[] tempstrs = line.split("\t");
                arr[i] = new int[n];
                for (int j = 0; j < n; j++) {
                    arr[i][j] = Integer.parseInt(tempstrs[j].trim());
                }
            }
            Solver solver = new Solver(3, true);
            solver.solve(arr);
            LatinSquare solution = solver.getSolution();
            if(solution == null) {
                System.out.println("no solution");
            } else {
                solution.print();
                System.out.println("Took "+solver.getNanoTime()+"ns");
                System.out.println("No of Nodes : "+solver.getNoOfNodes());
                System.out.println("No of Backtracks : "+solver.getNoOfBT());
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
