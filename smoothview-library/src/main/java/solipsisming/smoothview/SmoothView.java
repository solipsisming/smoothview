package solipsisming.smoothview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 类似于textview显示文本控件</p>
 * <p>
 * 创建于 2018-4-17 09:47:57
 *
 * @author 洪东明
 */
public class SmoothView extends View implements View.OnLongClickListener {

    private StaticLayout mLayout;//绘制布局

    private static final Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;//默认对齐方式

    private float mLineSpacingExtra;//文本的行边距

    private final TextPaint mTextPaint;//文本画笔

    private CharSequence mText;//文本

    private int mCurTextColor;//画笔颜色

    private static final Canvas dumpCache = new Canvas();//缓冲区

    private int mWidth;//布局的宽度
    private int mHeight;//布局的高度
    private int mTextSize;

    //    private CopyPopupWindow copyPopupWindow;
    static {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.measureText("H");
    }

    public SmoothView(Context context) {
        this(context, null);
    }

    public SmoothView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mText = "";

        Resources res = getResources();

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.density = res.getDisplayMetrics().density;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SmoothView);
        int count = attrs.getAttributeCount();

        ColorStateList textColor = null;
        int textSize = 28;
        CharSequence text = "";

        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SmoothView_text) {
                text = a.getText(attr);
            } else if (attr == R.styleable.SmoothView_textColor) {
                textColor = a.getColorStateList(attr);
            } else if (attr == R.styleable.SmoothView_textSize) {
                textSize = a.getDimensionPixelSize(attr, textSize);
            } else if (attr == R.styleable.SmoothView_lineSpacingExtra) {
                mLineSpacingExtra = a.getDimensionPixelSize(attr, (int) mLineSpacingExtra);
            }
        }

        setTextColor(textColor != null ? textColor : ColorStateList.valueOf(0xFF000000));
        setRawTextSize(textSize);
        setText(text);
        a.recycle();
        this.setOnLongClickListener(this);
//        copyPopupWindow = new CopyPopupWindow((Activity) context);
    }

    @Override
    public boolean onLongClick(View v) {
        // TODO Auto-generated method stub
//        copyPopupWindow.showPopupWindow(this);

        return false;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public CharSequence getText() {
        return mText;
    }

    /**
     * 设置字体大小
     *
     * @param size 字体大小
     */
    public void setTextSize(float size) {

        if (mTextSize == size)
            return;

        Context c = getContext();
        Resources r;

        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();

        setRawTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, r.getDisplayMetrics()));
    }

    private void setRawTextSize(float size) {
        if (size != mTextPaint.getTextSize()) {
            mTextPaint.setTextSize(size);

            if (mLayout != null) {
                requestLayout();
                invalidate();
            }
        }
    }

    /**
     * 设置画笔颜色
     *
     * @param colors 颜色值
     */
    public void setTextColor(ColorStateList colors) {
        if (colors == null) {
            throw new NullPointerException();
        }

        mCurTextColor = colors.getColorForState(getDrawableState(), 0);
        mTextPaint.setColor(mCurTextColor);
        invalidate();
    }

    /**
     * 设置画笔颜色
     *
     * @param color 颜色值
     */
    public void setTextColor(int color) {
        if (color != mCurTextColor) {
            mCurTextColor = color;
            mTextPaint.setColor(mCurTextColor);
            invalidate();
        }
    }

    /**
     * 设置文本
     *
     * @param text 文本
     */
    public void setText(CharSequence text) {
        if (!mText.equals(text)) {

            if (text == null) {
                text = "";
            }

            mText = text;
            if (mLayout == null)
                makeNewLayout();

            requestLayout();
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        makeNewLayout();
        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * 创建布局
     */
    private void makeNewLayout() {
        mLayout = new StaticLayout(mText, mTextPaint, mWidth, alignment, 1.0f,
                mLineSpacingExtra, true);
        int linecount = mLayout.getLineCount();
        mHeight = mLayout.getLineTop(linecount);
        mLayout.draw(dumpCache);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLayout != null && !TextUtils.isEmpty(mText)) {//绘制文本
            mLayout.draw(canvas, null, null, 0);
        }
    }
}