package newhostital.model;

public class Patient {
    private int patientId;
    private String Name;
    private String gender;
    private int  age;
    private String phone;
    private String identity;
    private String username;

    public int  getPatientId() {return patientId;}
    public void setPatientId(int patientId) {this.patientId = patientId;}
    public String getName() {return Name;}
    public void setName(String name) {this.Name = name;}
    public String getGender() {return gender;}
    public void setGender(String gender) {this.gender = gender;}
    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}
    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}
    public String getIdentity() {return identity;}
    public void setIdentity(String identity) {this.identity = identity;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

}
