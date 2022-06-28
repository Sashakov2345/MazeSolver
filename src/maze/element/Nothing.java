package maze.element;

public class Nothing extends Element {
    private boolean isWall;

    public Nothing(int i, int j, boolean isWall) {
        super(i, j);
        this.isWall = isWall;
    }

    @Override
    public String toString() {
        if (isWall()) {
            return ("\u2588\u2588");
        } else {
            return ("  ");
        }
    }

    public String printToFile() {
        if (isWall()) {
            return ("\u2588");
        } else {
            return (" ");
        }
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }
}
