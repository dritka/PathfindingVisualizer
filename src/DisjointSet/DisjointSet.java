package DisjointSet;

import java.util.ArrayList;
import Maze.Node;

public class DisjointSet {
    private ArrayList<Node> nodeList;

    public DisjointSet() {
        this.nodeList = new ArrayList<>();
    }

    public static void makeSet(ArrayList<Node> nodeList) {
        for (Node node : nodeList) {
            DisjointSet set = new DisjointSet();
            set.nodeList.add(node);
            node.set = set;
        }
    }

    public static DisjointSet findSet(Node node) {
        return node.set;
    }

    public static void union(Node node1, Node node2) {
        DisjointSet set1 = node1.set;
        DisjointSet set2 = node2.set;

        if (set1.nodeList.size() > set2.nodeList.size()) {
            ArrayList<Node> nodeSet2 = set2.nodeList;

            for (Node node : nodeSet2) {
                node.set = set1;
                set1.nodeList.add(node);
            }
        } else {
            ArrayList<Node> nodeSet1 = set1.nodeList;

            for (Node node : nodeSet1) {
                node.set = set2;
                set2.nodeList.add(node);
            }
        }
    }
}
