package com.infinityicon.mobilenews;

public class News {
	int iID;
	String strTitle;
	String strDate;
	String strImage;
	String strSummary;
	String strContent;
	
	public News(int iID, String strTitle, String strDate, String strImage,
			String strSummary, String strContent) {
		super();
		this.iID = iID;
		this.strTitle = strTitle;
		this.strDate = strDate;
		this.strImage = strImage;
		this.strSummary = strSummary;
		this.strContent = strContent;
	}

	public int getiID() {
		return iID;
	}

	public void setiID(int iID) {
		this.iID = iID;
	}

	public String getStrTitle() {
		return strTitle;
	}

	public void setStrTitle(String strTitle) {
		this.strTitle = strTitle;
	}

	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	public String getStrImage() {
		return strImage;
	}

	public void setStrImage(String strImage) {
		this.strImage = strImage;
	}

	public String getStrSummary() {
		return strSummary;
	}

	public void setStrSummary(String strSummary) {
		this.strSummary = strSummary;
	}

	public String getStrContent() {
		return strContent;
	}

	public void setStrContent(String strContent) {
		this.strContent = strContent;
	}
}
