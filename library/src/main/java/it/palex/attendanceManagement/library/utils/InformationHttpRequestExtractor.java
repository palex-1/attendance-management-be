package it.palex.attendanceManagement.library.utils;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alessandro Pagliaro
 *
 */
public class InformationHttpRequestExtractor {

	private static final String HEADER_USER_AGENT="User-Agent";
	private static final String NO_USER_AGENT_USER_AGENT = "NO_USER_AGENT";
	
	private static final String IP_WHEN_NOT_FOUND = "0.0.0.0";
	
	
	
	/**
	 * 
	 * @param httpRequest
	 * @return ip from request using ClientIPAddressRetriever, null will be returned if ip is not valid
	 */
	public static String getIpFromRequest(HttpServletRequest httpRequest, String proxyType){
		if(httpRequest==null || proxyType==null){
			throw new NullPointerException("httpRequest:"+httpRequest+" proxyType:"+proxyType);
		}
		String ip = ClientIPAddressRetriever.getIpAddress(httpRequest, proxyType);
		
    	if(ip==null || !InetAddressValidator.getInstance().isValid(ip)){
    		return IP_WHEN_NOT_FOUND;
    	}
    	return ip;
	}
	
	
	/**
	 * 
	 * @param httpRequest
	 * @return the user agent if exists in httpRequest else <strong>NO_USER_AGENT_USER_AGENT</strong> string<br>
	 * The method does not return null
	 * 
	 */
	public static String getUserAgentFromRequest(HttpServletRequest httpRequest){
		if(httpRequest==null){
			throw new NullPointerException();
		}
		String userAgent = httpRequest.getHeader(HEADER_USER_AGENT);
		
		//if user agent is correctly setted by client else use a generic user agent
		userAgent = userAgent == null ? NO_USER_AGENT_USER_AGENT : userAgent;
		
		return userAgent;
	}
	
	
	/**
	 * 
	 * @param httpRequest
	 * @return a string representation of http request, example: user-agent: gecko_fjf...
	 */
	public static String getHttpRequestInStringFormat(HttpServletRequest httpRequest){
		if(httpRequest==null){
			throw new NullPointerException();
		}
		Enumeration<String> headers = httpRequest.getHeaderNames();
		if(headers==null){
			return "";
		}
		StringBuilder sb = new StringBuilder(500);
		
		while(headers.hasMoreElements()){
			String header = headers.nextElement();
			String value = httpRequest.getHeader(header);
			sb.append(header);
			sb.append(":");
			sb.append(value);
			sb.append("\r\n");
		}
		
		return sb.toString();
	}
	
	
}

