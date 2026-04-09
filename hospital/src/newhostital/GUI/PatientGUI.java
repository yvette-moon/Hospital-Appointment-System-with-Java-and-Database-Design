package newhostital.GUI;

import newhostital.model.*;
import newhostital.service.Doctorservice;
import newhostital.service.PatientService;
import newhostital.DAO.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientGUI extends JFrame {

    private PatientService patientService = new PatientService();
    private Patient patient;
    private JTextField tfName, tfGender, tfAge, tfIdentity, tfPhone;
    private JComboBox<String> cbDept, cbDoctor;
    private JTable tableRecords;
    private DefaultTableModel tableModel;
    private JTable tableAdvice;
    private DefaultTableModel adviceModel;


    public  PatientGUI (Patient patient){
            this.patient = patient;
            setTitle("病人界面 - " + patient.getName());
            setSize(800, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            JTabbedPane tabbedPane = new JTabbedPane();

            // 1️⃣ 个人信息 + 预约面板
            JPanel panelInfo = new JPanel(new GridLayout(9, 2, 5, 3));
            tfName = new JTextField(patient.getName());
            tfGender = new JTextField(patient.getGender());
            tfAge = new JTextField(String.valueOf(patient.getAge()));
            tfIdentity = new JTextField(patient.getIdentity());
            tfPhone = new JTextField(patient.getPhone());

            panelInfo.add(new JLabel("姓名:"));
            panelInfo.add(tfName);
            panelInfo.add(new JLabel("性别:"));
            panelInfo.add(tfGender);
            panelInfo.add(new JLabel("年龄:"));
            panelInfo.add(tfAge);
            panelInfo.add(new JLabel("身份证:"));
            panelInfo.add(tfIdentity);
            panelInfo.add(new JLabel("电话:"));
            panelInfo.add(tfPhone);

            panelInfo.add(new JLabel("科室:"));
            cbDept = new JComboBox<>(getDepartments());
            panelInfo.add(cbDept);

            panelInfo.add(new JLabel("医生:"));
            cbDoctor = new JComboBox<>();
            panelInfo.add(cbDoctor);
        // 初始化医生列表
            updateDoctorComboBox(getSelectedDeptId());
        // 科室切换时动态更新医生列表
           cbDept.addActionListener(e -> updateDoctorComboBox(getSelectedDeptId()));
            JButton btnSave = new JButton("保存信息并预约");
            panelInfo.add(new JLabel()); // 占位，让按钮居右
            panelInfo.add(btnSave);

            btnSave.addActionListener(e -> saveAndBook());

            tabbedPane.addTab("个人信息/预约", panelInfo);

            // 2️⃣ 挂号记录 + 医嘱
            JPanel panelRecords = new JPanel(new BorderLayout(10,10));
            tableModel = new DefaultTableModel(new String[]{
                    "挂号ID", "科室", "医生ID", "状态"
            }, 0);
            tableRecords = new JTable(tableModel);
            panelRecords.add(new JScrollPane(tableRecords), BorderLayout.CENTER);

            JButton btnRefresh = new JButton("刷新记录");
            panelRecords.add(btnRefresh, BorderLayout.SOUTH);
            btnRefresh.addActionListener(e -> loadRecords());

            tabbedPane.addTab("挂号记录", panelRecords);



            JPanel panelPrescriptions = new JPanel(new BorderLayout(10, 10));
            adviceModel  = new DefaultTableModel(new String[]{
                    "挂号ID", "科室", "医生ID", "诊断", "医嘱"
            }, 0);
            tableAdvice = new JTable(adviceModel);
             panelPrescriptions.add(new JScrollPane(tableAdvice), BorderLayout.CENTER);
            JButton btnAdviceRefresh = new JButton("刷新医嘱");
            panelPrescriptions.add(btnAdviceRefresh, BorderLayout.SOUTH);
            btnAdviceRefresh.addActionListener(e -> loadPrescriptions());

            tabbedPane.addTab("医嘱", panelPrescriptions);



// ====== 添加选项卡到主界面 ======
            add(tabbedPane, BorderLayout.CENTER);
            loadRecords();
            setVisible(true);
        }


    // 获取科室列表（示例从数据库或写死）
    private String[] getDepartments(){

        return new String[]{"内科","外科","儿科","骨科"};
    }

    private int getSelectedDeptId(){
        return cbDept.getSelectedIndex()+1; // 简化，科室ID从1开始
    }
//更新下拉框
    private void updateDoctorComboBox(int deptId) {
        cbDoctor.removeAllItems();
        List<Doctor> doctors = Doctorservice.getDoctorsByDeptId(deptId);
        for (Doctor d : doctors) {
            cbDoctor.addItem(d.getName());
        }
    }
    private int getSelectedDoctorId() {
        String doctorName = (String) cbDoctor.getSelectedItem();
        if (doctorName == null) return -1;
        Doctor d = DoctorDao.getDoctorByName(doctorName);
        System.out.println( doctorName);
        return d != null ? d.getDoctorID() : -1;
    }

 //预约事件
    private void saveAndBook(){
        // 保存病人信息
        patient.setGender(tfGender.getText());
        patient.setAge(Integer.parseInt(tfAge.getText()));
        patient.setIdentity(tfIdentity.getText());
        patient.setPhone(tfPhone.getText());
        patient.setName(tfName.getText());
        String deptName = (String) cbDept.getSelectedItem();
        String doctorName = (String) cbDoctor.getSelectedItem();
        patientService.savePatient(patient);

        // 创建预约
        Appointment appt = new Appointment();
        appt.setPatientId(patient.getPatientId());
        appt.setPatient_name(patient.getName());
        appt.setDeptId(getSelectedDeptId());
        appt.setDoctorId(getSelectedDoctorId());
        appt.setDeptName(deptName);
        appt.setDoctorName(doctorName);
        appt.setStatus("待就诊");
        AppointmentDao.create(appt);
        JOptionPane.showMessageDialog(this,"预约成功！");
        loadRecords();
    }

    private void loadRecords() {
        tableModel.setRowCount(0);
        List<Appointment> list =PatientDao.getRecordsByPatientId(patient.getPatientId());
        for (Appointment a : list) {
            tableModel.addRow(new Object[]{
                    a.getAppointId(),
                    a.getDeptName(),    // 科室名称
                    a.getDoctorName(),  // 医生姓名
                    a.getStatus()
            });
        }
    }

    private void loadPrescriptions() {
        adviceModel.setRowCount(0);
        List<Appointment> list = PatientDao.getRecordsByPatientId(patient.getPatientId());
        for (Appointment a : list) {
            // 医嘱可能为空
            if (a.getDiagnosis() != null || a.getAdvice() != null) {
                adviceModel.addRow(new Object[]{
                        a.getAppointId(),
                        a.getDeptName(),
                        a.getDoctorName(),
                        a.getDiagnosis(),
                        a.getAdvice()
                });
            }
        }
    }



}
