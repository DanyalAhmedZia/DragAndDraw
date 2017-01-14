package com.apps.danyal.draganddraw;

import android.graphics.PointF;

import java.io.Serializable;

/**
 * Created by Ahmed Murtaza on 1/11/2017.
 */

public class Box implements Serializable{
    private PointF mCurrent;
    private PointF mOrigin;

    public Box(PointF origin){
        mOrigin = origin;
        mCurrent = origin;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public PointF getOrigin() {
        return mOrigin;
    }
}
