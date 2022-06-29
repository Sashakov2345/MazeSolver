package maze.element;

public abstract class Element {
    protected int y;
    protected int x;
    protected int value;


    public Element(int i, int j) {
        y = i;
        x = j;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public abstract String printToFile();

    @Override
    public abstract String toString();
}
