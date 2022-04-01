/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

// Brute-force implementation. Write a mutable data type PointSET.java that represents a set of
// points in the unit square. Implement the following API by using a red–black BST (you must use
// either SET or java.util.TreeSet; do not implement your own red–black BST.
public class PointSET {
    // Corner cases.  Throw an IllegalArgumentException if any argument is null.
    // Performance requirements.  Your implementation should support insert() and contains() in
    // time proportional to the logarithm of the number of points in the set in the worst case;
    // it should support nearest() and range() in time proportional to the number of points in the
    // set.

    private final TreeSet<Point2D> pointSet;


    // construct an empty set of points
    public PointSET() {
        pointSet = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null.");
        pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null.");
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        // 
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        return null;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        return null;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        //
    }
}
