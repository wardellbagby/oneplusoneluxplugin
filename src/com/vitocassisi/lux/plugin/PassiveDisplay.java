/* Copyright 2013 Vito Cassisi
    
    You may not use this library except in compliance with the following:
        - It cannot be used for commercial purposes under any circumstances, unless explicit permission is granted in writing from the author.
        - Modifications to this library may not be distributed.
    
    Unless required by applicable law or agreed to in writing, software
    distributed under this licence is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. -->*/

package com.vitocassisi.lux.plugin;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.AndroidRuntimeException;
import android.util.Log;

public abstract class PassiveDisplay extends Service implements IPassiveDisplay {

	Messenger mClient = null;

	public static final int MSG_FAILURE = -1;
	public static final int MSG_GET_ALL = 0;
	public static final int MSG_REGISTER_CLIENT = 1;
	public static final int MSG_UNREGISTER_CLIENT = 2;
	public static final int MSG_SET_RGB = 3;
	public static final int MSG_SET_BACKLIGHT = 4;
	public static final int MSG_SET_MAX_BACKLIGHT = 5;
	public static final int MSG_SET_BUTTON_BACKLIGHT = 6;
	public static final int MSG_GET_BUTTON_BACKLIGHT = 7;
	public static final int MSG_GET_BACKLIGHT = 8;
	public static final int MSG_GET_MAX_BACKLIGHT = 9;
	public static final int MSG_GET_RGB = 10;

	public static final int INVALID_VALUE = -1;

	public static final String KEY_DEFAULT = "com.vitocassisi.lux.plugin";
	public static final String KEY_COMPATIBLE = "com.vitocassisi.lux.plugin.compatible";
	public static final String KEY_CLEANUP = "com.vitocassisi.lux.plugin.cleanup";

	public static final String KEY_ABS_DIR = "com.vitocassisi.lux.plugin.bundle.absDir";
	public static final String KEY_DELIMITER = "com.vitocassisi.lux.plugin.bundle.delimiter";
	public static final String KEY_INDEX = "com.vitocassisi.lux.plugin.bundle.index";
	public static final String KEY_MIN = "com.vitocassisi.lux.plugin.bundle.min";
	public static final String KEY_MAX = "com.vitocassisi.lux.plugin.bundle.max";

	public static final String KEY_MODEL = "com.vitocassisi.lux.plugin.model";
	public static final String KEY_KERNEL = "com.vitocassisi.lux.plugin.kernel";
	public static final String KEY_RGB = "com.vitocassisi.lux.plugin.getrgb";
	public static final String KEY_RGB_R = "com.vitocassisi.lux.plugin.setrgb.r";
	public static final String KEY_RGB_G = "com.vitocassisi.lux.plugin.setrgb.g";
	public static final String KEY_RGB_B = "com.vitocassisi.lux.plugin.setrgb.b";
	public static final String KEY_BACKLIGHT = "com.vitocassisi.lux.plugin.setbacklightlevel.brightness";
	public static final String KEY_MAX_BACKLIGHT = "com.vitocassisi.lux.plugin.setmaxbacklight.brightness";
	public static final String KEY_BUTTON_BACKLIGHT = "com.vitocassisi.lux.plugin.setbuttonlightlevel.brightness";
	public static final String KEY_LUX = "com.vitocassisi.lux.plugin.getlux";

	public static final String DELIMITED_SPACE = " ";
	public static final String DELIMITED_NEWLINE = "nl";
	public static final String DELIMITED_NONE = null;
	
	public static final String[] NO_COMMANDS_REQUIRED = new String[1];
	public static final String[] NOT_IMPLEMENTED = null;

	public boolean mCalled = false;

	final Messenger mMessenger = new Messenger(new MessageHandler());

	final class SuperNotCalledException extends AndroidRuntimeException {
		public SuperNotCalledException(String msg) {
			super(msg);
		}
	}

