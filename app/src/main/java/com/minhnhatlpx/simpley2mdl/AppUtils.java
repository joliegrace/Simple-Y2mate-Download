package com.minhnhatlpx.simpley2mdl;
import android.content.*;
import android.support.design.widget.*;
import android.view.*;
import android.widget.*;
import android.net.*;
import android.app.*;
import java.io.*;
import android.os.*;

public class AppUtils
{
	public static void showSnackbar(View view,String message)
	{
		Snackbar.make(view,message,Snackbar.LENGTH_LONG).show();
	}
	
	public static void showToast(Context context, String message)
	{
		Toast.makeText(context, message , Toast.LENGTH_LONG).show();
		
	}
	
	public static void downloadFile(Context context,String url, String fileName)
	{
		DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() +"/"+ fileName);
		DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url))
			.setTitle(fileName)
			.setDescription("Downloading")
			.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
			.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
			.setDestinationUri(Uri.fromFile(file))
			.setAllowedOverMetered(true)
			.setAllowedOverRoaming(true);

		downloadManager.enqueue(request);
	}
	
	public static void launchBrowser(Context context, String url)
	{
		Intent bIntent = new Intent(Intent.ACTION_VIEW);
		bIntent.setData(Uri.parse(url));
		context.startActivity(bIntent);
	}
	
	
}
