package vn.iotstart.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Sửa lại: Dùng import đúng
import vn.iotstart.connection.DBConnectionMySQL; 
import vn.iotstart.dao.UserDao;
import vn.iotstart.model.User;

public class UserDaoImpl implements UserDao {
	// Không nên khai báo conn, ps, rs ở đây, sẽ gây lỗi khi nhiều người dùng
	
	@Override
	public User get(String username) {
		// Sửa lại câu SQL: Bỏ dấu [] và dùng đúng tên bảng "Users"
		// Giả sử tên bảng của bạn là "Users"
		String sql = "SELECT * FROM Users WHERE user_name = ?";
		
		// Khai báo trong try-with-resources để tự động đóng
		try (Connection conn = DBConnectionMySQL.getConnection(); // Sửa lại cách gọi
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setString(1, username);
			
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) { // Sửa lại: Dùng "if" thay vì "while" vì username là duy nhất
					User user = new User();
					
					// Sửa lại tên cột theo chuẩn snake_case
					user.setId(rs.getInt("id"));
					user.setEmail(rs.getString("email"));
					user.setUserName(rs.getString("user_name"));
					user.setFullName(rs.getString("full_name"));
					user.setPassWord(rs.getString("password"));
					user.setAvatar(rs.getString("avatar"));
					user.setRoleid(rs.getInt("role_id")); // Sửa lại: Dùng getInt
					user.setPhone(rs.getString("phone"));
					user.setCreatedDate(rs.getDate("created_date")); // Sửa lại tên cột
					
					return user;
				}
			}
		} catch (Exception e) {
			e.printStackTrace(); // Lỗi sẽ được in ra console
		}
		
		return null; // Trả về null nếu có lỗi hoặc không tìm thấy
	}

	public UserDaoImpl() {
		super();
	}
}