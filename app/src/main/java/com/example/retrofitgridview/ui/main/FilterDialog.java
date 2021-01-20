package com.example.retrofitgridview.ui.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.retrofitgridview.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class FilterDialog extends AppCompatDialogFragment {
    private SwitchMaterial swCopyRight;
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.filter_dialog, null);
        builder.setView(view)
                .setTitle("Filtros")
                .setNegativeButton("Cancelar", (dialog, which) -> {

                })
                .setPositiveButton("ok", (dialog, which) -> {

                });
        swCopyRight = view.findViewById(R.id.switchCopyright);

        return builder.create();
    }
}
