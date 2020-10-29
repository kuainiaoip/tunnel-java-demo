package com.kuainiaoip.tunnel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

public class jsoup_demo {
    // 代理隧道验证信息
    final static String ProxyUser = "67fec52c-ba99-4e96-95a2-5f7687203e5b";
    final static String ProxyPass = "5VavbY";

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
                    .header("User-Agent", " Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0")
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
        String targetUrl = "http://myip.ipip.net";

        // JDK 8u111版本后，目标页面为HTTPS协议，启用proxy用户密码鉴权
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");

        getUrlProxyContent(targetUrl);
    }
}
