package com.brass1.whattodo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<ToDoItem> {
	
	Context context;
	List<ToDoItem> items=new ArrayList<ToDoItem>();
	int resId;
	
	public CustomAdapter(Context context,int layoutResourceId, List<ToDoItem> objects) {
		super(context,layoutResourceId,objects);
		this.resId=layoutResourceId;
		this.context=context;
		this.items=objects;
	}
	
	public int getint() {
		return 1;
	}
	
	@Override
	public View getView(final int position,View convertView,ViewGroup parent) {
		CheckBox check;
		ListView lv=(ListView)parent;
		if (convertView==null) {
			LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(resId, parent,false);
			check=(CheckBox)convertView.findViewById(R.id.check);
			convertView.setTag(check);
			final ListView list=(ListView)parent.findViewById(R.id.todo_list);
			check.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					CheckBox box=(CheckBox) v;
					ToDoItem item=(ToDoItem)box.getTag();
					item.setChecked(box.isChecked());
					list.performItemClick(null,0,-1);
				}
			});
		}
		else {
			check=(CheckBox)convertView.getTag();
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
		check.setChecked(rowItem.getChecked());
		check.setTag(rowItem);
		return convertView;
	}
	

}
