package com.slsatl.aac;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

public class AACUtil {
	public static String getRealPathFromURI(Uri contentUri,Activity thisAct) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = thisAct.managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	public static void saveImageToAAC(String sourcePath, String destinationPath)
			throws IOException {
		Bitmap bi = resizeToLexIcon(BitmapFactory.decodeFile(sourcePath));
		writePhotoJpg(bi,destinationPath);                                                  
	}
	
	public static Bitmap resizeToLexIcon(Bitmap d) {
	    Bitmap out = Bitmap.createScaledBitmap(d, 100, 100, false);
	    return out;
	}
	
	public static void writePhotoJpg(Bitmap data, String pathName) {
	    File file = new File(pathName);
	    try {
	        file.createNewFile();
	        FileOutputStream os = new FileOutputStream(file);
	        data.compress(Bitmap.CompressFormat.JPEG, 100, os);
	        os.flush();
	        os.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public static boolean serialIsValidated(){
		String [] column = {"value"};
		Cursor c = Keeper.myDB.query("aac_param", column, "param='isValidated' ", null, null, null, null);
		c.moveToFirst();
		int isValidated = c.getInt(c.getColumnIndex("value"));
		if(isValidated==1){
			return true;
		}else{
			return false;
		}
	}
	public static void setValidate(int validate){
		String strFilter = "param='isValidated'";
		ContentValues args = new ContentValues();
		args.put("value",validate);
		Keeper.myDB.update("aac_param", args, strFilter, null);
	}
	public static String getHelpHtml(String tabId){
		String html = "";
		if(tabId.equals("About")){
			if(Keeper.locale.toString().equals("th_th")){
				html = getThAboutHelpHTML();
			}else{
				html = getEnAboutHelpHTML();
			}  		
    	}else if(tabId.equals("Settings")){
    		if(Keeper.locale.toString().equals("th_th")){
				html = getThSettingsHelpHTML();
			}else{
				html = getEnSettingsHelpHTML();
			}
    	}else if(tabId.equals("Compose")){
    		if(Keeper.locale.toString().equals("th_th")){
				html = getThComposeHelpHTML();
			}else{
				html = getEnComposeHelpHTML();
			}
    	}
		return html;
	}
	private static String getThAboutHelpHTML(){
		String html = "";
		html = "<html>" +
				"<body>" +
				"<h2><u>เกี่ยวกับจุฬาเอเอซี</u></h2>" +
				"<img src=\"launcher_icon.png\" width=\"40px\" height=\"40px\"><br/>" +
				"<p>จุฬาเอเอซีเป็นแอพพลิเคชันสื่อความหมายทดแทน (Augmentative and Alternative Communication: AAC) เพื่อช่วยเหลือผู้ที่สูญเสียความสามารถหรือมีความบกพร่องในการพูดที่ทำให้ไม่สามารถสื่อสารกับบุคคลอื่นด้วยภาษาพูดได้ จุฬาเอเอซีทำหน้าที่เป็นเสมือนกล่องบรรจุเสียงพูดที่ใช้ในการสนทนา โดยแบ่งคำศัพท์ที่ใช้ในการสนทนาเป็นหมวดหมู่ตามการใช้งาน ซึ่งแต่ละหน่วยคำศัพท์จะประกอบด้วยเสียงพูด รูปภาพ และข้อความ" +
				"<p>นอกจากนี้ผู้ใช้ที่มีความสามารถในการพิมพ์ข้อความยังสามารถสร้างเสียงสังเคราะห์ตามข้อความที่พิมพ์ผ่านจุฬาเอเอซีได้</p>" +
				"<p>จุฬาเอเอซีและระบบเว็บที่รองรับถูกพัฒนาขึ้นและดูแลโดยห้องวิจัยระบบภาษาพูดและเทคโนโลยีช่วยเหลือ คณะวิศวกรรมศาสตร์ จุฬาลงกรณ์มหาวิทยาลัย</p>" +
				"</body>" +
				"</html>";
		return html;
	}
	private static String getEnAboutHelpHTML(){
		String html = "";
		html = "<html>" +
				"<body>" +
				"<h2><u>About</u></h2>" +
				"<img src=\"launcher_icon.png\" width=\"40px\" height=\"40px\"><br/>" +
				"<p>Chula AAC is a category-based AAC (Augmentative and Alternative Communication) application by which users with special needs on spoken language assistance can use Chula AAC to produce audible speech by composing pictures or by typing characters via Android keyboards.</p>" +
				"<p>Chula AAC as well as its web-based support system were originally developed and are currently maintained by the Spoken Language Systems and Assistive Technology Lab at the Faculty of Engineering, Chulalongkorn University.</p>" +
				"</body>" +
				"</html>";
		return html;
	}
	private static String getThSettingsHelpHTML(){
		String html = "";
		html = "<html>" +
				"<body>" +
				"<h2><u>การตั้งค่า</u></h2>" +
				"<img src=\"help_icon_settings.gif\" width=\"20px\" height=\"20px\"><br/>" +
				"<h3>จัดการหมวดหมู่/คำศัพท์</h3>" +
				"<ul> <u><b>หมวดหมู่</b></u>" +
				"<li><u>แสดง หรือ ซ่อน หมวดหมู่ <img src=\"help_icon_hideshow.gif\" width=\"20px\" height=\"20px\">:</u> ผู้ใช้สามารถเลือกที่จะ ‘ซ่อน’ หรือ ‘แสดง’ หมวดหมู่ ที่แสดงในหน้าเลือกคำศัพท์ได้ด้วยการกดที่ปุ่มกลมบนแถบสไลด์ที่อยู่ด้านขวาของชื่อหมวดหมู่<br/>" +
				"สถานะ แสดง แถบสไลด์จะเป็นสีเขียว <img src=\"help_icon_toggle_show.gif\" width=\"50px\" height=\"20px\"><br/>" +
				"สถานะ ซ่อน แถบสไลด์จะเป็นสีชมพู  <img src=\"help_icon_toggle_hide.gif\" width=\"50px\" height=\"20px\"></li>" +
				"<li><u>จัดเรียงหมวดหมู่ <img src=\"help_icon_rearrange.gif\" width=\"20px\" height=\"20px\">:</u> ผู้ใช้สามารถจัดเรียงหมวดหมู่ที่แสดงอยู่ในหน้าเลือกคำศัพท์ได้ด้วยการกดลูกศรขึ้น [pic] เพื่อเลื่อนลำดับของหมวดหมู่ให้แสดงก่อน และกดลูกศรลง [pic] เพื่อเลื่อนลำดับให้แสดงทีหลัง</li>" +
				"<li><u>เพิ่มหมวดหมู่ใหม่ <img src=\"help_icon_add.gif\" width=\"20px\" height=\"20px\">:</u> ผู้ใช้สามารถเพิ่มหมวดหมู่ใหม่ได้ โดยเลือกภาพสำหรับหมวดหมู่จากไฟล์รูปในเครื่อง ใส่ชื่อกำกับ และกดบันทึก </li>" +
				"</ul>" +
				"<ul><u><b>คำศัพท์</b></u><br/>" +
				"เลือกหมวดหมู่ของคำศัพท์ที่ต้องการจัดการก่อน" +
				"<li><u>แสดง หรือ ซ่อน คำศัพท์ <img src=\"help_icon_hideshow.gif\" width=\"20px\" height=\"20px\">:</u> ผู้ใช้สามารถเลือกที่จะ ‘ซ่อน’ หรือ ‘แสดง’ คำศัพท์ ที่แสดงในหน้าเลือกคำศัพท์ได้ด้วยการกดที่ปุ่มกลมบนแถบสไลด์ที่อยู่ด้านขวาของชื่อคำศัพท์<br/>" +
				"สถานะ แสดง แถบสไลด์จะเป็นสีเขียว<img src=\"help_icon_toggle_show.gif\" width=\"50px\" height=\"20px\"><br/>" +
				"สถานะ ซ่อน แถบสไลด์จะเป็นสีชมพู<img src=\"help_icon_toggle_hide.gif\" width=\"50px\" height=\"20px\"></li>" +
				"<li><u>จัดเรียงคำศัพท์ <img src=\"help_icon_rearrange.gif\" width=\"20px\" height=\"20px\">:</u> ผู้ใช้สามารถจัดเรียงคำศัพท์ที่แสดงอยู่ในหน้าเลือกคำศัพท์ได้ด้วยการกดลูกศรขึ้น [pic] เพื่อเลื่อนลำดับของคำศัพท์ให้แสดงก่อน และกดลูกศรลง [pic] เพื่อเลื่อนลำดับให้แสดงทีหลัง</li>" +
				"<li><u>เพิ่มคำศัพท์ใหม่ <img src=\"help_icon_add.gif\" width=\"20px\" height=\"20px\">:</u> ผู้ใช้สามารถเพิ่คำศัพท์ใหม่ได้ โดยเลือกภาพสำหรับคำศัพท์จากไฟล์รูปในเครื่อง ใส่ชื่อกำกับ และกดบันทึก </li>" +
				"</ul>" +
				"<h3>ตั้งค่าภาษา</h3>" +
				"<p>Chula AAC เวอร์ชันปัจจุบันสนับสนุนการใช้งาน 2 ภาษา คือภาษาไทยและภาษาอังกฤษ ผู้ใช้สามารถเลือกภาษาที่ต้องการใช้งานได้จากเมนูนี้ </p>" +
				"<p>*สำหรับผู้ใช้ภาษาไทย อุปกรณ์ต้องได้รับการติดตั้งโปรแกรมสังเคราะห์เสียงภาษาไทย หากยังไม่ได้ทำการติดตั้ง กรุณาอ่านหัวข้อ การติดตั้งโปรแกรมสังเคราะห์เสียงภาษาไทย</p>" +
				"<h3>การติดตั้งโปรแกรมสังเคราะห์เสียงภาษาไทย</h3>" +
				"<p>ติดตั้งโปรแกรมสังเคราะห์เสียงภาษาไทย VAJA Text-To-Speech Engine โดยสามารถดาวน์โหลดได้จาก <a href=\"https://play.google.com/store/apps/details?id=com.spt.tts.vaja&feature=search_result#?t=W251bGwsMSwxLDEsImNvbS5zcHQudHRzLnZhamEiXQ..\">https://play.google.com/store/apps/details?id=com.spt.tts.vaja&feature=search_result#?t=W251bGwsMSwxLDEsImNvbS5zcHQudHRzLnZhamEiXQ..</a>" +
				"หลังจากติดตั้งโปรแกรม VAJA TTS เสร็จแล้วให้ทำตามขั้นตอนดังนี้</p>" +
				"<ol>" +
				"<li>ไปที่ Android Settings -> Voice input and output -> Text-To-Speech settings</li>" +
				"<li>เปิดใช้งาน VAJA TTS Engine ในหัวข้อ Engines และตั้ง VAJA TTS เป็น default engine</li>" +
				"<li>เลือก Install voice data รอจนติดตั้งเสร็จ</li>" +
				"</ol>" +
				"<h3>อัพเดต</h3>" +
				"<p>ผู้ใช้สามารถตรวจสอบการอัพเดตโปรแกรมได้โดยการเลือกเมนูนี้</p>" +
				"<h3>ดาวน์โหลดหมวดหมู่เพิ่ม</h3>" +
				"<ol>" +
				"<li>ในหน้าแรก กดรูป <img src=\"help_icon_download.gif\" width=\"20px\" height=\"20px\"></li>" +
				"<li>พิมพ์คำค้นหาหลักที่ต้องการในช่องสี่เหลี่ยม แล้วกดปุ่ม <img src=\"help_button_search.gif\" width=\"20px\" height=\"20px\"></li>" +
				"<li>เลือกหมวดหมู่ที่ต้องการดาวน์โหลดจากรายการที่ปรากฏ โดยสัมผัสเพื่อใส่เครื่องหมายในกล่องทางซ้ายของหมวดหมู่ที่ต้องการ หมวดหมู่ที่มีอยู่ในเครื่องแล้วจะถูกแสดงด้วย <img src=\"help_icon_cat_exist.gif\" width=\"20px\" height=\"20px\"> และไม่สามารถเลือกได้ </li>" +
				"<li>กดปุ่ม <img src=\"help_button_download.gif\" width=\"20px\" height=\"20px\"> เพื่อดาวน์โหลด</li>" +
				"<li>กดปุ่ม <img src=\"help_button_clear.gif\" width=\"20px\" height=\"20px\"> เพื่อลบคำค้นหาหลัก</li>" +
				"</body>" +
				"</html>";
		return html;
	}
	private static String getEnSettingsHelpHTML(){
		String html = "";
		html = "<html>" +
				"<body>" +
				"<h2><u>Settings</u></h2>" +
				"<img src=\"help_icon_settings.gif\" width=\"20px\" height=\"20px\"><br/>" +
				"<h3>Manage Categories/Vocabs</h3>" +
				"<ul> <u><b>Categories</b></u>" +
				"<li><u>hide/show <img src=\"help_icon_hideshow.gif\" width=\"20px\" height=\"20px\">:</u> Users can toggle the visibility of each category by touch the button on the right of the category name.<br/>" +
				"Green bars indicate visible categories <img src=\"help_icon_toggle_show_en.gif\" width=\"50px\" height=\"20px\"><br/>" +
				"Pink bars indicate hidden categories <img src=\"help_icon_toggle_hide_en.gif\" width=\"50px\" height=\"20px\"></li>" +
				"<li><u>Rearrange<img src=\"help_icon_rearrange.gif\" width=\"20px\" height=\"20px\">:</u> Users can re-arrange the categories based on their preferences by pressing either the up or down arrows on the right of each category name. Press the save button on the top of the page to make the arrangement effective.</li>" +
				"<li><u>Add new category<img src=\"help_icon_add.gif\" width=\"20px\" height=\"20px\">:</u> Users can add their custom categories by choosing a picture and specifying the category name from the pop-up dialog. </li>" +
				"</ul>" +
				"<ul><u><b>Vocabs</b></u><br/>" +
				"Vocab settings are accessed by choosing a category item from the Category setting page." +
				"<li><u>hide/show <img src=\"help_icon_hideshow.gif\" width=\"20px\" height=\"20px\">:</u> Users can toggle the visibility of each category by touch the button on the right of the vocab name.<br/>" +
				"Green bars indicate visible categories<img src=\"help_icon_toggle_show_en.gif\" width=\"50px\" height=\"20px\"><br/>" +
				"Pink bars indicate hidden categories<img src=\"help_icon_toggle_hide_en.gif\" width=\"50px\" height=\"20px\"></li>" +
				"<li><u>Rearrange <img src=\"help_icon_rearrange.gif\" width=\"20px\" height=\"20px\">:</u>Users can re-arrange the vocabs based on their preferences by pressing either the up or down arrows on the right of each vocab name. Press the save button on the top of the page to make the arrangement effective.</li>" +
				"<li><u>Add new vocab <img src=\"help_icon_add.gif\" width=\"20px\" height=\"20px\">:</u> Users can add their custom vocabs by choosing a picture and specifying the vocab name from the pop-up dialog. </li>" +
				"</ul>" +
				"<h3>Set Language</h3>" +
				"<p>Chula AAC currently supports Thai and English. Choose your desired language from the list.</p>" +
				"<h3>Update</h3>" +
				"<p>Users can check for a newer version of Chula AAC from this menu item.</p>" +
				"<h3>Download Additional Categories</h3>" +
				"<ol>" +
				"<li>From the front page, touch <img src=\"help_icon_download.gif\" width=\"20px\" height=\"20px\"></li>" +
				"<li>Type in keywords and touch <img src=\"help_button_search.gif\" width=\"20px\" height=\"20px\"></li>" +
				"<li>Categories with vocabs related to the search keywords will be listed. Check categories to be downloaded. Categories existed in your Chula AAC are marked with <img src=\"help_icon_cat_exist.gif\" width=\"20px\" height=\"20px\"> and cannot be checked. </li>" +
				"<li>Touch <img src=\"help_button_download.gif\" width=\"20px\" height=\"20px\"> to download checked categories</li>" +
				"<li>Touch <img src=\"help_button_clear.gif\" width=\"20px\" height=\"20px\"> to clear the search box.</li>" +
				"</body>" +
				"</body>" +
				"</html>";
		return html;
	}
	private static String getThComposeHelpHTML(){
		String html = "";
		html = "<html>" +
				"<body>" +
				"<h2><u>การใช้งาน</u></h2>" +
				"<h3>การสังเคราะห์เสียงจากการเลือกรูปภาพคำศัพท์</h3>" +
				"<ol>" +
				"<li>กดรูป<br/><img src=\"help_android_main.gif\" width=\"100px\" height=\"100px\"><br/>เพื่อไปที่หน้าเลือกคำศัพท์</li>" +
				"<li>เลือกหมวดหมู่ของคำศัพท์โดยกดที่รูปภาพใต้แถบสีชมพู<br/><img src=\"help_cat_bar.gif\" width=\"145px\" height=\"40px\"><br/> สไลด์ซ้ายหรือขวาเพื่อดูหมวดหมู่เพิ่ม</li>" +
				"<li>เลือกคำศัพท์ที่ต้องการจากรูปใต้แถบสีเขียว สไลด์ขึ้นลงเพื่อดูคำศัพท์เพิ่ม</li>" +
				"<li>กดปุ่ม <img src=\"help_icon_speak.gif\" width=\"20px\" height=\"20px\"> เพื่อสังเคราะห์เสียง </li>" +
				"<li>กดปุ่ม <img src=\"help_icon_speak_delete.gif\" width=\"20px\" height=\"20px\">เพื่อลบคำศัพท์ที่ไม่ต้องการ</li>" +
				"</ol>" +
				"<p>ผู้ใช้สามารถเข้าสู่หน้าการสังเคราะห์เสียงจากข้อความ โดยกดปุ่มเมนูของเครื่อง และเลือก <img src=\"help_icon_write.gif\" width=\"20px\" height=\"20px\"></p>" +
				"<h3>การสังเคราะห์เสียงจากข้อความ</h3>" +
				"<ol>" +
				"<li>กดรูป  <img src=\"help_icon_tts.gif\" width=\"37px\" height=\"20px\"></li>" +
				"<li>พิมพ์ข้อความที่ต้องการ</li>" +
				"<li>กดปุ่ม  <img src=\"help_icon_speak.gif\" width=\"20px\" height=\"20px\"> เพื่อสังเคราะห์เสียง</li>" +
				"<li>กดปุ่ม  <img src=\"help_icon_speak_delete.gif\" width=\"20px\" height=\"20px\"> เพื่อลบข้อความ</li>" +
				"<li>หากต้องการใช้ข้อความที่เคยสังเคราะห์เสียงแล้ว สามารถเลือกได้จากข้อความในแถบสีขาวด้านล่าง</li>" +
				"</ol>" +
				"<p>ผู้ใช้สามารถเข้าสู่หน้าการสังเคราะห์เสียงจากการเลือกรูปภาพคำศัพท์ โดยกดปุ่มเมนูของเครื่อง และเลือก <img src=\"help_icon_picture.gif\" width=\"20px\" height=\"20px\"></p>" +
				"</body>" +
				"</html>";
		return html;
	}
	private static String getEnComposeHelpHTML(){
		String html = "";
		html = "<html>" +
				"<body>" +
				"<h2><u>Composing</u></h2>" +
				"<h3>Speech Synthesis from Pictures</h3>" +
				"<ol>" +
				"<li>Touch<br/><img src=\"help_android_main.gif\" width=\"100px\" height=\"100px\"><br/>to go to composing from picture page</li>" +
				"<li>Choose a category from the Category Selection bar<br/><img src=\"help_cat_bar.gif\" width=\"145px\" height=\"40px\">.<br/> Swipe left or right to see more categories</li>" +
				"<li>Touch a vocab picture to add to the selected vocab list. Items in this list are items waiting to be synthesized. Swipe up or down to see all vocabs.</li>" +
				"<li>Touch <img src=\"help_icon_speak.gif\" width=\"20px\" height=\"20px\"> to synthesis speech of the items in the selected vocab list. </li>" +
				"<li>Touch <img src=\"help_icon_speak_delete.gif\" width=\"20px\" height=\"20px\"> to remove the last vocab in the selected vocab list</li>" +
				"</ol>" +
				"<p>Users can switch to the \"Text-to-Speech Synthesis\" page by pressing the device's menu button and choose <img src=\"help_icon_write.gif\" width=\"20px\" height=\"20px\"></p>" +
				"<h3>Text-to-Speech Synthesis</h3>" +
				"<ol>" +
				"<li>Touch  <img src=\"help_icon_tts.gif\" width=\"37px\" height=\"20px\"></li>" +
				"<li>Type in the message box the desired message.</li>" +
				"<li>Touch  <img src=\"help_icon_speak.gif\" width=\"20px\" height=\"20px\"> to synthesis speech of the message.</li>" +
				"<li>Touch  <img src=\"help_icon_speak_delete.gif\" width=\"20px\" height=\"20px\"> to clear the current message box.</li>" +
				"<li>Previously synthesized messages are stored in the list in the lower part of the page. Touch an item in the list to re-synthesized the message.</li>" +
				"</ol>" +
				"<p>Users can switch to the \"Speech Synthesis from Pictures\" page by pressing the device's menu button and choose  <img src=\"help_icon_picture.gif\" width=\"20px\" height=\"20px\"></p>" +
				"</body>" +
				"</html>";
		return html;
	}
}
