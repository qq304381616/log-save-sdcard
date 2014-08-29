import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity {

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			System.out.println("handler");
			super.handleMessage(msg);
		}
	};

	TimerTask task = new TimerTask() {
		public void run() {
			System.out.println("timer");
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Timer timer = new Timer(true);
		timer.schedule(task, 1000, 2000); // 延时1000ms后执行，1000ms执行一次
		// timer.cancel(); //退出计时器
	}
}
