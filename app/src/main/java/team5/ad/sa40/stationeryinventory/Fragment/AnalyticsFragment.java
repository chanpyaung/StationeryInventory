package team5.ad.sa40.stationeryinventory.Fragment;


import android.app.Fragment;
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
public class AnalyticsFragment extends android.support.v4.app.Fragment {

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
                    }
                    BarDataSet barDataSet1 = new BarDataSet(valueSet1, jsonReportItems.get(i).getMonthYear());
                    barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
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


}
