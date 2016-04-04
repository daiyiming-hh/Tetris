package dym.unique.com.tetris.cell;

import android.graphics.Color;

import dym.unique.com.tetris.utils.Position;

/**
 * Created by daiyiming on 2016/2/13.
 */
public class JCell extends Cell {
    @Override
    protected int initColor() {
        return Color.parseColor("#0000EF");
    }

    @Override
    protected void setUpPositions(Position[] positions) {
        positions[0].set(achor.getX() - 1, achor.getY() - 1);
        positions[1].set(achor.getX(), achor.getY() - 1);
        positions[2].set(achor.getX(), achor.getY() - 2);
        positions[3].set(achor.getX(), achor.getY() - 3);
    }

    @Override
    protected void setRightPositions(Position[] positions) {
        positions[0].set(achor.getX() - 1, achor.getY() - 1);
        positions[1].set(achor.getX() - 1, achor.getY() - 2);
        positions[2].set(achor.getX(), achor.getY() - 1);
        positions[3].set(achor.getX() + 1, achor.getY() - 1);
    }

    @Override
    protected void setDownPositions(Position[] positions) {
        positions[0].set(achor.getX() - 1, achor.getY() - 1);
        positions[1].set(achor.getX() - 1, achor.getY() - 2);
        positions[2].set(achor.getX() - 1, achor.getY() - 3);
        positions[3].set(achor.getX(), achor.getY() - 3);
    }

    @Override
    protected void setLeftPositions(Position[] positions) {
        positions[0].set(achor.getX() - 1, achor.getY() - 2);
        positions[1].set(achor.getX(), achor.getY() - 2);
        positions[2].set(achor.getX() + 1, achor.getY() - 2);
        positions[3].set(achor.getX() + 1, achor.getY() - 1);
    }
}
