package com.dryfire.sold.Developer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.dryfire.sold.R;

import java.util.ArrayList;

public class SupportDevelopment extends AppCompatActivity implements View.OnClickListener {

    private CardView coffeeButton;
    private CardView pepsiButton;
    private CardView cookieButton;
    private CardView mealButton;
    private CardView tshirtButton;
    private CardView truegiverButton;
    private Toolbar toolbar;
    final int UPI_PAYMENT = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.development_support);

        coffeeButton = findViewById(R.id.buy_coffee);
        pepsiButton = findViewById(R.id.buy_pepsi);
        cookieButton = findViewById(R.id.buy_cookie);
        mealButton = findViewById(R.id.buy_meal);
        tshirtButton = findViewById(R.id.buy_tshirt);
        truegiverButton = findViewById(R.id.true_giver);
        toolbar = findViewById(R.id.suppprt_tooolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coffeeButton.setOnClickListener(this);
        pepsiButton.setOnClickListener(this);
        cookieButton.setOnClickListener(this);
        mealButton.setOnClickListener(this);
        tshirtButton.setOnClickListener(this);
        truegiverButton.setOnClickListener(this);
    }

    private void payUsingUpi(String amount, String upiId, String name, String note) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa",upiId)  //upi
                .appendQueryParameter("pn",name) //name
                .appendQueryParameter("tn",note) //note
                .appendQueryParameter("am",amount) //amo
                .appendQueryParameter("cu","INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiPayIntent,"Pay with");

        if(null != chooser.resolveActivity(getPackageManager())){
            startActivityForResult(chooser,UPI_PAYMENT);
        }else{
            Toast.makeText(this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case UPI_PAYMENT:
                if((RESULT_OK == resultCode) || (resultCode == 11)){
                    if(data != null){
                        String text = data.getStringExtra("response");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(text);
                        upiPaymentDataOperation(dataList);
                    }else{
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("Nothing");
                        upiPaymentDataOperation(dataList);
                    }
                }else {
                   ArrayList<String> dataList = new ArrayList<>();
                   dataList.add("Nothing");
                   upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> dataList) {
        if(isConnectionAvailable(SupportDevelopment.this)){
            String str = dataList.get(0);
            String paymentCancel = "";
            if(str == null){
                str = "discard";
            }
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) ||
                            equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(SupportDevelopment.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(SupportDevelopment.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(SupportDevelopment.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SupportDevelopment.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean isConnectionAvailable(SupportDevelopment supportDevelopment) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            NetworkInfo networkInfo =  connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()
            && networkInfo.isConnectedOrConnecting()
            && networkInfo.isAvailable()){
                return  true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buy_coffee:
                buttonEffect(v,"#F44336");
                payUsingUpi("20","kstripathi7okicici","Saurabh","aise hi");
                break;
            case R.id.buy_pepsi:
                buttonEffect(v,"#AED581");
                payUsingUpi("40","kstripathi7@okicici","Saurabh","aise hi");
                break;
            case R.id.buy_cookie:
                buttonEffect(v,"#64B5F6");
                payUsingUpi("60","kstripathi7@okicici","Saurabh","aise hi");
                break;
            case R.id.buy_meal:
                buttonEffect(v,"#FFD54F");
                payUsingUpi("100","kstripathi7@okicici","Saurabh","aise hi");
                break;
            case R.id.buy_tshirt:
                buttonEffect(v,"#A1887F");
                payUsingUpi("350","kstripathi7@okicici","Saurabh","aise hi");
                break;
            case R.id.true_giver:
                buttonEffect(v,"#9575CD");
                payUsingUpi("1000","kstripathi7@okicici","Saurabh","aise hi");
                break;
        }

    }

    public static void buttonEffect(View button, final String color_code){
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(Color.parseColor(color_code), PorterDuff.Mode.MULTIPLY);  //0xe0f47521
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;

            }
        });

    }
}
