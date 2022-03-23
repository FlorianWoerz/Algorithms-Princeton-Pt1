/* *****************************************************************************
 *  Name:              Florian Woerz
 *  Course:            Algorithms, Part I by Princeton University
 *  Submitted:         2022-03-23
 *  Score:             100/100
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

/**
 * Creates a data type that models an n-by-n board with sliding tiles by implementing an immutable
 * data type Board.
 *
 * @author Florian Woerz
 */
public class Board {

    private final int[][] board;
    private final int dimension;

    /**
     * The constructor creates a board from an n-by-n array of tiles, where tiles[row][col]
     * specifies the tile at coordinates (row, col). We will assume that the constructor receives
     * an n-by-n array containing n^2 integers between 0 and n^2 - 1, where 0 represents the blank
     * square. It can also be assumed that 2 ≤ n < 128.
     *
     * @param tiles 2-dimensional array containing the tile entries
     */
    public Board(int[][] tiles) {
        // By the specification of Princeton we can assume that the puzzle inputs (i.e., the
        // arguments to the Board constructor and input to Solver) are valid. It however never
        // hurts to include some basic error checking. This error checking is far from complete!
        if (tiles == null || tiles.length != tiles[0].length) {
            throw new IllegalArgumentException("The input is not valid.");
        }

        dimension = tiles.length;
        this.board = makeImmutableCopy(tiles); // We will make the board immutable
    }

    /**
     * Makes an immutable copy of the `tiles` array received by the constructor Board().
     *
     * @param tiles 2-dimensional array containing the tile entries
     * @return copy of the `tiles` array
     */
    private int[][] makeImmutableCopy(int[][] tiles) {
        int[][] copy = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                copy[i][j] = tiles[i][j];
            }
        }
        return copy;
    }

    /**
     * Implements a string representation of the board.
     *
     * @return a string composed of n + 1 lines:
     * The first line contains the board size n; the remaining n lines contain the n-by-n grid of
     * tiles in row-major order, using 0 to designate the blank square.
     */
    public String toString() {
        StringBuilder representation = new StringBuilder();
        representation.append(dimension() + "\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                representation.append(String.format("%d ", board[i][j]));
            }
            representation.append("\n");
        }
        return representation.toString();
    }

    /**
     * Returns the board dimension n
     *
     * @return board dimension
     */
    public int dimension() {
        return dimension;
    }

    /**
     * Calculates and returns the number of tiles out of place, i.e., the Hamming distance to the
     * unique solution board
     * 1 2 3
     * 4 5 6
     * 7 8 0
     * This is a valid distance metric.
     *
     * @return Hamming distance to the solution board
     */
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (xyCoordinateTo1D(i, j) != board[i][j]) {
                    hamming++;
                }
            }
        }
        // Recall that the empty tile should be at the bottom right of the solution. We will not
        // treat it as a tile when computing the Hamming distance.
        return hamming - 1;
    }

    /**
     * Calculates the expected tile entry of the solution at position (i, j), where we will use
     * Java's 0-based indexing!
     *
     * @param i coordinate, 0 <= i <= n - 1
     * @param j coordinate, 0 <= j <= n - 1
     * @return expected tile entry at (i, j) with 0-based indexing
     */
    private int xyCoordinateTo1D(int i, int j) {
        return i * dimension() + j + 1;
    }

    /**
     * Return the sum of the Manhattan distances between tiles and the tiles of the goal board,
     * i.e., the sum of the vertical and horizontal distance from the tiles to their goal positions.
     *
     * @return Sum of Manhattan distances of board to goal board
     */
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (board[i][j] == 0) continue; // the empty tile is not considered
                int[] correctCoordinates = numberToXYPosition(board[i][j]);
                int xDiff = Math.abs(correctCoordinates[0] - i);
                int yDiff = Math.abs(correctCoordinates[1] - j);
                manhattan += xDiff + yDiff;
            }
        }
        return manhattan;
    }

    /**
     * Return an array of size two specifying the correct coordinates (0-based) of the integer
     * `num` in the goal board.
     *
     * @param num the tile number whose solution coordinates we are interested in
     * @return solution coordinates of `num` (0-based!)
     */
    private int[] numberToXYPosition(int num) {
        return new int[] { (num - 1) / dimension(), (num - 1) % dimension() };
    }

    /**
     * Checks whether the board is the goal board.
     *
     * @return true if and only if the board is the goal board.
     */
    public boolean isGoal() {
        return manhattan() == 0; // could have also checked if hamming() == 0
    }


    /**
     * Compares two boards for equality: Compares this board to the specified board `y`.
     * Two boards are equal if and only if they have
     * -- the same size and
     * -- their corresponding tiles are in the same positions.
     * The equals() method is inherited from java.lang.Object, so it must obey all of Java’s
     * requirements. Java’s convention is that equals() must be an equivalence relation. It must be
     * -- reflexive  : x.equals(x) is true.
     * -- symmetric  : x.equals(y) is true if and only if y.equals(x).
     * -- transitive : if x.equals(y) and y.equals(z) are true, then so is x.equals(z).
     * In addition, it must take an Object as argument and satisfy the following properties:
     * -- consistent : multiple invocations of x.equals(y) consistently return the same value,
     * provided neither object is modified.
     * -- Not null   : x.equals(null) returns false.
     *
     * @param y the other board
     * @return true if this board equals y; false otherwise
     */
    @Override
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;

        if (that.dimension() != this.dimension()) return false;

        // Could have also used Arrays.deepEquals()
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (that.board[i][j] != this.board[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // all neighboring boards

    /**
     * Returns an iterable of all neighboring boards. A board is said to be neighboring to this
     * board if it can be obtained by a legal move (i.e., by sliding a tile to the empty space).
     *
     * @return iterable of all neighboring boards
     */
    public Iterable<Board> neighbors() {

        Stack<Board> neighborStack = new Stack<>();

        // Get the coordinates of the empty tile
        int[] emptyTile = emptyTile();
        int i = emptyTile[0];
        int j = emptyTile[1];

        // Depending on the position of the empty tile, we can have 2, 3, or 4 neighboring boards
        if (i > 0)
            neighborStack.push(new Board(swap(i, j, i - 1, j)));
        if (i < dimension() - 1)
            neighborStack.push(new Board(swap(i, j, i + 1, j)));
        if (j > 0)
            neighborStack.push(new Board(swap(i, j, i, j - 1)));
        if (j < dimension() - 1)
            neighborStack.push(new Board(swap(i, j, i, j + 1)));

        return neighborStack;
    }

    /**
     * Return the coordinates of the empty tile in an array of size 2.
     *
     * @return coordinate array of empty tile (0-based!)
     */
    private int[] emptyTile() {
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (board[i][j] == 0) {
                    return new int[] { i, j };
                }
            }
        }
        return new int[] { -1, -1 }; // this never happens for a correct game board
    }

    /**
     * Swaps the tiles at coordinates (i1, j1) and (i2, j2), where the coordinates are 0-based.
     *
     * @param i1 first coordinate of first tile
     * @param j1 second coordinate of first tile
     * @param i2 first coordinate of second tile
     * @param j2 second coordinate of second tile
     * @return modified tile array
     */
    private int[][] swap(int i1, int j1, int i2, int j2) {
        int[][] copy = makeImmutableCopy(board);
        int tmp = copy[i1][j1];
        copy[i1][j1] = copy[i2][j2];
        copy[i2][j2] = tmp;
        return copy;
    }

    /**
     * Returns a "twin" board to this board, i.e., a board that is obtained by exchanging any pair
     * of tiles. This will be used to determine whether a puzzle is solvable: exactly one of a
     * board and its twin are solvable. A twin is obtained by swapping any pair of tiles (the
     * blank square is not a tile). For example, here is a board and several possible twins. Our
     * solver will use only one twin, thus it suffices to return one twin.
     * 1  3       3  1       1  3       1  3       1  3       6  3
     * 4  2  5    4  2  5    2  4  5    4  5  2    4  2  5    4  2  5
     * 7  8  6    7  8  6    7  8  6    7  8  6    8  7  6    7  8  1
     * board      twin       twin       twin       twin       twin
     *
     * @return canonical twin to this board (i.e., the twin where entries (0,0) and (0,1) are
     * exchanged if they are both non-empty; the twin where entries (1,0) and (1,1) are exchanged
     * otherwise.
     */
    public Board twin() {

        int[][] twinTiles = makeImmutableCopy(this.board);

        if (twinTiles[0][0] != 0 && twinTiles[0][1] != 0)
            return new Board(swap(0, 0, 0, 1));
        else
            return new Board(swap(1, 0, 1, 1));
    }

    // Unit testing (not graded)
    public static void main(String[] args) {

        int[][] newtry = { { 1, 0 }, { 2, 3 } };
        Board newboard = new Board(newtry);

        System.out.println(newboard.twin().toString());
    }

}
