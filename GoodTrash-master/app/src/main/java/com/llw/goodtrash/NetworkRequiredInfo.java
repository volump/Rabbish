package com.llw.goodtrash;

import android.app.Application;

import com.llw.mvplibrary.BuildConfig;
import com.llw.mvplibrary.network.INetworkRequiredInfo;


/**
 * 网络访问信息
 * @author llw
 */
public class NetworkRequiredInfo implements INetworkRequiredInfo {

    private static final String VERSION_NAME = "1.0.0";
    private static final int VERSION_CODE = 1;
    private Application application;

    public NetworkRequiredInfo(Application application){
        this.application = application;
    }

    /**
     * 版本名
     */
    @Override
    public String getAppVersionName() {
        return VERSION_NAME;
    }
    /**
     * 版本号
     */
    @Override
    public String getAppVersionCode() {
        return String.valueOf(VERSION_CODE);
    }

    /**
     * 是否为debug
     */
    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    /**
     * 应用全局上下文
     */
    @Override
    public Application getApplicationContext() {
        return application;
    }
}
