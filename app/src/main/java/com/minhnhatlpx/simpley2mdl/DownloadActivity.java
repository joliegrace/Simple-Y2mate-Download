package com.minhnhatlpx.simpley2mdl;

import android.support.v7.app.*;
import com.minhnhatlpx.simpley2mdl.R;
import android.os.*;
import android.support.v7.widget.*;
import android.content.*;
import android.widget.ImageView;
import android.support.design.widget.*;
import com.koushikdutta.ion.*;
import android.util.*;
import android.graphics.*;
import com.bumptech.glide.*;
import android.view.*;
import android.widget.TextView;
import java.util.*;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.app.AlertDialog;
import android.app.*;
import android.support.v4.content.*;
import android.support.v4.app.*;
import android.content.pm.*;
import android.support.annotation.*;
import android.*;
import android.net.*;
import java.nio.file.*;
import java.net.*;
import java.io.*;
import android.webkit.*;

public class DownloadActivity extends AppCompatActivity
{

	private y2mateClient client;
	private Intent youtubeIntent;
	protected String YOUTUBE_URL;
	private String V_ID;
	private String ID;
	private String download_url;
	
	private CoordinatorLayout main_view;
	private RelativeLayout thumbTitleLayout;
	private LinearLayout loading_layout,list_layout;
	private Toolbar mToolbar;
	private ImageView thumbnail;
	private TextView title;
	
	private RecyclerView Mp4RV;
	private RecyclerView Mp3RV;
	
	private QualityAdapter Mp4Adapter;
	private QualityAdapter Mp3Adapter;
	
	private List<Quality> Mp4Qualitys = new ArrayList<>();
	private List<Quality> Mp3Qualitys = new ArrayList<>();
	
	private int STORAGE_PERMISSION_CODE = 100;
	
	private Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	    youtubeIntent = null;
		
		youtubeIntent = getIntent();

		YOUTUBE_URL = youtubeIntent.getStringExtra(Intent.EXTRA_TEXT);
		
		setContentView(R.layout.activity_download);
		
		mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
		
		
		initializView();
		
		
		checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
		
		client = new y2mateClient(this);
		
		Mp4Adapter = new QualityAdapter(this);
		Mp3Adapter = new QualityAdapter(this);
		
		LoadingView();

