import HttpApi.CookieList;
import HttpApi.HttpMethod;
import HttpApi.HttpResponseBody;
import HttpApi.HttpUtil;
import Module.ChaoXin.ChaoXingFanYaModule;
import Module.ChaoXin.HomeWorkStatu;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Author
 */

public class Main {

    public static void main(String[] args){
        try {
//            mainFunction(args);
            testFunction(args);
//            test2Function(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mainFunction(String[] args) throws Exception {

        String uname = "15070914797";
        String pwd = "BayMax123456";
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedpwd = encoder.encodeToString(pwd.getBytes(StandardCharsets.UTF_8));

        String url = "http://passport2.chaoxing.com/fanyalogin";

        HttpUtil httpUtil = new HttpUtil(url,HttpMethod.POST);
        httpUtil.setParam("uname",uname);
        httpUtil.setParam("password",encodedpwd);
        httpUtil.setParam("t","true");

        HttpResponseBody httpResponseBody = httpUtil.connect();

        System.out.println(httpResponseBody.getRequestMethod());
        System.out.println(httpResponseBody.getResponseCode());
        System.out.println(httpResponseBody.getResponseMessage());
        System.out.println(httpResponseBody.readHeaderFields());
        System.out.println(httpResponseBody.getResponseContent());

        CookieList cookieList = httpResponseBody.generateCookieList();

        System.out.println(cookieList.toDetialString());
        System.out.println(cookieList.toRequestHeaderString());

        //获取课程列表
        System.out.println("=========== 获取课程列表 ===========");

        HttpUtil courseListHttpUtil = new HttpUtil("http://mooc2-ans.chaoxing.com/visit/courses/list", HttpMethod.GET);
        courseListHttpUtil.setRequestProerty("Cookie",cookieList.toRequestHeaderString());
        courseListHttpUtil.setRequestProerty("Host","mooc2-ans.chaoxing.com");
        courseListHttpUtil.setRequestProerty("Referer","http://mooc2-ans.chaoxing.com/visit/interaction?moocDomain=http://mooc1-1.chaoxing.com");
        courseListHttpUtil.setRequestProerty("X-Requested-With","XMLHttpRequest");

        HttpResponseBody courseListHttpResponseBody = courseListHttpUtil.connect();

        System.out.println(courseListHttpResponseBody.getRequestMethod());
        System.out.println(courseListHttpResponseBody.getResponseCode());
        System.out.println(courseListHttpResponseBody.getResponseMessage());
        System.out.println(courseListHttpResponseBody.readHeaderFields());
        System.out.println(courseListHttpResponseBody.getResponseContent());

        System.out.println("\n=========== Jsoup Start ===============\n");

        Document document = Jsoup.parse(courseListHttpResponseBody.getResponseContent());

        Elements courseinfoListElements
                = document.getElementsByClass("course-info");

        for(Element e : courseinfoListElements){
            Elements e_a = e.select("a");
            Elements e_a_span = e_a.get(0).getElementsByTag("span");
            System.out.println(e_a_span.get(0).attr("title"));
            System.out.println(e_a.get(0).attr("href"));
        }

        System.out.println("============ Testing workList ============");

        HttpUtil workList302HttpUtil1 = new HttpUtil("http://mooc1.chaoxing.com/visit/stucoursemiddle?courseid=221332754&clazzid=48429985&cpi=97857020&ismooc2=1",HttpMethod.GET);
        workList302HttpUtil1.setRequestProerty("Cookie",cookieList.toRequestHeaderString());
        HttpResponseBody workList302HttpResponseBody = workList302HttpUtil1.connect();
        System.out.println(workList302HttpResponseBody.readHeaderFields());
        System.out.println(workList302HttpResponseBody.getResponseContent());


    }



    public static void testFunction(String[] args) throws Exception {
        String uname = "15070914797";
        String pwd = "BayMax123456";

        ChaoXingFanYaModule chaoXingFanYaModule
                = new ChaoXingFanYaModule(uname,pwd);

        System.out.println("Login : " + chaoXingFanYaModule.login());
        System.out.println(chaoXingFanYaModule.showCookieDetailList());

        chaoXingFanYaModule.loadCourseList();
        System.out.println(chaoXingFanYaModule.showCourseDetailList());

        chaoXingFanYaModule.loadCourseHomeWorkList("221332754");

        System.out.println(chaoXingFanYaModule.getCourseList().get("221332754").getHomeWorkList().toDetailsString());


        System.out.println(chaoXingFanYaModule.getCookieList().toDetialString());
    }

    public static void test2Function(String[] args) throws Exception{
        System.out.println(HomeWorkStatu.WaitToReadOver);
        System.out.println(HomeWorkStatu.getStatuByString("已完成").name());
    }
}
