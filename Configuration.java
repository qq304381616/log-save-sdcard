
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * ��manifest������ android:configChanges="orientation"
 * orientation ��Ļ������ͺ������ת��
 * keyboardHidden ������ʾ�����ء�
 * fontScale �û��������ѡ�������С��
 * locale �û�ѡ���˲�ͬ�������趨��
 * keyboard �������ͱ���������ֻ���12�����л���ȫ����
 * touchscreen��navigation ���̻򵼺���ʽ�仯��һ�㲻�ᷢ���������¼���
 * @author HL
 */
public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Configuration config = getResources().getConfiguration();
		String screen = config.orientation == Configuration.ORIENTATION_LANDSCAPE ? "������Ļ" : "������Ļ";
		int mccCode = config.mcc;// mcc:��ȡ�ƶ��źŵĹ�����
		int mncCode = config.mnc; // mnc:��ȡ�ƶ��źŵ�������
		String naviName = config.orientation == Configuration.NAVIGATION_NONAV ? "û�з������"
				: config.orientation == Configuration.NAVIGATION_WHEEL ? "���ַ������"
						: config.orientation == Configuration.NAVIGATION_DPAD ? "��������Ʒ���" : "�켣����Ʒ���";
		String touchName = config.touchscreen == Configuration.TOUCHSCREEN_NOTOUCH ? "�޴�����"
				: config.touchscreen == Configuration.TOUCHSCREEN_STYLUS ? "������ʽ������" : "������ָ�Ĵ�����";

		// �����ǰΪ����
		if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// ����Ϊ����
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		// �����ǰΪ����
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			// ����Ϊ����
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	/**
	 * ���� android:configChanges="orientation"
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		String screen = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? "����" : "����";
	}
}
