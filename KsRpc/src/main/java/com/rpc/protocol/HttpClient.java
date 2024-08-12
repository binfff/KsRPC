package com.rpc.protocol;

import com.rpc.common.Invocation;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

/**
 * HttpClient
 *
 * @author gjh
 * @version 1.0
 * @Date 2023-07-22 10:21
 * @description TODO
 */
public class HttpClient {
    public String send(String hostname,Integer port,Invocation invocation){
      // 发送http请求
      try {
        URL url =new URL("http",hostname,port,"/");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);

        OutputStream outputStream = httpURLConnection.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);

        oos.writeObject(invocation);
        oos.flush();
        oos.close();

        InputStream inputStream = httpURLConnection.getInputStream();
        String s = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        return s;
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }
}
