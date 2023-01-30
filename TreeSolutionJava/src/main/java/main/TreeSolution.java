package main;

import model.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TreeSolution {

    private static List<Node> nodes;

    public static void main(String[] args) {
        // create edges as a nested list of integers
        List<List<Integer>> edgeList = Arrays.stream(args)
                // separate each edge
                .flatMap(edges -> Arrays.stream(edges.split("],?\\[")))
                // extract 2 integers from each edge
                .map(edge -> Arrays.stream(edge.replaceAll("[\\[\\]]", "").split(","))
                        .map(TreeSolution::validateAndParseValue)
                        .toList()
                ).toList();
        // run validation
        validateEdges(edgeList);
        System.out.println("Edges:\n" + edgeList);
        nodes = new ArrayList<>();
        nodes.add(new Node(1));
        Node root = createEdges(nodes.get(0), edgeList);
        System.out.println("Result:\n" + root);
    }

    /**
     * Generates all child nodes and edges from the current node.
     *
     * @param node     current node
     * @param edgeList list of edges to create
     * @return current node
     */
    private static Node createEdges(Node node, List<List<Integer>> edgeList) {
        if (edgeList != null && !edgeList.isEmpty()) {
            // group edges if node is parent
            Map<Boolean, List<List<Integer>>> edgeGrouping = edgeList.stream()
                    .collect(Collectors.groupingBy(e -> e.contains(node.getValue())));
            if (edgeGrouping.get(true) != null) {
                edgeGrouping.get(true).forEach(edge -> createChild(node, edge, edgeGrouping.get(false)));
            }
        }
        return node;
    }

    /**
     * Given a node and an edge, generates the child and all its children
     *
     * @param node     current node
     * @param edge     edge to create
     * @param edgeList list of edges to create
     */
    private static void createChild(Node node, List<Integer> edge, List<List<Integer>> edgeList) {
        int childValue = node.getValue() == edge.get(0) ? edge.get(1) : edge.get(0);
        if (nodes.stream().anyMatch(n -> n.getValue() == childValue)) {
            System.err.println("Node '" + childValue + "' has too many parents");
            System.exit(1);
        }
        Node child = node.getChild(childValue)
                .orElseGet(() -> node.addChild(new Node(childValue)));
        nodes.add(child);
        createEdges(child, edgeList);
    }

    /**
     * Validates all edges will construct a valid tree
     * Prints error and terminates if edges are not valid
     *
     * @param edgeList list of edges to validate
     */
    private static void validateEdges(List<List<Integer>> edgeList) {
        edgeList.forEach(edge -> {
            // validate 2 values
            if (edge == null || edge.size() != 2) {
                System.err.println(edge + " is not a valid edge");
                System.exit(1);
            }
        });
    }

    /**
     * Checks if a value is a valid node value
     *
     * @param value node value to validate
     * @return int value if valid
     */
    private static int validateAndParseValue(String value) {
        int intValue = 0;
        try {
            intValue = Integer.parseInt(value);
        } finally {
            if (intValue < 1) {
                System.err.println("'" + value + "' is not a valid node value");
                System.exit(1);
            }
        }
        return intValue;
    }
}
