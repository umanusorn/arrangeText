package com.slsatl.aac2;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SortingScore {
	
	static String strScore="";
	static List<String>allScore = new ArrayList<String>();
	
	public static void Sorting(float totalScore, String sentence, int countCompare){
		
		strScore = countCompare+"||"+totalScore+"||"+sentence;
		Log.d ( "wtf",countCompare+"||"+totalScore+"||"+sentence );
		//strScore = totalScore+"||"+sentence;
		//strScore = sentence;
		allScore.add(strScore);
		AnswerSentence();
		//Collections.sort(allScore);
		System.out.println("Max Score is :" + allScore.get(0)); //Answer Sentence : allScore.get(0)
		//System.out.println(allScore);
	}
	
	public static List<String> AnswerSentence(){
		Log.d("",allScore+"viewAllScore");
		System.out.println(allScore+"-------------------");
		Collections.sort(allScore);
		return allScore;
		
	}
	
	

}