	    client.generator(YOUTUBE_URL,
			new y2mateClient.ResponseListener()
			{

				@Override
				public void onSuccess(y2mate y2mates)
				{
					Mp4generatorHandle(y2mates.getResult());
				}

				@Override
				public void onFailed(Exception e)
				{
					AppUtils.showSnackbar(main_view,"Error : " + e.getMessage());

				}

			});
		client.mp3generator(YOUTUBE_URL, new y2mateClient.ResponseListener()
			{

				@Override
				public void onSuccess(y2mate y2mates)
				{
					Mp3generatorHandle(y2mates.getResult());
				}

				@Override
				public void onFailed(Exception e)
				{
					AppUtils.showSnackbar(main_view,"Error : " + e.getMessage());

				}


			});
		
	}

	private void initializView()
	{
		main_view = findViewById(R.id.main_view);
	    thumbTitleLayout = findViewById(R.id.thumb_title_layout);
		list_layout = findViewById(R.id.list_layout);
		loading_layout = findViewById(R.id.loading_view_layout);
		thumbnail = findViewById(R.id.thumbnail);
		title = findViewById(R.id.title);
		Mp4RV = findViewById(R.id.recycler_mp4);
		Mp3RV = findViewById(R.id.recycler_mp3);
		
		setupRV(Mp4RV); 
		setupRV(Mp3RV);
		
	}

	private void setupRV(RecyclerView rv)
	{
		LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
		rv.setLayoutManager(llm);
		rv.setHasFixedSize(true);
		rv.setNestedScrollingEnabled(false);
	}
	

	private void Mp4generatorHandle(String data)
	{
		String data_split = data.replaceAll("/\\/g","");
		
		GeneratorResult parserResult = HtmlParser.parserGenerator(data_split);
		
		if(parserResult == null)
		{
			AppUtils.showSnackbar(main_view, "Parser Error or wrong URL... ! ");
		}
		else
		{
			Mp4postGenerator(parserResult , data_split);
		}
		
		
	}
	
	private void Mp4postGenerator(GeneratorResult result, String html)
	{
		V_ID = result.getVideoId();

		ID = result.getId();
		
		Mp4Qualitys = HtmlParser.getMp4Qualitys(html);
		
		
		client.imageLoad(thumbnail, result.getThumbnail());
		
		title.setText(result.getTitle());
		
		
		
		Mp4Adapter.setQualityList(Mp4Qualitys);
		Mp4Adapter.setCardColor(getResources().getColor(R.color.mp4Color));
		Mp4Adapter.setListener(new QualityAdapter.QualityAdapterListener()
			{

				@Override
				public void onItemClicked(Quality quality)
				{
					convert(ID, V_ID, quality.getType(), quality.getQuality());
				}
				
			
		});
		Mp4RV.setAdapter(Mp4Adapter);
		
	}
	
	private void Mp3generatorHandle(String data)
	{
		String data_split = data.replaceAll("/\\/g","");

		Mp3Qualitys = HtmlParser.getMp3Qualitys(data_split);
		
		if(Mp3Qualitys==null)
		{
			AppUtils.showSnackbar(main_view,"Parser Error or wrong URL...");
		}else
		{
			Mp3Adapter.setQualityList(Mp3Qualitys);
			Mp3Adapter.setCardColor(getResources().getColor(R.color.mp3Color));
			Mp3Adapter.setListener(new QualityAdapter.QualityAdapterListener()
				{

					@Override
					public void onItemClicked(Quality quality)
					{
						convert(ID, V_ID, quality.getType(), quality.getQuality());
					}


				});
			Mp3RV.setAdapter(Mp3Adapter);
		}
		
		UnLoadingView();

		
	}
	
	private void convert(String id, String v_id, String ftype, String fquality)
	{
		
		final ProgressDialog prDialog = new ProgressDialog(this);
		prDialog.setTitle("Convert");
		prDialog.setMessage("Convert and fetching download link...");
		prDialog.setCancelable(false);
		prDialog.show();
		
		client.Convert(id,v_id,ftype,fquality,new y2mateClient.ResponseListener()
			{

				@Override
				public void onSuccess(y2mate y2mates)
				{
					String data_split1 = y2mates.getResult().replaceAll("/\r/g","");
					String data_split2 = data_split1.replaceAll("/\n/g","");
					String data = data_split2.replaceAll("/\\/g","");
					
					download_url = HtmlParser.getDownloadUrl(data);
					new Thread() {
						public void run() {
							final String fileName = client.getFileName(download_url);
							
							handler.post(new Runnable() {
								public void run() {
									prDialog.dismiss();
									downloadDialog(download_url, fileName);
								}
							});
							
						}
					}.start();
					
					
				}

				@Override
				public void onFailed(Exception e)
				{
					AppUtils.showSnackbar(main_view, "Convert Error : "+e.getMessage());
					
					prDialog.dismiss();
				}
				
		});
	}
	
	
	private void downloadDialog(final String url,final String fileName)
	{
		AlertDialog.Builder adB = new AlertDialog.Builder(this);
		if(url!=null)
		{
			adB.setTitle("File");
			adB.setMessage(fileName);
			adB.setPositiveButton("Download", new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialogInterface, int i)
					{
						// Download
						AppUtils.downloadFile(DownloadActivity.this, url , fileName);

					}

				});
			adB.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialogInterface, int i)
					{
						dialogInterface.dismiss();
					}


				});
		}else
		{
			adB.setTitle("Error");
			adB.setMessage("download link unavailable!"+"\n"+"try to tap download button again or using y2mate in browser");
			adB.setPositiveButton("Try on browser", new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialogInterface, int i)
					{
						AppUtils.launchBrowser(DownloadActivity.this,"https://y2mate.com/youtube/"+V_ID);
					}

				});
			adB.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialogInterface, int i)
					{
						dialogInterface.dismiss();
					}


			});
		}
		
		
		adB.setCancelable(false);
		adB.create();
		adB.show();
		
	}
	
	
	private void LoadingView()
	{
		thumbTitleLayout.setVisibility(RelativeLayout.GONE);
		list_layout.setVisibility(LinearLayout.GONE);
		loading_layout.setVisibility(LinearLayout.VISIBLE);
		
	}
	private void UnLoadingView()
	{
		thumbTitleLayout.setVisibility(RelativeLayout.VISIBLE);
		list_layout.setVisibility(LinearLayout.VISIBLE);
		loading_layout.setVisibility(LinearLayout.GONE);

	}
	public void checkPermission(String permission, int requestCode)
    {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			if(ContextCompat.checkSelfPermission(DownloadActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

				ActivityCompat.requestPermissions(DownloadActivity.this, new String[] { permission }, requestCode);

			}
		}
        
    }

    
    @Override
    public void onRequestPermissionsResult(int requestCode,
										   @NonNull String[] permissions,
										   @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
										 permissions,
										 grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AppUtils.showToast(this, "Storage Permission Granted");
            } else {
				AppUtils.showToast(this, "Storage Permission Denied");
            }
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    MenuInflater inf = getMenuInflater();
		
		inf.inflate(R.menu.menu, menu);
        
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.action_open_in_browser:
				
				if(V_ID!=null){
					AppUtils.launchBrowser(this,"https://y2mate.com/youtube/"+V_ID);
				}else
				{
					AppUtils.launchBrowser(this,"https://y2mate.com/");
				}
				
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	

}
