package com.lcc.login.demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 其实发送和校验验证码应该是在一起的一个页面，既然分开就分开吧（mvp）
 */
public class VcodeDialogFragment extends DialogFragment implements View.OnFocusChangeListener,
		View.OnClickListener{
	@BindView(R.id.et_vcode)
	EditText et_vcode;
	@BindView(R.id.btn_next)
	Button mBtnNext;
	@BindView(R.id.send_v_code)
	TextView send_v_code;
	@BindView(R.id.btn_close)
	ImageButton btn_close;
	@BindView(R.id.loading)
	ProgressBar loading;

	private String phone;
	protected Handler taskHandler = new Handler();
	private static final int DELAY_MILLIS = 1 * 1000;
	private int verifyCodeCountdown = 60;

	public static VcodeDialogFragment newInstance(String phone) {
		VcodeDialogFragment mFragment = new VcodeDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString("phone", phone);
		mFragment.setArguments(bundle);
		return mFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.vcode_dialog, null);
		ButterKnife.bind(this, view);
		loading.setVisibility(View.GONE);
		initView();
		builder.setView(view);
		return builder.create();
	}

	private void initView(){
		phone = getArguments().getString("phone");
		et_vcode.addTextChangedListener(adapter);
		mBtnNext.setOnClickListener(this);
		send_v_code.setOnClickListener(this);
		btn_close.setOnClickListener(this);
	}

	@Override
	public void onFocusChange(View view, boolean b) {
		PhoneDialogFragment dialog = new PhoneDialogFragment();
		dialog.show(getFragmentManager(), "phonedailog");
		dismiss();
	}

	TextWatcherAdapter adapter = new TextWatcherAdapter() {
		@Override
		public void afterTextChanged(Editable s) {
                int len = s.length();
                if (len == 4 ){
					mBtnNext.setEnabled(true);
				} else{
					mBtnNext.setEnabled(false);
				}
		}
	};

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.btn_next:
				toSetPasswordPage();
				break;
			case R.id.send_v_code:
				showVerifySuccess();
				break;
			case R.id.btn_close:
				dismiss();
				break;
		}
	}

	public void SendVcodeonNetworkError(String msg) {
		//发送验证吗的时候发生网络错误
		loading.setVisibility(View.GONE);
		showVerifySuccess();
	}


	public void onRequestVcodeSuccess() {
		loading.setVisibility(View.GONE);
		showVerifySuccess();
	}

	/**
	 * 点击之后改变按钮的事件
	 */
	public void showVerifySuccess() {
		verifyCodeCountdown = 60;
		send_v_code.setClickable(false);
		taskHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (verifyCodeCountdown == 0) {
					send_v_code.setClickable(true);
					send_v_code.setText("点击后重新发送");
					return;
				}
				send_v_code.setText(verifyCodeCountdown + "秒后重新发送");
				verifyCodeCountdown--;
				taskHandler.postDelayed(this, DELAY_MILLIS);
			}
		}, DELAY_MILLIS);
	}

	private void toSetPasswordPage(){
		SetPasswordDialogFragment fragment = SetPasswordDialogFragment.newInstance();
		fragment.show(getFragmentManager(), "SetPasswordDialogFragment");
		dismiss();
	}
}
