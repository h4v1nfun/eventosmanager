package com.eventosmanager;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.view.View;
import android.view.View.OnClickListener;

public class LoginActivity extends Activity {

	private ImageButton backButton;
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
		
		setContentView(R.layout.login);
		
		backButtonListener();
		authFbButton = (LoginButton) findViewById(R.id.authFbbutton);
		authFbButton.setApplicationId(getString(R.string.app_id));
		//authFbButton.setReadPermissions(Arrays.asList("user_status", "user_events"));
		
	}
	
	public void backButtonListener(){
		
		backButton = (ImageButton) findViewById(R.id.backButton);
		
		backButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getApplicationContext(), com.eventosmanager.MainActivity.class);
				
				startActivity(intent);
				finish();
			}
		});
	}
	
	public void onSessionStateChange(Session session, SessionState state, Exception exception) {
		// only make changes if the activity is visible
		if (state.isOpened()) {
			// logged in
			// put event managment button visible
			//isLogged = true;
			//showButton(isLogged);
		} else if (state.isClosed()) {
			// logged out
			// hide the button
			//isLogged = false;
			//showButton(isLogged);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	//	isResumed = true;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	//	isResumed = false;
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

}
