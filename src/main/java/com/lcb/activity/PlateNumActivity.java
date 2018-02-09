package com.lcb.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lcb.R;
import com.lcb.base.BaseActivity;
import com.lcb.bean.DataPlateNum;
import com.lcb.pickerview.OptionsPickerView;
import com.lcb.pickerview.other.pickerViewUtil;
import com.lcb.view.GridPasswordView.GridPasswordView;

import java.util.ArrayList;

public class PlateNumActivity extends BaseActivity implements View.OnClickListener {
    String tag = "PlateNumActivity";
    private GridPasswordView gpvOne, gpvTwo;
    private TextView oneOne, oneTwo, Two, result;
    private ArrayList<String> privince = new ArrayList<>();
    private ArrayList<ArrayList<String>> city = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platenum);
        intView();
    }

    private void intView() {
        findViewById(R.id.platenum_btn).setOnClickListener(this);
        findViewById(R.id.back_platenum).setOnClickListener(this);
        gpvOne = (GridPasswordView) findViewById(R.id.platenum_gpvone);
        gpvTwo = (GridPasswordView) findViewById(R.id.platenum_gpvtwo);
        oneOne = (TextView) findViewById(R.id.platenum_oneone);
        oneTwo = (TextView) findViewById(R.id.platenum_onetwo);
        Two = (TextView) findViewById(R.id.platenum_two);
        result = (TextView) findViewById(R.id.platenum_result);
        oneOne.setOnClickListener(this);
        oneTwo.setOnClickListener(this);
        Two.setOnClickListener(this);
        /*设置成明码状态,默认是暗码所以要切换状态.或者直接设置setPasswordVisibility*/
//        gpvOne.togglePasswordVisibility();
        gpvTwo.togglePasswordVisibility();
        gpvOne.setPasswordVisibility(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_platenum:
                finish();
                break;
            case R.id.platenum_btn:
                result.setText("车牌一:" + oneOne.getText() + oneTwo.getText() + gpvOne.getPassWord() + "\n" +
                        "车牌二:" + Two.getText() + gpvTwo.getPassWord());
                break;
            case R.id.platenum_oneone:
                ChoicePrivince();

                break;
            case R.id.platenum_onetwo:
                ChoiceCity();

                break;
            case R.id.platenum_two:
                ChoicePrivinceCity();
                break;
        }
    }

    /*设置单级联动城市*/
    private void ChoiceCity() {
        privince = DataPlateNum.getcity();
        pickerViewUtil.alertBottomWheelOption(this, "选择城市字母", privince, new pickerViewUtil.OnWheelViewClick() {
            @Override
            public void onClick(View view, int postion) {
                oneTwo.setText(privince.get(postion));
            }
        });
    }

    /*设置单级联动省份*/
    private void ChoicePrivince() {
        privince = DataPlateNum.getPrivince();
        pickerViewUtil.alertBottomWheelOption(this, "选择省份缩写", privince, new pickerViewUtil.OnWheelViewClick() {
            @Override
            public void onClick(View view, int postion) {
                oneOne.setText(privince.get(postion));
            }
        });
    }

    /*通过二级联动选车牌*/
    private void ChoicePrivinceCity() {
        DataPlateNum.initTwo(privince, city);
        String[] labels = {};
        new pickerViewUtil().twoPicker(this, "请选择车牌归属地", new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
//                String tv = privince.get(options1)+ city.get(options1).get(option2)
        /*因为我只加了两个二级参数所以第二options1设为0(我未设置联动所以默认都是0中的)   */
                Two.setText(privince.get(options1) + city.get(0).get(option2));
            }
        }, privince, city, labels, false);

    }

}
