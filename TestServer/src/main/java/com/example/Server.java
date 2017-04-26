package com.example;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws JSONException, UnsupportedEncodingException {

        Myclass myclass = new Myclass("张三","男");

        System.out.print("张三-男");

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        String str = gson.toJson(myclass);

        System.out.print(str);


        try {

            ServerSocket server = new ServerSocket(8889);//定义客户端的端口号
            Socket client = server.accept();//定义一个Socket对象

            InputStream is = client.getInputStream();//服务器接受信息输入流，也就是接受从服务器段发送过来的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(is));//用bufferedreader包装下输入流

            OutputStream os = client.getOutputStream();//这是用来给服务器发送消息的输出流

            boolean flag = true; // 定义一个死循环，让服务器不停的接受从客户端发送来的字符串

            while (flag) {
                String s = br.readLine();//s是从客户端接受到得字符串

                if (null == s) {
                    break;
                }

                System.out.println(s);

                String strResponse = "HTTP/1.1 200 OK\nContent-Length: " + str.getBytes().length + "\n\n" + str;

                os.write(strResponse.getBytes(),
                         0,
                         strResponse.getBytes().length);

                os.flush();
            }

            client.close();
        }
        catch (IOException e) {//try 跟catch你不用管，这是用来处理异常的，就是固定格式
            e.printStackTrace();
        }
    }
}  
