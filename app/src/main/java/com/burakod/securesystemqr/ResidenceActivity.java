package com.burakod.securesystemqr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResidenceActivity extends AppCompatActivity implements View.OnClickListener{

    Spinner placeSpinner,blokSpinner,daireSpinner;
    Button btnBack,btnGuestCreate;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    EditText guestFullName;
    String guestQrCode;
    String guestCode;
    String userEmail;
    String userTitle;
    String guestName;
    String guestPlace;
    String guestPlaceBlok;
    String guestPlaceDaireNo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residence);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        btnGuestCreate = findViewById(R.id.btnCreateGuest);
        btnGuestCreate.setOnClickListener(this);
        guestFullName = findViewById(R.id.guestFullName);




        /*Spinner Setup*/

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

        placeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String [] places = getResources().getStringArray(R.array.places);
                guestPlace = places[i];

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

        /*Spinner Setup*/








        /*
        *
        *
        * */
    }

    @Override
    public void onClick(View view) {

        if (view.getId()== R.id.btn_back)
        {
            startActivity(new Intent(ResidenceActivity.this,EventsActivity.class));
        }
        if (view.getId()== R.id.btnCreateGuest)
        {
            CreateGuest();
        }

    }

    private void CreateGuest() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userEmail = user.getEmail();
        guestName = guestFullName.getText().toString();
        int end =  userEmail.indexOf("@");
        userTitle = userEmail.substring(0,end);

        DatabaseReference ref = database.getReference("QrSecureSystem");
        DatabaseReference guestRef = ref.child("Users");

        guestQrCode = "http://api.qrserver.com/v1/create-qr-code/?color=000000&bgcolor=FFFFFF&data="+userTitle+"%7C"+guestName+"%7C"+guestPlace+"%7C"+guestPlaceBlok+"%7C"+guestPlaceDaireNo+"&qzone=1&margin=0&size=800x1200&ecc=L";
        //http://api.qrserver.com/v1/create-qr-code/?color=000000&bgcolor=FFFFFF&data=username%7Cguestname%7Cplace%7Cblok%7Cdaireno&qzone=1&margin=0&size=400x400&ecc=L
        guestCode = userTitle+"|"+guestName+"|"+guestPlace+"|"+guestPlaceBlok+"|"+guestPlaceDaireNo;
        guestRef.child(userTitle).setValue(new Guest(guestName,guestPlace,guestPlaceBlok,guestPlaceDaireNo,guestCode,guestQrCode));


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(ResidenceActivity.this, QrCodeShow.class);
        i.putExtra("QRCODELINK", guestQrCode);
        startActivity(i);



    }
}
