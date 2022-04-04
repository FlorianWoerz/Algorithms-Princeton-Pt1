/* *****************************************************************************
 *  Name:              Florian Woerz
 *  Course:            Algorithms, Part I by Princeton University
 *  Submitted:         2022-04-01
 *  Score:             97/100
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;


/**
 * Provides a data type to represent a set of points in the unit square (i.e., all points have x-
 * and y-coordinates between 0 and 1) using a 2d-tree to support efficient range search (finding of
 * all of the points contained in a query rectangle) and nearest-neighbor search (find a closest
 * point to a query point). We will implement a brute-force approach, *NOT* using a red-black BST.
 * <p>
 * Our implementation supports insert() and contains() in time proportional to the logarithm of the
 * number of points in the set in the worst case.
 * It furthermore supports nearest() and range() in time proportional to the number of points in the
 * set.
 *
 * @author Florian Woerz
 */
public class PointSET {

    private final TreeSet<Point2D> pointSet;


    /**
     * Constructs an empty set of points
     */
    public PointSET() {
        pointSet = new TreeSet<Point2D>();
    }


    /**
     * Checks if the set is empty
     *
     * @return true if the set is empty; false if it contains at least one point
     */
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }


    /**
     * Returns the number of points in the set
     *
     * @return number of points in the set
     */
    public int size() {
        return pointSet.size();
    }


    /**
     * Adds the point p to the set (if it is not already in the set)
     *
     * @param p point to add
     */
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null.");
        pointSet.add(p);
    }


    /**
     * Checks if the set contains a point p
     *
     * @param p point to check containment
     * @return true if the set contains p; false otherwise
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null.");
        return pointSet.contains(p);
    }


    /**
     * Draws all points to standard draw
     */
    public void draw() {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : pointSet) {
            p.draw();
        }
    }


    /**
     * Returns an iterable for all points that are inside the specified rectangle
     * (or on the boundary)
     *
     * @param rect Rectangle whose contained points should be included in the iterable
     * @return iterable over points inside the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Rectangle cannot be null.");
        Queue<Point2D> queue = new Queue<Point2D>();
        for (Point2D p : pointSet) {
            if (rect.contains(p)) {
                queue.enqueue(p);
            }
        }
        return queue;
    }


    /**
     * Returns a nearest neighbor in the set to point p; null if the set is empty
     *
     * @param p point whose nearest neighbor should be returned
     * @return a nearest neighbor point in the set w.r.t. point p; null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null.");
        if (isEmpty()) {
            return null;
        } else {
            Point2D minDistancePoint = null;
            for (Point2D q : pointSet) {
                // Do not call 'distanceTo()' in this program; instead use 'distanceSquaredTo()'. [Performance]
                if (minDistancePoint == null || p.distanceSquaredTo(q) < p.distanceSquaredTo(minDistancePoint)) {
                    minDistancePoint = q;
                }
            }
            return minDistancePoint;
        }
    }

    // Unit testing of the methods (optional)
    public static void main(String[] args) {
        // No tests included.
    }
}
