package com.slsatl.aac;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.slsatl.aac.Algor.AlgorStructure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;


public
class ComposePage extends Activity implements TextToSpeech.OnInitListener {

GridView grid_main, grid_select;

LinearLayout linear_cate;

String InputValue;

static int currCid;

static int currCategoryVariation = 0;

static ConfirmDialog confirmDialog;

static Vector<LexIconAndLabel> vocabShow;

static Vector<CatIconAndLabel> cateShow;

ImageButton delSelectButton, speakButton, speakEdit_button;

Button clearBtn;

static TextToSpeech mTts;

static boolean ttsLangOk;

static String speech;

static int buttonResource;

static String THAIspeech;

static String delVocab;

View[] headerElement = new View[ cateShow.size () ];

public List<TextView> tvHeader = new ArrayList<TextView> ();

public List<AlgorStructure> algorStructures = new ArrayList<AlgorStructure> ();

TextView answerTv;

TextView sc1tv;

MenuItem toTtsMItm, helpMItm;

ComposePage thisPage;

public
void LaunchTTS () {
    if ( ! ComposePage.ttsLangOk ) {
	  Toast.makeText ( getApplicationContext (),
				 getApplicationContext ().getString ( R.string.TTS_NO_LANG )
				 + "("
				 + Keeper.locale.toString ()
				 + ")",
				 Toast.LENGTH_SHORT ).show ();
	  return;
    }

    //speech = collectWords(Keeper.selected);
    speech = answerTv.getText ().toString ();
    Log.d ( "selectedWord", speech );
    convertTospeech ( mTts, speech );
}

public static
String collectWords ( Vector<VocabSelected> a ) {
    int textLegth = a.size ();
    String text = "";
    for ( int i = 0 ; i < textLegth ; i++ ) {
	  text = text + a.elementAt ( i ).word + " ";
    }
    return text;
}

public static
void convertTospeech ( TextToSpeech x, String input ) {
    String currLocale = Keeper.locale.toString ();
    if ( currLocale.equals ( "th_th" ) ) {
	  // Remove all whitespaces due to VAJA's bug.
	  input = input.replaceAll ( "\\s", "" );
    }
    String text = input.trim ();
    //Log.e("TTS",input+" : "+currLocale);
    x.speak ( text, TextToSpeech.QUEUE_FLUSH, null );
}


public
void hideAllSubCate () {
    for ( int i = 0 ; i < headerElement.length ; i++ ) {
	  headerElement[ i ].setVisibility ( View.GONE );
    }
}

public
String onClickCallAlgor ( String classStr, String pos, String tag, String subClassStr ) {
    String sortedOrder = "";
    String speech = collectWords ( Keeper.selected );
    Log.d ( "selectedText=", speech );
    String[] testAlgor = {"ddd" , "ssdsf"};
    algorStructures.add ( new AlgorStructure ( classStr, pos, tag, subClassStr ) );

    //if(algorStructures.size()>3)

	String s = MainClass.main ( algorStructures, getApplicationContext () );
char c = 8;
s=s.replace ( "||","," );
	s=s.replaceAll(".*,", "");
    answerTv.setText ( s );
    //sc1tv.setText(classStr);
    return sortedOrder;
}

public
void onClickClear () {
    for ( int i = 0 ; i < 20 ; i++ ) {
	  onClickdelBtn ();
    }
}

public
void onClickHeader ( View view ) {
    hideAllSubCate ();
//s
    int id = view.getId ();

    switch ( id ) {
	  case 0:
		for ( int i = 14 ; i <= 24 ; i++ ) {
		    headerElement[ i - 1 ].setVisibility ( View.VISIBLE );
		}
		break;
	  case 1:
		headerElement[ 11 ].setVisibility ( View.VISIBLE );
		break;
	  case 2:
		headerElement[ 9 ].setVisibility ( View.VISIBLE );
		break;
	  case 3:
		headerElement[ 1 ].setVisibility ( View.VISIBLE );
		break;
	  case 4:
		headerElement[ 10 ].setVisibility ( View.VISIBLE );
		break;
	  case 5:
		headerElement[ 2 ].setVisibility ( View.VISIBLE );
		break;
	  case 6:
		headerElement[ 3 ].setVisibility ( View.VISIBLE );
		headerElement[ 4 ].setVisibility ( View.VISIBLE );
		headerElement[ 5 ].setVisibility ( View.VISIBLE );
		headerElement[ 6 ].setVisibility ( View.VISIBLE );
		break;
	  case 7:
		headerElement[ 26 ].setVisibility ( View.VISIBLE );
		headerElement[ 27 ].setVisibility ( View.VISIBLE );
		break;
	  case 8:
		headerElement[ 24 ].setVisibility ( View.VISIBLE );
		headerElement[ 25 ].setVisibility ( View.VISIBLE );
		break;
	  case 9:

		headerElement[ 7 ].setVisibility ( View.VISIBLE );
		headerElement[ 8 ].setVisibility ( View.VISIBLE );
		break;
	  case 10:
		headerElement[ 0 ].setVisibility ( View.VISIBLE );
		break;
	  case 11:
		headerElement[ 12 ].setVisibility ( View.VISIBLE );
		break;
	  default:
		Log.e ( "Error", "unknown cases" );
		break;

    }
    setTvHeaderBg2White ();
    //tvHeader.get(id).setBackgroundColor(getResources().getColor(R.color.PINK_CHULA));
    showAllHeader ();
    tvHeader.get ( id ).setVisibility ( View.GONE );
    TextView catetvSelected = ( TextView ) findViewById ( R.id.catetvSelected );
    catetvSelected.setText ( tvHeader.get ( id ).getText ().toString () );
    preClick ();

}

public
void onClickItem_After ( String classStr, String pos, String tag, String subClassStr ) {
    onClickCallAlgor ( classStr, pos, tag, subClassStr );
}

public
void onClickdelBtn () {
    if ( Keeper.selected.size () != 0 ) {
	  Keeper.selected.remove ( Keeper.selected.size () - 1 );
	  algorStructures.clear ();
    }
    speech = collectWords ( Keeper.selected );
    answerTv.setText ( "" );
    configureUI2 ();
}

@Override
public
void onCreate ( Bundle savedInstanceState ) {
    super.onCreate ( savedInstanceState );

    thisPage = this;
    requestWindowFeature ( Window.FEATURE_NO_TITLE );
	confirmDialog = new ConfirmDialog ();
		/*
		try {
			ComposePage.cateShow = queryCategory(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Locale locale = Keeper.locale;
    	String currLocale = locale.toString();
    	if(currLocale.equals("th_th")){
    	      currLocale = "th_TH";
    	}
		String [] column = {"cid"};
		Cursor c = Keeper.myDB.query("category", column, "lang ='"+currLocale+"' and enable=1
		", null, null, null,
		"weight");
		c.moveToFirst();
		currCid = c.getInt(c.getColumnIndex("cid"));

		try {
			vocabShow = queryVocabs(currCid,1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
//
    mTts = new TextToSpeech ( this, this );

    setContentView ( R.layout.main_switch );
    sc1tv = ( TextView ) findViewById ( R.id.sc1tv );
    grid_main = ( GridView ) findViewById ( R.id.GridView01 );
    grid_main.setAdapter ( new VocabGridAdapter ( this ) );
    grid_select = ( GridView ) findViewById ( R.id.GridPressed );
    linear_cate = ( LinearLayout ) findViewById ( R.id.linear_cate );
    TextView wordSelected = ( TextView ) findViewById ( R.id.wordSelectedTv );
    answerTv = ( TextView ) findViewById ( R.id.sentenceAnswerTv );
    wordSelected.setTypeface ( null, Typeface.BOLD );
    // Populate linear_cate by inflating each View instance with R.id.cate_icon_image
    LayoutInflater
	  li
	  = ( LayoutInflater ) this.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );

    //Log.d ( "linearCate", "cateSize =" + cateShow.size () );
    for ( int position = 0 ; position < cateShow.size () ; position++ ) {
	  headerElement[ position ] = li.inflate ( R.layout.categridview, null );
	  CatIconAndLabel a = ( CatIconAndLabel ) ComposePage.cateShow.toArray ()[ position ];
	  ImageView
		iv
		= ( ImageView ) headerElement[ position ].findViewById ( R.id.cate_icon_image );
	  iv.setBackgroundDrawable ( a.pic );
	  TextView tv = ( TextView ) headerElement[ position ].findViewById ( R.id.cate_icon_text );
	  tv.setText ( a.word );

	 // Log.d ( "linearCate", "pos=" + position );
	  linear_cate.addView ( headerElement[ position ] );
	  //	..
	  headerElement[ position ].setOnClickListener ( new CateClickListener ( a.word,
													 a.cid,
													 this,
													 grid_main,
													 sc1tv,
													 position ) );
    }

    delSelectButton = ( ImageButton ) findViewById ( R.id.delBtnComposeActivity );
    speakButton = ( ImageButton ) findViewById ( R.id.speak_button );
    speakEdit_button = ( ImageButton ) findViewById ( R.id.widget35 );
    clearBtn = ( Button ) findViewById ( R.id.clearAllBtn );

    clearBtn.setOnClickListener ( new View.OnClickListener () {

	  @Override public
	  void onClick ( View view ) {
		onClickClear ();
	  }
    } );


    // Add onItemClickListener
    grid_main.setOnItemClickListener ( new OnItemClickListener () {

	  public
	  void onItemClick ( AdapterView<?> parent, View v,
				   int position, long id ) {

		if ( Keeper.selected.size () > 5 ) {
		    Toast.makeText ( getApplicationContext (),
					   R.string.PLS_DEL_B4, Toast.LENGTH_SHORT ).show ();
		}
		else {

		    LexIconAndLabel x = ( LexIconAndLabel ) parent.getItemAtPosition ( position );
		    Drawable selectPic = x.pic;
		    String selectWord = x.word;

		    Log.e ( "toto", "selectWord = " + selectWord );

		    //todo run algor

		    String[] column = {"cid" ,
					     "lid" ,
					     "core" ,
					     "enable" ,
					     "tag" ,
					     "pos" ,
					     "classTag" ,
					     "class" ,
					     "subClassTag" ,
					     "subClass" ,
					     "picPath" ,
					     "nextCid" ,
					     "voicePath" ,
					     "weight"};


		    Cursor c = Keeper.myDB.query ( "NewLexicalItem",
							     column,
							     "tag ='"
							     + selectWord + "' ",
							     null,
							     null,
							     null,
							     null );
		    c.moveToFirst ();

		    String getVoice = c.getString ( c.getColumnIndex ( "voicePath" ) );

		    String pos = c.getString ( c.getColumnIndex ( "pos" ) );
		    String classStr = c.getString ( c.getColumnIndex ( "class" ) );
		    String subClassStr = c.getString ( c.getColumnIndex ( "subclass" ) );

		    //Log.d("testGet data  pos,class,subclass",pos+","+classStr+","+subClassStr);

		    int nextCid = c.getInt ( c.getColumnIndex ( "nextCid" ) );
		    if ( nextCid == 0 ) {
			  String[] catColumn = {"nextCid"};
			  Cursor c2 = Keeper.myDB.query ( Constant.TABLE_NEW_CATE,
								    catColumn,
								    "cid ='"
								    + currCid
								    + "' ",
								    null,
								    null,
								    null,
								    null );
			  c2.moveToFirst ();
			  nextCid = c2.getInt ( c2.getColumnIndex ( "nextCid" ) );
		    }

		    Log.e ( "toto", "getVoice = " + getVoice );
		    VocabSelected vocabSelected = new VocabSelected ( selectPic,
											selectWord, getVoice );
			vocabSelected.pos = Keeper.selected.size ();

		    Keeper.selected.add ( vocabSelected );
		    c.close ();
		    configureUI2 ();

		    if ( nextCid != 0 ) {
			  currCid = nextCid;
			  try {
				vocabShow = queryVocabs ( currCid, 1 );
			  }
			  catch ( IOException e ) {
				e.printStackTrace ();
			  }
			  grid_main.setAdapter ( new VocabGridAdapter ( thisPage ) );
		    }
		    onClickItem_After ( classStr, pos, selectWord, subClassStr );
		}


	  }

    } );
    delSelectButton.setOnClickListener ( new View.OnClickListener () {

	  public
	  void onClick ( View v ) {
		onClickdelBtn ();
	  }

    } );
    speakButton.setOnClickListener ( new View.OnClickListener () {

	  public
	  void onClick ( View v ) {
		buttonResource = R.id.speak_button;
		Log.d ( "enterSpeakBtn", "" );
		//onClickCallAlgor(classStr,pos,selectWord,subClassStr);
		LaunchTTS ();
	  }
    } );


    setHeaderBtn ();
    tvHeader.get ( 0 ).performClick ();
    headerElement[ 13 ].performClick ();

}// end of onCreate()

@Override
protected
void onStart () {
    super.onStart ();
    configureUI2 ();
}

@Override
protected
void onDestroy () {
    //Close the Text to Speech Library
    if ( mTts != null ) {

	  mTts.stop ();
	  mTts.shutdown ();
	  Log.d ( "", "TTS Destroyed" );
    }
    super.onDestroy ();
}

@Override
public
boolean onCreateOptionsMenu ( Menu menu ) {

    int toTtsBtnId = Menu.FIRST;
    int helpBtnId = Menu.FIRST + 1;

    toTtsMItm = menu.add ( Menu.NONE, toTtsBtnId, toTtsBtnId, getString ( R.string.TTS ) );
    toTtsMItm.setIcon ( android.R.drawable.ic_menu_edit );

    helpMItm = menu.add ( Menu.NONE, helpBtnId, helpBtnId, getString ( R.string.HELP ) );
    helpMItm.setIcon ( android.R.drawable.ic_menu_help );

    return super.onCreateOptionsMenu ( menu );

}

@Override
public
boolean onOptionsItemSelected ( MenuItem item ) {
    switch ( item.getItemId () ) {
	  case Menu.FIRST: // go to TTS page
		launchTTSPage ();
		break;
	  case Menu.FIRST + 1: // help
		launchHelpPage ();
		break;
    }
    return true;
}

protected
void launchTTSPage () {
    Intent i = new Intent ( this, TTSPage.class );
    startActivity ( i );
}

protected
void launchHelpPage () {
    Intent i = new Intent ( this, HelpPage.class );
    HelpPage.currHelpTabId = "Compose";
    startActivity ( i );
}

private
void configureUI2 () {
    /*grid_select.setOnItemLongClickListener ( new AdapterView.OnItemLongClickListener () {

	  @Override public
	  boolean onItemLongClick ( AdapterView<?> adapterView, View view, int i, long l ) {

		showConfirmDeleteDialog (view);
		return false;
	  }


    } );*/
    grid_select.setAdapter ( new ImageAdapterSelect ( this ) );
}

public void showConfirmDeleteDialog ( final View view){

    ConfirmDialog.show (this,
				getString( R.string.confirm_delete),
				new ConfirmDialog.ConfirmListener () {

				    @Override public
				    void onConfirm ( String key ) {
							//todo need to remove via Id which store the position, and remove in algorStructure

							view.setVisibility ( View.GONE );
							//Log.d ( "viewId", String.valueOf ( view.getId () ) );
							//Keeper.selected.remove ( view.getId ()-1);
							algorStructures.clear ();
						}
					/*speech = collectWords ( Keeper.selected );
					answerTv.setText ( "" );
							configureUI2 ();*/

				},
				"key" );
}

@Override
public
void onInit ( int status ) {
    // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
    if ( status == TextToSpeech.SUCCESS ) {
	  // Set preferred language to US english.
	  // Note that a language may not be available, and the result will
	  // indicate this.
	  // **************** write check method for EN or TH********//


	  int result = mTts.setLanguage ( Keeper.locale );

	  // int result mTts.setLanguage(Locale.FRANCE);
	  if ( result == TextToSpeech.LANG_MISSING_DATA
		 || result == TextToSpeech.LANG_NOT_SUPPORTED ) {
		// Lanuage data is missing or the language is not supported.
		Toast.makeText ( getApplicationContext (),
				     getApplicationContext ().getString ( R.string.TTS_NO_LANG )
				     + "("
				     + Keeper.locale.toString ()
				     + ")",
				     Toast.LENGTH_SHORT ).show ();
		//Log.e("TAG", "Language is not available.");
		ComposePage.ttsLangOk = false;
	  }
	  else {
		ComposePage.ttsLangOk = true;
	  }

    }
    else {
	  // Initialization failed.
	  //Log.e("TAG", "Could not initialize TextToSpeech.");
	  Toast.makeText ( getApplicationContext (),
				 getApplicationContext ().getString ( R.string.TTS_INIT_ERR ),
				 Toast.LENGTH_SHORT ).show ();
    }

}

public
void preClick () {

    for ( int i = 0 ; i < headerElement.length ; i++ ) {
	  if ( headerElement[ i ].getVisibility () == View.VISIBLE ) {
		headerElement[ i ].performClick ();
		break;
	  }
    }
}

public static
Vector<CatIconAndLabel> queryCategory ( int enableMode ) throws IOException {
    //ComposePage.cateShow = new Vector <IconAndLabel>();
    Vector<CatIconAndLabel> retrievedCat = new Vector<CatIconAndLabel> ();
    String[] column = {"cid" ,
			     "title" ,
			     "subtitle" ,
			     "coverPath" ,
			     "variation" ,
			     "nextCid" ,
			     "enable" ,
			     "mainClassID" ,
			     "classTag" ,
			     "subClassTag"};

    Locale locale = Keeper.locale;
    String currLocale = locale.toString ();
    if ( currLocale.equals ( "th_th" ) ) {
	  currLocale = "th_TH";
    }

    currLocale = "th_TH";
    Cursor c;
    if ( enableMode == 1 ) {
	  c =
		Keeper.myDB.query ( Constant.TABLE_NEW_CATE,
					  column,
					  "lang ='" + currLocale + "' and enable=1 ",
					  null,
					  null,
					  null,
					  "weight" );
    }
    else if ( enableMode == 0 ) {
	  c =
		Keeper.myDB.query ( Constant.TABLE_NEW_CATE,
					  column,
					  "lang ='" + currLocale + "' and enable=0 ",
					  null,
					  null,
					  null,
					  "weight" );
    }
    else {
	  c = Keeper.myDB.query ( Constant.TABLE_NEW_CATE,
					  column,
					  "lang ='" + currLocale + "' ",
					  null,
					  null,
					  null,
					  "weight" );
    }

    for ( c.moveToFirst (); ! c.isAfterLast () ; c.moveToNext () ) {
	  int cidIndexColumn = c.getColumnIndex ( "cid" );
	  int titleIndexColumn = c.getColumnIndex ( "title" );
	  int subtitleIndexColumn = c.getColumnIndex ( "subtitle" );
	  int coverPathIndexColumn = c.getColumnIndex ( "coverPath" );
	  int variationIndexColumn = c.getColumnIndex ( "variation" );
	  int nextCidIndexColumn = c.getColumnIndex ( "nextCid" );
	  int enableIndexColumn = c.getColumnIndex ( "enable" );

	  int cid = c.getInt ( cidIndexColumn );
	  String title = c.getString ( titleIndexColumn );
	  String subtitle = c.getString ( subtitleIndexColumn );
	  int variation = c.getInt ( variationIndexColumn );
	  int nextCid = c.getInt ( nextCidIndexColumn );
	  int enable = c.getInt ( enableIndexColumn );
	  String coverPath;
	  if ( c.getString ( coverPathIndexColumn ) == null || c.getString ( coverPathIndexColumn )
										  .trim ()
										  .equals ( "" ) ) {
		coverPath = "sdcard/AAConAndroid/aac_system_no_pic.gif";
	  }
	  else {
		coverPath = "sdcard/AAConAndroid/" + c.getString ( coverPathIndexColumn );
	  }
	  retrievedCat.add ( new CatIconAndLabel ( cid,
								 title,
								 subtitle,
								 coverPath,
								 variation,
								 nextCid,
								 enable ) );
    }
    c.close ();
    return retrievedCat;
}

public static
Vector<LexIconAndLabel> queryVocabs ( int cid, int enableMode ) throws IOException {
    Vector<LexIconAndLabel> retrievedLex = new Vector<LexIconAndLabel> ();
    String[] column = {"lid" , "tag" , "picPath" , "voicePath" , "nextCid" , "enable"};
    Cursor c;
    if ( enableMode == 1 ) {
	  c = Keeper.myDB.query ( "NewLexicalItem",
					  column,
					  " cid=" + cid + " AND enable=1 ",
					  null,
					  null,
					  null,
					  "weight" );
    }
    else if ( enableMode == 0 ) {
	  c = Keeper.myDB.query ( "NewLexicalItem",
					  column,
					  " cid=" + cid + " AND enable=0 ",
					  null,
					  null,
					  null,
					  "weight" );
    }
    else {
	  c = Keeper.myDB.query ( "NewLexicalItem",
					  column,
					  " cid=" + cid,
					  null,
					  null,
					  null,
					  "weight" );
    }
    for ( c.moveToFirst (); ! c.isAfterLast () ; c.moveToNext () ) {
	  int lidIndexColumn = c.getColumnIndex ( "lid" );
	  int tagIndexColumn = c.getColumnIndex ( "tag" );
	  int picPathIndexColumn = c.getColumnIndex ( "picPath" );
	  int voicePathIndexColumn = c.getColumnIndex ( "voicePath" );
	  int nextCidIndexColumn = c.getColumnIndex ( "nextCid" );
	  int enableIndexColumn = c.getColumnIndex ( "enable" );

	  int lid = c.getInt ( lidIndexColumn );
	  String tag = c.getString ( tagIndexColumn );
	  String voicePath = c.getString ( voicePathIndexColumn );
	  int nextCid = c.getInt ( nextCidIndexColumn );
	  int enable = c.getInt ( enableIndexColumn );
	  String picPath;
	  if ( c.getString ( picPathIndexColumn ) == null || c.getString ( picPathIndexColumn )
										.trim ()
										.equals ( "" ) ) {
		picPath = "sdcard/AAConAndroid/aac_system_no_pic.gif";
	  }
	  else {
		picPath = "sdcard/AAConAndroid/" + c.getString ( picPathIndexColumn );
	  }
	  retrievedLex.add ( new LexIconAndLabel ( lid,
								 cid,
								 tag,
								 picPath,
								 voicePath,
								 nextCid,
								 enable ) );
    }
    c.close ();
    return retrievedLex;
}

public static
String removeSpaces ( String s ) {
    String noSpace = s.replaceAll ( "\\W", "" );
    return noSpace;
}

public
void setHeaderBtn () {

    TextView catetvSelected = ( TextView ) findViewById ( R.id.catetvSelected );
    catetvSelected.setTypeface ( null, Typeface.BOLD );

    tvHeader.add ( ( TextView ) findViewById ( R.id.cate1tv ) );
    tvHeader.add ( ( TextView ) findViewById ( R.id.cate2tv ) );
    tvHeader.add ( ( TextView ) findViewById ( R.id.cate3tv ) );
    tvHeader.add ( ( TextView ) findViewById ( R.id.cate4tv ) );
    tvHeader.add ( ( TextView ) findViewById ( R.id.cate5tv ) );
    tvHeader.add ( ( TextView ) findViewById ( R.id.cate6tv ) );
    tvHeader.add ( ( TextView ) findViewById ( R.id.cate7tv ) );
    tvHeader.add ( ( TextView ) findViewById ( R.id.cate8tv ) );
    tvHeader.add ( ( TextView ) findViewById ( R.id.cate9tv ) );
    tvHeader.add ( ( TextView ) findViewById ( R.id.cate10tv ) );
    tvHeader.add ( ( TextView ) findViewById ( R.id.cate11tv ) );
    tvHeader.add ( ( TextView ) findViewById ( R.id.cate12tv ) );

    for ( int i = 0 ; i < tvHeader.size () ; i++ ) {
	  tvHeader.get ( i ).setOnClickListener ( new View.OnClickListener () {

		@Override public
		void onClick ( View view ) {
		    onClickHeader ( view );
		}
	  } );
	  tvHeader.get ( i ).setId ( i );
	  tvHeader.get ( i ).setTypeface ( null, Typeface.BOLD );
    }
}

public
void setTvHeaderBg2White () {
    for ( int i = 0 ; i < tvHeader.size () ; i++ ) {

	  tvHeader.get ( i ).setBackgroundColor ( getResources ().getColor ( R.color.WHITE ) );
	  Drawable drawable = getResources ().getDrawable ( R.drawable.boundary );
	  //tvHeader.get(i).setDrawables(drawable,drawable,drawable,drawable);
	  //linear_cate.setBackgroundDrawable(drawable);
    }
}

public
void showAllHeader () {
    for ( int i = 0 ; i < tvHeader.size () ; i++ ) {
	  tvHeader.get ( i ).setVisibility ( View.VISIBLE );
    }
}

}
