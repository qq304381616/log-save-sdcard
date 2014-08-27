package com.example.mywifi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.widget.Toast;

public class WifiFunction {
	// ����һ��WifiManager����
	private static WifiManager meWifiManager;
	// ����һ��WifiInfo����
	private WifiInfo meWifiInfo;
	// ɨ��������������б�
	private List<ScanResult> meWifiList;
	// ���������б�
	private List<WifiConfiguration> meWifiConfigurations;
	WifiLock meWifiLock;

	public WifiFunction(Context context) {
		// ȡ��WifiManager����
		meWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// ȡ��WifiInfo����
		meWifiInfo = meWifiManager.getConnectionInfo();
	}

	// ��wifi
	public void openWifi() {
		if (!meWifiManager.isWifiEnabled()) {
			meWifiManager.setWifiEnabled(true);
		}
	}

	// �ر�wifi
	public void closeWifi() {
		if (!meWifiManager.isWifiEnabled()) {
			meWifiManager.setWifiEnabled(false);
		}
	}

	// ��鵱ǰwifi״̬
	public int checkState() {
		return meWifiManager.getWifiState();
	}

	// ����wifiLock
	public void acquireWifiLock() {
		meWifiLock.acquire();
	}

	// ����wifiLock
	public void releaseWifiLock() {
		// �ж��Ƿ�����
		if (meWifiLock.isHeld()) {
			meWifiLock.acquire();
		}
	}

	// ����һ��wifiLock
	public void createWifiLock() {
		meWifiLock = meWifiManager.createWifiLock("test");
	}

	// �õ����úõ�����
	public List<WifiConfiguration> getConfiguration() {
		return meWifiConfigurations;
	}

	// ָ�����úõ������������
	public void connetionConfiguration(int index) {
		if (index > meWifiConfigurations.size()) {
			return;
		}
		// �������ú�ָ��ID������
		meWifiManager.enableNetwork(meWifiConfigurations.get(index).networkId,
				true);
	}

	public void startScan() {
		meWifiManager.startScan();
		// �õ�ɨ����
		meWifiList = meWifiManager.getScanResults();
		// �õ����úõ���������
		meWifiConfigurations = meWifiManager.getConfiguredNetworks();
	}

	// �õ������б�
	public List<ScanResult> getWifiList() {
		return meWifiList;
	}

	// �鿴ɨ����
	public StringBuffer lookUpScan() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < meWifiList.size(); i++) {
			sb.append("Index_" + new Integer(i + 1).toString() + ":");
			// ��ScanResult��Ϣת����һ���ַ�����
			// ���аѰ�����BSSID��SSID��capabilities��frequency��level
			sb.append((meWifiList.get(i)).toString()).append("\n"+"~");
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

	// �õ����ӵ�ID
	public int getNetWordId() {
		return (meWifiInfo == null) ? 0 : meWifiInfo.getNetworkId();
	}

	// �õ�wifiInfo��������Ϣ
	public String getWifiInfo() {
		return (meWifiInfo == null) ? "NULL" : meWifiInfo.toString();
	}

	// ���һ�����粢����
	public void addNetWork(WifiConfiguration configuration) {
		int wcgId = meWifiManager.addNetwork(configuration);
		meWifiManager.enableNetwork(wcgId, true);
	}

	// �Ͽ�ָ��ID������
	public void disConnectionWifi(int netId) {
		meWifiManager.disableNetwork(netId);
		meWifiManager.disconnect();
	}

	// �������ӷ�ʽ
	public WifiConfiguration CreateWifiInfo(String SSID, String Password,
			int Type) {
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
		if (Type == 3) // WIFICIPHER_WPA
		{
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

	public static WifiConfiguration IsExsits(String SSID) {
		List<WifiConfiguration> existingConfigs = meWifiManager
				.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}
		return null;
	}

	public static void setIpAssignment(String assign, WifiConfiguration wifiConf)
			throws SecurityException, IllegalArgumentException,
			NoSuchFieldException, IllegalAccessException {
		setEnumField(wifiConf, assign, "ipAssignment");
	}

	public static void setIpAddress(InetAddress addr, int prefixLength,
			WifiConfiguration wifiConf) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException, NoSuchMethodException,
			ClassNotFoundException, InstantiationException,
			InvocationTargetException {
		Object linkProperties = getField(wifiConf, "linkProperties");
		if (linkProperties == null)
			return;
		Class laClass = Class.forName("android.net.LinkAddress");
		Constructor laConstructor = laClass.getConstructor(new Class[] {
				InetAddress.class, int.class });
		Object linkAddress = laConstructor.newInstance(addr, prefixLength);

		ArrayList mLinkAddresses = (ArrayList) getDeclaredField(linkProperties,
				"mLinkAddresses");
		mLinkAddresses.clear();
		mLinkAddresses.add(linkAddress);
	}
	public static void setGateway(InetAddress gateway,
			WifiConfiguration wifiConf) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException, ClassNotFoundException,
			NoSuchMethodException, InstantiationException,
			InvocationTargetException {
		Object linkProperties = getField(wifiConf, "linkProperties");
		if (linkProperties == null)
			return;
		Class routeInfoClass = Class.forName("android.net.RouteInfo");
		Constructor routeInfoConstructor = routeInfoClass
				.getConstructor(new Class[] { InetAddress.class });
		Object routeInfo = routeInfoConstructor.newInstance(gateway);

		ArrayList mRoutes = (ArrayList) getDeclaredField(linkProperties,
				"mRoutes");
		mRoutes.clear();
		mRoutes.add(routeInfo);
	}

	public static void setDNS(InetAddress dns, WifiConfiguration wifiConf)
			throws SecurityException, IllegalArgumentException,
			NoSuchFieldException, IllegalAccessException {
		Object linkProperties = getField(wifiConf, "linkProperties");
		if (linkProperties == null)
			return;

		ArrayList<InetAddress> mDnses = (ArrayList<InetAddress>) getDeclaredField(
				linkProperties, "mDnses");
		mDnses.clear(); // or add a new dns address , here I just want to
						// replace DNS1
		mDnses.add(dns);
	}

	public static Object getField(Object obj, String name)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getField(name);
		Object out = f.get(obj);
		return out;
	}

	public static Object getDeclaredField(Object obj, String name)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getDeclaredField(name);
		f.setAccessible(true);
		Object out = f.get(obj);
		return out;
	}

	public static void setEnumField(Object obj, String value, String name)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getField(name);
		f.set(obj, Enum.valueOf((Class<Enum>) f.getType(), value));
	}

	public static void set_static(String SSID,String static_ip,String static_gateway,String static_dns) {

		WifiConfiguration tempConfig = WifiFunction.IsExsits(SSID);
		if (tempConfig != null) {
			try {
				setIpAssignment("STATIC", tempConfig);

				setIpAddress(InetAddress.getByName(static_ip), 24,
						tempConfig);

				setGateway(InetAddress.getByName(static_gateway), tempConfig);

				setDNS(InetAddress.getByName(static_dns), tempConfig);

			} catch (Exception e) {

				e.printStackTrace();
			}
			meWifiManager.updateNetwork(tempConfig);
		}
	}
	
}
