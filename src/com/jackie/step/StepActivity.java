package com.jackie.step;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jackie.step.service.StepService;
import com.jackie.step.tools.Count;
import com.jackie.step.tools.StepDetector;

/**
 * @author Jackie ʱ�䣺2012.12.10 Activity ������
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 * 
 */
@SuppressLint("HandlerLeak")
public class StepActivity extends Activity {

	private static final String STEP_ACTIVITY = "StepActivity";
	private TextView tv_show_step;// ����
	private TextView tv_week_day;// ����
	private TextView tv_date;// ����

	private TextView tv_timer;// ����ʱ��

	private TextView tv_distance;// �г�
	private TextView tv_calories;// ��·��
	private TextView tv_velocity;// �ٶ�

	private Button btn_start;// ��ʼ��ť
	private Button btn_stop;// ֹͣ��ť

	// �Ǳ�
	private ImageView iv_star_1;
	private ImageView iv_star_2;
	private ImageView iv_star_3;
	private ImageView iv_star_4;
	private ImageView iv_star_5;
	private ImageView iv_star_6;
	private ImageView iv_star_7;
	private ImageView iv_star_8;
	private ImageView iv_star_9;
	private ImageView iv_star_10;

	private View mMenuView;
	private PopupWindow mPopupWindow;
	private ListView mMenuList;

	private Count count;

	private long timer = 0;// �˶�ʱ��
	private static long startTimer = 0;// ��ʼʱ��

	private static long tempTime = 0;

	private Double distance = 0.0;// ·�̣���
	private Double calories = 0.0;// ��������·��
	private Double velocity = 0.0;// �ٶȣ���ÿ��

	private int step_length = 0;
	private int weight = 0;
	private int total_step = 0;

	private Thread thread;

	Handler handler = new Handler() {// Handler�������ڸ��µ�ǰ����

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			distance = count.countDistance(step_length);

			if (timer != 0 && distance != 0.0) {

				// ���ء�����
				// �ܲ�������kcal�������أ�kg�������루�����1.036
				calories = weight * distance * 0.001;

				velocity = distance * 1000 / timer;
			} else {
				calories = 0.0;
				velocity = 0.0;
			}

			if (count == null) {
				count = new Count(StepActivity.this);
			}

			total_step = count.getStep();

			tv_show_step.setText(total_step + "");// ��ʾ��ǰ����

			tv_distance.setText(count.formatDouble(distance));// ��ʾ·��
			tv_calories.setText(count.formatDouble(calories));// ��ʾ��·��
			tv_velocity.setText(count.formatDouble(velocity));// ��ʾ�ٶ�

			tv_timer.setText(count.getFormatTime(timer));// ��ʾ��ǰ����ʱ��

			changeStep(total_step);// ���õ�ǰ�������Ǳ�

		}