	class MessageHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Bundle input = msg.getData();
			Bundle output;
			Message tempMsg;

			switch (msg.what) {
			case MSG_REGISTER_CLIENT:
				mClient = msg.replyTo;
				tempMsg = Message.obtain(null, MSG_GET_ALL);
				output = new Bundle();
				output.putBoolean(
						KEY_COMPATIBLE,
						isSupportedDevice(input.getString(KEY_MODEL),
								input.getString(KEY_KERNEL)));
				mCalled = false;
				output.putStringArray(KEY_CLEANUP, onCleanup());
				if (!mCalled) {
					throw new SuperNotCalledException(
							"super.onCleanup() not called!");
				}

				LuxBundle tempBundle = getBacklightLevel();
				if (tempBundle != null)
					output.putBundle(KEY_BACKLIGHT, tempBundle.mBundle);
				tempBundle = getMaxBacklight();
				if (tempBundle != null)
					output.putBundle(KEY_MAX_BACKLIGHT, tempBundle.mBundle);
				tempBundle = getRGB();
				if (tempBundle != null)
					output.putBundle(KEY_RGB, getRGB().mBundle);
				tempBundle = getButtonLightLevel();
				if (tempBundle != null)
					output.putBundle(KEY_BUTTON_BACKLIGHT, tempBundle.mBundle);
				tempBundle = getLuxValue();
				if (tempBundle != null)
					output.putBundle(KEY_LUX, tempBundle.mBundle);

				tempMsg.setData(output);
				try {
					mClient.send(tempMsg);
				} catch (RemoteException e) {
					mClient = null;
				}
				break;
			case MSG_UNREGISTER_CLIENT:
				mClient = null;
				break;
			case MSG_SET_RGB:
				tempMsg = Message.obtain(null, MSG_SET_RGB);
				output = new Bundle();
				output.putStringArray(
						KEY_DEFAULT,
						setRGB(input.getInt(KEY_RGB_R, INVALID_VALUE),
								input.getInt(KEY_RGB_G, INVALID_VALUE),
								input.getInt(KEY_RGB_B, INVALID_VALUE)));
				tempMsg.setData(output);
				try {
					mClient.send(tempMsg);
				} catch (RemoteException e) {
					mClient = null;
				}
				break;

			case MSG_SET_BACKLIGHT:
				tempMsg = Message.obtain(null, MSG_SET_BACKLIGHT);
				output = new Bundle();
				output.putStringArray(KEY_DEFAULT, setBacklightLevel(input
						.getInt(KEY_BACKLIGHT, INVALID_VALUE)));
				tempMsg.setData(output);
				try {
					mClient.send(tempMsg);
				} catch (RemoteException e) {
					mClient = null;
				}
				break;

			case MSG_SET_MAX_BACKLIGHT:
				tempMsg = Message.obtain(null, MSG_SET_MAX_BACKLIGHT);
				output = new Bundle();
				output.putStringArray(KEY_DEFAULT, setMaxBacklight(input
						.getInt(KEY_MAX_BACKLIGHT, INVALID_VALUE)));
				tempMsg.setData(output);
				try {
					mClient.send(tempMsg);
				} catch (RemoteException e) {
					mClient = null;
				}
				break;

			case MSG_SET_BUTTON_BACKLIGHT:
				tempMsg = Message.obtain(null, MSG_SET_BUTTON_BACKLIGHT);
				output = new Bundle();
				output.putStringArray(KEY_DEFAULT, setBacklightLevel(input
						.getInt(KEY_BUTTON_BACKLIGHT, INVALID_VALUE)));
				tempMsg.setData(output);
				try {
					mClient.send(tempMsg);
				} catch (RemoteException e) {
					mClient = null;
				}
				break;

			case MSG_GET_BUTTON_BACKLIGHT:
				tempMsg = Message.obtain(null, MSG_GET_BUTTON_BACKLIGHT);
				tempMsg.setData(getButtonLightLevel().mBundle);

				try {
					mClient.send(tempMsg);
				} catch (RemoteException e) {
					mClient = null;
				}
				break;

			case MSG_GET_BACKLIGHT:
				tempMsg = Message.obtain(null, MSG_GET_BACKLIGHT);
				tempMsg.setData(getBacklightLevel().mBundle);
				try {
					mClient.send(tempMsg);
				} catch (RemoteException e) {
					mClient = null;
				}
				break;

			case MSG_GET_MAX_BACKLIGHT:
				tempMsg = Message.obtain(null, MSG_GET_MAX_BACKLIGHT);
				tempMsg.setData(getMaxBacklight().mBundle);
				try {
					mClient.send(tempMsg);
				} catch (RemoteException e) {
					mClient = null;
				}
				break;

			case MSG_GET_RGB:
				tempMsg = Message.obtain(null, MSG_GET_RGB);
				tempMsg.setData(getRGB().mBundle);
				try {
					mClient.send(tempMsg);
				} catch (RemoteException e) {
					mClient = null;
				}
				break;

			default:
				Log.e("Lux Plug-in", "Message Failure!");
				tempMsg = Message.obtain(null, MSG_FAILURE);
				try {
					mClient.send(tempMsg);
				} catch (RemoteException e) {
					mClient = null;
				}
				super.handleMessage(msg);
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}

