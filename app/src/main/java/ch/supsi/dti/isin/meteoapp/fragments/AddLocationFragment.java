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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

        TextView textArea = v.findViewById(R.id.editTextTextPersonName);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Aggiungi location")
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String city = textArea.getText().toString();
                                sendResultBack(Activity.RESULT_OK, city);
                            }
                        })
                .create();
    }

    private void sendResultBack(int resultCode, String city) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("return_city", city);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
