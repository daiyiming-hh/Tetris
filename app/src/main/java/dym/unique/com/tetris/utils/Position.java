package dym.unique.com.tetris.utils;

public class Position {

    private int x = 0, y = 0;

    public Position(){}

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(int x, int y) {
        setX(x);
        setY(y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}