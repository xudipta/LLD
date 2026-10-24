package loggingsystem;

public abstract class LogProcessor {
    public static int DEBUG =1;
    public static int ERROR =2;
    public static int INFO =3;

    LogProcessor nextLogProcessor;
    LogProcessor(LogProcessor logProcessor){
        nextLogProcessor=logProcessor;
    }

    public void log(int logLevel,String msg){
        if(nextLogProcessor!=null){
            nextLogProcessor.log(logLevel,msg);
        }
    }

}
