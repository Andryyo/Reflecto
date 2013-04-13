package com.Andryyo.Reflecto;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.FloatMath;

import java.util.Iterator;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Андрей
 * Date: 09.04.13
 * Time: 19:35
 * To change this template use File | Settings | File Templates.
 */
public class Line {
    public Point start;
    public Point end;
    public Point crossing;
    private float angle;
    private float Av;
    private float Ah;
    private float n;
    private Surface startSurface = null;
    private Surface nearestSurface = null;

    public Line(Point start, Point end, Surface startSurface,float Av, float Ah)  {
        this.start = start;
        this.end = end;
        this.Ah = Ah;
        this.Av = Av;
        this.startSurface = startSurface;
        angle = start.calculateAngle(end);
    }

    public Surface getNearestSurface(Vector<Surface> surfaces, Vector<Point> vertexes)   {
        Vector<Surface> candidates = new Vector<Surface>(surfaces);
        if (startSurface!=null)
            candidates.remove(startSurface);
        Iterator<Surface> i = candidates.iterator();
        while (i.hasNext())
        {
            Surface surface = i.next();
            if (end.x>start.x)
            {
                if (surface.p2.x<start.x)
                {
                    i.remove();
                    i = candidates.iterator();
                }
            }
            else
                if (surface.p1.x>start.x)
                {
                    i.remove();
                    i = candidates.iterator();
                }
            else
            {
            Point pmin = surface.p1;
            Point pmax = surface.p2;
            if (pmin.y>pmax.y)
                {
                    pmin = surface.p2;
                    pmax = surface.p1;
                }
            if (end.y>start.y)
            {
                if (pmax.y<start.y)
                    {
                        i.remove();
                        i = candidates.iterator();
                    }
            }
            else
                if (pmin.y>start.y)
                {
                    i.remove();
                    i = candidates.iterator();
                }
            }
        }
        if (candidates.isEmpty()) return null;
        for (Surface surface : candidates)
        {
            surface.p1.distance = surface.p1.calculateDistance(this.start);
            surface.p1.angle = this.start.calculateAngle(surface.p1);
            surface.p2.distance = surface.p2.calculateDistance(this.start);
            surface.p2.angle = this.start.calculateAngle(surface.p2);
        }
        i = candidates.iterator();
        while (i.hasNext())
        {
            Surface surface = i.next();
            float angle1 = Math.min(surface.p1.angle,surface.p2.angle);
            float angle2 = Math.max(surface.p1.angle,surface.p2.angle);
            float buf = angle;
            if (Math.abs(angle2 - angle1)>Math.PI)
                {
                    buf -= Math.PI+angle1;
                    if (buf < -Math.PI)
                        buf += (float) (2*Math.PI);
                    angle2 -= Math.PI+angle1;
                    angle1 = (float) Math.PI;
                    float t;
                    t = angle1;
                    angle1 = angle2;
                    angle2 = t;
                }
            if ((buf<angle1)||
                (buf>angle2))
                {
                    i.remove();
                    i = candidates.iterator();
                }
        }
        if (candidates.isEmpty()) return null;
        float shortestdistance = Float.POSITIVE_INFINITY;
        Point buf = new Point();
        for (Surface surface : candidates)
        {
            float distance = calculateDistance(surface);
            if (distance<shortestdistance)
            {
                shortestdistance = distance;
                buf = new Point(crossing);
                nearestSurface = surface;
            }
        }
        crossing = buf;
        end = buf;
        return nearestSurface;
    }

    public Line[] reflect()
    {
        if (nearestSurface == null)
            return new Line[0];
        else
            {
                Line[] lines = new Line[1];
                Point end = new Point(this.crossing);
                float b = nearestSurface.p1.calculateAngle(nearestSurface.p2);
                end.x+=FloatMath.cos(-angle+2*b);
                end.y+=FloatMath.sin(-angle+2*b);
                lines[0] = new Line(crossing,end,nearestSurface,1,1);
                return (lines);
            }
    }

    public void draw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawLine(start.x,start.y,end.x,end.y,paint);
    }

    private float calculateDistance(Surface surface)    {
        float A1 = start.y-end.y;
        float B1 = end.x-start.x;
        float C1 = start.x*end.y - end.x*start.y;
        float A2 = surface.p1.y-surface.p2.y;
        float B2 = surface.p2.x-surface.p1.x;
        float C2 = surface.p1.x*surface.p2.y - surface.p2.x*surface.p1.y;
        crossing = new Point((B1*C2-B2*C1)/(A1*B2-A2*B1),
                                    ((C1*A2-C2*A1)/(A1*B2-A2*B1)));
        return crossing.calculateDistance(start);
    }

    public void extend()
    {
        end.x += (end.x - start.x)*1000;
        end.y += (end.y - start.y)*1000;
    }

}
