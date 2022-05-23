package bin.Logic;

import java.io.File;
import java.time.LocalDateTime;

public class Logger extends Stream {

    private static final String logPath = "src\\logs\\";
    private static Logger log;

    private Logger(){
        super(logPath + createLogName() + ".log");
    }
    public Logger(String path){
        super(path);
    }

    private static String createLogName(){
        String baseName = LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue() + "-" + LocalDateTime.now().getDayOfMonth() + "_";
        int index = chooseIndex(baseName, 1);
        return baseName+index;
    }
    private static int chooseIndex(String fileName, int i){
        File testingPath = new File(logPath + fileName + i + ".log");
        if(!testingPath.isFile()){
            return i;
        }else{
            return chooseIndex(fileName, i+1);
        }
    }

    public static void init(){
        log = new Logger();
    }
    public static void enterLog(String action){
        log.writeToFile(LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond() + " - " + action + "\n");
        log.save();
    }
    public static void close(){
        log.closeFile();
    }

    public void enter(String action){
        writeToFile(LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond() + " - " + action + "\n");
        save();
    }
    public void send(String message){
        writeToFile(message+"\n");
        save();
    }
}
