package ch.supsi.dti.isin.meteoapp.fragments;

import static java.lang.Thread.sleep;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.util.List;

import ch.supsi.dti.isin.meteoapp.R;
import ch.supsi.dti.isin.meteoapp.activities.DetailActivity;
import ch.supsi.dti.isin.meteoapp.activities.MainActivity;
import ch.supsi.dti.isin.meteoapp.model.LocationDatabase;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;
import ch.supsi.dti.isin.meteoapp.model.Location;

public class ListFragment extends Fragment {

    private static LocationDatabase db;
    public static ListFragment listFragment;
    private RecyclerView mLocationRecyclerView;
    private static LocationAdapter mAdapter;
    private TextView mTextViewResult;
    boolean list_empty = true;
    private static TextView currentLocation;
    private MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mainActivity = (MainActivity) getActivity();
        db = LocationDatabase.getInstance(requireContext());
        listFragment = this;
        new Thread(()->list_empty = db.locationDao().getLocations().isEmpty()).start();
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(list_empty) {
            new Thread(() -> persistLocationToDB(new Location("Rome","Italy"))).start();
            new Thread(() -> persistLocationToDB(new Location("Paris","France"))).start();
            new Thread(() -> persistLocationToDB(new Location("Berlin","Germany"))).start();
            new Thread(() -> persistLocationToDB(new Location("Stockholm","Sweden"))).start();
        }

    }

    public static void setName(String name){
        currentLocation.setText(name);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preferiti_generale, container, false);
        //db

        mTextViewResult = view.findViewById(R.id.textView4);
        mLocationRecyclerView = view.findViewById(R.id.recyclerView_generale);
        mLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        currentLocation = view.findViewById(R.id.currentLocation);

        List<Location> locations = LocationsHolder.get(getActivity()).getLocations();
        mAdapter = new LocationAdapter(locations);
        mLocationRecyclerView.setAdapter(mAdapter);

        new Thread(() -> refreshUI()).start();

        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
        private Button mButtonView;
        private Location mLocation;

        public LocationHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item, parent, false));
            itemView.setOnClickListener(this);
            mNameTextView = itemView.findViewById(R.id.name);
            mButtonView = itemView.findViewById(R.id.button2);
        }

        @Override
        public void onClick(View view) {
            Intent intent = DetailActivity.newIntent(getActivity(), mLocation.getId(), mLocation.getName(), mLocation.getCountry());
            startActivity(intent);
        }

        public void bind(Location location) {
            mLocation = location;
            mNameTextView.setText(mLocation.getName());
            mButtonView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    new Thread(() -> removeLocationFromDB(mLocation)).start();
                    try {
                        sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    //TODO: ESTRARRE DA QUA IL NOME DEL CITTA DA AGGIUNGERE
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == 0) {
            String city = (String) data.getSerializableExtra("return_city");
            String country = (String) data.getSerializableExtra("return_country");
            if(city.length() > 0) {
                Location location = new Location();
                location.setName(city);
                location.setCountry(country);
                
                //Persiste nel database la location
                new Thread(() -> persistLocationToDB(location)).start();

                LocationsHolder.get(getActivity()).addLocationToList(location);

                try {
                    sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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



    public void refreshUI() {
        List<Location> locations = db.locationDao().getLocations();

        Location current = MainActivity.getCurrentLocation();

        //textview
        currentLocation.setText(current.getName());
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = DetailActivity.newIntent(getActivity(), current.getId(), current.getName(), current.getCountry());
                startActivity(intent);
            }
        });


        mAdapter.replaceLocations(locations);

        //new Handler(Looper.getMainLooper()).post(()->mAdapter.notifyDataSetChanged());
    }

    private void removeLocationFromDB(Location mLocation){
        db.locationDao().deleteLocation(mLocation);
        refreshUI();
    }

}
