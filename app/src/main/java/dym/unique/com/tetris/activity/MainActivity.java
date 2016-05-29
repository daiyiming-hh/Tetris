package dym.unique.com.tetris.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import dym.unique.com.tetris.R;
import dym.unique.com.tetris.dialog.InquiryDialog;
import dym.unique.com.tetris.utils.MaxScoreTool;
import dym.unique.com.tetris.view.GameView;

public class MainActivity extends Activity implements View.OnClickListener, GameView.OnGameMessageChangeListener, View.OnTouchListener {

    private TextView tv_time = null;
    private TextView tv_score = null;
    private RelativeLayout rl_cover = null;
    private ImageView img_pause = null;
    private ImageView img_left = null;
    private ImageView img_right = null;
    private ImageView img_rotate = null;
    private GameView gv_view = null;
    private MaxScoreTool maxScore = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        //获得控件
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_score = (TextView) findViewById(R.id.tv_score);
        rl_cover = (RelativeLayout) findViewById(R.id.rl_cover);
        img_pause = (ImageView) findViewById(R.id.img_pause);
        img_left = (ImageView) findViewById(R.id.img_left);
        img_right = (ImageView) findViewById(R.id.img_right);
        img_rotate = (ImageView) findViewById(R.id.img_rotate);
        gv_view = (GameView) findViewById(R.id.gv_view);

        //初始化事件
        img_left.setOnClickListener(this);
        img_right.setOnClickListener(this);
        img_rotate.setOnClickListener(this);
        img_pause.setOnClickListener(this);
        gv_view.setOnGameMessageChangeListener(this);
        rl_cover.setOnTouchListener(this);

        //设置遮盖层没有被隐藏
        rl_cover.setTag(false);

        //获取工具
        maxScore = new MaxScoreTool(this);
    }

    private void flushMaxScore() {
        //刷新游戏数据
        int ms = maxScore.get();
        if (gv_view.getScore() > ms) {
            maxScore.save(gv_view.getScore());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_left: {
                gv_view.moveCellLeft();
            } break;
            case R.id.img_right: {
                gv_view.moveCellRight();
            } break;
            case R.id.img_rotate: {
                gv_view.rotateCell();
            } break;
            case R.id.img_pause: {
                pauseGame();
            } break;
        }
    }

    private void pauseGame() {
        gv_view.pause();
        new InquiryDialog(this, "游戏暂停\n" + "分数：" + gv_view.getScore() + "   记录：" + maxScore.get(), new String[]{"继续游戏", "重新开始", "退出"}, false, new InquiryDialog.OnSelectionClickListener() {
            @Override
            public void OnSelectionClicked(int position) {
                switch (position) {
                    case 0: {
                        gv_view.restart();
                    } break;
                    case 1: {
                        flushMaxScore();
                        restartGame();
                    } break;
                    case 2: {
                        flushMaxScore();
                        MainActivity.this.finish();
                    } break;
                }
            }
        }).show();
    }

    private void restartGame() {
        gv_view.clearCells();
        gv_view.clearMessage();
        gv_view.start();
    }

    @Override
    public void onBackPressed() {
        if (! gv_view.isPause()) {
            pauseGame();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void OnGameTimeChanged(int time) {
        String minute = (time / 60) < 10 ? "0" + (time / 60) : String.valueOf(time / 60);
        String second = (time % 60) < 10 ? "0" + (time % 60) : String.valueOf(time % 60);
        tv_time.setText(minute + ":" + second);
    }

    @Override
    public void OnGameScoreChanged(int score) {
        tv_score.setText(String.valueOf(score));
    }

    @Override
    public void OnGameOver() {
        if (! gv_view.isPause()) {
            //暂停游戏
            gv_view.pause();
            new InquiryDialog(this, "游戏结束\n分数：" + gv_view.getScore() + "   记录：" + maxScore.get(), new String[]{"重新开始", "退出游戏"}, false, new InquiryDialog.OnSelectionClickListener() {
                @Override
                public void OnSelectionClicked(int position) {
                    switch (position) {
                        case 0: {
                            flushMaxScore();
                            restartGame();
                        }
                        break;
                        case 1: {
                            flushMaxScore();
                            MainActivity.this.finish();
                        }
                        break;
                    }
                }
            }).show();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.rl_cover) { //屏蔽覆盖层的点击事件
            if ((event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) && (! (boolean) rl_cover.getTag())) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(rl_cover, "alpha", 1, 0);
                animator.setDuration(300);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        rl_cover.setTag(true);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rl_cover.setVisibility(View.GONE);
                        gv_view.start();
                    }
                });
                animator.start();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        if (! gv_view.isPause()) {
            pauseGame();
        }
        super.onPause();
    }
}





















