package com.knoxtech.internhub;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.knoxtech.internhub.model.Data;
import com.squareup.picasso.Picasso;

public class FullActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ImageView companLogo;
    private TextView designationName,companyName, location, full_salary, longDescription;
    private String applyUrl;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String type = bundle.getString("type");
        String docId=bundle.getString("doc");
        designationName=findViewById(R.id.designationName);
        companyName=findViewById(R.id.companyName);
        location=findViewById(R.id.location);
        full_salary=findViewById(R.id.full_salary);
        longDescription=findViewById(R.id.long_description);
        companLogo=findViewById(R.id.companyLogo);
        assert type != null;
        assert docId != null;
        DocumentReference docRef = db.collection(type).document(docId);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Data data = documentSnapshot.toObject(Data.class);
                designationName.setText(data.getDesignation());
                companyName.setText(data.getCompanyName());
                location.setText(data.getLocation());
                full_salary.setText(data.getSalary());
                longDescription.setText(data.getDescription());
                applyUrl=data.getApplyUrl();
                Picasso.get().load(data.getLogo()).into(companLogo);

            }
        });


        findViewById(R.id.applyNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(applyUrl));
                startActivity(browserIntent);
            }
        });

        findViewById(R.id.bookmarkbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("",applyUrl);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(FullActivity.this, "Copied", Toast.LENGTH_SHORT).show();
            }
        });

    }
}