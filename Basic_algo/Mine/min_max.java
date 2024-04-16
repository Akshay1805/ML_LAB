import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        Nprint prt = new Nprint();

        Node initial = new Node();
        initial.pc = 0;
        System.out.println(initial.eval());
        int w;
        while(true) {
            initial.findmv();
            w=initial.eval();
            if(w==1){
                System.out.println("win");
                break;
            }
            if(w==-1){
                System.out.println("lose");
                break;
            }
            if(w==0){
                System.out.println("draw");
                break;
            }
            prt.print(initial.child[initial.step]);
            initial = initial.child[initial.step];

        }
    }
}

class Nprint {
    void print(Node n) {
        System.out.println();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if(n.box[i][j]==1) {
                    System.out.print("x|" + "\t");
                }
                if(n.box[i][j]==-1) {
                    System.out.print("O|" + "\t");
                }
                if(n.box[i][j]==0) {
                    System.out.print(" |" + "\t");
                }


            }
            System.out.println(' ');
        }
    }
}

class Node {
    int[][] box = new int[3][3];
    Node[] child;
    Node parent;

    int childno;
    int eval, pc, step;

    int mval = Integer.MIN_VALUE;
    int[] players = {-1, 1};
    Node() {
        childno = this.empt_count();
        eval = this.eval();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                box[i][j] = 0;
            }
        }
    }

    Node(Node P) {
        for (int i = 0; i < 3; i++) {
            box[i] = Arrays.copyOf(P.box[i], P.box[i].length);
        }
        parent = P;
        childno = this.empt_count();
        eval = this.eval();
    }

    int eval() {
        int[][][] WIN_COMBINATIONS = {
                {{0, 0}, {0, 1}, {0, 2}}, {{1, 0}, {1, 1}, {1, 2}}, {{2, 0}, {2, 1}, {2, 2}}, // Rows
                {{0, 0}, {1, 0}, {2, 0}}, {{0, 1}, {1, 1}, {2, 1}}, {{0, 2}, {1, 2}, {2, 2}}, // Columns
                {{0, 0}, {1, 1}, {2, 2}}, {{0, 2}, {1, 1}, {2, 0}}             // Diagonals
        };
        for (int i = 0; i < 8; i++) {
            if (box[WIN_COMBINATIONS[i][0][0]][WIN_COMBINATIONS[i][0][1]] == box[WIN_COMBINATIONS[i][1][0]][WIN_COMBINATIONS[i][1][1]]) {
                if (box[WIN_COMBINATIONS[i][0][0]][WIN_COMBINATIONS[i][0][1]] == box[WIN_COMBINATIONS[i][2][0]][WIN_COMBINATIONS[i][2][1]]) {  ///fix logic
                    if (box[WIN_COMBINATIONS[i][0][0]][WIN_COMBINATIONS[i][0][1]] == 1) {
                        return 1; // we win
                    }
                    if (box[WIN_COMBINATIONS[i][0][0]][WIN_COMBINATIONS[i][0][1]] == -1) {
                        return -1;//opponent win
                    }

                }
            }
        }
        int count = this.empt_count();

        if (count > 0) {
            return 2;//not terminal
        }
        return 0;//draw
    }

    int empt_count() {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (box[i][j] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

//    void gen_child( ) {
//        Node[] arr = new Node[childno];
//        int c = 0;
//        int t=(this.pc + 1) % 2;
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                if (this.box[i][j] == 0) {
//                    arr[c] = new Node(this);
//                    arr[c].box[i][j] = players[pc];
//                    arr[c].pc = t;
//                    c++;
//                }
//            }
//        }
//        child = arr;
//    }


    int findmv() {
        int eval = this.eval();
        if (mval != Integer.MIN_VALUE) {
            return mval;
        }

        if (eval == 1) {
            mval = 1;
            return 1;
        }
        if (eval == -1) {
            mval = -1;
            return -1;
        }
        if (eval == 0) {
            mval = 0;
            return 0;
        }
        int m = -2, tem;
        if (eval == 2) {
            // Ensure the correct player's move is generated
            this.gen_child();

            for (int i = 0; i < childno; i++) {
                if (child[i] == null) {
                    continue;
                }
                tem = child[i].findmv();
                step = i;
                if (m == -2) {
                    m = tem;
                    continue;
                }
                if (pc == 0) {
                    if (m > tem) {
                        m = tem;
                    }
                    if (m == -1) {
                        break;
                    }
                    continue;
                }
                if (m < tem) {
                    m = tem;
                }
                if (m == 1) {
                    break;
                }
            }
        }
        mval = m;
        return m;
    }

    void gen_child() {
        Node[] arr = new Node[childno];
        int c = 0;
        int t = (this.pc + 1) % 2; // Switch player's turn
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (this.box[i][j] == 0) {
                    arr[c] = new Node(this);
                    arr[c].box[i][j] = players[t]; // Use the updated player
                    arr[c].pc = t;
                    c++;
                }
            }
        }
        child = arr;
    }

}
