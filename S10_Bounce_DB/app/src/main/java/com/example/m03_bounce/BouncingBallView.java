package com.example.m03_bounce;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BouncingBallView extends View {

    // List of balls
    private ArrayList<Ball> balls = new ArrayList<Ball>();
    // Never used, tested without this and it worked + there are no default balls since I wanted them all added by the user
    // private Ball ball_1;
    private Box box;

    // To add balls to db on onTouch
    private DBClass db;
    private int id;

    // For touch inputs - previous touch (x, y)
    private float previousX;
    private float previousY;

    public BouncingBallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.v("BouncingBallView", "Constructor BouncingBallView");

        // Create the box, uses ARGB colors
        box = new Box(Color.WHITE);

        // Get balls from the database and add them to the list
        DBClass DBtest = new DBClass(context);
        List<DataModel> ALL = DBtest.findAll();
        for (DataModel one : ALL) {
            Log.w("DataModel", "Item => " + one.toString());
            balls.add(new Ball(one.getModelColor(), one.getModelX(),
                    one.getModelY(), one.getModelDX(), one.getModelDY()));
        }

        // To enable keypad
        this.setFocusable(true);
        this.requestFocus();
        // To enable touch mode
        this.setFocusableInTouchMode(true);
    }

    // Since this is instantiated by getting the view from the XML, I made this so these values can be passed here so the touch balls can be saved
    public void setDB(DBClass db, int id) {
        this.db = db;
        this.id = id;
    }

    // Called back to draw the view. Also called after invalidate().
    @Override
    protected void onDraw(Canvas canvas) {
        Log.v("BouncingBallView", "onDraw");

        // Draw the components
        box.draw(canvas);
        //canvas.drawARGB(0,25,25,25);
        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        // Draw all the balls and update their positions
        for (Ball b : balls) {
            b.draw(canvas);
            b.moveWithCollisionDetection(box);
        }
        // Force a re-draw
        this.invalidate();
    }

    // Called back when the view is first created or its size changes.
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set the movement bounds for the ball
        box.set(0, 0, w, h);
        Log.w("BouncingBallLog", "onSizeChanged w=" + w + " h=" + h);
    }

    // Touch-input handler
    // Works the same as before, but also saves ball to DB
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
        float deltaX, deltaY;
        // Never used
        // float scalingFactor = 5.0f / ((box.xMax > box.yMax) ? box.yMax : box.xMax);
        switch (event.getAction()) {
            // Do this way, with motion DOWN and UP and not MOVE to be able to spawn balls on touch and let ago instead of just touch
            case MotionEvent.ACTION_DOWN:
                previousX = currentX;
                previousY = currentY;
                break;

            case MotionEvent.ACTION_UP:
                // Modify rotational angles according to movement
                deltaX = currentX - previousX;
                deltaY = currentY - previousY;
                // As mentioned in an earlier comment, not using ball_1 anymore so this is commented out
                //ball_1.setSpeedX(ball_1.getSpeedX() + deltaX*scalingFactor);
                //ball_1.setSpeedY(ball_1.getSpeedY() + deltaY*scalingFactor);
                //Log.w("BouncingBallLog", " Xspeed=" + ball_1.speedX + " Yspeed=" + ball_1.speedY);
                Log.w("BouncingBallLog", "x,y= " + previousX + " ," + previousY + "  Xdiff=" + deltaX + " Ydiff=" + deltaY);

                // Add ball to ball list and then save it to the db
                balls.add(new Ball(Color.BLUE, previousX, previousY, deltaX, deltaY));
                DataModel data = new DataModel(id++, "TouchBall", previousX, previousY, deltaX, deltaY, Color.BLUE);
                db.save(data);

                // A way to clear list when too many balls, also clears the DB for parity between the screen and what's going on behind the scenes
                // Also no longer creates a single ball when the list is reset
                if (balls.size() > 20) {
                    Log.v("BouncingBallLog", "Too many balls, they have been cleared!");
                    balls.clear();
                    db.wipeDatabase();
                }

        }
        // Save current x, y (no longer needed here since we're using UP and DOWN instead of MOVE)
        // previousX = currentX;
        // previousY = currentY;

        // Event handled
        return true;
    }

    // Could have simply replaced this method, but I made a new one instead, so this is commented out
    /*public void RussButtonPressed() {
        Log.d("BouncingBallView  BUTTON", "User tapped the  button ... VIEW");

        //get half of the width and height as we are working with a circle
        int viewWidth = this.getMeasuredWidth();
        int viewHeight = this.getMeasuredHeight();

        // make random x,y, velocity
        int x = rand.nextInt(viewWidth);
        int y = rand.nextInt(viewHeight);
        int dx = rand.nextInt(50);
        int dy = rand.nextInt(20);

        balls.add(new Ball(Color.RED, x, y, dx, dy));  // add ball at every touch event
    }*/

    // Adds a ball to this list of balls, see checkIfOutOfBounds() method for more info on the extra stuff
    public void addBall(Ball ball) {
        Log.d("BouncingBallView  BUTTON", "User tapped the addBall button ... VIEW");
        int viewWidth = this.getMeasuredWidth();
        int viewHeight = this.getMeasuredHeight();

        // Check if the values entered are out of bounds
        ball.setX(checkIfOutOfBounds(ball.getX(), viewWidth));
        ball.setY(checkIfOutOfBounds(ball.getY(), viewHeight));

        balls.add(ball);
    }

    // Method to check if the x and y of a ball are out of bounds, if so, simply returns a 0 for the axis, if not, returns the original value
    public float checkIfOutOfBounds(float position, int viewSize) {
        if (position < 0) {
            return 0;
        }
        if (position > viewSize) {
            return 0;
        }
        return position;
    }

    // Simply clears the list of balls
    public void clearBalls() {
        balls.clear();
    }
}
