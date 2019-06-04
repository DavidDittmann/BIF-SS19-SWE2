package Misc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Logging {
    public static void LogInfo(Class c, String msg){
        Logger LOG = LogManager.getLogger(c);
        LOG.info(msg);
    }
    public static void LogDebug(Class c,String msg){
        Logger LOG = LogManager.getLogger(c);
        LOG.debug(msg);
    }
    public static void LogWarn(Class c,String msg){
        Logger LOG = LogManager.getLogger(c);
        LOG.warn(msg);
    }
    public static void LogError(Class c,String msg){
        Logger LOG = LogManager.getLogger(c);
        LOG.error(msg);
    }
    public static void LogFatal(Class c,String msg){
        Logger LOG = LogManager.getLogger(c);
        LOG.fatal(msg);
    }
}
