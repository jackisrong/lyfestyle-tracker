package com.example.lyfestyletracker.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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

import com.example.lyfestyletracker.Login;
import com.example.lyfestyletracker.R;

import java.util.LinkedHashMap;
import java.util.Map;

public class CreateUserConsultant extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user_consultant);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.userEmail);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText passwordEditText2 = findViewById(R.id.password2);
        final EditText inputUsername = findViewById(R.id.userName);
        final EditText inputPersonName = findViewById(R.id.personName);
        final Button registerButton = findViewById(R.id.registerButton);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                registerButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText2.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    if (!matchingPassword(passwordEditText.getText().toString(), (passwordEditText2.getText().toString()))){
                        showSignupFailed(loginResult.getError());
                    }
                }
                if (loginResult.getSuccess() != null) {
                    if (matchingPassword(passwordEditText.getText().toString(), (passwordEditText2.getText().toString()))) {
                        updateUiWithUser(loginResult.getSuccess());
                    }
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    public boolean usernameAvail(String username){
        //TODO: NEED TO QUERY DB TO SEE IF USERNAME IS TAKEN
        return false;
    }

    public void addToDatabase(String name, String username, String email, String password) {
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("query_type", "special_change");
        //TODO: NEED TO IMPLEMENT THE DATABASE
    }

    public boolean matchingPassword(String string1, String string2) {
        return string1.equals(string2);
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = "Please Sign In";
        Intent intent = new Intent(CreateUserConsultant.this, Login.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showSignupFailed(@StringRes Integer errorString) {
        String sorry = "Registration Unable to Process, Please Retry";
        Toast.makeText(getApplicationContext(), sorry, Toast.LENGTH_SHORT).show();
    }
}