package com.infinityicon.mobilenews;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.util.Log;

public class DownloadImages {
	private final String PATH = "/data/data/com.infinityicon.mobilenews/images/";
	public DownloadImages ( ) {
		File imagesDir = new File(PATH); // creates images directory in PATH
		if (!imagesDir.exists()) {
			imagesDir.mkdir();
		}		
	}
	public boolean FileExists ( String fileName ) {
		File imageFile = new File ( PATH + fileName );
		if ( imageFile.exists())
			return true;
		else
			return false;
	}
	
	public List<String> ListLocalFiles ( ) {
		List<String> fileNames = new ArrayList<String> ();
		
		File imageDirFiles = new File ( PATH );
		if ( imageDirFiles != null ) {
			File [ ] filename = imageDirFiles.listFiles();
			for ( File f : filename ) {
				fileNames.add( f.getName() );
			}
		}
		return fileNames;
	}
	
	public void DeleteLocalFile ( String strFileName ) {
		File f = new File ( PATH + strFileName );
		if ( f.exists() ) {
			f.delete();
		}
	}
	
	public void DownloadFromURL(String imageURL, String fileName) {
		try {
			File imagesDir = new File(PATH); // creates images directory in PATH
			if (!imagesDir.exists()) {
				imagesDir.mkdir();
			}

			URL url = new URL(imageURL);
			File file = new File(PATH + fileName);

			if (!file.exists()) {

				long startTime = System.currentTimeMillis();
				Log.d("DownloadImages", imageURL);

				URLConnection ucon = url.openConnection();
				InputStream is = ucon.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);

				ByteArrayBuffer baf = new ByteArrayBuffer(50);
				int current = 0;

				while ((current = bis.read()) != -1) {
					baf.append((byte) current);
				}

				FileOutputStream fos = new FileOutputStream(file);
				fos.write(baf.toByteArray());
				fos.close();
				Log.d("DownloadImages",
						"Image Ready in "
								+ ((System.currentTimeMillis() - startTime) / 1000)
								+ " sec");
			}
			else
				Log.d("DownloadImages", "Image already there" );
			
		} catch (IOException e) {
			Log.d("DownloadImages", "Error " + e);
		}
	}
}
