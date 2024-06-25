package com.infinityicon.mobilenews;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLParser {
	public String getXMLFromURL ( String url ) {
		String xml = null;
		
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient ( );
			HttpPost httpPost = new HttpPost ( url );
			
			HttpResponse httpResponse = httpClient.execute ( httpPost );
			HttpEntity httpEntity = httpResponse.getEntity ( );
			
			xml = EntityUtils.toString( httpEntity );
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xml;
	}
	
	public Document getDomElement ( String xml ) {
		Document doc = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance ( );
			
			DocumentBuilder db = dbf.newDocumentBuilder ( );
			InputSource is = new InputSource ( );
			is.setCharacterStream( new StringReader ( xml ) );
			doc = db.parse( is );
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}
	
	public String getValue ( Element item, String str ) {
		NodeList n = item.getElementsByTagName ( str );
		return this.getElementValue ( n.item( 0 ) );
	}

	public final String getElementValue(Node elem) {
		Node child;
		if ( elem != null ) {
			if ( elem.hasChildNodes ( ) ) {
				for ( child = elem.getFirstChild ( ); child!=null; child = child.getNextSibling ( ) ) {
					if ( child.getNodeType() == Node.TEXT_NODE ) {
						return child.getNodeValue ( );
					}
				}
			}
		}
		return "";
	}
}
