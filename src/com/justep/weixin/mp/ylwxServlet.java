package com.justep.weixin.mp;

import java.io.File;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import com.alibaba.fastjson.JSONObject;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;

public class ylwxServlet extends HttpServlet {
	/**
	 * 
	 */
	WxMpServiceInstance instance = WxMpServiceInstance.getInstance();

	public void service(ServletRequest request, ServletResponse response) {
		String action = request.getParameter("action");
		if ("sendmessage".equals(action)) {
			try {
				sendmessage(request, response);
			} catch (WxErrorException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		} else if ("downloadmedia".equals(action)) {
			try {
				downloadmedia(request, response);
			} catch (WxErrorException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	private void downloadmedia(ServletRequest request, ServletResponse response) throws WxErrorException {
		JSONObject params = (JSONObject) JSONObject.parse(request.getParameter("params"));
		String picture_media_id = params.getString("picture_media_id");
		System.out.println(picture_media_id);
		instance.downloadimg(picture_media_id);
	}

	private void sendmessage(ServletRequest request, ServletResponse response) throws WxErrorException {
		// TODO 自动生成的方法存根
		JSONObject params = (JSONObject) JSONObject.parse(request.getParameter("params"));
		String userid = params.getString("userid");
		String doctors_analysis = params.getString("doctors_analysis");
		String doctors_recommend = params.getString("doctors_recommend");
		String price = params.getString("price");
		System.out.println(price);
		instance.customMessageSend(doctors_recommend,price,doctors_analysis,userid);
	}
}
