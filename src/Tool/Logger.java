package Tool;

import java.util.Date;

public class Logger {
    //需要输出调试内容时，修改为true
    private static final boolean DEBUG = false;

    public static void debug(String s){
        if(DEBUG){
            System.out.println("[DEBUG] " + s);
        }
    }

    public static void log(String s){
        System.out.println((new Date()) + " [INFO]: " + s);
    }
}
