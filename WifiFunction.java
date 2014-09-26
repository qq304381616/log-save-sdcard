package com.example.mywifi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;

public class WifiFunction {

	private static WifiManager meWifiManager;

	private WifiInfo meWifiInfo;

	/** 扫描出的网络连接列表 */
	private List<ScanResult> meWifiList;
	// 网络连接列表
	private List<WifiConfiguration> meWifiConfigurations;

	private WifiLock meWifiLock;

	public WifiFunction(Context context) {
		// 取得WifiManager对象
		meWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		// 取得WifiInfo对象
		meWifiInfo = meWifiManager.getConnectionInfo();
	}

	// 判断WiFi连接时是否能够访问Internet
	// 或主动连接百度等判断是否有返回
	public boolean hasWifiInternetAccess(Context context) {
		WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (mWifiManager.isWifiEnabled()) {
			return mWifiManager.pingSupplicant();
		}
		return false;
	}

	/** 判断网络连接 */
	public boolean isNetworkAvailable(Context context) {

		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null)
			return false;

		String typeName = info.getTypeName(); // 判断网络类型 "WIFI" or "MOBILE".
		if (typeName == "MOBILE" && info.getExtraInfo().equals("cmwap")) {
		}
		return true;
	}

	// 打开wifi
	public void openWifi() {
		if (!meWifiManager.isWifiEnabled()) {
			meWifiManager.setWifiEnabled(true);
		}
	}

	// 关闭wifi
	public void closeWifi() {
		if (!meWifiManager.isWifiEnabled()) {
			meWifiManager.setWifiEnabled(false);
		}
	}

	// 检查当前wifi状态
	public int checkState() {
		return meWifiManager.getWifiState();
	}

	// 锁定wifiLock
	public void acquireWifiLock() {
		meWifiLock.acquire();
	}

	// 解锁wifiLock
	public void releaseWifiLock() {
		// 判断是否锁定
		if (meWifiLock.isHeld()) {
			meWifiLock.acquire();
		}
	}

	// 创建一个wifiLock
	public void createWifiLock() {
		meWifiLock = meWifiManager.createWifiLock("test");
	}

	// 得到配置好的网络
	public List<WifiConfiguration> getConfiguration() {
		return meWifiConfigurations;
	}

	// 指定配置好的网络进行连接
	public void connetionConfiguration(int index) {
		if (index > meWifiConfigurations.size()) {
			return;
		}
		// 连接配置好指定ID的网络
		meWifiManager.enableNetwork(meWifiConfigurations.get(index).networkId, true);
	}

	public void startScan() {
		meWifiManager.startScan();
		// 得到扫描结果
		meWifiList = meWifiManager.getScanResults();
		// 得到配置好的网络连接
		meWifiConfigurations = meWifiManager.getConfiguredNetworks();
	}

	// 得到网络列表
	public List<ScanResult> getWifiList() {
		return meWifiList;
	}

	// 查看扫描结果
	public StringBuffer lookUpScan() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < meWifiList.size(); i++) {
			sb.append("Index_" + new Integer(i + 1).toString() + ":");
			// 将ScanResult信息转换成一个字符串包
			// 其中把包括：BSSID、SSID、capabilities、frequency、level
			sb.append((meWifiList.get(i)).toString()).append("\n" + "~");
		}
		return sb;
	}

	public String getMacAddress() {
		return (meWifiInfo == null) ? "NULL" : meWifiInfo.getMacAddress();
	}

	public String getBSSID() {
		return (meWifiInfo == null) ? "NULL" : meWifiInfo.getBSSID();
	}

	public int getIpAddress() {
		return (meWifiInfo == null) ? 0 : meWifiInfo.getIpAddress();
	}

	// 得到连接的ID
	public int getNetWordId() {
		return (meWifiInfo == null) ? 0 : meWifiInfo.getNetworkId();
	}

	// 得到wifiInfo的所有信息
	public String getWifiInfo() {
		return (meWifiInfo == null) ? "NULL" : meWifiInfo.toString();
	}

	// 添加一个网络并连接
	public void addNetWork(WifiConfiguration configuration) {
		int wcgId = meWifiManager.addNetwork(configuration);
		meWifiManager.enableNetwork(wcgId, true);
	}

	// 断开指定ID的网络
	public void disConnectionWifi(int netId) {
		meWifiManager.disableNetwork(netId);
		meWifiManager.disconnect();
	}

	// 密码连接方式
	public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";

		if (Type == 1) // WIFICIPHER_NOPASS
		{
			config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 2) // WIFICIPHER_WEP
		{
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 3) // WIFICIPHER_WPA
		{
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}
		return config;
	}

	public static WifiConfiguration IsExsits(String SSID) {
		List<WifiConfiguration> existingConfigs = meWifiManager.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}
		return null;
	}

	public static void setIpAssignment(String assign, WifiConfiguration wifiConf) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		setEnumField(wifiConf, assign, "ipAssignment");
	}

	public static void setIpAddress(InetAddress addr, int prefixLength, WifiConfiguration wifiConf)
			throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException,
			NoSuchMethodException, ClassNotFoundException, InstantiationException, InvocationTargetException {
		Object linkProperties = getField(wifiConf, "linkProperties");
		if (linkProperties == null)
			return;
		Class laClass = Class.forName("android.net.LinkAddress");
		Constructor laConstructor = laClass.getConstructor(new Class[] { InetAddress.class, int.class });
		Object linkAddress = laConstructor.newInstance(addr, prefixLength);

		ArrayList mLinkAddresses = (ArrayList) getDeclaredField(linkProperties, "mLinkAddresses");
		mLinkAddresses.clear();
		mLinkAddresses.add(linkAddress);
	}

	public static void setGateway(InetAddress gateway, WifiConfiguration wifiConf) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException,
			NoSuchMethodException, InstantiationException, InvocationTargetException {
		Object linkProperties = getField(wifiConf, "linkProperties");
		if (linkProperties == null)
			return;
		Class routeInfoClass = Class.forName("android.net.RouteInfo");
		Constructor routeInfoConstructor = routeInfoClass.getConstructor(new Class[] { InetAddress.class });
		Object routeInfo = routeInfoConstructor.newInstance(gateway);

		ArrayList mRoutes = (ArrayList) getDeclaredField(linkProperties, "mRoutes");
		mRoutes.clear();
		mRoutes.add(routeInfo);
	}

	public static void setDNS(InetAddress dns, WifiConfiguration wifiConf) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		Object linkProperties = getField(wifiConf, "linkProperties");
		if (linkProperties == null)
			return;

		ArrayList<InetAddress> mDnses = (ArrayList<InetAddress>) getDeclaredField(linkProperties, "mDnses");
		mDnses.clear(); // or add a new dns address , here I just want to
						// replace DNS1
		mDnses.add(dns);
	}

	public static Object getField(Object obj, String name) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getField(name);
		Object out = f.get(obj);
		return out;
	}

	public static Object getDeclaredField(Object obj, String name) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getDeclaredField(name);
		f.setAccessible(true);
		Object out = f.get(obj);
		return out;
	}

	public static void setEnumField(Object obj, String value, String name) throws SecurityException,
			NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getField(name);
		f.set(obj, Enum.valueOf((Class<Enum>) f.getType(), value));
	}

	public static void set_static(String SSID, String static_ip, String static_gateway, String static_dns) {

		WifiConfiguration tempConfig = WifiFunction.IsExsits(SSID);
		if (tempConfig != null) {
			try {
				setIpAssignment("STATIC", tempConfig);

				setIpAddress(InetAddress.getByName(static_ip), 24, tempConfig);

				setGateway(InetAddress.getByName(static_gateway), tempConfig);

				setDNS(InetAddress.getByName(static_dns), tempConfig);

			} catch (Exception e) {

				e.printStackTrace();
			}
			meWifiManager.updateNetwork(tempConfig);
		}
	}

}