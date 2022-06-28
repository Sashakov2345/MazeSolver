package maze;

import maze.element.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Maze {
    final int HEIGHT;
    final int WIDTH;
    final int MAHEIGHT;
    final int MAWIDTH;
    final int INH;
    final int INW;
    private static final char SPACE = ' ';
    private Element[][] mazeArray;
    private Node[][] nodeArray;
    private List<Edge> edgesOfSpanningTree;
    private boolean replaceMazeToLoaded = false;
    private LoadedMaze loadedMazeObj;
    private Element[][] mazeForPrinting;

    public boolean isReplaceMazeToLoaded() {
        return replaceMazeToLoaded;
    }

    public LoadedMaze getLoadedMazeObj() {
        return loadedMazeObj;
    }

    public void setReplaceMazeToLoaded(boolean replaceMazeToLoaded, LoadedMaze loadedMazeObj) {
        this.loadedMazeObj = loadedMazeObj;
        this.replaceMazeToLoaded = replaceMazeToLoaded;
    }

    public Maze(int HEIGHT, int WIDTH) {
        INH = HEIGHT;
        INW = WIDTH;
        this.HEIGHT = (HEIGHT - 1) / 2;
        this.WIDTH = (WIDTH - 1) / 2;
        MAHEIGHT = 2 * this.HEIGHT - 1;
        MAWIDTH = 2 * this.WIDTH - 1;
        nodeArray = new Node[this.HEIGHT][this.WIDTH];
        mazeArray = new Element[MAHEIGHT][MAWIDTH];
        fillTheNodeArray();
        fillNodeListOfEdges();
        edgesOfSpanningTree = buildSpanningTree();
        createMaze(edgesOfSpanningTree);
        mazeForPrinting = new Element[INH][INW];
        createMazeForPrinting();
    }

    private void fillTheNodeArray() {
        int number = 0;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                nodeArray[i][j] = new Node(i, j, number, fillEdgeList(i, j));
                number++;
            }
        }
    }

    private void fillNodeListOfEdges() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                List<Edge> edgeList = nodeArray[i][j].getEdgeList();
                for (Edge edge : edgeList) {
                    List<Node> nodeList = edge.getNodeList();
                    int y = edge.getY();
                    int x = edge.getX();
                    if (2 * i == y) {
                        if (j * 2 < x) {
                            nodeList.add(nodeArray[i][j + 1]);
                        } else {
                            nodeList.add(nodeArray[i][j - 1]);
                        }
                    } else {
                        if (i * 2 < y) {
                            nodeList.add(nodeArray[i + 1][j]);
                        } else {
                            nodeList.add(nodeArray[i - 1][j]);
                        }
                    }
                    Collections.sort(nodeList);
                }
            }
        }
    }

    private List<Edge> fillEdgeList(int y, int x) {
        Random random1 = new Random();
        Random random2 = new Random(random1.nextInt());
        List<Edge> edgeList = new ArrayList<>(4);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (Math.abs(i) != Math.abs(j)) {
                    if (2 * y + i >= 0 && 2 * y + i < MAHEIGHT && 2 * x + j >= 0 && 2 * x + j < MAWIDTH) {
                        Random random = new Random((Objects.hash(y) + Objects.hash(x)) + random2.nextInt());
                        Edge edge = new Edge(2 * y + i, 2 * x + j, random.nextInt(10) + 1);
                        edgeList.add(edge);
                    }
                }
            }
        }
        return edgeList;
    }

    private boolean isEdgeAddable(List<Node> nodesIn, Edge edge) {
        boolean result = false;
        for (Node node : edge.getNodeList()) {
            if (!node.isInTree()) {
                result = true;
                break;
            }
        }
        return result;
    }

    private Node addNewNode(List<Node> nodesIn, Edge edge) {
        Node result = null;
        for (Node node : edge.getNodeList()) {
            if (!nodesIn.contains(node)) {
                result = node;
                node.setInTree(true);
//                System.out.println("done");
                break;
            }
        }
        return result;
    }

    private List<Edge> buildSpanningTree() {
        int numberOfNodes = WIDTH * HEIGHT;
        List<Node> nodesIn = new ArrayList<>(numberOfNodes);
        List<Edge> listOfEdgesForComparison = new ArrayList<>();
        List<Edge> listOfChosenEdges = new ArrayList<>();
        Comparator<Edge> edgeComparator = Comparator.comparing(o -> o.getWeight());
        nodesIn.add(nodeArray[0][0]);
        nodeArray[0][0].setInTree(true);
        while (nodesIn.size() < numberOfNodes) {
            for (int i = nodesIn.size() - 1; i >= 0; i--) {
                Node node = nodesIn.get(i);
                if (!node.isChecked()) {
                    for (Edge edge : node.getEdgeList()) {
                        if (!listOfEdgesForComparison.contains(edge) && isEdgeAddable(nodesIn, edge) && !edge.isChosen()) {
                            listOfEdgesForComparison.add(edge);
                        }
                    }
                    node.setChecked(true);
                }
            }
            listOfEdgesForComparison.removeIf(edge -> !isEdgeAddable(nodesIn, edge));
            Collections.sort(listOfEdgesForComparison, edgeComparator);
            Edge chosenEdge = listOfEdgesForComparison.get(0);
            chosenEdge.setChosen(true);
            nodesIn.add(addNewNode(nodesIn, chosenEdge));
            listOfChosenEdges.add(chosenEdge);
            listOfEdgesForComparison.remove(chosenEdge);

        }
        return listOfChosenEdges;
    }

    private void printTree(List<Edge> edgeList) {
        for (Edge edge : edgeList) {
            System.out.println("y=" + edge.getY() + " x=" + edge.getX() + " weight=" + edge.getWeight());
        }
    }

    private void createMaze(List<Edge> edgeList) {
        for (int i = 0; i < MAHEIGHT; i++) {
            for (int j = 0; j < MAWIDTH; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    Node node = nodeArray[i / 2][j / 2];
                    mazeArray[i][j] = node;
                    continue;
                } else if (i % 2 != 0 && j % 2 != 0) {
                    mazeArray[i][j] = new Nothing(i, j, true);
                    continue;
                } else {
                    mazeArray[i][j] = new Nothing(i, j, true);
                }
            }
        }
        for (Edge edge : edgeList) {
            int y = edge.getY();
            int x = edge.getX();
            mazeArray[y][x] = edge;
        }
    }

    public void printMazeToFile(File file) {
        if (isReplaceMazeToLoaded()) {
            loadedMazeObj.printMazeToFile(file);
            return;
        }
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (int i = 0; i < INH; i++) {
                for (int j = 0; j < INW; j++) {
                    printWriter.print(mazeForPrinting[i][j].printToFile());
                }
                printWriter.println();
            }

        } catch (IOException e) {
            System.out.println("File can't be created " + e.getMessage());
        }
    }

    private int getEvenNumber(int bound) {
        Random random = new Random();
        while (true) {
            int result = random.nextInt(bound);
            if (result % 2 == 0) {
                return result;
            }
        }
    }

    private void createMazeForPrinting() {
        int incI = 1;
        int incJ = 1;
        if (MAHEIGHT + 2 < INH) {
            incI++;
        }
        if (MAWIDTH + 2 < INW) {
            incJ++;
        }
        int exitJ = getEvenNumber(MAWIDTH + 1) + incJ;
        int enterI = getEvenNumber(MAHEIGHT + 1) + incI;
        for (int i = 0; i < incI; i++) {
            for (int j = 0; j < INW; j++) {
                if (j != exitJ) {
                    mazeForPrinting[INH - 1][j] = new Nothing(INH - 1, j, true);
                } else {
                    mazeForPrinting[INH - 1][j] = new Nothing(INH - 1, j, false);
                }
                mazeForPrinting[i][j] = new Nothing(i, j, true);
            }
        }
        for (int j = 0; j < incJ; j++) {
            for (int i = incI; i < INH; i++) {
                if (i != enterI) {
                    mazeForPrinting[i][j] = new Nothing(i, j, true);
                } else {
                    mazeForPrinting[i][j] = new Nothing(i, j, false);
                }
                mazeForPrinting[i][INW - 1] = new Nothing(i, INW - 1, true);
            }
        }
        for (int i = incI; i < INH - 1; i++) {
            for (int j = incJ; j < INW - 1; j++) {
                mazeForPrinting[i][j] = mazeArray[i - incI][j - incJ];
            }
        }
    }


    public void printMaze() {
        if (isReplaceMazeToLoaded()) {
            loadedMazeObj.printMaze();
        } else {
            for (int i = 0; i < INH; i++) {
                for (int j = 0; j < INW; j++) {
                    System.out.print(mazeForPrinting[i][j].toString());
                }
                System.out.println();
            }
        }
    }

    public void findTheEscape() {
        if (isReplaceMazeToLoaded()) {
            loadedMazeObj.findTheEscape();
        } else {
            List<String> maze = new ArrayList<>();
            StringBuilder line;
            for (int i = 0; i < INH; i++) {
                line = new StringBuilder("");
                for (int j = 0; j < INW; j++) {
                    line.append(mazeForPrinting[i][j].printToFile());
                }
                maze.add(line.toString());
            }
            LoadedMaze loadedMaze = new LoadedMaze(maze);
            loadedMaze.findTheEscape();
        }
    }
}

