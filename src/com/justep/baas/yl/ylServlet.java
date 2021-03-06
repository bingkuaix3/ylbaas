package com.justep.baas.yl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.data.Table;
import com.justep.baas.data.Transform;
import com.justep.baas.data.Util;

public class ylServlet extends HttpServlet {
	public static final String DATABASE = "jdbc/film";
	public static final String TABLE_NAME = "orders";

	public void service(ServletRequest request, ServletResponse response) {
		String action = request.getParameter("action");
		if ("queryorder".equals(action)) {
			try {
				queryorder(request, response);
			} catch (SQLException | NamingException | IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		} else if ("saveorder".equals(action)) {

			try {
				saveorder(request, response);
			} catch (SQLException | NamingException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}

		}

	}

	private void saveorder(ServletRequest request, ServletResponse response) throws SQLException, NamingException {
		// TODO 自动生成的方法存根
		JSONObject params = (JSONObject) JSONObject.parse(request.getParameter("params"));
		JSONObject orderData = params.getJSONObject("orderdata");
		Connection conn = Util.getConnection(DATABASE);
		try {
			conn.setAutoCommit(false);
			if (orderData != null) {
				Table table = Transform.jsonToTable(orderData);
				Util.saveData(conn, table, TABLE_NAME);
			}
			conn.commit();
		} catch (SQLException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
			conn.rollback();
		} catch (ParseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} finally {
			conn.close();
		}

	}

	private void queryorder(ServletRequest request, ServletResponse response) throws SQLException, NamingException, IOException {
		// TODO 自动生成的方法存根
		JSONObject params = (JSONObject) JSONObject.parse(request.getParameter("params"));

		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		String orderid= params.getString("orderid");
		String sql = "select * from orders where order_status='未处理'";
		List<Object> sqlParams = new ArrayList<Object>();
		// 构造过滤条件
		List<String> filters = new ArrayList<String>();
		if(orderid==null){
		filters.add("order_status='未处理'");
		}else{
			filters.add("orderid = ?");
			sqlParams.add(orderid);
		}
		Table table = null;
		Connection conn = Util.getConnection(DATABASE);
		try {
			table = Util.queryData(conn, TABLE_NAME, null, filters, "order_status ASC", sqlParams, offset, limit);
			// table=Util.queryData(conn, sql, null, null, offset, limit);

		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} finally {
			conn.close();
		}
		Util.writeTableToResponse(response, table);
	}

}
