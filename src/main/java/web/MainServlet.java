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
		} else if ("/openCost.do".equals(path)) {
			openCost(req, res);
		} else if ("/deleteCost.do".equals(path)) {
			deleteCost(req, res);
		} else if ("/detailCost.do".equals(path)) {
			detailCost(req, res);
		} else if ("/toModifyCost.do".equals(path)) {
			toModifyCost(req, res);
		} else if ("/saveModifyCost.do".equals(path)) {
			saveModifyCost(req, res);
		} else {
			throw new RuntimeException("û�����ҳ��");
		}
	}

	// ��ѯ�ʷ�
	protected void findCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html;charset=utf-8");
		// ��ȡ��ǰҳ���ҳ��
		String s = req.getParameter("pageNum");
		int curr = (s == null || s.length() == 0) ? 1 : new Integer(s);

		CostDao dao = new CostDao();
		// ��ȡ�ܵ��ʷ�����
		int totalCost = dao.totalCost();
		// �������ҳ����
		int max = (int) Math.ceil(totalCost / 10.0);
		// �Ե�ǰҳ������ж�
		if (curr < 1) {
			curr = 1;
		} else if (curr > max) {
			curr = max;
		}
		// ��ѯ��ǰҳ����ʷ�
		List<Cost> list = dao.findPageCost(curr);

		// ������ת����jsp
		req.setAttribute("costPage", curr);
		req.setAttribute("totalCost", totalCost);
		req.setAttribute("costs", list);
		// ��ǰ:/netctoss/findCost.do
		// Ŀ��:/netctoss/WEB-INF/cost/find.jsp
		req.getRequestDispatcher("WEB-INF/cost/find.jsp").forward(req, res);
	}

	// ��ת������ʷ�ҳ��
	protected void toAddCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/cost/add.jsp").forward(req, res);
	}

	// ���
	protected void addCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		CostDao dao = new CostDao();
		// ��ȡ����
		// ����ʽΪpost,Ҫ���ñ���
		req.setCharacterEncoding("utf-8");
		String name = req.getParameter("name").trim();

		// ��������Ѵ��ڴ�����,���ñ���,ֱ�ӷ���,����addSuccess����Ϊfalse
		if (dao.nameExist(name)) {
			req.setAttribute("addSuccess", false);
			req.getRequestDispatcher("WEB-INF/cost/add.jsp").forward(req, res);
			return;
		}

		String descr = req.getParameter("descr").trim();
		String costType = req.getParameter("radFeeType").trim();
		String baseDuration = req.getParameter("baseDuration").trim();
		String baseCost = req.getParameter("baseCost").trim();
		String unitCost = req.getParameter("unitCost").trim();
		// ����cost
		Cost cost = new Cost();
		cost.setName(name);
		cost.setDescr(descr);
		cost.setCostType(costType);
		if (!isNone(baseDuration))
			cost.setBaseDuration(new Integer(baseDuration));
		if (!isNone(baseCost))
			cost.setBaseCost(new Double(baseCost));
		if (!isNone(unitCost))
			cost.setUnitCost(new Double(unitCost));
		// ����cost
		dao.save(cost);
		// �ض��򵽲�ѯҳ��
		res.sendRedirect("findCost.do");
	}

	// �ж�һ���ַ����Ƿ�Ϊ��
	private boolean isNone(String s) {
		if (s == null || s.length() == 0)
			return true;
		return false;
	}

	// ��ͨ
	protected void openCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Integer id = new Integer(req.getParameter("costId"));
		new CostDao().modifyStatus(id, 0);
		// �ض��򵽲�ѯҳ��
		res.sendRedirect("findCost.do");
	}

	// ɾ��
	protected void deleteCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Integer id = new Integer(req.getParameter("costId"));
		new CostDao().delete(id);
		// �ض��򵽲�ѯҳ��
		res.sendRedirect("findCost.do");
	}

	// ��ת����ʾҳ��,��ʾĳһ���ʷѵ���ϸ��Ϣ
	protected void detailCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Integer id = new Integer(req.getParameter("costId"));
		Cost cost = new CostDao().findCostById(id);
		req.setAttribute("cost", cost);
		req.getRequestDispatcher("WEB-INF/cost/detail.jsp").forward(req, res);
	}

	// ��ת���޸�ҳ��
	protected void toModifyCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Integer id = new Integer(req.getParameter("costId"));
		Cost cost = new CostDao().findCostById(id);
		req.setAttribute("cost", cost);
		req.getRequestDispatcher("WEB-INF/cost/modify.jsp").forward(req, res);
	}

	// �����޸���Ϣ
	protected void saveModifyCost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		Cost cost = new Cost();
		try {
			req.setCharacterEncoding("utf-8");
			String costId = req.getParameter("costId").trim();
			String name = req.getParameter("name").trim();
			String costType = req.getParameter("radFeeType").trim();
			String baseDuration = req.getParameter("baseDuration").trim();
			String baseCost = req.getParameter("baseCost").trim();
			String unitCost = req.getParameter("unitCost").trim();
			String descr = req.getParameter("descr").trim();

			cost.setCostId(new Integer(costId));
			cost.setName(name);
			cost.setCostType(costType);
			if (!isNone(baseDuration))
				cost.setBaseDuration(new Integer(baseDuration));
			if (!isNone(baseCost))
				cost.setBaseCost(new Double(baseCost));
			if (!isNone(unitCost))
				cost.setUnitCost(new Double(unitCost));
			cost.setDescr(descr);
			new CostDao().modifyCost(cost);
			req.setAttribute("cost", cost);
			req.setAttribute("hasSaveModify", true);
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("hasSaveModify", false);
		} finally {
			req.getRequestDispatcher("WEB-INF/cost/modify.jsp").forward(req, res);
		}

	}

}
