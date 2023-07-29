package com.dastanapps.tettris.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.dastanapps.tettris.MainActivity

class GridAdapter(private val context: Context, private val gameGrid: Array<IntArray>) :
    BaseAdapter() {

    override fun getCount(): Int = MainActivity.GRID_WIDTH * MainActivity.GRID_HEIGHT

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView: ImageView = if (convertView == null) {
            ImageView(context)
        } else {
            convertView as ImageView
        }

        val row = position / gameGrid[0].size
        val col = position % gameGrid[0].size
        val cellValue = gameGrid[row][col]

        imageView.setBackgroundColor(if (cellValue != 0) Color.CYAN else Color.TRANSPARENT)


        imageView.layoutParams = ViewGroup.LayoutParams(80, 80)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setPadding(1, 1, 1, 1)
        return imageView
    }
}
