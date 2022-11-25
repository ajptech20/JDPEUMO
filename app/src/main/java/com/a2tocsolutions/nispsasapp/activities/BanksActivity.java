package com.a2tocsolutions.nispsasapp.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.adapter.BankAdapter;
import com.a2tocsolutions.nispsasapp.model.Banks;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BanksActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private List<Banks> bankList;
    private BankAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banks);

        ButterKnife.bind(this);

        setTitle("Banks");
        bankList = new ArrayList<>();
        adapter = new BankAdapter(this, bankList);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addItemDecoration(new DividerItemDecoration(recyclerview.getContext(), DividerItemDecoration.VERTICAL));
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(adapter);

        prepareBanks();
    }

    private void prepareBanks(){
        int[] covers = new int[]{
                R.drawable.accessbank,
                R.drawable.ecobank,
                R.drawable.fidelitybank,
                R.drawable.first,
                R.drawable.fcmb,
                R.drawable.gtb,
                R.drawable.heritagebank,
                R.drawable.keystone,
                R.drawable.stanbicibtc,
                R.drawable.unionbank,
                R.drawable.uba,
                R.drawable.unitybank,
                R.drawable.wemabank,
                R.drawable.zenith
        };

        Banks a = new Banks(1, "Access Bank", covers[0], "https://ibank.accessbankplc.com/RetailBank/#/");
        bankList.add(a);

        a = new Banks(2, "EcoBank", covers[1], "https://www.ecobank.com/ng/personal-banking/everyday-banking/login");
        bankList.add(a);

        a = new Banks(3, "Fidelity Bank", covers[2], " https://www.fidelitybank.ng/online-banking/");
        bankList.add(a);

        a = new Banks(4, "First Bank", covers[3], "https://www.firstbanknigeria.com/personal-banking/ways-to-bank/online-banking/ ");
        bankList.add(a);

        a = new Banks(5, "FCMB", covers[4], "https://ibank.fcmb.com/Individual-Banking.aspx");
        bankList.add(a);

        a = new Banks(6, "GTB", covers[5], "https://www.gtbank.com/personal-banking/ways-to-bank/internet-banking");
        bankList.add(a);

        a = new Banks(7, "Heritage Bank", covers[6], "https://www.the-heritage-bank.com");
        bankList.add(a);

        a = new Banks(8, "Keystone Bank", covers[7], "https://ibank.keystonebankng.com/ibank/");
        bankList.add(a);

        a = new Banks(9, "Stanbic IBTC", covers[8], "https://ibanking.stanbicibtcbank.com/Login");
        bankList.add(a);

        a = new Banks(10, "Union Bank", covers[9], "https://uniononline.unionbankng.com/OnlineBanking/");
        bankList.add(a);

        a = new Banks(11, "UBA", covers[10], "https://www.ubagroup.com/countries/ng/personalbanking/eproducts/udir");
        bankList.add(a);

        a = new Banks(12, "Unity Bank", covers[11], "https://www.unitybankng.com/products/single/internet-banking");
        bankList.add(a);

        a = new Banks(13, "Wema Bank", covers[12], "https://www.wemabank.com/internet-banking/");
        bankList.add(a);

        a = new Banks(13, "Zenith Bank", covers[13], "https://ibank.zenithbank.com/InternetBanking/App/Security/login");
        bankList.add(a);


        adapter.notifyDataSetChanged();
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

}
