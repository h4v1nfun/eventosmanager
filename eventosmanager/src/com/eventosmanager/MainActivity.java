package com.eventosmanager;

//import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
//import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.Toast;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;


public class MainActivity extends FragmentActivity {
	
	private static final int SELECTION = 0;
	private final int FRAGMENT_COUNT = SELECTION + 1;
	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
	private boolean isResumed = false;
	private boolean isLogged = false;
	private Button eventButtonView;
	private Button aboutButton;
	private Button loginButton;
	private LoginButton authFbButton;
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		FragmentManager fm = getSupportFragmentManager();
		fragments[SELECTION] = fm.findFragmentById(R.id.selectionFragment);
		eventButtonView = (Button) findViewById(R.id.eventButtonView);
		aboutButton = (Button) findViewById(R.id.aboutView);
		loginButton = (Button) findViewById(R.id.loginView);
		Session session = Session.getActiveSession();
		
		if (session != null && session.isOpened()) {
			// show the event managment button
			isLogged = true;
			showButton(isLogged);
		} else {
			isLogged = false;
			showButton(isLogged);
		}
		
		authFbButton = (LoginButton) findViewById(R.id.authFbbutton);
		authFbButton.setApplicationId(getString(R.string.app_id));
		authFbButton.setVisibility(View.INVISIBLE);
		//authFbButton.setReadPermissions(Arrays.asList("user_status", "user_events"));

		eventButtonView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// manage event button
				// show context menu in short press
				registerForContextMenu(v);
				openContextMenu(v);
				unregisterForContextMenu(v);
			}
		});
		
		aboutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// about Button
				// start about activity
				Intent intent = new Intent(getApplicationContext(), com.eventosmanager.AboutActivity.class);
				
				startActivity(intent);
				
			}
		});
		
		loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// about Button
				// start about activity
				Intent intent = new Intent(getApplicationContext(), com.eventosmanager.LoginActivity.class);
				
				startActivity(intent);
				
			}
		});
		
		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			transaction.hide(fragments[i]);
		}
		transaction.commit();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		isResumed = true;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		showFragment(SELECTION, false);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		// context Menu Creation
		// menu options in eventmenu.xml
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.eventmenu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// context Menu Item selection
		//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.listEvent:
		{
			Toast.makeText(MainActivity.this, "TODO list events", Toast.LENGTH_SHORT).show();
			return true;
		}
		case R.id.createEvent:
		{
			Toast.makeText(MainActivity.this, "TODO create events", Toast.LENGTH_SHORT).show();
			return true;
		}
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	public void onSessionStateChange(Session session, SessionState state, Exception exception) {
		// only make changes if the activity is visible
		if (state.isOpened()) {
			// logged in
			// put event managment button visible
			isLogged = true;
			showButton(isLogged);
		} else if (state.isClosed()) {
			// logged out
			// hide the button
			isLogged = false;
			showButton(isLogged);
		}
		if (isResumed) {
			FragmentManager manager = getSupportFragmentManager();
			// get the number of entries in the back stack
			int backStackSize = manager.getBackStackEntryCount();
			// clear the back stack
			for (int i = 0; i < backStackSize; i++) {
				manager.popBackStack();
			}
			showFragment(SELECTION, false);
		}
	}
	
	private void showFragment(int fragmentIndex, boolean addToBackStack) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			if (i == fragmentIndex) {
				transaction.show(fragments[i]);
			} else {
				transaction.hide(fragments[i]);
			}
		}
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}
	
	private void showButton (boolean logged) {
		// show or hides the logged in interface
		if (logged == true) {
			eventButtonView.setVisibility(View.VISIBLE);
			loginButton.setText("Log Out");
		} else if (logged == false) {
			eventButtonView.setVisibility(View.INVISIBLE);
			loginButton.setText("Login");
		}
	}
}
