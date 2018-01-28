package com.example.developergu.refreshmaster.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import com.example.developergu.refreshmaster.R;


/** Created by developgergu on 2017/12/18. */
public class SquareButton extends AppCompatImageButton {
  // 可配置size：按高度、按宽度
  private static final int DEPEND_WIDTH = 0;
  private static final int DEPEND_HEIGHT = 1;
  private int mDepend = DEPEND_WIDTH; // default

  public SquareButton(Context context) {
    super(context);
    init(context, null);
  }

  public SquareButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public SquareButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  private void init(Context context, AttributeSet attrs) {
    if (attrs != null) {
      TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SquareButton);
      if (ta != null) {
        mDepend = ta.getInt(R.styleable.SquareButton_dependSide, mDepend);
        ta.recycle();
      }
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int size =
        mDepend == DEPEND_WIDTH
            ? MeasureSpec.getSize(widthMeasureSpec)
            : MeasureSpec.getSize(heightMeasureSpec);
    setMeasuredDimension(size, size);
  }
}
