package com.uniqueyi.poplib;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.PopupWindow;


/**
 * Created by
 * author:  cwy.
 * date:    2017/9/27.  16:11.
 * prj:     lib.
 */


public class RvPop {

    private Context context;

    private MClick mClick;

    public void setmClick(MClick mClick) {
        this.mClick = mClick;
    }

    public static RvPop getInstance(Context context) {
        return new RvPop(context);
    }

    public RvPop(Context context) {
        this.context = context;
    }

    private PopupWindow mPopupWindow;

    private View getPopupWindowContentView() {
        // 一个自定义的布局，作为显示的内容
        int layoutId = R.layout.view_pop_top_arrow;   // 布局ID
        View contentView = LayoutInflater.from(context).inflate(layoutId, null);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClick != null) {
                    mClick.delClick();
                }
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });
        return contentView;
    }


    private float mRawX;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public RvPop showPopupWindow(final View anchorView) {
        return showPopupWindow(anchorView, 0);
    }

    public RvPop showPopupWindow(final View anchorView, int rawX) {
        final View contentView = getPopupWindowContentView();
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mPopupWindow = new PopupWindow(contentView,
                contentView.getMeasuredWidth(), contentView.getMeasuredHeight(), false);


        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());

        // setOutsideTouchable设置生效的前提是setTouchable(true)和setFocusable(false)
        mPopupWindow.setOutsideTouchable(true);

        // 设置为true之后，PopupWindow内容区域 才可以响应点击事件
        mPopupWindow.setTouchable(true);

        // true时，点击返回键先消失 PopupWindow
        // 但是设置为true时setOutsideTouchable，setTouchable方法就失效了（点击外部不消失，内容区域也不响应事件）
        // false时PopupWindow不处理返回键
        mPopupWindow.setFocusable(false);
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;   // 这里面拦截不到返回键
            }
        });
        // 设置好参数之后再show
        final PopupWindowUtil.Bean windowPos = PopupWindowUtil.calculatePopWindowPos(anchorView, contentView);
        int xOff = 20; // 可以自己调整偏移
        windowPos.point[0] -= xOff;
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 自动调整箭头的位置
                autoAdjustArrowPos(contentView, anchorView, windowPos.direction);
                contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        Log.d("TAGG", "onTouch: " + mRawX);
        if (rawX == 0) {
            rawX = windowPos.point[0] / 2;
        }
        int y = windowPos.point[1];
        if (windowPos.direction) {
            y += 26;
        } else {
            y -= 26;
        }
        mPopupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, rawX, y);
        return this;
    }

    private void autoAdjustArrowPos(View contentView, View anchorView, boolean isNeed) {
        View upArrow = contentView.findViewById(R.id.up_arrow);
        View downArrow = contentView.findViewById(R.id.down_arrow);

        int pos[] = new int[2];
        contentView.getLocationOnScreen(pos);
        int popLeftPos = pos[0];
        anchorView.getLocationOnScreen(pos);
        int anchorLeftPos = pos[0];
        int arrowLeftMargin = anchorLeftPos - popLeftPos + anchorView.getWidth() / 2 - upArrow.getWidth() / 2;
        upArrow.setVisibility(isNeed ? View.INVISIBLE : View.VISIBLE);
        downArrow.setVisibility(isNeed ? View.VISIBLE : View.INVISIBLE);
//        RelativeLayout.LayoutParams upArrowParams = (RelativeLayout.LayoutParams) upArrow.getLayoutParams();
//        upArrowParams.leftMargin = arrowLeftMargin;
//        RelativeLayout.LayoutParams downArrowParams = (RelativeLayout.LayoutParams) downArrow.getLayoutParams();
//        downArrowParams.leftMargin = arrowLeftMargin;
    }


    public static abstract class MClick {
        public void delClick() {
        }

        public void addClick() {
        }
    }
}
