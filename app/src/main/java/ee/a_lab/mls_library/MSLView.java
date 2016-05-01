package ee.a_lab.mls_library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by giorg_000 on 02.02.2016.
 */
public class MSLView extends View {

    private int mBallPositionX;
    private int mBallPositionY;
    private static int mBallRadius;
    private static Paint ballColor;
    private int mViewWidth;
    private int mViewHeight;
    private int mRubberStart;

    private int mDistanceBetweenRubberAndMagnetInCm;
    private int mBallRadiusCm;


    private BallPositionChangeListener ballPositionChangeListener;


    public interface BallPositionChangeListener {
        void onChangePosition(int x, int y);
    }

    public void setOnBallPositionChangeListener(BallPositionChangeListener eventListener) {
        this.ballPositionChangeListener = eventListener;
    }

    public MSLView(Context context) {
        super(context);
        init();
    }

    public MSLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MLSView,
                0, 0
        );

        try {
            mDistanceBetweenRubberAndMagnetInCm = a.getColor(R.styleable.MLSView_distanceMagnetRubber, 50);
            mBallRadiusCm = a.getColor(R.styleable.MLSView_ballRadius, 20);

            init();
        } finally {
            a.recycle();
        }
    }

    public MSLView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MLSView,
                0, 0
        );

        try {
            mDistanceBetweenRubberAndMagnetInCm = a.getColor(R.styleable.MLSView_distanceMagnetRubber, 50);
            mBallRadiusCm = a.getColor(R.styleable.MLSView_ballRadius, 20);

            init();
        } finally {
            a.recycle();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int desiredWidth = 100;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }


        if (height * 3 / 4 > width) {
            height = width * 4 / 3;
        } else {
            width = height * 3 / 4;
        }

        mViewWidth = width;
        mViewHeight = height;


        mRubberStart = height * 47 / 100;
        mBallRadius = height / 20;
        mBallPositionX = width / 2;
        mBallPositionY = mRubberStart - mBallRadius;

        int distanceTillMagnet = height / 4;
        int distanceBetweenMagnetAndRubber = mRubberStart - distanceTillMagnet - 2 * mBallRadius;


        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    private void init() {
        ballColor = new Paint();
        ballColor.setColor(Color.RED);
        ballColor.setStyle(Paint.Style.FILL_AND_STROKE);
        ballColor.setStrokeWidth(10);
    }

    @Override
    public void onDraw(final Canvas canvas) {

        Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.mls);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);


        ballColor.setColor(Color.RED);
        canvas.drawCircle(mBallPositionX, mBallPositionY, mBallRadius, ballColor);

        ballColor.setColor(Color.BLACK);
        canvas.drawLine(mBallPositionX - mBallRadius - 10, mBallPositionY, mBallPositionX + mBallRadius + 10, mBallPositionY, ballColor);

    }


    public void setBallPosition(int y) {
        mBallPositionY = mRubberStart - mBallRadius - y;
        invalidate();
    }

}
