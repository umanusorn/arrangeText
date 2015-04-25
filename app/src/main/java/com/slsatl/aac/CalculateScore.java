package com.slsatl.aac;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CalculateScore {
	public static void totalScore(List<String> imgSet, List<String> classSet, List<String> subclassSet) {

		String strWord = "";
		String strClass = "";
		String strSubclass = "";
		int countCompare = 0;
		String lineWord = null;
		String lineClass = null;
		String lineSubclass = null;
		
		List<String> scoreWordGram = new ArrayList<String>();
		List<String> scoreClassGram = new ArrayList<String>();
		List<String> scoreSubclassGram = new ArrayList<String>();

		List<String> imgWordGram = new ArrayList<String>();
		List<String> imgClassGram = new ArrayList<String>();
		List<String> imgSubclassGram = new ArrayList<String>();

		List<String> newWordSet = new ArrayList<String>();
		List<String> newClassSet = new ArrayList<String>();
		List<String> newSubclassSet = new ArrayList<String>();
		
		int count = 0;
		float totalScoreWord = (float) 0;
		float totalScoreClass = (float) 0;
		float totalScoreSubclass = (float) 0;

		String[] imgSetCalWord = {};
		String[] imgSetCalClass = {};
		String[] imgSetCalSubclass = {};

		for (int a = 0; a < imgSet.size(); a = a + 1) {
			System.out.print(imgSet.get(a));

			strWord = imgSet.get(a).replace("[", "");
			strWord = strWord.replace("]", "");

			newWordSet.add(strWord);

			if (newWordSet.isEmpty() == false) {
				imgSetCalWord = newWordSet.get(a).split(",");
			}
		}

		for (int j = 0; j < MainClass.imgWord.size(); j++) {
			imgSetCalWord[j] = imgSetCalWord[j].trim();
		}

		// --------------------------------------------------------------------------------

		for (int a = 0; a < classSet.size(); a = a + 1) {
			//System.out.print(classSet.get(a));

			strClass = classSet.get(a).replace("[", "");
			strClass = strClass.replace("]", "");

			newClassSet.add(strClass);

			if (newClassSet.isEmpty() == false) {

				imgSetCalClass = newClassSet.get(a).split(",");
			}
		}

		for (int j = 0; j < MainClass.imgWord.size(); j++) {
			imgSetCalClass[j] = imgSetCalClass[j].trim();
		}
		
		// --------------------------------------------------------------------------------

		for (int a = 0; a < subclassSet.size(); a = a + 1) {
			System.out.print(subclassSet.get(a)+"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			strSubclass = subclassSet.get(a).replace("[", "");
			strSubclass = strSubclass.replace("]", "");
			newSubclassSet.add(strSubclass);

			if (newSubclassSet.isEmpty() == false) {
				imgSetCalSubclass = newSubclassSet.get(a).split(",");
			}
		}

		for (int j = 0; j < MainClass.imgWord.size(); j++) {
			imgSetCalSubclass[j] = imgSetCalSubclass[j].trim();
		}

		// ---------------------------------------------------------------------------------

		
		

		// ------------------------------------2-Gram of word-----------------------------------------------
		for (int i = 0; i < MainClass.imgWord.size() - 1; i++) {
			imgWordGram.add(imgSetCalWord[i] +","+ imgSetCalWord[i + 1]);
			
			Set setWord = MainClass.hashtable_Word.entrySet();
		    Iterator it_Word = setWord.iterator();
		    while (it_Word.hasNext()) {
		    	Map.Entry entry = (Map.Entry) it_Word.next();
		    	
		    	//System.out.println("imgWordGram :"+imgWordGram.get(i));
		    	//System.out.println("entry.getKey :"+entry.getKey());
		    	if (entry.getKey().equals(imgWordGram.get(i))) {
		    		
					scoreWordGram.add("String: " + imgWordGram.get(i) + "\t score: " + entry.getValue());
					totalScoreWord = totalScoreWord + Float.parseFloat((String) entry.getValue());
					System.out.println(imgWordGram.get(i)+"***************");
		    	}
				else{
					totalScoreWord = totalScoreWord-1;
				}
		    }
		}
		System.out.println(scoreWordGram);
		System.out.println("totalScoreWord: " + totalScoreWord);

			// ------------------------------------2-Gram of Class-----------------------------------------------
		for (int j = 0; j < MainClass.imgWord.size() - 1; j++) {
			imgClassGram.add(imgSetCalClass[j] +","+ imgSetCalClass[j + 1]);
			
			Set setClass = MainClass.hashtable_Subclass.entrySet();
		    Iterator it_Class = setClass.iterator();
		    while (it_Class.hasNext()) {
		    	Map.Entry entry = (Map.Entry) it_Class.next();
		    	
		    	//System.out.println("imgClassGram :"+imgClassGram.get(j));
		    	//System.out.println("entry.getKey :"+entry.getKey());
		    	if (entry.getKey().equals(imgClassGram.get(j))) {
		    		System.out.println("Fon***********************************************************");
					scoreClassGram.add("String: " + imgClassGram.get(j) + "\t score: " + entry.getValue());
					totalScoreClass = totalScoreClass + Float.parseFloat((String) entry.getValue());
		    	}
				else{
					totalScoreClass = totalScoreClass-1;
				}
		    }
		}
		    
		System.out.println(scoreClassGram);
		System.out.println("totalScoreClass: " + totalScoreClass);

		// ------------------------------------2-Gram of Subclass-----------------------------------------------
		for (int j = 0; j < MainClass.imgWord.size() - 1; j++) {
			imgSubclassGram.add(imgSetCalSubclass[j] +","+ imgSetCalSubclass[j + 1]);
			//System.out.println(imgSetCalSubclass[j] +","+ imgSetCalSubclass[j + 1]+"------------------------------------");
			
			Set setSubclass = MainClass.hashtable_Class.entrySet();
		    Iterator it_Subclass = setSubclass.iterator();
		    while (it_Subclass.hasNext()) {
		    	Map.Entry entry = (Map.Entry) it_Subclass.next();
		    	//System.out.println("imgSubclassGram :"+imgSubclassGram.get(j));
		    	//System.out.println("entry.getKey :"+entry.getKey());
		    	if (entry.getKey().equals(imgSubclassGram.get(j))) {
		    		//System.out.println("Fon***********************************************************");
					scoreSubclassGram.add("String: " + imgSubclassGram.get(j) + "\t score: " + entry.getValue());
					totalScoreSubclass = totalScoreSubclass + Float.parseFloat((String) entry.getValue());
		    	}
				else{
					totalScoreSubclass = totalScoreSubclass-1;
				}
		    }
		}
		    
		System.out.println(scoreSubclassGram);
		System.out.println("totalScoreSubclass: " + totalScoreSubclass);
		
		Float totalAllScore = totalScoreWord + totalScoreClass + totalScoreSubclass;
		System.out.println("TotalAllScore: " + totalAllScore);
		
		SortingScore.Sorting(totalAllScore, strWord.replace(",", ""), countCompare);
	}
	
}
