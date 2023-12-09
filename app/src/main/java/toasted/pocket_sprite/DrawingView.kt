package toasted.pocket_sprite

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View



@SuppressLint("ViewConstructor")
class DrawingView(context: Context, attrs: AttributeSet, bitmap: Bitmap) : View(context, attrs) {
    private var paint: Paint = Paint()
    private var bit = bitmap

    init {
        // Initialize your paint object here
        paint.color = Color.BLACK
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bit, 0f, 0f, paint)
    }
}