package com.jdpmc.jwatcherapp.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jdpmc.jwatcherapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jdpmc.jwatcherapp.activities.AgricSatDetail.CUGNAME;
import static com.jdpmc.jwatcherapp.activities.AgricSatDetail.DATE;
import static com.jdpmc.jwatcherapp.activities.AgricSatDetail.DEPARTURE_POINT;
import static com.jdpmc.jwatcherapp.activities.AgricSatDetail.DEPARTURE_TIME;
import static com.jdpmc.jwatcherapp.activities.AgricSatDetail.DESTINATION_POINT;
import static com.jdpmc.jwatcherapp.activities.AgricSatDetail.DESTINATION_TIME;
import static com.jdpmc.jwatcherapp.activities.AgricSatDetail.ENCODE;
import static com.jdpmc.jwatcherapp.activities.AgricSatDetail.NAME;
import static com.jdpmc.jwatcherapp.activities.AgricSatDetail.PHONE_NO;
import static com.jdpmc.jwatcherapp.activities.AgricSatDetail.PLATENO;
import static com.jdpmc.jwatcherapp.activities.AgricSatDetail.PRODUCT_INFO;
import static com.jdpmc.jwatcherapp.activities.AgricSatDetail.SESAPH_ID;
import static com.jdpmc.jwatcherapp.activities.AgricSatDetail.SEX;

public class AgricSat extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {

    @BindView(R.id.surname)
    TextInputLayout surname;

    @BindView(R.id.input_surname)
    EditText input_surname;

    @BindView(R.id.firstname)
    TextInputLayout firstname;

    @BindView(R.id.input_firstname)
    EditText input_firstname;

    @BindView(R.id.sex)
    TextInputLayout sex;

    @BindView(R.id.input_sex)
    EditText input_sex;

    @BindView(R.id.phoneno)
    TextInputLayout phoneno;

    @BindView(R.id.input_phoneno)
    EditText input_phoneno;

    @BindView(R.id.plateno)
    TextInputLayout plateno;

    @BindView(R.id.input_plateno)
    EditText input_plateno;

    @BindView(R.id.encode)
    TextInputLayout encode;

    @BindView(R.id.input_encode)
    EditText input_encode;

    @BindView(R.id.productinfo)
    TextInputLayout productinfo;

    @BindView(R.id.input_productinfo)
    EditText input_productinfo;

    @BindView(R.id.cugname)
    TextInputLayout cugname;

    @BindView(R.id.input_cugname)
    EditText input_cugname;

    @BindView(R.id.checkTerms0)
    CheckBox checkTerms0;

    @BindView(R.id.checkTerms1)
    CheckBox checkTerms1;

    @BindView(R.id.checkTerms2)
    CheckBox checkTerms2;

    @BindView(R.id.checkTerms3)
    CheckBox checkTerms3;

    @BindView(R.id.sesaph_id)
    TextInputLayout sesaph_id;

    @BindView(R.id.input_sesaphid)
    EditText input_sesaphid;

    @BindView(R.id.submit)
    MaterialButton submit;

    @BindView(R.id.date_text)
    TextView date_text;

    @BindView(R.id.time_text)
    TextView time_text;

    @BindView(R.id.time2_text)
    TextView time2_text;

    @BindView(R.id.time2)
    RelativeLayout time2;

    @BindView(R.id.time)
    RelativeLayout time;

    @BindView(R.id.departurepoint)
    TextInputLayout departurepoint;

    @BindView(R.id.input_departurepoint)
    EditText input_departurepoint;

    @BindView(R.id.destinationpoint)
    TextInputLayout destinationpoint;

    @BindView(R.id.input_destinationpoint)
    EditText input_destinationpoint;

