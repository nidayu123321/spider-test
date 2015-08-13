package com.test;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONObject;

import java.io.*;


public class Test {

	public static void main(String[] args) {
		// 1. 生成 HttpClinet对象并设置参数
		HttpClient httpClient = new HttpClient();
		//设置编码
		HttpClientParams h = new HttpClientParams();
		h.setHttpElementCharset("UTF-8");
		h.setContentCharset("UTF-8");
		httpClient.setParams(h);
		//设置代理
//		HostConfiguration hc = new HostConfiguration();
//		hc.setProxy("67.177.104.230", 58720);
//		httpClient.setHostConfiguration(hc);
		httpClient.getState().getCookies();

		String text = null;
		String url = null;
		String phoneNo = "18801320760";
		String password = "260736";
//		String phoneNo = "18516501903";
//		String password = "780623";

		PostMethod postMethod = new PostMethod("http://iservice.10010.com/e3/static/check/checklogin/");

		//测试湖南联通的查询余额
		url = "https://uac.10010.com/cust/secure/school/safety_school_sub?find=4001";
		getUrl(httpClient, url, null);
		url = String.format("%sportal/Service/MallLogin?userName=%s&password=%s&pwdType=01&productType=01&rememberMe=1&_=%d&redirectURL=www.10010.com", "https://uac.10010.com/", phoneNo, password, System.currentTimeMillis());
		text = getUrl(httpClient,postMethod, url, null);
		System.out.println(text);
		JSONObject json2 = new JSONObject(text);
		String resultCode = json2.getString("resultCode");
		if (resultCode.equals("0000")) {
//			url = "http://iservice.10010.com/e3/static/check/checklogin/";
			String[][] params = {{"_", System.currentTimeMillis() + "" }};
			String[][] headers = {{"Accept", "application/json, text/javascript, */*; q=0.01"},{"Host", "iservice.10010.com"}, {"Connection", "Keep-Alive"},
					{"Accept-Encoding", "gzip, deflate"},{"Accept-Language", "zh-cn"}, {"Cache-Control", "no-cache"},{"Referer", "http://iservice.10010.com/e3/query/account_balance.html?menuId=000100010002"}};
//					{"Cookie", "mallcity=31|310; gipgeo=31|310; _n3fa_cid=ba4c9dafd1d44ff3de97acb251d159a6; _n3fa_ext=ft=1434536150; _n3fa_lvt_a9e72dfe4a54a20c3d6e671b3bad01d9=1434536150; _n3fa_lpvt_a9e72dfe4a54a20c3d6e671b3bad01d9=1434536197; WT_FPC=id=28e2cad2b538c7a4c5b1434536153197:lv=1434536223759:ss=1434536153197; Hm_lvt_9208c8c641bfb0560ce7884c36938d9d=1434536153; Hm_lpvt_9208c8c641bfb0560ce7884c36938d9d=1434536201; __utma=231252639.412000185.1434536153.1434536153.1434536153.1; __utmb=231252639.8.10.1434536153; __utmc=231252639; __utmz=231252639.1434536153.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmv=231252639.Shanghai; ang_sessionid=982427026106189496; ang_seqid=7; ang_catchyou=1; piw=%7B%22login_name%22%3A%22185****3849%22%2C%22nickName%22%3A%22%E6%9D%8E%E6%80%A1%E5%B7%9E%22%2C%22rme%22%3A%7B%22ac%22%3A%22%22%2C%22at%22%3A%22%22%2C%22pt%22%3A%2201%22%2C%22u%22%3A%2218570913849%22%7D%2C%22verifyState%22%3A%22%22%7D; JUT=1hkRPc0TqBWPqpzWutNnwLXrAyUX87IwqqOGibV0QaGLr1nmeh/H3plleTHrTuP/yJEvPmlDc3a4kicClZZE8oRDGwUa30lbibJ84w7GNAs04lseWdVqO0GWr+vHDNPX/hZWUR0qGRGDIVCSQUW5LOOgePtAW0jQuHkjtO09Mi6Otjxlq/uG+tv9od+uwLOnU7T+g683dFxVugbUbPHlgWEdZfoSFoVDY4NLcAHCzu+WJfponVlwSV83LNRjQ83Oh0k0gSlr4vfuXvosPggAUQKQPMUgPybLgWuISvBSk9TA9o/E2c+HVdkJ8GMgWfr9xYSVPEViLnk5Ec97lrq2CODp1gepDExL3SzKtuZ7WZ/M5f030dQIYrIir9EWs4dycZ1zz/5q5BpRP9qN8EMcMu+HAOeJCLNgeJzcv5032ZAoj/lNRogkS0GHNH8z4LtP5AN8nfHy0oCg/R+EmCya3+GsitYatvGNt8ys0DTf8TxfuW/EmIlhUsJAPC3VuhUL30DWPSNAkkA=8jy5rWORkKZTngvaxyAWLw==; _uop_id=89cb3affef3c5e9c15176b4cc94d8c1d"}};
			text = postUrl(httpClient, postMethod, url, params, headers);
			System.out.println(text);
		}

		//测试北京移动的查询余额
//		saveFile(httpClient, "https://login.10086.cn/captchazh.htm?type=05&timestamp=" + System.currentTimeMillis(), "C:\\a.jpg");
//		System.out.println("请输入验证码:");
//		Scanner in = new Scanner(System.in);
//		String authCode = in.nextLine();
//		url = "https://login.10086.cn/login.htm?accountType=01&account="+phoneNo+"&password="+password+"&pwdType=01&inputCode="+authCode+"&backUrl=http://shop.10086.cn/i/&rememberMe=0&channelID=12003&protocol=https:";
//		String[][] headers = {{"Referer", "https://login.10086.cn/login.html?channelID=12003&backUrl=http://shop.10086.cn/i/"}, {"Accept", "application/json, text/javascript, */*; q=0.01"},
//				{"Host", "login.10086.cn"}, {"Content-Type", "application/json; charset=utf-8"}, {"Accept-Encoding", "gzip, deflate"}};
//		text = getUrl(httpClient, url, headers);
//		if(text!=null){
//			JSONObject obj = new JSONObject(text);
//			if(obj.optString("code").contains("0000")){
//				String artifact = obj.optString("artifact");
//				url = obj.optString("assertAcceptURL");
//				url = url +"?backUrl=http://shop.10086.cn/i/&artifact="+artifact;
//				getUrl(httpClient, url, null);
//				url = "http://shop.10086.cn/i/?welcome="+System.currentTimeMillis();
//				getUrl(httpClient, url, null);
//				url = "http://shop.10086.cn/i/v1/fee/real/"+phoneNo+"?time="+System.currentTimeMillis();
//				text = getUrl(httpClient, url, null);
//				System.out.println(text);
//				obj = new JSONObject(text);
//				if (obj.optString("retCode").contains("000000")) {
//					System.out.println("OK");
//				}
//			}
//		}
	}

