package com.lcb.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

/**
 * 也可以使用FragmentPagerAdapter。关于这两者之间的区别，可以自己去搜一下。
 * http://www.cnblogs.com/lianghui66/p/3607091.html
 */
public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

	List<Fragment> list;
	int a = 0;
	public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Fragment getItem(int arg0) { 
		a = arg0;
		return list.get(arg0);
	}

	/**
	 * 每次更新完成ViewPager的内容后，调用该接口，此处复写主要是为了让导航按钮上层的覆盖层能够动态的移动
	 */
	@Override
	public void finishUpdate(ViewGroup container) {
		super.finishUpdate(container);// 这句话要放在最前面，否则会报错
		// Toast.makeText(, "这是页数     "+a, Toast.LENGTH_SHORT);
	}

}
