package newhostital.GUI;

import newhostital.model.Appointment;

import newhostital.model.Doctor;
import newhostital.service.Doctorservice;
import newhostital.DAO.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorGUI extends JFrame {

    private Doctorservice doctorService = new Doctorservice();
    private Doctor doctor;


    private JTable tablePending;
    private DefaultTableModel modelPending;

    private JTable tableHistory;
    private DefaultTableModel modelHistory;

    private JTextField tfDiagnosis;
    private JTextField tfAdvice;
    private JButton btnUpdatePrescription;

    public DoctorGUI(Doctor doctor) {
        this.doctor =doctor;

        setTitle("医生界面 - " +doctor.getName());
        setSize(900,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // 1️⃣ 待诊挂号
        JPanel panelPending = new JPanel(new BorderLayout());
        modelPending = new DefaultTableModel(new String[]{
                "挂号ID","病人","科室","状态"
        },0);
        tablePending = new JTable(modelPending);
        panelPending.add(new JScrollPane(tablePending), BorderLayout.CENTER);

        JPanel panelAction = new JPanel(new GridLayout(3,2,5,5));
        tfDiagnosis = new JTextField();
        tfAdvice = new JTextField();
        btnUpdatePrescription = new JButton("填写诊断/医嘱");

        JButton btnCall = new JButton("下一个病人");

        panelAction.add(new JLabel("诊断:")); panelAction.add(tfDiagnosis);
        panelAction.add(new JLabel("医嘱:")); panelAction.add(tfAdvice);
        panelAction.add(btnCall); panelAction.add(btnUpdatePrescription);

        panelPending.add(panelAction, BorderLayout.SOUTH);

        btnCall.addActionListener(e -> callPatient());
        btnUpdatePrescription.addActionListener(e -> updatePrescription());

        tabbedPane.addTab("待诊挂号", panelPending);

        // 2️⃣ 历史医嘱
        JPanel panelHistory = new JPanel(new BorderLayout());
        modelHistory = new DefaultTableModel(new String[]{
                "挂号ID","病人","科室","诊断","医嘱"
        },0);
        tableHistory = new JTable(modelHistory);
        panelHistory.add(new JScrollPane(tableHistory), BorderLayout.CENTER);

        JButton btnRefreshHistory = new JButton("刷新历史记录");
        panelHistory.add(btnRefreshHistory, BorderLayout.SOUTH);
        btnRefreshHistory.addActionListener(e -> loadHistory());

        tabbedPane.addTab("历史医嘱", panelHistory);

        add(tabbedPane, BorderLayout.CENTER);

        loadPending();
        loadHistory();

        setVisible(true);
    }



    private void loadPending() {
        modelPending.setRowCount(0);
        List<Appointment> list = DoctorDao.getRecordsByDoctorId(doctor.getDoctorID());
        for (Appointment a : list) {
            if ("待就诊".equals(a.getStatus())) {   // 只显示待就诊
                modelPending.addRow(new Object[]{
                        a.getAppointId(),
                        a.getPatient_name(),
                        a.getDeptName(),
                        a.getStatus()
                });
            }
        }
    }

    private void loadHistory() {
        modelHistory.setRowCount(0);
        List<Appointment> list = DoctorDao.getRecordsByDoctorId(doctor.getDoctorID());
        for (Appointment a : list) {
            if (!"待就诊".equals(a.getStatus())) {  // 历史记录
                modelHistory.addRow(new Object[]{
                        a.getAppointId(),
                        a.getPatient_name(),
                        a.getDeptName(),
                        a.getDiagnosis(),
                        a.getAdvice()
                });
            }
        }
    }



    private void callPatient(){
        int row = tablePending.getSelectedRow();
        if(row<0){
            JOptionPane.showMessageDialog(this,"请选择挂号记录！");
            return;
        }
        int appointmentId = (int) modelPending.getValueAt(row,0);
        doctorService.updateAppointmentStatus(appointmentId,"已就诊");
        JOptionPane.showMessageDialog(this,"已叫诊！");
        loadPending();
    }

    private void updatePrescription() {
        int row = tablePending.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请选择挂号记录！");
            return;
        }

        int appointmentId = (int) modelPending.getValueAt(row, 0);
        String diagnosis = tfDiagnosis.getText();
        String advice = tfAdvice.getText();

        if (diagnosis.isEmpty() || advice.isEmpty()) {
            JOptionPane.showMessageDialog(this, "诊断与医嘱不能为空！");
            return;
        }

        try (var conn = DBhelper.getConnection()) {

            // ① 获取病人姓名、科室（从 appointment 表）
            String infoSql = "SELECT patient_name, deptname, patient_id FROM appointment WHERE appoint_id=?";
            var psInfo = conn.prepareStatement(infoSql);
            psInfo.setInt(1, appointmentId);
            var rsInfo = psInfo.executeQuery();

            if (!rsInfo.next()) {
                JOptionPane.showMessageDialog(this, "无法找到挂号记录信息！");
                return;
            }

            String patientName = rsInfo.getString("patient_name");
            String deptName = rsInfo.getString("deptname");
            int patientId = rsInfo.getInt("patient_id");

            // ② 判断 prescription 是否已存在
            String checkSql = "SELECT pres_id FROM prescription WHERE appoint_id=?";
            var psCheck = conn.prepareStatement(checkSql);
            psCheck.setInt(1, appointmentId);
            var rs = psCheck.executeQuery();

            if (rs.next()) {
                //更新
                int presId = rs.getInt("pres_id");

                String update = "UPDATE prescription SET dignosis=?, content=?, doctorname=?, patientname=?, deptname=? WHERE pres_id=?";
                var psUp = conn.prepareStatement(update);

                psUp.setString(1, diagnosis);
                psUp.setString(2, advice);
                psUp.setString(3, doctor.getName());
                psUp.setString(4, patientName);
                psUp.setString(5, deptName);
                psUp.setInt(6, presId);

                psUp.executeUpdate();
            } else {
                // 插入
                String insert = "INSERT INTO prescription(appoint_id, patient_id, doctor_id, dignosis, content, doctorname, patientname, deptname) VALUES (?,?,?,?,?,?,?,?)";

                var psIn = conn.prepareStatement(insert);

                psIn.setInt(1, appointmentId);
                psIn.setInt(2, patientId);
                psIn.setInt(3, doctor.getDoctorID());
                psIn.setString(4, diagnosis);
                psIn.setString(5, advice);
                psIn.setString(6, doctor.getName());
                psIn.setString(7, patientName);
                psIn.setString(8, deptName);

                psIn.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(this, "诊断/医嘱已保存！");
        loadHistory();
    }

}
