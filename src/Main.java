import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.lang.Math;

public class Main extends JFrame {
    private final static int ARRAY_ROW = 15;
    private final static int ARRAY_COL = 15;
    private final static int WINDOW_WIDTH = 1152;
    private final static int WINDOW_HEIGHT = 648;
    private static JButton[][] grid = new JButton[ARRAY_COL][ARRAY_ROW];

    //Make visualization using Java (TODO: See if it's easier to do in C#)
    private Main() {
        setLayout(new GridLayout(ARRAY_ROW, ARRAY_COL));
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //When exiting out, it wont hide it. It actually closes the app.

        var col = 0;
        //while loops slight edge in performance
        while (col < ARRAY_COL) {
            var row = 0;
            while (row < ARRAY_ROW) {
                grid[col][row] = new JButton(col + "_" + row); //make grid
                if (Math.random() < 0.1) { // 10%
                    grid[col][row].setBackground(Color.black);
                    add(grid[col][row]);
                } else {
                    add(grid[col][row]);
                }
                row++;
            }
            col++;
        }
        setVisible(true); //need to show grid
    }
    
    private static int Manhattan(Node start, Node goal) {
        // |x1 - x2| + |y1 - y2|
        return (Math.abs(start.getRow() - goal.getRow())) + (Math.abs(start.getCol() - goal.getCol()));
    }

    //Check if it is in the open list
    private static Node checkArray(Node in, ArrayList<Node> list) {
        return list.stream().filter(aList -> aList.equals(in)).findFirst().orElse(null);
    }

    private static void sortArray(ArrayList<Node> list) {
        int count;
        Node newList;

        var a = 0;
        while (a < list.size()) {
            count = a;
            var b = a;
            while (b < list.size() - 1) {
                if (list.get(b + 1).getF() < list.get(count).getF()) count = b + 1;
                b++;
            }
            Node value = list.get(a);
            newList = list.get(count);
            list.set(a, newList);
            list.set(count, value);
            a++;
        }
    }

    private static void aStar(Node start, Node end, JButton[][] grid) {
        var search = true;
        ArrayList<Node> visitedNodes = new ArrayList<>();
        ArrayList<Node> unvisitedNodes = new ArrayList<>();
        visitedNodes.add(start);

        while (search) {
            Node visited = visitedNodes.remove(0);
            //goal state
            if (visited.equals(end)) {
                search = false;
                while (!visited.equals(start)) {
                    visited = visited.getParent();
                    grid[visited.getRow()][visited.getCol()].setBackground(Color.pink); //path
                }
            } else {

                var r = visited.getRow();
                var c = visited.getCol();
                //check surrounding nodes
                var i = r - 1;
                while (i <= r + 1) {
                    var j = c - 1;
                    while (j <= c + 1) {
                        if (i >= 0 && i < 15 && j >= 0 && j < 15 && (i != r || j != c) && (grid[i][j].getBackground() != Color.black)) {
                            Node m = new Node(i, j, 1, grid[i][j]);
                            m.setParent(visited);
                            int newG = 10;

                            if (Math.abs(i - r) + Math.abs(j - c) == 2) newG = 14;
                            m.setG(visited.getG() + newG);
                            m.setH(Manhattan(m, end));
                            m.setF();

                            if (checkArray(m, unvisitedNodes) == null) {
                                Node q = checkArray(m, visitedNodes);
                                if (q == null) {
                                    visitedNodes.add(m);
                                } else {
                                    if (m.getG() < q.getG()) {
                                        q.setG(m.getG());
                                        q.setParent(visited);
                                    }
                                }
                            }
                        }
                        j++;
                    }
                    i++;
                }
                sortArray(visitedNodes);
                unvisitedNodes.add(visited);
            }
        }
    }

    public static void main(String args[]) {
        new Main(); //Show Grid behind input boxes
        var restart = true;
        try {
            while (restart) {
                String input1 = JOptionPane.showInputDialog("Starting Row(one number): ");
                String input2 = JOptionPane.showInputDialog("Starting Column(one number): ");
                String input3 = JOptionPane.showInputDialog("Ending Row(one number): ");
                String input4 = JOptionPane.showInputDialog("Ending Column(one number): ");

                int startRow = Integer.parseInt(input1);
                int startCol = Integer.parseInt(input2);
                int endRow = Integer.parseInt(input3);
                int endCol = Integer.parseInt(input4);

                grid[endRow][endCol].setBackground(Color.red);
                Node start = new Node(startRow, startCol, 0, grid[startRow][startCol]);
                Node end = new Node(endRow, endCol, 0, grid[endRow][endCol]);
                start.setG(0);
                start.setH(Manhattan(start, end));
                System.out.println("Manhattan Distance = " + start.getH());
                start.setF();
                System.out.println("Start " + start.toString());
                System.out.println("End " + end.toString());
                aStar(start, end, grid);
                grid[startRow][startCol].setBackground(Color.green);

                String input5 = JOptionPane.showInputDialog(null, "Would you like to restart? Y or N");
                if (input5.equals("N") || input5.equals("n")) restart = false;
            }
        }
        catch (ArithmeticException e) {
            System.out.println("Could not find proper path");
        }
    }
}