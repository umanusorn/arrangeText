package com.slsatl.aac;


	

public class ConstantDBfromDownload {
	String cate;
	String locale;
	String tag;
	String picCovered;
	String pic;
	String type;
	String sound;
	
	public  ConstantDBfromDownload(String []b){
	
		this.cate = b[0];
		this.locale = b[1];
		this.tag = b[2] ;
		this.picCovered = b[3];
		this.pic = b[4];
		this.type= b[5];
		this.sound = b[6];
		
	}

}
