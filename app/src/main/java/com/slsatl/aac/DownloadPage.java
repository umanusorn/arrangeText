package com.slsatl.aac;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.json.*;

import com.slsatl.aac.MainPage.PrepareComposePage;
//import com.test.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DownloadPage extends Activity{
	
	static Context ncontext;
    private static ArrayList<BitmapAndLabel> m_orders = null;
    private DownloadAdapter m_adapter;
    private LinearLayout lockPane;

    private ListView lv; 

    String downloadCate = "IwannaDownloadThiscate";
    Vector < BitmapAndLabel> cc;
    Vector <SearchResult> searchResult;
    static Bitmap vv;
	String inputValue;
	String searchBy;

	private RadioGroup mRadioGroup;
	EditText searchInput;
	ImageButton searchButton, delSearchButton;
	HttpGet request;
	Button download;
	
	AdapterListViewData adapterListViewData;
	ArrayList<DataShow> listData = new ArrayList<DataShow>(); 
	ListView listViewData;
	ArrayList<CategoryObject> listResultDB = new ArrayList<CategoryObject>();
	 
	String dbName = "aac_1_03.sqlite3";
	String categoryRequest_url= "http://www.sls-atl.com/?q=aac/webservice&action=categories";
	String baseKeywordUrlRequest = "http://www.sls-atl.com/?q=aac/webservice&action=categories&keyword=";
	String downloadBasePath_url = "http://www.sls-atl.com/?q=aac%2Fwebservice&action=download&cid=";
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.downloadview);
        setTitle(R.string.ACT_TITLE_DOWNLOAD);
        
        ncontext = getApplicationContext();
        
        lockPane = (LinearLayout)findViewById(R.id.downloadLayout);     
        
       	listViewData = (ListView)findViewById(R.id.listViewData);
       	
       	adapterListViewData = new AdapterListViewData(getBaseContext(),listData);
		listViewData.setAdapter(adapterListViewData);
        /*
        boolean canConnect=false;
       	BasicHttpParams httpParams = new BasicHttpParams();
        ConnManagerParams.setTimeout(httpParams, 1000l);
        HttpClient client = new DefaultHttpClient( httpParams);
    	String result ="";
    	request = new HttpGet(categoryRequest_url);
    	
    	try {
    	  	
    	  result=getHtmlResult(client.execute(request));
    	  canConnect=true;
		
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       	
       	
   		listData.clear();
   		listResultDB.clear();
   		
   	if(canConnect){
		printTable();
		
   		try {
   			result = "["+result+"]";
   			
			JSONArray data_raw = new JSONArray(result);
			JSONObject d1 = data_raw.getJSONObject(0);
			
			String base_path = d1.getString("aac-pict-url");
			//Log.i ("info", base_path);
			String category = d1.getString("category");
			//Log.i ("info", category);
			
			JSONArray data = new JSONArray(category);
			for(int i = 0; i < data.length(); i++){
			
				JSONObject c = data.getJSONObject(i);
				String title = c.getString("title");
				//Log.i ("info", title);
				
				String version = c.getString("version");
				String coverPath = c.getString("coverPath");
				
				String subtitle=c.getString("subtitle");
				String cid=c.getString("cid");
				String nextCid=c.getString("nextCid");
				String core=c.getString("core");
				String variation=c.getString("variation");
				String weight=c.getString("weight");
				String lang=c.getString("lang");
				String enable=c.getString("enable");
				
				String clientVersion=findVersion(cid);
				String clientTitle=findTitle(cid);
				String clientSubtitle=findSubtitle(cid);
				
				//listData.add(new DataShow(true,false,cid,title,subtitle,version,base_path,coverPath,weight,lang,variation,nextCid,enable,core,clientVersion,clientTitle,clientSubtitle));
				
				if(clientTitle.equals("")||clientTitle==""){//new record
						Log.i ("info", "New record "+title + " " + cid);
						listData.add(new DataShow(true,false,cid,title,subtitle,version,base_path,coverPath,weight,lang,variation,nextCid,enable,core,clientVersion,clientTitle,clientSubtitle));
				
				}
				else{//found in local database
						if(version.equals(clientVersion)||version==clientVersion){
							Log.i ("info", "Old record:Not checkbox "+title + " " + cid);
							listData.add(new DataShow(false,true,cid,title,subtitle,version,base_path,coverPath,weight,lang,variation,nextCid,enable,core,clientVersion,clientTitle,clientSubtitle));
					    }
					    else{
					    	Log.i ("info", "Old recoed: checkbox "+title + " " + cid);
					    	listData.add(new DataShow(false,false,cid,title,subtitle,version,base_path,coverPath,weight,lang,variation,nextCid,enable,core,clientVersion,clientTitle,clientSubtitle));
					    
					    }
				}
			   
			    
			}
			}
			
			catch(Exception e) {
				Log.i ("info", "error from code");
				
			}
   		}
        	
   			adapterListViewData = new AdapterListViewData(getBaseContext(),listData);
   			listViewData.setAdapter(adapterListViewData);
       */
 
    
       /////
       
        searchButton = (ImageButton)findViewById(R.id.search_button);
        delSearchButton = (ImageButton)findViewById(R.id.clearSearch_button);
        searchInput = (EditText)findViewById(R.id.search_txt);
        download = (Button)findViewById(R.id.button1);
        //mRadioGroup = (RadioGroup) findViewById(R.id.menu);
        //RadioButton RadioButton1 = (RadioButton) findViewById(R.id.ChooseVocab);
        //RadioButton RadioButton2 = (RadioButton) findViewById(R.id.chooseCate);
        //TextView searchBytxt = (TextView)findViewById(R.id.choice);
        //RadioButton1.setText(R.string.CHOOSE_VOCAB);
        //RadioButton1.toggle();
        //RadioButton2.setText(R.string.CHOOSE_CATE);
        //searchBytxt.setText(R.string.SEARCH_BY);
        //mRadioGroup.setOnCheckedChangeListener(this);
        //searchBy =RadioButton1.getText().toString();
        
        
        
        download.setOnClickListener(new View.OnClickListener() {
            
			public void onClick(View v) {
            	String query ="%5B";
            	//SQLiteDatabase db = openOrCreateDatabase( dbName, SQLiteDatabase.CREATE_IF_NECESSARY, null);
            	boolean isDownload=false;
            	for(int i=0;i<listData.size();i++){
            		DataShow temp = (DataShow)listData.get(i);
            		String old = (String)temp.getVersion();
            		String cur  = (String)temp.getClientVersion();
            		String title= (String)temp.getTitle();
            		String enable = temp.getEnable();
            		String core = temp.getCore();
            		String nextCid = temp.getNextCid();
            		String variation = temp.getVariation();
            		String subtitle=temp.getSubtitle();
            		String cid = temp.getCid();
            		String weight = temp.getWeight();
            		String coverPath = temp.getCoverPath();
            		String lang = temp.getLang();
            		
            		boolean isNew = temp.IsNew();
            		boolean isCheck = temp.isCheck();
            		
            		if(isNew&&isCheck){
            			query+=cid+"%2C";
            			Log.i ("info", "cid one "+cid);
            			isDownload =true;
            			//insert Category table with all
            			/*
            			try{
            				String sql ="insert into category (enable,core,nextCid,variation,subtitle,cid,version,weight,title,coverPath,lang) values("+enable+","+core+","+nextCid+","+variation+","+"'"+subtitle+"',"+cid+","+old+","+weight+",'"+title+"','"+coverPath+"','"+lang+"')";
            				db.execSQL(sql);
            			}
            			catch(Exception e){
            				
            			}
            			*/
            			
            		}
            			
            		if(!isNew&&!old.equals(cur)&&isCheck){
            			query+=cid+"%2C";
            			Log.i ("info", "cid two "+cid);
            			isDownload =true;
            			//Update Category table with all
            			//Delete all lexical 
            			/*
            			try{
            				String sql ="update category set enable="+enable+",core="+core+",nextCid="+nextCid+",variation="+variation+",subtitle="+"'"+subtitle+"',version="+old+",weight="+weight+",title='"+title+"',coverPath='"+coverPath+"',lang='"+lang+"' where cid="+cid;
                        	db.execSQL(sql);
                        
                        	sql = "delete from lexicalItem where cid="+cid;
                   	  		db.execSQL(sql);
            			}
            			catch(Exception e){
            				
            			}
            			*/
            		}
            		
            	
            	}
            	//db.close();
            	if(query.substring(query.length()-1).equals("C")){
            		query = query.substring(0,query.length()-3);
            		
            	}
            	query+="%5D";
            	
            	if(isDownload){
            		new fetchIconResult().execute(query);
            	}else{
            		AlertDialog alertDialog = new AlertDialog.Builder(DownloadPage.this).create();
             		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
             		alertDialog.setTitle(R.string.DIALOG_TITLE_NO_CAT_CHECKED);
             		alertDialog.setMessage(getString(R.string.DIALOG_MSG_NO_CAT_CHECKED));
             		alertDialog.setButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
             			public void onClick(DialogInterface dialog, int which) {
         
             			} });
             		alertDialog.show();
            	}
            	/*
            	File file = new File(Environment.getExternalStorageDirectory() + "/AAConAndroid/aacpic.zip");
            	File file1 = new File(Environment.getExternalStorageDirectory() + "/AAConAndroid/aac.png");
            	File file2 = new File(Environment.getExternalStorageDirectory() + "/AAConAndroid/1.jpg");
            	boolean deleted = file.delete();
            	boolean deleted1 = file1.delete();
            	boolean deleted2 = file2.delete();
            	*/
            	
            }
        });
    
        
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {       	
            	new SearchingTask().execute();
            	lockPane.setClickable(false);    
            }
        });
        
        delSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	searchInput.setText(null);
            }
        });
        
        
        
    
        
    }
    
    private String findSubtitle(String id) {
    	boolean found = false;
		String result="";
    	for(int i = 0;i<listResultDB.size();i++){
			CategoryObject a = listResultDB.get(i);
			String tempSubtitle = a.getSubtitle();
			String tempID=a.getCid();
			String version = a.getVersion();
			
			if(tempID.equals(id)){
				found=true;
				result=tempSubtitle;
				break;
				
			}
		
		}
    	if(found)return result;
    	else return "";
	}

	private String findTitle(String id) {
		boolean found = false;
		String result="";
    	for(int i = 0;i<listResultDB.size();i++){
			CategoryObject a = listResultDB.get(i);
			String tempTitle = a.getTitle();
			String tempID=a.getCid();
			String version = a.getVersion();
			
			if(tempID.equals(id)){
				found=true;
				result=tempTitle;
				break;
			}
		
		}
    	if(found)return result;
    	else return "";
	}

	private String findVersion(String id) {
		boolean found = false;
		String result="";
		for(int i = 0;i<listResultDB.size();i++){
			CategoryObject a = listResultDB.get(i);
			String tempID = a.getCid();
			String version = a.getVersion();
			
			if(tempID.equals(id)){
				found=true;
				result=version;
				break;
			}
		
		}
		if(found)return result;
    	else return "";
	}

    public void printTable(){
    	Log.i("info","printTable start");
    	
    	// select database
    	SQLiteDatabase db = openOrCreateDatabase( dbName, SQLiteDatabase.CREATE_IF_NECESSARY, null);
    	try{
	    
    		//modify your query here
	   	  	Cursor cursor = db.query("category", new String[] { "enable","core","nextCid","variation","subtitle","cid","version","weight","title","coverPath","lang" }, null,null, null, null, null, null);
	   	 		 
	   	  	if(cursor != null){
	       	  	if (cursor.moveToFirst())  {
	       	  		int i = 0;
	       	  		// move to each row
	       	  		while(true){
	       	  			// move to each column
	       	  			// u can use 2 dimensions array to keep results 
	       	  			for (int j = 0; j < cursor.getColumnCount(); j++){
	       	  				Log.i("info",i + " " + j + " is " + cursor.getString(j));
	       	  				
	       	  			}
	       	  			
	       	  			listResultDB.add(new CategoryObject(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10)));
	       	  			if(cursor.isLast()) break;
	       	  			cursor.moveToNext(); // move to next row
	       	  			i++;
	       	  			
	       	  		}	       	          
	       	    }
	       	    cursor.close();
	   	 	}
		}// end try
	    catch (Exception e) {
	    	Log.i("info","error from database");
	    }
		db.close();
		Log.i("info","printTable done");
	    	
	}// end printTable
    /*
	public void configUI() {
		this.m_adapter = new DownloadAdapter(this, R.layout.downloadrow, m_orders);
		setListAdapter(this.m_adapter);
		lv = this.getListView();
			
		}



	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		int x = mRadioGroup.getCheckedRadioButtonId();
		RadioButton temp = (RadioButton) findViewById(x);
    	searchBy =  temp.getText().toString();
	
		
	}
	*/
  
    
	    public Bitmap DownloadThumbnail(String image) throws IOException {                                                 
	 	   
	    	URL aURL = new URL("http://161.200.80.217/AACproject/Download/"+image);
	    	URLConnection conn = aURL.openConnection();
	    	conn.connect();
	    	conn.connect();
	    	InputStream is = conn.getInputStream();
	    	BufferedInputStream bis = new BufferedInputStream(new PatchInputStream(is));
	    	Bitmap bm = BitmapFactory.decodeStream(bis);
	    	bis.close();
	    	is.close();
	    	return bm;
	    }
	    
	    
	    
	    
	 public String getHtmlResult(HttpResponse x) throws ClientProtocolException, IOException
	 {
	       String cat ="";
	       String imgURL  = "";
	        String result = "";
	        
	        
	        BufferedReader reader = new BufferedReader(
	            new InputStreamReader(
	              x.getEntity().getContent()
	            )
	          );
	        

	        String line = null;
	        String jsonRecieve = "";
	        while ((line = reader.readLine()) != null){
	        	//Log.d("I got it",line);
	        	jsonRecieve += line;
	        	Log.d("I got it",line);
	        }
	        
	        return jsonRecieve;
	    }
	 
	 public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	   
	 private ProgressDialog mProgressDialog;
	    
	 protected Dialog onCreateDialog(int id) {
	        switch (id) {
			case DIALOG_DOWNLOAD_PROGRESS:
				mProgressDialog = new ProgressDialog(this);
				mProgressDialog.setMessage("Downloading file..");
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				mProgressDialog.setCancelable(false);
				mProgressDialog.show();
				return mProgressDialog;
			default:
				return null;
	        }
	    }

	public class fetchIconResult extends AsyncTask<String, Void, Void> {
		//private final ProgressDialog dialog = new ProgressDialog(DownloadPage.this);
		File downloadZipPic;
		File downloadZipSound;
		String zipPath;

		// can use UI thread here
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		// automatically done on worker thread (separate from UI thread)
		
		protected Void doInBackground(final String... args) {
			int count=0;
			try {

				URL url = new URL(downloadBasePath_url+args[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();

				int lenghtOfFile = conexion.getContentLength();
				
				
				Log.d("ANDRO_ASYNC", "Path " +downloadBasePath_url+args[0]);
				Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
				//Log.d("ANDRO_ASYNC", "File Name: "+conexion.getHeaderField(0));
				
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/AAConAndroid/"+args[0]+".zip");

				byte data[] = new byte[1024000];

				long total = 0;

					while ((count = input.read(data)) != -1) {
						total += count;
						onProgressUpdate(""+(int)((total*100)/lenghtOfFile));
						output.write(data, 0, count);
					}

					output.flush();
					output.close();
					input.close();
					String zipFile =  "sdcard/AAConAndroid/"+args[0]+".zip"; 
	            	String unzipLocation =  "sdcard/AAConAndroid/"; 
	            	 
	            	Decompress d = new Decompress(zipFile, unzipLocation); 
	            	d.unzip(); 
	            	Log.d("DODODODODO","ddd");
	            	
	            	//read .idx file to insert lexicalItem table and delete it
	            	
	            	
					File file = new File(Environment.getExternalStorageDirectory()+"/AAConAndroid/data.idx");

					String content = "";

					try {
    					BufferedReader br = new BufferedReader(new FileReader(file));
    					String line;

    					while ((line = br.readLine()) != null) {
        					content+=line;
    					}
					}
					catch (IOException e) {
    					
					}

	            	content = "["+content+"]";
	            	JSONArray data_raw,data_row;
	            	data_raw = new JSONArray(content);
	    			
	    			JSONObject d1 = data_raw.getJSONObject(0);
	    			String data_ex = d1.getString("data");
	    			
	    			
	    			data_row = new JSONArray(data_ex);
	    			Log.d ("info", ""+data_row.length());
	    			SQLiteDatabase db = openOrCreateDatabase( dbName, SQLiteDatabase.CREATE_IF_NECESSARY, null);
	    			
	    			for(int i =0;i<data_row.length();i++){
	    				
	    				String category = data_row.getJSONObject(i).getString("category");
	    				String lexical = data_row.getJSONObject(i).getString("lexical-item");
	    				
	    				//Log.d ("category",category);
	    				//Log.d ("lexical", lexical);
	    				
	    				JSONObject  category_nested= new JSONObject(category);
	    				String title = category_nested.getString("title");
	    				String cid = category_nested.getString("cid");
	    				String core = category_nested.getString("core");
	    				String subtitle = category_nested.getString("subtitle");
	    				String enable = category_nested.getString("enable");
	    				String variation = category_nested.getString("variation");
	    				String version = category_nested.getString("version");
	    				String weight = category_nested.getString("weight");
	    				String coverPath = category_nested.getString("coverPath");
	    				String lang = category_nested.getString("lang");
	    				String nextCid = category_nested.getString("nextCid");
	    				String sql ="insert or replace into NewCategory (enable,core,nextCid,variation,subtitle,cid,version,weight,title,coverPath,lang) values("+enable+","+core+","+nextCid+","+variation+","+"'"+subtitle+"',"+cid+","+version+","+weight+",'"+title+"','"+coverPath+"','"+lang+"')";
        				Log.d("database",sql);
	    				db.execSQL(sql);
        				sql = "delete from lexicalItem where cid="+cid;
        				Log.d("database",sql);
        				db.execSQL(sql);
	    				
	    				
	    				JSONArray lexical_nested = new JSONArray(lexical);
	    				for(int j=0;j<lexical_nested.length();j++){
	    					JSONObject lexical_row=lexical_nested.getJSONObject(j);
	    					String lid_l = lexical_row.getString("lid");
	    					
		    				String tag_l = lexical_row.getString("tag");
		    				String picPath_l = lexical_row.getString("picPath");
		    				String voicePath_l = lexical_row.getString("voicePath");
		    				String cid_l=lexical_row.getString("cid");
		    				String nextCid_l=lexical_row.getString("nextCid");
		    				String core_l=lexical_row.getString("core");
		    				String weight_l=lexical_row.getString("weight");
		    				String enable_l=lexical_row.getString("enable");
		    				
		    				sql ="insert into NewLexicalItem (lid,tag,picPath,voicePath,cid,nextCid,core,weight,enable) values("+lid_l+",'"+tag_l+"','"+picPath_l+"','"+voicePath_l+"',"+cid_l+","+nextCid_l+","+core_l+","+weight_l+","+enable_l+")";
		    				Log.d("database",sql);
		    				db.execSQL(sql);
	    				}
	    			}
	    			db.close();
	    			
	    			
	    			File file_idx = new File(Environment.getExternalStorageDirectory() + "/AAConAndroid/"+"data.idx");
	    			boolean delete = file_idx.delete();
	    			File file_zip = new File(Environment.getExternalStorageDirectory() + "/AAConAndroid/"+args[0]+".zip");
	    			boolean deleted = file_zip.delete();
	
	            	mProgressDialog.dismiss();
	            	
	            	Intent intent = getIntent();
	                finish();
	                startActivity(intent);
	                
				}
			catch (Exception e) {
					
					mProgressDialog.dismiss();
					Intent intent = getIntent();
	                finish();
	                startActivity(intent);
	                
				}
				return null;

				

		}

		// can use UI thread here
		protected void onProgressUpdate(String... progress) {
			 Log.d("ANDRO_ASYNC",progress[0]);
			 mProgressDialog.setProgress(Integer.parseInt(progress[0]));
			
		}

		protected void onPostExecute(String unused) {
			Log.d("Is done","onPost");
			mProgressDialog.dismiss();
			//mProgressDialog.cancel();
			
		}
	}

	 public class PatchInputStream extends FilterInputStream {
	    	public PatchInputStream(InputStream in) {
	    	     super(in);
	    	     }
	    	     public long skip(long n) throws IOException {
	    	     long m = 0L;
	    	     while (m < n) {
	    	     long _m = in.skip(n-m);
	    	     if (_m == 0L) break;
	    	     m += _m;
	    	     }
	    	     return m;
	    	     }
	    	    }
	 
	 public class DownloadAdapter extends ArrayAdapter<BitmapAndLabel> {
			String cateDL;
	         ArrayList<BitmapAndLabel> items;
	        LayoutInflater vi;
	        Vector <ConstantDBfromDownload> constantDB;
	        String zipname;
	        TextView tt;
	        public DownloadAdapter(Context context, int textViewResourceId,  ArrayList<BitmapAndLabel> items) {
	                super(context, textViewResourceId, items);
	                DownloadAdapter.this.items = items;
	                
	       }
	        
	        //@Override
	        public View getView(final int position, View convertView, ViewGroup parent) {
	                View v;
	                if (convertView == null) {
	                    vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                    v = vi.inflate(R.layout.downloadrow, null);

	                }else{
	                	v = convertView;
	                }
	                BitmapAndLabel o = (BitmapAndLabel)items.toArray()[position];
	                tt = (TextView) v.findViewById(R.id.toptext);
	                ImageView vv = (ImageView) v.findViewById(R.id.icon);
	                tt.setText(o.showWord);                            
	                vv.setBackgroundDrawable(o.showPic);
	                Button bb = (Button) v.findViewById(R.id.downloadDL_button);
	                bb.setText(R.string.DL);
	                bb.setOnClickListener(new View.OnClickListener() {
	                    public void onClick(View v) {
	                    	cateDL =tt.getText().toString();
	                    	// check name category exist
	                    
	        	    		String[] column = { "category" };
	        	    		Cursor c = Keeper.myDB.query("vocabulary", column, "category = '"
	        	    				+ cateDL + "' AND isDefault = 'l'", null,
	        	    				"category", null, null);
	        	    		constantDB = new Vector <ConstantDBfromDownload>();
	        	    		if (!c.moveToFirst()) {
		                    	try {
									String catePath = URLEncoder.encode(cateDL,"UTF-8");
		                        	HttpClient client = new DefaultHttpClient();
		                            HttpGet request = new HttpGet("http://161.200.80.217/AACproject/WebApplication8/Download.aspx?download="+catePath);
		                            getHtmlwriteDB(client.execute(request));
		               
		                		} catch (ClientProtocolException e) {
		                			// TODO Auto-generated catch block
		                			e.printStackTrace();
		                		} catch (IOException e) {
		                			// TODO Auto-generated catch block
		                			e.printStackTrace();
		                		}
		                		
		                	 	for(int i = 0; i < constantDB.size();i++){
		                	 		
		                	 		/*Log.e("voice",  constantDB.elementAt(i).sound);
		                	 		Log.e("category",  constantDB.elementAt(i).cate);
		                	 		Log.e("lang",  constantDB.elementAt(i).locale);
		                	 		Log.e("tag",  constantDB.elementAt(i).tag);
		                	 		Log.e("picCovered", "1");
		                	 		Log.e("picPath",  constantDB.elementAt(i).pic);
		                	 		Log.e("isDefault", "l");*/
		            	    	
		                	 		if(constantDB.elementAt(i).picCovered.equals("1")){
		                	 			createRowForCategory(constantDB,i);
		                	 		}else if (constantDB.elementAt(i).picCovered.equals("0")){
		                	 			createRowForVocab(constantDB,i);
		                	 		}
		                	 	}
		                	 	

	        	    		} else {
	        	    		// category exist
	        	    		// show pop up window, can't download because the category has already exist
	        	    		  	 Toast.makeText(getApplicationContext(),
	        								R.string.DL_EXIST, Toast.LENGTH_SHORT).show();

	        	    		}
	                    
	                	 	Log.e("check",zipname+Keeper.locale.toString());
	                	
	                     new writeBackDownload().execute(zipname); 
	                    	 
	                    }
	                });
	                
	                return v;
	                
	                
	                
	        }
	        @Override
	    	public BitmapAndLabel getItem(int arg0) {
	    		// TODO Auto-generated method stub
	    		return items.get(arg0);
	    	}
	  
	        public void getHtmlwriteDB(HttpResponse x) throws ClientProtocolException, IOException
	        {
	       
	           String [] temp;
	            String result = "";
	     
	            BufferedReader reader = new BufferedReader(new InputStreamReader(
	            		x.getEntity().getContent()) );

	            String line = null;
	            while ((line = reader.readLine()) != null){
	             temp = new String [7];
	             result += line + "\n";
	             Log.d("",line.toString());  
	             Pattern pattern = Pattern.compile( "AAC%(.*?)@" );
	             Matcher m = pattern.matcher( line );
	             if( m.find()) {
	                 String row = m.group(1);
	                 String[] afterSplit = row.split(",");
	                 for(int i=0;i<afterSplit.length;i++) {
	                	 temp [i] =afterSplit[i]; 
	                 }
	                 constantDB.add(new ConstantDBfromDownload(temp));
	              }	
	           
	             Pattern pattern1 = Pattern.compile( "AACZIP(.*?)HTML" );
	             Matcher m1 = pattern1.matcher( line );
	             if( m1.find()) {
	                 zipname = m1.group(1);     
	               
	                 }
	              	

	            }
	            
	            
	       
	        }
	        public void createRowForCategory(Vector <ConstantDBfromDownload>a, int pos) {
	    		
	    		ContentValues initialValues = new ContentValues();
	    		// voice must retrieve from smwhr
	    		initialValues.put("voice", a.elementAt(pos).sound);
	    		initialValues.put("category", a.elementAt(pos).cate);
	    		initialValues.put("lang", a.elementAt(pos).locale);
	    		initialValues.put("tag", a.elementAt(pos).tag);
	    		initialValues.put("picCovered", "1");
	    		initialValues.put("picPath", a.elementAt(pos).pic);
	    		initialValues.put("isDefault", "l");
	    		Keeper.myDB.insert("vocabulary", null, initialValues);

	    		ContentValues vocabValues = new ContentValues();
	    		// voice must retrieve from smwhr
	    		initialValues.put("voice",a.elementAt(pos).sound );
	    		vocabValues.put("category", a.elementAt(pos).cate);
	    		vocabValues.put("lang", a.elementAt(pos).locale);
	    		vocabValues.put("tag", a.elementAt(pos).tag);
	    		vocabValues.put("picCovered", "0");
	    		vocabValues.put("picPath", a.elementAt(pos).pic);
	    		vocabValues.put("isDefault", "l");
	    		Keeper.myDB.insert("vocabulary", null, vocabValues);

	    	}
	        public void createRowForVocab(Vector <ConstantDBfromDownload>a , int pos) {

	    		ContentValues initialValues = new ContentValues();
	    		initialValues.put("voice", a.elementAt(pos).sound);
	    		initialValues.put("category", a.elementAt(pos).cate);
	    		initialValues.put("lang", a.elementAt(pos).locale);
	    		initialValues.put("tag", a.elementAt(pos).tag);
	    		initialValues.put("picCovered", "0");
	    		initialValues.put("picPath", a.elementAt(pos).pic);
	    		initialValues.put("isDefault", "l");
	    		Keeper.myDB.insert("vocabulary", null, initialValues);

	    	}
	        
	        public class writeBackDownload extends AsyncTask<String, Void, Void> {
	    		private final ProgressDialog dialog = new ProgressDialog(DownloadPage.this);
				File downloadZipPic;
				File downloadZipSound;
				String zipPath;

	    		// can use UI thread here
	    		protected void onPreExecute() {

	    			this.dialog.setMessage(getString(R.string.DL_NEW_CATE));
	    			this.dialog.show();
	    		}

	    		// automatically done on worker thread (separate from UI thread)
	    		protected Void doInBackground(final String... args) {
	    		
					
		
						MainPage.DownloadFile("http://www.whatseat.net/"+args[0]+".zip",args[0]+".zip");
					if(Keeper.locale.equals(new Locale("th_TH"))){
						MainPage.DownloadFile("http://www.whatseat.net/"+args[0]+"_th.zip",args[0]+"_th.zip");
					}
	    			
	    		        try {
	    					Thread.sleep(3000);
	    					MainPage.unZipFile(args[0]+".zip");
	    					if(Keeper.locale.equals(new Locale("th_TH"))){
	    						MainPage.unZipFile(args[0]+"_th.zip");
	    						this.downloadZipSound = new File("sdcard/AAConAndroid/"+args[0]+"_th.zip");
	    					}
	    		 			Thread.sleep(5000);
	    				} catch (InterruptedException e1) {
	    					// TODO Auto-generated catch block
	    					e1.printStackTrace();
	    				} catch (IOException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	    				this.downloadZipPic = new File("sdcard/AAConAndroid/"+args[0]+".zip");
	    	
	    			return null;

	    		}

	    		// can use UI thread here
	    		protected void onPostExecute(final Void unused) {
	    			
	    			this.downloadZipPic.delete();
	    			if(Keeper.locale.equals(new Locale("th_TH"))){
		    			this.downloadZipSound.delete();
	    			}
	    			if (this.dialog.isShowing()){
	    				this.dialog.dismiss();
	    			}
	    			    		
	    		}
	    		
	    	}
	   
	     
	}
	 
		class SearchingTask extends AsyncTask<String, Void, Void> {
		    private final ProgressDialog dialog = new ProgressDialog(DownloadPage.this);

		    // can use UI thread here
		    protected void onPreExecute() {
		       this.dialog.setMessage(getString(R.string.PROGRESS_SEARCHING));
		       this.dialog.show();     
		    }

		    // automatically done on worker thread (separate from UI thread)
		    protected Void doInBackground(final String... args) {
		    	boolean canConnect=false;
             	BasicHttpParams httpParams = new BasicHttpParams();
                ConnManagerParams.setTimeout(httpParams, 1000l);
                HttpClient client = new DefaultHttpClient(httpParams);
                
                boolean isInput=false;
            	String result ="";
            	String input = searchInput.getText().toString().trim();
            	if(!input.equals("")) isInput=true;
            	String qu="%5B";
            	
            	// ["data","data2"]
            	String []darray = input.split("\\s+");
            	for(int i=0;i<darray.length;i++){
            		darray[i] = darray[i].trim();
            		darray[i] = darray[i].replaceAll("\\W", "");
            		if(i==darray.length-1)qu+="%22"+darray[i]+"%22";	
            		else qu+="%22"+darray[i]+"%22%2C";
            	}
            	qu+="%5D";
            		
            	if(isInput)request = new HttpGet(baseKeywordUrlRequest+qu);
            	else request = new HttpGet(categoryRequest_url);
            	
            	try {       	  	
            	  result=getHtmlResult(client.execute(request));
            	  canConnect=true;				
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}          
            	listData.clear();
            	listResultDB.clear();
              
	            if(canConnect){
	      		  printTable();
	              try {
	            	  	result = "["+result+"]";
	         			
		      			JSONArray data_raw = new JSONArray(result);
		      			JSONObject d1 = data_raw.getJSONObject(0);
		      			String base_path = d1.getString("aac-pict-url");
		      			String category = d1.getString("category");
		      			
		      			JSONArray data = new JSONArray(category);
		      			for(int i = 0; i < data.length(); i++){
	      			
		      				JSONObject c = data.getJSONObject(i);
		      				
		      				String version = c.getString("version");
		      				String coverPath = c.getString("coverPath");
		      				String title = c.getString("title");
		      				String subtitle=c.getString("subtitle");
		      				String cid=c.getString("cid");
		      				String nextCid=c.getString("nextCid");
		      				String core=c.getString("core");
		      				String variation=c.getString("variation");
		      				String weight=c.getString("weight");
		      				String lang=c.getString("lang");
		      				String enable=c.getString("enable");
		      				Log.i ("info", title);
		      				String clientVersion=findVersion(cid);
		      				String clientTitle=findTitle(cid);
		      				String clientSubtitle=findSubtitle(cid);
		      				
		      				if(clientVersion.equals("")){//new record
		      						listData.add(new DataShow(true,false,cid,title,subtitle,version,base_path,coverPath,weight,lang,variation,nextCid,enable,core,clientVersion,clientTitle,clientSubtitle));
		      				
		      				}else{//found in local database
		      						if(version.equals(clientVersion)){
		      							listData.add(new DataShow(false,true,cid,title,subtitle,version,base_path,coverPath,weight,lang,variation,nextCid,enable,core,clientVersion,clientTitle,clientSubtitle));
		      					    }else{
		      					    	listData.add(new DataShow(false,false,cid,title,subtitle,version,base_path,coverPath,weight,lang,variation,nextCid,enable,core,clientVersion,clientTitle,clientSubtitle));
		      					    }
		      				}
		      			} // for()
	              }catch(Exception e) {
	      				Log.i ("info", "error from code");    				
	      		  }
	            }else{
	            	AlertDialog alertDialog = new AlertDialog.Builder(DownloadPage.this).create();
             		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
             		alertDialog.setTitle(R.string.CONN_FAILED);
             		alertDialog.setMessage(getString(R.string.CONNECT_INT));
             		alertDialog.setButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
             			public void onClick(DialogInterface dialog, int which) {
         
             			} });
             		alertDialog.show();
	            } // if(canConnect)
	            DownloadPage.this.runOnUiThread(new Runnable() {
	            	@Override
	            	public void run() {
	            		adapterListViewData.notifyDataSetChanged();
	            	}
	            });
	            
              	return null;
		    }

		    // can use UI thread here
		    protected void onPostExecute(final Void unused) {
		       if (this.dialog.isShowing()) {
		          this.dialog.dismiss();
		       }  	 
		       lockPane.setClickable(true); 
		    }
		 }
		
		
}