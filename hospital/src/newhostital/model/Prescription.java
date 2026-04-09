package newhostital.model;

import java.time.LocalDateTime;

public class Prescription {
    private int presId;
    private int appointId;
    private int doctorId;
    private String content;
    private String diagnosis;
    private String patientName;
    private String DoctorName;
    private String DeptName;
    private int patientId;




    public int getPresId() {return presId;}
    public void setPresId(int presId) {this.presId = presId;}
    public int getAppointId() {return appointId;}
    public void setAppointId(int appointId) {this.appointId = appointId;}
    public int getDoctorId() {return doctorId;}
    public void setDoctorId(int doctorId) {this.doctorId = doctorId;}
    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}
    public String getDiagnosis() {return diagnosis;}
    public void setDiagnosis(String diagnosis) {this.diagnosis = diagnosis;}
    public String getPatientName() {return patientName;}
    public void setPatientName(String patientName) {this.patientName = patientName;}
    public String getDoctorName() {return DoctorName;}
    public void setDoctorName(String doctorName) {this.DoctorName = doctorName;}
    public String getDeptName() {return DeptName;}
    public void setDeptName(String deptName) {this.DeptName = deptName;}
    public int getPatientId() {return patientId;}
    public void setPatientId(int patientId) {this.patientId = patientId;}




}
