package newhostital.DAO;

import newhostital.model.Prescription;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDao {



    // 新增医嘱
    public static void save(Prescription p) {
        try (Connection conn = DBhelper.getConnection()) {
            String sql = "INSERT INTO prescription (appoint_id, patient_id, doctor_id, content, dignosis, doctorname, patientname) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, p.getAppointId());
            ps.setInt(2, p.getPatientId());
            ps.setInt(3, p.getDoctorId());
            ps.setString(4, p.getContent());
            ps.setString(5, p.getDiagnosis());
            ps.setString(6, p.getDoctorName());
            ps.setString(7, p.getPatientName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 更新医嘱
    // ===== 修改医嘱：只更新 dignosis (诊断) 和 content (医嘱内容) =====
    public static void update(Prescription p) {
        String sql = "UPDATE prescription SET dignosis=?, content=? WHERE pres_id=?";
        try (Connection conn = DBhelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getDiagnosis());
            ps.setString(2, p.getContent());
            ps.setInt(3, p.getPresId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 删除医嘱
    public static void delete(int presId) {
        try (Connection conn = DBhelper.getConnection()) {
            String sql = "DELETE FROM prescription WHERE pres_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, presId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<Prescription> getAllPrescriptions() {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT * FROM prescription";

        try (Connection conn = DBhelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Prescription p = new Prescription();
                p.setPresId(rs.getInt("pres_id"));
                p.setAppointId(rs.getInt("appoint_id"));
                p.setDoctorId(rs.getInt("doctor_id"));
                p.setPatientId(rs.getInt("patient_id"));
                p.setDoctorName(rs.getString("doctorname"));
                p.setPatientName(rs.getString("patientname"));
                p.setDeptName(rs.getString("deptname"));
                p.setDiagnosis(rs.getString("dignosis"));
                p.setContent(rs.getString("content"));

                list.add(p);
            }
        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

}
