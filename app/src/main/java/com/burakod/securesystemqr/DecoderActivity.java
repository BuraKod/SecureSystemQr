package com.burakod.securesystemqr;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DecoderActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private ImageView noPass,yesPass;
    String qrCode;
    String userName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);
        noPass = findViewById(R.id.nopass);
        yesPass = findViewById(R.id.yespass);


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);

        }
        qrCodeReaderView = findViewById(R.id.qrdecoderview);
        resultTextView = findViewById(R.id.resultCode);

        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
    }

    @Override
    public void onQRCodeRead(final String text, PointF[] points) {
        resultTextView.setText(text);

        int end =  text.indexOf("|");


        if (end == -1){
            resultTextView.setText("Kayıt Bulunamadı.");
        }
        else
        {

            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            userName = text.substring(0,end);
            //resultTextView.setText(userName);
            String child = "QrSecureSystem/Users/"+userName+"/code";
            DatabaseReference ref = database.child(child);

            Query resultQuery = ref.orderByChild(child);
            resultQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                       qrCode =  dataSnapshot.getValue().toString();


                        resultTextView.setText(qrCode);
                        if (text.equals(qrCode)) {
                            yesPass.setVisibility(View.VISIBLE);
                            noPass.setVisibility(View.GONE);
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            startActivity(new Intent(DecoderActivity.this, EventsActivity.class));
                        }
                        else{
                            resultTextView.setText("Qr Code Kayıt Bulunamadı.");
                        }


                }



                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("Testing", "onCancelled", databaseError.toException());
                }
            });





        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }


}
