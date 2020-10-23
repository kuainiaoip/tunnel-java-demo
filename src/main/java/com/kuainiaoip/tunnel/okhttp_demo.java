package com.kuainiaoip.tunnel;

import okhttp3.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class okhttp_demo {
    public static void main(String args[]) throws IOException {
        // 目标网站
        String targetUrl = "https://httpbin.org/get";

        // 用户名密码
        final String username = "";
        final String password = "";

        String ip = "tunnel.kuainiaoip.com";   // 代理服务器IP
        int port = 28999;

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));

        Authenticator authenticator = new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic(username, password);
                return response.request().newBuilder()
                        .header("Proxy-Authorization", credential)
                        .build();
            }
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(proxy)
                .proxyAuthenticator(authenticator)
                .build();

        Request request = new Request.Builder()
                .url(targetUrl)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3100.0 Safari/537.36")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());

    }
}