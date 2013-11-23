package com.jackie.step;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

	private String[] strs;
	private Context context;
	private LayoutInflater mLayoutInflater;

	public ListViewAdapter(String[] strs, Context context) {
		super();
		this.strs = strs;
		this.context = context;
		this.mLayoutInflater = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strs.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.listview_item, null);
		}
		TextView textView = (TextView) convertView.findViewById(R.id.textview);
		textView.setText(strs[position]);
		return convertView;
	}

}
