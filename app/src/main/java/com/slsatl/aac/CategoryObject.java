package com.slsatl.aac;

public class CategoryObject {
private String title;
private String version;
private String weight;
private String coverPath;
private String lang;
private String enable;
private String core;
private String cid;
private String nextCid;
private String variation;
private String subtitle;

public CategoryObject(String enable,
                      String core,
                      String nextCid,
                      String variation,
                      String subtitle,
                      String cid,
                      String v,
                      String w,
                      String t,
                      String c,
                      String l)
{
	this.title = t;
	this.version = v;
	this.weight = w;
	this.coverPath = c;
	this.lang = l;
	this.enable = enable;
	this.core = core;
	this.nextCid = nextCid;
	this.variation = variation;
	this.subtitle = subtitle;
	this.cid = cid;

}

public String getCid() {
	return this.cid;
}

public String getCoverPath() {
	return this.coverPath;
}

public String getEnable() {
	return this.enable;
}

public String getLang() {
	return this.lang;
}

public String getSubtitle() {
	return this.subtitle;
}

public String getTitle() {
	return this.title;
}

public String getVersion() {
	return this.version;
}

public String getWeight() {
	int random = ((int) Math.random()*1000000);
	return this.weight;
}
}
