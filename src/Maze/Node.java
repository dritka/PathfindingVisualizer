package Maze;

import DisjointSet.DisjointSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Node extends JButton implements ActionListener {
    public Node parent;
    public boolean visited;
    public int col;
    public int row;
    public int g;
    public int h;
    public NodeType type;
    public DisjointSet set;
    public static int option = Integer.MAX_VALUE;
    /*
    0 -> set node as start
    1 -> set node as goal
    2 -> set node as wall
    3 -> delete node or set node as passage (it's the same thing)
    */

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
        type = NodeType.PASSAGE;
        setBackground(Color.white);
        setForeground(Color.black);
        addActionListener(this);
    }

    public void setStart() {
        if (type.equals(NodeType.PASSAGE)) {
            setBackground(Color.blue);
            setForeground(Color.white);
            setText(null);
            type = NodeType.START;

            if (Maze.s != null) {
                Maze.s.setPassage();
            }

            Maze.s = this;
            Maze.c = this;
        }
    }

    public void setGoal() {
        if (type.equals(NodeType.PASSAGE)) {
            setBackground(Color.red);
            setForeground(Color.white);
            setText(null);
            type = NodeType.GOAL;

            if (Maze.g != null) {
                Maze.g.setPassage();
            }

            Maze.g = this;
        }
    }

    public void setWall() {
        setBackground(Color.gray);
        setForeground(Color.black);
        setText(null);
        type = NodeType.WALL;
    }

    public void setPassage() {
        setBackground(Color.white);
        setForeground(Color.black);
        setText(null);
        type = NodeType.PASSAGE;
    }

    public void setVisited() {
        if (!type.equals(NodeType.START) && !type.equals(NodeType.GOAL)) {
            setBackground(Color.cyan);
            setForeground(Color.black);
        }

        visited = true;
    }

    public void setCurrentNode() {
        if (type.equals(NodeType.PASSAGE)) {
            setBackground(Color.orange);
            setForeground(Color.black);
        }
    }

    public void setPath() {
        if (!type.equals(NodeType.START) && !type.equals(NodeType.GOAL)) {
            setBackground(Color.green);
            setForeground(Color.black);
        }
    }

    public void reset() {
        if (type.equals(NodeType.PASSAGE)) {
            setPassage();
        }

        parent = null;
        visited = false;
        g = 0;
        h = 0;
    }

    public int getFCost() {
        return g + h;
    }

    public void calculate() {
        Node start = Maze.s;
        Node goal = Maze.g;

        if (start != null && goal != null) {
            g = (Math.abs(row - start.row) + Math.abs(col - start.col));
            h = (Math.abs(row - goal.row) + Math.abs(col - goal.col));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (option) {
            case 0 -> setStart();
            case 1 -> setGoal();
            case 2 -> setWall();
            case 3 -> setPassage();
        }
    }
}
