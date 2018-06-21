package honmiv.snake;

import android.graphics.Point;

public class Snake {
    Point Coords[];
    int Length;
    int Dir;
    int PrevDir;

    Snake(int S) {
        Coords = new Point[S];
        for (int i = 0; i < S; i++)
            Coords[i] = new Point(-1, -1);
        Length = 0;
        Dir = PrevDir = 3; // 0-  up 1- down 2- left 3- right
        for (int i = 0; i < 4; i++) {
            Add(i, 0);
        }
    }

    private void Add(int x, int y) {
        Coords[Length].x = x;
        Coords[Length].y = y;
        Length++;
    }

    int Move(Meal meal, int S) {
        PrevDir = Dir;
        int dx = 0;
        int dy = 0;
        if (Dir == 0) dy = -1;
        if (Dir == 1) dy = 1;
        if (Dir == 2) dx = -1;
        if (Dir == 3) dx = 1;
        int cur_x = Coords[Length - 1].x + dx;
        int cur_y = Coords[Length - 1].y + dy;

        if (cur_x >= Math.sqrt(S) || cur_x < 0 || cur_y >= Math.sqrt(S) || cur_y < 0)
            return 0;
        else {
            for (int i = 0; i < Length; i++) {
                if (cur_x == Coords[i].x && cur_y == Coords[i].y)
                    return 0;
                if (cur_x == meal.Coords.x && cur_y == meal.Coords.y) {
                    meal.needNewCoords = true;
                    Add(cur_x, cur_y);
                    return 2;
                }
            }
            for (int i = 0; i < Length - 1; i++) {
                Coords[i].x = Coords[i + 1].x;
                Coords[i].y = Coords[i + 1].y;
            } //подтягивание к голове
            Coords[Length - 1].x = cur_x;
            Coords[Length - 1].y = cur_y;
            return 1;
        }
    }
}
