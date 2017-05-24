package web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		// �м���������Ҫ����,�轫�����ų�
		String[] paths = new String[] { "/toLogin.do", "/login.do", "/createImg.do" };
		String path = req.getServletPath();
		System.out.println(path);
		for (String p : paths) {
			if (p.equals(path)) {
				chain.doFilter(req, res);
				return;
			}
		}
		// ��session�л�ȡ�˺�
		HttpSession session = req.getSession();
		Object adminCode = session.getAttribute("adminCode");
		// �ж��û��Ƿ��¼
		if (adminCode == null) {
			// û��¼,�ض��򵽵�¼ҳ��
			res.sendRedirect(req.getContextPath() + "/toLogin.do");
		} else {
			// �ѵ�¼,����,�������ִ��
			chain.doFilter(req, res);
		}
	}

	public void init(FilterConfig cfg) throws ServletException {

	}

}
