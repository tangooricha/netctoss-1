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
			throw new RuntimeException("没有这个页面");
		}
	}

	// 查询资费
	protected void findCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html;charset=utf-8");
		// 获取当前页面的页码
		String s = req.getParameter("pageNum");
		int curr = (s == null || s.length() == 0) ? 1 : new Integer(s);
		// 获取排序信息
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
		// 获取总的资费数量
		int totalCost = dao.totalCost();
		// 计算最大页码数
		int max = (int) Math.ceil(totalCost / 10.0);
		// 页面中显示的开始页和结束页,最多出现5个页码
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
		// 查询当前页面的资费
		List<Cost> list = dao.findOrderPageCost(orderName, orderType, curr);

		// 将请求转发到jsp
		req.setAttribute("currPage", curr);
		req.setAttribute("maxPage", max);
		req.setAttribute("startPage", start);
		req.setAttribute("endPage", end);
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
		CostDao dao = new CostDao();
		// 获取参数
		// 请求方式为post,要设置编码
		req.setCharacterEncoding("utf-8");
		String name = req.getParameter("name").trim();

		// 如果表中已存在此名字,则不用保存,直接返回,并将addSuccess设置为false
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
		// 设置cost
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
		// 保存cost
		dao.save(cost);
		// 重定向到查询页面
		res.sendRedirect("findCost.do");
	}

	// 判断一个字符串是否为空
	private boolean isNone(String s) {
		if (s == null || s.length() == 0)
			return true;
		return false;
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

	// 跳转到登录页面
	protected void toLogin(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/main/login.jsp").forward(req, res);
	}

	// 判断登录逻辑:账号,密码,验证码
	protected void login(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// 接收参数
		req.setCharacterEncoding("utf-8");
		String adminCode = req.getParameter("adminCode");
		String password = req.getParameter("password");
		String code = req.getParameter("code");

		HttpSession session = req.getSession();
		String imgCode = (String) session.getAttribute("imgCode");
		if (isNone(code) || !code.equalsIgnoreCase(imgCode)) {
			// 验证码错误
			req.setAttribute("error", "验证码错误");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
			return;
		}

		// 验证账号密码
		AdminDao dao = new AdminDao();
		Admin a = dao.findByCode(adminCode);
		if (a == null) {
			// 账号错误
			req.setAttribute("error", "账号错误");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
		} else if (!a.getPassword().equals(password)) {
			// 密码错误
			req.setAttribute("error", "密码错误");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
		} else {
			// 将账号存入cookie
			Cookie c = new Cookie("adminCode", adminCode);
			res.addCookie(c);
			// 将账号存入session
			session.setAttribute("adminCode", adminCode);
			// 验证通过,重定向到主页
			res.sendRedirect("toIndex.do");
		}

	}

	// 跳转到主页
	protected void toIndex(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/main/index.jsp").forward(req, res);
	}

	// 跳转到修改密码页面
	protected void toModifyPwd(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/user/modifyPwd.jsp").forward(req, res);
	}

	// 处理修改密码业务
	protected void modifyPwd(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String adminCode = "";
		Cookie[] cookies = req.getCookies();
		for (Cookie c : cookies) {
			if ("adminCode".equals(c.getName())) {
				adminCode = c.getValue();
				break;
			}
		}

		// 如果adminCode为空,则跳转到登录页面
		if (isNone(adminCode)) {
			res.sendRedirect("login.do");
			return;
		}

		String oldPwd = req.getParameter("oldPwd");
		String newPwd = req.getParameter("newPwd");
		String repeatPwd = req.getParameter("repeatPwd");
		// 修改结果,返回给浏览器作判断,
		// -1:查无此人; -2:旧密码错误; -3:两次输入密码不一致
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

		// 利用ajax,传递数据到浏览器页面
		PrintWriter pw = res.getWriter();
		pw.print(result);
	}

	// 登出
	protected void logOut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// 使session无效,即账号退出
		HttpSession session = req.getSession();
		session.invalidate();
		res.sendRedirect(req.getContextPath() + "/toLogin.do");
	}

	// 给浏览器的登录页面传验证码
	protected void createImg(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// 获取验证码和图片
		Object[] objs = ImageUtil.createImage();

		// 将验证码存入session
		HttpSession session = req.getSession();
		session.setAttribute("imgCode", objs[0]);

		// 将图片发送给浏览器
		res.setContentType("image/png");
		// 获取字节输出流,该流由服务器创建,输出的目标就是当前访问的那个浏览器
		// PrintWriter 是用于输出字符串的流,
		// 如果想要输出图片或其他非字符串的数据,不能用PrintWriter,只能用OutputStream.
		OutputStream os = res.getOutputStream();
		BufferedImage img = (BufferedImage) objs[1];
		ImageIO.write(img, "png", os);
		os.close();
	}
}
