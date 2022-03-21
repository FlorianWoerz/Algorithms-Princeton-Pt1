// To begin, create a data type that models an n-by-n board with sliding tiles. Implement an immutable data type Board with the following API:
public class Board {

    private final int[][] board;
    private final int dimension; // The private instance (or static) variable 'dimension' can be made 'final'; it is initialized only in the declaration or constructor.

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dimension = tiles.length;
        // make board immutable
        // check that Board is immutable by changing argument array after
        // construction and making sure Board does not mutate
        this.board = makeImmutable(tiles);

    }

    private int[][] makeImmutable(int[][] tiles) {
        int[][] copy = new int[tiles.length][tiles.length];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                copy[i][j] = tiles[i][j];
            }
        }
        return copy;
    }

    // string representation of this board
    public String toString() {
        StringBuilder representation = new StringBuilder();
        representation.append(dimension() + "\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                representation.append(String.format("%d ", board[i][j]));
            }
        }
        return representation.toString();
    }

    // board dimension n
    public int dimension() {
        return 0;
    }

    // number of tiles out of place
    public int hamming() {
        return 0;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return 0;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return false;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return null;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // Todo...
    }

}
