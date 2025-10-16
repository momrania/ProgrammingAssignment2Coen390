package com.example.progass2.controller;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.progass2.R;
import com.example.progass2.model.DatabaseHelper;
import com.example.progass2.model.Profile;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    private DatabaseHelper dbHelper;
    private DialogListener listener;

    public interface DialogListener {
        void onProfileAdded(); // to notify MainActivity to refresh the list
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DialogListener) {
            listener = (DialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InsertProfileDialog.DialogListener");
        }
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment, null);

        EditText editSurname = view.findViewById(R.id.editTextSurname);
        EditText editName = view.findViewById(R.id.editTextName);
        EditText editId = view.findViewById(R.id.editTextId);
        EditText editGpa = view.findViewById(R.id.editTextGpa);

        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        Button buttonSave = view.findViewById(R.id.buttonSave);

        builder.setView(view);
        Dialog dialog = builder.create();

        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        buttonSave.setOnClickListener(v -> {
            String surname = editSurname.getText().toString().trim();
            String name = editName.getText().toString().trim();
            String idStr = editId.getText().toString().trim();
            String gpaStr = editGpa.getText().toString().trim();

            if (TextUtils.isEmpty(surname) || TextUtils.isEmpty(name) ||
                    TextUtils.isEmpty(idStr) || TextUtils.isEmpty(gpaStr)) {
                Toast.makeText(getContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            int profileId;
            float gpa;
            try {
                profileId = Integer.parseInt(idStr);
                gpa = Float.parseFloat(gpaStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid ID or GPA format.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (profileId < 10000000 || profileId > 99999999) {
                Toast.makeText(getContext(), "ID must be 8 digits.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (gpa < 0.0f || gpa > 4.3f) {
                Toast.makeText(getContext(), "GPA must be between 0 and 4.3.", Toast.LENGTH_SHORT).show();
                return;
            }

            Profile profile = new Profile(profileId, surname, name, gpa);

            dbHelper.addProfile(profile);
            Toast.makeText(getContext(), "Profile saved successfully.", Toast.LENGTH_SHORT).show();

            dialog.dismiss();
            listener.onProfileAdded();
        });

        return dialog;
    }
}
