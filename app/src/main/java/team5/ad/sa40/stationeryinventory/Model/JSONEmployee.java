package team5.ad.sa40.stationeryinventory.Model;

/**
 * Created by johnmajor on 9/9/15.
 */

import com.google.gson.annotations.Expose;
public class JSONEmployee {

    @Expose
    private String DeptID;
    @Expose
    private String Email;
    @Expose
    private Integer EmpID;
    @Expose
    private String EmpName;
    @Expose
    private String Password;
    @Expose
    private Integer Phone;
    @Expose
    private String RoleID;

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
     * The Email
     */
    public String getEmail() {
        return Email;
    }

    /**
     *
     * @param Email
     * The Email
     */
    public void setEmail(String Email) {
        this.Email = Email;
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
     * The EmpName
     */
    public String getEmpName() {
        return EmpName;
    }

    /**
     *
     * @param EmpName
     * The EmpName
     */
    public void setEmpName(String EmpName) {
        this.EmpName = EmpName;
    }

    /**
     *
     * @return
     * The Password
     */
    public String getPassword() {
        return Password;
    }

    /**
     *
     * @param Password
     * The Password
     */
    public void setPassword(String Password) {
        this.Password = Password;
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
     * The RoleID
     */
    public String getRoleID() {
        return RoleID;
    }

    /**
     *
     * @param RoleID
     * The RoleID
     */
    public void setRoleID(String RoleID) {
        this.RoleID = RoleID;
    }

}