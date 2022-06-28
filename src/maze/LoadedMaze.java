package maze;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class LoadedMaze {
    private final int HEIGHT;
    private final int WIDTH;
    private static final char SPACE = ' ';
    private static final char ESCAPE_SIGN = '/';
    private Deque<int[]> stack = new ArrayDeque<>();
    private List<String> loadedMaze;
    private Character[][] charArray;
    private Character[][] charArrayWithEscape;


    public LoadedMaze(List<String> loadedMaze) {
        HEIGHT = loadedMaze.size();
        WIDTH = loadedMaze.get(0).length();
        this.loadedMaze = loadedMaze;
        charArray = new Character[HEIGHT][WIDTH];
        charArrayWithEscape = new Character[HEIGHT][WIDTH];
    }

    public void printMazeToFile(File file) {
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (String s : loadedMaze) {
                printWriter.println(s);
            }
        } catch (IOException e) {
            System.out.println("File can't be created " + e.getMessage());
        }
    }

    public void printMaze() {
        for (String s : loadedMaze) {
            char[] chars = s.toCharArray();
            for (char c : chars) {
                System.out.print(c);
                System.out.print(c);
            }
            System.out.println();
        }
    }


    public List<String> getLoadedMaze() {
        return loadedMaze;
    }

    public void setLoadedMaze(List<String> loadedMaze) {
        this.loadedMaze = loadedMaze;
    }

    private boolean nodeExist(int[] node) {
        int iN = node[0];
        int jN = node[1];
        if (iN >= 0 && iN < HEIGHT && jN >= 0 && jN < WIDTH) {
            return true;
        } else return false;
    }

    public void printEscape() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                charArrayWithEscape[i][j] = charArray[i][j];
            }
        }
        for (int[] node : stack) {
            int i = node[0];
            int j = node[1];
            charArrayWithEscape[i][j] = ESCAPE_SIGN;

        }
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                char c = charArrayWithEscape[i][j].charValue();
                System.out.print(c);
                System.out.print(c);
            }
            System.out.println();
        }

    }

    public void findTheEscape() {
        int[] enter = new int[2];
        int[] exit = new int[2];
        boolean isFirst = true;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                charArray[i][j] = loadedMaze.get(i).charAt(j);
                if ((i == 0 || i == HEIGHT - 1 || j == 0 || j == WIDTH - 1) && charArray[i][j] == SPACE) {
                    if (isFirst) {
                        enter[0] = i;
                        enter[1] = j;
                        stack.addFirst(enter);
                        isFirst = false;
                    } else {
                        exit[0] = i;
                        exit[1] = j;
                    }

                }
            }
        }
        DeepSearch deepSearch = new DeepSearch(exit);
        deepSearch.deepSearch(enter);
        printEscape();
    }

    private class DeepSearch {
        private boolean exitFounded;
        private final int[] EXIT;
        private boolean[][] isVisited = new boolean[HEIGHT][WIDTH];

        public DeepSearch(int[] EXIT) {
            this.EXIT = EXIT;
            exitFounded = false;
        }

        private void printStack() {
            int i = 0;
            for (int[] node : stack) {
                System.out.print("Node " + i + ": " + node[0] + "  " + node[1] + "; ");
                i++;
            }
            System.out.println();
        }

        private void deepSearch(int[] node) {
            int iN = node[0];
            int jN = node[1];
            isVisited[iN][jN] = true;
            if (iN == EXIT[0] && jN == EXIT[1]) {
                exitFounded = true;
                stack.addFirst(node);
                return;
            }
            for (int i = iN - 1; i <= iN + 1; i++) {
                for (int j = jN - 1; j <= jN + 1; j++) {
                    if (Math.abs(i - iN) != Math.abs(j - jN) && !exitFounded) {
                        if (nodeExist(new int[]{i, j})) {
                            if (charArray[i][j] == SPACE && !isVisited[i][j]) {
                                int[] nextNode = new int[]{i, j};
                                stack.addFirst(nextNode);
                                deepSearch(nextNode);
                            }
                        }
                    }
                }
            }

            if (!exitFounded) {
                stack.removeFirst();
            }
        }
    }
}
