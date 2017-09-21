package cn.app.library.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.app.library.R;


/**
 * Created by Tiger on 2015-12-17.
 */
public class RefreshableRecyclerView extends ViewGroup {

    private int scrollDistance = 0;
    private boolean canLoadMore = true;
    private OnLoadMoreListener onLoadMoreListener;

    private OnScrollListener mOnScrollListener;

    private SwipeRefreshLayout swipeRefreshLayout;
    private BettterRecyclerView recyclerView;
    private LinearLayout loadMoreLayout;
    private ImageView topButton;
    private boolean showTopButton = false;
    private int topPadding = 0;

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            setScrollDistance(scrollDistance + dy);
            if (showTopButton) {
                if (scrollDistance >= 2 * recyclerView.getHeight()) {
                    if (topButton.getVisibility() != VISIBLE) {
                        topButton.setVisibility(VISIBLE);
                    }
                } else {
                    if (topButton.getVisibility() != GONE) {
                        topButton.setVisibility(GONE);
                    }
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (topPadding > 0) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    recyclerView.scrollBy(0, topPadding);
                }
            }
            if (canLoadMore) {
                if (!swipeRefreshLayout.isRefreshing()) {
                    if (!isLoadingMore()) {
                        //所有item总数
                        int totalCount = recyclerView.getLayoutManager().getItemCount();
                        //可见的item总数
                        int visibleCount = recyclerView.getLayoutManager().getChildCount();
                        //第一个可见item的位置
                        int firstVisiblePosition = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0));
                        if (firstVisiblePosition + visibleCount >= totalCount) {
                            if (onLoadMoreListener.getLoadState() == OnLoadMoreListener.STATE_WAITING) {
                                startLoadMore();
                            }
                        }
                    }
                }
            }
        }

    };

    public RefreshableRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public RefreshableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.widegt_refershable_recyclerview, null);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshable_swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(0, 140, 240), Color.rgb(0, 140, 240), Color.rgb(0, 140, 240));
        recyclerView = (BettterRecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addOnScrollListener(onScrollListener);
        loadMoreLayout = (LinearLayout) view.findViewById(R.id.refreshable_loading_more);
        topButton = (ImageView) view.findViewById(R.id.refreshable_top);
        topButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setScrollDistance(0);
                topButton.setVisibility(GONE);
                recyclerView.scrollToPosition(0);
            }
        });

        addView(view);
    }


    /**
     * Set the layout manager to the recycler
     *
     * @param manager
     */
    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        recyclerView.setLayoutManager(manager);
    }


    public void setOnTouchListener(OnTouchListener listener) {
        recyclerView.setOnTouchListener(listener);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        recyclerView.setItemAnimator(animator);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        recyclerView.addItemDecoration(itemDecoration);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        recyclerView.addItemDecoration(itemDecoration, index);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        recyclerView.removeItemDecoration(itemDecoration);
    }


    /**
     * Set the listener when refresh is triggered and enable the SwipeRefreshLayout
     *
     * @param listener
     */
    public void setRefreshListener(android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener listener) {
        swipeRefreshLayout.setOnRefreshListener(listener);
    }

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public boolean isLoadingMore() {
        if (onLoadMoreListener == null) {
            return true;
        }
        return onLoadMoreListener.getLoadState() == OnLoadMoreListener.STATE_LOADING;
    }

    private void startLoadMore() {
        if (onLoadMoreListener == null) {
            return;
        }
        swipeRefreshLayout.setEnabled(false);
        loadMoreLayout.setVisibility(View.VISIBLE);
        onLoadMoreListener.startLoadMore();
    }

    private void setScrollDistance(int scrollDistance) {
        this.scrollDistance = scrollDistance;
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(scrollDistance);
        }
    }

    public void complete() {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(true);
        loadMoreLayout.setVisibility(View.GONE);
        if (onLoadMoreListener != null) {
            onLoadMoreListener.finishLoadMore();
        }
    }

    public void setOnScrollListener(OnScrollListener mOnScrollListener) {
        this.mOnScrollListener = mOnScrollListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setTopPadding(int topPadding) {
        this.topPadding = topPadding;
    }

    public int getTopPadding() {
        return topPadding;
    }

    public void setShowTopButton(boolean showTopButton) {
        this.showTopButton = showTopButton;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public void setRefreshing(boolean isRefreshing) {
        swipeRefreshLayout.setRefreshing(isRefreshing);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public void clear() {
        setScrollDistance(0);
        topButton.setVisibility(GONE);
    }


    public static abstract class OnLoadMoreListener {

        public final static int STATE_WAITING = 0;
        public final static int STATE_LOADING = 1;

        private int loadState = STATE_WAITING;

        public abstract void onLoadMore();

        public void startLoadMore() {
            loadState = STATE_LOADING;
            onLoadMore();
        }

        public void finishLoadMore() {
            loadState = STATE_WAITING;
        }

        public int getLoadState() {
            return loadState;
        }
    }

    public interface OnScrollListener {

        void onScroll(int distance);

    }

}
