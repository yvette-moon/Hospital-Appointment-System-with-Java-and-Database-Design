package newhostital.model;

public class Doctor {
    private int doctorID;
    private String Name;
    private String phone;
    private Integer deptId;
    private String username;
    private String deptname;

    public int getDoctorID() {return doctorID;}
    public void setDoctorID(int doctorID) {this.doctorID = doctorID;}
    public  String getName() {return Name;}
    public void setName(String name) {this.Name = name;}
    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}
    public Integer getDeptId() {return deptId;}
    public void setDeptId(Integer deptId) {this.deptId = deptId;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getDeptname() {return deptname;}
    public void setDeptname(String deptname) {this.deptname = deptname;}
}
