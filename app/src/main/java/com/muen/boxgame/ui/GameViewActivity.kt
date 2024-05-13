package com.muen.boxgame.ui

import android.content.Intent
import android.os.Bundle
import com.muen.boxgame.GameBitmaps
import com.muen.boxgame.GameSound
import com.muen.boxgame.databinding.ActivityGameViewBinding
import com.muen.boxgame.util.BaseActivity
import kotlin.system.exitProcess

class GameViewActivity : BaseActivity<ActivityGameViewBinding>() {
    private var mGameLevel = 0
    override fun onCreateViewBinding(): ActivityGameViewBinding {
        val intent = intent
        mGameLevel = intent.getIntExtra("GuanQia", 1)
        GameBitmaps.loadBitmaps(resources) //须在生成GameView对象前加载它用到的图片
        GameSound.loadSound(assets) //须在生成GameView对象前加载它会用到的音效
        return ActivityGameViewBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
    }

    override fun initListener() {
        super.initListener()
        viewBinding.btnPrvLevel.setOnClickListener {
            viewBinding.gameBoard.gotoPrvLevel()
        }

        viewBinding.btnNextLevel.setOnClickListener {
            viewBinding.gameBoard.gotoNextLevel()
        }

        viewBinding.btnReset.setOnClickListener {
            viewBinding.gameBoard.resetGame()
        }

        viewBinding.btnExit.setOnClickListener {
            exitApp()
        }

    }

    fun getGameLevel(): Int {
        return mGameLevel
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        GameBitmaps.releaseBitmaps() //释放图片占用的内存
        GameSound.releaseSound() //释放音效占用的内存

    }

    private fun exitApp() {
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
        exitProcess(0)
    }
}