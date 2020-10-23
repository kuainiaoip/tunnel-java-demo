package com.kuainiaoip.tunnel;

import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 使用httpclient请求隧道服务器 请求http和https网页均适用
 */
public class httpclient_demo {

    private static String pageUrl = "https://httpbin.org/get"; // 要访问的目标网页
    private static String proxyIp = "tunnel.kuainiaoip.com"; // 隧道服务器域名
    private static int proxyPort = 28999; // 端口号
    // 用户名密码
    private static String username = "";
    private static String password = "";

    public static void main(String[] args) throws Exception {
        // JDK 8u111版本后，目标页面为HTTPS协议，启用proxy用户密码鉴权
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(proxyIp, proxyPort),
                new UsernamePasswordCredentials(username, password));
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
        try {
            URL url = new URL(pageUrl);
            HttpHost target = new HttpHost(url.getHost(), url.getDefaultPort(), url.getProtocol());
            HttpHost proxy = new HttpHost(proxyIp, proxyPort);

            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
            HttpGet httpget = new HttpGet(url.getPath());
            httpget.setConfig(config);
            httpget.addHeader("Accept-Encoding", "gzip"); // 使用gzip压缩传输数据让访问更快
            CloseableHttpResponse response = httpclient.execute(target, httpget);
            try {
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
}