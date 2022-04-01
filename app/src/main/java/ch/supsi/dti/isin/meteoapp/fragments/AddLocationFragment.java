package ch.supsi.dti.isin.meteoapp.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import ch.supsi.dti.isin.meteoapp.R;

public class AddLocationFragment extends DialogFragment {

    public static AddLocationFragment newInstance() {
        return new AddLocationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_location, null);

        TextView textAreaCity = v.findViewById(R.id.textCity);
        TextView textAreaCountry = v.findViewById(R.id.textCountry);
        return new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setView(v)
                .setTitle("Aggiungi location")
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String city = textAreaCity.getText().toString();
                                String country = textAreaCountry.getText().toString();
                                if(!city.equals("") && !country.equals("")){
                                    sendResultBack(Activity.RESULT_OK, city, country);
                                }else{
                                    Toast toast = new Toast(getContext());
                                    toast.setText("Error");
                                    toast.show();
                                }
                            }
                        })
                .create();
    }

    private void sendResultBack(int resultCode, String city, String country) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("return_city", city);
        intent.putExtra("return_country", country);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
