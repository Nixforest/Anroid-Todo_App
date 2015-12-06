package com.nixforest.todo_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class TodoList extends Activity {
	static final private int ADD_NEW_TODO = Menu.FIRST;
	static final private int REMOVE_TODO = Menu.FIRST + 1;
	static final private int CANCEL_TODO = Menu.FIRST + 2;
	private ListView myListView;
	private EditText myEditText;
	private ArrayList<String> todoItems;
	private ArrayAdapter<String> aa;
	private boolean	addingNew = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list);
		// Get references to UI widgets
		myListView = (ListView)findViewById(R.id.myListView);
		myEditText = (EditText)findViewById(R.id.myEditText);
		// Create the array list of to do items
		todoItems = new ArrayList<String>();
		// Create the array adapter to bind the array to the listview
		aa = new ArrayAdapter<String>(this,
				//android.R.layout.simple_list_item_1,
				R.layout.todolist_item,
				todoItems);
		// Bind the array adapter to the listView
		myListView.setAdapter(aa);
		myEditText.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if (arg1 == KeyEvent.KEYCODE_ENTER) {
						if (!myEditText.getText().toString().isEmpty()) {
							todoItems.add(0, myEditText.getText().toString());
							myEditText.setText("");
							aa.notifyDataSetChanged();
							cancelAdd();
							return true;
						}
					}
				}
				return false;
			}
		});
		registerForContextMenu(myListView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.todo_list, menu);
		super.onCreateOptionsMenu(menu);
		// Create and add new menu items
		MenuItem itemAdd = menu.add(0, ADD_NEW_TODO, Menu.NONE, R.string.add_new);
		MenuItem itemCancel = menu.add(0, CANCEL_TODO, Menu.NONE, R.string.cancel);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case CANCEL_TODO: {
				cancelAdd();
				return true;
			}
			case ADD_NEW_TODO: {
				addNewItem();
				return true;
			}
			default: break;
		}
		return false;
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		switch (item.getItemId()) {
			case REMOVE_TODO: {
				AdapterView.AdapterContextMenuInfo menuInfo;
				menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
				int idx = menuInfo.position;
				removeItem(idx);
				return true;
			}
			default: break;
		}
		return false;
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu,
									View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Selected To Do Item");
		menu.add(0, REMOVE_TODO, Menu.NONE, R.string.remove);
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		// Get references to UI widgets
		MenuItem cancelItem = menu.findItem(CANCEL_TODO);
		cancelItem.setVisible(addingNew);
		return true;
	}
	private void addNewItem() {
		addingNew = true;
		myEditText.setVisibility(View.VISIBLE);
		myEditText.requestFocus();
	}
	private void removeItem(int _index) {
		todoItems.remove(_index);
		aa.notifyDataSetChanged();
	}
	private void cancelAdd() {
		addingNew = false;
		myEditText.setVisibility(View.GONE);
		myEditText.setText("");
	}
}
