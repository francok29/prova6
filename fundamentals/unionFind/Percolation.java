import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // the length of one side of the grid
    private int gridLength;

    // keep track if the cite is opened.
    private boolean[] isOpen;
    // percolation represents connectivity between sites. connected open sites, percolate to on another.

    private WeightedQuickUnionUF percolation;
    // quick union structure for tracking fullness without backwash
    // similar to percolation above, but without botttom virtual site
    private WeightedQuickUnionUF fullness;

    // when these two virtualindices are connected, the system percolates.
    // virtualTopIndex is connected to entire top   row
    private int virtualTopIndex;

    // virtualBottomIndex is connected to entire bottom row
    // initializes to (N^2)+1;
    private int virtualBottomIndex;


    private int getSiteIndex(int row ,int col)
    {
        checkBounds(row, col);
        return (row-1)*gridLength + col;
    }

    private void checkBounds(int row, int col)
    {
        if ( row > gridLength || row < 1 )
        {
            throw new IndexOutOfBoundsException("row index is out of bound");
        }
        if ( col > gridLength || col < 1 )
        {
            throw new IndexOutOfBoundsException("column index is out of bounds");
        }
    }


    public Percolation(int N)
    {
        if (N < 1) {
        
            throw new IllegalArgumentException();
        }

        gridLength = N;
        int arraySize = N*N+2;
        isOpen = new boolean[arraySize];

        virtualTopIndex = 0;
        virtualBottomIndex = N*N+1;

        isOpen[virtualTopIndex] = true;
        isOpen[virtualBottomIndex] = true;

        percolation = new WeightedQuickUnionUF(arraySize);
        fullness = new WeightedQuickUnionUF(arraySize);

        for (int j = 1; j <= N; j++) {
            // connect all top row sites to virtual top site
            int i = 1;
            int topSiteIndex = getSiteIndex(i,j);
            percolation.union(virtualTopIndex, topSiteIndex);
            fullness.union(virtualTopIndex, topSiteIndex);

            // connect all bottom row sites to virtual bottom site
            i = N;
            int bottomSiteIndex = getSiteIndex(i,j);
            percolation.union(virtualBottomIndex, bottomSiteIndex);
            fullness.union(virtualBottomIndex, bottomSiteIndex);
        } 
    }

    public void open(int row, int col)
    {
        int siteIndex = getSiteIndex(row, col);
        if (!isOpen[siteIndex]) {
        
            isOpen[siteIndex] = true;

            if (col > 1 && isOpen(row, col-1))
            {
                int indexToLeft = getSiteIndex(row, col-1);
                percolation.union(siteIndex, indexToLeft);
                fullness.union(siteIndex, indexToLeft);
            }

            if (col < gridLength && isOpen(row, col+1))
            {
                int indexToRight = getSiteIndex(row, col+1);
                percolation.union(siteIndex, indexToRight);
                fullness.union(siteIndex, indexToRight);
            }

            if (row > 1 && isOpen(row-1, col))
            {
                int indexToTop = getSiteIndex(row-1, col);
                percolation.union(siteIndex, indexToTop);
                fullness.union(siteIndex, indexToTop);
            }
            if (row < gridLength && isOpen(row+1, col))
            {
                int indexToBottom = getSiteIndex(row+1, col);
                percolation.union(siteIndex, indexToBottom);
                fullness.union(siteIndex, indexToBottom);
            }
        }
    }

    public boolean isOpen(int row, int col)
    {
        int siteIndex = getSiteIndex(row, col);
        return isOpen[siteIndex];

    }

    public boolean isFull(int row, int col)
    {
        int siteIndex = getSiteIndex(row, col);
        return(fullness.connected(virtualTopIndex, siteIndex) && isOpen[siteIndex]);
    }

    public boolean percolate()
    {
     
        if(gridLength > 1) 
        {
            return percolation.connected(virtualTopIndex, virtualBottomIndex);
        }
        else 
        {
            return isOpen[getSiteIndex(1,1)];
        }

    }

    public static void main(String[] args)
    {
        // unit test
        Percolation percolation2 = new Percolation(2);
        StdOut.println(percolation2.percolate());
        percolation2.open(1,1);
        StdOut.println(percolation2.percolate());
        percolation2.open(2,1);
        StdOut.println(percolation2.percolate());
            
    }
}
