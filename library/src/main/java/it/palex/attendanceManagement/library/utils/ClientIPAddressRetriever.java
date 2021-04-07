package it.palex.attendanceManagement.library.utils;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alessandro Pagliaro
 *
 */
public class ClientIPAddressRetriever {

	public static final String HTTP_HEADER_X_REAL_IP = "X-REAL-IP";
	public static final String HTTP_HEADER_X_FORWARDED_FOR = "X-FORWARDED-FOR";
	
	public static final String PROXY_TYPE_CLOUDFLARE = "CLOUDFLARE";
	public static final String PROXY_TYPE_NGINX = "NGINX";
	public static final String PROXY_TYPE_IGNORE_PROXY = "IGNORE";
	public static final String SPRING_CLOUD_GATEWAY = "SPRING_CLOUD_GATEWAY";
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ClientIPAddressRetriever.class);
	
	/**
	 * 
	 * @param httpRequest
	 * @param proxyType
	 * @return ip address of the client behind a proxy. Use PROXY_TYPE_IGNORE_PROXY to ignore proxy
	 */
	public static String getIpAddress(HttpServletRequest httpRequest, String proxyType){

		if(httpRequest==null || proxyType==null){
			throw new NullPointerException();
		}
		
		switch( proxyType ){
			
			case PROXY_TYPE_CLOUDFLARE:{
				return getIpAddressBehindCloudflareProxy(httpRequest);
			}
			case PROXY_TYPE_NGINX:{
				return getIpAddressBehindNGINXProxy(httpRequest);
			}
			case PROXY_TYPE_IGNORE_PROXY:{
				return getIpAddressIgnoringProxy(httpRequest);
			}
			case SPRING_CLOUD_GATEWAY:{
				return getIpAddressBehindSpringCloudGateway(httpRequest);
			}
			default :{
				throw new IllegalArgumentException("Proxy type is invalid "+proxyType);
			}
		}
	}
	
	
	/*
	 * Cloudflare set the ip address in X-FORWARDED-FOR header
	 */
	public static String getIpAddressBehindCloudflareProxy(HttpServletRequest request){
		if(request==null){
			throw new NullPointerException();
		}
		String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }else{
            	//if remote address header does not has any entry get remote address
            	remoteAddr = remoteAddr.split(",").length>0 ? remoteAddr.split(",")[0] : request.getRemoteAddr();
            }
        }

        return remoteAddr;
	}
	
	
	
	/*
	 * NGINX set the ip address of client in X-REAL_IP header
	 */
	private static String getIpAddressBehindNGINXProxy(HttpServletRequest request){
		if(request==null){
			throw new NullPointerException();
		}
		String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-REAL-IP");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }else{
            	//if remote address header does not has any entry get remote address
            	remoteAddr = remoteAddr.split(",").length>0 ? remoteAddr.split(",")[0] : request.getRemoteAddr();
            }
        }
        
        return remoteAddr;
	}
	
	
	public static String getIpAddressBehindSpringCloudGateway(HttpServletRequest request) {
		if(request==null){
			throw new NullPointerException();
		}
		String remoteAddr = "";

        if (request != null) {
        	remoteAddr = null;
        	remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }else{
            	//if remote address header does not has any entry get remote address
            	String[] addresses = remoteAddr.split(",");
            	if(addresses.length>0) {
            		remoteAddr = addresses[addresses.length-1];
            	}
            }
        }
        
        return remoteAddr;
	}
		
	
	public static String getIpAddressIgnoringProxy(HttpServletRequest request){
		if(request==null){
			throw new NullPointerException();
		}
		return request.getRemoteAddr();
	}
	
	
	
	
}
