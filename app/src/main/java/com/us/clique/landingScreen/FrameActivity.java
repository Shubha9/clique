package com.us.clique.landingScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.us.clique.R;
import com.us.clique.activites.LoginActivity;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.databinding.ActivityFrame1Binding;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class FrameActivity extends AppCompatActivity {
    Context context;
    private frameAdapter adapter;
    private LinearLayout lnrIndicator;
    RelativeLayout relativeLayout;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivityFrame1Binding binding;
    public static final String PREFS_NAME = "MyPrefsFile";
    int currentpos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_frame1);
        AndroidInjection.inject(this);

        binding = DataBindingUtil.setContentView(FrameActivity.this,
                R.layout.activity_frame1);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setLandingScreen(bottomNavigationViewModel);

        lnrIndicator = findViewById((R.id.indicators));
        frames();
        ViewPager2 viewPager2 = findViewById(R.id.viewpager);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative1);
        viewPager2.setAdapter(adapter);
        Setindicators();
        setCurrentIndicator(currentpos);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
                currentpos=position;
            }
        });
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentpos==2)
                {
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }else
                {
                    viewPager2.setCurrentItem(currentpos+1);
                }

            }
        });
        //User has successfully logged in, save this information
// We need an Editor object to make preference changes.
        SharedPreferences settings = getSharedPreferences(this.PREFS_NAME, 0); // 0 - for private mode
        SharedPreferences.Editor editor = settings.edit();

//Set "hasLoggedIn" to true
        editor.putBoolean("hasLoggedIn", true);

// Commit the edits!
        editor.commit();
    }

    private void frames() {
        ArrayList<frameitems> items = new ArrayList<frameitems>();
        frameitems frame1 = new frameitems();
        frame1.setTitle("Lorem Ipsum");
        frame1.setSkip("Skip >>");
//        frame1.setDescription("It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.");
        frame1.setDescription("Your events based social networking \n" +
                "platform- connecting locals with similar\n" +
                "interests to share activities.");
        frame1.setImg(R.drawable.frame1);

        frameitems frame2 = new frameitems();
        frame2.setTitle("Lorem Ipsum");
        frame2.setSkip("Skip >>");
        frame2.setDescription("It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.");
       /* frame2.setDescription(" How it works?\n" +
                "1.Post or search about any activity or event you want to do\n" +
                "2.Connect to and message others who are interested in the same activity\n" +
                "3. Meet IRL and share the experience");*/
        frame2.setImg(R.drawable.frame2);

        frameitems frame3 = new frameitems();
        frame3.setTitle("Lorem Ipsum");
        frame3.setSkip("Skip >>");
        //   frame3.setDescription("It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.");
        frame3.setDescription("Start meeting new people and experience \n" +
                "more together!");
        frame3.setImg(R.drawable.frame3);
        items.add(frame1);
        items.add(frame2);
        items.add(frame3);

        adapter = new frameAdapter(context, items, this);
    }

    private void Setindicators() {
        ImageView[] indicators = new ImageView[adapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.tab_bg));
            indicators[i].setLayoutParams(layoutParams);
            lnrIndicator.addView(indicators[i]);
        }
    }


    private void setCurrentIndicator(int index) {
        int childCount = lnrIndicator.getChildCount();
        if (index == 0) {
            relativeLayout.setBackgroundResource(R.color.white);
            binding.btn.setBackgroundResource(R.drawable.button_bg);
            binding.btn.setTextColor(Color.parseColor("#FFFFFF"));

        } else if (index == 1) {
            binding.btn.setBackgroundResource(R.drawable.button_white_bg);
            binding.btn.setTextColor(Color.parseColor("#0053CB"));
            relativeLayout.setBackgroundResource(R.color.blue);
        } else {
            relativeLayout.setBackgroundResource(R.color.white);
            binding.btn.setBackgroundResource(R.drawable.button_bg);
            binding.btn.setTextColor(Color.parseColor("#FFFFFF"));
        }
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) lnrIndicator.getChildAt(i);
            if (i == index) {
                if (index == 1) {
                    imageView.setImageDrawable(
                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_white_indicator));
                } else {
                    imageView.setImageDrawable(
                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_indicator));
                }


            } else {
                if (index == 1) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_white_inactive_indi));
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_bg));
                }
            }
        }

    }
}