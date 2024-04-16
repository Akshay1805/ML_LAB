import java.util.*;
class Node{
    int id;
    double fn,gn,hn;
    Node parent;
    public Node(int id)
    {
        this.id=id;
    }
    void calculateFn()
    {
        fn=gn+hn;
    }
}
class NodeComparator implements Comparator<Node>{
    public int compare(Node n1,Node n2)
    {
        if(n1.fn > n2.fn)
            return 1;
        else if (n1.fn < n2.fn)
            return -1;
        return 0;
    }
}
public class astar {
    public static void main(String[] args) {
        double cost=0;
        int graph[][]=new int[][]{{0,2,3,0,0,0,0,0},
        {0,0,0,3,0,0,0,0},
        {0,0,0,1,3,0,0,0},
        {0,0,0,0,1,3,0,0},
        {0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,2},
        {0,0,0,0,2,0,0,0},
        {0,0,0,0,0,0,1,0}};
        double heuristics[]=new double[]{6.0,4.0,4.0,4.0,3.5,1.0,1.0,0.0};
        char nodes[]=new char[]{'S','A','B','C','D','E','F','G'};

        //ArrayList<Integer> path=new ArrayList<>();
        Node start=new Node(0);
        start.gn=0;
        start.hn=heuristics[0];
        start.parent=null;
        PriorityQueue<Node> pq=new PriorityQueue<Node>(new NodeComparator());
        pq.add(start);
        Node s=new Node(9);
        while(!pq.isEmpty())
        {        
            Node t=pq.poll();
            //path.add(t.id);
            //System.out.println(t.id);
            if(t.id==7)
            {
                s=t;
                cost=t.gn;
                break;
            }       
            for(int i=0;i<=7;i++)
            {
                if(graph[t.id][i]>0)
                {
                    Node temp=new Node(i);
                    temp.hn=heuristics[i];
                    temp.gn=graph[t.id][i]+t.gn;
                    temp.calculateFn();
                    temp.parent=t;
                    pq.add(temp);
                }
            } 
        }
        ArrayList<Integer> paths=new ArrayList<>();
        paths.add(s.id);
        while(s.parent!=null)
        {
            paths.add(s.parent.id);
            s=s.parent;
        }
        Collections.reverse(paths);
        System.out.println(cost);
        for(int i:paths)
        {
            System.out.print(nodes[i]+" ");
        }
    }
}
