package com.linda.facebattle.NetWork;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;


public class NetUtil {


    public static String address="xxxx";




    /**
     * 根据输入流返回一个字符串
     * @param is
     * @return
     * @throws Exception
     */
    private static String getStringFromInputStream(InputStream is) throws Exception{

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        byte[] buff=new byte[1024];
        int len=-1;
        while((len=is.read(buff))!=-1){
            baos.write(buff, 0, len);
        }
        is.close();
        String html=baos.toString();
        baos.close();


        return html;
    }


    //post
    public static String post(Map<String, String> params,
                                    String urlPath) {
        StringBuilder stringBuilder = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    stringBuilder
                            .append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                            .append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            try {
                URL url;
                url=new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();
                urlConnection.setConnectTimeout(3000);
                urlConnection.setRequestMethod("POST"); // 以post请求方式提交
                urlConnection.setDoInput(true); // 读取数据
                urlConnection.setDoOutput(true); // 向服务器写数据
                // 获取上传信息的大小和长度
                byte[] myData = stringBuilder.toString().getBytes();
                // 设置请求体的类型是文本类型,表示当前提交的是文本数据
                urlConnection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length",
                        String.valueOf(myData.length));
                // 获得输出流，向服务器输出内容
                OutputStream outputStream = urlConnection.getOutputStream();
                // 写入数据
                outputStream.write(myData, 0, myData.length);
                outputStream.close();
                // 获得服务器响应结果和状态码
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == 200) {
                    // 取回响应的结果
                    return getStringFromInputStream(urlConnection.getInputStream());
                }else {
                    System.out.println(String.valueOf(responseCode));
                    return "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return "error";
    }



    public static String getTime(){
        HttpURLConnection conn=null;
        try {
            URL url=new URL(address+"time");
            conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(5000);
            conn.connect();
            int code=conn.getResponseCode();
            if(code==200){
                InputStream is=conn.getInputStream();
                String state=getStringFromInputStream(is);
                return state;
            }else {
                System.out.println(code);
                return "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
        }finally{
            if(conn!=null){
                conn.disconnect();
            }
        }
        return "error";
    }


    public static String toUploadFile(File file, String fileKey, String RequestURL,
                              Map<String, String> param) {

           String BOUNDARY =  UUID.randomUUID().toString(); // 边界标识 随机生成
           String PREFIX = "--";
           String LINE_END = "\r\n";
           String CONTENT_TYPE = "multipart/form-data"; // 内容类型


        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", "utf-8"); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

            /**
             * 当文件不为空，把文件包装并且上传
             */
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = null;
            String params = "";

            /***
             * 以下是用于上传参数
             */
            if (param != null && param.size() > 0) {
                System.out.println(param.keySet().size());
                for(int i =0 ;i<param.keySet().size();i++){
                    sb = new StringBuffer();
                    String key = (String) param.keySet().toArray()[i];
                    String value = param.get(key);
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
                    sb.append(value).append(LINE_END);
                    params = sb.toString();
                    dos.write(params.getBytes());
                }
//                Iterator<String> it = param.keySet().iterator();
//                while (it.hasNext()) {
//                    sb = null;
//                    sb = new StringBuffer();
//                    String key = it.next();
//                    String value = param.get(key);
//                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
//                    sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
//                    sb.append(value).append(LINE_END);
//                    params = sb.toString();
//                    dos.write(params.getBytes());
////                  dos.flush();
//                }
            }

            sb = null;
            params = null;
            sb = new StringBuffer();
            /**
             * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
             * filename是文件的名字，包含后缀名的 比如:abc.png
             */
            sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
            sb.append("Content-Disposition:form-data; name=\"" + fileKey
                    + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
            sb.append("Content-Type:image/jpeg" + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
            sb.append(LINE_END);
            params = sb.toString();
            System.out.println(params);
            sb = null;

            dos.write(params.getBytes());
            /**上传文件*/
            InputStream is = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            int curLen = 0;
            while ((len = is.read(bytes)) != -1) {
                curLen += len;
                dos.write(bytes, 0, len);
            }
            is.close();

            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
//
//          dos.write(tempOutputStream.toByteArray());
            /**
             * 获取响应码 200=成功 当响应成功，获取响应的流
             */
            int res = conn.getResponseCode();
            if (res == 200) {
                InputStream input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb1.append((char) ss);
                }
                String result = sb1.toString();
                return result;
            } else {

                return "error";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "error";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }





}
