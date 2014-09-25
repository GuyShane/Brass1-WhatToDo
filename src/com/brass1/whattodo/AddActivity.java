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
