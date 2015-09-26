package br.com.k19.scouts.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/logar")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {


		req.getSession().setAttribute("logado", true);
		
		res.sendRedirect("/" + req.getContextPath());
	}
}
