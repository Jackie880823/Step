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
	 * 由于只能监听单边的运动变化,所以运动步数在不为0的情况下,实质频数为2 * setp + 1
	 * @return 返回当前总的步数
	 */
	public int getStep() {
		return StepDetector.CURRENT_SETP != 0 ? StepDetector.CURRENT_SETP * 2 -1
				: 0;
	}

	/**
	 * 计算并格式化doubles数值，保留两位有效数字
	 * 
	 * @param doubles
	 * @return 返回当前路程
	 */
	public String formatDouble(Double doubles) {
		DecimalFormat format = doubles > 9999.99 ? new DecimalFormat("####.#")
				: new DecimalFormat("####.##");
		String distanceStr = format.format(doubles);
		return distanceStr.equals(context.getString(R.string.zero)) ? context
				.getString(R.string.double_zero) : distanceStr;
	}

	/**
	 * 得到一个格式化的时间
	 * 
	 * @param time
	 *            时间 毫秒
	 * @return 时：分：秒
	 */
	public String getFormatTime(long time) {
		// long millisecond = time % 1000;
		time = time / 1000;
		long second = time % 60;
		long minute = (time % 3600) / 60;
		long hour = time / 3600;

		// 毫秒秒显示两位
		// String strMillisecond = "" + (millisecond / 10);
		// 秒显示两位
		String strSecond = ("00" + second)
				.substring(("00" + second).length() - 2);
		// 分显示两位
		String strMinute = ("00" + minute)
				.substring(("00" + minute).length() - 2);
		// 时显示两位
		String strHour = ("00" + hour).substring(("00" + hour).length() - 2);

		return strHour + ":" + strMinute + ":" + strSecond;
		// + strMillisecond;
	}

	/**
	 * 计算行走的路程
	 */
	public double countDistance(int step_length) {
		return StepDetector.CURRENT_SETP % 2 == 0 ? (StepDetector.CURRENT_SETP / 2)
				* 3 * step_length * 0.01
				: ((StepDetector.CURRENT_SETP / 2) * 3 + 1) * step_length
						* 0.01;
	}
}
