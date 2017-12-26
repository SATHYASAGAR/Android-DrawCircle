package com.example.sathy.drawcircle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathy on 21-Feb-17.
 */

public class SimpleDrawingView extends View implements View.OnTouchListener {

    static Float X = 0f, Y = 0f, radiusCurrent = 0f;
    static String circleColorCurrent = SelectModeActivity.circleColorCurrent;
    static Float deletePointXOnDown = 0f, deletePointYOnDown = 0f, deletePointXOnUp = 0f, deletePointYOnUp = 0f;
    static Float moveCirclePointXOnDown = 0f, moveCirclePointYOnDown = 0f;
    static Float XVelocity = 0f, YVelocity = 0f;
    static VelocityTracker velocity;

    private boolean swipeInProgress = false;

    static List<Circle> circles = new ArrayList<Circle>();
    static Integer canvasHeight = 0;
    static Integer canvasWidth = 0;
    Paint circleFrame;

    public SimpleDrawingView(Context context) {
        super(context);
    }

    public SimpleDrawingView(Context context, AttributeSet xmlAttributes) {
        super(context, xmlAttributes);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        switch (actionCode) {
            case MotionEvent.ACTION_DOWN:
                return handleActionDown(event);
            case MotionEvent.ACTION_UP:
                return handleActionUp(event);
            case MotionEvent.ACTION_MOVE:
                return handleActionMove(event);
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                swipeInProgress = false;
                return false;
        }
        return false;
    }

    private boolean handleActionDown(MotionEvent event) {
        swipeInProgress = true;

        if (SelectModeActivity.drawModeSwitch == 1) {
            X = event.getX();
            Y = event.getY();
            radiusCurrent = 0f;
            circleColorCurrent = SelectModeActivity.circleColorCurrent;
        }

        if (SelectModeActivity.deleteModeSwitch == 1) {
            deletePointXOnDown = event.getX();
            deletePointYOnDown = event.getY();
        }

        if (SelectModeActivity.moveModeSwitch == 1) {
            moveCirclePointXOnDown = event.getX();
            moveCirclePointYOnDown = event.getY();
            velocity = VelocityTracker.obtain();
            velocity.addMovement(event);
        }
        return true;
    }

    private boolean handleActionMove(MotionEvent event) {
        if (!swipeInProgress) return false;
        if (SelectModeActivity.drawModeSwitch == 1) {
            Float moveEndX = event.getX();
            Float moveEndY = event.getY();
            Float distance1, distance2, distance3, distance4; //Distances from left, top, right and down canvas
            distance1 = Y; distance2 = SimpleDrawingView.canvasWidth - X;
            distance3 = SimpleDrawingView.canvasHeight - Y; distance4 = X;
            Float moveRadius = (float) Math.sqrt((X - moveEndX) * (X - moveEndX) + (Y - moveEndY) * (Y - moveEndY));
            Float minDistance = Math.min(Math.min(distance1, distance2), Math.min(distance3, distance4));
            radiusCurrent = Math.min(moveRadius, minDistance);
        }

        if (SelectModeActivity.moveModeSwitch == 1) {
            velocity.addMovement(event);
        }
        return true;
    }

