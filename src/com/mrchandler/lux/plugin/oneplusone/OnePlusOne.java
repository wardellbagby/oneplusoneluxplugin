package com.mrchandler.lux.plugin.oneplusone;

import com.vitocassisi.lux.plugin.PassiveDisplay;

import java.io.File;

public class OnePlusOne extends PassiveDisplay {

    @Override
    public String[] setRGB(int r, int g, int b) {
        return new String[]{
                "chmod 666 /sys/devices/platform/kcal_ctrl.0/kcal",
                "chmod 666 /sys/devices/platform/kcal_ctrl.0/kcal_ctrl",
                "echo \"" + r
                        + " "
                        + g
                        + " "
                        + b
                        + "\" > /sys/devices/platform/kcal_ctrl.0/kcal",
                "echo 1 > /sys/devices/platform/kcal_ctrl.0/kcal_ctrl"};
    }

    @Override
    public String[] setBacklightLevel(int brightness) {
        return new String[]{
                "chmod 666 /sys/devices/mdp.0/qcom,mdss_fb_primary.168/leds/lcd-backlight/brightness",
                "echo \""
                        + brightness
                        + "\" > /sys/devices/mdp.0/qcom,mdss_fb_primary.168/leds/lcd-backlight/brightness",
                "chmod 444 /sys/devices/mdp.0/qcom,mdss_fb_primary.168/leds/lcd-backlight/brightness"};
    }

    @Override
    public String[] setMaxBacklight(int max) {
        return new String[]{
                "chmod 666 /sys/devices/mdp.0/qcom,mdss_fb_primary.168/leds/lcd-backlight/max_brightness",
                "echo \""
                        + max
                        + "\" > /sys/devices/mdp.0/qcom,mdss_fb_primary.168/leds/lcd-backlight/max_brightness"};
    }

    @Override
    public String[] setButtonLightLevel(int brightness) {
        // To be implemented later, if it all possible.
        return null;
    }

    @Override
    public LuxBundle getButtonLightLevel() {
        // To be implemented later, if it all possible.
        return null;
    }

    @Override
    public LuxBundle getBacklightLevel() {
        return LuxBundle
                .builder(
                        "/sys/devices/mdp.0/qcom,mdss_fb_primary.168/leds/lcd-backlight/brightness",
                        DELIMITED_NONE, 0, 255);
    }

    @Override
    public LuxBundle getMaxBacklight() {
        return LuxBundle
                .builder(
                        "/sys/devices/mdp.0/qcom,mdss_fb_primary.168/leds/lcd-backlight/max_brightness",
                        DELIMITED_NONE, 0, 255);
    }

    @Override
    public LuxBundle getRGB() {
        return LuxBundle.builder("/sys/devices/platform/kcal_ctrl.0/kcal",
                DELIMITED_SPACE, 0, 255);
    }

    @Override
    public String[] canCauseSysIssues() {
        //No known issues
        return null;
    }

    @Override
    public boolean isSupportedDevice(String model, String kernel) {
        // Only tested to work on franco kernel right now, but should work on any kernel that uses kcal for color control.
        char sep = File.separatorChar;
        File kcal_control = new File(sep + "sys" + sep + "devices" + sep + "platform" + sep + "kcal_ctrl.0" + sep + "kcal");
        return model.contains("A0001") && kcal_control.exists();
    }

    @Override
    public LuxBundle getLuxValue() {
        return null;
    }

    @Override
    public String[] onCleanup() {
        //commands which should be run when the plug-in is disabled/removed
        String[] onClean = super.onCleanup();
        String[] temp = new String[onClean.length + 1];
        System.arraycopy(onClean, 0, temp, 0, onClean.length);
        temp[onClean.length] = "chmod 666 /sys/devices/mdp.0/qcom,mdss_fb_primary.168/leds/lcd-backlight/max_brightness";
        return temp;
    }
}
