package com.eventosmanager;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.view.View;
import android.view.View.OnClickListener;

public class AboutActivity extends Activity {

	private ImageButton backButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		backButtonListener();
		
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

}
