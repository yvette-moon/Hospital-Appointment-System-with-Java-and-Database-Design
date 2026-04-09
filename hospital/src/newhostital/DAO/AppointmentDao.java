package newhostital.DAO;
import newhostital.model.Appointment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class AppointmentDao {

    public static void create(Appointment a) {
        try (Connection conn = DBhelper.getConnection()) {
             String sql="insert into appointment ( patient_id, doctor_id ,status,patient_name,deptname,doctor_name)" +
                     "values(?,?,?,?,?,?)";
             PreparedStatement ps = conn.prepareStatement(sql);
             ps.setInt(1,a.getPatientId());
             ps.setInt(2,a.getDoctorId());
             ps.setString(3,"待就诊");
             ps.setString(4,a.getPatient_name());
             ps.setString(5,a.getDeptName());
             ps.setString(6,a.getDoctorName());
             ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 新增：根据关键字查询挂号记录
    public static List<Appointment> getappointments(String keyword) {
        List<Appointment> list = new ArrayList<>();
        String sql;
        boolean useKeyword = keyword != null && !keyword.trim().isEmpty();

        if (useKeyword) {
            sql = "SELECT * FROM appointment WHERE patient_name LIKE ? OR doctor_name LIKE ?";
        } else {
            sql = "SELECT * FROM appointment";
        }

        try (Connection conn = DBhelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (useKeyword) {
                String kw = "%" + keyword.trim() + "%";
                ps.setString(1, kw);
                ps.setString(2, kw);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointId(rs.getInt("appoint_id"));
                a.setPatientId(rs.getInt("patient_id"));
                a.setPatient_name(rs.getString("patient_name"));
                a.setDoctorId(rs.getInt("doctor_id"));
                a.setDoctorName(rs.getString("doctor_name"));
                a.setDeptName(rs.getString("deptname"));
                a.setDeptId(rs.getInt("dept_id")); // 如果数据库有 dept_id
                a.setStatus(rs.getString("status"));
                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    public static void updateStatus(int appointmentId, String status){
        try(Connection conn = DBhelper.getConnection()){
            String sql = "UPDATE appointment SET status=? WHERE appoint_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,status);
            ps.setInt(2,appointmentId);
            ps.executeUpdate();
        } catch(SQLException e){ e.printStackTrace(); }
}
    // 更新预约
    public static void update(Appointment a) {
        try (Connection conn = DBhelper.getConnection()) {
            String sql = "UPDATE appointment SET patient_id=?,doctor_id=?,status=?, patient_name=?, deptname=?, doctor_name=? WHERE appoint_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,a.getPatientId());
            ps.setInt(2,a.getDoctorId());
            ps.setString(3, a.getStatus());
            ps.setString(4, a.getPatient_name());
            ps.setString(5, a.getDeptName());
            ps.setString(6, a.getDoctorName());
            ps.setInt(7, a.getAppointId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除预约
    public static void delete(int appointmentId) {
        try (Connection conn = DBhelper.getConnection()) {
            String sql = "DELETE FROM appointment WHERE appoint_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, appointmentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}