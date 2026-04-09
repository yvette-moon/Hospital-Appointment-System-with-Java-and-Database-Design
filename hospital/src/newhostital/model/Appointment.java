package newhostital.model;

public class Appointment {
    private int appointId;
    private int patientId;
    private int doctorId;
    private String doctor_name;
    private String status;
    private String patient_name;
    private String diagnosis;
    private String advice;
    private String deptname;
    private int deptId;

    public int getAppointId() {return appointId;}
    public void setAppointId(int appointId) {this.appointId = appointId;}
    public int getPatientId() {return patientId;}
    public void setPatientId(int patientId) {this.patientId = patientId;}
    public int getDoctorId() {return doctorId;}
    public void setDoctorId(int doctorId) {this.doctorId = doctorId;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public String getPatient_name() {return patient_name;}
    public void setPatient_name(String patient_name) {this.patient_name = patient_name;}
    public String getDiagnosis() {return diagnosis;}
    public void setDiagnosis(String diagnosis) {this.diagnosis = diagnosis;}
    public String getAdvice() {return advice;}
    public void setAdvice(String advice) {this.advice = advice;}
    public int getDeptId() {return deptId;}
    public void setDeptId(int deptId) {this.deptId = deptId;}
    public void setDeptName(String deptName) {this.deptname = deptName;}
    public String getDeptName() {return deptname;}
    public void setDoctorName(String doctorName) {this.doctor_name = doctorName;}
    public String getDoctorName() {return doctor_name;}
}

