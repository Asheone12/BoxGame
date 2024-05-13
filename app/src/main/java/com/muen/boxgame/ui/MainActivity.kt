package com.muen.boxgame.ui

import android.content.Intent
import com.muen.boxgame.databinding.ActivityMainBinding
import com.muen.boxgame.util.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun onCreateViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initListener() {
        super.initListener()
        viewBinding.btnStartGame.setOnClickListener {
            val xgq = Intent(this, GameLevelActivity::class.java)
            startActivity(xgq)

        }

        viewBinding.btnGameIntro.setOnClickListener {
            val i = Intent(this, GameIntroActivity::class.java)
            startActivity(i)
        }

        viewBinding.btnExit.setOnClickListener {
            finish()
        }
    }
}