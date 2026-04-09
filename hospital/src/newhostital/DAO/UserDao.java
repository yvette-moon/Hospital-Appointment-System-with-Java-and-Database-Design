package newhostital.DAO;
import newhostital.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    //chazhao
    public User findByUsername(String username, String passwordHash,String role) {
        String sql = "select user_id,username,role from user where username=? and password=? And role=?";
        try (Connection conn = DBhelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ps.setString(3,role);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();

                    u.setUsername(rs.getString("username"));
                    u.setRole(rs.getString("role"));
                    u.setUserid(rs.getInt("user_id"));
                    u.setRole(rs.getString("role"));
                    //int rid = rs.getInt("related_id");

                    //if (rs.wasNull()) u.setRelatedId(null);
                    //else u.setRelatedId(rid);

                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //注册
        return null;
    }
    public static boolean register(String username,String password,String role){
        //先查询
        try (Connection conn = DBhelper.getConnection()){

        //再插入
        String insert="insert into user (username,password,role) values (?,?,?)";
            PreparedStatement insertStemt=conn.prepareStatement(insert);
            insertStemt.setString(1,username);
            insertStemt.setString(2,password);
            insertStemt.setString(3,role);
            insertStemt.executeUpdate();
            return true;

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }

    }
    public static boolean exists(String username) {
        String sql = "SELECT COUNT(*) FROM user WHERE username = ?";
        try (Connection conn = DBhelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<User> getUsers(String keyword){
        List<User> list = new ArrayList<>();
        try (Connection conn = DBhelper.getConnection()) {
            String sql = "SELECT * FROM user";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               User u= new User();
                u.setUserid(rs.getInt("user_id"));;
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static void update(User user){
        try (Connection conn = DBhelper.getConnection()) {
            String sql = "UPDATE user SET username=?,password=?,role=?WHERE user_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setInt(4, user.getUserid());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void delete(int id){
        try (Connection conn = DBhelper.getConnection()) {
            String sql = "DELETE FROM user WHERE user_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }

