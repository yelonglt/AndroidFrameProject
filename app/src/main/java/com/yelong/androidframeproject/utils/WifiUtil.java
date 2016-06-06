package com.yelong.androidframeproject.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;

import java.util.List;

/**
 * 定义一个WifiManager对象,提供Wifi管理的各种主要API，主要包含wifi的扫描、建立连接、配置信息等
 *
 * @author 800hr：yelong
 *         <p>
 *         2015-7-8
 */
public class WifiUtil {
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private WifiLock mWifiLock;

    /**
     * 当前网络配置列表
     */
    private List<WifiConfiguration> mWifiConfigurations;
    /**
     * 扫描网络的结果集
     */
    private List<ScanResult> mScanResults;

    public WifiUtil(Context context) {
        mWifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    public int checkWifiState() {
        return mWifiManager.getWifiState();
    }

    public void createWifiLock(String tag) {
        mWifiLock = mWifiManager.createWifiLock(tag);
    }

    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    public void releaseWifiLock() {
        if (mWifiLock.isHeld()) {
            mWifiLock.release();
        }
    }

    /**
     * 开始扫描
     */
    public void startScan() {
        mWifiManager.startScan();
        mWifiConfigurations = mWifiManager.getConfiguredNetworks();
        mScanResults = mWifiManager.getScanResults();
    }

    public List<WifiConfiguration> getWifiConfigurations() {
        return mWifiConfigurations;
    }

    public void connectConfiguration(int index) {
        if (index > mWifiConfigurations.size()) {
            return;
        }
        mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId,
                true);
    }

    public List<ScanResult> getScanResults() {
        return mScanResults;
    }

    public String lookScanResult() {
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        for (ScanResult result : mScanResults) {
            buffer.append("Index_").append(i++).append(":");
            buffer.append(result.toString()).append("\n");
        }
        return buffer.toString();
    }

    public String getMacAddress() {
        return mWifiInfo == null ? "" : mWifiInfo.getMacAddress();
    }

    public String getBSSID() {
        return mWifiInfo == null ? "" : mWifiInfo.getBSSID();
    }

    public int getIpAddress() {
        return mWifiInfo == null ? 0 : mWifiInfo.getIpAddress();
    }

    public int getNetworkId() {
        return mWifiInfo == null ? 0 : mWifiInfo.getNetworkId();
    }

    public String getWifiInfo() {
        return mWifiInfo == null ? "" : mWifiInfo.toString();
    }

    public void addNetwork(WifiConfiguration configuration) {
        int netId = mWifiManager.addNetwork(configuration);
        mWifiManager.enableNetwork(netId, true);
    }

    public WifiConfiguration createWifiInfo(String SSID, String Password,
                                            int Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        WifiConfiguration tempConfig = isExist(SSID);
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }
        if (Type == 1) {// WIFICIPHER_NOPASS
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 2) { // WIFICIPHER_WEP
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 3) { // WIFICIPHER_WPA
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    private WifiConfiguration isExist(String ssid) {
        if (mScanResults.size() > 0) {
            for (int i = 0; i < mScanResults.size(); i++) {
                if (mScanResults.get(i).SSID.equals(ssid)) {
                    return mWifiConfigurations.get(i);
                }
            }
        }
        return null;
    }

    public void disConnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    public boolean connectWifi(int netId) {
        if (mWifiConfigurations.size() > 0) {
            int size = mWifiConfigurations.size();
            for (int i = 0; i < size; i++) {
                WifiConfiguration config = mWifiConfigurations.get(i);
                if (config.networkId == netId) {
                    while (!mWifiManager.enableNetwork(netId, true)) {
                        // status:0--已经连接，1--不可连接，2--可以连接
                        Log.i("ConnectWifi",
                                "status" + String.valueOf(config.status));
                    }
                }
            }
        }
        return false;
    }

    public int isConfiguration(String SSID) {
        if (mWifiConfigurations.size() > 0) {
            for (WifiConfiguration config : mWifiConfigurations) {
                if (config.SSID.equals(SSID)) {
                    return config.networkId;
                }
            }
        }
        return -1;
    }

    public int addWifiConfig(List<ScanResult> wifiList, String ssid, String pwd) {
        int wifiId = -1;
        for (int i = 0; i < wifiList.size(); i++) {
            ScanResult wifi = wifiList.get(i);
            if (wifi.SSID.equals(ssid)) {
                Log.i("AddWifiConfig", "equals");
                WifiConfiguration wifiCong = new WifiConfiguration();
                wifiCong.SSID = "\"" + wifi.SSID + "\"";// \"转义字符，代表"
                wifiCong.preSharedKey = "\"" + pwd + "\"";// WPA-PSK密码
                wifiCong.hiddenSSID = false;
                wifiCong.status = WifiConfiguration.Status.ENABLED;
                wifiId = mWifiManager.addNetwork(wifiCong);// 将配置好的特定WIFI密码信息添加,添加完成后默认是不激活状态，成功返回ID，否则为-1
                if (wifiId != -1) {
                    return wifiId;
                }
            }
        }
        return wifiId;
    }

    /**
     * 获取连接wifi详细信息
     *
     * @param info
     * @return
     */
    public String getWifiInfo(WifiInfo info) {
        if (info == null) {
            return null;
        }
        if (info.getBSSID() == null) {
            return null;
        }

        int strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
        int speed = info.getLinkSpeed();
        String units = WifiInfo.LINK_SPEED_UNITS;
        String ssid = info.getSSID();

        return String.format("Connected to %s at %s%s. Strength %s/5", ssid,
                speed, units, strength);
    }

}
