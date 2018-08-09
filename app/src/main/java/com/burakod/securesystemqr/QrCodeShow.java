package com.burakod.securesystemqr;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class QrCodeShow extends AppCompatActivity implements View.OnClickListener {

    String qrCodeLink;
    Button btnShare,btnMain;
    WebView mWebView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_show);
        qrCodeLink = getIntent().getStringExtra("QRCODELINK");
        btnShare = findViewById(R.id.btn_share);
        btnMain = findViewById(R.id.btn_backMain);
        btnMain.setOnClickListener(this);
        btnShare.setOnClickListener(this);

        initWebView(qrCodeLink);
        
    }

    private void initWebView(String qrCodeLink) {

        mWebView =  findViewById(R.id.webQrCode);


        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT > 7) {
            settings.setPluginState(WebSettings.PluginState.ON);
        } else {
            Toast.makeText(this, "Beklenmedik bir hata oluştu.", Toast.LENGTH_SHORT).show();
        }



        mWebView.loadUrl(qrCodeLink);
        //mWebView.loadData(html, "text/html", null);
    }

    public void onClickWhatsApp(View view) {

        PackageManager pm=getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "YOUR TEXT HERE";

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, qrCodeLink);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.btn_backMain){
            startActivity(new Intent(QrCodeShow.this,EventsActivity.class));
        }
        if (view.getId()== R.id.btn_share)
        {
            onClickWhatsApp(view);
        }

    }
}
