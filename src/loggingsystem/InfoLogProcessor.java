package loggingsystem;

public class InfoLogProcessor extends LogProcessor{
    InfoLogProcessor(LogProcessor logProcessor) {
        super(logProcessor);
    }

    @Override
    public void log(int logLevel, String msg) {
        if(logLevel == LogProcessor.INFO){
            System.out.println("INFO: "+msg);
        }else{
            super.log(logLevel, msg);
        }
    }
}
