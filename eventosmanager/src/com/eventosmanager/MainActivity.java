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
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;


public class MainActivity extends FragmentActivity {
	
	private static final int SELECTION = 0;
	private final int FRAGMENT_COUNT = SELECTION + 1;
	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
	private boolean isResumed = false;
	private Button eventButtonView;
	private Button eventButtonCreate;
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
		eventButtonCreate = (Button) findViewById(R.id.eventButtonCreate);
		Session session = Session.getActiveSession();
		
		if (session != null && session.isOpened()) {
			// show the event managment buttons
			eventButtonView.setVisibility(View.VISIBLE);
			eventButtonCreate.setVisibility(View.VISIBLE);
		} else {
			eventButtonView.setVisibility(View.INVISIBLE);
			eventButtonCreate.setVisibility(View.INVISIBLE);
		}
		
		authFbButton = (LoginButton) findViewById(R.id.authFb_button);
		authFbButton.setApplicationId(getString(R.string.app_id));
		
		eventButtonView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// view event button
				Toast.makeText(MainActivity.this, "TODO view events", Toast.LENGTH_SHORT).show();			
			}
		});
		
		eventButtonCreate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// cretae event button
				Toast.makeText(MainActivity.this, "TODO create events", Toast.LENGTH_SHORT).show();
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
	

	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		// only make changes if the activity is visible
		if (state.isOpened()) {
			// logged in
			// put event managment buttons visible
			eventButtonView.setVisibility(View.VISIBLE);
			eventButtonCreate.setVisibility(View.VISIBLE);
		} else if (state.isClosed()) {
			// logged out
			// hide the buttons
			eventButtonView.setVisibility(View.INVISIBLE);
			eventButtonCreate.setVisibility(View.INVISIBLE);
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
	
}
