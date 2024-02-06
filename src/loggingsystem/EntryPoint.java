package loggingsystem;

public class EntryPoint {

    public static void main(String[] args) {
        LogProcessor log = new InfoLogProcessor(new DebugLogProcessor(new ErrorLogProcessor(null)));
        log.log(LogProcessor.DEBUG,"There is a debug message");
        log.log(LogProcessor.ERROR,"There is a error message");
        log.log(LogProcessor.INFO,"There is a info message");
    }


}
