package com.jdpmc.jwatcherapp.activities;

import static com.jdpmc.jwatcherapp.utils.Constants.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.model.LoginResponse;
import com.jdpmc.jwatcherapp.model.Profile;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
import com.jdpmc.jwatcherapp.utils.FancyToast;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.progress)
    ProgressBar progress;

    @BindView(R.id.name)
    TextInputLayout name;

    @BindView(R.id.input_name)
    EditText input_name;

    @BindView(R.id.phone)
    TextInputLayout phone;

    @BindView(R.id.input_phone)
    EditText input_phone;

    @BindView(R.id.btn_login)
    MaterialButton btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ButterKnife.bind(this);
        String terms = PreferenceUtils.getTerms(getApplicationContext());
        String phonenumber = PreferenceUtils.getPhoneNumber(getApplicationContext());
        if (!phonenumber.isEmpty() && !terms.isEmpty()) {
            Intent intent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                intent = new Intent(LoginActivity.this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        }


        btn_login.setOnClickListener(v -> {
            String name = input_name.getText().toString().trim();
            String phone_number = input_phone.getText().toString().trim();
            logUserIn(name, phone_number);
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        String TAG = null;
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId token failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = Objects.requireNonNull(task.getResult()).getToken();

                        // Log and toast
                        Log.d(TAG, "This is the token " +token);
                       // FancyToast.makeText(getApplicationContext(), token, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        PreferenceUtils.saveUserkey(token, getApplicationContext());

                    }
                });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // validation on all fields
    public Boolean verifyLoginFields() {

        name.setError(null);
        phone.setError(null);

        if (input_name.length() == 0) {

            name.setError(getString(R.string.error_name));

            return false;
        }

        if (input_phone.length() == 0) {

            phone.setError(getString(R.string.error_phone));

            return false;
        }

        return true;
    }

    private void logUserIn(String name, String phonenumber) {
        if (verifyLoginFields()) {
            progress.setVisibility(View.VISIBLE);
            String usertoken =   PreferenceUtils.getUserkey(getApplicationContext());
            try {
                Service service = DataGenerator.createService(Service.class, BASE_URL);
                Call<LoginResponse> call = service.loginUser(name, phonenumber, usertoken);

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                LoginResponse loginResponse = response.body();
                                List<Profile> profileList = loginResponse.getProfile();
                                if (profileList != null) {
                                    for (Profile profile : profileList) {
                                        String username = profile.getUsername();
                                        String phonenumber = profile.getPhonenumber();

                                        PreferenceUtils.saveUsername(username, getApplicationContext());
                                        PreferenceUtils.savePhoneNumber(phonenumber, getApplicationContext());
                                    }
                                }
                                progress.setVisibility(View.GONE);
                                emptyInputEditText();
                                FancyToast.makeText(getApplicationContext(), "Welcome to J-Watcher", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                Intent intent = new Intent(LoginActivity.this, Terms.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            progress.setVisibility(View.GONE);
                            FancyToast.makeText(getApplicationContext(), "error sign up", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                        progress.setVisibility(View.GONE);
                        FancyToast.makeText(getApplicationContext(), "I am not Connected to the Internet !", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                });
            } catch (Exception e) {
                progress.setVisibility(View.GONE);
                FancyToast.makeText(getApplicationContext(), "error sign up", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        }
    }

    private void emptyInputEditText() {
        input_name.setText("");
        input_phone.setText("");
    }
}
