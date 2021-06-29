package com.knoxtech.internhub;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.knoxtech.internhub.model.Data;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class FullActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ImageView companyL;
    private TextView designationName,companyName, location, full_salary, longDescription;
    private String applyUrl;
    private RewardedAd mRewardedAd;
    private final String TAG = "FullActivity";
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionShare) {
            share();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void share() {


                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT,  "https://internhub.page.link/internhub");
                            intent.setType("text/plain");
                            startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full);
        Toolbar toolbar=findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        MobileAds.initialize(this, initializationStatus -> {
        });
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String type = bundle.getString("type");
        String docId=bundle.getString("doc");

        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-1992993118513832/2752556507",
                adRequest, new RewardedAdLoadCallback(){
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");
                        if (mRewardedAd != null) {
                            Activity activityContext = FullActivity.this;
                            mRewardedAd.show(activityContext, rewardItem -> {
                                // Handle the reward.
                                int rewardAmount = rewardItem.getAmount();
                                String rewardType = rewardItem.getType();
                                Log.d(TAG, "The user earned the "+rewardAmount+" "+rewardType);


                            });
                        } else {
                            Log.d(TAG, "The rewarded ad wasn't ready yet.");
                        }
                    }

                });


        designationName=findViewById(R.id.designationName);
        companyName=findViewById(R.id.companyName);
        location=findViewById(R.id.location);
        full_salary=findViewById(R.id.full_salary);
        longDescription=findViewById(R.id.long_description);
        companyL=findViewById(R.id.companyLogo);
        assert type != null;
        assert docId != null;
        DocumentReference docRef = db.collection(type).document(docId);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            Data data = documentSnapshot.toObject(Data.class);
            assert data != null;
            designationName.setText(data.getDesignation());
            companyName.setText(data.getCompanyName());
            location.setText(data.getLocation());
            full_salary.setText(data.getSalary());
            longDescription.setText(data.getDescription());
            applyUrl=data.getApplyUrl();
            Picasso.get().load(data.getLogo()).into(companyL);

        });


        findViewById(R.id.applyNow).setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(applyUrl));
                startActivity(browserIntent);


        });

        findViewById(R.id.bookmarkbutton).setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("",applyUrl);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(FullActivity.this, "Copied", Toast.LENGTH_SHORT).show();
        });


    }
}