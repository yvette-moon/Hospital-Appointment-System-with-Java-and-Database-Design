package newhostital.DAO;

import newhostital.model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;




public class AdminDAO {
    public Admin getAdByUsername(String username){
        try(Connection conn=DBhelper.getConnection()){
            String sql="select * from admin where username=?";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1,username);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                Admin p=new Admin();
                p.setAdmin_id(rs.getInt("admin_id"));
                p.setName(rs.getString("name"));

                p.setUsername(rs.getString("username"));
                return p;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public static  void save(Admin p){
        try(Connection conn=DBhelper.getConnection()){

            String sqlInsert="INSERT INTO admin (name ,username) VALUES (?, ?)";
            PreparedStatement insertStmt=conn.prepareStatement(sqlInsert);
            insertStmt.setString(1,p.name());

            insertStmt.setString(2,p.getUsername());

            insertStmt.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();

        }
    }
}
