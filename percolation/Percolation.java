/* *****************************************************************************
 *  Name:              Florian Woerz
 *  Course:            Algorithms, Part I by Princeton University
 *  Submitted:         2022/02/25
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Simulates an (n x n)-grid percolation model. Initially, all `sites` in the matrix are `blocked`.
 * Includes a method to `open` sites by specifying coordinates. and check whether a site is open.
 * We say that a site is `full` if it is open and can be connected to an open site in the top row
 * via a chain of neighboring open sites. We say that the system `percolates` if there is a full
 * site in the bottom row.
 * Includes further methods to check if a site is full, return the number of open sites and check
 * if the system percolates.
 *
 * IDEA: Use a (n x n) Boolean array `grid` to track if a site is open or blocked. By convention,
 * the row and column indices are integers between 1 and n. We specify a site by its coordinated,
 * where the first entry in the tuple stands for the row and the second for the column. We use
 * the tuple (1, 1) to address the upper-left site.
 * Furthermore, we will use the Weighted Quick Union Find data structure to model the connected
 * components in the percolation system. We will include a virtual top node (called source; being
 * connected to all open sites in the top row) and a virtual bottom node (called sink; being
 * connected to all open sites in the bottom row). This will enable us to avoid checking n^2
 * top-bottom site combinations when asking if the system percolates.
 * However, this introduces a `backwash issue`: Open sites in the bottom row can incorrectly be
 * calculated as being full, although they might not be connected to an open site on the top. To
 * address this issue we include a second WeightedQuickUnionUF structure that does not include the
 * sink node.
 *
 * @author Florian Woerz
 */
public class Percolation {
    // Management of the grid
    private final int size;
    private final boolean[][] grid; // an entry is true iff the site is open
    private int numberOfOpenSites;

    // Management of the WeightedQuickUnionUF objects
    private final WeightedQuickUnionUF ufGrid;
    private final WeightedQuickUnionUF ufFull;
    private int indexOfSource;
    private int indexOfSink;

    /**
     * Creates an (n x n)-percolation object with all sites initially blocked.
     * This is done by creating an (n x n)-WeightedQuickUnionUF object with two extra nodes for the
     * virtual source and virtual sink. Also creates a Boolean array `grid` to keep track of whether
     * a node is open or blocked.
     * To avoid the backwash issue, we initialize a second (n x n)-WeightedQuickUnionUF object that
     * only contains one extra source node. This WeightedQuickUnionUF object will be used to check
     * for fullness of the percolation system while avoiding the backwash issue.     *
     * @param n Length and width of the percolation system
     */
    public Percolation(int n) {
        // Exception handling
        if (n <= 0) throw new IllegalArgumentException("n must be positive.");
        size = n;

        // Set up blocked grid (all entries have default value 'false')
        grid = new boolean[size][size];
        numberOfOpenSites = 0;

        // Set up Union-Find data structures as described above
        ufGrid = new WeightedQuickUnionUF(size * size + 2);
        ufFull = new WeightedQuickUnionUF(size * size + 1);
        indexOfSource = size * size;
        indexOfSink = size * size + 1;
    }


    /**
     * Opens the site (row, col) if it is not already open. The site will be unioned with all
     * adjacent open nodes.
     * @param row Row number of the site (1 <= row <= n)
     * @param col Column number of the site (1 <= col <= n)
     */
    public void open(int row, int col) {
        // Call our Exception handling procedure.
        checkRowAndColIndices(row, col);

        // In the problem statement, the row and col indices are between 1 and n. We need to adjust
        // them now because Java arrays are zero-based.
        row--;
        col--;

        // If the site is already open there is no need to open it again.
        if (grid[row][col]) return;

        // Otherwise, open up the site, increase number of open sites and connect adjacent sites.
        grid[row][col] = true;
        numberOfOpenSites++;
        connectAdjacentSites(row, col);
    }


