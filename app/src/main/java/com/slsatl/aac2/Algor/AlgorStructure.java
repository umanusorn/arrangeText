package com.slsatl.aac2.Algor;

/**
 * Created by um2015 on 2/7/2015.
 */
public class AlgorStructure {

	public String classStr="";
	public String subClassStr="";
 public String pos="";
public String tag="";

public AlgorStructure(String classStr,String pos,String tag,String subClassStr){

	this.classStr=classStr;
	this.subClassStr=subClassStr;
	this.pos=pos;
	this.tag=tag;

}

public String getClassStr() {
	return classStr;
}

public String getPos() {
	return pos;
}

public String getStructuredString(){

	return subClassStr+","+pos+","+tag+","+classStr;
}

public String getSubClassStr() {
	return subClassStr;
}

public String getTag() {
	return tag;
}

public void setClassStr(String classStr) {
	this.classStr = classStr;
}

public void setPos(String pos) {
	this.pos = pos;
}

public void setSubClassStr(String subClassStr) {
	this.subClassStr = subClassStr;
}

public void setTag(String tag) {
	this.tag = tag;
}
}
