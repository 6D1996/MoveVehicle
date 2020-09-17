/**

 * *                #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG              #
 * #                                                   #
 */

package com.example.movevehicle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.RotateAnimation;

import java.util.ArrayList;
import java.util.List;


/**
 * Fixed on 2020/9/17,which is a happy day!
 */
public class ScalableImageView extends androidx.appcompat.widget.AppCompatImageView{


    private static final float MIN_POINT_DISTINCT = 10F;
    private Matrix matrix;
    private Matrix cacheMatrix;  //缓存的matrix ，同时记录上一次滑动的位置
    private float mPointDistinct = 1f;
    private float mDegree;
    private Bitmap bitmap;
    private float rotate = 0F;// 旋转的角度

    List<Float>angleList= new ArrayList<>();//For story the angle data
    int circle =0;
    float degree0=0;//按下角度
    float degree=0;//转动角度

    enum Mode {
        NONE, DOWN, MOVE
    }

    private Mode mode; //当前mode
    private Context mContext;

    private PointF mStart = new PointF();
    private PointF mEnd = new PointF();

    public ScalableImageView(Context context) {
        this(context, null);
    }

    public ScalableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScalableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        circle =0;
        degree=0;
        degree0=0;
        matrix = new Matrix();
        cacheMatrix = new Matrix();
        mode = Mode.NONE;
        bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.steering_wheel);  //避免OOM
        setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                cacheMatrix.set(matrix); //先拷贝一份到缓存
                double delta_x0 = (event.getX()- bitmap.getWidth()/2);
                double delta_y0 = (event.getY() - bitmap.getHeight()/ 2);
                double radius0 = Math.atan2(delta_y0,delta_x0);
                degree0= (float) Math.toDegrees(radius0);
                mode = Mode.DOWN;
                mStart.set(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                //单点触控的时候
                if (mode == Mode.DOWN) {
                    matrix.set(cacheMatrix);
                        double delta_x = (event.getX()- bitmap.getWidth()/2);
                        double delta_y = (event.getY() - bitmap.getHeight()/ 2);
                        double radius = Math.atan2(delta_y,delta_x);
                        degree= (float) Math.toDegrees(radius)-degree0;
                    angleList.add(degree);
                    if(angleList.size()>100){
                        for(int i=0;i<angleList.size()-100;i++){
                            angleList.remove(i);
                        }
                    }
                    if(angleList.size()>2) {
                        if (angleList.get(angleList.size() - 1) - angleList.get(angleList.size() - 2) > 350)
                            circle--;
                        if (angleList.get(angleList.size() - 1) - angleList.get(angleList.size() - 2) < -350)
                            circle++;
                        degree = degree + 360 * circle;
                        if(degree>900) degree= 900;
                        if(degree<-900)degree=-900;
                    }
                    Log.d("Degree",degree+"");

                    final RotateAnimation rotateAnimation = new RotateAnimation(0f, degree, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                    matrix.postRotate(degree,bitmap.getWidth()/2, bitmap.getHeight()/ 2);
                }
                break;
            case MotionEvent.ACTION_UP:
                init();
                Log.d("Degree",degree+"");
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                mode = Mode.NONE;
                break;

        }

        setImageMatrix(matrix);
        return true;
    }


}
