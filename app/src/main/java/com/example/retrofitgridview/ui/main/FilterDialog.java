package com.example.retrofitgridview.ui.main;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.retrofitgridview.R;
import com.example.retrofitgridview.ui.book.MainListFragment;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class FilterDialog extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {

    private SwitchMaterial swCopyRight;
    private TextView tvFromYear;
    private TextView tvToYear;
    private FilterDialogListener listener;
    private Spinner languageSelector;
    private Spinner categorySelector;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String FROM_YEAR = "fromYear";
    public static final String TO_YEAR = "toYear";
    public static final String SWITCH_COPYRIGHT = "switchCopyright";
    public static final String LANGUAGE = "language";
    public static final String LANGUAGE_SHARED_PREFS = "languageSharedPrefs";


    private Context context;
    private String savedLanguage;
    private Boolean savedSwitch;
    private String savedFromYear;
    private String savedToYear;
    private String language;
    private String[] languagesAbbreviated;
    private String[] languages;


    public FilterDialog(Context context) {
        this.context = context;
       // loadFilters();
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.filter_dialog, null);

        languages = getResources().getStringArray(R.array.languages);
        languagesAbbreviated = getResources().getStringArray(R.array.languagesAbbreviation);

        swCopyRight = view.findViewById(R.id.switchCopyright);
        tvFromYear = view.findViewById(R.id.tvFromYear);
        tvToYear = view.findViewById(R.id.tvToYear);
        languageSelector = view.findViewById(R.id.spinnerLanguageSelector);
        categorySelector = view.findViewById(R.id.spinnerCategorySelector);
        setSpinner(languageSelector, R.array.languages);
        setSpinner(categorySelector, R.array.categories);
        clickListener(tvFromYear);
        clickListener(tvToYear);

        builder.setView(view)
                .setTitle("Filtros")
                .setNegativeButton("Cancelar", (dialog, which) -> {
                })
                .setPositiveButton("Aplicar", (dialog, which) -> {
                    Boolean copyright = swCopyRight.isChecked();
                    String fromYear = tvFromYear.getText().toString();
                    String toYear = tvToYear.getText().toString();
                    String language = languagesAbbreviated[languageSelector.getSelectedItemPosition()];
                    listener.getFilters(copyright, fromYear, toYear, language);
                    saveFilters();
                    MainListFragment mainListFragment = MainListFragment.newInstance(fromYear, toYear, copyright, language);
                    ((MainActivity) getActivity()).setFragment(mainListFragment);
                });
        loadFilters();
        updateFilters();
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (FilterDialogListener) context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        language = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface FilterDialogListener {
        void getFilters(Boolean copyright, String fromYear, String toYear, String language);
    }

    private DatePickerDialog createDialogWithoutDateField(TextView tvYear) {
        DatePickerDialog dpd = new DatePickerDialog(getContext());

        dpd.getDatePicker().getTouchables().get(0).performClick();
        Calendar calendar = Calendar.getInstance();
        dpd.getDatePicker().init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                (view, year, monthOfYear, dayOfMonth) ->
                {
                    Log.d("fecha", year + "");
                    tvYear.setText(year + "");
                    dpd.dismiss();
                });

        return dpd;
    }

    private void clickListener(View view) {
        view.setOnClickListener(v -> {
            createDialogWithoutDateField((TextView) v).show();
        });
    }

    public void saveFilters() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SWITCH_COPYRIGHT, swCopyRight.isChecked());
        editor.putString(FROM_YEAR, tvFromYear.getText().toString());
        editor.putString(TO_YEAR, tvToYear.getText().toString());
        editor.putString(LANGUAGE, languagesAbbreviated[languageSelector.getSelectedItemPosition()]);
        editor.apply();
    }

    public void loadFilters() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        savedSwitch = sharedPreferences.getBoolean(SWITCH_COPYRIGHT, false);
        savedFromYear = sharedPreferences.getString(FROM_YEAR, "");
        savedToYear = sharedPreferences.getString(TO_YEAR, "");
        String value = sharedPreferences.getString(LANGUAGE, "");
        int i = Arrays.asList(languagesAbbreviated).indexOf(value);
        savedLanguage = languages[i];
    }

    public void updateFilters() {
        swCopyRight.setChecked(savedSwitch);
        tvFromYear.setText(savedFromYear);
        tvToYear.setText(savedToYear);
        String[] languages = getResources().getStringArray(R.array.languages);
        languageSelector.setSelection(Arrays.asList(languages).indexOf(savedLanguage));
    }

    public void setSpinner(Spinner spinner, int content) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), content, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public Boolean getSavedSwitch() {
        return savedSwitch;
    }

    public String getSavedFromYear() {
        return savedFromYear;
    }

    public String getSavedToYear() {
        return savedToYear;
    }


}
