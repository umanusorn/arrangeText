package com.slsatl.aac2;

public class CatIconAndLabel extends IconAndLabel {
	int cid;
	String title;
	String subtitle;
	String coverPath;
	int variation;
	int nextCid;
	int enabled;
	public CatIconAndLabel(int cid, String title, String subtitle, String coverPath, int variation, int nextCid, int enabled){
		super(coverPath,title);
		this.cid = cid;
		this.title = title;
		this.subtitle = subtitle;
		this.coverPath = coverPath;
		this.variation = variation;
		this.nextCid = nextCid;
		this.enabled = enabled;
	}
}
