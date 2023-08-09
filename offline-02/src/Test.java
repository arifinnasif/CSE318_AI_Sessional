import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        File file = new File(args[0]);
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
            Solver solver = new Solver(Integer.parseInt(args[1]), Boolean.parseBoolean(args[2]));
            solver.solve(arr);
            LatinSquare solution = solver.getSolution();
            if(solution == null) {
                System.out.print(args[1]+",");
                System.out.print(args[2]+",");
                System.out.println("no_solution");
            } else {
//                solution.print();
                System.out.print(args[1]+",");
                System.out.print(args[2]+",");
                System.out.print(solver.getNanoTime()+",");
                System.out.print(solver.getNoOfNodes()+",");
                System.out.println(solver.getNoOfBT());
            }



        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
