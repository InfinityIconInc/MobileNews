package com.infinityicon.mobilenews;

import java.util.Date;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpdateNews extends Activity implements OnClickListener {
	final String URL = "http://www.newcellphoneprices.com/n1.php";
	final String XML_WRAPPER = "news";
	final String XML_ID = "id";
	final String XML_TITLE = "title";
	final String XML_DATE = "date";
	final String XML_IMAGE = "image";
	final String XML_SUMMARY = "summary";
	final String XML_CONTENT = "content";

	final String URL_IMAGE = "http://www.newcellphoneprices.com/Mobile_Set_Images/newspicts/";
	final String URL_LOCAL_IMAGE = "/data/data/com.infinityicon.mobilenews/images/";
	final String TAG = "UpdateNews";
	
	Button btnUpdateNews;
	ProgressBar pb;
	TextView txtMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView ( R.layout.update_news_layout);
		
		btnUpdateNews = ( Button ) findViewById( R.id.btnUpdateNews );
		btnUpdateNews.setOnClickListener( this );
		
		pb = ( ProgressBar ) findViewById( R.id.pbProgress );
		
		txtMessage = ( TextView ) findViewById( R.id.txtMessage );
		txtMessage.setText( "Press the Button Above to Get Latest News! (512KB). You should be connected to the Internet.");
	}
	
	@Override
	public void onClick(View button) {
		Button b = ( Button ) button;
		String strText = b.getText().toString();
		if  ( strText.compareTo( "Get Latest News") == 0 ) {
			new UpdateNewsThread ( ).execute( URL );
			txtMessage.setText( "Wait a Minute!");
			b.setEnabled( false );
		}
		else
			startActivity ( new Intent ( UpdateNews.this, MainActivity.class));
	}
	
	class UpdateNewsThread extends AsyncTask <String, Integer, String> {
		XMLParser parser;
		NodeList nl;
		DataAccess dataAccess;
		DownloadImages di;
		float progressedItems;
		float totalItems;
		int currentProg;

		@Override
		protected String doInBackground(String... URLparams) {
			Log.d ( TAG, "Entered doinBG");
			parser = new XMLParser();
			String xml = parser.getXMLFromURL( URLparams[0] );
			Document doc = parser.getDomElement(xml);
			nl = doc.getElementsByTagName(XML_WRAPPER);
			Log.d ( TAG, "Passed Init");
			totalItems = 30f;
			progressedItems = 0f;
			currentProg = 0;

			Log.d ( TAG, "B4 DI");
			di = new DownloadImages ( );
			Log.d ( TAG, "B4 DA");
			dataAccess = new DataAccess ( UpdateNews.this );
			Log.d ( TAG, "B4 ListLocalFiles");
			List<String> existingFiles = di.ListLocalFiles();//getting existing files list
			//for have a look at what files are old and need to be deleted
			Log.d ( TAG, "B4 ZapNews");
			dataAccess.ZapNews();

			for (int i = 0; i < nl.getLength(); i++) {
				Element e = (Element) nl.item(i);
				Log.d("XML Parser:", parser.getValue(e, XML_TITLE));
				if ( Integer.parseInt( parser.getValue(e,	XML_ID) ) == -1 ) {
					totalItems = Float.parseFloat( parser.getValue(e, XML_TITLE));
					progressedItems = 0f;
					continue;
				}
				
				currentProg = Math.round((progressedItems/totalItems) * 100 );
				publishProgress ( currentProg );
				
				Log.e ( "TotalItems:", " " + totalItems );
				Log.e ( "ProgrItems:", " " + progressedItems );
				Log.e ( TAG, "Prog: " + currentProg);
				String strNewsContent = parser.getValue(e, XML_CONTENT);
				strNewsContent = strNewsContent.replace("[==p1==]", "");
				strNewsContent = strNewsContent.replace("[==p2==]", "");
				strNewsContent = strNewsContent.replace("[==p3==]", "");

				dataAccess.InsertNews(new News(Integer.parseInt(parser.getValue(e,
						XML_ID)), parser.getValue(e, XML_TITLE), parser.getValue(e,
						XML_DATE), URL_IMAGE + parser.getValue(e, XML_IMAGE), parser.getValue(
						e, XML_SUMMARY), strNewsContent.trim()));
//				di.DownloadFromURL(URL_IMAGE + parser.getValue(e, XML_IMAGE),
//						parser.getValue(e, XML_IMAGE));
				//Match existingFiles list with updated file list and deleted the
				//old files listing in existingFiels variable
				for ( int j = 0; j < existingFiles.size(); j ++ ) {
					if ( existingFiles.get( j ).compareTo( parser.getValue ( e, XML_IMAGE ) ) == 0 ) {
						Log.e ( "REM:", existingFiles.get( j ));
						existingFiles.remove( j );
					}
				}
				progressedItems++;
				///////////////////////////////////////////////////////////////////////////
			}
			//PHYSICALLY DELETING FILES those are old based upon upper existingFiles
			//variable remains
			for ( String f : existingFiles ) {
				di.DeleteLocalFile( f );
				Log.e ( "DEL:", f );
			}
			///////////////////////////////////////////////////////////////////////////////
			Date d = new Date();
			dataAccess.ZapUpdated();
			dataAccess.InsertUpdated(d.getYear() + "-" + d.getMonth() + "-"
					+ d.getDate());
			return "News Updated!!!";
		}

		@Override
		protected void onPostExecute(String result) {
			pb.setProgress( 100 );
			txtMessage.setText( result );
			btnUpdateNews.setText( "Done");
			btnUpdateNews.setEnabled( true );
			dataAccess.DestoryDB();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			pb.setProgress( values[0] );
		}
	}
}