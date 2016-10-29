package com.lcc.login.demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SetPasswordDialogFragment extends DialogFragment implements View.OnFocusChangeListener,
		 View.OnClickListener,CompoundButton.OnCheckedChangeListener{
	@BindView(R.id.et_password)
	EditText et_password;
	@BindView(R.id.btn_next)
	Button mBtnNext;
	@BindView(R.id.btn_close)
	ImageButton btn_close;
	@BindView(R.id.iv_display)
	CheckBox iv_display;
	@BindView(R.id.loading)
	ProgressBar loading;

	public static SetPasswordDialogFragment newInstance() {
		SetPasswordDialogFragment mFragment = new SetPasswordDialogFragment();
		return mFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_set_password, null);
		ButterKnife.bind(this, view);
		initView(view);
		builder.setView(view);
		return builder.create();
	}

	private void initView(View view){
		et_password.addTextChangedListener(adapter);
		loading.setVisibility(View.GONE);
		mBtnNext.setOnClickListener(this);
		btn_close.setOnClickListener(this);
		iv_display.setOnCheckedChangeListener(this);
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
                if (len >= 6 && len <= 16 ){
					mBtnNext.setEnabled(true);
				} else{
					mBtnNext.setEnabled(false);
				}
		}
	};

	public void showLoading() {
		//等待加载
		loading.setVisibility(View.VISIBLE);
	}

	public void onSetPasswordSuccess() {
		//设置密码成功
		loading.setVisibility(View.GONE);
	}

	public void onSetPasswordFail(String msg) {
		//设置失败
		loading.setVisibility(View.GONE);
	}

	public void onNetworkError(String msg) {
		//网络错误
		loading.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.btn_next:
				break;
			case R.id.btn_close:
				dismiss();
				break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
		if (b){
			et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		}else {
			et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
		}
	}
}
