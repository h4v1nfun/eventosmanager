package com.eventosmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
//import android.view.Menu;
//import android.view.MenuItem;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;


public class MainActivity extends FragmentActivity {
	
	private static final int SPLASH = 0;
	private static final int SELECTION = 1;
	//private static final int SETTINGS = 2;
	//private final int FRAGMENT_COUNT = SETTINGS + 1;
	private final int FRAGMENT_COUNT = SELECTION + 1;
	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
	//private MenuItem settings;
	private boolean isResumed = false;
	private Button eventButtonView;
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
		fragments[SPLASH] = fm.findFragmentById(R.id.splashFragment);
		fragments[SELECTION] = fm.findFragmentById(R.id.selectionFragment);
		//fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);
		eventButtonView = (Button) findViewById(R.id.eventButtonView);
		eventButtonView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "Soon...", Toast.LENGTH_SHORT).show();			
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
		//Session session = Session.getActiveSession();
		showFragment(SELECTION, false);
		//if (session != null && session.isOpened()) {
			// if the session is allready open show selection fragment
		//	showFragment(SELECTION, false);
		//} else {
			// otherwise show the splash screen
			//showFragment(SPLASH, false);
		//}
	}
	
	//@Override
	//public boolean onPrepareOptionsMenu(Menu menu) {
		// only add the menu when the selection fragment is showing
	//	if (fragments[SELECTION].isVisible()) {
		//	if (menu.size() == 0) {
			//	settings = menu.add(R.string.settings);
		//	}
		//	return true;
		//} else {
		//	menu.clear();
		//	settings = null;
		//}
		//return false;
	//}
	
	//@Override
	//public boolean onOptionsItemSelected(MenuItem item) {
		//if (item.equals(settings)) {
			//showFragment(SETTINGS, true);
			//return true;
		//}
		//return false;
	//}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		// only make changes if the activity is visible
		if (isResumed) {
			FragmentManager manager = getSupportFragmentManager();
			// get the number of entries in the back stack
			int backStackSize = manager.getBackStackEntryCount();
			// clear the back stack
			for (int i = 0; i < backStackSize; i++) {
				manager.popBackStack();
			}
			showFragment(SELECTION, false);
			//if (state.isOpened()) {
				// if session state is open show the authenticated fragment
			//	showFragment(SELECTION, false);
			//} else if (state.isClosed()) {
				// if session state is closed show the login fragment
			//	showFragment(SELECTION, false);
				//showFragment(SPLASH, false);
			//}
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
}
