import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        int adj[][]={
                {0,2,3,-1,-1,-1,-1,-1},
                {-1,0,-1,3,-1,-1,-1,-1},
                {-1,-1,0,1,5,-1,-1,-1},
                {-1,-1,-1,0,1,3,-1,-1},
                {-1,-1,-1,-1,0,-1,2,-1},
                {-1,-1,-1,-1,-1,0,-1,2},
                {-1,-1,-1,-1,-1,-1,0,1},
                {-1,-1,-1,-1,-1,-1,-1,0},

        };
        int hue[]={6,4,4,4,3,1,1,0};

        Node strt = new Node(0,0,hue[0],null);
        Comparator<Node> fcompare = Comparator.comparingInt(Node::getf);
        PriorityQueue<Node> open = new PriorityQueue<Node>(fcompare);
        ArrayList<Node> visited = new ArrayList<Node>();
        open.add(strt);
        visited.add(strt);

        Node finnode=null,tem;
        int ndno;
        while (!open.isEmpty()){
            tem=open.remove();
            ndno = tem.nodeno;
//            System.out.println(ndno);
            if(hue[ndno]==0){
                finnode=tem;
//                System.out.println(ndno);
                break;
            }
            for (int i=0;i<8;i++){
                if(adj[ndno][i]>0) {
                    finnode = new Node(i,adj[ndno][i]+ tem.dept,hue[i],tem);
                    if(notvist(finnode,visited)){
                        open.add(finnode);
                        visited.add(finnode);
                    }
                }
            }
        }
        Stack<Node> prtstk = new Stack<Node>();
        do {
            prtstk.push(finnode);
            finnode = finnode.Parent;
        } while (finnode != null);
//        System.out.println(prtstk);
        while (!prtstk.isEmpty()){
            System.out.println(prtstk.pop().nodeno);
        }
    }

    public static boolean notvist(Node n,ArrayList visited){
        Iterator<Node> iterator = visited.iterator();
        Node tem;
        while (iterator.hasNext()) {
            tem =iterator.next();
            if((tem.nodeno==n.nodeno)&&(tem.dept==n.dept)&&(tem.heu==n.heu)&&(tem.Parent==n.Parent)){
                return false;
            }

        }
        return true;

    }
}

class Node{
    Node Parent;
    int nodeno;
    int dept,heu;
    Node(int n,int d,int h,Node p){
        dept=d;
        nodeno=n;
        heu=h;
        Parent=p;
    }
    int getf(){
        return dept+heu;
    }
}