	public static String getUrl(HttpClient httpClient, PostMethod postMethod, String url, String[][] headers) {
		String ret = null;
		// 1. 生成 HttpClinet对象并设置参数
//		HttpClient httpClient = new HttpClient();
		// 设置 Http 连接超时为5秒
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

		// 2.生成 GetMethod 对象并设置参数
		GetMethod getMethod = new GetMethod(url);
		// 设置 get 请求超时为 5 秒
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// 设置请求重试处理，用的是默认的重试处理：请求五次
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(5, true));

		//设置请求头参数
		if(headers!=null) {
			for (int i = 0; i < headers.length; i++) {
				Header h = new Header(headers[i][0], headers[i][1]);
				getMethod.setRequestHeader(h);
			}
		}
		// 3. 执行 HTTP GET请求
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			// 4. 判断访问的状态码
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
			}
			getMethod.getRequestHeaders();

			// 5.处理 HTTP响应内容
			// HTTP响应头部信息，这里简单打印
			Header[] cheaders = getMethod.getResponseHeaders();
			for (Header h : cheaders) {
//				postMethod.setRequestHeader(h.getName(), h.getValue());
				System.out.println(h.getName() + " " + h.getValue());
			}
			// 读取 HTTP 响应内容，这里简单打印网页内容
			ret = getMethod.getResponseBodyAsString();
