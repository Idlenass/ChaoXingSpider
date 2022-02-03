package HttpApi;

import Tool.Logger;

import java.util.*;

public class CookieList {
    private Map<String , Cookie> cookieList = new HashMap<>();

        //Custom Methods

    /**
     * 将新的cookielist合并
     * @param newCookieList
     * @return
     */
    public Map<String , Cookie> upDateCookieList(CookieList newCookieList){
        for(String s : newCookieList.getCookieList().keySet()){
            this.cookieList.put(s,newCookieList.getCookieList().get(s));
        }
        return this.cookieList;
    }

    /**
     * 转为应写入请求头的cookie文本
     * @return
     */
    public String toRequestHeaderString(){
        StringBuilder stringBuilder = new StringBuilder();
        Set<String> cookieKeySet = cookieList.keySet();
        Iterator<String> cookieKeyIterator = cookieKeySet.iterator();
        while(cookieKeyIterator.hasNext()){
            Cookie cookie = cookieList.get(cookieKeyIterator.next());
            stringBuilder.append(cookie.getKey()).append("=").append(cookie.getValue());
            if(cookieKeyIterator.hasNext()){
                stringBuilder.append(";");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 显示所有cookie的详细信息
     * @return
     */
    public String toDetialString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("========== Print CookieList Details ==========\n");
        Set<String> keySet = cookieList.keySet();
        Iterator<String> iterator = keySet.iterator();
        while(iterator.hasNext()){
            stringBuilder.append(cookieList.get(iterator.next()).toDetailsString() + "\n");
        }
        stringBuilder.append("========== CookieList Details End ==========\n");
        return stringBuilder.toString();
    }

    /**
     * 根据单条cooide文本组成的List生成cookielist
     * @param cookiestrlist
     * @return 生成了几条cookie
     */
    private int generaterCookieList(List<String> cookiestrlist){
        if(cookiestrlist == null){
            return 0;
        }
        for(String s : cookiestrlist){
            Logger.debug("at HttpApi.CookieList.generaterCookieList : Scaning cookiestr : " + s);
            Cookie cookie = new Cookie(s);
            cookieList.put(cookie.getKey(),cookie);
            Logger.debug("at HttpApi.CookieList.generaterCookieList : Generated cookie : \n" + cookie.toDetailsString());
        }
        return cookiestrlist.size();
    }

    //Custom Constrctor
    public CookieList(List<String> cookiestrlist){
        this.generaterCookieList(cookiestrlist);
    }

    //Empty Constructor
    public CookieList() {
    }

    //Full Constructor
    public CookieList(Map<String, Cookie> cookieList) {
        this.cookieList = cookieList;
    }

        //Getter AND Setter

    public Map<String, Cookie> getCookieList() {
        return cookieList;
    }

    public void setCookieList(Map<String, Cookie> cookieList) {
        this.cookieList = cookieList;
    }
}
