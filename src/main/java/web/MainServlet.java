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
		//������ת����jsp
		req.setAttribute("costs", list);
		//��ǰ:/netctoss/findCost.do
		//Ŀ��:/netctoss/WEB-INF/cost/find.jsp
		req.getRequestDispatcher("WEB-INF/cost/find.jsp").forward(req, res);
	}

}
