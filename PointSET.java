import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;

public class PointSET {
    private final SET<Point2D> pointSet;
    public PointSET() {
        this.pointSet = new SET<Point2D>();
    }                            // construct an empty set of points
    public boolean isEmpty() {
        return this.pointSet.isEmpty();
    }                      // is the set empty?
    public int size() {
        return this.pointSet.size();
    }                         // number of points in the set
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (contains(p)) return;
        this.pointSet.add(p);
    }             // add the point to the set (if it is not already in the set)
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return this.pointSet.contains(p);
    }          // does the set contain point p?
    public void draw() {
        StdDraw.setPenColor(Color.BLUE);
        for (Point2D point : this.pointSet) {
            StdDraw.filledSquare(point.x(), point.y(), 1);
        }
    }                        // draw all points to standard draw
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        SET<Point2D> pointStack = new SET<Point2D>();
        for (Point2D point : this.pointSet) {
            if (rect.contains(point)) pointStack.add(point);
        }
        return pointStack;
    }          // all points that are inside the rectangle (or on the boundary)
    public Point2D nearest(Point2D p) {
        Point2D nearestPoint = null;
        double distance = 2;
        for (Point2D point : this.pointSet) {
            if (point.equals(p)) return point;
            double cmp = p.distanceSquaredTo(point);
            if (distance >= cmp) {
                nearestPoint = point;
                distance = cmp;
            }
        }
        return nearestPoint;
    }           // a nearest neighbor in the set to point p; null if the set is empty

    @Override
    public String toString() {
        return this.pointSet.toString();
    }

    public static void main(String[] args) {
        PointSET pointSet = new PointSET();
        pointSet.insert(new Point2D(2, 2));
        pointSet.insert(new Point2D(1, 3));
        pointSet.insert(new Point2D(6, 1));
        StdOut.println(pointSet);
        StdOut.println(pointSet.size());
        StdOut.println(pointSet.isEmpty());
        StdOut.println(pointSet.nearest(new Point2D(2, 3)));
        StdOut.println(pointSet.range(new RectHV(0, 0, 4, 2)));
    }             // unit testing of the methods (optional)
}
