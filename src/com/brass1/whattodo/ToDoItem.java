package com.brass1.whattodo;

/*
 * This class is used to store data about a to do list item
 * It has the text, and whether or not it has been marked as completed
 */

public class ToDoItem {
	private String text;
	private boolean checked;
	
	public ToDoItem(String text,boolean checked) {
		this.text=text;
		this.checked=checked;
	}
	
	public boolean getChecked() {
		return this.checked;
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setChecked(boolean check) {
		this.checked=check;
	}
	
	public void setText(String text) {
		this.text=text;
	}
	
	@Override
	public String toString() {
		return this.text;
	}

}
