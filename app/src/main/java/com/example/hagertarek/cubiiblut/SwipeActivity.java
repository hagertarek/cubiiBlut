package com.example.hagertarek.cubiiblut;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SwipeActivity extends ActionBarActivity {
    ViewPager viewPager ;
    CustomSwipeAdapter adapter ;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private Toolbar toolbar;
    String Name ;
    String Pass ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
      //  dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        // adding bottom dots
//        addBottomDots(0);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        adapter = new CustomSwipeAdapter(this);
        viewPager.setAdapter(adapter);
    }
    private void addBottomDots(int currentPage) {
        dots = new TextView[CustomSwipeAdapter.image_resources.length];
        int[] colorsActive = getResources().getIntArray(R.array.array_pager_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_pager_inactive);
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("•"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
           getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.description_menu) {
            startActivity(new Intent(SwipeActivity.this,DescriptionActivity.class));        }
        else if (id == R.id.contactUs_menu) {
            startActivity(new Intent(SwipeActivity.this,ContactUsActivity.class));        }

        else if (id == R.id.products_menu) {
            startActivity(new Intent(SwipeActivity.this,ProductsActivity.class));        }

        return super.onOptionsItemSelected(item);
    }

    public void onLoginClick(View view) {
        //dialog with style:CustomDialogTheme
        final Dialog dialogLang = new Dialog(this, R.style.CustomDialogTheme);
        //use custom design for dialog
        dialogLang.setContentView(R.layout.login_dialog);
        //set dialog size
        dialogLang.getWindow().setLayout(900, 600);
        dialogLang.setTitle("   Login");
        //define Cancel Button and Listener
        Button dialogButton = (Button) dialogLang.findViewById(R.id.loginButt);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userName = (EditText) dialogLang.findViewById(R.id.userN);
                EditText userPass = (EditText) dialogLang.findViewById(R.id.userP);
                 String name = userName.getText().toString();
                 String pass = userPass.getText().toString();
                dialogLang.dismiss();
                Name = name;
                Pass = pass;
               if(Name.equals("hager")&Pass.equals("123")) {
                   startActivity(new Intent(SwipeActivity.this, MainActivity.class));
               }else{
                   Toast.makeText(SwipeActivity.this, "Wrong User name or password", Toast.LENGTH_SHORT).show();


               }

            }
        });

        dialogLang.show();
    }
}
