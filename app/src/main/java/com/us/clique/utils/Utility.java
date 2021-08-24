package com.us.clique.utils;

import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {


    public static boolean isPasswordValidMethod(String password) {


        boolean isValid = false;

        // ^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$
        // ^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$
        String expression = "[!@#$%&*()_+=|<>?{}\\[\\]~-]";
        CharSequence inputStr = password;
        Pattern pattern = Pattern.compile ("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\\\S+$).{4,}$");
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()&&password.length()>6) {
            isValid = true;
        }else{
            isValid = false;
        }
        return isValid;
    }
    public static void preventTwoClick(final View view){
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            public void run() {
                view.setEnabled(true);
            }
        }, 500);
    }
}
