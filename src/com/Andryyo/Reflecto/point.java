package com.Andryyo.Reflecto;

import android.util.FloatMath;

/**
 * Created with IntelliJ IDEA.
 * User: Андрей
 * Date: 09.04.13
 * Time: 19:56
 * To change this template use File | Settings | File Templates.
 */
public class Point {
    float x,y;
    float distance = Float.NaN;
    float angle = Float.NaN;
    public Point()
    {
        x = 0;
        y = 0;
    }
    public Point(float x, float y)
    {
        this.x = x;
        this.y = y;
        distance = 0;
        angle = 0;
    }

    public Point(Point point)
    {
        this.x = point.x;
        this.y = point.y;
        this.distance = point.distance;
        this.angle = point.angle;
    }

    public void add(Point point)
    {
        x+=point.x;
        y+=point.y;
        distance = 0;
        angle = 0;
    }

    public float calculateDistance(Point point)
    {
        return FloatMath.sqrt((this.x-point.x)*(this.x-point.x)+(this.y - point.y)*(this.y - point.y));
    }

    public float calculateAngle(Point point)
    {
       float angle = (float)(Math.acos((point.x - x)/(this.calculateDistance(point))));
       angle *= Math.signum(point.y - y);
       //if (Math.signum(point.y - y)==-1)
       //     angle= (float) (Math.PI*2-angle);
       return angle;
    }
}
