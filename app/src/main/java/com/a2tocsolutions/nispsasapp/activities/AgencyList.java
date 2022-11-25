package com.a2tocsolutions.nispsasapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.adapter.AgencyAdapter;
import com.a2tocsolutions.nispsasapp.model.Agency;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgencyList extends AppCompatActivity {


    private List<Agency> agencyList;
    private AgencyAdapter adapter;

    @BindView(R.id.agencies_recyclerview)
    RecyclerView agencies_recyclerview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agency_list);

        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle("PARTNERS");


        agencyList = new ArrayList<>();
        adapter = new AgencyAdapter(this, agencyList);

        agencies_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        agencies_recyclerview.addItemDecoration(new DividerItemDecoration(agencies_recyclerview.getContext(), DividerItemDecoration.VERTICAL));
        agencies_recyclerview.setHasFixedSize(true);
        agencies_recyclerview.setAdapter(adapter);

        prepareAgencies();


    }

        private void prepareAgencies() {
            int[] covers = new int[]{
                    R.drawable.coat_of_arm,
                    R.drawable.nscdc,
                    R.drawable.nigeria_police,
                    R.drawable.civil_defence,
                    R.drawable.fire_service,
                    R.drawable.immigration,
                    R.drawable.prison_service,
                    R.drawable.roadsave,
                    R.drawable.fmowa,
                    R.drawable.pwd_logo,
                    R.drawable.nema,
            };

            Agency

            a = new Agency(R.drawable.nigeria_police, "https://www.npf.gov.ng/","NIGERIAN POLICE FORCE");
            agencyList.add(a);

            a = new Agency(R.drawable.fire_service, "https://interior.gov.ng/federal-fire-service/","FEDERAL FIRE SERVICE");
            agencyList.add(a);

            a = new Agency(R.drawable.prison_service, "https://interior.gov.ng/nigeria-correctional-service/","NIGERIA CORRECTIONAL SERVICE");
            agencyList.add(a);

            a = new Agency(R.drawable.nema, "http://www.nema.gov.ng/","NEMA");
            agencyList.add(a);


            a = new Agency(R.drawable.nscdc, " ","NSCDC");
            agencyList.add(a);

            adapter.notifyDataSetChanged();
        }


}
