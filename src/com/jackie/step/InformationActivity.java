package com.jackie.step;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class InformationActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_inform);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_understand:
			this.finish();
			break;

		default:
			break;
		}
	}
}
