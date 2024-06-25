package com.infinityicon.mobilenews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
	private final int DELAY = 2000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView ( R.layout.splash_screen_layout );
		
		new Handler ( ).postDelayed( new Runnable ( ) {
			public void run ( ) {
				Intent splashIntent = new Intent ( SplashScreen.this, MainActivity.class );
				startActivity ( splashIntent );
				SplashScreen.this.finish();
			}
		}, DELAY );
	}
}