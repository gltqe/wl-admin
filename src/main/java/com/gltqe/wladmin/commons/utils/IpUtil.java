package com.gltqe.wladmin.commons.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * IP工具类
 *
 * @author gltqe
 * @date 2022/7/3 0:25
 **/
public class IpUtil {

    private final static String UN_KNOWN = "unKnown";
    private static final String BROWSER_FIREFOX = "Firefox";
    private static final String BROWSER_CHROME = "Chrome";
    private static final String BROWSER_TRIDENT = "Trident";
    private final static String X_FORWARDED_FOR = "X-Forwarded-For";
    private final static String X_REAL_IP = "X-Real-IP";
    private final static String USER_AGENT = "User-Agent";
    private final static String WINDOWS = "WINDOWS";
    private final static String WINDOWS_10 = "WINDOWS NT 10.0";
    private final static String WINDOWS_6_2 = "WINDOWS NT 6.2";
    private final static String WINDOWS_6_1 = "WINDOWS NT 6.1";
    private final static String WINDOWS_6_0 = "WINDOWS NT 6.0";
    private final static String WINDOWS_5_2 = "WINDOWS NT 5.2";
    private final static String WINDOWS_5_1 = "WINDOWS NT 5.1";
    private final static String WINDOWS_5_01 = "WINDOWS NT 5.01";
    private final static String WINDOWS_5_0 = "WINDOWS NT 5.0";
    private final static String WINDOWS_4_0 = "WINDOWS NT 4.0";
    private final static String WINDOWS_98_WIN_9X = "WINDOWS 98; WIN 9X 4.90";
    private final static String WINDOWS_98 = "WINDOWS 98";
    private final static String WINDOWS_95 = "WINDOWS 95";
    private final static String WINDOWS_CE = "WINDOWS CE";
    private final static String MAC = "MAC";
    private final static String UNIX = "UNIX";
    private final static String LINUX = "LINUX";
    private final static String SUNOS = "SUNOS";

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return java.lang.String
     * @author
     * @date 2022/7/3 0:25
     **/
    public final static String getIpAddress(HttpServletRequest request) {
        String ip = "";
        try {
            // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
            ip = request.getHeader(X_FORWARDED_FOR);
            if (ip != null && ip.length() > 0 && !UN_KNOWN.equalsIgnoreCase(ip)) {
                // 多次反向代理后会有多个ip值，第一个ip才是真实ip
                int index = ip.indexOf(",");
                if (index != -1) {
                    return ip.substring(0, index);
                } else {
                    return ip;
                }
            }
            ip = request.getHeader(X_REAL_IP);
            if (ip != null && ip.length() > 0 && !UN_KNOWN.equalsIgnoreCase(ip)) {
                return ip;
            }
            ip = request.getRemoteAddr();
        } catch (Exception e) {
        }
        return ip;
    }

    /**
     * 获取系统名
     *
     * @param request
     * @return java.lang.String
     * @author
     * @date 2022/7/3 0:27
     **/
    public static String getOs(HttpServletRequest request) {
        String osName = "";
        String userAgent = request.getHeader(USER_AGENT).toUpperCase();
        if (userAgent.contains(WINDOWS)) {
            if (userAgent.contains(WINDOWS_10)) {
                osName = "Windows 10";
            } else if (userAgent.contains(WINDOWS_6_2)) {
                osName = "Windows 8";
            } else if (userAgent.contains(WINDOWS_6_1)) {
                osName = "Windows 7";
            } else if (userAgent.contains(WINDOWS_6_0)) {
                osName = "Windows Vista";
            } else if (userAgent.contains(WINDOWS_5_2)) {
                osName = "Windows XP";
            } else if (userAgent.contains(WINDOWS_5_1)) {
                osName = "Windows XP";
            } else if (userAgent.contains(WINDOWS_5_01)) {
                osName = "Windows 2000";
            } else if (userAgent.contains(WINDOWS_5_0)) {
                osName = "Windows 2000";
            } else if (userAgent.contains(WINDOWS_4_0)) {
                osName = "Windows NT 4.0";
            } else if (userAgent.contains(WINDOWS_98_WIN_9X)) {
                osName = "Windows ME";
            } else if (userAgent.contains(WINDOWS_98)) {
                osName = "Windows 98";
            } else if (userAgent.contains(WINDOWS_95)) {
                osName = "Windows 95";
            } else if (userAgent.contains(WINDOWS_CE)) {
                osName = "Windows CE";
            }
        } else if (userAgent.contains(MAC)) {
            osName = "Mac";
        } else if (userAgent.contains(UNIX)) {
            osName = "UNIX";
        } else if (userAgent.contains(LINUX)) {
            osName = "Linux";
        } else if (userAgent.contains(SUNOS)) {
            osName = "SunOS";
        } else {
            osName = "未知";
        }
        return osName;
    }

    /**
     * 获取浏览器类型
     *
     * @param request
     * @return java.lang.String
     * @author
     * @date 2022/7/3 0:27
     **/
    public static String getBrowser(HttpServletRequest request) {
        // 获取请求头：user-agent
        String userAgent = request.getHeader(USER_AGENT);
        // 判断用户使用的浏览器类型
        if (userAgent.contains(BROWSER_FIREFOX)) {
            return "火狐";
        } else if (userAgent.contains(BROWSER_CHROME)) {
            return "谷歌";
        } else if (userAgent.contains(BROWSER_TRIDENT)) {
            return "IE";
        } else {
            return "未知";
        }
    }
}
