package com.lcc.login.demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneDialogFragment extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    @BindView(R.id.btn_close)
    ImageButton btn_close;
    @BindView(R.id.loading)
    ProgressBar loading;

    private String phone;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.phone_dialog, null);
        ButterKnife.bind(this, view);
        initView();
        builder.setView(view);
        return builder.create();
    }

    private void initView() {
        mEtPhone.addTextChangedListener(adapter);
        mEtPhone.setText(phone);
        mBtnNext.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        loading.setVisibility(View.GONE);
    }

    TextWatcherAdapter adapter = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 11 && s.toString().matches("^1[34578]\\d{9}$")) {
                mBtnNext.setEnabled(true);
            } else {
                mBtnNext.setEnabled(false);
            }
        }
    };


    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
    }


    public void CheckPhoneSuccess(String msg) {
        //已经注册
        loading.setVisibility(View.GONE);
    }


    public void CheckPhoneFail(String msg) {
        //没有注册
        loading.setVisibility(View.GONE);
        toLoginPage();
    }


    public void onNetworkError(String msg) {
        toLoginPage();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                showLoading();
                toLoginPage();
                break;
            case R.id.btn_close:
                dismiss();
                break;
        }
    }

    private void toLoginPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginDialogFragment fragment = LoginDialogFragment.newInstance(phone);
                fragment.show(getFragmentManager(), "login");
                dismiss();
            }
        }, 2000);

    }
}
