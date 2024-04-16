import java.util.*;

public class dfs 
{
    public static void main(String[] args) 
    {
        int n,e,a,b,flag=0;
        System.out.println("Enter number of nodes:");
        Scanner s=new Scanner(System.in);
        n=s.nextInt();

        int adjmat[][]=new int[n][n];
        int visited[]=new int[n];

        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++)
                adjmat[i][j]=0;

        System.out.println("Enter number of edges:");
        e=s.nextInt();
        for(int i=0;i<e;i++)
        {
            a=s.nextInt();
            b=s.nextInt();
            adjmat[a][b]=1;
            adjmat[b][a]=1;
        }

        System.out.println("Enter source and destination:");
        a=s.nextInt();
        b=s.nextInt();

        ArrayList<Integer> path=new ArrayList<Integer>();
        Stack<Integer> stack=new Stack<Integer>();
        stack.push(a);
        //visited[a]=1;
        //5path.add(a);

        while(!stack.isEmpty())
        {
            a=stack.pop();
            if(visited[a]==1)
                continue;
            path.add(a);
            visited[a]=1;
            for(int i=n-1;i>=0;i--)
            {
                if(adjmat[a][i]==1 && visited[i]==0)
                {
                    stack.push(i);
                }
            }
            if(a==b)
            {
                flag=1;
                break;
            }
            
        }
        if(flag==1)
            System.out.println(path);
        else
            System.out.println("Path doesn't exist");
        s.close();
    }
}
