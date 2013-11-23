package com.jackie.step;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.jackie.step.service.StepService;
import com.jackie.step.tools.StepDetector;

/**
 * @author Jackie ʱ�䣺2012.12.12 Activity ������������
 */
public class MainActivity extends Activity {

	private Animation animation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (StepService.FLAG || StepDetector.CURRENT_SETP > 0) {// �����Ѿ�������ֱ����ת�����н���
			Intent intent = new Intent(MainActivity.this, StepActivity.class);
			startActivity(intent);
			this.finish();
		} else {
			this.setContentView(R.layout.activity_main);

			animation = AnimationUtils.loadAnimation(MainActivity.this,
					R.anim.animation_main);
			this.findViewById(R.id.iv_index).setAnimation(animation);

			animation.setAnimationListener(new AnimationListener() {// ��������

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation animation) {// ��������ʱ��ת�����н���
							// TODO Auto-generated method stub
							Intent intent = new Intent(MainActivity.this,
									StepActivity.class);
							MainActivity.this.startActivity(intent);
							MainActivity.this.finish();
						}
					});
		}
	}

}
