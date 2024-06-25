package com.infinityicon.mobilenews;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class DisplayNews extends Activity {
	DataAccess da;
	Cursor c;
	SimpleCursorAdapter adapter;
	ListView lv;
	TextView txtAd;
	DrawableManager dm;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_news_layout);

		lv = (ListView) findViewById(R.id.lstNewsList);
		txtAd = ( TextView ) findViewById( R.id.txtAd );
		
		txtAd.setText( Html.fromHtml("<a href=\"https://play.google.com/store/apps/details?id=com.infinityicon.newcellphoneprices\">Latest Mobile Phone Prices</a>"));
		txtAd.setMovementMethod(LinkMovementMethod.getInstance());

		String strTitle = getIntent().getExtras().getString("title");
		da = new DataAccess(DisplayNews.this);
		dm = new DrawableManager ( );
		
		Log.d ( "DisplayNews", strTitle );
		c = da.GetNewsByTitle(strTitle);
		startManagingCursor ( c );

		adapter = new SimpleCursorAdapter(DisplayNews.this,
				R.layout.news_detail_layout, c, new String[] { da.N_TITLE,
						da.N_DATE, da.N_IMAGE, da.N_CONTENT }, new int[] {
						R.id.txtNewsTitle, R.id.txtNewsDate, R.id.imgNewsImage,
						R.id.txtNewsText });
		
		adapter.setViewBinder( new SimpleCursorAdapter.ViewBinder() {
			
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				switch ( view.getId() ) {
				case R.id.imgNewsImage:
					String strImgURL = cursor.getString( cursor.getColumnIndex( da.N_IMAGE));
					dm.fetchDrawableOnThread(strImgURL, (ImageView) view );
					return true;
				case R.id.txtNewsText:
					String strNewsText = cursor.getString( cursor.getColumnIndex( da.N_CONTENT));
					( ( TextView )view).setText( strNewsText );
				default:
					return false;
				}
			}
		});
		
		lv.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		c.close ( );
		da.DestoryDB();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuUpdateNews:
			startActivity(new Intent(DisplayNews.this, UpdateNews.class));
			return true;
		default:
			return false;
		}
	}
}