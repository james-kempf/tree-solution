package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Node {

    final int value;
    List<Node> children;

    public Node(int value) {
        this.value = value;
        children = new ArrayList<>();
    }

    public int getValue() {
        return value;
    }

    public Optional<Node> getChild(int childValue) {
        return children.stream()
                .filter(child -> child.getValue() == childValue)
                .findFirst();
    }

    public Node addChild(Node node) {
        children.add(node);
        return node;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(value);
        children.forEach(child -> {
            if (children.indexOf(child) == children.size() - 1) {
                stringBuilder.append("\n└──╴");
                stringBuilder.append(child.toString().replace("\n", "\n    "));
            } else {
                stringBuilder.append("\n├──╴");
                stringBuilder.append(child.toString().replace("\n", "\n│   "));
            }
        });
        return stringBuilder.toString();
    }
}
