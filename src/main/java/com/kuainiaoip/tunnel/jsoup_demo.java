package com.kuainiaoip.tunnel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

/***
 * 此处 Jsoup Version 1.9.1
 * @author sun
 * JDK 8u111版本后环境下：要访问的目标页面为HTTPS协议时，需修改“jdk.http.auth.tunneling.disabledSchemes”值
 */
public class jsoup_demo {
    // 代理隧道验证信息
    final static String ProxyUser = "";
    final static String ProxyPass = "";

    // 代理服务器
    final static String ProxyHost = "tunnel.kuainiaoip.com";
    final static Integer ProxyPort = 28999;

    public static String getUrlProxyContent(String url) {
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

        try {
            // 此处自己处理异常、其他参数等
            Document doc = Jsoup.connect(url)
                    .followRedirects(false)
                    .timeout(3000)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0")
                    .header("Accept", "text/html, application/xhtml+xml, */*")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .ignoreContentType(true)
                    .proxy(proxy)
                    .get();

            if (doc != null) {
                System.out.println(doc.body().html());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) throws Exception {
        // 要访问的目标页面
        String targetUrl = "https://httpbin.org/get";

        // JDK 8u111版本后，目标页面为HTTPS协议，启用proxy用户密码鉴权
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");

        getUrlProxyContent(targetUrl);
    }
}
