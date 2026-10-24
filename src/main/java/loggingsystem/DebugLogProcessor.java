package loggingsystem;

public class DebugLogProcessor extends LogProcessor{
    DebugLogProcessor(LogProcessor logProcessor) {
        super(logProcessor);
    }
    @Override
    public void log(int logLevel, String msg) {
        if(logLevel == LogProcessor.DEBUG){
            System.out.println("DEBUG: "+msg);
        }else{
            super.log(logLevel, msg);
        }
    }
}
