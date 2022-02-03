package HttpApi;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpResponseBody {
    private Map<String, List<String>> headerFields;
    private String responseMessage;
    private int responseCode;
    private String requestMethod;
    private String responseContent;

        //Custom Methods


    /**
     * 一个普通的传递层
     * 从headerFields中获取响应头要求设置的cookie字段
     * 直接传递给CookieList由其自身解析处理
     * @return
     */
    public CookieList generateCookieList(){
        return new CookieList(headerFields.get("Set-Cookie"));
    }

    /**
     * 解析响应头并输出响应头内容
     */
    public String readHeaderFields(){
        StringBuilder stringBuilder = new StringBuilder();
        Set<Map.Entry<String, List<String>>> headerFieldsSntrySet = headerFields.entrySet();
        Iterator<Map.Entry<String,List<String>>> headerFieldsIterator = headerFieldsSntrySet.iterator();
        stringBuilder.append("========== Print HeaderFields =========\n");
        while(headerFieldsIterator.hasNext()){
            Map.Entry<String,List<String >> nextEntry = headerFieldsIterator.next();
            String key = nextEntry.getKey();
            List<String> value = nextEntry.getValue();
            stringBuilder.append(key + " : " + value.toString() + "\n");
        }
        stringBuilder.append("========== Print Set-Cookis =========\n");
        List<String> setcookie = headerFields.get("Set-Cookie");
        for(String s : setcookie){
            stringBuilder.append(s + "\n");
        }
        stringBuilder.append("========== Set-Cookis End =========\n");
        stringBuilder.append("========== HeaderFields End =========\n");
        return stringBuilder.toString();
    }

    /**
     * 读取响应字节流并将之转为字符串
     * @param in
     *         要读取的字节流
     * @return
     * @throws IOException
     */
    private String readResponseContent(InputStream in) throws IOException{
        Reader reader = null;
        StringBuilder content = new StringBuilder();
        try{
            reader = new InputStreamReader(in);
            char[] buffer = new char[1024];
            int head = 0;
            while( (head=reader.read(buffer))>0 ){
                content.append(new String(buffer, 0, head));
            }
            return content.toString();
        }finally{
            if(null!=in) in.close();
            if(null!=reader) reader.close();
        }
    }

    //空构造
    public HttpResponseBody() {
    }

    //根据connection对象自动生成参数
    public HttpResponseBody(HttpURLConnection httpURLConnection) throws IOException {
        this.headerFields = httpURLConnection.getHeaderFields();
        this.responseMessage = httpURLConnection.getResponseMessage();
        this.responseCode = httpURLConnection.getResponseCode();
        this.requestMethod = httpURLConnection.getRequestMethod();
        this.responseContent = this.readResponseContent(httpURLConnection.getInputStream());
    }

    //完全参数构造
    public HttpResponseBody(Map<String, List<String>> headerFields, String responseMessage, int responseCode, String requestMethod, String responseContent) {
        this.headerFields = headerFields;
        this.responseMessage = responseMessage;
        this.responseCode = responseCode;
        this.requestMethod = requestMethod;
        this.responseContent = responseContent;
    }

        //Getter AND Setter

    public Map<String, List<String>> getHeaderFields() {
        return headerFields;
    }

    public void setHeaderFields(Map<String, List<String>> headerFields) {
        this.headerFields = headerFields;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }
}
