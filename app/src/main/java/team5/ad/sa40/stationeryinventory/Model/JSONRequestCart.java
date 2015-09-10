package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

/**
 * Created by johnmajor on 9/10/15.
 */
public class JSONRequestCart {

    @Expose
    private Integer CartItemID;
    @Expose
    private Integer EmpID;
    @Expose
    private String ItemID;
    @Expose
    private Integer Qty;

    /**
     *
     * @return
     * The CartItemID
     */
    public Integer getCartItemID() {
        return CartItemID;
    }

    /**
     *
     * @param CartItemID
     * The CartItemID
     */
    public void setCartItemID(Integer CartItemID) {
        this.CartItemID = CartItemID;
    }

    /**
     *
     * @return
     * The EmpID
     */
    public Integer getEmpID() {
        return EmpID;
    }

    /**
     *
     * @param EmpID
     * The EmpID
     */
    public void setEmpID(Integer EmpID) {
        this.EmpID = EmpID;
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

}


