/******************************************************************************
 *  Compilation:
 *  Execution:
 *  Dependencies:
 *  This program
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int size;
    private final boolean[][] grid;
    private int openSites;
    private final WeightedQuickUnionUF uf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        if ( n <= 0 ) throw new IllegalArgumentException("ERROR: The grid must how at least one row and one column.");
        this.size = n;

        this.grid = new boolean[n][n];
        this.openSites = 0;

        // n square + 2 ghost sites:
        // All top-row sites will connect to the Virtual Top [ index 0 ]
        // All bottom-row sites will connect to the Virtual Bottom [ index n*n + 1 ]
        // CHECK Percolation: index 0 connected to n*n+1.
        this.uf = new WeightedQuickUnionUF(n * n + 2);
    }

    // A function to get the index of current:
    private int getIndex ( int row, int col){
        return ( row - 1 ) * size + col;
    }


    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        if (row < 1 ) throw new IllegalArgumentException("ERROR: The grid must have at least 1 row!");
        if (row > size ) throw new IllegalArgumentException("ERROR: Out of Bounds, row must now exceed expected number of rows!");

        if (col < 1 ) throw new IllegalArgumentException("ERROR: The grid must have at least 1 row!");
        if (col > size ) throw new IllegalArgumentException("ERROR: Out of Bounds, row must now exceed expected number of rows!");

        if (!isOpen( row, col )){
            grid[row-1][col-1] = true;
            openSites++;

            // Calculate current: Same for all unions
            int current = getIndex( row, col );
            int neighbor;

            // Connect to Virtual Top
            if ( row == 1){
                uf.union(current, 0);
            }

            if ( row == size ){
                uf.union(current, size * size + 1);
            }

            // Connecting Neighbors: First check UP, DOWN, LEFT, RIGHT
            // Checking UP:
            if ( row > 1 && isOpen(row-1, col)){
                neighbor = getIndex(row - 1, col);
                uf.union(current, neighbor);
            }

            // Checking Down:
            if ( row < size && isOpen (row + 1, col)){
                neighbor = getIndex(row + 1, col);
                uf.union(current, neighbor);
            }

            // Checking Left:
            if ( col > 1 && isOpen( row, col - 1)){
                neighbor = getIndex(row, col - 1);
                uf.union(current, neighbor);
            }

            // Checking Right:
            if ( col < size && isOpen(row, col + 1)){
                neighbor = getIndex(row, col + 1);
                uf.union(current, neighbor);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        if (row < 1 ) throw new IllegalArgumentException("ERROR: The grid must have at least 1 row!");
        if (row > size ) throw new IllegalArgumentException("ERROR: Out of Bounds, row must now exceed expected number of rows!");

        if (col < 1 ) throw new IllegalArgumentException("ERROR: The grid must have at least 1 row!");
        if (col > size ) throw new IllegalArgumentException("ERROR: Out of Bounds, row must now exceed expected number of rows!");

        return grid[row-1][col-1];

    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        if (row < 1 ) throw new IllegalArgumentException("ERROR: The grid must have at least 1 row!");
        if (row > size ) throw new IllegalArgumentException("ERROR: Out of Bounds, row must now exceed expected number of rows!");

        if (col < 1 ) throw new IllegalArgumentException("ERROR: The grid must have at least 1 row!");
        if (col > size ) throw new IllegalArgumentException("ERROR: Out of Bounds, row must now exceed expected number of rows!");

        return isOpen( row, col ) && uf.find(getIndex( row, col )) == uf.find(0 );
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return openSites;
    }

    // does the system percolate?
    public boolean percolates(){
        return uf.find( 0 ) == uf.find( size * size + 1 );
    }

    // test client (optional)
    static void main(String[] ignoredArgs){
        int n = 3;
        Percolation p = new Percolation(n);

        System.out.println("Initial percolation: " + p.percolates()); // Should be false

        System.out.println("Opening (1, 2)...");
        p.open(1, 2);
        System.out.println("Is (1, 2) full? " + p.isFull(1, 2)); // Should be true

        System.out.println("Opening (2, 2)...");
        p.open(2, 2);
        System.out.println("Is (2, 2) full? " + p.isFull(2, 2)); // Should be true

        System.out.println("System percolates? " + p.percolates()); // Should be false (bottom not open)

        System.out.println("Opening (3, 2)...");
        p.open(3, 2);

        System.out.println("Final open sites: " + p.numberOfOpenSites()); // Should be 3
        System.out.println("System percolates? " + p.percolates()); // Should be TRUE

        // Test an isolated site
        p.open(3, 1);
        System.out.println("Is (3, 1) full? " + p.isFull(3, 1)); // Should be false (unless backwash exists)

    }
}
