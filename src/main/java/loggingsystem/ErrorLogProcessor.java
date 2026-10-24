package loggingsystem;

public class ErrorLogProcessor extends LogProcessor{
    ErrorLogProcessor(LogProcessor logProcessor) {
        super(logProcessor);
    }

    @Override
    public void log(int logLevel, String msg) {
        if(logLevel == LogProcessor.ERROR){
            System.out.println("ERROR: "+msg);
        }else{
            super.log(logLevel, msg);
        }
    }
}
