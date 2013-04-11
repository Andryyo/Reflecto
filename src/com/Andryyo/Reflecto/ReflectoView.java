package com.Andryyo.Reflecto;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Андрей
 * Date: 09.04.13
 * Time: 19:06
 * To change this template use File | Settings | File Templates.
 */
public class ReflectoView extends View {

    private Vector<Point> vertexes = new Vector<Point>();
    private Vector<Surface> surfaces = new Vector<Surface>();
    private Vector<Line> past = new Vector<Line>();
    private Vector<Line> future = new Vector<Line>();
    private Vector<Point> unusedvertexes = new Vector<Point>();
    Line line;

    public ReflectoView(Context context, AttributeSet attrs)    {
        super(context,attrs);
        Line lines[] = new Line[1];
        lines[0] = new Line(new Point(0,0),new Point(1,1),null,1,1);
        restart(lines);
        invalidate();

    }

    public void restart(Line[] lines)   {
        future.removeAllElements();
        past.removeAllElements();
        future.addAll(Arrays.asList(lines));
        Iterator<Line> i = future.iterator();
            while (i.hasNext()) {
                Line line = i.next();
                Surface surface = line.getNearestSurface(surfaces, vertexes);
                if (surface == null) {
                    i.remove();
                    i = future.iterator();
                    line.extend();
                    past.add(line);
                } else {
                    i.remove();
                    future.addAll(Arrays.asList(line.reflect()));
                    i = future.iterator();
                    past.add(line);
                }
            }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if (me.getAction()==MotionEvent.ACTION_DOWN)
        {
            Point point = new Point(me.getX(),me.getY());
            unusedvertexes.add(point);
            if (unusedvertexes.size()==2)
            {
                vertexes.add(unusedvertexes.lastElement());
                unusedvertexes.remove(unusedvertexes.lastElement());
                vertexes.add(unusedvertexes.lastElement());
                unusedvertexes.remove(unusedvertexes.lastElement());
                surfaces.add(new Surface(vertexes.get(vertexes.size()-1),
                        vertexes.get(vertexes.size()-2),1,1));
                Line lines[] = new Line[1];
                lines[0] = new Line(new Point(0,0),new Point(1,1),null,1,1);
                restart(lines);
                invalidate();
            }
        }
        return true;
    }

    @Override
    public void onDraw(Canvas canvas)   {
           for (Line line : past)
               line.draw(canvas);
           for (Surface surface : surfaces)
               surface.draw(canvas);
    }
}
