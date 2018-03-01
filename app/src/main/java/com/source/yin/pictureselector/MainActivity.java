package com.source.yin.pictureselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.source.yin.pictureselector.ui.PictureSelectorActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnImageSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnImageSelect = findViewById(R.id.btn_image_select);
        btnImageSelect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_image_select:
                startActivity(new Intent(getApplicationContext(), PictureSelectorActivity.class));
                break;
        }
    }
}
