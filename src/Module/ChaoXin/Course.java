package Module.ChaoXin;

import Tool.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Course {

    private String clazzId;
    private String courseId;
    private String url_detail;
    private String url_img;
    private String course_name;
    private String teacher;

    private HomeWorkList homeWorkList = new HomeWorkList();

    /**
     * 生成详情预览文本 一般调试用
     * @return
     */
    public String toDetailsString(){
        return  "======= Course Details Begin =======\n" +
                "clazzId = " + clazzId + "\n" +
                "courseId = " + courseId + "\n" +
                "url_detail = " + url_detail + "\n" +
                "url_img = " + url_img + "\n" +
                "course_name = " + course_name + "\n" +
                "teacher = " + teacher + "\n" +
                "======= Course Details End =======\n";
    }

    /**
     * 根据li元素解析Course信息
     * @param e
     */
    private boolean generateCourseByElement(Element e){
        Elements
                inputs_clazzId,
                inputs_courseId,
                as_url_detail,
                imgs_url_img,
                spans_course_name,
                ps_teacher
                        ;

        //选择clazzid元素
        inputs_clazzId = e.select("input.clazzId");
        //如果选择不到
        if(inputs_clazzId.size() == 0){
            Logger.log("loadCourseList : Course.generateCourseByElement Canot get inputs_clazzId Elements : size = 0");
            Logger.log("generateCourse Faild.");
            return false;
        }

        //选择courseId元素
        inputs_courseId = e.select("input.courseId");
        //如果选择不到
        if(inputs_courseId.size() == 0){
            Logger.log("loadCourseList : Course.generateCourseByElement Canot get inputs_courseId Elements : size = 0");
            Logger.log("generateCourse Faild.");
            return false;
        }

        //选择url_detail元素
        as_url_detail = e.getElementsByTag("a");
        //如果选择不到
        if(as_url_detail.size() == 0){
            Logger.log("loadCourseList : Course.generateCourseByElement Canot get as_url_detail Elements : size = 0");
            Logger.log("generateCourse Faild.");
            return false;
        }

        //选择url_img元素
        imgs_url_img = e.getElementsByTag("img");
        //如果选择不到
        if(imgs_url_img.size() == 0){
            Logger.log("loadCourseList : Course.generateCourseByElement Canot get imgs_url_img Elements : size = 0");
            Logger.log("generateCourse Faild.");
            return false;
        }

        //选择spans_course_name元素
        spans_course_name = e.select("span.course-name");
        //如果选择不到
        if(spans_course_name.size() == 0){
            Logger.log("loadCourseList : Course.generateCourseByElement Canot get spans_course_name Elements : size = 0");
            Logger.log("generateCourse Faild.");
            return false;
        }

        //选择ps_teacher元素
        ps_teacher = e.select("p.color3");
        //如果选择不到
        if(ps_teacher.size() == 0){
            Logger.log("loadCourseList : Course.generateCourseByElement Canot get ps_teacher Elements : size = 0");
            Logger.log("generateCourse Faild.");
            return false;
        }

        //选择完成，读取数据

        this.clazzId = inputs_clazzId.get(0).attr("value");
        this.courseId = inputs_courseId.get(0).attr("value");
        this.url_detail = as_url_detail.attr("href");
        this.url_img = imgs_url_img.get(0).attr("src");
        this.course_name = spans_course_name.get(0).attr("title");
        this.teacher = ps_teacher.get(0).attr("title");

        return true;
    }

        //构造

    public Course(Element element_li) {
        this.generateCourseByElement(element_li);
    }

    public Course(String clazzId, String courseId, String url_detail, String url_img, String name, String teacher) {
        this.clazzId = clazzId;
        this.courseId = courseId;
        this.url_detail = url_detail;
        this.url_img = url_img;
        this.course_name = name;
        this.teacher = teacher;
    }

        //Getter And Setter

    public String getClazzId() {
        return clazzId;
    }

    public void setClazzId(String clazzId) {
        this.clazzId = clazzId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getUrl_detail() {
        return url_detail;
    }

    public void setUrl_detail(String url_detail) {
        this.url_detail = url_detail;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public HomeWorkList getHomeWorkList() {
        return homeWorkList;
    }

    public void setHomeWorkList(HomeWorkList homeWorkList) {
        this.homeWorkList = homeWorkList;
    }
}
