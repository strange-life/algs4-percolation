import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final byte BLOCKED = Byte.parseByte("000", 2);
    private static final byte OPENED = Byte.parseByte("001", 2);
    private static final byte CONNECTED_TO_TOP = Byte.parseByte("010", 2);
    private static final byte CONNECTED_TO_BOTTOM = Byte.parseByte("100", 2);

    private final int size;
    private final byte[] sites;
    private final WeightedQuickUnionUF uf;
    private int openSiteCount;
    private boolean percolated;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        int length = n * n;

        size = n;
        sites = new byte[length];
        for (int i = 0; i < length; i += 1) {
            sites[i] = BLOCKED;
        }
        uf = new WeightedQuickUnionUF(length);
        openSiteCount = 0;
        percolated = false;
    }

    public void open(int row, int col) {
        if (!validateIndex(row) || !validateIndex(col)) {
            throw new IllegalArgumentException();
        }
        if (isOpen(row, col)) return;

        int index = getIndex(row, col);

        sites[index] |= OPENED;
        openSiteCount += 1;

        if (row == 1) {
            sites[index] |= CONNECTED_TO_TOP;
        }

        if (row == size) {
            sites[index] |= CONNECTED_TO_BOTTOM;
        }

        unionNeighbor(row, col, row - 1, col);
        unionNeighbor(row, col, row, col + 1);
        unionNeighbor(row, col, row + 1, col);
        unionNeighbor(row, col, row, col - 1);

        int root = uf.find(index);
        if ((sites[root] & CONNECTED_TO_TOP) == CONNECTED_TO_TOP
                && (sites[root] & CONNECTED_TO_BOTTOM) == CONNECTED_TO_BOTTOM) {
            percolated = true;
        }
    }

    public boolean isOpen(int row, int col) {
        if (!validateIndex(row) || !validateIndex(col)) {
            throw new IllegalArgumentException();
        }

        int index = getIndex(row, col);

        return (sites[index] & OPENED) == OPENED;
    }

    public boolean isFull(int row, int col) {
        if (!validateIndex(row) || !validateIndex(col)) {
            throw new IllegalArgumentException();
        }

        int index = getIndex(row, col);
        int root = uf.find(index);

        return (sites[root] & CONNECTED_TO_TOP) == CONNECTED_TO_TOP;
    }

    public int numberOfOpenSites() {
        return openSiteCount;
    }

    public boolean percolates() {
        return percolated;
    }

    private void unionNeighbor(int row, int col, int neighborRow, int neighborCol) {
        if (!validateIndex(neighborRow) || !validateIndex(neighborCol) || !isOpen(neighborRow,
                                                                                  neighborCol)) {
            return;
        }

        int p = getIndex(row, col);
        int q = getIndex(neighborRow, neighborCol);
        int rootP = uf.find(p);
        int rootQ = uf.find(q);

        if (rootP == rootQ) return;
        uf.union(rootP, rootQ);

        int root = uf.find(p);
        sites[root] |= sites[rootP] | sites[rootQ];
    }

    private int getIndex(int row, int col) {
        return (row - 1) * size + (col - 1);
    }

    private boolean validateIndex(int index) {
        return index >= 1 && index <= size;
    }
}
