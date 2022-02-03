package Module.ChaoXin;

public enum HomeWorkStatu {
    WaitToDo("待做"),
    WaitToReadOver("待批阅"),
    Finished("已完成"),
    Error("ERROR");

    private String str;

    HomeWorkStatu(String str){
        this.str = str;
    }

    /**
     * 根据文本返回状态枚举
     * @param s
     * @return
     */
    public static HomeWorkStatu getStatuByString(String s){
        switch (s){
            case "待做":{
                return WaitToDo;
            }
            case "待批阅":{
                return WaitToReadOver;
            }
            case "已完成":{
                return Finished;
            }
            default:{
                return Error;
            }
        }
    }

    @Override
    public String toString(){
        return this.str;
    }

}
