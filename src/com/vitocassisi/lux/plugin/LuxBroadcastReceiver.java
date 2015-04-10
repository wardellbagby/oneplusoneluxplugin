/* Copyright 2013 Vito Cassisi
    
    You may not use this library except in compliance with the following:
        - It cannot be used for commercial purposes under any circumstances, unless explicit permission is granted in writing from the author.
        - Modifications to this library may not be distributed.
    
    Unless required by applicable law or agreed to in writing, software
    distributed under this licence is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. -->*/

package com.vitocassisi.lux.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.mrchandler.lux.plugin.oneplusone.R;

public class LuxBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newPlugin = new Intent("com.vitocassisi.lux.plugin.installed");
        newPlugin.putExtra("com.vitocassisi.lux.plugin",
                context.getPackageName());
        newPlugin.putExtra("com.vitocassisi.lux.plugin.name", context
                .getResources().getString(R.string.app_name));
        context.sendBroadcast(newPlugin);
        Log.d("Plugin",
                "Sending package name... " + context.getPackageName()
                        + " name: "
                        + context.getResources().getString(R.string.app_name));
    }
}