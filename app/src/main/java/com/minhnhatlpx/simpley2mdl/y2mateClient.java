package com.minhnhatlpx.simpley2mdl;
import android.content.*;
import com.koushikdutta.ion.*;
import com.google.gson.reflect.*;
import java.util.*;
import com.koushikdutta.async.future.*;
import android.widget.*;
import com.bumptech.glide.*;
import android.os.*;
import java.net.*;

public class y2mateClient
{
	private Context context;
	private static String GEN_URL = "https://www.y2mate.com/mates/analyze/ajax";
	private static String MP3_GEN_URL = "https://www.y2mate.com/mates/mp3/ajax";
	private static String CONVERT_URL = "https://www.y2mate.com/mates/convert";

	
	public y2mateClient(Context context)
	{
		this.context = context;
	}
	
	public void generator(String video_url,final ResponseListener listener)
	{
		Ion.with(context)
		.load(GEN_URL)
		.setMultipartParameter("url",video_url)
		.setMultipartParameter("q_auto","0")
		.setMultipartParameter("ajax","1")
		.as(new TypeToken<y2mate>(){})
		.setCallback(new FutureCallback<y2mate>()
			{

				@Override
				public void onCompleted(Exception e, y2mate y2mates)
				{
					if(y2mates == null)
					{
						listener.onFailed(e);
					}else{
						
						listener.onSuccess(y2mates);
						
					}
				}
				
				
			
		});
	}
	
	public void mp3generator(String video_url,final ResponseListener listener)
	{
		Ion.with(context)
			.load(MP3_GEN_URL)
			.setMultipartParameter("url",video_url)
			.setMultipartParameter("q_auto","0")
			.setMultipartParameter("ajax","1")
			.as(new TypeToken<y2mate>(){})
			.setCallback(new FutureCallback<y2mate>()
			{

				@Override
				public void onCompleted(Exception e, y2mate y2mates)
				{
					if(y2mates == null)
					{
						listener.onFailed(e);
					}else{

						listener.onSuccess(y2mates);

					}
				}



			});
	}
	
	public void Convert(String id, String v_id, String ftype, String fquality, final ResponseListener listener)
	{
		Ion.with(context)
			.load(CONVERT_URL)
			.setMultipartParameter("type","youtube")
			.setMultipartParameter("_id",id)
			.setMultipartParameter("v_id",v_id)
			.setMultipartParameter("ajax","1")
			.setMultipartParameter("token","")
			.setMultipartParameter("ftype",ftype)
			.setMultipartParameter("fquality",fquality)
			.as(new TypeToken<y2mate>(){})
			.setCallback(new FutureCallback<y2mate>()
			{

				@Override
				public void onCompleted(Exception e, y2mate y2mates)
				{
					if(y2mates == null)
					{
						listener.onFailed(e);
					}else{

						listener.onSuccess(y2mates);

					}
				}



			});
	}
	
	public void imageLoad(ImageView imgView, String src)
	{
		Glide.with(context)
		.load(src)
		.into(imgView);
	}
	
	//I try using Ion-Koush for request but it not return response header
	//So I will using this and with handler
	public static String getFileName(String url)
	{
		URL iurl;
		String filename = null;
		try {
			iurl = new URL(url);
			HttpURLConnection con = (HttpURLConnection) iurl.openConnection();
			con.setRequestMethod("HEAD");
			con.setInstanceFollowRedirects(false);
			con.connect();

			String content = con.getHeaderField("Content-Disposition");

			String filename_encode = content.split("filename=")[1].split("''")[1].split(";")[0];
		
			filename = URLDecoder.decode(filename_encode, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filename;
	}
	
	
	interface ResponseListener
	{
		void onSuccess(y2mate y2mates);
		void onFailed(Exception e);
	}
	
}
