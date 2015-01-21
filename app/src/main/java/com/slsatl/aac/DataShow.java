package com.slsatl.aac;

public class DataShow {
private boolean isNew;
private boolean status;
private String  cid;
private String  title;
private String  subtitle;
private String  version;
private String  base_path;
private String  coverPath;
private String  weight;
private String  lang;
private String  variation;
private String  nextCid;
private String  enable;
private String  core;
private String  clientTitle;
private String  clientVersion;
private String  clientSubtitle;
private boolean check;


public DataShow(boolean isNew,
                boolean status,
                String cid,
                String title,
                String subtitle,
                String version,
                String base_path,
                String coverPath,
                String weight,
                String lang,
                String variation,
                String nextCid,
                String enable,
                String core,
                String clientVersion,
                String clientTitle,
                String clientSubtitle)
{
	// TODO Auto-generated constructor stub
	this.title = title;
	this.cid = cid;
	this.isNew = isNew;
	this.subtitle = subtitle;
	this.weight = weight;
	this.lang = lang;
	this.variation = variation;
	this.enable = enable;
	this.nextCid = nextCid;
	this.core = core;
	this.clientSubtitle = clientSubtitle;
	this.clientTitle = clientTitle;
	this.coverPath = coverPath;
	this.base_path = base_path;
	this.check = false;
	this.status = status;
	this.version = version;
	this.clientVersion = clientVersion;

}

public boolean IsNew() {
	return this.isNew;
}

public String getBase_path() {
	return this.base_path;
}

//ใช้ส่งค่ากลับไป setText ของ TextView
public String getCid() {
	return this.cid;
}

public String getClientSubtitle() {
	return this.clientSubtitle;
}

public String getClientTitle() {
	return this.clientTitle;
}

public String getClientVersion() {
	return this.clientVersion;
}

public String getCore() {
	return this.core;
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

public String getNextCid() {
	return this.nextCid;
}

public boolean getStatus() {
	return this.status;
}

public String getSubtitle() {
	return this.subtitle;
}

public String getTitle() {
	return this.title;
}

public String getVariation() {
	return this.variation;
}

public String getVersion() {
	return this.version;
}

public String getWeight() {
	return this.weight;
}

public boolean isCheck() {
	return this.check;
}

public void setCheck(boolean a) {
	this.check = a;
}


}
