package com.lcb.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter {
	private List<View> viewList;

	public ViewPagerAdapter(List<View> viewList) {
		this.viewList = viewList;
	}

	/**
	 * 获得当前页卡的数量
	 */
	public int getCount() {
		return viewList.size();
	}

	/**
	 * 判断视图是否由对象产生
	 */
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	/**
	 * 实例化页卡
	 */
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(viewList.get(position));
		return viewList.get(position);
	}

	/**
	 * 删除页卡
	 */
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(viewList.get(position));
	}
}