//			ret = new String(getMethod.getResponseBodyAsString().getBytes("gb2312"));
			// 读取为 InputStream，在网页内容数据量大时候推荐使用
			// InputStream response = getMethod.getResponseBodyAsStream();
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 6.释放连接
			getMethod.releaseConnection();
		}
		return ret;
	}


	/**
	 * 下载图片验证码
	 * @param httpClient
	 * @param url
	 * @param filePath
	 */
	public static void saveFile(HttpClient httpClient, String url, String filePath){
		// 设置 Http 连接超时为5秒
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		// 2.生成 GetMethod 对象并设置参数
		GetMethod getMethod = new GetMethod(url);
		// 设置 get 请求超时为 5 秒
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// 设置请求重试处理，用的是默认的重试处理：请求三次
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		//设置请求头参数
//		getMethod.setRequestHeader(new Header("Accept", "*/*"));
//		getMethod.setRequestHeader(new Header("Accept-Encoding", "gzip, deflate"));
//		getMethod.setRequestHeader(new Header("Accept-Language", "zh-CN"));
//		getMethod.setRequestHeader(new Header("Connection", "Keep-Alive"));
		// 3. 执行 HTTP GET请求
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			// 4. 判断访问的状态码
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
			}
			// 读取 HTTP 响应内容
			// 读取为 InputStream，在网页内容数据量大时候推荐使用
			InputStream response = getMethod.getResponseBodyAsStream();
			File file=new File(filePath);
			OutputStream os=null;
			try{
				os=new FileOutputStream(file);
				byte buffer[]=new byte[4*1024];
				int len = 0;
				while((len = response.read(buffer)) != -1){
					os.write(buffer,0,len);
				}
				os.flush();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try{
					os.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 6.释放连接
			getMethod.releaseConnection();
		}
	}


	/**
	 * 发送GET请求
	 * @param httpClient
	 * @param url
	 * @param headers
	 * @return
	 */
	public static String getUrl(HttpClient httpClient, String url, String[][] headers) {
		String ret = null;
		// 1. 生成 HttpClinet对象并设置参数
//		HttpClient httpClient = new HttpClient();
		// 设置 Http 连接超时为5秒
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

		// 2.生成 GetMethod 对象并设置参数
		GetMethod getMethod = new GetMethod(url);
		// 设置 get 请求超时为 5 秒
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// 设置请求重试处理，用的是默认的重试处理：请求五次
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(5, true));

		//设置请求头参数
		if(headers!=null) {
			for (int i = 0; i < headers.length; i++) {
				Header h = new Header(headers[i][0], headers[i][1]);
				getMethod.setRequestHeader(h);
			}
		}
		// 3. 执行 HTTP GET请求
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			// 4. 判断访问的状态码
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
			}
			getMethod.getRequestHeaders();

			// 5.处理 HTTP响应内容
			// HTTP响应头部信息，这里简单打印
//			Header[] cheaders = getMethod.getResponseHeaders();
//			for (Header h : cheaders) {
//				System.out.println(h.getName() + " " + h.getValue());
//			}
			// 读取 HTTP 响应内容，这里简单打印网页内容
			ret = getMethod.getResponseBodyAsString();
//			ret = new String(getMethod.getResponseBodyAsString().getBytes("gb2312"));
			// 读取为 InputStream，在网页内容数据量大时候推荐使用
			// InputStream response = getMethod.getResponseBodyAsStream();
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 6.释放连接
			getMethod.releaseConnection();
		}
		return ret;
	}

	/**
	 * 发送POST请求
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 */
	public static String postUrl(HttpClient httpClient, PostMethod postMethod, String url, String[][] params, String[][] headers) {
		String ret = null;
		// 1.生成 HttpClinet 对象并设置参数
//		HttpClient httpClient = new HttpClient();
		// 设置Http连接超时为5秒
//		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

		// 2.生成 PostMethod 对象并设置参数
//		PostMethod postMethod = new PostMethod(url);
		//设置请求头参数
		if(headers!=null) {
			for (int i = 0; i < headers.length; i++) {
				Header h = new Header(headers[i][0], headers[i][1]);
				postMethod.setRequestHeader(h);
			}
		}

		//设置请求参数
		if (params!=null) {
			NameValuePair[] postParams = new NameValuePair[params.length];
			for (int i = 0; i < params.length; i++) {
				NameValuePair param = new NameValuePair(params[i][0], params[i][1]);
				postParams[i] = param;
			}
			postMethod.setRequestBody(postParams);
		}

		// 设置post请求超时为 5 秒
		postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 50000);
		// 设置请求重试处理，用的是默认的重试处理：请求三次
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

		// 3.执行 HTTP POST请求
		try {
			int statusCode = httpClient.executeMethod(postMethod);
			// 4.判断访问的状态码
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + postMethod.getStatusLine());
			}

			// 5.处理 HTTP响应内容
			// HTTP响应头部信息，这里简单打印
//			Header[] cheaders = postMethod.getResponseHeaders();
//			for (Header h : cheaders) {
//				System.out.println(h.getName() + " " + h.getValue());
//			}

			// 读取 HTTP 响应内容，这里简单打印网页内容
			ret = postMethod.getResponseBodyAsString();// 读取为字节数组
			// 读取为 InputStream，在网页内容数据量大时候推荐使用
			// InputStream response = postMethod.getResponseBodyAsStream();
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 6.释放连接
			postMethod.releaseConnection();
		}
		return ret;
	}
}
