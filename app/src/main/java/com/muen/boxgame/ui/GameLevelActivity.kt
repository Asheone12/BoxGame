package com.muen.boxgame.ui

import android.content.Intent
import android.widget.Toast
import com.muen.boxgame.GameInitialData
import com.muen.boxgame.GameSound
import com.muen.boxgame.ui.adapter.GameLevelAdapter
import com.muen.boxgame.PrfsManager
import com.muen.boxgame.R
import com.muen.boxgame.databinding.ActivityGameLevelBinding
import com.muen.boxgame.ui.widget.GameBitmaps
import com.muen.boxgame.util.BaseActivity
import java.io.IOException
import kotlin.system.exitProcess

class GameLevelActivity : BaseActivity<ActivityGameLevelBinding>() {
    private lateinit var mGameLevels_PassedOrNot:Array<Boolean?>

    private var xgqAdapter: GameLevelAdapter? = null

    override fun onCreateViewBinding(): ActivityGameLevelBinding {
        return ActivityGameLevelBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
        if(GameInitialData.GameLevels.size == 0){
            try {
                GameInitialData.readInitialData(resources, GameInitialData.CONFIG_FILE_NAME)
            } catch (e: IOException) {
                Toast.makeText(this, "无法读取配置文件。无法获取关卡数据。退出。", Toast.LENGTH_LONG)
                    .show()
                exitProcess(-1)
            }
        }
        mGameLevels_PassedOrNot = arrayOfNulls(GameInitialData.GameLevels.size)
        for(level in 1 .. GameInitialData.GameLevels.size){
            mGameLevels_PassedOrNot[level - 1] = PrfsManager.getPassedLevel(this, level)
        }
        xgqAdapter = GameLevelAdapter(
            this,
            R.layout.item_level_gridview,
            mGameLevels_PassedOrNot
        )
        viewBinding.gvXuanGuanQia.adapter = xgqAdapter
    }

    override fun initListener() {
        super.initListener()
        viewBinding.gvXuanGuanQia.setOnItemClickListener { parent, view, position, id ->
            val level: Int = position + 1
            val intent = Intent(this@GameLevelActivity, GameViewActivity::class.java)
            intent.putExtra("GuanQia", level)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        xgqAdapter!!.notifyDataSetChanged() //有可能，玩家过了一关
    }

    override fun onDestroy() {
        super.onDestroy()
        GameBitmaps.releaseBitmaps() //释放图片占用的内存
        GameSound.releaseSound() //释放音效占用的内存
    }
}