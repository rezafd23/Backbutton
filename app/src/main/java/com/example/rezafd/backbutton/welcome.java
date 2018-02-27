package com.example.rezafd.backbutton;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class welcome extends AppCompatActivity implements View.OnClickListener{

    private ViewPager mPager;
    private int[]layouts={R.layout.firstslide,R.layout.secondslide,R.layout.thirdslide,R.layout.fourthslide};
    private MPagerAdapter mPagerAdapter;

    private LinearLayout Dots_Layout;
    private ImageView[] dots;

    private Button btnnext,btnskip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (new PreferenceManager(this).checkPreference()){
            loadHome();
        }

        if (Build.VERSION.SDK_INT>=19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        setContentView(R.layout.activity_welcome);

        mPager = (ViewPager)findViewById(R.id.viewpager);
        mPagerAdapter = new MPagerAdapter(layouts,this);
        mPager.setAdapter(mPagerAdapter);

        btnskip=(Button)findViewById(R.id.btnskip);
        btnnext=(Button)findViewById(R.id.btnnext);

        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        createDots(0);

        btnnext.setOnClickListener(this);
        btnskip.setOnClickListener(this);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                createDots(position);
                if (position==layouts.length-1){
                    btnnext.setText("Start");
                    btnskip.setVisibility(View.INVISIBLE);
                } else {
                    btnnext.setText("Next");
                    btnskip.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void createDots(int current_position){
        if (Dots_Layout!=null)
            Dots_Layout.removeAllViews();
        dots = new ImageView[layouts.length];

        for (int i=0;i<layouts.length;i++){
            dots[i]=new ImageView(this);
            if (i==current_position){
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.default_dots));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);

            Dots_Layout.addView(dots[i],params);
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnnext:
                loadNextSlide();
                break;
            case R.id.btnskip:
                loadHome();
                new PreferenceManager(this).writePreference();
                break;
        }
    }

    private void loadHome(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    private void loadNextSlide(){
        int next_slide = mPager.getCurrentItem()+1;
        if (next_slide<layouts.length){
            mPager.setCurrentItem(next_slide);
        } else {
            loadHome();
            new PreferenceManager(this).writePreference();
        }
    }
}
