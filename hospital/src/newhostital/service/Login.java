package newhostital.service;
import newhostital.DAO.UserDao;
import newhostital.model.User;
import newhostital.util.EncryptUtil;

/**
 * LoginService: 根据 username/password 返回 role 和 relatedId
 */
public class Login{
    private static final UserDao dao = new UserDao();

    public User login(String username, String password,String role) {
        String hash = EncryptUtil.md5(password);
        return dao.findByUsername(username, hash,role);
    }
    public static boolean register(String username, String password, String role){
        String hash = EncryptUtil.md5(password);
       return dao.register(username, hash, role);
    }
    public  boolean exists(String username){
        return dao.exists(username);
    }
}