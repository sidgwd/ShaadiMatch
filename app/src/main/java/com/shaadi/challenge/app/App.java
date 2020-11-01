package com.shaadi.challenge.app;

import android.app.Application;

import com.shaadi.challenge.R;
import com.shaadi.challenge.Util.AppConfig;
import com.shaadi.challenge.databinding.AppDataBindingComponent;

import android.databinding.DataBindingUtil;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataBindingUtil.setDefaultComponent(new AppDataBindingComponent());
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(AppConfig.APP_FONT_LIGHT)
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
