package au.com.hearty.view.decorator

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import au.com.hearty.R

class MeasurementItemDividerDecorator constructor(
    private val margin: Int,
    private val context: Context
) : DividerItemDecoration(context, HORIZONTAL) {

    private val mDivider = context.getResources().getDrawable(R.drawable.item_measurement_divider)

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left: Int = parent.paddingLeft + margin
        val right: Int = parent.width - parent.paddingRight - margin

        val childCount: Int = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)

            val params =
                child.layoutParams as RecyclerView.LayoutParams
            val top: Int = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }
}