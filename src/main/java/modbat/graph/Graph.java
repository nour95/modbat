package modbat.graph;

import java.util.*;
import java.util.stream.Collectors;

//adapted from https://algorithms.tutorialhorizon.com/weighted-graph-implementation-java/
public class Graph<NT, ET> {
    private Node<NT> root;
    private final Map<Node<NT>, List<Edge<NT, ET>>> adjacencyMap;

    public Graph() {
        this(null);
    }

    public Graph(Node<NT> root) {
        this.root = root;
        adjacencyMap = new HashMap<>();
    }

    public void setRoot(Node<NT> root) {
        this.root = root;
    }

    //for directed graph
    public void addEdge(Node<NT> source, Node<NT> destination) {
        addEdge(source, destination, null);
    }

    public void addEdge(Node<NT> source, Node<NT> destination, ET data) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("either source or destination is null");
        }

        Edge<NT, ET> edge = new Edge<>(source, destination, data);

        if (adjacencyMap.containsKey(source)) {
            adjacencyMap.get(source).add(edge);
        } else {
            LinkedList<Edge<NT, ET>> newList = new LinkedList<>();
            newList.add(edge);
            adjacencyMap.put(source, newList);
        }

        // create a new empty list for destination node
        if (!adjacencyMap.containsKey(destination)) {
            adjacencyMap.put(destination, new LinkedList<>());
        }
    }

    public Node<NT> getRoot() {
        return root;
    }

    public boolean isLeaf(Node<NT> node) {
        if (adjacencyMap.get(node) == null) {
            throw new IllegalArgumentException("node does not exist");
        }

        return adjacencyMap.get(node).isEmpty();
    }

    public Set<Node<NT>> getAllNodes() {
        return adjacencyMap.keySet();
    }

    public List<Edge<NT, ET>> getAllEdges() {
        return adjacencyMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public List<Edge<NT, ET>> outgoingEdges(Node<NT> from) {
        return adjacencyMap.get(from);
    }

    public List<Edge<NT, ET>> incomingEdges(Node<NT> to) {
        return getAllEdges().stream().filter(edge -> edge.getDestination().equals(to)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<Node<NT>> keys = adjacencyMap.keySet();
        List<Edge<NT, ET>> list;

        for (Node<NT> node : keys) {
            list = adjacencyMap.get(node);

            for (Edge<NT, ET> data : list) {
                sb.append(data).append("\n");
            }
        }

        return sb.toString();
    }


}
