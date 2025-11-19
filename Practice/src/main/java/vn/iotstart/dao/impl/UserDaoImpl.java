package vn.iotstart.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import vn.iotstart.connection.DBConnectionMySQL;
import vn.iotstart.dao.UserDao;
import vn.iotstart.model.User;

public class UserDaoImpl implements UserDao {
	public Connection conn = null;
	public PreparedStatement ps = null;
	public ResultSet rs = null;

	@Override
	public User get(String username) {
		String sql = "SELECT * FROM User WHERE username = ?";

		try {
			conn = new DBConnectionMySQL().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			rs = ps.executeQuery();

			if (rs.next()) {
				User user = new User();

				user.setUserId(rs.getInt("userId"));
				user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setFullname(rs.getString("fullname"));
				user.setPassword(rs.getString("password"));
				user.setImages(rs.getString("images"));
				user.setPhone(rs.getString("phone"));
				user.setRoleid(rs.getInt("roleid"));

				user.setStatus(rs.getInt("status"));
				user.setCode(rs.getString("code"));
				user.setSellerid(rs.getInt("sellerid"));

				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; // Không tìm thấy user
	}

	@Override
	public void insertregister(User user) {
		String sql = "INSERT INTO Users (email,username,fullname,password,status,roleid,code) Values (?,?,?,?,?,?,?)";
		try {
			conn = new DBConnectionMySQL().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getEmail());
			ps.setString(2, user.getUsername());
			ps.setString(3, user.getFullname());
			ps.setString(4, user.getPassword());
			ps.setInt(5, user.getStatus());
			ps.setInt(6, user.getRoleid());
			ps.setString(7, user.getCode());

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean checkExistEmail(String email) {
		boolean duplicate = false;
		String query = "select * from user where email = ?";
		try {
			conn = new DBConnectionMySQL().getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, email);
			rs = ps.executeQuery();
			if (rs.next()) {
				duplicate = true;
			}
			ps.close();
			conn.close();
		} catch (Exception ex) {
		}
		return duplicate;
	}

	@Override
	public User findOne(String username) {
		String sql = "SELECT * FROM User WHERE username = ?";
		User user = null;

		try {
			conn = new DBConnectionMySQL().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			rs = ps.executeQuery();

			if (rs.next()) {
				user = new User();
				// SỬA LẠI TÊN CỘT Ở ĐÂY: "id" -> "userId"
				user.setUserId(rs.getInt("userId"));

				user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setFullname(rs.getString("fullname"));
				user.setRoleid(rs.getInt("roleid"));
				user.setStatus(rs.getInt("status"));

				// Nên lấy thêm password để sau này có thể dùng cho việc đổi mật khẩu nếu cần
				user.setPassword(rs.getString("password"));
			}
			// Đừng quên đóng kết nối đúng cách để tránh rò rỉ bộ nhớ
			// (Khuyên dùng try-with-resources ở phiên bản sau, nhưng hiện tại cứ close thủ
			// công)
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Luôn đóng kết nối trong finally hoặc dùng try-catch-resources
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return user;
	}

	@Override
	public boolean checkExistUsername(String username) {
		boolean duplicate = false;
		String query = "select * from user where username = ?";
		try {
			conn = new DBConnectionMySQL().getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, username);
			rs = ps.executeQuery();
			if (rs.next()) {
				duplicate = true;
			}
			ps.close();
			conn.close();
		} catch (Exception ex) {
		}
		return duplicate;
	}

	@Override
	public void updatestatus(User user) {
		String sql = "UPDATE User SET status=?, code=? WHERE email = ?";
		try {
			conn = new DBConnectionMySQL().getConnection();
			ps = conn.prepareStatement(sql);

			ps.setInt(1, user.getStatus());
			ps.setString(2, user.getCode());
			ps.setString(3, user.getEmail());
			ps.executeUpdate();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}