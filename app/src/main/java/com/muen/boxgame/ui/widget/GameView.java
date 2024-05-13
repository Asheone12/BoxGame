package com.muen.boxgame.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.muen.boxgame.GameData;
import com.muen.boxgame.GameInitialData;
import com.muen.boxgame.GameSound;
import com.muen.boxgame.PrfsManager;
import com.muen.boxgame.R;
import com.muen.boxgame.TCell;
import com.muen.boxgame.ui.GameViewActivity;

import java.io.IOException;



public class GameView extends View {

    private GameViewActivity mGameActivity;
    private float mColumnWidth;
    private float mRowHeight;
    private GameData mGameData;
    private int mGameLevel;
    private int mTopLeft_x = 0;
    private int mTopLeft_y = 0;
    private Rect mManRect = new Rect();          //搬运工所在的位置

    private Rect mRectSoundSwitch = new Rect();

    public GameView(Context context) {
        super(context);
        init((GameViewActivity) context);
    }

    private void init(GameViewActivity context) {
        mGameActivity = context;
        mGameLevel = mGameActivity.getGameLevel();
        setFocusable(true);
        setFocusableInTouchMode(true);
        try {
            mGameData = new GameData(getResources(), mGameLevel);
        } catch (IOException e) {
            Toast.makeText(mGameActivity, "无法打开或读取配置文件。程序退出。", Toast.LENGTH_LONG).show();
            System.exit(-1);
        }
    }

    //为使用布局文件而设置的构造函数
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init((GameViewActivity)context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mColumnWidth = (float)w / mGameData.getBoardColumnNum();
        mRowHeight = (float)w / mGameData.getBoardRowNum();
        mTopLeft_y = (h - w) / 2;
        getManRect(mGameData.getmManPostion(), mRowHeight, mColumnWidth);
        super.onSizeChanged(w, h, oldw, oldh);
    }


    private void getManRect(TCell tCell, float rowHeight, float columnWidth ) {
        int left = (int)(mTopLeft_x + tCell.column * columnWidth);
        int top = (int)(mTopLeft_y + tCell.row * rowHeight);
        int right = (int)(left + columnWidth);
        int bottom = (int)(top + rowHeight);
        mManRect.set(left, top, right, bottom);
    }

