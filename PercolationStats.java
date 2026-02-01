/******************************************************************************
 *  Compilation:
 *  Execution:
 *  Dependencies:
 *  This program
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

import java.util.Scanner;

public class PercolationStats {
    private final double[] results;
    private final int numberOfTrials;

    public PercolationStats(int n, int trials){
        // perform independent trials on an n-by-n grid
        if (n <=  0) throw new IllegalArgumentException("Error: n x n grid parameters must be greater than or equal to zero");
        if ( trials <= 0 ) throw new IllegalArgumentException("Trials must be greater than or equal to zero.");

        this.numberOfTrials = trials;
        this.results = new double[trials];

        for ( int i = 0; i < trials; i++){
            // Creating a new Percolation object
            Percolation percolate = new Percolation(n);

            // Pick a random row and col to open
            while (!percolate.percolates()){
                int row = StdRandom.uniformInt(1, n+1);
                int col = StdRandom.uniformInt(1, n+1);
                percolate.open(row,col);
            }
            results[i] = ( double ) percolate.numberOfOpenSites() / ( n * n );
        }
    }

    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        return mean() - ( 1.96 * stddev() / Math.sqrt(numberOfTrials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return mean() + (1.96 * stddev() / Math.sqrt(numberOfTrials));
    }

    // test client (see below)
    public static void main(String[] args) {
        // 1. Get n and trials from command line arguments
        int n, trials;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter grid size (n): ");
        n = scanner.nextInt();
        System.out.print("Enter number of trials: ");
        trials = scanner.nextInt();

        PercolationStats stats = new PercolationStats(n, trials);

        System.out.println("mean                     = " + stats.mean());
        System.out.println("std-dev                  = " + stats.stddev());
        System.out.println("95% confidence interval  = ["
                + stats.confidenceLo() + ", "
                + stats.confidenceHi() + "]");
    }
}
