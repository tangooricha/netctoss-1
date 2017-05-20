package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Cost;
import util.DBUtil;

public class CostDao implements Serializable {
	private static final long serialVersionUID = 3067082910327068077L;

	/**
	 * 查询所有套餐信息
	 * 
	 * @return 从数据库中读取到的所有资费项目
	 */
	public List<Cost> findAll() {
		List<Cost> list = new ArrayList<Cost>();
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT * FROM COST ORDER BY COST_ID";
			Statement smt = conn.createStatement();
			ResultSet rs = smt.executeQuery(sql);
			while (rs.next()) {
				Cost c = new Cost();
				c.setCostId(rs.getInt("cost_id"));
				c.setName(rs.getString("name"));
				c.setBaseDuration(rs.getInt("base_duration"));
				c.setBaseCost(rs.getDouble("base_cost"));
				c.setUnitCost(rs.getDouble("unit_cost"));
				c.setStatus(rs.getString("status"));
				c.setDescr(rs.getString("descr"));
				c.setCreatime(rs.getTimestamp("creatime"));
				c.setStartime(rs.getTimestamp("startime"));
				c.setCostType(rs.getString("cost_type"));
				list.add(c);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询资费失败", e);
		} finally {
			DBUtil.close(conn);
		}
	}

	/**
	 * 保存一条资费项目,该项目从网页中填写
	 * 
	 * @param cost
	 */
	public void save(Cost cost) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "INSERT INTO COST VALUES(COST_SEQ.NEXTVAL,?,?,?,?,1,?,SYSDATE,NULL,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, cost.getName());
			ps.setInt(2, cost.getBaseDuration());
			ps.setDouble(3, cost.getBaseCost());
			ps.setDouble(4, cost.getUnitCost());
			ps.setString(5, cost.getDescr());
			ps.setString(6, cost.getCostType());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("保存资费项目失败", e);
		} finally {
			DBUtil.close(conn);
		}
	}
	
	/**
	 * 根据costId从数据库删除一条记录
	 * 
	 * @param id
	 */
	public void delete(Integer id) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "DELETE FROM COST WHERE COST_ID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("删除资费失败", e);
		} finally {
			DBUtil.close(conn);
		}
	}
	
	public void modifyStatus(Integer id,Integer val){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "UPDATE COST SET STATUS = ?, STARTIME = SYSDATE WHERE COST_ID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, val);
			ps.setInt(2, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("开通资费失败", e);
		} finally {
			DBUtil.close(conn);
		}
	}
	
	
	
	
	

	public static void main(String[] args) {
		CostDao dao = new CostDao();
		List<Cost> cost = dao.findAll();
		for (Cost c : cost) {
			System.out.println(c.getCostId() + "," + c.getName());
		}
	}
	
}
