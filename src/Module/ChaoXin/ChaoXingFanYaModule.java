package Module.ChaoXin;

import HttpApi.CookieList;
import HttpApi.HttpMethod;
import HttpApi.HttpResponseBody;
import HttpApi.HttpUtil;
import Tool.Logger;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ChaoXingFanYaModule {

    //网址路径常量
    private static final String url_Login = "http://passport2.chaoxing.com/fanyalogin";
    private static final String url_CourseList = "http://mooc2-ans.chaoxing.com/visit/courses/list";
    private static final String url_CourseHomeWork = "https://mooc1.chaoxing.com";

    private String uname = "";
    private String pwd = "";
    private boolean login = false;
    private CookieList cookieList = new CookieList();
    private CourseList courseList = new CourseList();

        //Custom Methods

    /**
     * 登入并获取cookie
     * @return
     * @throws Exception
     */
    public boolean login() throws Exception {

        //用户名或密码为空
        if(uname.equals("") || pwd.equals("")){
            Logger.log("ChaoXing Login ERROR : uname or pwd are null");
            Logger.log("ChaoXing Login Faild.");
            return false;
        }

        //base64加密密码
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedpwd = encoder.encodeToString(pwd.getBytes(StandardCharsets.UTF_8));

        //初始化Http链接模块
        HttpUtil httpUtil = new HttpUtil(url_Login, HttpMethod.POST);

        //设置表单参数
        httpUtil.setParam("uname",uname);
        httpUtil.setParam("password",encodedpwd);
        httpUtil.setParam("t","true");//如果没有这个参数，则密码无需加密

        //发送请求，获取响应体
        HttpResponseBody httpResponseBody = httpUtil.connect();

        //判断http请求状态码，200为正常，其他为异常
        if(httpResponseBody.getResponseCode() != 200){
            Logger.log("ChaoXing Login ERROR : HttpResponseCode " + httpResponseBody.getResponseCode());
            Logger.log("ChaoXing Login Faild.");
            return false;
        }

        //判断登入成功与否
        JSONObject jsonObject = JSONObject.parseObject(httpResponseBody.getResponseContent());
        //返回的json数据中如不包含status键值（可能是http异常）或者status为false（账号密码错误）
        if(!(jsonObject.containsKey("status") || jsonObject.getBoolean("status"))){
            Logger.log("ChaoXing Login ERROR : " + jsonObject.getString("msg2"));
            Logger.log("ChaoXing Login Faild.");
            return false;
        }

        //如果登入成功，则设置登入状态为true，并解析cookie列表
        if(jsonObject.getBoolean("status")){
            this.login = true;
            this.cookieList = httpResponseBody.generateCookieList();
        }

        return this.login;
    }

    /**
     * 加载课程列表
     * @throws Exception
     */
    public void loadCourseList() throws Exception {

        //开始之前先判断一下是否登入
        if(!this.login){
            Logger.log("loadCourseList : Not Login yet");
            Logger.log("ChaoXing LoadingCourseList Faild.");
            return;
        }

        //初始化Http模块
        HttpUtil httpUtil = new HttpUtil(url_CourseList, HttpMethod.GET);
        httpUtil.setRequestProerty("Cookie",cookieList.toRequestHeaderString());
        httpUtil.setRequestProerty("Host","mooc2-ans.chaoxing.com");
        httpUtil.setRequestProerty("Referer","http://mooc2-ans.chaoxing.com/visit/interaction?moocDomain=http://mooc1-1.chaoxing.com");
        httpUtil.setRequestProerty("X-Requested-With","XMLHttpRequest");

        //开始链接
        HttpResponseBody httpResponseBody = httpUtil.connect();

        //判断http请求状态码，200为正常，其他为异常
        if(httpResponseBody.getResponseCode() != 200){
            Logger.log("ChaoXing LoadingCourseList ERROR : HttpResponseCode " + httpResponseBody.getResponseCode());
            Logger.log("ChaoXing LoadingCourseList Faild.");
            return;
        }

        //更新cookie
        this.cookieList.upDateCookieList(httpResponseBody.generateCookieList());

        //Jsoup 解析
        Document document = Jsoup.parse(httpResponseBody.getResponseContent());
        //解析CourseList
        this.courseList = new CourseList(document);
    }

    /**
     * 加载一门课程的作业列表
     * @param courseId
     */
    public void loadCourseHomeWorkList(String courseId) throws Exception {
        //开始之前先判断一下是否登入
        if(!this.login){
            Logger.log("loadCourseList : Not Login yet");
            Logger.log("ChaoXing LoadingCourseList Faild.");
            return;
        }

        //获取指定课程
        Course course = this.courseList.get(courseId);
        if(course == null){
            Logger.log("loadCourseHomeWorkList : courseId : " + courseId + " donot exidt");
            return;
        }

        //初始化Http模块
        HttpUtil httpUtil_CourseInfoPage = new HttpUtil(course.getUrl_detail(),HttpMethod.GET);
        httpUtil_CourseInfoPage.setRequestProerty("Cookie",cookieList.toRequestHeaderString());

        //开始链接
        HttpResponseBody httpResponseBody_CourseInfoPage = httpUtil_CourseInfoPage.connect();

        //判断http请求状态码，200为正常，其他为异常
        if(httpResponseBody_CourseInfoPage.getResponseCode() != 200){
            Logger.log("ChaoXing LoadingWorkList ERROR : HttpResponseCode " + httpResponseBody_CourseInfoPage.getResponseCode());
            Logger.log("ChaoXing LoadingWorkList Faild.");
            return;
        }

        //更新cookie
        this.cookieList.upDateCookieList(httpResponseBody_CourseInfoPage.generateCookieList());

        //由于从课程列表页面到作业详情页面中间有一层跳转（课程详情页面）
        //故需要先解析课程详情页面 找到作业列表页面的地址

        //Jsoup 解析课程详情页面
        Document document_CourseInfo = Jsoup.parse(httpResponseBody_CourseInfoPage.getResponseContent());

        Elements a_homework_url = document_CourseInfo.select("a[title=\"作业\"]");
        //如果未获取到
        if(a_homework_url.size() == 0){
            Logger.log("loadCourseHomeWorkList : a_homework_url not fount");
            Logger.log("loadCourseHomeWorkList Faild");
            return;
        }
        //获取作业列表二级路径
        String url_path = a_homework_url.get(0).attr("data");

        //初始化Http模块
        HttpUtil httpUtil_CourseHomeWork = new HttpUtil(url_CourseHomeWork + url_path,HttpMethod.GET);
        httpUtil_CourseHomeWork.setRequestProerty("Cookie",cookieList.toRequestHeaderString());

        //开始链接
        HttpResponseBody httpResponseBody_CourseHomeWork = httpUtil_CourseHomeWork.connect();

        //判断http请求状态码，200为正常，其他为异常
        if(httpResponseBody_CourseHomeWork.getResponseCode() != 200){
            Logger.log("ChaoXing LoadingWorkList ERROR : HttpResponseCode " + httpResponseBody_CourseHomeWork.getResponseCode());
            Logger.log("ChaoXing LoadingWorkList Faild.");
            return;
        }

        //更新cookie
        this.cookieList.upDateCookieList(httpResponseBody_CourseHomeWork.generateCookieList());

        //Jsoup 解析课程详情页面
        Document document_CourseHomeWork = Jsoup.parse(httpResponseBody_CourseHomeWork.getResponseContent());

        //解析作业列表并传入课程对象
        course.setHomeWorkList(new HomeWorkList(document_CourseHomeWork));
    }

    /**
     * 获取cookie列表详细信息 一般调试用
     * @return
     */
    public String showCookieDetailList(){
        return this.cookieList.toDetialString();
    }

    /**
     * 获取course列表详细信息 一般调试用
     * @return
     */
    public String showCourseDetailList(){
        return this.courseList.toDetailsString();
    }

        //构造

    public ChaoXingFanYaModule() {
    }

    public ChaoXingFanYaModule(String uname, String pwd) {
        this.uname = uname;
        this.pwd = pwd;
    }

        //Getter AND Setter

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public CookieList getCookieList() {
        return cookieList;
    }

    public void setCookieList(CookieList cookieList) {
        this.cookieList = cookieList;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public CourseList getCourseList() {
        return courseList;
    }

    public void setCourseList(CourseList courseList) {
        this.courseList = courseList;
    }
}
