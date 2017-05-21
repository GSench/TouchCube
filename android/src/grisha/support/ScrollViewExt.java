package grisha.support;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ScrollViewExt extends ScrollView {

    private ScrollViewListener scrollViewListener = null;
    private int extentVertical, extentHorizontal;

    public ScrollViewExt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public ScrollViewExt(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public ScrollViewExt(Context context) {
        super(context);

    }

    // -------------------------------------------------------------------------------
    /*
     * ??????? ?????????
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);

        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /*
     * ?????????? ???????????? ????? ?????????? ?????????
     */
    @Override
    protected int computeVerticalScrollExtent() {
        extentVertical = super.computeVerticalScrollExtent();
        return extentVertical;
    }


    // -------------------------------------------------------------------------------
    public int getExtentVertical() {
        return extentVertical;
    }

    public int getExtentHorizontal() {
        return extentHorizontal;
    }

    // -------------------------------------------------------------------------------
    /**
     * @return ?????? ????????
     */
    final public int getChildWidth() {
        return getChildAt(0).getWidth();
    }

    /**
     * @return ?????? ????????
     */
    final public int getChildHeight() {
        return getChildAt(0).getHeight();
    }

    // -------------------------------------------------------------------------------
    /**
     * ?????????? ?????????? ?????????
     *
     * @param svl
     */
    public void setOnScrollChanged(ScrollViewListener svl) {
        scrollViewListener = svl;
    }

    // -------------------------------------------------------------------------------
    /**
     * ????????? ??????????? ?????????
     *
     */
    public interface ScrollViewListener {

        /**
         * @param scrollView
         *            - ???? ?????????
         * @param l
         * @param t
         *            - y ?????????? ?????????? ?????????
         * @param oldl
         * @param oldt
         *            - ?????? y ?????????? ?????????? ?????????
         */
        void onScrollChanged(ScrollViewExt scrollView, int l, int t, int oldl,
                             int oldt);
    }

}