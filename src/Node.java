public class Node {
    private int row, col, f, g, h, type;
    private Node parent;

    public Node(int r, int c, int t, Object in) {
        row = r;
        col = c;
        type = t;
        parent = null;
        //type 0 is passable, 1 is not
    }

    public void setF() {
        f = g + h;
    }

    public int getF() {
        return f;
    }

    public int getG() {
        return g;
    }

    public void setG(int value) {
        g = value;
    }

    public int getH() {
        return h;
    }

    public void setH(int value) {
        h = value;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node n) {
        parent = n;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean equals(Object in) {
        Node n = (Node) in;
        return row == n.getRow() && col == n.getCol();
    }

    public String toString() {
        return "Node: " + row + "_" + col;
    }
}
