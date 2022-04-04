/* *****************************************************************************
 *  Name:              Florian Woerz
 *  Course:            Algorithms, Part I by Princeton University
 *  Submitted:         2022/03/10
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Examines 4 points at a time and checks whether they all lie on the same line segment, returning
 * all such line segments. To check whether the 4 points p, q, r, and s are collinear, it checks
 * whether the three slopes between p and q, between p and r, and between p and s are all equal.
 *
 * @author Florian Woerz
 */
public class BruteCollinearPoints {

    private final LineSegment[] lineSegments;

    /**
     * Finds all line segments containing 4 points
     * @param points
     */
    public BruteCollinearPoints(Point[] points) {
        // Corner cases. Throw an IllegalArgumentException if the argument to the constructor is
        // null, if any point in the array is null, or if the argument to the constructor contains
        // a repeated point.

        // Check for null argument
        if (points == null) throw new IllegalArgumentException("The array `points` is null.");

        // Check for null-points in the array
        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException("The array contains a null element.");
        }

        // Check for duplicate-points (this should be done while avoiding a hash set since they have
        // not been covered in class up to this point). This could have also been done with two
        // for loops amounting to O(n^2) time, where n := points.length. Hence, sorting the array
        // first with a worst-case cost of O(n*log(n)) is cheaper. Then we can iterate once over
        // the array for a cost of O(n).

        final int N = points.length;

        // Test 11: check that data type does not mutate the constructor argument
        // data type mutated the points[] array
        // data type should have no side effects unless documented in API

        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);

        for (int i = 1; i < N; i++) {
            if (sortedPoints[i].equals(sortedPoints[i-1])) {
                // got a duplicate element
                throw new IllegalArgumentException("The array contains a duplicate entry.");
            }
        }

        // We don't waste time micro-optimizing the brute-force solution. Though, there are two
        // easy opportunities. First, we can iterate through all combinations of 4 points
        // (N choose 4) instead of all 4 tuples (N^4), saving a factor of 4! = 24.
        // Second, we don't need to consider whether 4 points are collinear if we already know that
        // the first 3 are not collinear; this can save a factor of N on typical inputs.

        List<LineSegment> list = new LinkedList<>();

        // p,q,r,s
        for (int p = 0; p < N-3; p++) {
            Point pointP = sortedPoints[p];

            for (int q = p + 1; q < N-2; q++) {
                Point pointQ = sortedPoints[q];
                double slopePQ = pointP.slopeTo(pointQ);

                for (int r = q + 1; r < N - 1; r++) {
                    Point pointR = sortedPoints[r];
                    double slopePR = pointP.slopeTo(pointR);
                    if (slopePQ == slopePR) {

                        for (int s = r + 1; s < N; s++) {
                            Point pointS = sortedPoints[s];
                            double slopePS = pointP.slopeTo(pointS);
                            if (slopePQ == slopePS) {
                                list.add(new LineSegment(pointP, pointS));
                            }
                        }
                    }
                }
            }
        }

        lineSegments = list.toArray(new LineSegment[list.size()]);


    }

    /**
     * The number of line segments.
     * @return
     */
    public int numberOfSegments() {
        return lineSegments.length;
    }

    /**
     * Includes each line segment containing 4 points exactly once. If 4 points appear on a line
     * segment in the order p→q→r→s, the method includes either the line segment p→s or s→p (but
     * not both) and does not include subsegments such as p→r or q→r. For simplicity, do not
     * supply any input to BruteCollinearPoints that has 5 or more collinear points.
     */
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    /**
     * Simple test client provided by Princeton University.
     * This client program takes the name of an input file as a command-line argument;
     * reads the input file (in the format specified below);
     * prints to standard output the line segments that our program discovers, one per line;
     * and draws to standard draw the line segments.
     *
     * Input format. Princeton University supplied several sample input files in the following
     * format: An integer n, followed by n pairs of integers (x, y), each between 0 and 32,767.
     * Below are two examples.
     *
     * % cat input6.txt        % cat input8.txt
     * 6                       8
     * 19000  10000             10000      0
     * 18000  10000                 0  10000
     * 32000  10000              3000   7000
     * 21000  10000              7000   3000
     *  1234   5678             20000  21000
     * 14000  10000              3000   4000
     *                          14000  15000
     *                           6000   7000
     *
     * % java-algs4 BruteCollinearPoints input8.txt
     * (10000, 0) -> (0, 10000)
     * (3000, 4000) -> (20000, 21000)
     *
     * % java-algs4 FastCollinearPoints input8.txt
     * (3000, 4000) -> (20000, 21000)
     * (0, 10000) -> (10000, 0)
     *
     * % java-algs4 FastCollinearPoints input6.txt
     * (14000, 10000) -> (32000, 10000)
     */
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
