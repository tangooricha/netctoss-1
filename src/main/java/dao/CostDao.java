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
	 * ��ѯ�����ײ���Ϣ
	 * 
	 * @return �����ݿ��ж�ȡ���������ʷ���Ŀ
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
			throw new RuntimeException("��ѯ�ʷ�ʧ��", e);
		} finally {
			DBUtil.close(conn);
		}
	}

	/**
	 * ����cost_id��ѯ�ʷ���Ϣ������һ��Cost����
	 * 
	 * @param id
	 * @return
	 */
	public Cost findCostById(Integer id) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT * FROM COST WHERE COST_ID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			Cost c = new Cost();
			rs.next();
			c.setCostId(id);
			c.setName(rs.getString("name"));
			c.setBaseDuration(rs.getInt("base_duration"));
			c.setBaseCost(rs.getDouble("base_cost"));
			c.setUnitCost(rs.getDouble("unit_cost"));
			c.setStatus(rs.getString("status"));
			c.setDescr(rs.getString("descr"));
			c.setCreatime(rs.getTimestamp("creatime"));
			c.setStartime(rs.getTimestamp("startime"));
			c.setCostType(rs.getString("cost_type"));
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("��ȡ�ʷ�ʧ��", e);
		} finally {
			DBUtil.close(conn);
		}
	}

	/**
	 * ����һ���ʷ���Ŀ,����Ŀ����ҳ����д
	 * 
	 * ��<cost>�����Ѵ�����ͬ���ʷ�����,�򷵻�false,���򷵻�true
	 * 
	 * @param cost
	 */
	public void save(Cost cost) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			//String sql = "INSERT INTO COST VALUES(COST_SEQ.NEXTVAL,?,?,?,?,1,?,SYSDATE,NULL,?)";
			String sql = "INSERT INTO COST VALUES(COST_SEQ.NEXTVAL,?,?,?,?,'1',?,SYSDATE,NULL,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, cost.getName());
			//setInt,setDouble��������null,
			//��ʵ��ҵ���и��ֶ�ȴ����Ϊnull,�������ݿ�Ҳ֧��Ϊnull,
			//�������ֶο��Ե���Object����
			ps.setObject(2, cost.getBaseDuration());
			ps.setObject(3, cost.getBaseCost());
			ps.setObject(4, cost.getUnitCost());
			ps.setString(5, cost.getDescr());
			ps.setString(6, cost.getCostType());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("�����ʷ���Ŀʧ��", e);
		} finally {
			DBUtil.close(conn);
		}
	}

	public boolean nameExist(String name) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "UPDATE COST SET NAME=? WHERE NAME=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, name);
			int i = ps.executeUpdate();
			return i > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("��ѯ�ʷ�����ʧ��", e);
		} finally {
			DBUtil.close(conn);
		}
	}

	/**
	 * ����costId�����ݿ�ɾ��һ����¼
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
			throw new RuntimeException("ɾ���ʷ�ʧ��", e);
		} finally {
			DBUtil.close(conn);
		}
	}

	/** �޸��ʷѵ�״̬,���ڿ�ͨ�ʷ� */
	public void modifyStatus(Integer id, Integer val) {
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
			throw new RuntimeException("��ͨ�ʷ�ʧ��", e);
		} finally {
			DBUtil.close(conn);
		}
	}

	/** �޸��ʷ�,�����޸��ʷ���Ϣ */
	public void modifyCost(Cost c) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "UPDATE COST SET NAME=?, COST_TYPE=?, BASE_DURATION=?, BASE_COST=?, UNIT_COST=?, DESCR=? WHERE COST_ID=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, c.getName());
			ps.setString(2, c.getCostType());
			ps.setObject(3, c.getBaseDuration());
			ps.setObject(4, c.getBaseCost());
			ps.setObject(5, c.getUnitCost());
			ps.setString(6, c.getDescr());
			ps.setInt(7, c.getCostId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("�޸���Ϣʧ��", e);
		} finally {
			DBUtil.close(conn);
		}
	}

	/** ��ȡ�ܵ��ʷ���Ŀ�� */
	public int totalCost() {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT COUNT(*) FROM COST";
			Statement smt = conn.createStatement();
			ResultSet rs = smt.executeQuery(sql);
			rs.next();
			Integer num = rs.getInt(1);
			return num;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("��ȡ�ʷ�����ʧ��", e);
		} finally {
			DBUtil.close(conn);
		}
	}

	/** ��ȡÿҳ���ʷ���Ŀ,Ĭ��Ϊÿҳ10����¼ */
	public List<Cost> findPageCost(int page) {
		Connection conn = null;
		List<Cost> list = new ArrayList<Cost>();
		int start = (page - 1) * 10 + 1;
		int end = page * 10;
		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT * FROM (" + "SELECT ROWNUM RN, T.* FROM ("
					+ "SELECT * FROM COST ORDER BY COST_ID) T ) " + "WHERE RN BETWEEN ? AND ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();
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
			throw new RuntimeException("��ȡ�ʷ�����ʧ��", e);
		} finally {
			DBUtil.close(conn);
		}
	}

	public static void main(String[] args) {
		CostDao dao = new CostDao();
		/*
		 * List<Cost> cost = dao.findAll(); for (Cost c : cost) {
		 * System.out.println(c.getCostId() + "," + c.getName()); }
		 */
		List<Cost> cost = dao.findPageCost(3);
		for (Cost c : cost) {
			System.out.println(c.getCostId() + "," + c.getName());
		}

	}
}
