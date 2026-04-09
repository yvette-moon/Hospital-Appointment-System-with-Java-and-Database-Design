package newhostital.GUI;

import newhostital.model.*;
import newhostital.service.AdminService;
import newhostital.util.EncryptUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminGUI extends JFrame {

    private AdminService adminService = new AdminService();

    private JTabbedPane tabbedPane;

    // 表格和模型
    private JTable tableDoctors, tablePatients, tableAppointments, tablePrescriptions,tableUsers;
    private DefaultTableModel modelDoctors, modelPatients, modelAppointments, modelPrescriptions,modelUsers;

    public AdminGUI(Admin ad) {
        setTitle("管理员界面");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();

        initDoctorPanel();
        initPatientPanel();
        initAppointmentPanel();
        initPrescriptionPanel();
        initUserPanel();

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

        private void initUserPanel () {
            JPanel panelUsers = new JPanel(new BorderLayout());
           modelUsers = new DefaultTableModel(
                    new String[]{"用户ID", "用户名", "角色", "密码"}, 0
            );
           tableUsers = new JTable(modelUsers);
            panelUsers.add(new JScrollPane(tableUsers), BorderLayout.CENTER);

;
            JPanel userButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            JButton btnAddUser = new JButton("添加用户");
            JButton btnEditUser = new JButton("编辑用户");
            JButton btnDeleteUser = new JButton("删除用户");

            userButtonPanel.add(btnAddUser);
            userButtonPanel.add(btnEditUser);
            userButtonPanel.add(btnDeleteUser);

            panelUsers.add(userButtonPanel, BorderLayout.SOUTH);
            btnAddUser.addActionListener(e -> addUserDialog());
            btnEditUser.addActionListener(e -> EditUser());
            btnDeleteUser.addActionListener(e -> DeleteUser());


            tabbedPane.addTab("用户管理", panelUsers);
            loadUsers();
        }
        private void loadUsers () {
            modelUsers.setRowCount(0);
            List<User> users = adminService.getUsers(null); // 可加关键字搜索
            for (User u : users) {
                modelUsers.addRow(new Object[]{u.getUserid(), u.getUsername(), u.getRole(), u.getPassword()});
            }

        }
// 添加用户
        private void addUserDialog () {
            JTextField tfUsername = new JTextField();
            JTextField tfPassword = new JTextField();
            JComboBox<String> cbRole = new JComboBox<>(new String[]{"admin", "doctor", "patient"});

            Object[] message = {
                    "用户名:", tfUsername,
                    "密码:", tfPassword,
                    "角色:", cbRole
            };

            int option = JOptionPane.showConfirmDialog(this, message, "添加用户", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                User u = new User();
                u.setUsername(tfUsername.getText());
                String hash = EncryptUtil.md5(tfPassword.getText());
                u.setPassword(hash);
                u.setRole((String) cbRole.getSelectedItem());
                adminService.addUser(u);
                loadUsers();
            }
        }

// 编辑用户
        private void EditUser () {
            int row = tableUsers.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "请选择要编辑的用户！");
                return;
            }
            int userId = (int) modelUsers.getValueAt(row, 0);
            String username = (String) modelUsers.getValueAt(row, 1);
            String role = (String) modelUsers.getValueAt(row, 2);

            JTextField tfUsername = new JTextField(username);
            JTextField tfPassword = new JTextField(); // 可以不显示原密码
            JComboBox<String> cbRole = new JComboBox<>(new String[]{"admin", "doctor", "patient"});
            cbRole.setSelectedItem(role);

            Object[] message = {
                    "用户名:", tfUsername,
                    "密码(不修改可留空):", tfPassword,
                    "角色:", cbRole
            };

            int option = JOptionPane.showConfirmDialog(this, message, "编辑用户", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                User u = new User();
                String hash = EncryptUtil.md5(tfPassword.getText());
                u.setUserid(userId);
                u.setUsername(tfUsername.getText());
                if (!tfPassword.getText().isEmpty()) {
                    u.setPassword(hash);
                }
                u.setRole((String) cbRole.getSelectedItem());
                adminService.updateUser(u);
                loadUsers();
            }
        }

