package Module.ChaoXin;

import Tool.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HomeWork {
    private String homework_name;
    private String starttime;
    private String deadline;
    private HomeWorkStatu homeWorkStatu;

    /**
     * 根据一个作业的html元素解析出作业的信息
     * @param element
     * @return
     */
    private boolean generateHomeWorkByElement(Element element){
        //为了区分两个a元素，先选取作业标题所在a元素的父div
        Elements divs = element.select("div.titTxt");
        //判空
        if(divs.size() == 0){
            Logger.log("HomeWork.generateHomeWorkByElement ERROR : cannot find div.titTxt");
            Logger.log("generateHomeWorkByElement Faild.");
            return false;
        }

        //继续获取含有作业名称的a元素
        Elements a_title = divs.get(0).select("a");
        //判空
        if(a_title.size() == 0){
            Logger.log("HomeWork.generateHomeWorkByElement ERROR : cannot find <a>");
            Logger.log("generateHomeWorkByElement Faild.");
            return false;
        }

        //读取信息
        homework_name = a_title.attr("title");

        //读取开始时间和截止时间

//        //先获取所有span
//        Elements spans = divs.get(0).select("span");
//        //遍历span找到包含文本的元素
//        for(Element e : spans){
//            if(e.text)
//        }

        //选取包含 开始时间 文本的元素
        Elements starttimeElements = divs.get(0).getElementsContainingOwnText("开始时间");
        //判空
        if(starttimeElements.size() == 0){
            Logger.log("HomeWork.generateHomeWorkByElement ERROR : cannot getElementsContainingOwnText(\"开始时间\")");
            Logger.log("generateHomeWorkByElement Faild.");
            return false;
        }

        //读取信息
        starttime = starttimeElements.get(0).parent().ownText();

        //选取包含 截止时间 文本的元素
        Elements deadlineElements = divs.get(0).getElementsContainingOwnText("截止时间");
        //判空
        if(deadlineElements.size() == 0){
            Logger.log("HomeWork.generateHomeWorkByElement ERROR : cannot getElementsContainingOwnText(\"截止时间\")");
            Logger.log("generateHomeWorkByElement Faild.");
            return false;
        }

        //读取信息
        deadline = starttimeElements.get(0).parent().ownText();

        //获取作业状态
        Elements strongs = divs.get(0).select("strong");
        //判空
        if(strongs.size() == 0){
            Logger.log("HomeWork.generateHomeWorkByElement ERROR : cannot select(\"strong\")");
            Logger.log("generateHomeWorkByElement Faild.");
            return false;
        }

        homeWorkStatu = HomeWorkStatu.getStatuByString(strongs.get(0).ownText());

        return true;
    }

    /**
     * 获取详情信息文本 一般用于调试
     * @return
     */
    public String toDetailsString(){
        String str = "";
        str += "===== Homework Details Begin =====\n";
        str += "homework_name" + " : " + homework_name + "\n";
        str += "starttime" + " : " + starttime + "\n";
        str += "deadline" + " : " + deadline + "\n";
        str += "homeWorkStatu" + " : " + homeWorkStatu + "\n";
        str += "===== Homework Details End =====\n";
        return str;
    }

        //构造

    public HomeWork(Element element) {
        this.generateHomeWorkByElement(element);
    }

    public HomeWork(String homework_name, String starttime, String deadline, HomeWorkStatu homeWorkStatu) {
        this.homework_name = homework_name;
        this.starttime = starttime;
        this.deadline = deadline;
        this.homeWorkStatu = homeWorkStatu;
    }

        //Getter and Setter

    public String getHomework_name() {
        return homework_name;
    }

    public void setHomework_name(String homework_name) {
        this.homework_name = homework_name;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public HomeWorkStatu getHomeWorkStatu() {
        return homeWorkStatu;
    }

    public void setHomeWorkStatu(HomeWorkStatu homeWorkStatu) {
        this.homeWorkStatu = homeWorkStatu;
    }
}
