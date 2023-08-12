package com.example.myapplication.FishingContent.recycler

import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView

class PhDividerItemDecoration(val mHeight : Float, val mColor : Int) : RecyclerView.ItemDecoration() {
    val mPaint = Paint()

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        mPaint.color = mColor

        super.onDrawOver(c, parent, state);

        val left = parent.getPaddingLeft().toFloat()
        val right = parent.getWidth() - parent.getPaddingRight().toFloat()
        val childCount = parent.getChildCount()

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i);
            val params = (child.getLayoutParams() as RecyclerView.LayoutParams)
            val top = (child.getBottom() + params.bottomMargin).toFloat()
            val bottom = top + mHeight;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }
}