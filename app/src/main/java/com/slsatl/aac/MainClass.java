package com.slsatl.aac; /**
 * Created by Worasa on 16/1/2558.
 */

import android.content.Context;
import android.util.Log;

import com.slsatl.aac.Algor.AlgorStructure;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MainClass {
static String TAG = "MainClass";
String strSetImg      = "";
String strSetPOS      = "";
String strSetClass    = "";
String strSetSubclass = "";

static List<String> imgPOS;
static List<String> imgWord;
static List<String> ansSentence;

static List<String> listWord     = new ArrayList<String>();
static List<String> listSubclass = new ArrayList<String>();
static List<String> listClass    = new ArrayList<String>();
static List<String> listPOS      = new ArrayList<String>();

static Hashtable hashtable_Word     = new Hashtable();
static Hashtable hashtable_POS      = new Hashtable();
static Hashtable hashtable_Class    = new Hashtable();
static Hashtable hashtable_Subclass = new Hashtable();


public static String main(List<AlgorStructure> algorStructures, Context context) {

	String lineWord = null;
	String lineSubclass = null;
	String lineClass = null;
	String linePOS = null;
	FileInputStream fstream;

	imgPOS = new ArrayList<String>();
	imgWord = new ArrayList<String>();
	ansSentence = new ArrayList<String>();

	Log.d(TAG, "pass1");
	imgWord.clear();
	ansSentence.clear();
/*

//Read text from file
	    StringBuilder text = new StringBuilder();

	    try {
		    BufferedReader br = new BufferedReader(new FileReader(file));
		    String line;

		    while ((line = br.readLine()) != null) {
			    text.append(line);
			    text.append('\n');
		    }
		    br.close();
	    }
*/


	try {

String fileName = "Word_Score.txt";
		BufferedReader readerWordGram = new BufferedReader(new InputStreamReader ( context.getAssets().open(fileName) ));
		String strLineWord = "";
		while ((lineWord = readerWordGram.readLine()) != null) {
			String[] parts = lineWord.split(",");

			for (int j = 0; j < parts.length; j++) {
				strLineWord = parts[0] + "," + parts[1];
				hashtable_Word.put(strLineWord, parts[2]);
			}
		}

	//	Log.d(TAG, "pass2");

		fileName= "POS_Score.txt";
		BufferedReader readerPOSGram = new BufferedReader(new InputStreamReader ( context.getAssets().open(fileName) ));

	//	Log.d(TAG, "pass3");
		String strLinePOS = "";
		while ((linePOS = readerPOSGram.readLine()) != null) {
			String[] parts = linePOS.split(",,");

			for (int j = 0; j < parts.length; j++) {
				strLinePOS = parts[0];
				hashtable_POS.put(strLinePOS, parts[1]);
			}
		}
		//Log.d(TAG, "pass4");
		fileName = "Subclass_Score.txt";
		BufferedReader readerSubclassGram = new BufferedReader(new InputStreamReader ( context.getAssets().open(fileName) ));
		String strLineSubclass = "";

	//	Log.d(TAG, "pass5");
		while ((lineSubclass = readerSubclassGram.readLine()) != null) {
			String[] parts = lineSubclass.split(",");

			for (int j = 0; j < parts.length; j++) {
				strLineSubclass = parts[0] + "," + parts[1];
				hashtable_Subclass.put(strLineSubclass, parts[2]);
			}
		}
	//	Log.d(TAG, "pass6");

		fileName="Class_Score.txt";
		BufferedReader readerClassGram = new BufferedReader(new InputStreamReader ( context.getAssets().open(fileName) ));
		String strLineClass = "";
		Log.d(TAG, "pass7");
		while ((lineClass = readerClassGram.readLine()) != null) {
			String[] parts = lineClass.split(",");

			for (int j = 0; j < parts.length; j++) {
				strLineClass = parts[0] + "," + parts[1];
				hashtable_Class.put(strLineClass, parts[2]);
			}
		}
		Log.d(TAG, "pass8");
//check hash table elements
//			Set set = hashtable_Class.entrySet();
//		    Iterator it = set.iterator();
//		    while (it.hasNext()) {

//		      Map.Entry entry = (Map.Entry) it.next();
//		      System.out.println(entry.getKey() + " : " + entry.getValue());
//		    }

	}
	catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

//------------------------------------------Already Created Hash table---------------------------------------------//
	long tStart = System.currentTimeMillis();

	//Log.d(TAG, "pass9");
//		try {
//
//			fstream = new FileInputStream("test4word.txt");
//			DataInputStream in = new DataInputStream(fstream);
//			BufferedReader br = new BufferedReader(new InputStreamReader(in));
//			String strLine;
//
//			while ((strLine = br.readLine()) != null) {
//				//System.out.println(strLine);
//				String[] parts = strLine.split(",,");
//
//				for (int i = 0; i < parts.length; i++) {
////					System.out.println(parts[i]);
//					imgWord.add(parts[i]);
//
//				}


//      subclass,pos,tag,class

	for (int i = 0; i < algorStructures.size(); i++) {
		AlgorStructure word = algorStructures.get(i);
		String structuredString = word.getStructuredString();
		Log.d("structuredString = ",structuredString);
		imgWord.add(structuredString);
	}
	
	/*imgWord.add("VERB2,V,เตะ,MOVEMENT");
	imgWord.add("SPORT,N,ฟุตบอล,WHAT");
	imgWord.add("SIZE,ADJ,ใหญ่,SIZE");
	imgWord.add("FAMILY,N,ปู่,WHAT");*/

	//Log.d(TAG, "pass10");
	System.out.println(imgWord);
	Log.d ( TAG, "pass11" + imgWord );
	PermutationAndReadGrammar.ImgPermutation(imgWord, 0,context);
	Log.d(TAG, "pass12" + imgWord);
	ansSentence.clear();
	for (int j = 0; j < SortingScore.allScore.size(); j++) {
		ansSentence.add(SortingScore.allScore.get(j));

	}

// todo show the answer string in textView
	try{
		Log.d(TAG + "view Answer", ansSentence.get(0));
	}catch (Exception e){
		//todo update text
		return "คำที่ให้มาผิดหลักไวยากรณ์";
	}

	Log.d(TAG, "pass13" + imgWord);
	imgWord.clear();

	//	}
//			File file = new File("test4word_Score.txt");
//            if (!file.exists()) {
//				file.createNewFile();
//			}
//			// true = append file
//			FileWriter fileWritter = new FileWriter(file.getName(), true);
//			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
//
//			for (int j = 0; j < ansSentence.size(); j++) {
//				bufferWritter.write(ansSentence.get(j));
//				bufferWritter.newLine();
//			}
//			bufferWritter.close();
//			System.out.println("Done");
//
//			in.close();

//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			System.err.println("Error: " + e.getMessage());
//		}

	long tEnd = System.currentTimeMillis();
	long tDelta = tEnd - tStart;
	double elapsedSeconds = tDelta / 1000.0;
	Log.d(TAG, "pass14" + imgWord + "Using times: " + elapsedSeconds);
	System.out.println ( "Using times: " + elapsedSeconds );


	String ans = ansSentence.get(0);
		imgWord.clear();
		ansSentence.clear();
	SortingScore.allScore.clear();
	//Log.d ( "answer=",ans );
	return ans;
}
}
