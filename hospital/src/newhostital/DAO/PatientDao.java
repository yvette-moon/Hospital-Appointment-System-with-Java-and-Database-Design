package newhostital.DAO;
 import newhostital.model.Appointment;
 import newhostital.model.Patient;
 import java.sql.*;
 import java.util.ArrayList;
 import java.util.List;

public class PatientDao {
    public static  void save(Patient p){
        try(Connection conn=DBhelper.getConnection()){
            String sqlCheck="Select patient_id from patient where identity=?";
            PreparedStatement checkStmt=conn.prepareStatement(sqlCheck);
            checkStmt.setString(1,p.getIdentity());
            ResultSet rs=checkStmt.executeQuery();
            if(rs.next()){
                String sqlupdate="UPDATE patient SET name=?, gender=?, age=?, phone=? ,username=?WHERE identity=?";
                PreparedStatement updateStmt=conn.prepareStatement(sqlupdate);
                updateStmt.setString(1,p.getName());
                updateStmt.setString(2,p.getGender());
                updateStmt.setInt(3,p.getAge());
                updateStmt.setString(4,p.getPhone());
                updateStmt.setString(5,p.getUsername());
                updateStmt.setString(6,p.getIdentity());

                updateStmt.executeUpdate();
            }else{
                String sqlInsert="INSERT INTO patient (name, gender, age, phone, identity,username) VALUES (?, ?,  ?, ?,?, ?)";
                PreparedStatement insertStmt=conn.prepareStatement(sqlInsert);
                insertStmt.setString(1,p.getName());
                insertStmt.setString(2,p.getGender());
                insertStmt.setInt(3,p.getAge());
                insertStmt.setString(4,p.getPhone());
                insertStmt.setString(5,p.getIdentity());
                insertStmt.setString(6,p.getUsername());
                insertStmt.executeUpdate();
            }

        }catch(SQLException e){
            e.printStackTrace();

        }
    }
    public static  Patient getPatientByUsername(String username){
        try(Connection conn=DBhelper.getConnection()){
            String sql="select * from patient where username=?";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1,username);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                Patient p=new Patient();
                p.setPatientId(rs.getInt("patient_id"));
                p.setName(rs.getString("name"));
                p.setGender(rs.getString("gender"));
                p.setAge(rs.getInt("age"));
                p.setPhone(rs.getString("phone"));
                p.setIdentity(rs.getString("identity"));
                return p;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    // 查询病人列表，可按姓名或身份证模糊查询
    public static List<Patient> getPatients(String keyword) {
        List<Patient> list = new ArrayList<>();
        try (Connection conn = DBhelper.getConnection()) {
            String sql = "SELECT * FROM patient";
            if (keyword != null && !keyword.isEmpty()) {
                sql += " WHERE name LIKE ? OR identity LIKE ?";
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            if (keyword != null && !keyword.isEmpty()) {
                String like = "%" + keyword + "%";
                ps.setString(1, like);
                ps.setString(2, like);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Patient p = new Patient();
                p.setPatientId(rs.getInt("patient_id"));
                p.setName(rs.getString("name"));
                p.setGender(rs.getString("gender"));
                p.setAge(rs.getInt("age"));
                p.setPhone(rs.getString("phone"));
                p.setUsername(rs.getString("username"));
                p.setIdentity(rs.getString("identity"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 单独更新病人
    public static void update(Patient p) {
        try (Connection conn = DBhelper.getConnection()) {
            String sql = "UPDATE patient SET name=?, gender=?, age=?, phone=? WHERE patient_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getName());
            ps.setString(2, p.getGender());
            ps.setInt(3, p.getAge());
            ps.setString(4, p.getPhone());
            ps.setInt(5, p.getPatientId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除病人
    public static void delete(int patientId) {
        try (Connection conn = DBhelper.getConnection()) {
            String sql = "DELETE FROM patient WHERE patient_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, patientId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<Appointment> getRecordsByPatientId(int patientId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM view_patient_records WHERE patient_id=?";
        try (Connection conn = DBhelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointId(rs.getInt("appoint_id"));
                a.setPatientId(rs.getInt("patient_id"));
                a.setPatient_name(rs.getString("patient_name"));
                a.setDoctorId(rs.getInt("doctor_id"));
                a.setDoctorName(rs.getString("doctor_name"));
                a.setDeptId(rs.getInt("dept_id"));
                a.setDeptName(rs.getString("deptname"));
                a.setStatus(rs.getString("status"));
                a.setDiagnosis(rs.getString("dignosis"));
                a.setAdvice(rs.getString("advice"));
                list.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}