		/**
		 * ���õ�ǰ�������Ǳ�
		 */
		private void changeStep(int total_step) {
			int level = total_step / 1000;
			switch (level) {
			case 10:
				iv_star_10.setImageResource(R.drawable.img_star);
			case 9:
				iv_star_9.setImageResource(R.drawable.img_star);
			case 8:
				iv_star_8.setImageResource(R.drawable.img_star);
			case 7:
				iv_star_7.setImageResource(R.drawable.img_star);
			case 6:
				iv_star_6.setImageResource(R.drawable.img_star);
			case 5:
				iv_star_5.setImageResource(R.drawable.img_star);
			case 4:
				iv_star_4.setImageResource(R.drawable.img_star);
			case 3:
				iv_star_3.setImageResource(R.drawable.img_star);
			case 2:
				iv_star_2.setImageResource(R.drawable.img_star);
			case 1:
				iv_star_1.setImageResource(R.drawable.img_star);
				break;
			case 0:
				iv_star_1.setImageResource(R.drawable.img_star_bc);
				iv_star_2.setImageResource(R.drawable.img_star_bc);
				iv_star_3.setImageResource(R.drawable.img_star_bc);
				iv_star_4.setImageResource(R.drawable.img_star_bc);
				iv_star_5.setImageResource(R.drawable.img_star_bc);
				iv_star_6.setImageResource(R.drawable.img_star_bc);
				iv_star_7.setImageResource(R.drawable.img_star_bc);
				iv_star_8.setImageResource(R.drawable.img_star_bc);
				iv_star_9.setImageResource(R.drawable.img_star_bc);
				iv_star_10.setImageResource(R.drawable.img_star_bc);
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_step);

		if (SettingsActivity.sharedPreferences == null) {
			SettingsActivity.sharedPreferences = this.getSharedPreferences(
					SettingsActivity.SETP_SHARED_PREFERENCES,
					Context.MODE_PRIVATE);
		}

		count = new Count(StepActivity.this);

		if (thread == null) {

			thread = new Thread() {// ���߳����ڼ�����ǰ�����ı仯

				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					int temp = 0;
					while (true) {
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (StepService.FLAG) {
							Message msg = new Message();
							if (temp != StepDetector.CURRENT_SETP) {
								temp = StepDetector.CURRENT_SETP;
							}
							if (startTimer != System.currentTimeMillis()) {
								timer = tempTime + System.currentTimeMillis()
										- startTimer;
							}
							handler.sendMessage(msg);// ֪ͨ���߳�
						}
					}
				}
			};
			thread.start();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// ��ȡ����ؼ�
		addView();

		// ��ʼ���ؼ�
		init();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 * ��ȡActivity��ؿؼ�
	 */
	private void addView() {
		tv_show_step = (TextView) this.findViewById(R.id.show_step);
		tv_week_day = (TextView) this.findViewById(R.id.week_day);
		tv_date = (TextView) this.findViewById(R.id.date);

		tv_timer = (TextView) this.findViewById(R.id.timer);

		tv_distance = (TextView) this.findViewById(R.id.distance);
		tv_calories = (TextView) this.findViewById(R.id.calories);
		tv_velocity = (TextView) this.findViewById(R.id.velocity);

		btn_start = (Button) this.findViewById(R.id.start);
		btn_stop = (Button) this.findViewById(R.id.stop);

		// �Ǳ�
		iv_star_1 = (ImageView) this.findViewById(R.id.iv_1);
		iv_star_2 = (ImageView) this.findViewById(R.id.iv_2);
		iv_star_3 = (ImageView) this.findViewById(R.id.iv_3);
		iv_star_4 = (ImageView) this.findViewById(R.id.iv_4);
		iv_star_5 = (ImageView) this.findViewById(R.id.iv_5);
		iv_star_6 = (ImageView) this.findViewById(R.id.iv_6);
		iv_star_7 = (ImageView) this.findViewById(R.id.iv_7);
		iv_star_8 = (ImageView) this.findViewById(R.id.iv_8);
		iv_star_9 = (ImageView) this.findViewById(R.id.iv_9);
		iv_star_10 = (ImageView) this.findViewById(R.id.iv_10);

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.menu, null);
		mMenuList = (ListView) mMenuView.findViewById(R.id.menuList);
		mPopupWindow = new PopupWindow(mMenuView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
		mPopupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
	}

	/**
	 * ��ʼ������
	 */
	private void init() {

		step_length = SettingsActivity.sharedPreferences.getInt(
				SettingsActivity.STEP_LENGTH_VALUE, 70);
		weight = SettingsActivity.sharedPreferences.getInt(
				SettingsActivity.WEIGHT_VALUE, 50);

		distance = count.countDistance(step_length);
		total_step = count.getStep();
		if ((timer += tempTime) != 0 && distance != 0.0) {

			// ���ء�����
			// �ܲ�������kcal�������أ�kg�������루�����1.036
			calories = weight * distance * 0.001;

			velocity = distance * 1000 / timer;
		} else {
			calories = 0.0;
			velocity = 0.0;
		}

		tv_timer.setText(count.getFormatTime(timer + tempTime));

		tv_distance.setText(count.formatDouble(distance));
		tv_calories.setText(count.formatDouble(calories));
		tv_velocity.setText(count.formatDouble(velocity));

		tv_show_step.setText(total_step + "");

		btn_start.setEnabled(!StepService.FLAG);
		btn_stop.setEnabled(StepService.FLAG);

		if (StepService.FLAG) {
			btn_stop.setText(getString(R.string.pause));
		} else if (StepDetector.CURRENT_SETP > 0) {
			btn_stop.setEnabled(true);
			btn_stop.setText(getString(R.string.cancel));
		}

		setDate();

		String[] strings = { "����", "˵��", "�������߿���" };
		ListViewAdapter adapter = new ListViewAdapter(strings, this);
		mMenuList.setAdapter(adapter);

		mMenuList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					intent = new Intent(StepActivity.this,
							SettingsActivity.class);
					startActivity(intent);
					break;

				case 1:
					intent = new Intent(StepActivity.this,
							InformationActivity.class);
					startActivity(intent);
					break;
				case 2:
					Uri appUri = Uri
							.parse("mstore:http://app.meizu.com/phone/apps/3b229850a74b443086f05436b10f509c");
					intent = new Intent(Intent.ACTION_VIEW, appUri);
					startActivity(intent);
					break;
				}
				mPopupWindow.dismiss();
			}
		});
		mMenuList.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub

				switch (keyCode) {
				case KeyEvent.KEYCODE_MENU:
					showMenuWindow();
					break;
				}

				return false;
			}
		});

	}

	/**
	 * ������ʾ������
	 */
	private void setDate() {
		Calendar mCalendar = Calendar.getInstance();// ��ȡ����Calendar����
		int weekDay = mCalendar.get(Calendar.DAY_OF_WEEK);// ���������
		int month = mCalendar.get(Calendar.MONTH) + 1;// ��ǰ�·�
		int day = mCalendar.get(Calendar.DAY_OF_MONTH);// ��ǰ����

		tv_date.setText(month + getString(R.string.month) + day
				+ getString(R.string.day));// ��ʾ��ǰ����

		String week_day_str = new String();
		switch (weekDay) {
		case Calendar.SUNDAY:// ������
			week_day_str = getString(R.string.sunday);
			break;

		case Calendar.MONDAY:// ����һ
			week_day_str = getString(R.string.monday);
			break;

		case Calendar.TUESDAY:// ���ڶ�
			week_day_str = getString(R.string.tuesday);
			break;

		case Calendar.WEDNESDAY:// ������
			week_day_str = getString(R.string.wednesday);
			break;

		case Calendar.THURSDAY:// ������
			week_day_str = getString(R.string.thursday);
			break;

		case Calendar.FRIDAY:// ������
			week_day_str = getString(R.string.friday);
			break;

		case Calendar.SATURDAY:// ������
			week_day_str = getString(R.string.saturday);
			break;
		}
		tv_week_day.setText(week_day_str);
	}

	public void onClick(View view) {
		Intent service = new Intent(this, StepService.class);
		switch (view.getId()) {
		case R.id.start:
			startService(service);
			btn_start.setEnabled(false);
			btn_stop.setEnabled(true);
			btn_stop.setText(getString(R.string.pause));
			startTimer = System.currentTimeMillis();
			tempTime = timer;
			break;

		case R.id.stop:
			if (StepService.FLAG && StepDetector.CURRENT_SETP > 0) {
				stopService(service);
				btn_stop.setText(getString(R.string.cancel));
			} else {
				StepDetector.CURRENT_SETP = 0;
				tempTime = timer = 0;

				btn_stop.setText(getString(R.string.pause));
				btn_stop.setEnabled(false);

				tv_timer.setText(count.getFormatTime(timer));

				tv_show_step.setText("0");
				tv_distance.setText(count.formatDouble(0.0));
				tv_calories.setText(count.formatDouble(0.0));
				tv_velocity.setText(count.formatDouble(0.0));

				handler.removeCallbacks(thread);
			}
			btn_start.setEnabled(true);
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Log.i(STEP_ACTIVITY, "onCreateOptionsMenu");
		menu.add("");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		showMenuWindow();
		return false;
	}

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // TODO Auto-generated method stub
	// switch (item.getItemId()) {
	// case R.id.menu_settings:
	// Intent intent = new Intent(this, SettingsActivity.class);
	// startActivity(intent);
	// break;
	//
	// case R.id.ment_information:
	// break;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	private void showMenuWindow() {

		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		} else {
			mPopupWindow.showAtLocation(findViewById(R.id.releativelayout),
					Gravity.BOTTOM | Gravity.RIGHT, 0, 0);
		}

	}

}
