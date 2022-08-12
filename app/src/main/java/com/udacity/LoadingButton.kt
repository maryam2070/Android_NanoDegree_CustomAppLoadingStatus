package com.udacity

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.animation.addListener
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.drawable.toDrawable
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var pCanvas: Canvas? =null
    private var f=false

    private val valueAnimator = ValueAnimator.ofInt(0,360)

    private var downloadWidth=0
    var buttonText="Download"

    var background_color = 0
    var textColor = 0
    var loading_color=0

    var buttonState: ButtonState by
    Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        Log.d("AAAAAAAAAA",new.toString())
        Log.d("AAAAAAAAAA",old.toString())
        Log.d("AAAAAAAAAA",p.toString())
        downloadWidth=0
        if(new==ButtonState.Completed) {
            buttonText="download"
            valueAnimator.cancel()
        }else {
            buttonText="downloading"
            valueAnimator.start()
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 40.0f
        typeface = Typeface.create( "", Typeface.BOLD)
        color=Color.WHITE //text color
        //background=resources.getColor(R.color.colorPrimary)
     //  setBackgroundColor(resources.getColor(R.color.colorPrimary)) //background
    }

    init {

        context.withStyledAttributes(attrs, R.styleable.LoadingButton)
        {
            background_color= getColor(R.styleable.LoadingButton_background_color, 0)
            loading_color= getColor(R.styleable.LoadingButton_loading_color, 0)
            textColor=getColor(R.styleable.LoadingButton_text_color,0)

        }

        buttonText="Download"
        isClickable=true
        valueAnimator.repeatCount=ValueAnimator.INFINITE
        valueAnimator.repeatMode=ValueAnimator.RESTART
        valueAnimator.setDuration(1000)

        valueAnimator.addUpdateListener { animation->
            downloadWidth= animation.animatedValue.toString().toInt()

          //  Log.d("AAAAAAAAAA",downloadWidth.toString())
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.setColor(background_color)
        canvas!!.drawRect(RectF(0F,0F,widthSize.toFloat(),heightSize.toFloat()),paint)

        paint.setColor(loading_color)
        canvas!!.drawRect(RectF(0F,0F,(downloadWidth*widthSize)/360F,heightSize.toFloat()),paint)

        paint.setColor(textColor)
        canvas!!.drawText(buttonText,300F,70F,paint)

        paint.setColor(Color.YELLOW)
        canvas.drawArc(480F,40F,520F,80F,0F,downloadWidth.toFloat(),true,paint)
        invalidate()
    }


    override fun performClick(): Boolean {
        //valueAnimator.start()

        invalidate()
        return super.performClick()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}