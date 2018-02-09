package com.lcb.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lcb.R;
import com.lcb.base.BaseApplication;
import com.lcb.constant.SpKey;

/**
 * Description: 登录的Dialog
 * AUTHOR: Champion Dragon
 * created at 2017/12/1
 **/

public class DialogLogin {
    private Dialog mLoadingDialog;
    private EditText user, pwd;

    public DialogLogin(Context context, final putDataListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_login, null);
        user = (EditText) view.findViewById(R.id.loginDialogInputUserName);
        pwd = (EditText) view.findViewById(R.id.loginDialogInputPassword);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.loginDialogCheckBoxRememberMe);

        user.setText(BaseApplication.sp.getString(SpKey.diaUser));
        pwd.setText(BaseApplication.sp.getString(SpKey.diaPwd));

        view.findViewById(R.id.loginCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingDialog.dismiss();
            }
        });
        view.findViewById(R.id.loginOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.putData(user.getText().toString(), pwd.getText().toString());
                if (checkBox.isChecked()) {
                    BaseApplication.sp.putString(SpKey.diaPwd, pwd.getText().toString());
                    BaseApplication.sp.putString(SpKey.diaUser, user.getText().toString());
                } else {
                    BaseApplication.sp.putString(SpKey.diaPwd, "");
                    BaseApplication.sp.putString(SpKey.diaUser, "");
                }
                mLoadingDialog.dismiss();
            }
        });

        DisplayMetrics dm = context.getResources().getDisplayMetrics();// 屏幕宽度
        mLoadingDialog = new Dialog(context, R.style.dialog);
        // 设置返回键无效
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(
                dm.widthPixels * 7 / 8,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();

    }

    public interface putDataListener {
        public void putData(String user, String psw);
    }


}
