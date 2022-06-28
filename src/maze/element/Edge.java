package maze.element;

import maze.element.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Edge extends Element {
    private int weight;
    private boolean chosen;
    private List<Node> nodeList = new ArrayList<>(2);


    public Edge(int i, int j, int weight) {
        super(i, j);
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public String toString() {
        if (isChosen()) {
            return ("  ");
        } else {
            return ("\u2588\u2588");
        }
    }

    public String printToFile() {
        if (isChosen()) {
            return (" ");
        } else {
            return ("\u2588");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge edge = (Edge) o;
        Collections.sort(this.getNodeList());
        Collections.sort(edge.getNodeList());
        for (int i = 0; i < this.getNodeList().size(); i++) {
            if (!Objects.equals(this.nodeList.get(i), edge.nodeList.get(i))) {
                return false;
            }
        }
        return true;
    }


    @Override
    public int hashCode() {
        return (Objects.hash(this.nodeList.get(0))+Objects.hash(this.nodeList.get(1)))/2;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }
}
