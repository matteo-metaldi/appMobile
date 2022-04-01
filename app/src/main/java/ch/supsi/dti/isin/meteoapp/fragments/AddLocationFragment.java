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
import android.widget.EditText;
import android.widget.LinearLayout;
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

    private AlertDialog alertDialog;

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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.citycountry_dialog, null);
        EditText textAreaCity = v.findViewById(R.id.city);
        EditText textAreaCountry = v.findViewById(R.id.country);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        if(!textAreaCity.getText().toString().equals("") && !textAreaCountry.getText().toString().equals("")){
                            sendResultBack(Activity.RESULT_OK, textAreaCity.getText().toString(), textAreaCountry.getText().toString());
                        }else{
                            Toast toast = new Toast(getContext());
                            toast.setText("Campo mancante nel form");
                            toast.show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //LoginDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
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