    private boolean handleActionUp(MotionEvent event) {
        if (!swipeInProgress) return false;
        if (SelectModeActivity.drawModeSwitch == 1) {
            Circle circleObject = new Circle(X, Y, radiusCurrent, circleColorCurrent);
            SimpleDrawingView.circles.add(circleObject);
            radiusCurrent = 0f;
        }

        if (SelectModeActivity.deleteModeSwitch == 1) {
            deletePointXOnUp = event.getX(); deletePointYOnUp = event.getY();
            for (int i = 0; i < SimpleDrawingView.circles.size(); i++) {
                Float centerPointDistanceOnDown = ((float) Math.sqrt(Math.pow((deletePointXOnDown - SimpleDrawingView.circles.get(i).getX()), 2) + Math.pow((deletePointYOnDown - SimpleDrawingView.circles.get(i).getY()), 2)));
                Float centerPointDistanceOnUp = ((float) Math.sqrt(Math.pow((deletePointXOnUp - SimpleDrawingView.circles.get(i).getX()), 2) + Math.pow((deletePointYOnUp - SimpleDrawingView.circles.get(i).getY()), 2)));
                if (centerPointDistanceOnDown < SimpleDrawingView.circles.get(i).getRadius() && centerPointDistanceOnUp < SimpleDrawingView.circles.get(i).getRadius()) {
                    SimpleDrawingView.circles.remove(SimpleDrawingView.circles.get(i));
                    i -= 1;
                }
            }
        }

        if (SelectModeActivity.moveModeSwitch == 1) {
            velocity.computeCurrentVelocity(1000);
            XVelocity = velocity.getXVelocity();
            YVelocity = velocity.getYVelocity();
            for (Circle eachCircle : SimpleDrawingView.circles) {
                double distanceX = moveCirclePointXOnDown - eachCircle.getX();
                double distanceY = moveCirclePointYOnDown - eachCircle.getY();
                Float centerToPointDistance = ((float) Math.sqrt(Math.pow(distanceX, 2) + Math.pow((distanceY), 2)));
                if (centerToPointDistance < eachCircle.getRadius()) {
                    eachCircle.setCanMoveFlag(1);
                    eachCircle.setXVelocity(XVelocity); eachCircle.setYVelocity(YVelocity);
                }
            }
        }
        return true;
    }

    protected void onDraw(Canvas canvas) {
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
        circleFrame = new Paint();
        circleFrame.setStyle(Paint.Style.STROKE);
        circleFrame.setStrokeWidth(6);
        for (int i = 0; i < circles.size(); i++) {
            setFrameCurrentCircleColor(circles.get(i).getColor());
            canvas.drawCircle(circles.get(i).getX(), circles.get(i).getY(), circles.get(i).getRadius(), circleFrame);
        }
        if (SelectModeActivity.moveModeSwitch == 1) {
            for (Circle eachCircle : SimpleDrawingView.circles) {
                if (eachCircle.getCanMoveFlag() == 1) {
                    Integer velocityLimitingValue = 100;
                    eachCircle.setX(eachCircle.getX() + (eachCircle.getXVelocity() / velocityLimitingValue));
                    eachCircle.setY(eachCircle.getY() + (eachCircle.getYVelocity() / velocityLimitingValue));
                    if (eachCircle.getRadius() + eachCircle.getX() > canvasWidth || eachCircle.getX() < eachCircle.getRadius()) {
                        eachCircle.setXVelocity(eachCircle.getXVelocity() * -1);
                    }
                    if (eachCircle.getRadius() + eachCircle.getY() > canvasHeight || eachCircle.getY() < eachCircle.getRadius()) {
                        eachCircle.setYVelocity(eachCircle.getYVelocity() * -1);
                    }
                }
            }
        }
        setFrameColor();
        canvas.drawCircle(X, Y, radiusCurrent, circleFrame);
        invalidate();
    }

    private void setFrameCurrentCircleColor(String currentCircleColor) {
        if (currentCircleColor.equalsIgnoreCase("Blue"))
            circleFrame.setColor(Color.BLUE);
        else if (currentCircleColor.equalsIgnoreCase("Red"))
            circleFrame.setColor(Color.RED);
        else if (currentCircleColor.equalsIgnoreCase("Green"))
            circleFrame.setColor(Color.GREEN);
        else circleFrame.setColor(Color.BLACK);
    }

    private void setFrameColor() {
        if (circleColorCurrent.equalsIgnoreCase("Blue"))
            circleFrame.setColor(Color.BLUE);
        else if (circleColorCurrent.equalsIgnoreCase("Red"))
            circleFrame.setColor(Color.RED);
        else if (circleColorCurrent.equalsIgnoreCase("Green"))
            circleFrame.setColor(Color.GREEN);
        else circleFrame.setColor(Color.BLACK);
    }
}