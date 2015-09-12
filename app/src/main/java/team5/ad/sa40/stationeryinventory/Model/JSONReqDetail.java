package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

/**
 * Created by johnmajor on 9/12/15.
 */
public class JSONReqDetail {
    @Expose
    private Integer IssueQty;
    @Expose
    private String ItemID;
    @Expose
    private Integer ReqID;
    @Expose
    private Integer ReqSN;
    @Expose
    private Integer RequestQty;

    /**
     *
     * @return
     * The IssueQty
     */
    public Integer getIssueQty() {
        return IssueQty;
    }

    /**
     *
     * @param IssueQty
     * The IssueQty
     */
    public void setIssueQty(Integer IssueQty) {
        this.IssueQty = IssueQty;
    }

    /**
     *
     * @return
     * The ItemID
     */
    public String getItemID() {
        return ItemID;
    }

    /**
     *
     * @param ItemID
     * The ItemID
     */
    public void setItemID(String ItemID) {
        this.ItemID = ItemID;
    }

    /**
     *
     * @return
     * The ReqID
     */
    public Integer getReqID() {
        return ReqID;
    }

    /**
     *
     * @param ReqID
     * The ReqID
     */
    public void setReqID(Integer ReqID) {
        this.ReqID = ReqID;
    }

    /**
     *
     * @return
     * The ReqSN
     */
    public Integer getReqSN() {
        return ReqSN;
    }

    /**
     *
     * @param ReqSN
     * The ReqSN
     */
    public void setReqSN(Integer ReqSN) {
        this.ReqSN = ReqSN;
    }

    /**
     *
     * @return
     * The RequestQty
     */
    public Integer getRequestQty() {
        return RequestQty;
    }

    /**
     *
     * @param RequestQty
     * The RequestQty
     */
    public void setRequestQty(Integer RequestQty) {
        this.RequestQty = RequestQty;
    }
}
