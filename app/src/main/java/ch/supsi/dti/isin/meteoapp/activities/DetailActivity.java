package ch.supsi.dti.isin.meteoapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.R;
import ch.supsi.dti.isin.meteoapp.fragments.DetailLocationFragment;

public class DetailActivity extends AppCompatActivity {
    private static final String EXTRA_LOCATION_ID = "ch.supsi.dti.isin.meteoapp.location_id";

    public static Intent newIntent(Context packageContext, UUID locationId) {
        Intent intent = new Intent(packageContext, DetailActivity.class);
        intent.putExtra(EXTRA_LOCATION_ID, locationId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_single_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            UUID locationId = (UUID) getIntent().getSerializableExtra(EXTRA_LOCATION_ID);
            fragment = new DetailLocationFragment().newInstance(locationId);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
