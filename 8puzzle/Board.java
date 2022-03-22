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
        return dimension;
    }

    // number of tiles out of place
    // To measure how close a board is to the goal board, we define two notions of distance.
    // The Hamming distance between a board and the goal board is the number of tiles in the wrong position.
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (xyCoordinateTo1D(i, j) != board[i][j]) {
                    hamming++;
                }
            }
        }
        return hamming - 1; // empty tile should be at the bottom right
    }

    private int xyCoordinateTo1D(int i, int j) {
        return i * dimension() + j + 1;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (board[i][j] == 0) continue;
                // System.out.println("Coordinates: " + i + ", " + j);
                int[] correctCoordinates = numberToXYPosition(board[i][j]);
                int xDiff = Math.abs(correctCoordinates[0] - i);
                int yDiff = Math.abs(correctCoordinates[1] - j);
                // System.out.println("Correct Coordinate for " + board[i][j] + " at " + i + ", " + j + " : " + correctCoordinates[0] + ", " + correctCoordinates[1]);
                // System.out.println("Adding " + xDiff + yDiff);
                manhattan += xDiff + yDiff;
                }
            }
        return manhattan;
    }

    private int[] numberToXYPosition(int num) {
        return new int[]{(num - 1) / dimension(), (num - 1) % dimension()};
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0; // could have also checked if hamming() == 0
    }

    // does this board equal y?
    // Comparing two boards for equality. Two boards are equal if they have the same size and
    // their corresponding tiles are in the same positions. The equals() method is inherited from
    // java.lang.Object, so it must obey all of Java’s requirements.
    @Override
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;

        if (that.dimension() != this.dimension()) return false;

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
    public Iterable<Board> neighbors() {
        Stack<Board> neighborStack = new Stack<>();

        int[] emptyTile = emptyTile();
        int i = emptyTile[0];
        int j = emptyTile[1];

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

    private int[][] swap(int i1, int j1, int i2, int j2) {
        int[][] copy = makeImmutableCopy(board);
        int tmp = copy[i1][j1];
        copy[i1][j1] = copy[i2][j2];
        copy[i2][j2] = tmp;
        return copy;
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
