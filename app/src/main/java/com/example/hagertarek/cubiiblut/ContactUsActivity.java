package com.example.hagertarek.cubiiblut;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
    }

    public void onMailClick(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@cubii.co"});
        startActivity(Intent.createChooser(intent, "Send Email"));

    }

    public void onWebClick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cubii.co/"));
        startActivity(browserIntent);
    }

    public void onPhoneClick(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:01000068843"));
        startActivity(intent);
    }
}
