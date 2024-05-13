package com.muen.boxgame.ui

import android.R
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.muen.boxgame.databinding.ActivityGameIntroBinding
import com.muen.boxgame.util.BaseActivity

class GameIntroActivity : BaseActivity<ActivityGameIntroBinding>() {
    override fun onCreateViewBinding(): ActivityGameIntroBinding {
        return ActivityGameIntroBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        val gameIntroAdapter = GameIntroAdapter(
            this,
            R.layout.simple_list_item_1,
            resources.getStringArray(com.muen.boxgame.R.array.game_intro)
        )
            viewBinding.lvGameIntro.adapter = gameIntroAdapter
    }

    private class GameIntroAdapter(
        private val mContext: Context,
        resource: Int,
        private val mIntroStatements: Array<String>
    ) :
        ArrayAdapter<String>(mContext, resource, mIntroStatements) {
        override fun getCount(): Int {
            return mIntroStatements.size
        }

        override fun getItem(position: Int): String {
            return mIntroStatements[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val tvStatement: TextView = if (convertView == null) TextView(mContext) else convertView as TextView
            if (position % 2 == 0) tvStatement.setTextColor(Color.BLACK) else tvStatement.setTextColor(
                Color.BLUE
            )
            tvStatement.text = mIntroStatements[position]
            return tvStatement
        }
    }
}