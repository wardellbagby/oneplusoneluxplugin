/* Copyright � 2013 Vito Cassisi
    
    You may not use this library except in compliance with the following:
        - It cannot be used for commercial purposes under any circumstances, unless explicit permission is granted in writing from the author.
        - Modifications to this library may not be distributed.
    
    Unless required by applicable law or agreed to in writing, software
    distributed under this licence is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. -->*/

package com.vitocassisi.lux.plugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.mrchandler.lux.plugin.oneplusone.R;

public class MainActivity extends Activity {
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Toast.makeText(this, R.string.plug_in_available, Toast.LENGTH_LONG).show();

		Intent newPlugin = new Intent("com.vitocassisi.lux.plugin.installed");
		newPlugin.putExtra("com.vitocassisi.lux.plugin", getPackageName());
		newPlugin.putExtra("com.vitocassisi.lux.plugin.name", getResources()
				.getString(R.string.app_name));
		sendBroadcast(newPlugin);
		Log.d("Plugin", "Sending package name... " + getPackageName()
				+ " name: " + getResources().getString(R.string.app_name));
		finish();
	}
}