package com.minhnhatlpx.simpley2mdl;

import android.app.*;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.content.*;
import java.net.*;
import android.net.*;
import java.nio.file.*;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
     
		Intent launchYoutubeIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
		launchYoutubeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity( launchYoutubeIntent );
		
		AppUtils.showToast(this, "Click to share button of a video and tap to this app for download");
		
		finish();
		
	}
	
}
