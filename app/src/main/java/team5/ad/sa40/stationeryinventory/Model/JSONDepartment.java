package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

public class JSONDepartment {

    @Expose
    private Integer CPID;
    @Expose
    private Integer Contact;
    @Expose
    private Integer DeptHead;
    @Expose
    private String DeptID;
    @Expose
    private String DeptName;
    @Expose
    private Integer DeptRep;
    @Expose
    private Integer Fax;
    @Expose
    private Integer Phone;

    /**
     *
     * @return
     * The CPID
     */
    public Integer getCPID() {
        return CPID;
    }

    /**
     *
     * @param CPID
     * The CPID
     */
    public void setCPID(Integer CPID) {
        this.CPID = CPID;
    }

    /**
     *
     * @return
     * The Contact
     */
    public Integer getContact() {
        return Contact;
    }

    /**
     *
     * @param Contact
     * The Contact
     */
    public void setContact(Integer Contact) {
        this.Contact = Contact;
    }

    /**
     *
     * @return
     * The DeptHead
     */
    public Integer getDeptHead() {
        return DeptHead;
    }

    /**
     *
     * @param DeptHead
     * The DeptHead
     */
    public void setDeptHead(Integer DeptHead) {
        this.DeptHead = DeptHead;
    }

    /**
     *
     * @return
     * The DeptID
     */
    public String getDeptID() {
        return DeptID;
    }

    /**
     *
     * @param DeptID
     * The DeptID
     */
    public void setDeptID(String DeptID) {
        this.DeptID = DeptID;
    }

    /**
     *
     * @return
     * The DeptName
     */
    public String getDeptName() {
        return DeptName;
    }

    /**
     *
     * @param DeptName
     * The DeptName
     */
    public void setDeptName(String DeptName) {
        this.DeptName = DeptName;
    }

    /**
     *
     * @return
     * The DeptRep
     */
    public Integer getDeptRep() {
        return DeptRep;
    }

    /**
     *
     * @param DeptRep
     * The DeptRep
     */
    public void setDeptRep(Integer DeptRep) {
        this.DeptRep = DeptRep;
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

}