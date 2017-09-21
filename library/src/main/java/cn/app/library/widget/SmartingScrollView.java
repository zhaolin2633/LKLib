package cn.app.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * @description： 带滚动监听的scrollview
 */
public class SmartingScrollView extends ScrollView {

	public interface OnScrollChangedListener {
		void onScrollChanged(ScrollView who, int x, int y, int oldx, int oldy);
	}

	private OnScrollChangedListener mOnScrollChangedListener;

	public SmartingScrollView(Context context) {
		super(context);
	}

	public SmartingScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SmartingScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (mOnScrollChangedListener != null) {
			mOnScrollChangedListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

	public void setOnScrollChangedListener(OnScrollChangedListener listener) {
		mOnScrollChangedListener = listener;
	}

}