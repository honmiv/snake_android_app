package honmiv.snake;

import android.graphics.Point;
import java.util.Random;

public class Meal {
    Point Coords = new Point(0,0);

    boolean needNewCoords = false;

    private int randomInd;

    public void newCoords(Snake snake, int S)
    {
        needNewCoords=false;
        Point freeSpace[]=new Point[S];
        for(int i =0; i<S;i++)
            freeSpace[i]=new Point(0,0);
        int field[][]=new int[(int)Math.sqrt(S)][(int)Math.sqrt(S)];
        int freeSpaceCounter=0;
        for(int i =0; i<snake.Length;i++)
            if (snake.Coords[i].x != -1)
                field[snake.Coords[i].y][snake.Coords[i].x] = 1;

        for (int i =0; i<(int)Math.sqrt(S);i++)
        {
            for (int j =0; j<(int)Math.sqrt(S);j++) {
                if(field[i][j]!=1)
                    field[i][j]=0;
                if(field[i][j]==0)
                {
                    freeSpace[freeSpaceCounter].x=j;
                    freeSpace[freeSpaceCounter].y=i;
                    freeSpaceCounter++;
                }
            }
        }

        randomInd = new Random().nextInt(S-snake.Length);
        Coords.x = freeSpace[randomInd].x;
        Coords.y = freeSpace[randomInd].y;
    }
}
