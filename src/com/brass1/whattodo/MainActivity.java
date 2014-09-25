package com.brass1.whattodo;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity implements ActionBar.TabListener {
	
	private FileHandler fh;
	private String toDoFilename="todo.txt";
	private String archivedFilename="archived.txt";
	private ArrayList<ToDoItem> items;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	final ActionBar actionBar=getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab().setText("To do").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Archived").setTabListener(this));
		fh=new FileHandler(this.getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id==R.id.action_addItem) {
        	Intent add_intent=new Intent(MainActivity.this, AddActivity.class);
        	startActivity(add_intent);
        	return true;
        }
        else if (id==R.id.action_selectItems) {
        	return true;
        }
        else if (id==R.id.action_emailAll) {
        	items=fh.getItems(toDoFilename);
        	items.addAll(fh.getItems(archivedFilename));
			Intent emailIntent=new Intent(Intent.ACTION_SEND);
			emailIntent.setData(Uri.parse("mailto:"));
			emailIntent.setType("text/plain");
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "To do list");
			StringBuilder sb=new StringBuilder();
			for (ToDoItem i:items) {
				sb.append(i.getText()+"\n");
			}
			String list=sb.toString();
			emailIntent.putExtra(Intent.EXTRA_TEXT, list);
			try {
				startActivity(Intent.createChooser(emailIntent, "Send email"));
			}
			catch (android.content.ActivityNotFoundException anfe) {
				Toast.makeText(this, "No email service found", Toast.LENGTH_SHORT).show();
			}
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		switch (tab.getPosition()) {
		case 0:
			Fragment toDoFrag=new ToDoFragment();
			getFragmentManager().beginTransaction().replace(android.R.id.content,toDoFrag).commit();
			break;
		case 1:
			Fragment archivedFrag=new ArchivedFragment();
			getFragmentManager().beginTransaction().replace(android.R.id.content,archivedFrag).commit();
			break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}
}
