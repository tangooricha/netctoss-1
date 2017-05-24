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
		// 有几个请求不需要过滤,需将它们排除
		String[] paths = new String[] { "/toLogin.do", "/login.do", "/createImg.do" };
		String path = req.getServletPath();
		System.out.println(path);
		for (String p : paths) {
			if (p.equals(path)) {
				chain.doFilter(req, res);
				return;
			}
		}
		// 从session中获取账号
		HttpSession session = req.getSession();
		Object adminCode = session.getAttribute("adminCode");
		// 判断用户是否登录
		if (adminCode == null) {
			// 没登录,重定向到登录页面
			res.sendRedirect(req.getContextPath() + "/toLogin.do");
		} else {
			// 已登录,放行,请求继续执行
			chain.doFilter(req, res);
		}
	}

	public void init(FilterConfig cfg) throws ServletException {

	}

}
