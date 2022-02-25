package ch.supsi.dti.isin.meteoapp.activities;

import androidx.fragment.app.Fragment;

import ch.supsi.dti.isin.meteoapp.fragments.ListFragment;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ListFragment();
    }
}
