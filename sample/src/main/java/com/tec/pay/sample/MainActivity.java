package com.tec.pay.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.google.gson.Gson;
import com.tec.pay.android.TecPay;
import com.tec.pay.android.TecPayParam;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements OnClickListener {

  Button button;
  Spinner spinner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    button = findViewById(R.id.button);
    button.setOnClickListener(this);

    spinner = findViewById(R.id.sp_urls);
    List<String> list = new ArrayList<>();
    list.add("http://tecpay-web.s3-website-us-east-1.amazonaws.com/");
    list.add("http://192.168.166.23:8080");
    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
        android.R.layout.simple_spinner_item, list);
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(dataAdapter);

    TecPay.init(getApplicationContext(), "a", "a");
  }

  @Override
  public void onClick(View v) {
    if (button.equals(v)) {
      TecPay.pay(TecPayParam.newBuilder().passBack((String) spinner.getSelectedItem()).build(),
          result -> {
        Log.d("PayCallback", new Gson().toJson(result));
      });
    }

  }

}