    /**
     * Connects the site at (row - 1, col - 1) to all adjacent open sites in the ufGrid and ufFull
     * objects. NOTE: We have adjusted the indices in `open` to Java's zero-based system!
     * If the site is in the top row then it will be unioned with the virtual source.
     * If the site is in the bottom row it will be unioned with the virtual sink
     * (only in the ufGrid object to avoid the backwash issue).
     * @param row Row number of the site (0 <= row <= n-1)
     * @param col Column number of the site (0 <= col <= n-1)
     */
    private void connectAdjacentSites(int row, int col) {
        // Convert the row and col indices to the position in the union find data structure.
        int site = xyToUFPosition(row, col);

        // If we open a site at the top of the grid, we have to connect it to the source.
        if (row == 0) {
            ufGrid.union(indexOfSource, site);
            ufFull.union(indexOfSource, site);
        }

        // If the site is in the bottom row we only union the node in `ufGrid` to the sink
        // to avoid the backwash issue.
        if (row == size - 1) {
            ufGrid.union(site, indexOfSink);
        }

        // If the site at the "top" of the current site is open, union the two.
        if (row - 1 >= 0 && grid[row - 1][col]) {
            ufGrid.union(site, xyToUFPosition(row - 1, col));
            ufFull.union(site, xyToUFPosition(row - 1, col));
        }

        // If the site at the "bottom" of the current site is open, union the two.
        if (row + 1 < size && grid[row + 1][col]) {
            ufGrid.union(site, xyToUFPosition(row + 1, col));
            ufFull.union(site, xyToUFPosition(row + 1, col));
        }

        // If the site at the "left" of the current site is open, union the two.
        if (col - 1 >= 0 && grid[row][col - 1]) {
            ufGrid.union(site, xyToUFPosition(row, col - 1));
            ufFull.union(site, xyToUFPosition(row, col - 1));
        }

        // If the site at the "right" of the current site is open, union the two.
        if (col + 1 < size && grid[row][col + 1]) {
            ufGrid.union(site, xyToUFPosition(row, col + 1));
            ufFull.union(site, xyToUFPosition(row, col + 1));
        }

    }


    /**
     * Converts the zero-adjusted (row, col) coordinates to the UF index
     * @param row Adjusted row index
     * @param col Adjusted column index
     * @return UF index of site
     */
    private int xyToUFPosition(int row, int col) {
        return row * size + col;
    }


    /**
     * Checks whether the site at position (row, col) is open.
     * @param row Row number of the site (1 <= row <= n)
     * @param col Column number of the site (1 <= col <= n)
     */
    public boolean isOpen(int row, int col) {
        checkRowAndColIndices(row, col);
        return grid[row - 1][col - 1];
    }


    /**
     * Checks if the site at position (row, col) is full. Recall that a site is called full iff
     * it connects to the virtual source. For this check we use the ufFull WeightedQuickUnionUF
     * object which does not contain the virtual source node. Hence, we don't run into the backwash
     * issue.
     * @param row Row number of the site (1 <= row <= n)
     * @param col Column number of the site (1 <= col <= n)
     * @return True iff site at (row, col) is full
     */
    public boolean isFull(int row, int col) {
        checkRowAndColIndices(row, col);
        row--;
        col--;
        if (!grid[row][col]) return false; // if the site is blocked it cannot be full
        int site = xyToUFPosition(row, col);
        return ufFull.find(site) == ufFull.find(indexOfSource); // grid.connected(site, indexOfSource);
    }


    /**
     * Returns the number of open sites in the percolation system.
     * @return Number of open sites in the percolation system
     */
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }


    /**
     * Return if the system percolates. A system percolated iff the virtual source is in the same
     * connected component as the virtual sink.
     * @return True iff system percolates
     */
    public boolean percolates() {
        return ufGrid.find(indexOfSource) == ufGrid
                .find(indexOfSink); // connected(indexOfSource, indexOfSink);
    }


    /**
     * Throws an Exception if the given (row, col) coordinates are not valid, i.e., when they are
     * not contained in [n] x [n].
     * @param row Row number of the site (1 <= row <= n)
     * @param col Column number of the site (1 <= col <= n)
     */
    private void checkRowAndColIndices(int row, int col) {
        if (row > size || row <= 0 || col > size || col <= 0)
            throw new IllegalArgumentException(
                    "row or col index is out of bounds for the grid: (" + row + ", " + col + ")");
    }


    public static void main(String[] args) {
        StdOut.println("Please run PercolationStats instead.");
    }

}
