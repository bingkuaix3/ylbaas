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
	private WxMpService wxMpService;

	public WxMpService getWxMpService() {
		return wxMpService;
	}

	public void setWxMpService(WxMpService wxMpService) {
		this.wxMpService = wxMpService;
	}

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
		JSONObject params = JSONObject.parseObject("params");
		String picture_media_id = params.getString("picture_media_id");
		File picturefile = wxMpService.mediaDownload(picture_media_id);
		String newpath = "E:/WeX5_V3.2.1/apache-tomcat/webapps/img/";
		File fnewpath = new File(newpath);
		if (!fnewpath.exists()) {
			fnewpath.mkdirs();
		}
		File newfile = new File(newpath + picture_media_id + ".jpg");
		picturefile.renameTo(newfile);
	}

	private void sendmessage(ServletRequest request, ServletResponse response) throws WxErrorException {
		// TODO 自动生成的方法存根
		JSONObject params = JSONObject.parseObject("params");
		String userid = params.getString("userid");
		String doctors_analysis = params.getString("doctors_analysis");
		String doctors_recommend = params.getString("doctors_recommend");
		String price = params.getString("price");
		WxMpCustomMessage.WxArticle article = new WxMpCustomMessage.WxArticle();
		article.setUrl("www.baidu.com");
		article.setPicUrl("http://bingkuaix3.imwork.net/a/1.jpg");
		article.setDescription(doctors_recommend+"共计："+price);
		article.setTitle(doctors_analysis);

		WxMpCustomMessage message = WxMpCustomMessage.NEWS().toUser(userid).addArticle(article).build();
		wxMpService.customMessageSend(message);
	}
}
