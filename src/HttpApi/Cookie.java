package HttpApi;

import Tool.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Cookie {
    private String Key = "";
    private String Value = "";
    private String Path = "";
    private String Domain = "";
    private String Expires = "";
    private boolean HttpOnly = false;

    private boolean generateDataByString(String cookiestr){
        boolean rtstatu = true;
        //用;分割一条cookie文本
        String[] strlist = cookiestr.split(";");
        //遍历每一个键值对
        for(String s : strlist){
            Logger.debug("at HttpApi.Cookie.generateDataByString : Scaning cookiestr : " + s);
            //分割键值对以获取属性和value
            String[] strlist2 = s.trim().split("=");
            //先看一下分割出来有几个数据，如果只有一个，可能是httponly，大于2个可能有错误
            Logger.debug("at HttpApi.Cookie.generateDataByString : length : " + strlist2.length);
            if(strlist2.length == 2){//正常的键值对
                Logger.debug("at HttpApi.Cookie.generateDataByString : Strlist2 : " + strlist2[0] + "," +strlist2[1]);
                //把数据放进对应变量
                switch (strlist2[0]){
                    case "Path":{
                        this.setPath(strlist2[1]);
                        break;
                    }
                    case "Domain":{
                        this.setDomain(strlist2[1]);
                        break;
                    }
                    case "Expires":{
                        this.setExpires(strlist2[1]);
                        break;
                    }
                    default:{//key
                        this.setKey(strlist2[0]);
                        this.setValue(strlist2[1]);
                        break;
                    }
                }
            }else if(strlist2.length == 1){//可能是HttpOnly
                Logger.debug("at HttpApi.Cookie.generateDataByString : HttpOnlySetted");
                if(strlist2[0].trim().equals("HttpOnly")){
                    this.setHttpOnly(true);
                }
            }else{//长度大于2，有问题
                System.out.println("[ERROR]: Cookie键值对异常: " + s);
                rtstatu = false;
            }
        }
        return rtstatu;
    }

    public String toDetailsString(){
        String str = "";
        str += "========== HttpApi.Cookie Details Begin ==========";
        str += "\n";
        str += "name : " + Key;
        str += "\n";
        str += "value : " + Value;
        str += "\n";
        str += "Domain : " + Domain;
        str += "\n";
        str += "Expires : " + Expires;
        str += "\n";
        str += "Path : " + Path;
        str += "\n";
        str += "========== HttpApi.Cookie Details End ==========";
        return str;
    }


    public Cookie() {
    }

    public Cookie(String cookiestr) {
        generateDataByString(cookiestr);
    }

    public Cookie(String key, String value, String path, String domain, String expires) {
        Key = key;
        Value = value;
        Path = path;
        Domain = domain;
        Expires = expires;
    }

    public Cookie(String key, String value, String path, String domain, String expires, boolean httpOnly) {
        Key = key;
        Value = value;
        Path = path;
        Domain = domain;
        Expires = expires;
        HttpOnly = httpOnly;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getDomain() {
        return Domain;
    }

    public void setDomain(String domain) {
        Domain = domain;
    }

    public String getExpires() {
        return Expires;
    }

    public void setExpires(String expires) {
        Expires = expires;
    }

    public boolean isHttpOnly() {
        return HttpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        HttpOnly = httpOnly;
    }
}
