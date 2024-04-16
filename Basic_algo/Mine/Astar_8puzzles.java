import java.util.*;

import static java.lang.Math.abs;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
//        System.out.println("Hello and welcome!");
        Hueristiccal hcal =new Hueristiccal();

        int[][] initialState = {
                {2, 8, 3},
                {1, 6, 4},
                {7, 0, 5}
        };

        int[][] goalState = {
                {1, 2, 3},
                {8, 0, 4},
                {7, 6, 5}
        };




        Comparator<Node> fcompare = Comparator.comparingInt(Node::getf);
        PriorityQueue <Node> open = new PriorityQueue<Node>(fcompare);
        ArrayList <Node>  visited = new ArrayList<Node>();
        Node parent = new Node(null,initialState,hcal.hueristicval(initialState,goalState),0);
        open.add(parent);
        visited.add(parent);
        Node ntem;
        int[][] atem;
        int[][] t1;
        int x=0,y=0;
        int tem;
        // Press Shift+F10 or click the green arrow button in the gutter to run the code.
        Node finnode=null;
        while(!open.isEmpty()){
            ntem = open.remove();
            atem=ntem.currstate;

//            for (int i=0;i<3;i++){
//                for (int j=0;j<3;j++){
//                    System.out.print(atem[i][j]+"\t");
//                }
//                System.out.println(" ");
//            }
//            System.out.println("\n");

            if(hcal.hueristicval(atem,goalState)==0){
                finnode=ntem;
                break;
            }
            for (int i=0;i<3;i++){
                for (int j=0;j<3;j++){
                    if(atem[i][j]==0){
                        x=i;
                        y=j;
                    }
                }
            }

            for (int i=-1;i<2;i++){
                for (int j=-1;j<2;j++){
                    if((i!=j)&&(i+x>=0)&&(j+y>=0)&&(i+x<3)&&(j+y<3)){
                        if(abs(i)==abs(j)){
                            continue;
                        }
                        t1 = new int[3][3];
                        for (int k = 0; k < 3; k++) {
                            t1[k] = atem[k].clone();
                        }


                        tem = t1[x+i][y+j];
                        t1[x][y]=tem;
                        t1[x+i][y+j] =0;


                        if(!hcal.checkvisited(t1,visited)) {
                            Node newn = new Node(ntem,t1,hcal.hueristicval(t1,goalState), ntem.dept+1);
                            open.add(newn);
                            visited.add(newn);
                            prtnode(newn);
                        }

                    }
                }
            }

        }
        if(finnode!=null) {
            int[][] finstate = finnode.currstate;
            printSolutionPath(finnode);
        }
    }

    private static void prtnode(Node finnode){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(finnode.currstate[i][j] + "\t");
            }
            System.out.println(" ");
        }
        System.out.println("hval"+finnode.getf());
    }
    private static void printSolutionPath(Node finnode) {
        Stack <Node> tem = new Stack<Node>();
        do {
            tem.push(finnode);
            finnode = finnode.parent;
        } while (finnode != null);
        while(!tem.isEmpty()){
            finnode=tem.pop();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    System.out.print(finnode.currstate[i][j] + "\t");
                }
                System.out.println(" ");
            }
            System.out.println("\t|\t");
            System.out.println("\t|\t");
            System.out.println("\tV\t");
        }
        System.out.println("Doneeeeeeee");
    }
}

class Node{

    int[][] currstate;
    int dept;
    Node parent;
    int heuristic;
    Node(Node p, int[][] cur,int hval,int d){
        heuristic=hval;
        parent=p;
        currstate=cur;
        dept=d;
    }
    public int getf(){
        return heuristic+dept;
    }
}

class Hueristiccal{
    public int hueristicval(int[][] initial, int[][] goal){
        int hval=0;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(initial[i][j]!=goal[i][j]){
                    hval++;
                }
            }
        }


        return hval;
    }

    public boolean checkvisited(int[][] newn,ArrayList <Node>  visited){
        Iterator<Node> iterator = visited.iterator();
        while (iterator.hasNext()) {

            if(ifeq(newn,iterator.next().currstate)){
                return true;
            }
        }
        return false;
    }

    public boolean ifeq(int[][] newn1,int[][] newn2){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(newn1[i][j]!=newn2[i][j]){
                    return false;
                }
            }
        }
        return true;
    }
}