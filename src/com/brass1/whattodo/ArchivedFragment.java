package com.brass1.whattodo;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ArchivedFragment extends Fragment {
	
	private ArrayList<ToDoItem>items;
	private FileHandler fh;
	private String toDoFilename="todo.txt";
	private String archivedFilename="archived.txt";
	private TextView archivedCount;
	private View fragView;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		fragView=inflater.inflate(R.layout.fragment_archived, container,false);
		
		fh=new FileHandler(getActivity().getApplicationContext());
		items=fh.getItems(archivedFilename);
		
		final ListView archivedList=(ListView)fragView.findViewById(R.id.archived_list);
		archivedCount=(TextView)fragView.findViewById(R.id.archived_count);
		
		//final CustomAdapter adapter=new CustomAdapter(getActivity(), R.layout.item_layout_todo, items);
		final ArrayAdapter<ToDoItem> adapter=new ArrayAdapter<ToDoItem>(getActivity(),R.layout.item_layout_archived,items);
		//final ArrayAdapter<ToDoItem> ad=new ArrayAdapter<ToDoItem>(getActivity(),android.R.layout.simple_list_item_1,items);
		archivedList.setAdapter(adapter);
		
		archivedList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				final LayoutInflater inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View popupView=inflater.inflate(R.layout.archived_popup, null);
				final PopupWindow popup=new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				Button popupDelete=(Button)popupView.findViewById(R.id.button_archived_popup_delete);
				Button popupUnarchive=(Button)popupView.findViewById(R.id.button_archived_popup_unarchive);
				Button popupEmail=(Button)popupView.findViewById(R.id.button_archived_popup_email);
				Button popupCancel=(Button)popupView.findViewById(R.id.button_archived_popup_cancel);
				popupDelete.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						View deleteView=inflater.inflate(R.layout.popup_delete, null);
						popup.dismiss();
						popup.setContentView(deleteView);
						Button deleteDelete=(Button)deleteView.findViewById(R.id.button_popup_delete_delete);
						Button deleteCancel=(Button)deleteView.findViewById(R.id.button_popup_delete_cancel);
						TextView deleteText=(TextView)deleteView.findViewById(R.id.text_popup_delete);
						deleteText.setText("Really delete item\n\""+items.get(position).getText()+"\"?");
						deleteDelete.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								deleteItem(items, position, adapter);
								Toast.makeText(getActivity(), "Item deleted", Toast.LENGTH_SHORT).show();
								archivedCount.setText("You have "+items.size()+" things archived");
								popup.dismiss();
								fh.saveItems(items, archivedFilename);
							}
						});
						deleteCancel.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								popup.dismiss();
							}
						});
						popup.showAtLocation(fragView, Gravity.CENTER, 0, 0);
					}
				});
				popupUnarchive.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						fh.saveItem(items.get(position), toDoFilename);
						deleteItem(items, position, adapter);
						archivedCount.setText("You have "+items.size()+" things archived");
						Toast.makeText(getActivity(), "Item unarchived", Toast.LENGTH_SHORT).show();
						popup.dismiss();
						fh.saveItems(items, archivedFilename);
					}
				});
				popupEmail.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent emailIntent=new Intent(Intent.ACTION_SEND);
						emailIntent.setData(Uri.parse("mailto:"));
						emailIntent.setType("text/plain");
						emailIntent.putExtra(Intent.EXTRA_SUBJECT, "To do item");
						emailIntent.putExtra(Intent.EXTRA_TEXT, items.get(position).getText());
						try {
							startActivity(Intent.createChooser(emailIntent, "Send email"));
						}
						catch (android.content.ActivityNotFoundException anfe) {
							Toast.makeText(getActivity(), "No email service found", Toast.LENGTH_SHORT).show();
						}
						popup.dismiss();
					}
				});
				popupCancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						popup.dismiss();
					}
				});
				popup.showAtLocation(fragView, Gravity.CENTER, 0, 0);
				return false;
			}
		});
		
		archivedCount.setText("You have "+items.size()+" things archived");
		
		return fragView;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		fh.saveItems(items, archivedFilename);
	}
	
	public void deleteItem(ArrayList<ToDoItem> items, int position, ArrayAdapter<ToDoItem> adapter) {
		items.remove(position);
		adapter.notifyDataSetChanged();
	}

}