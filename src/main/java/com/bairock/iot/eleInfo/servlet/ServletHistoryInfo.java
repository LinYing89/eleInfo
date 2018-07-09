package com.bairock.iot.eleInfo.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bairock.iot.eleInfo.HistoryInfo;
import com.bairock.iot.eleInfo.dao.HistoryInfoDao;

/**
 * Servlet implementation class HistoryInfo
 */
@WebServlet("/HistoryInfo")
public class ServletHistoryInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		List<HistoryInfo> histories = new HistoryInfoDao().findAll();
		Collections.reverse(histories);
		request.setAttribute("histories", histories);
		request.getRequestDispatcher("/historyInfo.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
