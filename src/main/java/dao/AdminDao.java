package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.Admin;
import util.DBUtil;

public class AdminDao implements Serializable {
	private static final long serialVersionUID = 221587074591276396L;
	
	//≤È’“admin
	public Admin findByCode(String adminCode) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT * FROM ADMIN_INFO WHERE ADMIN_CODE=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, adminCode);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Admin a = new Admin();
				a.setAdminId(rs.getInt("admin_id"));
				a.setAdminCode(rs.getString("admin_code"));
				a.setPassword(rs.getString("password"));
				a.setName(rs.getString("name"));
				a.setTelephone(rs.getString("telephone"));
				a.setEmail(rs.getString("email"));
				a.setEnrolldate(rs.getTimestamp("enrolldate"));
				return a;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("≤È—Øπ‹¿Ì‘± ß∞‹", e);
		} finally {
			DBUtil.close(conn);
		}
		return null;
	}
	
	//–ﬁ∏ƒadmin√‹¬Î
	public void modifyPwdByCode(String adminCode, String password){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "UPDATE ADMIN_INFO SET PASSWORD=? WHERE ADMIN_CODE=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, password);
			ps.setString(2, adminCode);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("–ﬁ∏ƒ√‹¬Î ß∞‹",e);
		} finally {
			DBUtil.close(conn);
		}
	}

	public static void main(String[] args) {
		AdminDao dao = new AdminDao();
		System.out.println(dao.findByCode("caocao"));
	}
	
}
