import java.util.Comparator;
import java.util.PriorityQueue;

class Tile{
    int tiles[][]=new int[3][3];
    int fn,gn,hn,i,j;

    public Tile(int ini[][])
    {
        for(int i=0;i<3;i++)
        { 
            for(int j=0;j<3;j++)
            {
               tiles[i][j]=ini[i][j];
            }
        }
    }

    int numberOfMisplacedTiles()
    {
        int num=0;
        int mat[][]=new int[][]{{1,2,3},{8,0,4},{7,6,5}};
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                if(mat[i][j]!=tiles[i][j])
                    num+=1;
            }
        }
        return num;
    }
    void  calculateFn()
    {

        hn=numberOfMisplacedTiles();
        fn=hn+gn;
    }

    void findSpace()
    {
        for(i=0;i<3;i++){
            for(j=0;j<3;j++){
                if(tiles[i][j]==0)
                    return;
            }
        }

    }

    boolean isUp()
    {
        findSpace();

        if(i-1>=0)
            return true;
        return false;
    }

    boolean isDown()
    {
        findSpace();
        if(i+1<3)
            return true;
        return false;
    }

    boolean isLeft()
    {
        findSpace();
        if(j-1>=0)
            return true;
        return false;
    }

    boolean isRight()
    {
        findSpace();
        if(j+1<3)
            return true;
        return false;
    }

    void up()
    {
        findSpace();
        int t=tiles[i][j];
        tiles[i][j]=tiles[i-1][j];
        tiles[i-1][j]=t;
    }

    void down()
    {
        findSpace();
        int t=tiles[i][j];
        tiles[i][j]=tiles[i+1][j];
        tiles[i+1][j]=t;
    }

    void left()
    {
        findSpace();
        int t=tiles[i][j];
        tiles[i][j]=tiles[i][j-1];
        tiles[i][j-1]=t;
    }

    void right()
    {
        findSpace();
        int t=tiles[i][j];
        tiles[i][j]=tiles[i][j+1];
        tiles[i][j+1]=t;
    }

    void print()
    {
        for(int k=0;k<3;k++)
        {
            for(int l=0;l<3;l++)
            {
                System.out.print(tiles[k][l]+" ");
            }
            System.out.println();
        }
    }
}

class TileComparator implements Comparator<Tile>{
    public int compare(Tile t1,Tile t2)
    {
        if(t1.fn > t2.fn)
            return 1;
        else if (t1.fn < t2.fn)
            return -1;
        return 0;
    }
}


public class puzz8 {
    public static void main(String[] args) {
        int ini[][]=new int[][]{{1,8,3},{2,6,4},{7,0,5}};
        Tile initialTile=new Tile(ini);
        initialTile.gn=0;

        PriorityQueue<Tile> pq=new PriorityQueue<Tile>(new TileComparator());
        pq.add(initialTile);
        int cost=0;
        while(!pq.isEmpty())
        {
            Tile t=pq.poll();
            t.print();
            System.out.println();
            if(t.numberOfMisplacedTiles()==0)
            {
                cost=t.gn;
                break;
            }
            int gtemp=t.gn+1;
            if(t.isUp())
            {
                Tile i=new Tile(t.tiles);
                i.up();
                i.gn=gtemp;
                i.calculateFn();
                pq.add(i);
            }
            if(t.isDown())
            {
                Tile i=new Tile(t.tiles);
                i.down();
                i.gn=gtemp;
                i.calculateFn();
                pq.add(i);
            }
            if(t.isLeft())
            {
                Tile i=new Tile(t.tiles);
                i.left();
                i.gn=gtemp;
                i.calculateFn();
                pq.add(i);
            }
            if(t.isRight())
            {
                Tile i=new Tile(t.tiles);
                i.right();
                i.gn=gtemp;
                i.calculateFn();
                pq.add(i);
            }
        }
        System.out.println(cost);
    }

}
