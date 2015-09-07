package team5.ad.sa40.stationeryinventory;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.Model.Delegate;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewDelegate extends android.support.v4.app.Fragment {

    @Bind(R.id.spnEmp)Spinner spnEmp;
    @Bind(R.id.txtStartDate)EditText text_start_date;
    @Bind(R.id.txtEndDate)EditText text_end_date;
    @Bind(R.id.btnSubmit)Button btnSubmit;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    public AddNewDelegate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_new_delegate, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = this.getArguments();
        Delegate delegate;
        if(bundle != null){
            delegate = (Delegate)bundle.getSerializable("delegate");
            text_start_date.setText(Setup.parseDateToString(delegate.getStartDate()));
            text_end_date.setText(Setup.parseDateToString(delegate.getEndDate()));
            btnSubmit.setText("Save");
        }

        String[] EmpNames = {"Erick", "John", "David Beckham", "Wayne", "Ryan"};
        ArrayAdapter<String> col_collect = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,EmpNames);
        col_collect.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spnEmp.setAdapter(col_collect);
        //Date dialog codes
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this.getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                text_start_date.setText(Setup.parseDateToString(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this.getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                text_end_date.setText(Setup.parseDateToString(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        text_start_date.setInputType(InputType.TYPE_NULL);
        text_start_date.requestFocus();
        text_end_date.setInputType(InputType.TYPE_NULL);

        text_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

        text_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        text_end_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    text_end_date.setError(null);
                    Date start = Setup.parseStringToDate(text_start_date.getText().toString());
                    Date end = Setup.parseStringToDate(text_end_date.getText().toString());

                    if (end.before(start)) {
                        text_end_date.setError("End Date should be after Start Date");
                    }
                }
            }
        });

        return view;
    }


}