// 删除用户
        private void DeleteUser () {
            int row = tableUsers.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "请选择要删除的用户！");
                return;
            }
            int userId = (int) modelUsers.getValueAt(row, 0);
            int option = JOptionPane.showConfirmDialog(this, "确认删除该用户？", "删除用户", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                adminService.deleteUser(userId);
                loadUsers();
            }
        }




    // ==== 医生面板 ====
    private void initDoctorPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        modelDoctors = new DefaultTableModel(new String[]{"ID", "姓名", "科室ID", "电话","用户名"}, 0);
        tableDoctors = new JTable(modelDoctors);
        panel.add(new JScrollPane(tableDoctors), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("新增"), btnUpdate = new JButton("修改"), btnDelete = new JButton("删除"),
                btnSearch = new JButton("查询"), btnRefresh = new JButton("刷新");
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnSearch);
        btnPanel.add(btnRefresh);
        panel.add(btnPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addDoctorDialog());
        btnUpdate.addActionListener(e -> updateDoctorDialog());
        btnDelete.addActionListener(e -> deleteDoctor());
        btnSearch.addActionListener(e -> searchDoctor());
        btnRefresh.addActionListener(e -> loadDoctors());

        tabbedPane.addTab("医生管理", panel);
        loadDoctors();
    }

    private void loadDoctors() {
        modelDoctors.setRowCount(0);
        List<Doctor> list = adminService.getDoctors(null);
        for (Doctor d : list)
            modelDoctors.addRow(new Object[]{d.getDoctorID(), d.getName(), d.getDeptId(), d.getPhone(),d.getUsername()});
    }

    private void addDoctorDialog() {
        JTextField tfName = new JTextField();
        JTextField tfDeptName = new JTextField();
        JTextField tfPhone = new JTextField();
        JTextField tfUsername = new JTextField();

        Object[] msg = {"姓名", tfName, "科室", tfDeptName, "电话", tfPhone};
        if (JOptionPane.showConfirmDialog(this, msg, "新增医生", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Doctor d = new Doctor();
            d.setName(tfName.getText());
            d.setDeptname(tfDeptName.getText());
            d.setPhone(tfPhone.getText());
            d.setUsername(tfUsername.getText());
            adminService.addDoctor(d);
            loadDoctors();
        }
    }

    private void updateDoctorDialog() {
        int row = tableDoctors.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "请选择医生");
            return;
        }
        int id = (int) tableDoctors.getValueAt(row, 0);
        JTextField tfName = new JTextField((String) tableDoctors.getValueAt(row, 1));
        JTextField tfDeptName = new JTextField(tableDoctors.getValueAt(row, 2).toString());
        JTextField tfPhone = new JTextField((String) tableDoctors.getValueAt(row, 3));
        JTextField tfUsername = new JTextField((String) tableDoctors.getValueAt(row, 4));
        Object[] msg = {"姓名", tfName, "科室", tfDeptName, "电话", tfPhone, "用户名", tfUsername};
        if (JOptionPane.showConfirmDialog(this, msg, "修改医生", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Doctor d = new Doctor();
            d.setDoctorID(id);
            d.setName(tfName.getText());
            d.setDeptname(tfDeptName.getText());
            d.setPhone(tfPhone.getText());
            d.setUsername(tfUsername.getText());
            adminService.updateDoctor(d);
            loadDoctors();
        }
    }

    private void deleteDoctor() {
        int row = tableDoctors.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "请选择医生");
            return;
        }
        int id = (int) tableDoctors.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "确认删除吗？", "删除", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            adminService.deleteDoctor(id);
            loadDoctors();
        }
    }

    private void searchDoctor() {
        String keyword = JOptionPane.showInputDialog(this, "请输入医生姓名：");
        if (keyword == null || keyword.isEmpty()) return;
        modelDoctors.setRowCount(0);
        List<Doctor> list = adminService.getDoctors(keyword);
        for (Doctor d : list)
            modelDoctors.addRow(new Object[]{d.getDoctorID(), d.getName(), d.getDeptId(), d.getPhone()});
    }

    // ==== 病人面板 ====
    private void initPatientPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        modelPatients = new DefaultTableModel(new String[]{"ID", "姓名", "性别", "年龄", "身份证", "电话"}, 0);
        tablePatients = new JTable(modelPatients);
        panel.add(new JScrollPane(tablePatients), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("新增"), btnUpdate = new JButton("修改"), btnDelete = new JButton("删除"),
                btnSearch = new JButton("查询"), btnRefresh = new JButton("刷新");
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnSearch);
        btnPanel.add(btnRefresh);
        panel.add(btnPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addPatientDialog());
        btnUpdate.addActionListener(e -> updatePatientDialog());
        btnDelete.addActionListener(e -> deletePatient());
        btnSearch.addActionListener(e -> searchPatient());
        btnRefresh.addActionListener(e -> loadPatients());

        tabbedPane.addTab("病人管理", panel);
        loadPatients();
    }

    private void loadPatients() {
        modelPatients.setRowCount(0);
        List<Patient> list = adminService.getPatients(null);
        for (Patient p : list)
            modelPatients.addRow(new Object[]{
                    p.getPatientId(), p.getName(), p.getGender(), p.getAge(), p.getIdentity(), p.getPhone()
            });
    }

    private void addPatientDialog() {
        JTextField tfName = new JTextField();
        JTextField tfGender = new JTextField();
        JTextField tfAge = new JTextField();
        JTextField tfIdentity = new JTextField();
        JTextField tfPhone = new JTextField();
        Object[] msg = {"姓名", tfName, "性别", tfGender, "年龄", tfAge, "身份证", tfIdentity, "电话", tfPhone};
        if (JOptionPane.showConfirmDialog(this, msg, "新增病人", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Patient p = new Patient();
            p.setName(tfName.getText());
            p.setGender(tfGender.getText());
            p.setAge(Integer.parseInt(tfAge.getText()));
            p.setIdentity(tfIdentity.getText());
            p.setPhone(tfPhone.getText());
            adminService.addPatient(p);
            loadPatients();
        }
    }

    private void updatePatientDialog() {
        int row = tablePatients.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "请选择病人");
            return;
        }
        int id = (int) tablePatients.getValueAt(row, 0);
        JTextField tfName = new JTextField((String) tablePatients.getValueAt(row, 1));
        JTextField tfGender = new JTextField((String) tablePatients.getValueAt(row, 2));
        JTextField tfAge = new JTextField(tablePatients.getValueAt(row, 3).toString());
        JTextField tfIdentity = new JTextField((String) tablePatients.getValueAt(row, 4));
        JTextField tfPhone = new JTextField((String) tablePatients.getValueAt(row, 5));
        Object[] msg = {"姓名", tfName, "性别", tfGender, "年龄", tfAge, "身份证", tfIdentity, "电话", tfPhone};
        if (JOptionPane.showConfirmDialog(this, msg, "修改病人", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Patient p = new Patient();
            p.setPatientId(id);
            p.setName(tfName.getText());
            p.setGender(tfGender.getText());
            p.setAge(Integer.parseInt(tfAge.getText()));
            p.setIdentity(tfIdentity.getText());
            p.setPhone(tfPhone.getText());
            adminService.updatePatient(p);
            loadPatients();
        }
    }

    private void deletePatient() {
        int row = tablePatients.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "请选择病人");
            return;
        }
        int id = (int) tablePatients.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "确认删除吗？", "删除", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            adminService.deletePatient(id);
            loadPatients();
        }
    }

    private void searchPatient() {
        String keyword = JOptionPane.showInputDialog(this, "请输入病人姓名：");
        if (keyword == null || keyword.isEmpty()) return;
        modelPatients.setRowCount(0);
        List<Patient> list = adminService.getPatients(keyword);
        for (Patient p : list)
            modelPatients.addRow(new Object[]{
                    p.getPatientId(), p.getName(), p.getGender(), p.getAge(), p.getIdentity(), p.getPhone()
            });
    }

    // ==== 挂号面板 ====
    private void initAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        modelAppointments = new DefaultTableModel(new String[]{"ID", "病人", "科室", "医生", "状态"}, 0);
        tableAppointments = new JTable(modelAppointments);
        panel.add(new JScrollPane(tableAppointments), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("新增");
        JButton btnUpdate = new JButton("修改");
        JButton btnDelete = new JButton("删除");
        JButton btnSearch = new JButton("查询");
        JButton btnRefresh = new JButton("刷新");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnSearch);
        btnPanel.add(btnRefresh);

        panel.add(btnPanel, BorderLayout.SOUTH);

        // ===== 绑定事件 =====
        btnAdd.addActionListener(e -> addAppointmentDialog());
        btnUpdate.addActionListener(e -> updateAppointmentDialog());
        btnDelete.addActionListener(e -> deleteAppointment());
        btnRefresh.addActionListener(e -> loadAppointments(null));
        btnSearch.addActionListener(e -> searchAppointmentDialog());

        tabbedPane.addTab("挂号管理", panel);

        // 默认加载所有挂号
        loadAppointments(null);
    }

    // ==================== 搜索挂号 ====================
    private void searchAppointmentDialog() {
        String keyword = JOptionPane.showInputDialog(this, "请输入病人名或医生名关键字：");
        if (keyword != null) {
            loadAppointments(keyword);
        }
    }

    // ==================== 加载挂号记录 ====================
    private void loadAppointments(String keyword) {
        modelAppointments.setRowCount(0);
        List<Appointment> list = adminService.getAppointments(keyword);
        for (Appointment a : list) {
            modelAppointments.addRow(new Object[]{
                    a.getAppointId(),
                    a.getPatient_name(),
                    a.getDeptName(),
                    a.getDoctorName(),
                    a.getStatus()
            });
        }
    }


    // ==================== 新增挂号 ====================
    private void addAppointmentDialog() {
        List<Patient> patients = adminService.getPatients(null);
        List<Doctor> doctors = adminService.getDoctors(null);

        JComboBox<String> cbPatient = new JComboBox<>();
        for (Patient p : patients) cbPatient.addItem(p.getName());

        JComboBox<String> cbDoctor = new JComboBox<>();
        for (Doctor d : doctors) cbDoctor.addItem(d.getName());

        JTextField tfStatus = new JTextField("待就诊");

        Object[] message = {
                "选择病人", cbPatient,
                "选择医生", cbDoctor,
                "状态", tfStatus
        };

        int option = JOptionPane.showConfirmDialog(this, message, "新增挂号", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String patientName = (String) cbPatient.getSelectedItem();
            String doctorName = (String) cbDoctor.getSelectedItem();

            Patient patient = patients.stream().filter(p -> p.getName().equals(patientName)).findFirst().orElse(null);
            Doctor doctor = doctors.stream().filter(d -> d.getName().equals(doctorName)).findFirst().orElse(null);

            if (patient == null || doctor == null) {
                JOptionPane.showMessageDialog(this, "选择有误");
                return;
            }

            Appointment a = new Appointment();
            a.setPatientId(patient.getPatientId());
            a.setPatient_name(patient.getName());
            a.setDoctorId(doctor.getDoctorID());
            a.setDoctorName(doctor.getName());
            a.setDeptId(doctor.getDeptId());
            a.setDeptName(doctor.getDeptname());
            a.setStatus(tfStatus.getText());

            adminService.addAppointment(a);
            loadAppointments(null);
        }
    }

    // ==================== 修改挂号 ====================
    private void updateAppointmentDialog() {
        int row = tableAppointments.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "请选择要修改的挂号");
            return;
        }

        // 当前挂号显示信息
        int appointId = (int) tableAppointments.getValueAt(row, 0);
        String currentPatientName = (String) tableAppointments.getValueAt(row, 1);
        String currentDeptName = (String) tableAppointments.getValueAt(row, 2);
        String currentDoctorName = (String) tableAppointments.getValueAt(row, 3);

        String currentStatus = (String) tableAppointments.getValueAt(row, 4);

        // 获取所有病人和医生
        List<Patient> patients = adminService.getPatients(null);
        List<Doctor> doctors = adminService.getDoctors(null);

        // 病人下拉
        JComboBox<String> cbPatient = new JComboBox<>();
        for (Patient p : patients) cbPatient.addItem(p.getName());
        cbPatient.setSelectedItem(currentPatientName);

        // 医生下拉
        JComboBox<String> cbDoctor = new JComboBox<>();
        for (Doctor d : doctors) cbDoctor.addItem(d.getName());
        cbDoctor.setSelectedItem(currentDoctorName);

        // 状态输入框
        JTextField tfStatus = new JTextField(currentStatus);

        Object[] message = {
                "病人", cbPatient,
                "医生", cbDoctor,
                "状态", tfStatus
        };

        int option = JOptionPane.showConfirmDialog(this, message, "修改挂号", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String patientName = (String) cbPatient.getSelectedItem();
            String doctorName = (String) cbDoctor.getSelectedItem();

            // 根据名字找到对应对象
            Patient patient = patients.stream().filter(p -> p.getName().equals(patientName)).findFirst().orElse(null);
            Doctor doctor = doctors.stream().filter(d -> d.getName().equals(doctorName)).findFirst().orElse(null);

            if (patient == null || doctor == null) {
                JOptionPane.showMessageDialog(this, "选择有误");
                return;
            }

            // 构建完整 Appointment 对象
            Appointment a = new Appointment();
            a.setAppointId(appointId);
            a.setPatientId(patient.getPatientId());
            a.setPatient_name(patient.getName());
            a.setDoctorId(doctor.getDoctorID());
            a.setDoctorName(doctor.getName());
            a.setDeptId(doctor.getDeptId());   // 通过 doctor 找 deptId
            a.setDeptName(doctor.getDeptname());
            a.setStatus(tfStatus.getText());

            // 更新数据库
            adminService.updateAppointment(a);

            // 刷新表格
            loadAppointments(null);
        }
    }


    // ==================== 删除挂号 ====================
    private void deleteAppointment() {
        int row = tableAppointments.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的挂号");
            return;
        }

        int appointId = (int) tableAppointments.getValueAt(row, 0);

        int option = JOptionPane.showConfirmDialog(this, "确定删除该挂号？", "删除挂号", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            adminService.deleteAppointment(appointId);
            loadAppointments(null);
        }
    }


    private void initPrescriptionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        modelPrescriptions = new DefaultTableModel(
                new String[]{"ID", "挂号ID", "诊断", "医嘱内容", "科室", "医生"}, 0
        );
        tablePrescriptions = new JTable(modelPrescriptions);
        panel.add(new JScrollPane(tablePrescriptions), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("新增");
        JButton btnUpdate = new JButton("修改");
        JButton btnDelete = new JButton("删除");
        JButton btnRefresh = new JButton("刷新");
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);

        panel.add(btnPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("医嘱管理", panel);

        // ===== 新增医嘱 =====
        btnAdd.addActionListener(e -> {
            List<Appointment> appointments = adminService.getAppointments(null);
            if (appointments.isEmpty()) {
                JOptionPane.showMessageDialog(this, "暂无挂号记录");
                return;
            }

            JComboBox<String> cbAppointments = new JComboBox<>();
            for (Appointment a : appointments) {
                cbAppointments.addItem(a.getAppointId() + " - " + a.getPatient_name() +
                        " / " + a.getDeptName());
            }

            JTextField tfDiagnosis = new JTextField();
            JTextField tfContent = new JTextField();
            Object[] msg = {"挂号", cbAppointments, "诊断", tfDiagnosis, "医嘱内容", tfContent};

            if (JOptionPane.showConfirmDialog(this, msg,
                    "新增医嘱", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                int sel = cbAppointments.getSelectedIndex();
                Appointment appt = appointments.get(cbAppointments.getSelectedIndex());

                Prescription p = new Prescription();
                p.setAppointId(appt.getAppointId());

                // 必须把外键 ID 塞进去（来自 appointment）
                p.setDoctorId(appt.getDoctorId());
                p.setPatientId(appt.getPatientId());

                // 辅助显示字段
                p.setDoctorName(appt.getDoctorName());
                p.setPatientName(appt.getPatient_name());
                p.setDeptName(appt.getDeptName());

                p.setDiagnosis(tfDiagnosis.getText());
                p.setContent(tfContent.getText());

                adminService.addPrescription(p);
                loadPrescriptions();
            }
        });

        // ===== 修改医嘱 =====
        btnUpdate.addActionListener(e -> {
            int row = tablePrescriptions.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "请选择要修改的医嘱");
                return;
            }

            int presId = (int) tablePrescriptions.getValueAt(row, 0);

            JTextField tfDiagnosis =
                    new JTextField((String) tablePrescriptions.getValueAt(row, 2));
            JTextField tfContent =
                    new JTextField((String) tablePrescriptions.getValueAt(row, 3));

            Object[] msg = {"诊断", tfDiagnosis, "医嘱内容", tfContent};

            if (JOptionPane.showConfirmDialog(this, msg,
                    "修改医嘱", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                Prescription p = new Prescription();
                p.setPresId(presId);
                p.setDiagnosis(tfDiagnosis.getText());
                p.setContent(tfContent.getText());

                adminService.updatePrescription(p);
                loadPrescriptions();
            }
        });

        // ===== 删除 =====
        btnDelete.addActionListener(e -> {
            int row = tablePrescriptions.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "请选择医嘱");
                return;
            }

            int presId = (int) tablePrescriptions.getValueAt(row, 0);
            if (JOptionPane.showConfirmDialog(this, "确定删除？",
                    "删除", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                adminService.deletePrescription(presId);
                loadPrescriptions();
            }
        });

        // ===== 刷新 =====
        btnRefresh.addActionListener(e -> loadPrescriptions());

        loadPrescriptions();
    }

    // ========== 加载医嘱 ==========
    private void loadPrescriptions() {
        modelPrescriptions.setRowCount(0);
        List<Prescription> list = adminService.getAllPrescriptions();
        for (Prescription p : list) {
            modelPrescriptions.addRow(new Object[]{
                    p.getPresId(),
                    p.getAppointId(),
                    p.getDiagnosis(),
                    p.getContent(),
                    p.getDeptName(),
                    p.getDoctorName()
            });
        }
    }

}


