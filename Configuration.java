
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * 在manifest中配置 android:configChanges="orientation"
 * orientation 屏幕在纵向和横向间旋转。
 * keyboardHidden 键盘显示或隐藏。
 * fontScale 用户变更了首选的字体大小。
 * locale 用户选择了不同的语言设定。
 * keyboard 键盘类型变更，例如手机从12键盘切换到全键盘
 * touchscreen或navigation 键盘或导航方式变化，一般不会发生这样的事件。
 * @author HL
 */
public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Configuration config = getResources().getConfiguration();
		String screen = config.orientation == Configuration.ORIENTATION_LANDSCAPE ? "横向屏幕" : "竖向屏幕";
		int mccCode = config.mcc;// mcc:获取移动信号的国家码
		int mncCode = config.mnc; // mnc:获取移动信号的网络码
		String naviName = config.orientation == Configuration.NAVIGATION_NONAV ? "没有方向控制"
				: config.orientation == Configuration.NAVIGATION_WHEEL ? "滚轮方向控制"
						: config.orientation == Configuration.NAVIGATION_DPAD ? "方向键控制方向" : "轨迹球控制方向";
		String touchName = config.touchscreen == Configuration.TOUCHSCREEN_NOTOUCH ? "无触摸屏"
				: config.touchscreen == Configuration.TOUCHSCREEN_STYLUS ? "触摸笔式触摸屏" : "接收手指的触摸屏";

		// 如果当前为横屏
		if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// 设置为竖屏
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		// 如果当前为竖屏
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			// 设置为横屏
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	/**
	 * 监听 android:configChanges="orientation"
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		String screen = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? "横屏" : "竖屏";
	}
}
