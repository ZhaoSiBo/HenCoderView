package com.starts.hencoderview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;

public class EllipsizedTextView extends androidx.appcompat.widget.AppCompatTextView {
    private static final int MAX_ELLIPSIZE_LINES = 100;

    private int mMaxLines;

    public EllipsizedTextView(Context context) {
        this(context, null, 0);
    }

    public EllipsizedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EllipsizedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Attribute initialization
        final TypedArray a = context.obtainStyledAttributes(attrs, new int[]{
                android.R.attr.maxLines
        }, defStyle, 0);

        mMaxLines = a.getInteger(0, 1);
        a.recycle();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        CharSequence newText = getWidth() == 0 || mMaxLines > MAX_ELLIPSIZE_LINES ? text :
                TextUtils.ellipsize(text, getPaint(), getWidth() * mMaxLines,
                        TextUtils.TruncateAt.END, false, null);
        super.setText(newText, type);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        if (width > 0 && oldWidth != width) {
            setText(getText());
        }
    }

    @Override
    public void setMaxLines(int maxlines) {
        super.setMaxLines(maxlines);
        mMaxLines = maxlines;
    }
}