package com.example.day7assignment;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends Activity {
	AutoCompleteTextView contacts;
	EditText mobilenumber;
	EditText email;
	Button go,buttonbackup;
	ArrayList<String> listData = new ArrayList<String>();
	ArrayList<String> ListPhoto = new ArrayList<String>();
	ArrayList<String> lisId = new ArrayList<String>();
	ArrayList<String> data = new ArrayList<String>();
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		contacts = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		mobilenumber = (EditText) findViewById(R.id.editText1);
		email = (EditText) findViewById(R.id.editText2);
		go = (Button) findViewById(R.id.button1);
		buttonbackup = (Button) findViewById(R.id.button2);
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

		ContentResolver resolver = getContentResolver();
		String[] columns = { "display_name", "data1", "raw_contact_id" };
		Cursor cur = resolver.query(uri, columns, null, null,
				"display_name");
		while (cur.moveToNext()) {
			listData.add(cur.getString(0));
			ListPhoto.add(cur.getString(1));
			lisId.add(cur.getString(2));
		}
		cur.close();
		adapter = new ArrayAdapter<String>(HomeActivity.this,
				android.R.layout.simple_list_item_1, listData);
		contacts.setAdapter(adapter);
		go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mobilenumber.setText(ListPhoto.get(listData.indexOf(contacts
						.getText().toString())));
				Uri uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
				ContentResolver resolver2 = getContentResolver();
				String str = "display_name like'"
						+ contacts.getText().toString() + "'";
				String[] coluStrings = { "data1" };
				Cursor cur2 = resolver2.query(uri, coluStrings, str,
						null, null);
				while (cur2.moveToNext()) {

					email.setText(cur2.getString(0));

				}
				cur2.close();
			}
		});
		
		buttonbackup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri_for_email = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
				ContentResolver resolver2 = getContentResolver();
				String[] coluStrings = { "data1" };
				int i =0;
				for (String str : lisId) {
					
					Cursor cur2 = resolver2.query(uri_for_email, coluStrings,
							str, null, null);
					cur2.moveToNext();
					data.add(lisId.get(i)+" , "+listData.get(i)+" , "+cur2.getString(0));
				i++;
				}
				ApplicationInfo appInfo=getApplicationInfo();
				File sdcardFile=Environment.getExternalStorageDirectory();
				String sdcardPath=sdcardFile.getAbsolutePath();
				final String notesFolderPath=sdcardPath+"/contacts";
				File notesFile=new File(notesFolderPath);
				if(!notesFile.exists())
				{
					notesFile.mkdir();
				}
				
				String filePath=notesFolderPath+"/"+"contacts"+".csv";
				
				//save desc
				try
				{
					FileWriter writer=new FileWriter(filePath);
					for (String data1 : data) {
					writer.write(data1+"\n");
					writer.flush();
					}
					writer.close();
				Toast.makeText(HomeActivity.this, "Success.", Toast.LENGTH_LONG).show();
				}
				
				catch(Exception ex)
				{
					Log.e("File Handling",ex.toString());
				}
			}
		});
}
}
