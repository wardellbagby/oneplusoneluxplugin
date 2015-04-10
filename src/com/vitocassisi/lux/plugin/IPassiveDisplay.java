/* Copyright 2013 Vito Cassisi
    
    You may not use this library except in compliance with the following:
        - It cannot be used for commercial purposes under any circumstances, unless explicit permission is granted in writing from the author.
        - Modifications to this library may not be distributed.
    
    Unless required by applicable law or agreed to in writing, software
    distributed under this licence is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. -->*/

package com.vitocassisi.lux.plugin;

import com.vitocassisi.lux.plugin.PassiveDisplay.LuxBundle;

public interface IPassiveDisplay {

	/** Get commands to directly set display R G B values (0 - 255)
	 * 
	 * @param r
	 * @param g
	 * @param b */
	public String[] setRGB(int r, int g, int b);

	/** Get commands to set system brightness (0 to 255)
	 * 
	 * @param brightness */
	public String[] setBacklightLevel(int brightness);

	/** Get commands to set maximum backlight level (0 - 255)
	 * 
	 * @param max */
	public String[] setMaxBacklight(int max);

	/** Get commands to set button brightness from (0 - 255)
	 * 
	 * @param brightness */
	public String[] setButtonLightLevel(int brightness);

	/** Get instructions for retrieving button brightness (0 - 255)
	 * 
	 * @return */
	public LuxBundle getButtonLightLevel();

	/** Get instructions for retrieving backlight level (0 - 255)
	 * 
	 * @return */
	public LuxBundle getBacklightLevel();

	/** Get instructions for retrieving maximum backlight level (0 - 255)
	 * 
	 * @return */
	public LuxBundle getMaxBacklight();

	/** Get instructions for retrieving RGB values (0 - 255)
	 * 
	 * @return */
	public LuxBundle getRGB();
	
	/** Get instructions for retrieving lux values (0 - 255)
	 * 
	 * @return */
	public LuxBundle getLuxValue();

	/** List human readable side-effects of using this plug-in. Return null if
	 * none.
	 * 
	 * @return */
	public String[] canCauseSysIssues();

	/** Return whether this plug-in is compatible with the current device model
	 * and kernel, as reported by Build.MODEL and getProperty("os.version")
	 * respectively.
	 * 
	 * @return */
	public boolean isSupportedDevice(String model, String kernel);

	/** Get commands to clean up any changes that should be reverted when the
	 * plug-in is removed or disabled
	 * 
	 * @return */
	public String[] onCleanup();
}