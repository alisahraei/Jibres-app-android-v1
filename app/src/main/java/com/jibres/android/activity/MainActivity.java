package com.jibres.android.activity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jibres.android.JibresApplication;
import com.jibres.android.R;
import com.jibres.android.activity.enter.EnterActivity;
import com.jibres.android.activity.language.LanguageActivity;
import com.jibres.android.activity.language.LanguageManager;
import com.jibres.android.activity.notif.NotifViewActivity;
import com.jibres.android.activity.security.fingerprint.FingerprintActivity;
import com.jibres.android.activity.security.pincode.PincodeActivity;
import com.jibres.android.activity.setting.SettingsActivity;
import com.jibres.android.activity.tiket.activity.TiketListActivity;
import com.jibres.android.managers.AppManager;
import com.jibres.android.managers.UrlManager;

public class MainActivity extends AppCompatActivity {
    String[] ac = {"EnterActivity","LanguageActivity",
            "SettingsActivity","key","FingerpringActivity",
            "PinCode","ListTiket","NotifViewActivity"
            /*"TestUplloadFile"*/
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("AppManager", "apikey: "+ AppManager.getApikey(this));
        Intent intent = new Intent(this,WebViewActivity.class);
        intent.putExtra("url", UrlManager.get.local+"/"+
                LanguageManager.getAppLanguage(this));
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((JibresApplication) getApplication()).refreshLocale(this);

        LinearLayout layout = findViewById(R.id.linear_layout);
        TextView textView;
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10,10,10,10);

        for (int i = 0; i < ac.length; i++) {
            textView = new TextView(this);
            textView.setText(ac[i]);
            int finalI = i;
            textView.setOnClickListener(view -> onClicks(ac[finalI]));
            textView.setLayoutParams(layoutParams);
            textView.setBackgroundResource(R.drawable.row_background);
            textView.setPadding(10,10,10,10);
            layout.addView(textView);
        }
    }

    private void onClicks(String activityName){
        Intent intent = null;
        switch (activityName){
            case "EnterActivity":
                intent = new Intent(getApplication(), EnterActivity.class);
                break;
            case "LanguageActivity":
                intent= new Intent(getApplication(), LanguageActivity.class);
                break;
            case "SettingsActivity":
                intent= new Intent(getApplication(), SettingsActivity.class);
                break;
            case "key":
                KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Intent intents = km.createConfirmDeviceCredentialIntent("TITLE","DESC");
                    startActivityForResult(intents,RESULT_FIRST_USER);
                }
                break;
            case "FingerpringActivity":
                intent= new Intent(getApplication(), FingerprintActivity.class);
                break;
            case "PinCode":
                intent= new Intent(getApplication(), PincodeActivity.class);
                break;
            case "ListTiket":
                intent= new Intent(getApplication(), TiketListActivity.class);
                break;
            case "NotifViewActivity":
                intent= new Intent(getApplication(), NotifViewActivity.class);
                break;
            /*case "TestUplloadFile":
                intent= new Intent(getApplication(), TestUploadFileActivity.class);
                break;*/
        }
        if (intent!=null){
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }
}
