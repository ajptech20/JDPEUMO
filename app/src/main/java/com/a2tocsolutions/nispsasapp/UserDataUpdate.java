package com.a2tocsolutions.nispsasapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.a2tocsolutions.nispsasapp.model.LoginResponse;
import com.a2tocsolutions.nispsasapp.networking.api.Service;
import com.a2tocsolutions.nispsasapp.networking.generator.DataGenerator;
import com.a2tocsolutions.nispsasapp.utils.FancyToast;
import com.a2tocsolutions.nispsasapp.utils.PreferenceUtils;
import com.ikhiloyaimokhai.nigeriastatesandlgas.Nigeria;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDataUpdate extends AppCompatActivity {

    private Spinner mStateSpinner, mLgaSpinner;
    private String mState, mLga;
    private List<String> states;
    private static final int SPINNER_HEIGHT = 500;

    String prevStarted = "yes";
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(prevStarted, Boolean.TRUE);
            editor.apply();
        } else {
            //moveToSecondary();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_data_update_activity);

        mStateSpinner = findViewById(R.id.stateSpinner);
        mLgaSpinner = findViewById(R.id.lgaSpinner);
        resizeSpinner(mStateSpinner, SPINNER_HEIGHT);
        resizeSpinner(mLgaSpinner, SPINNER_HEIGHT);
        Button submit = findViewById(R.id.electionSubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = PreferenceUtils.getPhoneNumber(getApplicationContext());
                String lga = mLgaSpinner.getSelectedItem().toString();
                String state = mStateSpinner.getSelectedItem().toString();
                AutoCompleteTextView polling = (AutoCompleteTextView) findViewById(R.id.autocomplete_Pollingunit);
                String pollingunit = polling.getText().toString();
                logUserIn(phone,state,lga,pollingunit);
            }
        });

        states = Nigeria.getStates();

        //call to method that'll set up state and lga spinner
        setupSpinners();



        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_wurd);
        AutoCompleteTextView polling = (AutoCompleteTextView) findViewById(R.id.autocomplete_Pollingunit);
// Get the string array
        String[] countries = getResources().getStringArray(R.array.WURDS);
        String[] pollingUnit = getResources().getStringArray(R.array.PollingUnit);
// Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        textView.setAdapter(adapter);

        ArrayAdapter<String> adapter2 =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pollingUnit);
        polling.setAdapter(adapter2);

    }
    public void setupSpinners() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        //populates the quantity spinner ArrayList

        ArrayAdapter<String> statesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, states);

        // Specify dropdown layout style - simple list view with 1 item per line
        statesAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // Apply the adapter to the spinner
        statesAdapter.notifyDataSetChanged();
        mStateSpinner.setAdapter(statesAdapter);

        // Set the integer mSelected to the constant values
        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mState = (String) parent.getItemAtPosition(position);
                setUpStatesSpinner(position);
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Unknown
            }
        });
    }


    /**
     * method to set up the state spinner
     *
     * @param position current position of the spinner
     */
    private void setUpStatesSpinner(int position) {
        List<String> list = new ArrayList<>(Nigeria.getLgasByState(states.get(position)));
        setUpLgaSpinner(list);
    }


    /**
     * Method to set up the local government areas corresponding to selected states
     *
     * @param lgas represents the local government areas of the selected state
     */
    private void setUpLgaSpinner(List<String> lgas) {

        ArrayAdapter lgaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lgas);
        lgaAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        lgaAdapter.notifyDataSetChanged();
        mLgaSpinner.setAdapter(lgaAdapter);

        mLgaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                mLga = (String) parent.getItemAtPosition(position);
                Toast.makeText(UserDataUpdate.this, "state: " + mState + " lga: " + mLga, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    /*public void moveToSecondary(){
        // use an intent to travel from one activity to another.
        Intent intent = new Intent(this, seesay.class);
        startActivity(intent);
    }*/

    private void resizeSpinner(Spinner spinner, int height) {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            //Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            //set popupWindow height to height
            popupWindow.setHeight(height);

        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
    private void logUserIn(String officerphone, String state, String lga, String polliunit) {

        ProgressBar progress = findViewById(R.id.electionProgress);

        progress.setVisibility(View.VISIBLE);
        String usertoken =   PreferenceUtils.getUserkey(getApplicationContext());
        try {
            Service service = DataGenerator.createService(Service.class, "https://nispsas.com.ng/");
            Call<LoginResponse> call = service.pollinUnit(officerphone, state,lga,polliunit);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            LoginResponse loginResponse = response.body();

                            progress.setVisibility(View.GONE);
                            emptyInputEditText();
                            PreferenceUtils.saveState(state, getApplicationContext());
                            PreferenceUtils.saveLga(lga, getApplicationContext());
                            PreferenceUtils.savePollingUnit(polliunit, getApplicationContext());
                            FancyToast.makeText(getApplicationContext(), "Your Data has been Updated Successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            /*Intent intent = new Intent(UserDataUpdate.this, seesay.class);
                            startActivity(intent);
                            finish();*/
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


    private void emptyInputEditText() {
        mStateSpinner = findViewById(R.id.stateSpinner);
        mLgaSpinner = findViewById(R.id.lgaSpinner);
        AutoCompleteTextView polling = (AutoCompleteTextView) findViewById(R.id.autocomplete_Pollingunit);
        polling.setText("");
    }
}
