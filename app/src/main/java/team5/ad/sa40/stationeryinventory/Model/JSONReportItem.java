package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnmajor on 9/16/15.
 */
public class JSONReportItem {

    @Expose
    private String MonthYear;
    @Expose
    private List<ReportItem> ReportItems = new ArrayList<ReportItem>();

    /**
     *
     * @return
     * The MonthYear
     */
    public String getMonthYear() {
        return MonthYear;
    }

    /**
     *
     * @param MonthYear
     * The MonthYear
     */
    public void setMonthYear(String MonthYear) {
        this.MonthYear = MonthYear;
    }

    /**
     *
     * @return
     * The ReportItems
     */
    public List<ReportItem> getReportItems() {
        return ReportItems;
    }

    /**
     *
     * @param ReportItems
     * The ReportItems
     */
    public void setReportItems(List<ReportItem> ReportItems) {
        this.ReportItems = ReportItems;
    }

}
