package com.Andryyo.Reflecto;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: Андрей
 * Date: 09.04.13
 * Time: 19:57
 * To change this template use File | Settings | File Templates.
 */
public class Surface {
    Point p1;
    Point p2;
    float n1;
    float n2;
    public Surface(Point p1, Point p2, float n1, float n2)
    {
        if (p1.x>p2.x)
        {
            this.p1 = p2;
            this.p2 = p1;
            this.n1 = n2;
            this.n2 = n1;
        }
        else
        {
            this.p1 = p1;
            this.p2 = p2;
            this.n1 = n1;
            this.n2 = n2;
        }
    }

    public void draw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawLine(p1.x,p1.y,p2.x,p2.y,paint);
    }
}