    private void goToLevel(int level){
        mGameLevel = level;
        try {
            mGameData = new GameData(getResources(), mGameLevel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mColumnWidth = getWidth() / mGameData.getBoardColumnNum();
        mRowHeight = getWidth() / mGameData.getBoardRowNum();   //正方形区域
        getManRect(mGameData.getmManPostion(), mRowHeight, mColumnWidth);
        invalidate();
    }

    public void resetGame(){
        goToLevel(mGameLevel);
    }

    public void gotoNextLevel() {
        if (mGameLevel < GameInitialData.GameLevels.size())  //mGameLevel从1开始计数
            goToLevel(mGameLevel + 1);
        else
            Toast.makeText(mGameActivity, R.string.no_more_levels, Toast.LENGTH_LONG).show();
    }

    public void gotoPrvLevel(){
        if (mGameLevel > 1)
            goToLevel(mGameLevel - 1);
        else
            Toast.makeText(mGameActivity, R.string.already_first_level, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //背景色
        Paint background = new Paint();
        background.setColor(getResources().getColor(R.color.board_background));
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);
        //游戏区域
        drawGameBoard(canvas);
        //音效开关
        //drawSoundSwitch(canvas);

        //成功过关
        if (mGameData.isGameOver()) {
            drawDoneLabel(canvas);
        }
    }


    private void drawGameBoard(Canvas canvas) {
        Rect destRect = new Rect();
        for (int r = 0; r < mGameData.getBoardRowNum(); r++ )
            for (int c = 0; c < mGameData.getBoardColumnNum(); c++){
                int topleft_x = (int)(mTopLeft_x + c * mColumnWidth);
                int topleft_y = (int)(mTopLeft_y + r * mRowHeight);
                destRect.set(topleft_x, topleft_y,(int)(topleft_x + mColumnWidth) + 2, (int)(topleft_y + mRowHeight) + 2);//+2是为了去除墙体之间的缝隙
                if (mGameData.hasFlag(r, c))
                    canvas.drawBitmap(GameBitmaps.mFlagBitmap, null, destRect, null);
                StringBuffer []gameState = mGameData.getGameState();
                switch (gameState[r].charAt(c)){
                    case GameInitialData.BOX:
                        canvas.drawBitmap(GameBitmaps.mBoxBitmap, null, destRect, null);
                        break;
                    case GameInitialData.FLAG:
                        canvas.drawBitmap(GameBitmaps.mFlagBitmap, null, destRect, null);
                        break;
                    case GameInitialData.NOTHING:
                        break;
                    case GameInitialData.MAN:
                        canvas.drawBitmap(GameBitmaps.mManBitmap, null, destRect, null);
                        break;
                    case GameInitialData.WALL:
//                        destRect.set(destRect.left, destRect.top, destRect.right+2, destRect.bottom + 2);  //+2是为了去除墙体之间的缝隙
                        canvas.drawBitmap(GameBitmaps.mWallBitmap, null, destRect, null);
                        break;
                    case GameInitialData.BOX_FLAG:
                        canvas.drawBitmap(GameBitmaps.mFlagBitmap, null, destRect, null);
                        canvas.drawBitmap(GameBitmaps.mBoxBitmap, null, destRect, null);
                        break;
                    case GameInitialData.MAN_FLAG:
                        canvas.drawBitmap(GameBitmaps.mFlagBitmap, null, destRect, null);
                        canvas.drawBitmap(GameBitmaps.mManBitmap, null, destRect, null);
                        break;
                }
            }
    }

    /*private void drawSoundSwitch(Canvas canvas) {
        mRectSoundSwitch.set(canvas.getWidth() - 2 * (int)mColumnWidth, 0, canvas.getWidth(), 2 * (int)mColumnWidth);
        if (GameSound.isSoundAllowed())
            canvas.drawBitmap(GameBitmaps.mSoundOpenBitmap, null, mRectSoundSwitch, null);
        else
            canvas.drawBitmap(GameBitmaps.mSoundCloseBitmap, null, mRectSoundSwitch, null);
    }*/

    private void drawDoneLabel(Canvas canvas) {
        int begin_x = mTopLeft_x + 120;
        int begin_y = mTopLeft_y + 120;
        int end_x = canvas.getWidth() - 120;
        int end_y = begin_y + (end_x - begin_x);
        Rect label_rect = new Rect(begin_x, begin_y, end_x, end_y);
        Paint paint = new Paint();
        paint.setAlpha(125);
        canvas.drawBitmap(GameBitmaps.mDoneBitmap, null, label_rect, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return super.onTouchEvent(event);

        int touch_x = (int) event.getX();
        int touch_y = (int) event.getY();
        if (!mGameData.isGameOver()) {
            //用户通过在游戏区域触摸来控制搬运工的行进
            //当触摸点落在搬运工所在单元格的上、下、左、右格子n时，即意味着指示搬运工走到格子n上（阻挡问题另外考虑）
            if (touch_left_to_man(touch_x, touch_y))
                mGameData.goLeft();
            if (touch_right_to_man(touch_x, touch_y))
                mGameData.goRight();
            if (touch_above_to_man(touch_x, touch_y))
                mGameData.goUp();
            if (touch_blow_to_man(touch_x, touch_y))
                mGameData.goDown();
            getManRect(mGameData.getmManPostion(), mRowHeight, mColumnWidth);  //重新计算搬运工的屏幕位置
            if (mRectSoundSwitch.contains(touch_x, touch_y))
                GameSound.switchSoundAllowed();
            invalidate();

            if (mGameData.isGameOver()){
                PrfsManager.setPassedLevel(mGameActivity, mGameLevel);   //记住已经通过本关卡
                if (GameSound.isSoundAllowed()) GameSound.playGameOverSound(mGameActivity.getAssets());
            }
        }

        return true;
    }

    private boolean touch_blow_to_man(int touch_x, int touch_y) {
        Rect belowRect = new Rect(mManRect.left, mManRect.top + (int)mRowHeight, mManRect.right, mManRect.bottom + (int)mRowHeight);
        return belowRect.contains(touch_x, touch_y);
    }

    private boolean touch_above_to_man(int touch_x, int touch_y) {
        Rect aboveRect = new Rect(mManRect.left, mManRect.top - (int)mRowHeight, mManRect.right, mManRect.bottom - (int)mRowHeight);
        return aboveRect.contains(touch_x, touch_y);
    }

    private boolean touch_right_to_man(int touch_x, int touch_y) {
        Rect rightRect = new Rect(mManRect.left + (int)mColumnWidth, mManRect.top, mManRect.right + (int)mColumnWidth, mManRect.bottom);
        return rightRect.contains(touch_x, touch_y);
    }

    private boolean touch_left_to_man(int touch_x, int touch_y) {
        Rect leftRect = new Rect(mManRect.left - (int)mColumnWidth, mManRect.top, mManRect.right - (int)mColumnWidth, mManRect.bottom);
        return leftRect.contains(touch_x, touch_y);
    }

    public void undoMove(){
        if (mGameData.undoMove()) {
            invalidate();
            getManRect(mGameData.getmManPostion(), mRowHeight, mColumnWidth);  //重新计算搬运工的位置
        }
    }
}
