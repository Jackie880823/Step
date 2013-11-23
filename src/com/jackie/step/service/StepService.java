package com.jackie.step.service;

import com.jackie.step.tools.StepDetector;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

/**
 * @author Jackie 2012.12.11 �ǲ�����
 */
public class StepService extends Service {

	public static Boolean FLAG = false;// �������б�־

	private SensorManager mSensorManager;// ����������
	private StepDetector detector;// ��������������

	private PowerManager mPowerManager;// ��Դ�������
	private WakeLock mWakeLock;// ��Ļ��

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		setForeground(true);// ����StepService�����ȼ���

		FLAG = true;// ���Ϊ������������

		// ʵ������������
		detector = new StepDetector(this);

		// ��ȡ�������ķ���
		mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		// ע�ᴫ����
		mSensorManager.registerListener(detector,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_FASTEST);

		// ��Դ�������
		mPowerManager = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, "Jackie");
		mWakeLock.acquire();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		FLAG = false;// ����ֹͣ
		if (detector != null) {
			mSensorManager.unregisterListener(detector);
		}

		if (mWakeLock != null) {
			mWakeLock.release();
		}
	}

}
