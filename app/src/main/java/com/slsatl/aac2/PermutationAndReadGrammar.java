package com.slsatl.aac2;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PermutationAndReadGrammar {

	// -------------------- ImgPermutation-----------------------------
	// static void ImgPermutation(List<String> arrWord, List<String> arrPOS, int
	// k) {

	static void ImgPermutation(List<String> arrWord, int k,Context context) {
		String strSetImg = "";
		String strSetPOS = "";
		String strSetClass = "";
		String strSetSubclass = "";
		
		for (int i = k; i < arrWord.size(); i++) {
			java.util.Collections.swap(arrWord, i, k);
			ImgPermutation(arrWord, k + 1,context);
			java.util.Collections.swap(arrWord, k, i);
		}

		if (k == arrWord.size() - 1) {

			strSetImg = arrWord.toString();
			//System.out.println(arrWord.toString());
			//readGrammar("newGrammar.txt", arrWord);
			
			if(arrWord.size()==2){
				readGrammar("2Pics.txt", arrWord,context);
			}
			if(arrWord.size()==3){
				readGrammar("3Pics.txt", arrWord,context);
			}
			if(arrWord.size()==4){
				readGrammar("4Pics.txt", arrWord,context);
			}
			if(arrWord.size()==5){
				readGrammar("5Pics.txt", arrWord,context);

			}
			if(arrWord.size()==6){
				readGrammar("6Pics.txt", arrWord,context);
				//System.out.println(arrWord.toString());
			}
		}
	}

	// -------------------------- readGrammar------------------------
	public static void readGrammar(String filename, List<String> setImg,Context context) {
		
		FileInputStream fstream;
		List<String> setImgCorrectGrammar = new ArrayList<String>();
		List<String> setClassCorrectGrammar = new ArrayList<String>();
		List<String> setSubclassCorrectGrammar = new ArrayList<String>();
		List<String> POSChecking  = new ArrayList<String>();
		List<String> setOfPOS = new ArrayList<String>();
		List<String> setOfWord = new ArrayList<String>();
		List<String> setOfClass = new ArrayList<String>();
		List<String> setOfSubclass = new ArrayList<String>();
		String POSGrammar = null;
		List<String> returnValueWord = new ArrayList<String>();
		List<String> returnValueClass = new ArrayList<String>();
		List<String> returnValueSubclass = new ArrayList<String>();

		try {
			
			for (int i = 0; i < setImg.size(); i++) {
				
				String[] a = setImg.get(i).split(",");
				
				for(int j =0; j < a.length; j++){
				//System.out.println(a[j]);
				POSChecking.add(a[j]);
				}
				
			}

			
			for (int i = 0; i < POSChecking.size(); i = i + 4) {
				setOfClass.add(POSChecking.get(i));
				
			}
			
			for (int i = 1; i < POSChecking.size(); i = i + 4) {
				setOfPOS.add(POSChecking.get(i));
			}
				
			for (int i = 2; i <= POSChecking.size(); i = i + 4) {
				//System.out.println("aaaa");
				setOfWord.add(POSChecking.get(i));				
			}
			
			for (int i = 3; i <= POSChecking.size(); i = i + 4) {
				//System.out.println("aaaa");
				setOfSubclass.add(POSChecking.get(i));				
			}

			//System.out.println(setOfSubclass);
			
			POSGrammar = setOfPOS.toString();
			//System.out.println("POSGrammar: "+POSGrammar);

			BufferedReader br = new BufferedReader(new InputStreamReader ( context.getAssets().open(filename) ));
			String strLine = null;

			while ((strLine = br.readLine()) != null) {
				//System.out.println("strLine: "+strLine);
				if (strLine.equals(POSGrammar)) {
					setImgCorrectGrammar.add(setOfWord.toString());
					setClassCorrectGrammar.add(setOfClass.toString());
					setSubclassCorrectGrammar.add(setOfSubclass.toString());
				}
				//System.out.println(setSubclassCorrectGrammar);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("Error: " + e.getMessage());
		}
		if(setImgCorrectGrammar.isEmpty() == false){ 	//setImgCorrectGrammar have elements
			//System.out.println(setImgCorrectGrammar);
			returnValueWord.add(setImgCorrectGrammar.toString().trim());
			returnValueClass.add(setClassCorrectGrammar.toString().trim());
			returnValueSubclass.add(setSubclassCorrectGrammar.toString().trim());

			CalculateScore.totalScore(returnValueWord, returnValueClass, returnValueSubclass);
		}
	}
}
