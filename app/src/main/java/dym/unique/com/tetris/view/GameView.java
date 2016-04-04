package dym.unique.com.tetris.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

import dym.unique.com.tetris.cell.Cell;
import dym.unique.com.tetris.cell.ICell;
import dym.unique.com.tetris.cell.JCell;
import dym.unique.com.tetris.cell.LCell;
import dym.unique.com.tetris.cell.OCell;
import dym.unique.com.tetris.cell.SCell;
import dym.unique.com.tetris.cell.TCell;
import dym.unique.com.tetris.cell.ZCell;
import dym.unique.com.tetris.utils.Position;

/**
 * Created by daiyiming on 2016/2/12.
 */
public class GameView extends View {
    private final static int CELL_SIZE_OFFSET = 1;
    private final static int ROW_NUMBER = 20;
    private final static int COLUMN_NUMBER = 10;
    private final static int PLAY_DURATION = 500;

    private int cellSize = 0;
    private int offsetX = 0;
    private Paint cellPaint = null;
    private Paint backgroundPaint = null;
    private Cell moveCell = null;
    private int[][] cells = null; //内部位置保存颜色
    private boolean isPause = true;
    private Random random = null;
    private int minScrollDistance = 0;
    private int downX = 0, downY = 0;

    private Runnable playRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isPause) {
                //检验游戏是否结束
                for (int i = 0; i < COLUMN_NUMBER; i++) {
                    if (cells[0][i] != 0 && listener != null) {
                        listener.OnGameOver();
                    }
                }
                //检测是否有满格满格消除
                for (int i = ROW_NUMBER - 1; i >= 0; i--) {
                    boolean isFull = true;
                    for (int j = 0; j < COLUMN_NUMBER; j++) {
                        if (cells[i][j] == 0) {
                            isFull = false;
                            break;
                        }
                    }
                    if (isFull) {
                        for (int j = i; j > 0; j--) {
                            for (int k = 0; k < COLUMN_NUMBER; k++) {
                                cells[j][k] = cells[j - 1][k];
                            }
                        }
                        for (int j = 0; j < COLUMN_NUMBER; j++) {
                            cells[0][j] = 0;
                        }
                        score++;
                        if (listener != null) {
                            listener.OnGameScoreChanged(score);
                        }
                        i++;
                    }
                }
                //检测Cell是否落地
                boolean isFell = false;
                for (int i = 0; i < moveCell.getPositions().length; i++) {
                    if (moveCell.getPositions()[i].getY() == ROW_NUMBER - 1 || ((moveCell.getPositions()[i].getY() + 1 >= 0) && (moveCell.getPositions()[i].getX() >= 0) && (cells[moveCell.getPositions()[i].getY() + 1][moveCell.getPositions()[i].getX()] != 0))) {
                        isFell = true;
                        break;
                    }
                }
                if (isFell) { //如果已经落地
                    //将Cell填入数组
                    for (int i = 0; i < moveCell.getPositions().length; i++) {
                        if (moveCell.getPositions()[i].getX() >= 0 && moveCell.getPositions()[i].getY() >= 0) {
                            cells[moveCell.getPositions()[i].getY()][moveCell.getPositions()[i].getX()] = moveCell.getColor();
                        }
                    }
                    //重新生成移动的Cell
                    moveCell = generateCell();
                } else { //如果没有落地
                    moveCell.moveDown();
                }

                time += (float) PLAY_DURATION / 1000;
                if (listener != null) {
                    listener.OnGameTimeChanged((int) time);
                }

                GameView.this.invalidate();
                handler.postDelayed(this, PLAY_DURATION);
            }
        }
    };
    public Handler handler = new Handler();

    private int score = 0;
    private float time = 0;
    private OnGameMessageChangeListener listener = null;

    public interface OnGameMessageChangeListener {
        void OnGameTimeChanged(int time);
        void OnGameScoreChanged(int score);
        void OnGameOver();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        cellPaint = new Paint();
        cellPaint.setDither(true);
        cellPaint.setAntiAlias(true);
        cellPaint.setStyle(Paint.Style.FILL);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setDither(true);
        backgroundPaint.setStyle(Paint.Style.FILL);

        random = new Random();

        minScrollDistance = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());

        cells = new int[ROW_NUMBER][COLUMN_NUMBER];
        clearCells();
    }

    public void clearCells() {
        for (int i = 0; i < ROW_NUMBER; i ++) {
            for (int j = 0; j < COLUMN_NUMBER; j ++) {
                cells[i][j] = 0;
            }
        }
    }

    public void start() {
        isPause = false;
        moveCell = generateCell();
        handler.removeCallbacks(playRunnable);
        handler.postDelayed(playRunnable, PLAY_DURATION);
    }

    public void restart() {
        isPause = false;
        if (moveCell == null) {
            moveCell = generateCell();
        }
        handler.removeCallbacks(playRunnable);
        handler.postDelayed(playRunnable, PLAY_DURATION);
    }

    public void pause() {
        isPause = true;
        handler.removeCallbacks(playRunnable);
    }

    private Cell generateCell() {
        Cell cell = null;
        switch (random.nextInt(7)) {
            case 0: {
                cell = new ICell();
            } break;
            case 1: {
                cell = new JCell();
            } break;
            case 2: {
                cell = new LCell();
            } break;
            case 3: {
                cell = new OCell();
            } break;
            case 4: {
                cell = new SCell();
            } break;
            case 5: {
                cell = new TCell();
            } break;
            case 6: {
                cell = new ZCell();
            } break;
        }
        return cell;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        cellSize = this.getMeasuredHeight() / ROW_NUMBER;
        offsetX = (this.getMeasuredWidth() - cellSize * COLUMN_NUMBER) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        //绘制背景
        canvas.drawColor(Color.parseColor("#f1f1f1"));
        RectF backgroundRectf = new RectF(offsetX - CELL_SIZE_OFFSET, 0, this.getWidth() - offsetX + CELL_SIZE_OFFSET, this.getHeight());
        canvas.drawRect(backgroundRectf, backgroundPaint);
        //绘制移动Cell
        if (moveCell != null) {
            for (int i = 0; i < moveCell.getPositions().length; i++) {
                drawCell(canvas, moveCell.getPositions()[i].getX(), moveCell.getPositions()[i].getY(), moveCell.getColor());
            }
        }
        //绘制数组
        for (int i = 0; i < ROW_NUMBER; i ++) {
            for (int j = 0; j < COLUMN_NUMBER; j ++) {
                if (cells[i][j] != 0) {
                    drawCell(canvas, j, i, cells[i][j]);
                }
            }
        }
        canvas.restore();
    }

    private void drawCell(Canvas canvas, int x, int y, int color) {
        cellPaint.setColor(color);
        RectF rectF = new RectF(offsetX + x * cellSize + CELL_SIZE_OFFSET, y * cellSize + CELL_SIZE_OFFSET, offsetX + (x + CELL_SIZE_OFFSET) * cellSize - CELL_SIZE_OFFSET, (y + 1) * cellSize - CELL_SIZE_OFFSET);
        canvas.drawRoundRect(rectF, cellSize / 8, cellSize / 8, cellPaint);
    }

    public void rotateCell() {
        if (! isPause) {
            Position[] testPositions = moveCell.testRotate();
            boolean isCanMove = true;
            for (int i = 0; i < testPositions.length; i ++) {
                if (testPositions[i].getX() < 0
                        || testPositions[i].getX() >= COLUMN_NUMBER
                        || testPositions[i].getY() < 0
                        || testPositions[i].getY() >= ROW_NUMBER
                        || cells[testPositions[i].getY()][testPositions[i].getX()] != 0) {
                    isCanMove = false;
                    break;
                }
            }
            if (isCanMove) {
                moveCell.rotate();
                this.invalidate();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = (int) event.getX();
                downY = (int) event.getY();
            } break;
            case MotionEvent.ACTION_UP: {
                if (event.getY() > downY && Math.abs(downX - event.getX()) < event.getY() - downY && (event.getY() - downY) >= minScrollDistance) { //下滑
                    moveCellOver();
                } else if (event.getY() < downY && Math.abs(downX - event.getX()) < downY - event.getY() && (downY - event.getY()) >= minScrollDistance) { //上滑
                    rotateCell();
                } else if (event.getX() > downX && Math.abs(downY - event.getY()) < event.getX() - downX && (event.getX() - downX) >= minScrollDistance) { //右滑
                    moveCellRight();
                } else if (event.getX() < downX && Math.abs(downY - event.getY()) < downX - event.getX() && (downX - event.getX()) >= minScrollDistance) { //左滑
                    moveCellLeft();
                }
            } break;
        }
        return true;
    }

    public void moveCellLeft() {
        if (! isPause) {
            //检测左侧是否是边缘或者有无方块
            boolean isCanMove = true;
            for (int i = 0; i < moveCell.getPositions().length; i++) {
                if (moveCell.getPositions()[i].getX() == 0 || ((moveCell.getPositions()[i].getY() >= 0) && (moveCell.getPositions()[i].getX() - 1 >= 0) && (cells[moveCell.getPositions()[i].getY()][moveCell.getPositions()[i].getX() - 1] != 0))) {
                    isCanMove = false;
                }
            }
            if (isCanMove) {
                moveCell.moveLeft();
                this.invalidate();
            }
        }
    }

    public void moveCellRight() {
        if (! isPause) {
            //检测右侧是否是边缘或者有无方块
            boolean isCanMove = true;
            for (int i = 0; i < moveCell.getPositions().length; i++) {
                if (moveCell.getPositions()[i].getX() == COLUMN_NUMBER - 1 || ((moveCell.getPositions()[i].getY() >= 0) && (moveCell.getPositions()[i].getX() + 1 < COLUMN_NUMBER) && (cells[moveCell.getPositions()[i].getY()][moveCell.getPositions()[i].getX() + 1] != 0))) {
                    isCanMove = false;
                }
            }
            if (isCanMove) {
                moveCell.moveRight();
                this.invalidate();
            }
        }
    }

    public int getScore() {
        return this.score;
    }

    public void clearMessage() {
        score = 0;
        time = 0;
        if (listener != null) {
            listener.OnGameTimeChanged((int) time);
            listener.OnGameScoreChanged(score);
        }
    }

    private void moveCellOver() {
        if (! isPause) {
            //找到要碰到低的最短的距离
            int minDistance = ROW_NUMBER;
            for (int i = ROW_NUMBER - 1; i >= 0; i --) {
                for (int j = 0; j < moveCell.getPositions().length; j ++) {
                    if (i > moveCell.getPositions()[j].getY()) {
                        if (i == ROW_NUMBER - 1 && cells[i][moveCell.getPositions()[j].getX()] == 0) {
                            int distance = i - moveCell.getPositions()[j].getY();
                            if (distance < minDistance) {
                                minDistance = distance;
                            }
                        } else if (cells[i][moveCell.getPositions()[j].getX()] != 0) {
                            int distance = i - moveCell.getPositions()[j].getY() - 1;
                            if (distance < minDistance) {
                                minDistance = distance;
                            }
                        }
                    }
                }
            }
            if (minDistance > 0 && minDistance != ROW_NUMBER) {
                for (int i = 0; i < moveCell.getPositions().length; i ++) {
                    if (moveCell.getPositions()[i].getX() >= 0 && moveCell.getPositions()[i].getY() + minDistance >= 0 && moveCell.getPositions()[i].getY() + minDistance < ROW_NUMBER) {
                        cells[moveCell.getPositions()[i].getY() + minDistance][moveCell.getPositions()[i].getX()] = moveCell.getColor();
                    }
                }
                moveCell = generateCell();
                this.invalidate();
            }
        }
    }

    public boolean isPause() {
        return isPause;
    }

    public void setOnGameMessageChangeListener(OnGameMessageChangeListener listener) {
        this.listener = listener;
    }

}
