package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

public class JSONSupplier {

    @Expose
    private String Address;
    @Expose
    private String Contact;
    @Expose
    private Integer Fax;
    @Expose
    private Integer Phone;
    @Expose
    private Integer Rank;
    @Expose
    private String RegNo;
    @Expose
    private String SupplierID;
    @Expose
    private String SupplierName;

    /**
     *
     * @return
     * The Address
     */
    public String getAddress() {
        return Address;
    }

    /**
     *
     * @param Address
     * The Address
     */
    public void setAddress(String Address) {
        this.Address = Address;
    }

    /**
     *
     * @return
     * The Contact
     */
    public String getContact() {
        return Contact;
    }

    /**
     *
     * @param Contact
     * The Contact
     */
    public void setContact(String Contact) {
        this.Contact = Contact;
    }

    /**
     *
     * @return
     * The Fax
     */
    public Integer getFax() {
        return Fax;
    }

    /**
     *
     * @param Fax
     * The Fax
     */
    public void setFax(Integer Fax) {
        this.Fax = Fax;
    }

    /**
     *
     * @return
     * The Phone
     */
    public Integer getPhone() {
        return Phone;
    }

    /**
     *
     * @param Phone
     * The Phone
     */
    public void setPhone(Integer Phone) {
        this.Phone = Phone;
    }

    /**
     *
     * @return
     * The Rank
     */
    public Integer getRank() {
        return Rank;
    }

    /**
     *
     * @param Rank
     * The Rank
     */
    public void setRank(Integer Rank) {
        this.Rank = Rank;
    }

    /**
     *
     * @return
     * The RegNo
     */
    public String getRegNo() {
        return RegNo;
    }

    /**
     *
     * @param RegNo
     * The RegNo
     */
    public void setRegNo(String RegNo) {
        this.RegNo = RegNo;
    }

    /**
     *
     * @return
     * The SupplierID
     */
    public String getSupplierID() {
        return SupplierID;
    }

    /**
     *
     * @param SupplierID
     * The SupplierID
     */
    public void setSupplierID(String SupplierID) {
        this.SupplierID = SupplierID;
    }

    /**
     *
     * @return
     * The SupplierName
     */
    public String getSupplierName() {
        return SupplierName;
    }

    /**
     *
     * @param SupplierName
     * The SupplierName
     */
    public void setSupplierName(String SupplierName) {
        this.SupplierName = SupplierName;
    }

}
