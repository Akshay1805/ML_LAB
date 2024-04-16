import java.util.*;

class Board{
    char xo[][];
    Board()
    {
        xo=new char[3][3];
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                xo[i][j]=' ';
            }
        }
    }

    void printBoard()
    {
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                System.out.print(xo[i][j]);
                if(j==2)
                    break;
                System.out.print(" | ");
            }
            System.out.println();
            if(i==2)
                break;
            System.out.println("---------");
        }
    }

    boolean isWin(char sym)
    {
        for(int i=0;i<3;i++)
        {
            if(xo[i][0]==sym && xo[i][1]==sym && xo[i][2]==sym)
                return true;
            if(xo[0][i]==sym && xo[1][i]==sym && xo[2][i]==sym)
                return true;
        }
        if(xo[0][0]==sym && xo[1][1]==sym && xo[2][2]==sym)
            return true;
        if(xo[0][2]==sym && xo[1][1]==sym && xo[2][0]==sym)
            return true;    

        return false;
    }

    boolean isFull()
    {
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                if(xo[i][j]==' ')
                    return false;
            }
        }
        return true;
    }

    int minimax(int depth,boolean maxplayer,int a,int b)
    {
        if(isWin('x'))
            return -10;
        if(isWin('o'))
            return 10;
        if(isFull())
            return 0;
        
        if(maxplayer)
        {
            int val=-999,temp,flag=1;
            for(int i=0;i<3;i++)
            {
                for(int j=0;j<3;j++)
                {
                    if(xo[i][j]!=' ')
                        continue;
                    xo[i][j]='o';

                    temp=minimax(depth+1,false,a,b);
                    xo[i][j]=' ';

                    val=Math.max(val, temp);
                    a=Math.max(a, val);
                    if(b<a)
                    {
                        flag=0;
                        break;
                    }
                    
                }
                if(flag==0)
                    break;
            }
            return val;
        }
        else{
            int val=999,temp,flag=1;
            for(int i=0;i<3;i++)
            {
                for(int j=0;j<3;j++)
                {
                    if(xo[i][j]!=' ')
                        continue;
                    xo[i][j]='x';

                    temp=minimax(depth+1,true,a,b);
                    xo[i][j]=' ';

                    val=Math.min(val, temp);
                    b=Math.min(b, val);
                    if(b<a)
                    {
                        flag=0;
                        break;
                    }
                }
                if(flag==0)
                    break;
            }   
            return val;
        }
    }

    void makeMoveO()
    {
        int x=0,y=0,val=-999,temp,flag=1;
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                if(xo[i][j]!=' ')
                    continue;
                xo[i][j]='o';

                if(isWin('o'))
                {
                    x=i;
                    y=j;
                    flag=0;
                    break;
                }

                temp=minimax(0,false,-9999,9999);
                xo[i][j]=' ';

                if(temp>val)
                {
                    val=temp;
                    x=i;
                    y=j;
                }
            }
            if(flag==0) 
                break;
        }
        xo[x][y]='o';
    }

    
}

public class minmax {
    public static void main(String[] args) {

        Scanner s=new Scanner(System.in);
        int x,y;

        Board b=new Board();
        b.printBoard();

        for(int i=0;i<4;i++)
        {
            System.out.print("Enter row: ");
            x=s.nextInt();
            System.out.print("Enter column: ");
            y=s.nextInt();

            b.xo[x][y]='x';
            b.printBoard();
            if(b.isWin('x'))
            {
                System.out.println("x wins");
                return;
            }
            
            b.makeMoveO();
            b.printBoard();
            if(b.isWin('o'))
            {
                System.out.println("o wins");
                return;
            }
        }
        System.out.print("Enter row: ");
        x=s.nextInt();
        System.out.print("Enter column: ");
        y=s.nextInt();

        b.xo[x][y]='x';
        b.printBoard();
        if(b.isWin('x'))
        {
            System.out.println("x wins");
            return;
        }

        System.out.println("Tie");
    }
}
