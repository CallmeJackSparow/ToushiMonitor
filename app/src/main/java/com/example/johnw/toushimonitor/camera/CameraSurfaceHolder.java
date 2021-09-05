package com.example.johnw.toushimonitor.camera;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.johnw.toushimonitor.MainActivity;
import com.example.johnw.toushimonitor.camera.SurfaceViewCallback;

/**
 * 相机界面SurfaceView的Holder类
 */
public class CameraSurfaceHolder {
    Context mContext;
    SurfaceHolder surfaceHolder;
    SurfaceView mSurfaceView;
    SurfaceViewCallback callback = new SurfaceViewCallback();

    /**
    * 设置相机界面SurfaceView的Holder
     * @param context 从相机所在的Activity传入的context
     * @param surfaceView Holder所绑定的响应的SurfaceView
    * */
    public void setCameraSurfaceHolder(Context context, SurfaceView surfaceView) {
        this.mContext = context;
        this.mSurfaceView = surfaceView;
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(callback);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        callback.setContext(context);
    }

    //public

}


