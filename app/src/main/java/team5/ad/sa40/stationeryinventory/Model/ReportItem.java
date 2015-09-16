package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

/**
 * Created by johnmajor on 9/16/15.
 */
public class ReportItem {

    @Expose
    private Double Price;
    @Expose
    private Integer Qty;
    @Expose
    private String Subject;

    /**
     *
     * @return
     * The Price
     */
    public Double getPrice() {
        return Price;
    }

    /**
     *
     * @param Price
     * The Price
     */
    public void setPrice(Double Price) {
        this.Price = Price;
    }

    /**
     *
     * @return
     * The Qty
     */
    public Integer getQty() {
        return Qty;
    }

    /**
     *
     * @param Qty
     * The Qty
     */
    public void setQty(Integer Qty) {
        this.Qty = Qty;
    }

    /**
     *
     * @return
     * The Subject
     */
    public String getSubject() {
        return Subject;
    }

    /**
     *
     * @param Subject
     * The Subject
     */
    public void setSubject(String Subject) {
        this.Subject = Subject;
    }

}

