package newhostital.DAO;

import newhostital.model.Appointment;
import newhostital.model.Doctor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorDao {
    public static List<Appointment> getRecordsByDoctorId(int doctorId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM view_doctor_records WHERE doctor_id=?";
        try (Connection conn = DBhelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
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

    public Doctor getDocByUsername(String username) {
        try (Connection conn = DBhelper.getConnection()) {
            String sql = "select * from doctor where username=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Doctor p = new Doctor();
                p.setDoctorID(rs.getInt("doctor_id"));
                p.setName(rs.getString("name"));
                p.setPhone(rs.getString("phone_number"));
                p.setDeptname(rs.getString("dept_name"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void save(Doctor p) {
        try (Connection conn = DBhelper.getConnection()) {

            String sqlInsert = "INSERT INTO doctor (name, phone_number, username,dept_name) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(sqlInsert);
            insertStmt.setString(1, p.getName());
            insertStmt.setString(2, p.getPhone());
            insertStmt.setString(3, p.getUsername());
            insertStmt.setString(4, p.getDeptname());
            insertStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public static List<Doctor> getDoctorsByDeptId(int deptId) {
        String sql = "SELECT doctor_id, name FROM doctor WHERE dept_id = ?";
        List<Doctor> list = new ArrayList<>();
        try (Connection conn = DBhelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Doctor d = new Doctor();
                d.setDoctorID(rs.getInt("doctor_id"));
                d.setName(rs.getString("name"));
                list.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Doctor getDoctorByName(String name) {
        try (Connection conn = DBhelper.getConnection()) {
            String sql = "SELECT doctor_id, name FROM doctor WHERE name = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Doctor d = new Doctor();
                d.setDoctorID(rs.getInt("doctor_id"));
                d.setName(rs.getString("name"));
                return d;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 根据关键字查询医生（name 或 dept_name）
    public static List<Doctor> getDoctors(String keyword) {
        List<Doctor> list = new ArrayList<>();
        try (Connection conn = DBhelper.getConnection()) {
            String sql = "SELECT * FROM doctor";
            if (keyword != null && !keyword.isEmpty()) {
                sql += " WHERE name LIKE ? OR dept_name LIKE ?";
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            if (keyword != null && !keyword.isEmpty()) {
                String like = "%" + keyword + "%";
                ps.setString(1, like);
                ps.setString(2, like);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Doctor d = new Doctor();
                d.setDoctorID(rs.getInt("doctor_id"));
                d.setName(rs.getString("name"));
                d.setPhone(rs.getString("phone_number"));
                d.setDeptname(rs.getString("dept_name"));
                d.setDeptId(rs.getInt("dept_id"));
                d.setUsername(rs.getString("username"));
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 更新医生信息
    public static void update(Doctor d) {
        try (Connection conn = DBhelper.getConnection()) {
            String sql = "UPDATE doctor SET name=?, phone_number=?, dept_name=?,username=?WHERE doctor_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, d.getName());
            ps.setString(2, d.getPhone());
            ps.setString(3, d.getDeptname());
            ps.setString(4, d.getUsername());
            ps.setInt(5, d.getDoctorID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 删除医生
    public static void delete(int doctorId) {
        try (Connection conn = DBhelper.getConnection())
        { String sql = "DELETE FROM doctor WHERE doctor_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, doctorId); ps.executeUpdate();
        } catch (SQLException e)
        { e.printStackTrace(); }
    }
}
