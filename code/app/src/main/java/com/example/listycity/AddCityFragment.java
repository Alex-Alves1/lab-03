package com.example.listycity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(City city);
        void onEditPressed(City city);
    }
    private AddCityDialogListener listener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }
    //Code from hint#3 on 301 website
    static AddCityFragment newInstance(City city) {
        Bundle args = new Bundle();
        args.putSerializable("city", city);

        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view =
                LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        Bundle args = getArguments();
        if (args != null) {
            //Code taken from Google Gemini and modified Jan 22nd 2026. "How to implement the serializable package
            //in the add city fragment to properly edit a city"
            City editCity = (City) args.getSerializable("city");
            editCityName.setText(editCity.getName());
            editProvinceName.setText(editCity.getProvince());
            return builder
                    .setView(view)
                    .setTitle("Edit City")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", (dialog, which) -> {
                        String name = editCityName.getText().toString();
                        String province = editProvinceName.getText().toString();
                        editCity.setName(name);
                        editCity.setProvince(province);
                        listener.onEditPressed(editCity);
                    }).create();
        } else {
            return builder
                    .setView(view)
                    .setTitle("Add a city")
                    .setNegativeButton("Cancel"
                            , null)
                    .setPositiveButton("Add"
                            , (dialog, which) -> {
                                String cityName = editCityName.getText().toString();
                                String provinceName = editProvinceName.getText().toString();
                                listener.addCity(new City(cityName, provinceName));
                            })
                    .create();
        }
    }
}