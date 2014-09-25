package com.brass1.whattodo;

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
