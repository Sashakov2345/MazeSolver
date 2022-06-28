package maze.element;

import maze.element.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node extends Element implements Comparable<Node> {
    private List<Edge> edgeList;
    private boolean checked=false;
    private int number;
    private boolean inTree=false;

    public boolean isInTree() {
        return inTree;
    }

    public void setInTree(boolean inTree) {
        this.inTree = inTree;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return number == node.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    public Node(int i, int j, int number, List <Edge> edgeList) {
        super(i, j);
        this.value=0;
        this.number=number;
        this.edgeList=edgeList;
        for (Edge edge:edgeList) {
            edge.getNodeList().add(this);
        }
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public void setEdgeList(List<Edge> edgeList) {
        this.edgeList = edgeList;
    }

    @Override
    public String toString() {
            return ("  ");
    }

    public String printToFile() {
        return (" ");
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.number, o.number);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
