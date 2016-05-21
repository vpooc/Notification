package com.vpooc.notification;

import org.xutils.x;

/**
 * Created by Administrator on 2016/5/20.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);//初始化xutils


    }
}
