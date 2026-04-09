package newhostital.GUI;
import newhostital.DAO.AdminDAO;
import newhostital.DAO.DoctorDao;
import newhostital.model.Admin;
import newhostital.model.Doctor;
import newhostital.model.Patient;
import newhostital.model.User;
import newhostital.service.Login;
import newhostital.service.PatientService;
import javax.swing.*;
import java.awt.*;
public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private Login userService = new Login();
    private JButton btnRegister;
    public LoginGUI() {
        setTitle("用户登录");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout( 10, 10));

        JLabel titleLabel = new JLabel("医院信息管理系统：登陆界面", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JLabel usernameLabel = new JLabel("用户名:", SwingConstants.LEFT);
        JLabel passwordLabel = new JLabel("密码:",SwingConstants.LEFT);
        JLabel roleLabel = new JLabel("角色:", SwingConstants.LEFT);

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        String[] roles = {"admin", "doctor", "patient"};
        roleBox = new JComboBox<>(roles);
        roleBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        centerPanel.add(usernameLabel);
        centerPanel.add(usernameField);
        centerPanel.add(passwordLabel);
        centerPanel.add(passwordField);
        centerPanel.add(roleLabel);
        centerPanel.add(roleBox);

        add(centerPanel, BorderLayout.CENTER);
        //底部
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));

        JButton loginButton = new JButton("登录");
        loginButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        btnRegister = new JButton("注册");
        btnRegister.setFont(new Font("微软雅黑", Font.PLAIN, 16));

        buttonPanel.add(loginButton);
        buttonPanel.add(btnRegister);

        add(buttonPanel, BorderLayout.SOUTH);


        // 登录事件
        loginButton.addActionListener(e -> doLogin());
        btnRegister.addActionListener(e -> openRegister());

        setVisible(true);
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入用户名和密码！");
            return;
        }

        User user = userService.login(username, password,role);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "用户名、密码或角色错误！", "登录失败", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "登录成功！欢迎 " + user.getUsername() + "（" + user.getRole() + "）");
            openRoleWindow(user);
            dispose();
        }
    }

    public void openRoleWindow(User user) {
        switch (user.getRole()) {
            case "admin":
                AdminDAO admin = new AdminDAO();
                Admin ad = admin.getAdByUsername(user.getUsername());
                new AdminGUI(ad).setVisible(true);
                break;
            case "doctor":
                DoctorDao dao = new DoctorDao();

                Doctor doc= dao.getDocByUsername(user.getUsername());
               new DoctorGUI(doc).setVisible(true);
                break;
            case "patient":
                Patient patient = PatientService.getPatient(user.getUsername());
                new PatientGUI(patient).setVisible(true);
                break;
        }
    }
    private void openRegister(){
        new RegisterGUI().setVisible(true);
    }

    public static void main(String[] args) {
       new LoginGUI().setVisible(true);
    }
}
