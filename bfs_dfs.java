import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int x = 5;
        int n = 7;
        Integer tem;
        Integer[][] adj = {
                {0, -1, -1, 1, 1, 1, 1},
                {-1, 0, -1, 1, 1, -1, 1},
                {-1, -1, 0, 1, -1, -1, 1},
                {1, 1, 1, 0, -1, -1, -1},
                {1, 1, -1, -1, 0, 1, -1},
                {1, -1, -1, -1, 1, 0, -1},
                {1, 1, 1, -1, -1, -1, 0}
        };


        boolean[] visited = new boolean[n];


        Stack<Integer> stk = new Stack <>();
        stk.push(x);
        visited[x] = true;

        while (!stk.isEmpty()) {
            tem = stk.pop();
            System.out.print(tem+" ");

            for (int i = 0; i < n; i++) {
                if (adj[tem][i] == 1 && !visited[i]) {
                    visited[i] = true;
                    stk.push(i);
                }
            }
        }
    }
}
