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
		// 获取访问路径
		String path = req.getServletPath();
		// 根据规范处理路径:/findCost.do
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
			throw new RuntimeException("没有这个页面");
		}
	}

	// 查询资费
	protected void findCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html;charset=utf-8");
		// 获取当前页面的页码
		String s = req.getParameter("pageNum");
		int curr = (s == null || s.length() == 0) ? 1 : new Integer(s);

		CostDao dao = new CostDao();
		// 获取总的资费数量
		int totalCost = dao.totalCost();
		// 计算最大页码数
		int max = (int) Math.ceil(totalCost / 10.0);
		// 对当前页码进行判断
		if (curr < 1) {
			curr = 1;
		} else if (curr > max) {
			curr = max;
		}
		// 查询当前页面的资费
		List<Cost> list = dao.findPageCost(curr);

		// 将请求转发到jsp
		req.setAttribute("costPage", curr);
		req.setAttribute("totalCost", totalCost);
		req.setAttribute("costs", list);
		// 当前:/netctoss/findCost.do
		// 目标:/netctoss/WEB-INF/cost/find.jsp
		req.getRequestDispatcher("WEB-INF/cost/find.jsp").forward(req, res);
	}

	// 跳转到添加资费页面
	protected void toAddCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/cost/add.jsp").forward(req, res);
	}

	// 添加
	protected void addCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// 获取参数
		// 请求方式为为post,要设置编码
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
		// 设置cost
		Cost cost = new Cost();
		cost.setName(name);
		cost.setBaseDuration(baseDuration);
		cost.setBaseCost(baseCost);
		cost.setUnitCost(unitCost);
		cost.setDescr(descr);
		cost.setCostType(costType);
		// 保存cost
		CostDao dao = new CostDao();
		if (dao.save(cost)) {
			req.setAttribute("addSuccess", true);
		} else {
			req.setAttribute("addSuccess", false);
		}
		// 重定向到查询页面
		req.getRequestDispatcher("WEB-INF/cost/add.jsp").forward(req, res);
	}

	// 开通
	protected void openCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Integer id = new Integer(req.getParameter("costId"));
		new CostDao().modifyStatus(id, 0);
		// 重定向到查询页面
		res.sendRedirect("findCost.do");
	}

	// 删除
	protected void deleteCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Integer id = new Integer(req.getParameter("costId"));
		new CostDao().delete(id);
		// 重定向到查询页面
		res.sendRedirect("findCost.do");
	}

	// 跳转到显示页面,显示某一条资费的详细信息
	protected void detailCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Integer id = new Integer(req.getParameter("costId"));
		Cost cost = new CostDao().findCostById(id);
		req.setAttribute("cost", cost);
		req.getRequestDispatcher("WEB-INF/cost/detail.jsp").forward(req, res);
	}

	// 跳转到修改页面
	protected void toModifyCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Integer id = new Integer(req.getParameter("costId"));
		Cost cost = new CostDao().findCostById(id);
		req.setAttribute("cost", cost);
		req.getRequestDispatcher("WEB-INF/cost/modify.jsp").forward(req, res);
	}

	// 保存修改信息
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
			cost.setBaseDuration(baseDuration.length() == 0 ? 0 : new Integer(baseDuration));
			cost.setBaseCost(baseCost.length() == 0 ? 0 : new Double(baseCost));
			cost.setUnitCost(unitCost.length() == 0 ? 0 : new Double(unitCost));
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
