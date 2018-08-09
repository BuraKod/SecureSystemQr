package com.burakod.securesystemqr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EventsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mResidence,mRestuarant,mEvents,mSignOut,mScan;
    TextView txtScan;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        mResidence = findViewById(R.id.ImageResidence);
        mSignOut = findViewById(R.id.ImageSignOut);
        mScan = findViewById(R.id.ImageScan);
        txtScan = findViewById(R.id.TextScan);
        mScan.setOnClickListener(this);
        mResidence.setOnClickListener(this);
        mSignOut.setOnClickListener(this);

        if (UsersStatu() == 1) {mScan.setVisibility(View.VISIBLE); txtScan.setVisibility(View.VISIBLE);}





    }

    private int UsersStatu() {
        int result = 0 ;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail ;

        userEmail = user.getEmail();

        if (userEmail.equals("security@burakod.com"))
        {
            result = 1;
        }

        return result;
    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.ImageResidence){
            startActivity(new Intent(EventsActivity.this,ResidenceActivity.class));
        }
        if (view.getId()== R.id.ImageSignOut)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(EventsActivity.this,MainActivity.class));
        }
        if(view.getId() == R.id.ImageScan)
        {
            startActivity(new Intent(EventsActivity.this,DecoderActivity.class));
        }
    }
}
