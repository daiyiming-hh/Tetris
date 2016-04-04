package dym.unique.com.tetris.cell;

import dym.unique.com.tetris.enums.Direction;
import dym.unique.com.tetris.utils.Position;

/**
 * Created by daiyiming on 2016/2/12.
 */
public abstract class Cell {
    private int color = 0; //背景颜色
    private Direction direction = Direction.up;
    protected Position[] positions = null;
    private Position[] testPositions = null;
    protected Position achor = null; //锚点

    public Cell() {
        achor = new Position(4, 0);
        color = initColor();
        positions = new Position[4];
        positions[0] = new Position();
        positions[1] = new Position();
        positions[2] = new Position();
        positions[3] = new Position();
        setUpPositions(positions);
        testPositions = new Position[4];
        testPositions[0] = new Position();
        testPositions[1] = new Position();
        testPositions[2] = new Position();
        testPositions[3] = new Position();
    }

    protected abstract int initColor();
    protected abstract void setUpPositions(Position[] positions);
    protected abstract void setRightPositions(Position[] positions);
    protected abstract void setDownPositions(Position[] positions);
    protected abstract void setLeftPositions(Position[] positions);

    public void rotate() {
        switch (direction) {
            case up: {
                direction = Direction.right;
                setRightPositions(positions);
            } break;
            case right: {
                direction = Direction.down;
                setDownPositions(positions);
            } break;
            case down: {
                direction = Direction.left;
                setLeftPositions(positions);
            } break;
            case left: {
                direction = Direction.up;
                setUpPositions(positions);
            } break;
        }
    }

    public Position[] testRotate() {
        switch (direction) {
            case up: {
                setRightPositions(testPositions);
            } break;
            case right: {
                setDownPositions(testPositions);
            } break;
            case down: {
                setLeftPositions(testPositions);
            } break;
            case left: {
                setUpPositions(testPositions);
            } break;
        }
        return testPositions;
    }

    public void moveLeft() {
        achor.set(achor.getX() - 1, achor.getY());
        resetPositions();
    }

    public void moveRight() {
        achor.set(achor.getX() + 1, achor.getY());
        resetPositions();
    }

    public void moveDown() {
        achor.set(achor.getX(), achor.getY() + 1);
        resetPositions();
    }

    private void resetPositions() {
        switch (direction) {
            case up: {
                setUpPositions(positions);
            } break;
            case right: {
                setRightPositions(positions);
            } break;
            case down: {
                setDownPositions(positions);
            } break;
            case left: {
                setLeftPositions(positions);
            } break;
        }
    }

    public Position[] getPositions() {
        return this.positions;
    }

    public int getColor() {
        return this.color;
    }

}
