package com.brass1.whattodo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;

/*
 * This file deals with saving and loading of files to make the data persistent.
 */

public class FileHandler {

	private Context context;
	private String seperator="__,__";
	
	public FileHandler(Context context) {
		this.context=context;
	}
	
	public ArrayList<ToDoItem> getItems(String filename) {
		ArrayList<ToDoItem>items=new ArrayList<ToDoItem>();
		try {
			FileInputStream in=context.openFileInput(filename);
			BufferedReader reader=new BufferedReader(new InputStreamReader(in));
			ToDoItem tempItem;
			String[] tempFields={""};
			String line=reader.readLine();
			while (line!=null) {
				tempFields=line.split(seperator);
				String tempText=tempFields[0];
				boolean tempCheck=Boolean.parseBoolean(tempFields[1]);
				tempItem=new ToDoItem(tempText, tempCheck);
				items.add(tempItem);
				line=reader.readLine();
			}
			in.close();
			reader.close();
		} catch (FileNotFoundException fnf) {
			fnf.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return items;
	}
	
	public void saveItem(ToDoItem item,String filename) {
		try {
			FileOutputStream out=context.openFileOutput(filename, Context.MODE_APPEND);
			String toFile=item.getText()+seperator+String.valueOf(item.getChecked())+"\n";
			out.write(toFile.getBytes());
			out.close();
		} catch (FileNotFoundException fnf) {
			fnf.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveItems(ArrayList<ToDoItem> items,String filename) {
		try {
			FileOutputStream out=context.openFileOutput(filename, Context.MODE_PRIVATE);
			for(ToDoItem item:items) {
				String toFile=item.getText()+seperator+String.valueOf(item.getChecked())+"\n";
				out.write(toFile.getBytes());
			}
			out.close();
		} catch (FileNotFoundException fnf) {
			fnf.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

}
