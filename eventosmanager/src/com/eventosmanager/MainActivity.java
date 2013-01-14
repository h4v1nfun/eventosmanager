package com.eventosmanager;

//import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
//import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.Settings;
//import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;


public class MainActivity extends Activity {
	
	public final String PREFERENCES_NAME = "mypreferences";
	public final String IS_LOG = "isLogged";
	
	private Button eventButtonView;
	private Button aboutButton;
	private Button loginButton;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private boolean isLogged = false;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		
		SharedPreferences settings = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor edit = settings.edit();
		//SharedPreferences.Editor editor = settings.edit();
		isLogged = settings.getBoolean(IS_LOG, false);
		
		//checks for internet connection
		//isOnline = haveInternet();
		if (haveInternet() == false) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(R.string.error); 
			alertDialog.setMessage(getString(R.string.netDownText)); 
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// no network exiting
					System.exit(0);
				}
			});
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Wifi", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// user pressed wifi button
					// opens wifi settings on phone
					startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));	
				}
			});
			alertDialog.show();
		}
		
		
		eventButtonView = (Button) findViewById(R.id.eventButtonView);
		aboutButton = (Button) findViewById(R.id.aboutView);
		loginButton = (Button) findViewById(R.id.loginView);
		
		// get the active session if any
		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(getApplicationContext(), null, statusCallback, savedInstanceState);
				settings.edit().putBoolean(IS_LOG, true);
			}
			if (session == null){
				settings.edit().putBoolean(IS_LOG, false);
			}
			Session.setActiveSession(session);
			if (session != null) {
				settings.edit().putBoolean(IS_LOG, true);
			}
			edit.commit();
			isLogged = settings.getBoolean(IS_LOG, false);
			showButton(isLogged);
		}
		
		// Context Menu
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
		
		// launch AboutActivity
		aboutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// about Button
				// start about activity
				Intent intent = new Intent(getApplicationContext(), com.eventosmanager.AboutActivity.class);
				
				startActivity(intent);
				
			}
		});
		
		// launch LoginActivity
		// TODO change to ConnectActivity
		loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// about Button
				// start about activity
				Intent intent = new Intent(getApplicationContext(), com.eventosmanager.LoginActivity.class);
				
				startActivityForResult(intent, 0);
				
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences settings = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		isLogged = settings.getBoolean(IS_LOG, false);
		showButton(isLogged);
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		SharedPreferences settings = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor edit = settings.edit();
		if (resultCode == 1) {
			settings.edit().putBoolean(IS_LOG, true);
		} else {
			settings.edit().putBoolean(IS_LOG, false);
		}
		edit.commit();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
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
	
    @Override
    public void onBackPressed() {
    	new AlertDialog.Builder(this)
    	.setTitle(R.string.exitMsgT) 
    	.setMessage(getString(R.string.exitMsg)) 
    	.setNegativeButton(android.R.string.no, null)
    	.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Exits the application
				System.exit(0);
			}
    	}).create().show();
    }
		
	private void showButton (boolean logged) {
		// show or hides the logged in or logged out interface
		if (logged == true) {
			eventButtonView.setVisibility(View.VISIBLE);
			loginButton.setText(R.string.logoutButton);
		} else if (logged == false) {
			eventButtonView.setVisibility(View.INVISIBLE);
			loginButton.setText(R.string.loginButton);
		}
	}
	
	private boolean haveInternet() {
		// checks for internet connectivity
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}
	private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            updateView();
        }
    }
	
	private void updateView() {
        Session session = Session.getActiveSession();
        SharedPreferences settings = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor edit = settings.edit();
        if (session.isOpened()) {
        	settings.edit().putBoolean(IS_LOG, true);
        	showButton(true);

        } else {
        	settings.edit().putBoolean(IS_LOG, false);
        	showButton(false);
        }
        edit.commit();
    }

}