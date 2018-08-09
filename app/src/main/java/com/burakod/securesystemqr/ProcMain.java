package com.burakod.securesystemqr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ProcMain extends AppCompatActivity implements View.OnClickListener{
    Button btnGuestCreate ,btnSignOut,btnTest;
    Spinner placeSpinner,blokSpinner,daireSpinner;
    EditText guestFullName;
    String userEmail;
    int userStatus= 0;
    WebView mWebView;
    String userTitle;
    String guestName;
    String guestPlace;
    String guestPlaceBlok;
    String guestPlaceDaireNo;
    String guestQrCode;
    String guestCode;


    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proc_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Name, email address, and profile photo Url

            userEmail = user.getEmail();

            if (userEmail.equals("security@burakod.com"))
            {
                userStatus = 1;
            }

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }




        btnGuestCreate = findViewById(R.id.btnCreateGuest);
        btnGuestCreate.setOnClickListener(this);

        btnSignOut = findViewById(R.id.signout);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        btnTest = findViewById(R.id.test);
        btnSignOut.setOnClickListener(this);
        btnTest.setOnClickListener(this);

        guestFullName = findViewById(R.id.guestFullName);

        //qrCode = findViewById(R.id.qrCode);

        placeSpinner = findViewById(R.id.places_spinner);
        blokSpinner = findViewById(R.id.blokSpinner);
        daireSpinner = findViewById(R.id.daireSpinner);



        ArrayAdapter placeadapter = ArrayAdapter.createFromResource(this,R.array.places,android.R.layout.simple_spinner_item);
        placeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        placeSpinner.setAdapter(placeadapter);

        ArrayAdapter blokadapter = ArrayAdapter.createFromResource(this,R.array.blok,android.R.layout.simple_spinner_item);
        blokadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blokSpinner.setAdapter(blokadapter);

        ArrayAdapter daireadapter = ArrayAdapter.createFromResource(this,R.array.daireno,android.R.layout.simple_spinner_item);
        daireadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daireSpinner.setAdapter(daireadapter);

        if (userStatus == 1){
            btnTest.setVisibility(View.VISIBLE);
        }

        placeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String [] places = getResources().getStringArray(R.array.places);
                guestPlace = places[i];
                btnTest.setText(guestPlace);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        blokSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String [] blok = getResources().getStringArray(R.array.blok);
                guestPlaceBlok = blok[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        daireSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String []daire = getResources().getStringArray(R.array.daireno);
                guestPlaceDaireNo = daire [i] ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });













    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ProcMain.this,MainActivity.class));
        }
        if (view.getId() == R.id.test){


            startActivity(new Intent(ProcMain.this,DecoderActivity.class));

        }
        if (view.getId() == R.id.btnCreateGuest){

            guestName = guestFullName.getText().toString();
            int end =  userEmail.indexOf("@");
            userTitle = userEmail.substring(0,end);
            btnSignOut.setText(userTitle);
            DatabaseReference ref = database.getReference("QrSecureSystem");
            DatabaseReference guestRef = ref.child("Users");

            guestQrCode = "http://api.qrserver.com/v1/create-qr-code/?color=000000&bgcolor=FFFFFF&data="+userTitle+"%7C"+guestName+"%7C"+guestPlace+"%7C"+guestPlaceBlok+"%7C"+guestPlaceDaireNo+"&qzone=1&margin=0&size=150x150&ecc=L";
            //http://api.qrserver.com/v1/create-qr-code/?color=000000&bgcolor=FFFFFF&data=username%7Cguestname%7Cplace%7Cblok%7Cdaireno&qzone=1&margin=0&size=400x400&ecc=L
            guestCode = userTitle+"|"+guestName+"|"+guestPlace+"|"+guestPlaceBlok+"|"+guestPlaceDaireNo;
            guestRef.child(userTitle).setValue(new Guest(guestName,guestPlace,guestPlaceBlok,guestPlaceDaireNo,guestCode,guestQrCode));

            //qrCode.setImageBitmap(getBitmapFromURL(guestQrCode));
            //qrCode.setImageDrawable(loadImageFromURL(guestQrCode,"test"));
            initWebView(guestQrCode);

        }
    }



    private void initWebView(String url) {
        mWebView =  findViewById(R.id.webQrCode);

        // WebViewの設定
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT > 7) {
            settings.setPluginState(WebSettings.PluginState.ON);
        } else {

        }



        mWebView.loadUrl(url);
        //mWebView.loadData(html, "text/html", null);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
    public Drawable loadImageFromURL(String url, String name) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, name);
            return d;
        } catch (Exception e) {
            return null;
        }
    }








}
