package team5.ad.sa40.stationeryinventory.Fragment;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.ReportAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONReport;
import team5.ad.sa40.stationeryinventory.Model.JSONReportItem;
import team5.ad.sa40.stationeryinventory.Model.ReportItem;
import team5.ad.sa40.stationeryinventory.R;
import team5.ad.sa40.stationeryinventory.Utilities.Setup;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnalyticsFragment extends android.support.v4.app.Fragment implements MainActivity.OnBackPressedListener{

    ArrayList<BarDataSet> dataSets;
    ArrayList<BarEntry> valueSet1;

    List<String> xAxis = new ArrayList<String>();
    ArrayList<Integer> rpIDList;
    BarData data;
    int reportID;

    public AnalyticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_analytics_graph, container, false);
        getActivity().setTitle("Analytics Report");
        final BarChart chart = (BarChart) view.findViewById(R.id.barChart);
        if(getArguments()!=null){
            reportID = getArguments().getInt("ReportID");
        }
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        final ReportAPI rpAPI = restAdapter.create(ReportAPI.class);
        rpAPI.getReports(new Callback<List<JSONReport>>() {
            @Override
            public void success(List<JSONReport> jsonReports, Response response) {
                for (JSONReport jsonRP : jsonReports) {
                    rpIDList = new ArrayList<Integer>();
                    rpIDList.add(jsonRP.getReportID());
                    Log.i("Report ID is ", String.valueOf(jsonRP.getReportID()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("GetReport Failure", error.getUrl() + error.toString());
            }
        });


        rpAPI.getReportItems(reportID, new Callback<List<JSONReportItem>>() {
            @Override
            public void success(List<JSONReportItem> jsonReportItems, Response response) {

                dataSets = new ArrayList<BarDataSet>();
                for(ReportItem rpItem : jsonReportItems.get(0).getReportItems()){
                    xAxis.add(rpItem.getSubject());
                }
                for(int i =0; i<jsonReportItems.size(); i++){

                    List<ReportItem> myreportItemlist = jsonReportItems.get(i).getReportItems();
                    valueSet1 = new ArrayList<>();

                    for(int j = 0 ; j<myreportItemlist.size(); j++){

                        BarEntry vle1 = new BarEntry(myreportItemlist.get(j).getQty(), j);
                        valueSet1.add(vle1);
                    }//
                    BarDataSet barDataSet1 = new BarDataSet(valueSet1, jsonReportItems.get(i).getMonthYear());
                    if(jsonReportItems.get(i).getMonthYear().equals("COMM") || jsonReportItems.get(i).getMonthYear().equals("ALPHA") || jsonReportItems.get(i).getMonthYear().equals("Clip")){
                        barDataSet1.setColor(Color.RED);
                    }
                    else if (jsonReportItems.get(i).getMonthYear().equals("CPSC") || jsonReportItems.get(i).getMonthYear().equals("BANE") || jsonReportItems.get(i).getMonthYear().equals("Envelope")){
                        barDataSet1.setColor(Color.GREEN);
                    }
                    else if (jsonReportItems.get(i).getMonthYear().equals("ENGL") || jsonReportItems.get(i).getMonthYear().equals("CHEP") || jsonReportItems.get(i).getMonthYear().equals("Eraser")){
                        barDataSet1.setColor(Color.BLUE);
                    }
                    else if (jsonReportItems.get(i).getMonthYear().equals("REGR") || jsonReportItems.get(i).getMonthYear().equals("OMEGA") || jsonReportItems.get(i).getMonthYear().equals("Exercise")){
                        barDataSet1.setColor(Color.MAGENTA);
                    }
                    else if (jsonReportItems.get(i).getMonthYear().equals("ZOOL") || jsonReportItems.get(i).getMonthYear().equals("File")){
                        barDataSet1.setColor(Color.YELLOW);
                    }
                    else if (jsonReportItems.get(i).getMonthYear().equals("Pen") || jsonReportItems.get(i).getMonthYear().equals("Ruler")){
                        barDataSet1.setColor(Color.BLACK);
                    }
                    else if (jsonReportItems.get(i).getMonthYear().equals("Puncher") || jsonReportItems.get(i).getMonthYear().equals("Scissors")){
                        barDataSet1.setColor(Color.CYAN);
                    }
                    else if (jsonReportItems.get(i).getMonthYear().equals("Pad") || jsonReportItems.get(i).getMonthYear().equals("Tape")){
                        barDataSet1.setColor(Color.DKGRAY);
                    }
                    else if (jsonReportItems.get(i).getMonthYear().equals("Paper") || jsonReportItems.get(i).getMonthYear().equals("Sharpener")){
                        barDataSet1.setColor(Color.WHITE);
                    }
                    else if (jsonReportItems.get(i).getMonthYear().equals("Shorthand") || jsonReportItems.get(i).getMonthYear().equals("Tacks")){
                        barDataSet1.setColor(Color.rgb(0,23,32));
                    }
                    else if (jsonReportItems.get(i).getMonthYear().equals("Stapler") || jsonReportItems.get(i).getMonthYear().equals("Transparency")){
                        barDataSet1.setColor(Color.GRAY);
                    }
                    else{
                        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
                    }
                    dataSets.add(barDataSet1);
                }


                data = new BarData(xAxis, dataSets);
                chart.setData(data);
                chart.setDescription("My Chart");
                chart.animateXY(2000, 2000);
                chart.invalidate();

            }


            @Override
            public void failure(RetrofitError error) {
                Log.i("Fail get reportlist", error.getUrl() + " " + error.toString());
            }
        });
        return  view;
    }


    @Override
    public void doBack() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        ReportAPI rpAPI = restAdapter.create(ReportAPI.class);
        rpAPI.getReports(new Callback<List<JSONReport>>() {
            @Override
            public void success(List<JSONReport> jsonReports, Response response) {
                AnalyticsAdapter.mReports = jsonReports;
                AnalyticsListFragment afrag = new AnalyticsListFragment();
                getFragmentManager().beginTransaction().replace(R.id.frame, afrag).commit();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("Fail getReports", error.toString());
            }
        });
    }
}
