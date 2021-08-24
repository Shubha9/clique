package com.us.clique.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.us.clique.R;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.databinding.ActivityLoginBinding;
import com.us.clique.signIn.SignInActivity;
import com.us.clique.signUp.SignUpActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class LoginActivity extends AppCompatActivity {

    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(LoginActivity.this,
                R.layout.activity_login);
        bottomNavigationViewModel = new ViewModelProvider(this,bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setLogIn(bottomNavigationViewModel);
        OnlineUserStatus onlineUserStatus=new OnlineUserStatus(getApplicationContext());
        onlineUserStatus.API();
        binding.signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);
            }
        });
        binding.signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(j);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}