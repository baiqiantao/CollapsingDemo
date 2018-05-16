package com.bqt.test;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewTreeObserver;

public class SecondActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener, AppBarLayout.OnOffsetChangedListener {
	private AppBarLayout appBar;
	private Toolbar toolBar;//android.support.v7.widget.Toolbar
	
	private int pxWhenExpanded;
	private int maxVerticalOffset;
	
	private boolean isAutoScrolling = false;//是否正在自动滚动
	private int scrollTo = SCROLL_TO_BOTTOM;//滚动方向
	private static final int SCROLL_TO_BOTTOM = 1;//滑动到底部，最初的动作
	private static final int SCROLL_TO_TOP = 2;//滑动到顶部，回看的动作
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_activity);
		
		appBar = findViewById(R.id.app_bar);
		toolBar = findViewById(R.id.top_toolbar);
		appBar.getViewTreeObserver().addOnGlobalLayoutListener(this);
		appBar.addOnOffsetChangedListener(this);
		
		setSupportActionBar(toolBar);
		pxWhenExpanded = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
	}
	
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onGlobalLayout() {
		appBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
		maxVerticalOffset = appBar.getHeight() - toolBar.getHeight() - getStatusBarHeight();
		Log.i("bqt", "appBar=" + appBar.getHeight() + ", toolBar=" + toolBar.getHeight() + ", 状态栏=" + getStatusBarHeight());
		Log.i("bqt", "maxVerticalOffset=" + maxVerticalOffset);
	}
	
	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
		//verticalOffset是appBar【当前】高度与【最初】高度的差。最初值是0，向上滑到顶的话值是【-maxVerticalOffset】
		Log.i("bqt", "verticalOffset=" + verticalOffset + ", scrollTo=" + scrollTo + ", isAutoScrolling=" + isAutoScrolling);
		if (isAutoScrolling) {
			if (verticalOffset == 0) {
				Log.i("bqt", "【展开了】");
				isAutoScrolling = false;
				scrollTo = SCROLL_TO_BOTTOM;
			} else if (Math.abs(verticalOffset) == maxVerticalOffset) {
				Log.i("bqt", "【折叠了】");
				isAutoScrolling = false;
				scrollTo = SCROLL_TO_TOP;
			}
		} else {//这一块有bug，不知为什么会往返跳动
			if (scrollTo == SCROLL_TO_BOTTOM && Math.abs(verticalOffset) >= pxWhenExpanded) {
				Log.i("bqt", "【开始自动折叠】");
				appBar.setExpanded(false, true);
				isAutoScrolling = true;
			} else if (scrollTo == SCROLL_TO_TOP && Math.abs(verticalOffset) <= maxVerticalOffset - pxWhenExpanded) {
				Log.i("bqt", "【开始自动展开】");
				appBar.setExpanded(true, true);
				isAutoScrolling = true;
			}
		}
	}
	
	/**
	 * 状态栏高度，单位px，一般为25dp
	 */
	private int getStatusBarHeight() {
		int height = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			height = getResources().getDimensionPixelSize(resourceId);
		}
		return height;
	}
	
}