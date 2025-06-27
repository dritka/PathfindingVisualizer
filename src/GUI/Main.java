package GUI;

import javax.swing.*;
import Maze.Maze;
import Maze.Node;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Algorithms.MazeGeneration.MazeGeneration;

public class Main extends JFrame implements ActionListener {
    public JMenuBar menu;
    public JMenu options, start, generate, add, delete;
    public JMenuItem BFS, DFS, dijkstra, aStar, recursiveDFS, iterativeDFS, kruskals, prims, add_start, add_goal, add_wall, delete_node, delete_map, reset;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        setTitle("Pathfinding Pro Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // ADD COMPONENTS TO THE WINDOW
        add(new Maze());
        menu = new JMenuBar();

        options = new JMenu("Options");
        start = new JMenu("Start simulation");
        generate = new JMenu("Generate maze");
        add = new JMenu("Add");
        delete = new JMenu("Delete");

        delete_node = new JMenuItem("Delete a node");
        delete_map = new JMenuItem("Delete map");
        reset = new JMenuItem("Reset map");
        BFS = new JMenuItem("BFS algorithm");
        DFS = new JMenuItem("DFS algorithm");
        dijkstra = new JMenuItem("Dijkstra's algorithm");
        aStar = new JMenuItem("A* search algorithm");
        recursiveDFS = new JMenuItem("Recursive DFS algorithm");
        iterativeDFS = new JMenuItem("Iterative DFS algorithm");
        kruskals = new JMenuItem("Kruskal's algorithm");
        prims = new JMenuItem("Prim's algorithm");
        add_start = new JMenuItem("Add start node");
        add_goal = new JMenuItem("Add goal node");
        add_wall = new JMenuItem("Add wall node");

        start.add(BFS);
        start.add(DFS);
        start.add(dijkstra);
        start.add(aStar);
        generate.add(recursiveDFS);
        generate.add(iterativeDFS);
        generate.add(kruskals);
        generate.add(prims);
        add.add(add_start);
        add.add(add_goal);
        add.add(add_wall);
        delete.add(delete_node);
        delete.add(delete_map);

        options.add(start);
        options.add(generate);
        options.add(add);
        options.add(delete);
        options.add(reset);

        BFS.addActionListener(this);
        DFS.addActionListener(this);
        dijkstra.addActionListener(this);
        aStar.addActionListener(this);
        recursiveDFS.addActionListener(this);
        iterativeDFS.addActionListener(this);
        kruskals.addActionListener(this);
        prims.addActionListener(this);
        add_start.addActionListener(this);
        add_goal.addActionListener(this);
        add_wall.addActionListener(this);
        delete_node.addActionListener(this);
        delete_map.addActionListener(this);
        reset.addActionListener(this);

        menu.add(options);
        setJMenuBar(menu);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source.equals(BFS)) Maze.bfs();
        if (source.equals(DFS)) Maze.dfs();
        if (source.equals(dijkstra)) Maze.dijkstra();
        if (source.equals(aStar)) Maze.aStar();
        if (source.equals(recursiveDFS)) MazeGeneration.recursive(Maze.nodes);
        if (source.equals(iterativeDFS)) MazeGeneration.iterative(Maze.nodes);
        if (source.equals(kruskals)) MazeGeneration.kruskals(Maze.nodes);
        if (source.equals(prims)) MazeGeneration.prims(Maze.nodes);
        if (source.equals(add_start)) Node.option = 0;
        if (source.equals(add_goal)) Node.option = 1;
        if (source.equals(add_wall)) Node.option = 2;
        if (source.equals(delete_node)) Node.option = 3;
        if (source.equals(delete_map)) Maze.delete();
        if (source.equals(reset)) Maze.reset();
    }
}