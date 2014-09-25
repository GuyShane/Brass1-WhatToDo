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
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ToDoFragment extends Fragment {
	
	private ArrayList<ToDoItem>items;
	private FileHandler fh;
	private String toDoFilename="todo.txt";
	private String archivedFilename="archived.txt";
	private TextView toDoCount;
	private TextView countSummary;
	private View fragView;
	private int numChecked;
	private int numUnchecked;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		fragView=inflater.inflate(R.layout.fragment_todo, container,false);
		
		fh=new FileHandler(getActivity().getApplicationContext());
		items=fh.getItems(toDoFilename);
		
		final ListView toDoList=(ListView)fragView.findViewById(R.id.todo_list);
		toDoCount=(TextView)fragView.findViewById(R.id.todo_count);
		countSummary=(TextView)fragView.findViewById(R.id.count_summary);
		
		Button selectedDelete=(Button)fragView.findViewById(R.id.selected_todo_delete);
		Button selectedArchive=(Button)fragView.findViewById(R.id.selected_todo_archive);
		Button selectedEmail=(Button)fragView.findViewById(R.id.selected_todo_email);
		
		final ToDoAdapter adapter=new ToDoAdapter(getActivity(), R.layout.item_layout_todo, items);
		
		toDoList.setAdapter(adapter);
		toDoList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		selectedDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i=items.size()-1;i>=0;i--) {
					if(toDoList.isItemChecked(i)) {
						delete(i,adapter);
					}
				}
				clearSelected(toDoList);
			}
		});
		
		selectedArchive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i=items.size()-1;i>=0;i--) {
					if (toDoList.isItemChecked(i)) {
						archive(i,adapter);
					}
				}
				clearSelected(toDoList);
			}
		});
		
		selectedEmail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(toDoList.getCheckedItemCount()!=0) {
					Intent emailIntent=new Intent(Intent.ACTION_SEND);
					emailIntent.setData(Uri.parse("mailto:"));
					emailIntent.setType("text/plain");
					emailIntent.putExtra(Intent.EXTRA_SUBJECT, "To do item");
					StringBuilder sb=new StringBuilder();
					for (int i=items.size()-1;i>=0;i--) {
						if (toDoList.isItemChecked(i)) {
							sb.append(items.get(i).getText()+"\n");
						}
					}
					String list=sb.toString();
					emailIntent.putExtra(Intent.EXTRA_TEXT, list);
					try {
						startActivity(Intent.createChooser(emailIntent, "Send email"));
					}
					catch (android.content.ActivityNotFoundException anfe) {
						Toast.makeText(getActivity(), "No email service found", Toast.LENGTH_SHORT).show();
					}
				}
				clearSelected(toDoList);
			}
		});
		
		toDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (id==-1) {
					numChecked=getNumChecked(items);
					numUnchecked=getNumUnchecked(items);
					countSummary.setText("Items checked: "+numChecked+", Items unchecked: "+numUnchecked);
				}
				else {
					if (toDoList.isItemChecked(position)) {
						toDoList.setItemChecked(position, true);
					}
					else {
						toDoList.setItemChecked(position, false);
					}
				}
			}
		});
		
		toDoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(final AdapterView<?> parent, View view,
					final int position, long id) {
				clearSelected(toDoList);
				final LayoutInflater inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View popupView=inflater.inflate(R.layout.todo_popup, parent);
				final PopupWindow popup=new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				Button popupDelete=(Button)popupView.findViewById(R.id.button_todo_popup_delete);
				Button popupArchive=(Button)popupView.findViewById(R.id.button_todo_popup_archive);
				Button popupEmail=(Button)popupView.findViewById(R.id.button_todo_popup_email);
				Button popupCancel=(Button)popupView.findViewById(R.id.button_todo_popup_cancel);
				popupDelete.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						View deleteView=inflater.inflate(R.layout.popup_delete, parent);
						popup.dismiss();
						popup.setContentView(deleteView);
						Button deleteDelete=(Button)deleteView.findViewById(R.id.button_popup_delete_delete);
						Button deleteCancel=(Button)deleteView.findViewById(R.id.button_popup_delete_cancel);
						TextView deleteText=(TextView)deleteView.findViewById(R.id.text_popup_delete);
						deleteText.setText("Really delete item\n\""+items.get(position).getText()+"\"?");
						deleteDelete.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								delete(position,adapter);
								Toast.makeText(getActivity(), "Item deleted", Toast.LENGTH_SHORT).show();
								popup.dismiss();
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
				popupArchive.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						archive(position,adapter);
						Toast.makeText(getActivity(), "Item archived", Toast.LENGTH_SHORT).show();
						popup.dismiss();
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
				return true;
			}
		});
		
		toDoCount.setText("You have "+items.size()+" things to do");
		numChecked=getNumChecked(items);
		numUnchecked=getNumUnchecked(items);
		countSummary.setText("Items checked: "+numChecked+", Items unchecked: "+numUnchecked);
		
		return fragView;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		fh.saveItems(items, toDoFilename);
	}
	
	private void deleteItem(ArrayList<ToDoItem> items, int position, ToDoAdapter adapter) {
		items.remove(position);
		adapter.notifyDataSetChanged();
	}
	
	private int getNumChecked(ArrayList<ToDoItem> items) {
		int checked=0;
		for (ToDoItem item:items) {
			if (item.getChecked()) {
				checked+=1;
			}
		}
		return checked;
	}
	
	private int getNumUnchecked(ArrayList<ToDoItem> items){
		return items.size()-getNumChecked(items);
	}
	
	private void clearSelected(ListView l) {
		for (int i=0;i<items.size();i++) {
			l.setItemChecked(i, false);
		}
	}
	
	private void delete(int position, ToDoAdapter adapter) {
		deleteItem(items, position, adapter);
		toDoCount.setText("You have "+items.size()+" things to do");
		numChecked=getNumChecked(items);
		numUnchecked=getNumUnchecked(items);
		countSummary.setText("Items checked: "+numChecked+", Items unchecked: "+numUnchecked);
		fh.saveItems(items, toDoFilename);
	}
	
	private void archive(int position, ToDoAdapter adapter) {
		fh.saveItem(items.get(position), archivedFilename);
		deleteItem(items, position, adapter);
		toDoCount.setText("You have "+items.size()+" things to do");
		numChecked=getNumChecked(items);
		numUnchecked=getNumUnchecked(items);
		countSummary.setText("Items checked: "+numChecked+", Items unchecked: "+numUnchecked);
		fh.saveItems(items, toDoFilename);
	}

}
