package com.brass1.whattodo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/*
 * This class is the adapter for the archived items view.
 * It inflates the views, and highlights items that are clicked.
 */

public class ArchivedAdapter extends ArrayAdapter<ToDoItem>{
	
	private Context context;
	private List<ToDoItem> items=new ArrayList<ToDoItem>();
	private int resId;
	
	public ArchivedAdapter(Context context,int layoutResourceId, List<ToDoItem> objects) {
		super(context,layoutResourceId,objects);
		this.resId=layoutResourceId;
		this.context=context;
		this.items=objects;
	}
	
	@Override
	public View getView(final int position,View convertView,ViewGroup parent) {
		ListView lv=(ListView)parent;
		if (convertView==null) {
			LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(resId, parent,false);
		}
		if (lv.isItemChecked(position)) {
			convertView.setBackgroundColor(0x4937dada);
		}
		else {
			convertView.setBackgroundColor(0x0);
		}
		TextView text=(TextView)convertView.findViewById(R.id.item);
		ToDoItem rowItem=items.get(position);
		text.setText(rowItem.getText());
		return convertView;
	}
}
