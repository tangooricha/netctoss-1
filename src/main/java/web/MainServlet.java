package web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AdminDao;
import dao.CostDao;
import entity.Admin;
import entity.Cost;
import util.ImageUtil;

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
		} else if ("/toLogin.do".equals(path)) {
			toLogin(req, res);
		} else if ("/login.do".equals(path)) {
			login(req, res);
		} else if ("/toIndex.do".equals(path)) {
			toIndex(req, res);
		} else if ("/toModifyPwd.do".equals(path)) {
			toModifyPwd(req, res);
		} else if ("/modifyPwd.do".equals(path)) {
			modifyPwd(req, res);
		} else if ("/logOut.do".equals(path)) {
			logOut(req, res);
		} else if ("/createImg.do".equals(path)) {
			createImg(req, res);
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
		// ��ȡ������Ϣ
		String orderName = req.getParameter("orderName");
		String orderType = req.getParameter("orderType");
		if (isNone(orderName) && isNone(orderType)) {
			orderName = (String) req.getAttribute("orderName");
			orderType = (String) req.getAttribute("orderType");
			if (isNone(orderName) && isNone(orderType)) {
				orderName = "cost_id";
				orderType = "asc";
			}
		} else {
			req.setAttribute("orderName", orderName);
			req.setAttribute("orderType", orderType);
		}
		CostDao dao = new CostDao();
		// ��ȡ�ܵ��ʷ�����
		int totalCost = dao.totalCost();
		// �������ҳ����
		int max = (int) Math.ceil(totalCost / 10.0);
		// ҳ������ʾ�Ŀ�ʼҳ�ͽ���ҳ,������5��ҳ��
		int start = 0, end = 0;
		if (curr == 1 || curr == 2) {
			start = 1;
			end = 5;
		} else if (curr == max || curr == max - 1) {
			start = max - 4;
			end = max;
		} else {
			start = curr - 2;
			end = curr + 2;
		}
		for (int i = start; i <= end; i++) {
			if (i < 1) {
				start = 1;
				break;
			} else if (i > max) {
				end = max;
				break;
			}
		}
		// ��ѯ��ǰҳ����ʷ�
		List<Cost> list = dao.findOrderPageCost(orderName, orderType, curr);

		// ������ת����jsp
		req.setAttribute("currPage", curr);
		req.setAttribute("maxPage", max);
		req.setAttribute("startPage", start);
		req.setAttribute("endPage", end);
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

	// ��ת����¼ҳ��
	protected void toLogin(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/main/login.jsp").forward(req, res);
	}

	// �жϵ�¼�߼�:�˺�,����,��֤��
	protected void login(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// ���ղ���
		req.setCharacterEncoding("utf-8");
		String adminCode = req.getParameter("adminCode");
		String password = req.getParameter("password");
		String code = req.getParameter("code");

		HttpSession session = req.getSession();
		String imgCode = (String) session.getAttribute("imgCode");
		if (isNone(code) || !code.equalsIgnoreCase(imgCode)) {
			// ��֤�����
			req.setAttribute("error", "��֤�����");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
			return;
		}

		// ��֤�˺�����
		AdminDao dao = new AdminDao();
		Admin a = dao.findByCode(adminCode);
		if (a == null) {
			// �˺Ŵ���
			req.setAttribute("error", "�˺Ŵ���");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
		} else if (!a.getPassword().equals(password)) {
			// �������
			req.setAttribute("error", "�������");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
		} else {
			// ���˺Ŵ���cookie
			Cookie c = new Cookie("adminCode", adminCode);
			res.addCookie(c);
			// ���˺Ŵ���session
			session.setAttribute("adminCode", adminCode);
			// ��֤ͨ��,�ض�����ҳ
			res.sendRedirect("toIndex.do");
		}

	}

	// ��ת����ҳ
	protected void toIndex(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/main/index.jsp").forward(req, res);
	}

	// ��ת���޸�����ҳ��
	protected void toModifyPwd(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/user/modifyPwd.jsp").forward(req, res);
	}

	// �����޸�����ҵ��
	protected void modifyPwd(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String adminCode = "";
		Cookie[] cookies = req.getCookies();
		for (Cookie c : cookies) {
			if ("adminCode".equals(c.getName())) {
				adminCode = c.getValue();
				break;
			}
		}

		// ���adminCodeΪ��,����ת����¼ҳ��
		if (isNone(adminCode)) {
			res.sendRedirect("login.do");
			return;
		}

		String oldPwd = req.getParameter("oldPwd");
		String newPwd = req.getParameter("newPwd");
		String repeatPwd = req.getParameter("repeatPwd");
		// �޸Ľ��,���ظ���������ж�,
		// -1:���޴���; -2:���������; -3:�����������벻һ��
		int result = 0;

		AdminDao dao = new AdminDao();
		Admin admin = dao.findByCode(adminCode);
		if (admin == null) {
			result = -1;
		} else if (!admin.getPassword().equals(oldPwd)) {
			result = -2;
		} else if (!newPwd.equals(repeatPwd)) {
			result = -3;
		} else {
			result = 1;
			dao.modifyPwdByCode(adminCode, newPwd);
		}

		// req.setAttribute("modifyResult", result);
		// req.getRequestDispatcher("toModifyPwd.do").forward(req, res);

		// ����ajax,�������ݵ������ҳ��
		PrintWriter pw = res.getWriter();
		pw.print(result);
	}

	// �ǳ�
	protected void logOut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// ʹsession��Ч,���˺��˳�
		HttpSession session = req.getSession();
		session.invalidate();
		res.sendRedirect(req.getContextPath() + "/toLogin.do");
	}

	// ��������ĵ�¼ҳ�洫��֤��
	protected void createImg(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// ��ȡ��֤���ͼƬ
		Object[] objs = ImageUtil.createImage();

		// ����֤�����session
		HttpSession session = req.getSession();
		session.setAttribute("imgCode", objs[0]);

		// ��ͼƬ���͸������
		res.setContentType("image/png");
		// ��ȡ�ֽ������,�����ɷ���������,�����Ŀ����ǵ�ǰ���ʵ��Ǹ������
		// PrintWriter ����������ַ�������,
		// �����Ҫ���ͼƬ���������ַ���������,������PrintWriter,ֻ����OutputStream.
		OutputStream os = res.getOutputStream();
		BufferedImage img = (BufferedImage) objs[1];
		ImageIO.write(img, "png", os);
		os.close();
	}
}
