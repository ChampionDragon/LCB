package com.lcb.Ezviz.PullToRefresh;

import android.view.View;

/**
 * Created by Administrator on 2017/6/22.
 */

public interface EmptyViewMethodAccessor {

    /**
     * Calls upto AdapterView.setEmptyView()
     *
     * @param emptyView - to set as Empty View
     */
    public void setEmptyViewInternal(View emptyView);

    /**
     * Should call PullToRefreshBase.setEmptyView() which will then
     * automatically call through to setEmptyViewInternal()
     *
     * @param emptyView - to set as Empty View
     */
    public void setEmptyView(View emptyView);
}
