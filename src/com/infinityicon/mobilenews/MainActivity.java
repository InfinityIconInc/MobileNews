package com.infinityicon.mobilenews;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {

	DataAccess dataAccess;
	ListView lstNews;
	TextView txtAd;
	SimpleCursorAdapter adapter;
	Cursor cursor;
	final String DATE_FORMAT = "yyyy-MM-dd";

	@SuppressWarnings({ "deprecation" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lstNews = (ListView) findViewById(R.id.lstNews);
		txtAd = ( TextView ) findViewById( R.id.txtAd );

		dataAccess = new DataAccess(this);

		cursor = dataAccess.GetAllNews();
		//startManagingCursor ( cursor );//android will manage cursor closing etc.
		txtAd.setText( Html.fromHtml("<a href=\"https://play.google.com/store/apps/details?id=com.infinityicon.newcellphoneprices\">Latest Mobile Phone Prices</a>"));
		txtAd.setMovementMethod(LinkMovementMethod.getInstance());
		//Linkify.addLinks(txtAd, Pattern.compile("Get"), "http://play.google.com/store/apps/details?id=com.infinityicon.newcellphoneprices" );
		
		if ( cursor.getCount() == 0 ) {
			startActivity ( new Intent ( this, UpdateNews.class));
		}
			
		adapter = new SimpleCursorAdapter(MainActivity.this,
				R.layout.news_list_layout, cursor, new String[] {
						dataAccess.N_TITLE, dataAccess.N_DATE,
						dataAccess.N_SUMMARY }, new int[] {
						R.id.txtTitle, R.id.txtDate, R.id.txtSummary });
		
		if ( cursor.getCount() != 0 ) {
			Cursor firstItem = ( Cursor ) adapter.getItem( 0 );
			//startManagingCursor ( firstItem );
			//SHOW UPDATE NEWS MESSAGE TO USER 
			String strDate = firstItem.getString
					(firstItem.getColumnIndex( dataAccess.N_DATE ));
			SimpleDateFormat dateFormat = new SimpleDateFormat ( DATE_FORMAT );
			try {
				Date lastDate = dateFormat.parse( strDate );
				Date todayDate = new Date ( );
				if ( lastDate.before(todayDate) ) {
					Long difference = (todayDate.getTime() - lastDate.getTime())/1000/60/60/24;
					Log.d ( "Date:", "compare: " + difference );
					if ( difference >= 3 ) {
						Log.d ( "MainActivity", "Get Latest News NOW");
						Toast.makeText(MainActivity.this, "Press Menu and Select 'Get Latest News' to get Todays News", Toast.LENGTH_LONG).show();
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

//		adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
//
//			@Override
//			public boolean setViewValue(View view, Cursor cursor,
//					int columnIndex) {
//				switch (view.getId()) {
//				case R.id.imgNews:
//					String strImage = cursor.getString(cursor
//							.getColumnIndex(dataAccess.N_IMAGE));
//					Log.d("Inside SetView", strImage);
//					((ImageView) view).setImageBitmap(BitmapFactory
//							.decodeFile(strImage));
//				}
//				return false;
//			}
//		});

		lstNews.setAdapter(adapter);
		lstNews.setOnItemClickListener( this );
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dataAccess.DestoryDB();
		cursor.close();
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
			startActivity(new Intent(MainActivity.this, UpdateNews.class));
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long id) {
		TextView tvTitle = (TextView) view.findViewById(R.id.txtTitle);
		String strTitle = tvTitle.getText().toString();
		Toast.makeText(this, "Loading " + strTitle, Toast.LENGTH_SHORT).show();

		Intent intent = new Intent(this, DisplayNews.class);
		intent.putExtra("title", strTitle);
		startActivity(intent);
	}

	// @Override
	// public void onClick(View v) {
	// startActivity ( new Intent ( MainActivity.this, UpdateNews.class ) );
	// parser = new XMLParser();
	// String xml = parser.getXMLFromURL(URL);
	// Document doc = parser.getDomElement(xml);
	// nl = doc.getElementsByTagName(XML_WRAPPER);
	// di = new DownloadImages();
	// List<String> existingFiles = di.ListLocalFiles();//getting existing files
	// list
	// //for have a look at what files are old and need to be deleted
	// dataAccess.ZapNews();
	//
	// for (int i = 0; i < nl.getLength(); i++) {
	// Element e = (Element) nl.item(i);
	// // Log.e("XML Parser:", parser.getValue(e, XML_ID));
	// Log.d("XML Parser:", parser.getValue(e, XML_TITLE));
	// // Log.e("XML Parser:", parser.getValue(e, XML_IMAGE));
	// // Log.e("XML Parser:", parser.getValue(e, XML_DATE));
	// // Log.e("XML Parser:", parser.getValue(e, XML_SUMMARY));
	// dataAccess.InsertNews(new News(Integer.parseInt(parser.getValue(e,
	// XML_ID)), parser.getValue(e, XML_TITLE), parser.getValue(e,
	// XML_DATE), URL_LOCAL_IMAGE + parser.getValue(e, XML_IMAGE),
	// parser.getValue(
	// e, XML_SUMMARY), parser.getValue(e, XML_CONTENT)));
	// di.DownloadFromURL(URL_IMAGE + parser.getValue(e, XML_IMAGE),
	// parser.getValue(e, XML_IMAGE));
	// //Match existingFiles list with updated file list and deleted the
	// //old files listing in existingFiels variable
	// // for ( Iterator<String> f = existingFiles.iterator(); f.hasNext(); ) {
	// // String s = f.next();
	// // if ( parser.getValue(e, XML_IMAGE).compareTo( s ) == 0 ) {
	// // f.remove();
	// // Log.e ( "REM:", f );
	// // }
	// for ( int j = 0; j < existingFiles.size(); j ++ ) {
	// if ( existingFiles.get( j ).compareTo( parser.getValue ( e, XML_IMAGE ) )
	// == 0 ) {
	// Log.e ( "REM:", existingFiles.get( j ));
	// existingFiles.remove( j );
	// }
	// }
	// ///////////////////////////////////////////////////////////////////////////
	// }
	// //PHYSICALLY DELETING FILES those are old based upon upper existingFiles
	// //variable remains
	// for ( String f : existingFiles ) {
	// di.DeleteLocalFile( f );
	// Log.e ( "DEL:", f );
	// }
	// ///////////////////////////////////////////////////////////////////////////////
	// Date d = new Date();
	// dataAccess.ZapUpdated();
	// dataAccess.InsertUpdated(d.getYear() + "-" + d.getMonth() + "-"
	// + d.getDate());
	// }
}
