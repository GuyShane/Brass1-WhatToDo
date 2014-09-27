package com.brass1.whattodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
 * This class is used to get input from the user to add a
 * new item to the to do list. The item is always added
 * to the to do list, and can later be archived if needed
 */

public class AddActivity extends Activity {
	private Button addOK;
	private Button addCancel;
	private TextView itemText;
	private FileHandler fh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		fh=new FileHandler(AddActivity.this);
		
		setContentView(R.layout.activity_add);
		
		addOK=(Button)findViewById(R.id.add_OK);
		addCancel=(Button)findViewById(R.id.add_cancel);
		itemText=(TextView)findViewById(R.id.add_text);
		
		addOK.setEnabled(false);
		addOK.setClickable(false);
		
		itemText.addTextChangedListener(new TextWatcher() {
			/*
			 * This is to ensure the user does not enter an empty string as a
			 * to do item. The OK button will only be active if there is text present
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(count!=0 && before==0) {
					addOK.setEnabled(true);
					addOK.setClickable(true);
				}
				else if (count==0) {
					addOK.setEnabled(false);
					addOK.setClickable(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		addOK.setOnClickListener(new OnClickListener() {
			/*
			 * Save the item and display the list
			 */
			@Override
			public void onClick(View v) {
				ToDoItem item=new ToDoItem(itemText.getText().toString(), false);
				fh.saveItem(item,"todo.txt");
				Toast.makeText(AddActivity.this, "Item added", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(AddActivity.this,MainActivity.class));
			}
		});
		
		addCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AddActivity.this,MainActivity.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
