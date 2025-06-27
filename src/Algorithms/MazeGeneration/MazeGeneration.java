package Algorithms.MazeGeneration;

import DisjointSet.DisjointSet;
import Maze.Maze;
import Maze.Node;
import Maze.NodeType;
import java.util.*;

public class MazeGeneration {
    private static final Random random = new Random();

    private static void setup(Node[][] nodes) {
        Maze.s = Maze.g = null;
        for (Node[] row : nodes) {
            for (Node node : row) {
                node.setWall();
            }
        }
    }

    private static void recursive_inner(Node[][] nodes, Node s) {
        s.setPassage();

        List<Node> neighbors = getNeighbors(nodes, s);
        Collections.shuffle(neighbors, random);


        for (Node n : neighbors) {
            int nr = n.row;
            int nc = n.col;

            if (isInBounds(nodes, nr, nc) && nodes[nr][nc].type.equals(NodeType.WALL)) {
                n.setPassage();
                connect(nodes, s, n);
                recursive_inner(nodes, n);
            }
        }
    }

    public static void recursive(Node[][] nodes) {
        setup(nodes);
        recursive_inner(nodes, getRandomNode(nodes));
    }

    private static void iterative_inner(Node[][] nodes) {
        Node s = getRandomNode(nodes);
        s.setPassage();

        Stack<Node> stack = new Stack<>();
        stack.push(s);

        while (!stack.isEmpty()) {
            Node c = stack.pop();
            List<Node> neighbors = getNeighbors(nodes, c);

            if (!neighbors.isEmpty()) {
                Node n = neighbors.get(random.nextInt(neighbors.size()));
                n.setPassage();
                connect(nodes, c, n);
                stack.push(c);
                stack.push(n);
            }
        }
    }

    public static void iterative(Node[][] nodes) {
        setup(nodes);
        iterative_inner(nodes);
    }

    private static void prims_inner(Node[][] nodes) {
        Node s = getRandomNode(nodes);
        s.setPassage();

        List<Node> frontier = new ArrayList<>();
        frontier.add(s);

        while (!frontier.isEmpty()) {
            Node c = frontier.remove(random.nextInt(frontier.size()));
            List<Node> neighbors = getNeighbors(nodes, c);

            if (!neighbors.isEmpty()) {
                Node n = neighbors.get(random.nextInt(neighbors.size()));
                n.setPassage();
                connect(nodes, c, n);
                frontier.add(c);
                frontier.add(n);
            }
        }
    }

    public static void prims(Node[][] nodes) {
        setup(nodes);
        prims_inner(nodes);
    }

    private static void kruskals_inner(Node[][] nodes) {
        ArrayList<Node> passages = new ArrayList<>();

        for (Node[] row : nodes) {
            for (Node node : row) {
                if (node.row % 2 != 0 || node.col % 2 != 0) {
                    node.setWall();
                } else {
                    node.setPassage();
                    passages.add(node);
                }
            }
        }

        DisjointSet.makeSet(passages);
        List<HashMap<Node, HashMap<Node, Node>>> walls = new ArrayList<>();

        for (Node passage : passages) {
            HashMap<Node, HashMap<Node, Node>> borderingWalls = getBorderingWalls(nodes, passage);
            walls.add(borderingWalls);
        }


        while (!walls.isEmpty()) {
            HashMap<Node, HashMap<Node, Node>> map = walls.remove(random.nextInt(walls.size()));

            for (Map.Entry<Node, HashMap<Node, Node>> entry : map.entrySet()) {
                Node w = entry.getKey();

                for (Map.Entry<Node, Node> divided : entry.getValue().entrySet()) {
                    Node f = divided.getKey();
                    Node s = divided.getValue();

                    if (!DisjointSet.findSet(f).equals(DisjointSet.findSet(s))) {
                        connect(nodes, f, s);
                        DisjointSet.union(f, s);
                    }
                }
            }
        }
    }

    public static void kruskals(Node[][] nodes) {
        kruskals_inner(nodes);
    }

    // Helper methods
    private static HashMap<Node, HashMap<Node, Node>> getBorderingWalls(Node[][] nodes, Node node) {
        HashMap<Node, HashMap<Node, Node>> bordering = new HashMap<>();

        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        int cr = node.row;
        int cc = node.col;

        for (int[] direction : directions) {
            HashMap<Node, Node> dividedCells = new HashMap<>();

            int wr = cr + direction[0];
            int wc = cc + direction[1];
            int nr = cr + direction[0] * 2;
            int nc = cc + direction[1] * 2;

            if (isInBounds(nodes, wr, wc) && nodes[wr][wc].type.equals(NodeType.WALL) &&
                    isInBounds(nodes, nr, nc) && nodes[nr][nc].type.equals(NodeType.PASSAGE)) {
                Node w = nodes[wr][wc];
                Node n = nodes[nr][nc];
                dividedCells.put(node, n);
                bordering.put(w, dividedCells);
            }
        }

        return bordering;
    }

    private static List<Node> getNeighbors(Node[][] nodes, Node node) {
        List<Node> neighbors = new ArrayList<>();

        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        int cr = node.row;
        int cc = node.col;

        for (int[] direction : directions) {
            int nr = cr + direction[0] * 2;
            int nc = cc + direction[1] * 2;

            if (isInBounds(nodes, nr, nc) && nodes[nr][nc].type.equals(NodeType.WALL)) {
                neighbors.add(nodes[nr][nc]);
            }
        }

        return neighbors;
    }

    private static void connect(Node[][] nodes, Node current, Node opposite) {
        int d_row = current.row - opposite.row;
        int d_col = current.col - opposite.col;

        if (d_row == 0) {
            switch (d_col) {
                case 2 -> nodes[current.row][current.col - 1].setPassage();
                case -2 -> nodes[current.row][current.col + 1].setPassage();
            }
        } else if (d_col == 0) {
            switch (d_row) {
                case 2 -> nodes[current.row - 1][current.col].setPassage();
                case -2 -> nodes[current.row + 1][current.col].setPassage();
            }
        }
    }

    private static Node getRandomNode(Node[][] nodes) {
        return nodes[random.nextInt(nodes.length)][random.nextInt(nodes[0].length)];
    }

    private static boolean isInBounds(Node[][] nodes, int row, int col) {
        return (row >= 0 && row <= nodes.length - 1) && (col >= 0 && col <= nodes[0].length - 1);
    }
}
