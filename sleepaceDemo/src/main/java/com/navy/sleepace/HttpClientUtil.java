package com.navy.sleepace;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpClientUtil {

	private static final Logger LOG = Logger.getLogger(HttpClientUtil.class);

	/**
	 * 定义超时配置
	 */
	public enum RequestConfigType {
		
		/**
		 * 设置10分钟超时
		 */
		TIMEOUT_600000(600000,600000,600000),
		/**
		 * 设置10s超时
		 */
		TIMEOUT_10000(10000, 10000, 10000),
		/**
		 * 设置2s超时
		 */
		TIMEOUT_2000(2000, 2000, 2000),
		/**
		 * 设置0.5秒
		 */
		TIMEOUT_500(500, 500, 500);
		private RequestConfig requestConfig;

		RequestConfigType(int socketTimeout, int connectTimeout,
				int requestTimeout) {
			this.requestConfig = RequestConfig.custom()
					.setSocketTimeout(socketTimeout)
					.setConnectTimeout(connectTimeout)
					.setConnectionRequestTimeout(requestTimeout).build();

		}
	}

	private static final CloseableHttpClient client;
	public static final String UTF8 = "UTF-8";
	public static final String GBK = "GBK";
	public static final String GB2312 = "GB2312";

	static {
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(100);
		connManager.setDefaultMaxPerRoute(10);
		client = HttpClients.custom().setConnectionManager(connManager).build();

	}

	public static String httpGet(String url, Map<String, String> params) {
		return httpGet(url, null, params);
	}
	
	public static String httpGet(String url, Map<String, String> headers,
			Map<String, String> params) {
		return httpGetCustomize(url, headers, params,
				RequestConfigType.TIMEOUT_10000);
	}

	/**
	 * 可修改超时时间http get请求
	 * 
	 * @param url
	 *            请求url
	 * @param headers
	 *            头
	 * @param params
	 *            请求参数
	 * @param requestConfigType
	 *            设置超时时间类型
	 * @return
	 */
	public static String httpGetCustomize(String url,
			Map<String, String> headers, Map<String, String> params,
			RequestConfigType requestConfigType) {
		if (params != null) {
			StringBuilder builder = new StringBuilder(url).append('?');
			for (Entry<String, String> e : params.entrySet()) {
				builder.append(e.getKey()).append('=').append(URLEncoder.encode(e.getValue()))
						.append('&');
			}
			url = builder.toString();
		}
		
		LOG.debug(url);

		HttpGet get = new HttpGet(url);
		get.setConfig(requestConfigType.requestConfig);

		String response = null;

		get.addHeader("Content-Type", "text/html;charset=UTF-8");
		if (headers != null) {
			Set<String> headersSet = headers.keySet();
			for (Iterator<String> ite = headersSet.iterator(); ite.hasNext();) {
				String headerKey = ite.next();
				get.addHeader(headerKey, headers.get(headerKey));
			}
		}

		try {
			HttpEntity entity = client.execute(get).getEntity();
			response = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			LOG.error(e);
			return null;
		} finally {
			get.releaseConnection();
		}

		return response;

	}
	
	public static String httpDeleteCustomize(String url,
			Map<String, String> headers, Map<String, String> params){
		return httpDeleteCustomize(url, headers, params,
				RequestConfigType.TIMEOUT_10000);
	}
	/**
	 * 可修改超时时间http delete请求
	 * 
	 * @param url
	 *            请求url
	 * @param headers
	 *            头
	 * @param params
	 *            请求参数
	 * @param requestConfigType
	 *            设置超时时间类型
	 * @return
	 */
	public static String httpDeleteCustomize(String url,
			Map<String, String> headers, Map<String, String> params,
			RequestConfigType requestConfigType) {
		if (params != null) {
			StringBuilder builder = new StringBuilder(url).append('?');
			for (Entry<String, String> e : params.entrySet()) {
				builder.append(e.getKey()).append('=').append(URLEncoder.encode(e.getValue()))
						.append('&');
			}
			url = builder.toString();
		}
		
		LOG.debug(url);

		HttpDelete delete = new HttpDelete(url);
		delete.setConfig(requestConfigType.requestConfig);

		String response = null;

		delete.addHeader("Content-Type", "text/html;charset=UTF-8");
		if (headers != null) {
			Set<String> headersSet = headers.keySet();
			for (Iterator<String> ite = headersSet.iterator(); ite.hasNext();) {
				String headerKey = ite.next();
				delete.addHeader(headerKey, headers.get(headerKey));
			}
		}

		try {
			HttpEntity entity = client.execute(delete).getEntity();
			response = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			LOG.error(e);
			return null;
		} finally {
			delete.releaseConnection();
		}

		return response;

	}
	
	


	public static String httpPost(String url, Map<String, String> headers,
			Map<String, String> params) {
		return httpPostCustomize(url, headers, params,
				RequestConfigType.TIMEOUT_10000);

	}

	/**
	 * 可修改超时时间http post请求
	 * 
	 * @param url
	 *            请求url
	 * @param headers
	 *            头
	 * @param params
	 *            请求参数
	 * @param requestConfigType
	 *            设置超时时间类型
	 * @return
	 */
	public static String httpPostCustomize(String url,
			Map<String, String> headers, Map<String, String> params,
			RequestConfigType requestConfigType) {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpPost post = new HttpPost(url);
		CloseableHttpResponse response = null;
		try {

			post.setConfig(requestConfigType.requestConfig);
	
			List<BasicNameValuePair> httpParams = null;
			if (params != null && !params.isEmpty()) {
				httpParams = new ArrayList<BasicNameValuePair>(params.size());
				for (Entry<String, String> entry : params.entrySet()) {
					String k = entry.getKey();
					String v = entry.getValue();
					if (v == null) {
						httpParams.add(new BasicNameValuePair(k, null));
					} else {
						httpParams.add(new BasicNameValuePair(k, v));
					}
				}
				if (headers != null) {
					for (Entry<String, String> e : headers.entrySet()) {
						post.addHeader(e.getKey(), e.getValue());
					}
				}
					post.setEntity(new UrlEncodedFormEntity(httpParams, UTF8));
				
			}
			String res = null;
			response=httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			res = EntityUtils.toString(entity, UTF8);
			return res;
		} catch (Exception e) {
			throw new RuntimeException("error post data to " + url, e);
		} finally {
			if(response!=null)
			{
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(httpClient!=null)
			{
				try {
					httpClient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			post.releaseConnection();
		}
	}

	/**
	 * Post Json body
	 *
	 * @param url
	 * @param headers
	 * @param jsonString
	 * @return
	 */
	public static String httpPost(String url, Map<String, String> headers,
			String jsonString) {
		return httpPostCustomize(url, headers, jsonString,
				RequestConfigType.TIMEOUT_10000);

	}

	public static String httpPostCustomize(String url,
			Map<String, String> headers, String jsonString,
			RequestConfigType requestConfigType) {
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfigType.requestConfig);

		if (headers != null) {
			for (Entry<String, String> e : headers.entrySet()) {
				post.addHeader(e.getKey(), e.getValue());
			}
		}
		if (jsonString != null) {
			StringEntity entity = new StringEntity(jsonString,
					ContentType.APPLICATION_JSON);
			post.setEntity(entity);
		}
		String response = null;
		try {
			HttpEntity entity = client.execute(post).getEntity();
			response = EntityUtils.toString(entity, UTF8);
		} catch (Exception e) {
			throw new RuntimeException("error post data to " + url, e);
		} finally {
			post.releaseConnection();
		}

		return response;

	}


	
}
