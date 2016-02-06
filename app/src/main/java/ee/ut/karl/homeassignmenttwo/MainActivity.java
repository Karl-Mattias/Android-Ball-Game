package ee.ut.karl.homeassignmenttwo;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private BallGameView ballGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ballGameView = new BallGameView(this);
        setContentView(ballGameView);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float y = event.values[1];

        float ballX = ballGameView.getBallX();
        float ballY = ballGameView.getBallY();
        float ballRadius = ballGameView.getBALL_RADIUS();
        float screenWidth = ballGameView.getWidth();
        float boxLeftBound = ballGameView.getBoxLeftBound();
        float boxRightBound = ballGameView.getBoxRightBound();
        float boxHeight = ballGameView.getBoxHeight();
        int move = 0;

        // Moving logic when tilting phone
        if (y > 2 && ballX < screenWidth - ballRadius){
            if (ballX + 8 > boxLeftBound - ballRadius && ballX + 8 < boxRightBound && ballY > boxHeight){
                Log.v("Blocked", "Blocked from right side");
            }else {
                move = 8;
            }
        }else if (y < -2 && ballX > ballRadius){
            if (ballX - 8 < boxRightBound + ballRadius && ballX - 8 > boxLeftBound && ballY > boxHeight){
                Log.v("Blocked", "Blocked from left side");
            }else {
                move = -8;
            }
        }

        ballX += move;
        ballGameView.setBallX(ballX);

        ballGameView.invalidate();
        ballGameView.requestLayout();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