    private Calendar calendar;
    private int hour, minute, year, day, month;
    private String date;
    private Boolean firstTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agric_sat);
        ButterKnife.bind(this);

        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        setTitle("Agric SAT-C");

        if (checkTerms0.isChecked() && checkTerms1.isChecked() && checkTerms2.isChecked() && checkTerms3.isChecked()) {
            submit.setEnabled(true);
        } else {
            submit.setEnabled(false);
        }

        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);
        month++;

        date = year + "-" + month + "-" + day;
        date_text.setText(date);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        time_text.setText(depictDate(hour, minute));
        time2_text.setText(depictDate(hour, minute));

        submit.setOnClickListener(v -> verifyData());
        time.setOnClickListener(v -> {
            firstTime = Boolean.TRUE;
            setTime();
        });

        time2.setOnClickListener(v -> {
            firstTime = Boolean.FALSE;
            setTime();
        });
    }

    public void setDate(View v) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, AgricSat.this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void setTime() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, minute,
                false);

        timePickerDialog.show();
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        month++;
        day = dayOfMonth;
        month = month;
        date = year + " - " + month + " - " + dayOfMonth;
        date_text.setText(date);

    }

    private void verifyData() {
        surname.setError(null);
        firstname.setError(null);
        sex.setError(null);
        phoneno.setError(null);
        plateno.setError(null);
        encode.setError(null);
        productinfo.setError(null);
        cugname.setError(null);
        sesaph_id.setError(null);
        departurepoint.setError(null);
        destinationpoint.setError(null);

        if (input_surname.length() == 0) {
            surname.setError(getString(R.string.error_surname));
        } else if (input_firstname.length() == 0  ) {

            firstname.setError(getString(R.string.error_firstname));

        } else if (input_sex.length() == 0){
            sex.setError(getString(R.string.error_sex));

        } else if (input_phoneno.length() == 0) {
            phoneno.setError(getString(R.string.error_phoneno));

        } else if (input_plateno.length() == 0) {
            plateno.setError(getString(R.string.error_plateno));

        } else if (input_encode.length() == 0) {
            encode.setError(getString(R.string.error_encode));

        } else if (input_productinfo.length() == 0) {
            productinfo.setError(getString(R.string.error_productinfo));

        } else if (input_cugname.length() == 0) {
            cugname.setError(getString(R.string.error_cugname));

        } else if (input_sesaphid.length() == 0) {
            sesaph_id.setError(getString(R.string.error_sesaphid));

        } else if (input_departurepoint.length() == 0) {
            departurepoint.setError(getString(R.string.error_departurepoint));

        }else if (input_destinationpoint.length() == 0) {
            destinationpoint.setError(getString(R.string.error_destinationpoint));
        } else {
            String dateText = date_text.getText().toString();
            String surname = input_surname.getText().toString();
            String firstname = input_firstname.getText().toString();
            String sex = input_sex.getText().toString();
            String phoneno = input_phoneno.getText().toString();
            String plateno = input_plateno.getText().toString();
            String encode = input_encode.getText().toString();
            String productinfo = input_productinfo.getText().toString();
            String cugname = input_cugname.getText().toString();
            String sesaphId = input_sesaphid.getText().toString();
            String destinationPoint = input_destinationpoint.getText().toString();
            String departurePoint = input_departurepoint.getText().toString();
            String departureTime = time_text.getText().toString();
            String destinationTime = time2_text.getText().toString();

            Intent intent = new Intent(this, AgricSatDetail.class);
            intent.putExtra(DATE, dateText);
            intent.putExtra(NAME, surname + " " + firstname );
            intent.putExtra(SEX, sex);
            intent.putExtra(PHONE_NO, phoneno);
            intent.putExtra(PLATENO, plateno);
            intent.putExtra(ENCODE, encode);
            intent.putExtra(PRODUCT_INFO, productinfo);
            intent.putExtra(CUGNAME, cugname);
            intent.putExtra(SESAPH_ID, sesaphId);
            intent.putExtra(DEPARTURE_POINT, departurePoint);
            intent.putExtra(DEPARTURE_TIME, departureTime);
            intent.putExtra(DESTINATION_POINT, destinationPoint);
            intent.putExtra(DESTINATION_TIME, destinationTime);

            startActivity(intent);

        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        if (firstTime == Boolean.TRUE) {

            time_text.setText( depictDate(hourOfDay, minute));

            firstTime = Boolean.FALSE;
        } else if (firstTime == Boolean.FALSE) {
            time2_text.setText( depictDate(hourOfDay, minute) );
        }

    }

    private String depictDate(int hrOfDay, int minute) {
        String am_pm = "";
        String formattedDate = "";

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hrOfDay);
        datetime.set(Calendar.MINUTE, minute);

        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";

        if (datetime.get(Calendar.MINUTE) < 10) {
            formattedDate = strHrsToShow+":"+"0"+datetime.get(Calendar.MINUTE)+" "+am_pm;
        } else {
            formattedDate = strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm;
        }

        return formattedDate;
    }

    public void onCheckboxClicked(View view) {
        boolean checked;
        checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkTerms0:
            case R.id.checkTerms1:
            case R.id.checkTerms2:
            case R.id.checkTerms3:
                if (checked) {
                    submit.setEnabled(true);
                } else {
                    submit.setEnabled(false);
                }
                break;

        }
    }


}
