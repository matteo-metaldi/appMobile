package ch.supsi.dti.isin.meteoapp.fragments;

import static java.lang.Thread.sleep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import ch.supsi.dti.isin.meteoapp.R;
import ch.supsi.dti.isin.meteoapp.activities.DetailActivity;
import ch.supsi.dti.isin.meteoapp.model.LocationDatabase;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;
import ch.supsi.dti.isin.meteoapp.model.Location;

public class ListFragment extends Fragment {

    private LocationDatabase db;

    private RecyclerView mLocationRecyclerView;
    private LocationAdapter mAdapter;
    private TextView mTextViewResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        db = LocationDatabase.getInstance(requireContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preferiti_generale, container, false);
        //db

        mTextViewResult = view.findViewById(R.id.textView4);
        mLocationRecyclerView = view.findViewById(R.id.recyclerView_generale);
        mLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //TODO: QUERY DB E PRENDI MERDA
        List<Location> locations = LocationsHolder.get(getActivity()).getLocations();
        mAdapter = new LocationAdapter(locations);
        mLocationRecyclerView.setAdapter(mAdapter);

        new Thread(() -> refreshUI()).start();

        mAdapter.notifyDataSetChanged();
        return view;
    }

    // Menu

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                FragmentManager manager = getFragmentManager();
                AddLocationFragment dialog = AddLocationFragment.newInstance();

                dialog.setTargetFragment(ListFragment.this, 0);
                dialog.show(manager, "TestDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Holder

    private class LocationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNameTextView;
        private Location mLocation;

        public LocationHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item, parent, false));
            itemView.setOnClickListener(this);
            mNameTextView = itemView.findViewById(R.id.name);
        }

        @Override
        public void onClick(View view) {
            Intent intent = DetailActivity.newIntent(getActivity(), mLocation.getId());
            startActivity(intent);
        }

        public void bind(Location location) {
            mLocation = location;
            mNameTextView.setText(mLocation.getName());
        }
    }

    //TODO: ESTRARRE DA QUA IL NOME DEL CITTA DA AGGIUNGERE
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == 0) {
            String city = (String) data.getSerializableExtra("return_city");
            if(city.length() > 0) {
                Location location = new Location();
                location.setName(city);

                //Persiste nel database la location
                new Thread(() -> persistLocationToDB(location)).start();

                LocationsHolder.get(getActivity()).addLocationToList(location);
                new Thread(() -> refreshUI()).start();
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    //Inserisce la location nel db con un thread borgo
    private void persistLocationToDB(Location location) {
        db.locationDao().insertLocation(location);
    }

    // Adapter

    private class LocationAdapter extends RecyclerView.Adapter<LocationHolder> {
        private List<Location> mLocations;

        public LocationAdapter(List<Location> locations) {
            mLocations = locations;
        }

        @Override
        public LocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new LocationHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(LocationHolder holder, int position) {
            Location location = mLocations.get(position);
            holder.bind(location);
        }

        @Override
        public int getItemCount() {
            return mLocations.size();
        }

        public void replaceLocations(List<Location> locations){
            mLocations = locations;
        }
    }



    private void refreshUI() {
        List<Location> locations = db.locationDao().getLocations();

        mAdapter.replaceLocations(locations);

        //new Handler(Looper.getMainLooper()).post(()->mAdapter.notifyDataSetChanged());
    }
}
