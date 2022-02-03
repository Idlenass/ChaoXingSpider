package Module.ChaoXin;

import Tool.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

public class CourseList {

    private Map<String, Course> courseMap = new HashMap<>();

    /**
     * 生成详情预览文本 一般调试用
     * @return
     */
    public String toDetailsString(){
        String str = "========== CourseList Details Begin ==========\n";
        for(String s : courseMap.keySet()){
            str += courseMap.get(s).toDetailsString();
        }
        str += "========== CourseList Details End ==========\n";
        return str;
    }

    /**
     * 根据document进行解析
     * @param document
     */
    private void generateCourseMapByDocument(Document document){
        //获取包含了所有课程的列表父元素ul
        Element courseListElement = document.getElementById("courseList");
        //捕捉不到
        if(courseListElement == null){
            Logger.log("loadCourseList : Canot get courseListElement : null");
            Logger.log("ChaoXing LoadingCourseList Faild.");
            return;
        }
        //获取其中的每一个li,也就是一个课程
        Elements courseListLiElements = courseListElement.select("li.course");
        for(Element e : courseListLiElements){
            Course course = new Course(e);
            this.courseMap.put(course.getCourseId(),course);
        }
    }

        //构造

    public CourseList() {
    }

    //根据document进行解析
    public CourseList(Document document) {
        this.generateCourseMapByDocument(document);
    }

    public CourseList(Map<String, Course> courseMap) {
        this.courseMap = courseMap;
    }

        //Getter And Setter

    public Course get(String courseId){
        return this.courseMap.get(courseId);
    }

    public Map<String, Course> getCourseMap() {
        return courseMap;
    }

    public void setCourseMap(Map<String, Course> courseMap) {
        this.courseMap = courseMap;
    }
}
