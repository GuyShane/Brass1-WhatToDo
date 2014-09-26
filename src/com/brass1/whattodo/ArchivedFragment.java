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

/*
 * This class displays the archived items to the user.
 * It handles the click events of the items.
 */

public class ArchivedFragment extends Fragment {
	
	private ArrayList<ToDoItem>items;
	private FileHandler fh;
	private String toDoFilename="todo.txt";
	private String archivedFilename="archived.txt";
	private TextView archivedCount;
	private View fragView;
	
	
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		fragView=inflater.inflate(R.layout.fragment_archived, container,false);
		
		fh=new FileHandler(getActivity().getApplicationContext());
		items=fh.getItems(archivedFilename);
		
		final ListView archivedList=(ListView)fragView.findViewById(R.id.archived_list);
		archivedCount=(TextView)fragView.findViewById(R.id.archived_count);
		
		Button selectedDelete=(Button)fragView.findViewById(R.id.selected_archived_delete);
		Button selectedUnarchive=(Button)fragView.findViewById(R.id.selected_archived_unarchive);
		Button selectedEmail=(Button)fragView.findViewById(R.id.selected_archived_email);
		
		final ArchivedAdapter adapter=new ArchivedAdapter(getActivity(),R.layout.item_layout_archived,items);
		archivedList.setAdapter(adapter);
		archivedList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		selectedDelete.setOnClickListener(new View.OnClickListener() {
			//LayoutInflater inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View deleteView=inflater.inflate(R.layout.popup_delete, null);
			PopupWindow popup=new PopupWindow(deleteView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			@Override
			public void onClick(View v) {
				if (archivedList.getCheckedItemCount()!=0) {
					Button deleteDelete=(Button)deleteView.findViewById(R.id.button_popup_delete_delete);
					Button deleteCancel=(Button)deleteView.findViewById(R.id.button_popup_delete_cancel);
					TextView deleteText=(TextView)deleteView.findViewById(R.id.text_popup_delete);
					deleteText.setText("Really delete selected?");
					deleteDelete.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							for (int i=items.size()-1;i>=0;i--) {
								if(archivedList.isItemChecked(i)) {
									delete(i,adapter);
								}
							}
							Toast.makeText(getActivity(), "Selected deleted", Toast.LENGTH_SHORT).show();
							popup.dismiss();
							clearSelected(archivedList);
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
			}
		});
		
		selectedUnarchive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (archivedList.getCheckedItemCount()!=0) {
					for (int i=items.size()-1;i>=0;i--) {
						if (archivedList.isItemChecked(i)) {
							unarchive(i,adapter);
						}
					}
					Toast.makeText(getActivity(), "Selected unarchived", Toast.LENGTH_SHORT).show();
					clearSelected(archivedList);
				}
			}
		});
		
		selectedEmail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(archivedList.getCheckedItemCount()!=0) {
					Intent emailIntent=new Intent(Intent.ACTION_SEND);
					emailIntent.setData(Uri.parse("mailto:"));
					emailIntent.setType("text/plain");
					emailIntent.putExtra(Intent.EXTRA_SUBJECT, "To do item");
					StringBuilder sb=new StringBuilder();
					for (int i=items.size()-1;i>=0;i--) {
						if (archivedList.isItemChecked(i)) {
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
				clearSelected(archivedList);
			}
		});
		
		archivedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (archivedList.isItemChecked(position)) {
					archivedList.setItemChecked(position, true);
				}
				else {
					archivedList.setItemChecked(position, false);
				}
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
	
	private void deleteItem(ArrayList<ToDoItem> items, int position, ArchivedAdapter adapter) {
		items.remove(position);
		adapter.notifyDataSetChanged();
	}
	
	private void clearSelected(ListView l) {
		for (int i=0;i<items.size();i++) {
			l.setItemChecked(i, false);
		}
	}
	
	private void delete(int position, ArchivedAdapter adapter) {
		deleteItem(items, position, adapter);
		archivedCount.setText("You have "+items.size()+" things archived");
		fh.saveItems(items, archivedFilename);
	}
	
	private void unarchive(int position, ArchivedAdapter adapter) {
		fh.saveItem(items.get(position), toDoFilename);
		deleteItem(items, position, adapter);
		archivedCount.setText("You have "+items.size()+" things archived");
		fh.saveItems(items, archivedFilename);
	}

}