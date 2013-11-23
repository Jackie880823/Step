package com.jackie.step.tools;

import java.text.DecimalFormat;

import android.content.Context;

import com.jackie.step.R;

public class Count {

	private Context context;

	public Count(Context context) {
		super();
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	/**
	 * ����ֻ�ܼ������ߵ��˶��仯,�����˶������ڲ�Ϊ0�������,ʵ��Ƶ��Ϊ2 * setp + 1
	 * @return ���ص�ǰ�ܵĲ���
	 */
	public int getStep() {
		return StepDetector.CURRENT_SETP != 0 ? StepDetector.CURRENT_SETP * 2 -1
				: 0;
	}

	/**
	 * ���㲢��ʽ��doubles��ֵ��������λ��Ч����
	 * 
	 * @param doubles
	 * @return ���ص�ǰ·��
	 */
	public String formatDouble(Double doubles) {
		DecimalFormat format = doubles > 9999.99 ? new DecimalFormat("####.#")
				: new DecimalFormat("####.##");
		String distanceStr = format.format(doubles);
		return distanceStr.equals(context.getString(R.string.zero)) ? context
				.getString(R.string.double_zero) : distanceStr;
	}

	/**
	 * �õ�һ����ʽ����ʱ��
	 * 
	 * @param time
	 *            ʱ�� ����
	 * @return ʱ���֣���
	 */
	public String getFormatTime(long time) {
		// long millisecond = time % 1000;
		time = time / 1000;
		long second = time % 60;
		long minute = (time % 3600) / 60;
		long hour = time / 3600;

		// ��������ʾ��λ
		// String strMillisecond = "" + (millisecond / 10);
		// ����ʾ��λ
		String strSecond = ("00" + second)
				.substring(("00" + second).length() - 2);
		// ����ʾ��λ
		String strMinute = ("00" + minute)
				.substring(("00" + minute).length() - 2);
		// ʱ��ʾ��λ
		String strHour = ("00" + hour).substring(("00" + hour).length() - 2);

		return strHour + ":" + strMinute + ":" + strSecond;
		// + strMillisecond;
	}

	/**
	 * �������ߵ�·��
	 */
	public double countDistance(int step_length) {
		return StepDetector.CURRENT_SETP % 2 == 0 ? (StepDetector.CURRENT_SETP / 2)
				* 3 * step_length * 0.01
				: ((StepDetector.CURRENT_SETP / 2) * 3 + 1) * step_length
						* 0.01;
	}
}
