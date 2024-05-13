package com.muen.boxgame.ui.widget;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.muen.boxgame.R;


public  class GameBitmaps {
    public static  Bitmap mWallBitmap;
    public static  Bitmap mManBitmap;
    public static  Bitmap mBoxBitmap;
    public static  Bitmap mFlagBitmap;
    public static  Bitmap mDoneBitmap;
    //public static  Bitmap mSoundOpenBitmap;
    public static  Bitmap mSoundCloseBitmap;

    public static void loadBitmaps(Resources res) {
        if (mBoxBitmap == null)
            mBoxBitmap = BitmapFactory.decodeResource(res, R.drawable.box_48x48);
        if (mManBitmap == null)
            mManBitmap = BitmapFactory.decodeResource(res, R.drawable.eggman_48x48);
        if (mFlagBitmap == null)
            mFlagBitmap = BitmapFactory.decodeResource(res, R.drawable.flag_48x48);
        if (mWallBitmap == null)
            mWallBitmap = BitmapFactory.decodeResource(res, R.drawable.wall_48x48);
        if (mDoneBitmap == null)
            mDoneBitmap = BitmapFactory.decodeResource(res, R.drawable.done_72x72);
//        if (mSoundOpenBitmap == null)
//            mSoundOpenBitmap = BitmapFactory.decodeResource(res, R.drawable.laba_open_48x48);
        if (mSoundCloseBitmap == null)
            mSoundCloseBitmap = BitmapFactory.decodeResource(res, R.drawable.laba_close_48x48);
    }

    public static void releaseBitmaps(){
        //game board
        releaseBmp(mBoxBitmap);
        releaseBmp(mManBitmap);
        releaseBmp(mWallBitmap);
        releaseBmp(mFlagBitmap);
        releaseBmp(mDoneBitmap);
        //releaseBmp(mSoundOpenBitmap);
        releaseBmp(mSoundCloseBitmap);
    }

    private static void releaseBmp(Bitmap bitmap) {
        if (bitmap != null){
            bitmap.recycle();
            bitmap = null;
        }
    }
}
