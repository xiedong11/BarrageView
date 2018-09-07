package com.zhuandian.barrageexample;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BarrageView extends RelativeLayout {
    private Context mContext;
    private BarrageHandler mHandler = new BarrageHandler();
    private Random random = new Random(System.currentTimeMillis());
    private static final long BARRAGE_GAP_MIN_DURATION = 1000;//两个弹幕的最小间隔时间
    private static final long BARRAGE_GAP_MAX_DURATION = 2000;//两个弹幕的最大间隔时间
    private int maxBarrageSpeed = 10000;//速度，ms
    private int minBarrageSpeed = 5000;//速度，ms
    private int maxBarrageTextSize = 30;//文字大小，dp
    private int minBarrageTextSize = 15;//文字大小，dp
    private int totalHeight = 0;
    private int lineHeight = 0;//每一行弹幕的高度
    private int totalLine = 0;//弹幕的行数
    private int barrageCount;
    private List<String> barrageItemList = new ArrayList<String>();
    private IBarrageItemClickListener itemClickListener;


    public BarrageView(Context context) {
        this(context, null);
    }

    public BarrageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarrageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    /**
     * 开启弹幕
     */
    public void startBarrageView() {
        barrageCount = barrageItemList.size();
        int duration = (int) ((BARRAGE_GAP_MAX_DURATION - BARRAGE_GAP_MIN_DURATION) * Math.random());
        mHandler.sendEmptyMessageDelayed(0, duration);
    }

    /**
     * 设置弹幕内容
     *
     * @param barrageItemList
     */
    public void setBarrageItemList(List<String> barrageItemList) {
        this.barrageItemList = barrageItemList;
    }

    public void setOnBarrageItemClickListener(IBarrageItemClickListener barrageItemClickListener) {
        this.itemClickListener = barrageItemClickListener;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        totalHeight = getMeasuredHeight();
        lineHeight = getLineHeight();
        totalLine = totalHeight / lineHeight;
    }

    private void generateItem() {
        BarrageItem barrageItem = new BarrageItem();
        final String barrageContent = barrageItemList.get((int) (Math.random() * barrageCount));
        int barrageTextSize = (int) (minBarrageTextSize + (maxBarrageTextSize - minBarrageTextSize) * Math.random());
        barrageItem.textView = new TextView(mContext);
        barrageItem.textView.setText(barrageContent);
        barrageItem.textView.setTextSize(barrageTextSize);
        barrageItem.textView.setTextColor(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        barrageItem.textMeasuredWidth = (int) getTextWidth(barrageItem, barrageContent, barrageTextSize);
        barrageItem.moveSpeed = (int) (minBarrageSpeed + (maxBarrageSpeed - minBarrageSpeed) * Math.random());
        if (totalLine == 0) {
            totalHeight = getMeasuredHeight();
            lineHeight = getLineHeight();
            totalLine = totalHeight / lineHeight;
        }
        barrageItem.verticalPos = random.nextInt(totalLine) * lineHeight;
        showBarrageItem(barrageItem);
        barrageItem.textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener!=null){
                    itemClickListener.onItemClick(barrageContent);
                }
            }
        });
    }

    private void showBarrageItem(final BarrageItem item) {
        int leftMargin = this.getRight() - this.getLeft() - this.getPaddingLeft();
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.topMargin = item.verticalPos;
        this.addView(item.textView, params);
        Animation anim = generateTranslateAnim(item, leftMargin);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                item.textView.clearAnimation();
                BarrageView.this.removeView(item.textView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        item.textView.startAnimation(anim);
    }

    private TranslateAnimation generateTranslateAnim(BarrageItem item, int leftMargin) {
        TranslateAnimation anim = new TranslateAnimation(leftMargin, -item.textMeasuredWidth, 0, 0);
        anim.setDuration(item.moveSpeed);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setFillAfter(true);
        return anim;
    }

    /**
     * 计算TextView中字符串的长度
     *
     * @param text 要计算的字符串
     * @param Size 字体大小
     * @return TextView中字符串的长度
     */
    public float getTextWidth(BarrageItem item, String text, float Size) {
        Rect bounds = new Rect();
        TextPaint paint;
        paint = item.textView.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    /**
     * 获得每一行弹幕的最大高度
     *
     * @return
     */
    private int getLineHeight() {
        BarrageItem item = new BarrageItem();
        String tx = barrageItemList.get(0);
        item.textView = new TextView(mContext);
        item.textView.setText(tx);
        item.textView.setTextSize(maxBarrageTextSize);

        Rect bounds = new Rect();
        TextPaint paint;
        paint = item.textView.getPaint();
        paint.getTextBounds(tx, 0, tx.length(), bounds);
        return bounds.height();
    }

    class BarrageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            generateItem();
            //每个弹幕产生的间隔时间随机
            int duration = (int) ((BARRAGE_GAP_MAX_DURATION - BARRAGE_GAP_MIN_DURATION) * Math.random());
            this.sendEmptyMessageDelayed(0, duration);
        }
    }

}