package com.knoxtech.internhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.knoxtech.internhub.ui.main.SectionsPagerAdapter;


public class MainActivity extends AppCompatActivity {

    Spinner sp_parent,sp_child,sp_type;

    ArrayAdapter<String> arrayAdapter_child;
    ArrayAdapter Adapterselection,AdapterType;
    String company,loc,typeString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        MainActivity.this,R.style.BottomSheetDialogTheme
                );
                final View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.bottom_sheet,
                                (RelativeLayout)findViewById(R.id.relative)
                        );

                sp_child=bottomSheetView.findViewById(R.id.spparent2);




                sp_parent=bottomSheetView.findViewById(R.id.spparent);
                sp_type=bottomSheetView.findViewById(R.id.type);
                final String[] type = new String[] {"Internship", "Jobs"};
                final String[] selection = new String[] {"companyName", "location"};
                AdapterType=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,type);
                sp_type.setAdapter(AdapterType);
                final String[] location = new String[]{"Australia", "Germany", "India", "New Zealand", "UAE", "USA", "Vietnam", "others"};
                final String[] companyName = new String[]{"Apple","Google","Microsoft","Amazon","Facebook","Samsung","Reliance","TCS","Infosys","HCL","Wipro","Oracle","Cisco","Persistent Systems","others"};

                sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        typeString = parent.getItemAtPosition(position).toString().trim();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Log.e("Error","Please select"+parent);
                    }
                });
                Adapterselection=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,selection);
                sp_parent.setAdapter(Adapterselection);
                sp_parent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        company=parent.getItemAtPosition(position).toString().trim();

                                if (position==0){
                                    arrayAdapter_child=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,companyName);

                                }else {
                                    arrayAdapter_child=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,location);
                                }
                                sp_child.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        loc=parent.getItemAtPosition(position).toString().trim();

                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        Log.e("Error","Please select"+parent);
                                    }
                                });
                                sp_child.setAdapter(arrayAdapter_child);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Log.e("Error","Please select"+parent);
                    }
                });



                bottomSheetView.findViewById(R.id.shortButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        bottomSheetDialog.dismiss();
                        Log.d("Table: ",""+typeString+" "+company+" "+loc);
                        Intent intent=new Intent(MainActivity.this,SortActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putString("collection", typeString);
                        bundle.putString("selection", company);
                        bundle.putString("equalto", loc);
                        intent.putExtras(bundle);
                        startActivity(intent);


                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }
}