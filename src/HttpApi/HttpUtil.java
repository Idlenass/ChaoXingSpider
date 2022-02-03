package HttpApi;

import Tool.Logger;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

/**
 * HTTP工具
 * @author wuzhehan
 *
 */
public class HttpUtil {

    private HttpMethod httpMethod;
    private String urlStr;
    private HttpURLConnection httpURLConnection;
    private Map<String, String> paramMap = new HashMap<>();
    private Map<String, String> requestProertyMap = new HashMap<>();

    /**
     * 链接
     * @return
     * @throws Exception
     */
    public HttpResponseBody connect() throws Exception {
        if(httpMethod == null || urlStr == null){
            Logger.debug("at HttpApi.HttpUtil.connect : 必要参数未初始化");
            return new HttpResponseBody();
        }
        HttpResponseBody httpResponseBody;
        if(this.httpMethod.equals(HttpMethod.GET)){
            httpResponseBody = this.get();
        }else{
            httpResponseBody = this.post();
        }
        return httpResponseBody;
    }

    /**
     * 添加请求头参数
     * @param key
     * @param value
     */
    public void setRequestProerty(String key, String value){
        requestProertyMap.put(key, value);
    }

    /**
     * 添加请求参数
     * @param key
     * @param value
     */
    public void setParam(String key, String value){
        paramMap.put(key, value);
    }

    /**
     * 模拟Http Get请求
     * @return
     * @throws Exception
     */
    private HttpResponseBody get() throws Exception{
        HttpResponseBody httpResponseBody = new HttpResponseBody();
        urlStr = urlStr + "?" + getParamString(paramMap);
        try{
            //创建URL对象
            URL url = new URL(urlStr);
            //获取URL连接
            httpURLConnection = (HttpURLConnection) url.openConnection();
            //设置通用的请求属性
            setHttpUrlConnection_Base(httpURLConnection);
            setHttpUrlConnection_Custom(httpURLConnection);
            //建立实际的连接
            httpURLConnection.connect();
            //获取响应的内容
            httpResponseBody = new HttpResponseBody(httpURLConnection);
        }finally{
            if(null!=httpURLConnection) httpURLConnection.disconnect();
        }
        return httpResponseBody;
    }

    /**
     * 模拟Http Post请求
     * @return
     * @throws Exception
     */
    private HttpResponseBody post() throws Exception{
        HttpResponseBody httpResponseBody = new HttpResponseBody();
        PrintWriter writer = null;
        try{
            //创建URL对象
            URL url = new URL(urlStr);
            //获取请求参数
            String param = getParamString(paramMap);
            //获取URL连接
            httpURLConnection = (HttpURLConnection) url.openConnection();
            //设置通用请求属性
            setHttpUrlConnection_Base(httpURLConnection);
            setHttpUrlConnection_Custom(httpURLConnection);
            //建立实际的连接
            httpURLConnection.connect();
            //将请求参数写入请求字符流中
            writer = new PrintWriter(httpURLConnection.getOutputStream());
            writer.print(param);
            writer.flush();
            //读取响应的内容
            httpResponseBody = new HttpResponseBody(httpURLConnection);
        }finally{
            if(null!=httpURLConnection) httpURLConnection.disconnect();
            if(null!=writer) writer.close();
        }
        return httpResponseBody;
    }

    /**
     * 设置Http连接属性 基本信息
     * @param conn
     *             http连接
     * @return
     * @throws ProtocolException
     * @throws Exception
     */
    private void setHttpUrlConnection_Base(HttpURLConnection conn) throws ProtocolException{
        conn.setRequestMethod(httpMethod.toString());
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("Accept-Language", "zh-CN");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
        conn.setRequestProperty("Connection", "Keep-Alive");
        if(null != httpMethod && HttpMethod.POST.equals(httpMethod)){
            conn.setDoOutput(true);
            conn.setDoInput(true);
        }
    }

    /**
     * 设置Http连接属性 基本信息
     * @param conn
     *             http连接
     * @return
     * @throws ProtocolException
     * @throws Exception
     */
    private void setHttpUrlConnection_Custom(HttpURLConnection conn){
        for(String s : requestProertyMap.keySet()){
            conn.setRequestProperty(s,requestProertyMap.get(s));
        }
    }

    /**
     * 将参数转为路径字符串
     * @param paramMap
     *             参数
     * @return
     */
    private String getParamString(Map<String, String> paramMap){
        if(null==paramMap || paramMap.isEmpty()){
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for(String key : paramMap.keySet() ){
            builder
                    .append("&")
                    .append(key)
                    .append("=")
                    .append(paramMap.get(key));
        }
        return builder.deleteCharAt(0).toString();
    }

        //构造

    public HttpUtil() {

    }

    public HttpUtil(String urlStr, HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        this.urlStr = urlStr;
    }

        //Getter AND Setter

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrlStr() {
        return urlStr;
    }

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, String> getRequestProertyMap() {
        return requestProertyMap;
    }

    public void setRequestProertyMap(Map<String, String> requestProertyMap) {
        this.requestProertyMap = requestProertyMap;
    }

    public HttpURLConnection getHttpURLConnection() {
        return httpURLConnection;
    }

    public void setHttpURLConnection(HttpURLConnection httpURLConnection) {
        this.httpURLConnection = httpURLConnection;
    }
}