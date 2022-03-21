import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return false;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return 0;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return null;
    }

    // test client (see below)
    public static void main(String[] args) {
        // Todo: ...

        // create initial board from file
        In in = new In("8puzzle/puzzle3x3-unsolvable1.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

        Board board = new Board(tiles);

        // Test some basic calls
        int manhattan = board.manhattan();

        // Test priority queue implementation of Princeton
        MinPQ<Integer> pq = new MinPQ<>();
    }

}
