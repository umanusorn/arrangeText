package com.slsatl.aac;

public class LexIconAndLabel extends IconAndLabel {
	int lid;
	int cid;
	String tag;
	String picPath;
	String voicePath;
	int nextCid;
	int enable;
	public LexIconAndLabel(int lid, int cid, String tag, String picPath, String voicePath, int nextCid, int enable){
		super(picPath,tag);
		this.lid = lid;
		this.cid = cid;
		this.tag = tag;
		this.picPath = picPath;
		this.voicePath = voicePath;
		this.nextCid = nextCid;
		this.enable = enable;
	}
}
