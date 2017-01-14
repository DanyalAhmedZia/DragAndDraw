package com.apps.danyal.draganddraw;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed Murtaza on 1/9/2017.
 */

public class BoxDrawingView extends View  {
    private Box mCurrentBox;
    private List<Box> mBoxes = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;
    private int mWidth;

    private static final String TAG = "BoxDrawingView";
    private static final String BOX_KEY = "box";
    private static final String BOXES_KEY = "boxes";
    public BoxDrawingView(Context context){
        this(context,null);
    }
    public BoxDrawingView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        // Paint the boxes a nice semitransparent red (ARGB)
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);
        // Paint the background off-white
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(),event.getY());
        String action = "";

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                mCurrentBox = new Box(current);
                mBoxes.add(mCurrentBox);
            break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null){
                    mCurrentBox.setCurrent(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break;
        }
        Log.i(TAG,action + " at x= " + current.x + " , y= " + current.y);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth ;
        int parentHeight;
        if (!isLandscape()){
            parentWidth = MeasureSpec.getSize(widthMeasureSpec);
            parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        }
        else {
            parentHeight = MeasureSpec.getSize(widthMeasureSpec);
            parentWidth = MeasureSpec.getSize(heightMeasureSpec);
        }


        this.setMeasuredDimension(parentWidth, parentHeight);
    }

    private Boolean isLandscape(){
        int oreintation = getResources().getConfiguration().orientation;
        return (oreintation == Configuration.ORIENTATION_LANDSCAPE) ;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);

        for (Box box: mBoxes) {
            float left  = Math.min(box.getOrigin().x,box.getCurrent().x);
            float right = Math.max(box.getOrigin().x,box.getCurrent().x);
            float top = Math.min(box.getOrigin().y,box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y,box.getCurrent().y);
            canvas.drawRect(left,top,right,bottom,mBoxPaint);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable p = super.onSaveInstanceState();
        SavedState ss = new SavedState(p);
        for (int i = 0 ; i<mBoxes.size() ; i++){
            ss.bundle.putSerializable(BOX_KEY + i,mBoxes.get(i));
        }
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());
        for (int i = 0 ; i < ss.bundle.size() ; i++ ){
            Box box = (Box) ss.bundle.getSerializable(BOX_KEY + i);
            mBoxes.add(box);
        }
    }

    static class SavedState extends BaseSavedState {
        public  Bundle bundle;

        SavedState(Parcelable superState) {
            super(superState);
            bundle = new Bundle();
        }

        private SavedState(Parcel in) {
            super(in);
            this.bundle = in.readBundle();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBundle(this.bundle);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
