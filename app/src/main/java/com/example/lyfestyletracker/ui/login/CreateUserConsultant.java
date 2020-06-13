package com.example.lyfestyletracker.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyfestyletracker.R;
import com.example.lyfestyletracker.ui.login.LoginViewModel;
import com.example.lyfestyletracker.ui.login.LoginViewModelFactory;

public class CreateUserConsultant extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_consultant);
    }

    public void createAccountUser(View view) {
        EditText userMail = findViewById(R.id.username);
        EditText userPass = findViewById(R.id.password);
        EditText userPassConf = findViewById(R.id.password2);

    }

}