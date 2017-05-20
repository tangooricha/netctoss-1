package web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CostDao;
import entity.Cost;

public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 3682284444702921748L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// ��ȡ����·��
		String path = req.getServletPath();
		// ���ݹ淶����·��:/findCost.do
		if ("/findCost.do".equals(path)) {
			findCost(req, res);
		} else if ("/toAddCost.do".equals(path)) {
			toAddCost(req, res);
		} else if ("/addCost.do".equals(path)) {
			addCost(req, res);
		} else if ("/updateCost.do".equals(path)) {
			updateCost(req, res);
		} else {
			throw new RuntimeException("û�����ҳ��");
		}
	}

	// ��ѯ�ʷ�
	protected void findCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html;charset=utf-8");
		// ��ѯ���е��ʷ�
		CostDao dao = new CostDao();
		List<Cost> list = dao.findAll();
		// ������ת����jsp
		req.setAttribute("costs", list);
		// ��ǰ:/netctoss/findCost.do
		// Ŀ��:/netctoss/WEB-INF/cost/find.jsp
		req.getRequestDispatcher("WEB-INF/cost/find.jsp").forward(req, res);
	}

	protected void toAddCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/cost/add.jsp").forward(req, res);
	}

	protected void addCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// ��ȡ����
		// ����ʽΪΪpost,Ҫ���ñ���
		req.setCharacterEncoding("utf-8");
		String name = req.getParameter("name").trim();
		String bd = req.getParameter("baseDuration").trim();
		String bc = req.getParameter("baseCost").trim();
		String uc = req.getParameter("unitCost").trim();
		Integer baseDuration = bd.length() == 0 ? 0 : new Integer(bd);
		Double baseCost = bc.length() == 0 ? 0 : new Double(bc);
		Double unitCost = uc.length() == 0 ? 0 : new Double(uc);
		String descr = req.getParameter("descr").trim();
		String costType = req.getParameter("radFeeType").trim();
		// ����cost
		Cost cost = new Cost();
		cost.setName(name);
		cost.setBaseDuration(baseDuration);
		cost.setBaseCost(baseCost);
		cost.setUnitCost(unitCost);
		cost.setDescr(descr);
		cost.setCostType(costType);
		// ����cost
		CostDao dao = new CostDao();
		dao.save(cost);
		// �ض��򵽲�ѯҳ��
		res.sendRedirect("findCost.do");
	}

	protected void updateCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		Integer deleteId = new Integer(req.getParameter("deleteId"));
		Integer openId = new Integer(req.getParameter("openId"));
		Integer modifyId = new Integer(req.getParameter("modifyId"));
		CostDao dao = new CostDao();
		if (deleteId != -1) {
			dao.delete(deleteId);
		} else if (openId != -1) {
			
		} else if (modifyId != -1) {

		}
		// �ض��򵽲�ѯҳ��
		res.sendRedirect("findCost.do");
	}

}
