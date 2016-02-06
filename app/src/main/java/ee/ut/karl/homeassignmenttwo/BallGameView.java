package ee.ut.karl.homeassignmenttwo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Karl on 20/10/2015 for HomeAssignmentTwo
 */
public class BallGameView extends View {

    private Paint linePaint;
    private Paint squarePaint;
    private Paint ballPaint;

    private float ballX;
    private float ballY;
    private float screenWidth;
    private float xPad;
    private float lineBeginningY;
    private float zeroHeight;
    private float boxLeftBound;
    private float boxRightBound;
    private float boxHeight;

    private final float BALL_RADIUS = 30;

    private long jumpStartTime = -1;
    private boolean jumpOnBox = false;

    public float getBallX() {
        return ballX;
    }

    public void setBallX(float ballX) {
        this.ballX = ballX;
    }

    public float getBallY() {
        return ballY;
    }

    public float getBALL_RADIUS() {
        return BALL_RADIUS;
    }

    public float getBoxLeftBound() {
        return boxLeftBound;
    }

    public float getBoxRightBound() {
        return boxRightBound;
    }

    public float getBoxHeight() {
        return boxHeight;
    }

    public BallGameView(Context context) {
        super(context);
        init();
    }

    public BallGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BallGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {

        squarePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        squarePaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.FILL);

        ballPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ballPaint.setStyle(Paint.Style.FILL);
        ballPaint.setARGB(255, 200, 70, 30);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jumpStartTime == -1) {
                    jumpStartTime = getDrawingTime();
                    if (ballX + BALL_RADIUS > boxLeftBound && ballX - BALL_RADIUS < boxRightBound){
                        jumpOnBox = true;
                    }
                }
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Account for padding
        xPad = (float)(getPaddingLeft() + getPaddingRight());
        float yPad = (float) (getPaddingTop() + getPaddingBottom());

        screenWidth = (float)w - xPad;
        Log.v("Size changed: ", Float.toString(screenWidth));
        float screenHeight = (float) h - yPad;

        lineBeginningY = screenHeight / 3 * 2 + yPad;
        zeroHeight = lineBeginningY - BALL_RADIUS;
        if (ballX == 0){
            ballX = xPad + BALL_RADIUS;
        }
        ballY = zeroHeight;
        boxHeight = lineBeginningY - 2 * BALL_RADIUS * 2;

        boxLeftBound = screenWidth / 2;
        boxRightBound = screenWidth / 2 + 2 * BALL_RADIUS * 2;
        Log.v("Box bounds", Float.toString(boxLeftBound) + ", " + Float.toString(boxRightBound));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(xPad, lineBeginningY, screenWidth, lineBeginningY, linePaint);
        canvas.drawRect(boxLeftBound, boxHeight, boxRightBound,
                lineBeginningY, squarePaint);

        float minHeight = zeroHeight;

        // when on the box
        if (ballX + BALL_RADIUS > boxLeftBound && ballX - BALL_RADIUS < boxRightBound){
            minHeight = boxHeight - BALL_RADIUS;
        }

        if (jumpStartTime != -1) {
            float jumpHeight = zeroHeight;
            if (jumpOnBox){
                jumpHeight = boxHeight - BALL_RADIUS;
            }
            long drawTime = getDrawingTime();
            float secondsPassed = (drawTime - jumpStartTime) / 100.0f;
            final double GRAVITATIONAL_CONSTANT = 9.8;
            final double initialSpeed = 60; // px/s
            // since y coordinates are minuses when going up then
            // current height = initialHeight - s * t * (-g * t^2)/2
            ballY = (float) (jumpHeight - (initialSpeed * secondsPassed -
                    (GRAVITATIONAL_CONSTANT * secondsPassed * secondsPassed) / 2));

            float currentVelocity = (float) (initialSpeed - GRAVITATIONAL_CONSTANT * secondsPassed);
            if (ballY >= minHeight && currentVelocity < 0) {
                ballY = minHeight;
                jumpStartTime = -1;
                jumpOnBox = false;
            }
        }else {
            ballY = minHeight;
        }

        canvas.drawCircle(ballX, ballY, BALL_RADIUS, ballPaint);
    }
}
