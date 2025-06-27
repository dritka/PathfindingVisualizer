package Maze;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import javax.swing.*;
import java.util.List;

import Constants.CONSTS;

public class Maze extends JPanel {
    // NODES
    public static Node[][] nodes = new Node[CONSTS.rows][CONSTS.cols];
    public static Node s, g, c;
    private static boolean done;
    private static Stack<Node> stack;
    private static Queue<Node> queue;
    Comparator<Node> comparator = Comparator.comparingInt((Node o) -> o.g);

    private static PriorityQueue<Node> priorityQueue;
    private static List<Node> open;
    private static List<Node> closed;

    public Maze() {
        this.setPreferredSize(new Dimension(CONSTS.width, CONSTS.height));
        this.setBackground(Color.black);
        this.setLayout(new GridLayout(CONSTS.rows, CONSTS.cols));
        this.setFocusable(true);

        // Key bindings
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('b'), "b");
        this.getActionMap().put("b", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bfs();
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('f'), "f");
        this.getActionMap().put("f", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dfs();
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('d'), "d");
        this.getActionMap().put("d", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dijkstra();
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('a'), "a");
        this.getActionMap().put("a", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aStar();
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('s'), "s");
        this.getActionMap().put("s", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Node.option = 0;
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('e'), "e");
        this.getActionMap().put("e", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Node.option = 1;
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('w'), "w");
        this.getActionMap().put("w", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Node.option = 2;
            }
        });

        setup();
        s = g = c = null;
        done = false;
        stack = new Stack<>();
        queue = new LinkedList<>();
        comparator = Comparator.comparingInt(o -> o.g);
        priorityQueue = new PriorityQueue<>(comparator);
        open = new ArrayList<>();
        closed = new ArrayList<>();
    }

    private void setup() {
        int row = 0;
        int col = 0;

        while (col < CONSTS.cols && row < CONSTS.rows) {
            nodes[row][col] = new Node(row, col);
            this.add(nodes[row][col]);

            col++;
            if (col == CONSTS.cols) {
                col = 0;
                row++;
            }
        }
    }

    public static boolean check() {
        return (s != null && g != null);
    }

    private static void dfsInner() {
        if (check()) {
            if (!stack.isEmpty()) {
                c = stack.pop();
                c.setVisited();

                if (c == g) {
                    done = true;
                    showPath();
                    return;
                }

                List<Node> neighbors = getNeighbors();

                for (Node neighbor : neighbors) {
                    if (!neighbor.visited) {
                        neighbor.parent = c;
                        neighbor.setVisited();
                        stack.push(neighbor);
                    }
                }
            } else {
                c.setVisited();
                stack.push(c);
            }
        }
    }

    public static void dfs() {
        new Thread(() -> {
            while (!done) {
                try {
                    dfsInner();
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void bfsInner() {
        if (check()) {
            if (!queue.isEmpty()) {
                c = queue.remove();
                c.setVisited();

                if (c == g) {
                    done = true;
                    showPath();
                    return;
                }

                java.util.List<Node> neighbors = getNeighbors();

                for (Node neighbor : neighbors) {
                    if (!neighbor.visited) {
                        neighbor.parent = c;
                        neighbor.setVisited();
                        queue.add(neighbor);
                    }
                }
            } else {
                c.setVisited();
                queue.add(c);
            }
        }
    }

    public static void bfs() {
        new Thread(() -> {
            while (!done) {
                try {
                    bfsInner();
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void dijkstraInner() {
        if (check()) {
            if (!priorityQueue.isEmpty()) {
                c = priorityQueue.remove();
                c.setVisited();

                if (c == g) {
                    done = true;
                    showPath();
                    return;
                }

                List<Node> neighbors = getNeighbors();
                HashMap<Node, Integer> weightMap = getWeightMapForEachNeighbor(neighbors);

                for (Node neighbor : neighbors) {
                    if (priorityQueue.contains(neighbor)) {
                        if (neighbor.g > c.g + weightMap.get(neighbor)) {
                            neighbor.g = c.g + weightMap.get(neighbor);
                            neighbor.parent = c;
                            neighbor.setVisited();
                            priorityQueue.remove(neighbor);
                            priorityQueue.add(neighbor);
                        }
                    }
                }
            } else {
                List<Node> nodeList = new ArrayList<>();

                for (Node[] row : nodes) {
                    for (Node node : row) {
                        if (!node.type.equals(NodeType.WALL)) {
                            node.g = Integer.MAX_VALUE;
                            nodeList.add(node);
                        }
                    }
                }

                c.g = 0;
                priorityQueue.addAll(nodeList);
            }
        }
    }

    public static void dijkstra() {
        new Thread(() -> {
            while (!done) {
                try {
                    dijkstraInner();
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static HashMap<Node, Integer> getWeightMapForEachNeighbor(java.util.List<Node> neighbors) {
        HashMap<Node, Integer> weightMap = new HashMap<>();

        for (Node neighbor : neighbors) {
            weightMap.put(neighbor, 1);
        }

        return weightMap;
    }

    private static void aStarInner() {
        if (check()) {
            if (!open.isEmpty()) {
                c = getBestNode(open);
                c.setVisited();
                open.remove(c);
                closed.add(c);

                if (c == g) {
                    done = true;
                    showPath();
                    return;
                }

                List<Node> neighbors = getNeighbors();

                for (Node n : neighbors) {
                    int ni = n.row;
                    int nj = n.col;

                    if (!isInBounds(ni, nj) || closed.contains(n)) continue;

                    if (n.getFCost() < c.getFCost() || !open.contains(n)) {
                        n.parent = c;

                        if (!open.contains(n)) {
                            open.add(n);
                        }
                    }
                }
            } else {
                for (Node[] row : nodes) {
                    for (Node node : row) {
                        node.calculate();
                    }
                }

                open.add(c);
            }
        }
    }

    public static void aStar() {
        new Thread(() -> {
            while (!done) {
                try {
                   aStarInner();
                   Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static Node getBestNode(java.util.List<Node> open) {
        int bni = 0;
        int bnfc = Integer.MAX_VALUE;

        for (int i = 0; i < open.size(); i++) {
            if (open.get(i).getFCost() < bnfc) {
                bni = i;
                bnfc = open.get(i).getFCost();
            } else if (open.get(i).getFCost() == bnfc) {
                if (open.get(i).h < open.get(bni).h) {
                    bni = i;
                }
            }
        }

        return open.get(bni);
    }

    private static List<Node> getNeighbors() {
        List<Node> n = new ArrayList<>();

        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        int cr = c.row;
        int cc = c.col;


        for (int[] direction : directions) {
            int nr = cr + direction[0];
            int nc = cc + direction[1];

            if (isInBounds(nr, nc) && !nodes[nr][nc].type.equals(NodeType.WALL)) n.add(nodes[nr][nc]);
        }

        return n;
    }

    public static boolean isInBounds(int row, int col) {
        return (row >= 0 && row <= nodes.length - 1) && (col >= 0 && col <= nodes[0].length - 1);
    }

    public static void showPath() {
        if (c == null) return;

        c.setPath();
        c = c.parent;
        showPath();
    }

    public static void reset() {
        c = s;
        done = false;

        for (Node[] row : nodes) {
            for (Node node : row) {
                node.reset();
            }
        }

        stack.clear();
        queue.clear();
        priorityQueue.clear();
        open.clear();
        closed.clear();
    }

    public static void delete() {
        s = g = null;
        reset();

        for (Node[] row : nodes) {
            for (Node node : row) {
                node.setPassage();
            }
        }
    }
}
