package com.slsatl.aac2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class AdapterListViewData extends BaseAdapter{

	private LayoutInflater mInflater;
    private Context context; //รับ Context จาก CustomListViewActivity
    private ArrayList<DataShow> listData = new ArrayList<DataShow>(); //list ในการเก็บข้อมูลของ DataShow
    
    
    public AdapterListViewData(Context context,ArrayList<DataShow> listData) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.listData = listData;
    }
 
    public int getCount() {
        // TODO Auto-generated method stub
        return listData.size(); //ส่งขนาดของ List ที่เก็บข้อมุลอยู่
    }
 
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }
 
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
 
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final HolderListAdapter holderListAdapter; //เก็บส่วนประกอบของ List แต่ละอัน
 
        if(convertView == null)
        {
            //ใช้ Layout ของ List เราเราสร้างขึ้นเอง (convertView.xml)
            convertView = mInflater.inflate( R.layout.adapter_listview, null);
 
             //สร้างตัวเก็บส่วนประกอบของ List แต่ละอัน
            holderListAdapter = new HolderListAdapter();
 
            //เชื่อมส่วนประกอบต่างๆ ของ List เข้ากับ View
            holderListAdapter.image = (ImageView) convertView.findViewById(R.id.imageView1);
            holderListAdapter.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            holderListAdapter.txtDetail = (TextView) convertView.findViewById(R.id.txtDetail);
            holderListAdapter.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            holderListAdapter.iconCatExist = (ImageView) convertView.findViewById(R.id.iconCatExist);
 
            convertView.setTag(holderListAdapter);
        }else{
            holderListAdapter = (HolderListAdapter) convertView.getTag();
        }
 
        //ดึงข้อมูลจาก listData มาแสดงทีละ position
        if(!listData.get(position).IsNew()&&listData.get(position).getStatus()){
        	holderListAdapter.checkBox.setVisibility(View.GONE);
        	holderListAdapter.iconCatExist.setVisibility(View.VISIBLE);
        }else{
        	holderListAdapter.iconCatExist.setVisibility(View.GONE);
        	holderListAdapter.checkBox.setVisibility(View.VISIBLE);
        }
        
        //Set img
       
         
         Bitmap temp =loadBitmap(listData.get(position).getBase_path()+"/"+listData.get(position).getCoverPath());
         
         if(temp!=null){
        	 holderListAdapter.image.setImageBitmap(temp);
         }
         else{
        	holderListAdapter.image.setImageResource(R.drawable.help_button);
         }
        
        //Set Title + Subtitle
        if(listData.get(position).IsNew()){
        	holderListAdapter.txtTitle.setText(listData.get(position).getTitle());
        	holderListAdapter.txtDetail.setText(listData.get(position).getSubtitle());
        }
        else{
        	holderListAdapter.txtTitle.setText(listData.get(position).getClientTitle());
        	holderListAdapter.txtDetail.setText(listData.get(position).getClientSubtitle());
        }
 
        //ถ้าทำการสัมผัส ที่ checkBox จะแสดงข้อความ ว่าทำการ checkBox List ที่เท่าไร
        holderListAdapter.checkBox.setOnClickListener(new OnClickListener() {
 
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(context,"CheckBox "+ position +" check!!",Toast.LENGTH_SHORT).show();
            	if(listData.get(position).isCheck()){
            		
            		listData.get(position).setCheck(false);
            	}
            	else{
            		
            		listData.get(position).setCheck(true);
            	}
            }
        });
 
        //ถ้าทำการเลือกที่ List จะแสดงข้อความ ว่าทำการเลือกที่ List ที่เท่าไร
        convertView.setOnClickListener(new OnClickListener() {
 
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	if(listData.get(position).isCheck()){
            		holderListAdapter.checkBox.setChecked(false);
            		listData.get(position).setCheck(false);
            	}
            	else{
            		holderListAdapter.checkBox.setChecked(true);
            		listData.get(position).setCheck(true);
            	}
            	
                //Toast.makeText(context,"List "+ position +" click!!",Toast.LENGTH_SHORT).show();
            }
        });
 
        return convertView;
    }
    
    
    public Bitmap loadBitmap( String imageUrl )
    {
        try
        {
            URL url = new URL( imageUrl.trim() ); // imageUrl คือ url ของรูปภาพ
            InputStream input = null;
            URLConnection conn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection)conn;
          httpConn.setRequestMethod("GET");
          httpConn.setReadTimeout(40000); // ตั้งเวลา  connect timeout
          httpConn.connect(); // connection
     
          if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
             input = httpConn.getInputStream(); // จับใส่ InputStream
          }
          Bitmap bitmap = BitmapFactory.decodeStream(input); //แปลงเป็น Bitmap
          input.close();
          httpConn.disconnect();
            return bitmap;
     
        }
        catch ( MalformedURLException e ){
          Log.d("fetchImage",
                "MalformedURLException invalid URL: " + imageUrl );
        }catch ( IOException e ){
          Log.d("fetchImage","IO exception: " + e);
        }catch(Exception e){
          Log.d("fetchImage","Exception: " + e);
        }
        return null;
    }
    
}
