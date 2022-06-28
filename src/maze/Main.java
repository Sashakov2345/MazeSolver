package maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    private static final char SPACE = ' ';
    private List<String> loadedMaze = new ArrayList<>();

    public boolean isMazeGeneratedOrLoaded() {
        return isMazeGeneratedOrLoaded;
    }

    public void setMazeGeneratedOrLoaded(boolean mazeGeneratedOrLoaded) {
        isMazeGeneratedOrLoaded = mazeGeneratedOrLoaded;
    }

    private boolean isMazeGeneratedOrLoaded = false;

    private int receiveIntInRange(int range) {
        Scanner scanner = new Scanner(System.in);
        try {
            int result = scanner.nextInt();
            if (result >= 0 && result <= range) {
                return result;
            } else {
                System.out.println("Incorrect option. Please try again(range)");
                return receiveIntInRange(range);
            }
        } catch (Exception e) {
            System.out.println("Incorrect option. Please try again(exception)");
            return receiveIntInRange(range);
        }
    }

    private int printMenu() {
        if (!isMazeGeneratedOrLoaded()) {
            System.out.println("=== Menu ===\n" +
                    "1. Generate a new maze\n" +
                    "2. Load a maze\n" +
                    "0. Exit");
            return 2;
        } else {
            System.out.println("=== Menu ===\n" +
                    "1. Generate a new maze\n" +
                    "2. Load a maze\n" +
                    "3. Save the maze\n" +
                    "4. Display the maze\n" +
                    "5. Find the escape\n" +
                    "0. Exit");
            return 5;
        }
    }

    private boolean checkForAccessibility(List<String> loadedMaze, int k, int index) {
        int width = loadedMaze.get(k).length();
        int height = loadedMaze.size();
        if (k > 0 && k < height - 1 && index > 0 && index < width - 1) {
            for (int i = k - 1; i <= k + 1; i++) {
                for (int j = index - 1; j <= index + 1; j++) {
                        String s = loadedMaze.get(i);
                        char c = s.charAt(j);
                        if (c == SPACE) {
                            return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    private boolean validLoadedMaze(List<String> loadedMaze) {
        if (Objects.equals(loadedMaze, null)) {
            System.out.println("Objects.equals(loadedMaze, null)");
            return false;
        }
        int width = loadedMaze.get(1).length();
        int height = loadedMaze.size();
        int numberOfEntries = 0;
        for (int k = 0; k < loadedMaze.size(); k++) {
            String s = loadedMaze.get(k);
            if (s.length() != width) {
                return false;
            }
            char[] chars = s.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                if (c == SPACE && (k == 0 || k == loadedMaze.size() - 1 || i == 0 || i == height - 1)) {
                    numberOfEntries++;
                }
                if (!checkForAccessibility(loadedMaze, k, i)) {
                    System.out.println("!checkForAccessibility(loadedMaze,k,i)");
                    return false;
                }
            }
        }
        if (numberOfEntries != 2) {
            System.out.println("numberOfEntries != 2");
            return false;
        }
        return true;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Main main = new Main();
        int height;
        int width;
        int choice = 0;
        boolean resume = true;
        boolean isFirst = true;
        int range = 2;
        Maze maze = new Maze(3, 3);
        while (resume) {
            if (choice == 1 || choice == 5) {
                System.out.println("\n");
            } else if (!isFirst) {
                System.out.println();
            }
            isFirst = false;
            range = main.printMenu();
            choice = main.receiveIntInRange(range);
            switch (choice) {
                case 1: {
                    System.out.println("Enter the size of a new maze");
                    height = main.receiveIntInRange(100);
                    width = height;
                    maze = new Maze(height, width);
                    maze.printMaze();
                    main.setMazeGeneratedOrLoaded(true);
                }
                break;
                case 2: {
                    String pathToFile = scanner.nextLine();
                    File file = new File(pathToFile);
                    try (Scanner fileScanner = new Scanner(file)) {
                        while (fileScanner.hasNextLine()) {
                            String s = fileScanner.nextLine();
                            main.loadedMaze.add(s);
                        }
                        if (!main.validLoadedMaze(main.loadedMaze)) {
                            System.out.println("Cannot load the maze. It has an invalid format");
                            break;
                        }
                        main.setMazeGeneratedOrLoaded(true);
                        maze.setReplaceMazeToLoaded(true,new LoadedMaze(main.loadedMaze));

                    } catch (FileNotFoundException e) {
                        System.out.println("The file ... does not exist");
                    }
                }
                break;
                case 3: {
                    File file = new File(scanner.nextLine());
                    maze.printMazeToFile(file);
                }
                break;
                case 4: {
                    maze.printMaze();
                }
                break;
                case 5:{
                    maze.findTheEscape();
                }
                break;
                case 0: {
                    resume = false;
                    System.out.println("Bye!");
                }
                break;
            }
        }
        scanner.close();

    }
}