	@Override
	abstract public String[] setRGB(int r, int g, int b);

	@Override
	abstract public String[] setBacklightLevel(int brightness);

	@Override
	abstract public String[] setMaxBacklight(int max);

	@Override
	abstract public String[] setButtonLightLevel(int brightness);

	@Override
	abstract public LuxBundle getButtonLightLevel();

	@Override
	abstract public LuxBundle getBacklightLevel();

	@Override
	abstract public LuxBundle getMaxBacklight();

	@Override
	abstract public LuxBundle getRGB();

	@Override
	abstract public String[] canCauseSysIssues();

	@Override
	abstract public boolean isSupportedDevice(String model, String kernel);

	@Override
	public String[] onCleanup() {
		String[] rgb = setRGB(255, 255, 255);
		String[] max = setMaxBacklight(255);
		int aLen = rgb.length;
		int bLen = max.length;
		String[] commands = new String[aLen + bLen];
		System.arraycopy(rgb, 0, commands, 0, aLen);
		System.arraycopy(max, 0, commands, aLen, bLen);
		mCalled = true;

		return commands;
	}

	public static class LuxBundle {
		private Bundle mBundle;

		/** Factory for creating LuxBundles.
		 * 
		 * @param absoluteDir
		 *            Absolute directory to relevant system file
		 * @param delimiter
		 *            Delimiter used to separate values
		 * @param index
		 *            Position of relevant value within the file
		 * @return */
		static LuxBundle builder(String absoluteDir, String delimiter, int min,
				int max, int index) {
			return new LuxBundle(absoluteDir, delimiter, min, max, index);
		}

		/** Factory for creating LuxBundles. Assumes first value is valid, and
		 * reads sequentially (if applicable). If you want to start at a
		 * particular value within the file, use builder(String absoluteDir,
		 * String delimiter, int index) instead.
		 * 
		 * @param absoluteDir
		 *            Absolute directory to relevant system file
		 * @param delimiter
		 *            Delimiter used to separate values
		 * @return */
		public static LuxBundle builder(String absoluteDir, String delimiter,
				int min, int max) {
			return new LuxBundle(absoluteDir, delimiter, -1, min, max);
		}

		private LuxBundle(String absoluteDir, String delimiter, int index,
				int min, int max) {
			mBundle = new Bundle();
			mBundle.putString(KEY_ABS_DIR, absoluteDir);
			mBundle.putString(KEY_DELIMITER, delimiter);
			mBundle.putInt(KEY_INDEX, index);
			mBundle.putInt(KEY_MIN, min);
			mBundle.putInt(KEY_MAX, max);
		}
	}
}