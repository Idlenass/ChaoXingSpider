package Module.ChaoXin;

import Tool.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

public class HomeWorkList {

    private Map<String, HomeWork> homeWorkMap = new HashMap<>();

    /**
     * 根据整个html文档解析作业列表
     * @param document
     */
    private void generateHomeWorkMapByDocument(Document document){
        Elements ul_homeworkList = document.select("ul.clearfix");
        //判断获取成功与否
        if(ul_homeworkList.size() == 0){
            Logger.log("HomeWorkList.generateHomeWorkMapByDocument : cannot find 'ul.clearfix'");
            Logger.log("generateHomeWorkMapByDocument Faild");
            return;
        }

        //遍历单个作业的li元素，生成作业实体
        Elements li_homework = ul_homeworkList.get(0).select("li");
        for(Element e : li_homework){
            HomeWork homeWork = new HomeWork(e);
            homeWorkMap.put(homeWork.getHomework_name(),homeWork);
        }
    }

    /**
     * 获取详情信息文本 一般用于调试
     * @return
     */
    public String toDetailsString(){
        String str = "";
        str += "========== HomeworkList Details Begin ==========\n";
        for(String s : homeWorkMap.keySet()){
            str += homeWorkMap.get(s).toDetailsString() + "\n";
        }
        str += "========== HomeworkList Details End ==========\n";
        return str;
    }

        //构造


    public HomeWorkList() {
    }

    public HomeWorkList(Document document) {
        this.generateHomeWorkMapByDocument(document);
    }

    public HomeWorkList(Map<String, HomeWork> homeWorkMap) {
        this.homeWorkMap = homeWorkMap;
    }

        //Getter and Setter


    public Map<String, HomeWork> getHomeWorkMap() {
        return homeWorkMap;
    }

    public void setHomeWorkMap(Map<String, HomeWork> homeWorkMap) {
        this.homeWorkMap = homeWorkMap;
    }
}
