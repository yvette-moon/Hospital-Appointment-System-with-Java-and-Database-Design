package newhostital.GUI;

import newhostital.DAO.AdminDAO;
import newhostital.model.Admin;
import newhostital.model.Doctor;
import newhostital.model.Patient;
import newhostital.service.Doctorservice;
import newhostital.service.Login;
import newhostital.service.PatientService;
import javax.swing.*;
import java.awt.*;

public class RegisterGUI  extends JFrame {

    private JTextField tfUsername, tfPassword, tfName, tfGender, tfAge, tfIdentity, tfPhone,tfdept;
    private JComboBox<String> cbRole;
    private JButton btnRegister;

    public RegisterGUI() {
        setTitle("用户注册");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(10, 2, 5, 5));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new JLabel("用户名:")); tfUsername = new JTextField(); add(tfUsername);
        add(new JLabel("姓名:")); tfName = new JTextField(); add(tfName);
        add(new JLabel("密码"));tfPassword = new JTextField();add(tfPassword);
        add(new JLabel("角色"));cbRole = new JComboBox<>(new String[]{"admin", "doctor", "patient"});add(cbRole);
        add(new JLabel("科室(医生)"));tfdept=new JTextField(); add(tfdept);
        add(new JLabel("性别:"));tfGender = new JTextField();add(tfGender);
        add(new JLabel("年龄:"));tfAge = new JTextField();add(tfAge);
        add(new JLabel("身份证:"));tfIdentity = new JTextField();add(tfIdentity);
        add(new JLabel("电话:"));tfPhone = new JTextField();add(tfPhone);

        btnRegister = new JButton("注册");add(btnRegister);

        btnRegister.addActionListener(e-> doRegister());
        setVisible(true);
    }
    private void doRegister(){
        String username = tfUsername.getText();
        String password = tfPassword.getText();
        String role = (String) cbRole.getSelectedItem();



        Login login = new Login();
        boolean ex=login.exists(username);
        if (ex) {
            JOptionPane.showMessageDialog(this, "用户名重复！请更换一个用户名");
            return;  // 直接结束，不再继续插入
        }
        boolean success =Login.register(username,password,role);
        if (!success) {
            JOptionPane.showMessageDialog(this, "注册失败，请稍后再试");
            return;
        }
        if(role.equals("patient")){
            PatientService ps = new PatientService();
            Patient p = new Patient();
            p.setName(tfName.getText());
            p.setGender(tfGender.getText());
            p.setAge(Integer.parseInt(tfAge.getText()));
            p.setIdentity(tfIdentity.getText());
            p.setPhone(tfPhone.getText());
            p.setUsername(tfUsername.getText());
            ps.savePatient(p);
        }
        if(role.equals("doctor")){
            Doctorservice ps = new Doctorservice();
            Doctor p = new Doctor();

            p.setName(tfName.getText());
            p.setPhone(tfPhone.getText());
            p.setUsername(tfUsername.getText());
            p.setDeptname(tfdept.getText());   // 只设置名字，不设置 id
            ps.saveDoctor(p);  // dept_id 自动由触发器处理
        }
        if(role.equals("admin")){
            AdminDAO ad = new AdminDAO();
            Admin p = new Admin();

            p.setName(tfName.getText());

            p.setUsername(tfUsername.getText());

            ad.save(p);  // dept_id 自动由触发器处理
        }
        JOptionPane.showMessageDialog(this,"注册成功！");
        this.dispose();

    }
}