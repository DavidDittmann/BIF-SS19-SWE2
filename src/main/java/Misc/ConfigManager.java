package Misc;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import java.util.NoSuchElementException;


/**
 * Configurations Manager zum laden und setzen von  configs.
 * Implementiert als Singleton. Die Instance ist über 'getInstance()' abholbar
 */
public class ConfigManager {
    static ConfigManager _instance;
    static XMLConfiguration config;
    FileBasedConfigurationBuilder<XMLConfiguration> builder;

    /**
     * Initialisierung des ConfigManagers --> Singleton
     */
    protected ConfigManager(){
        try {
            Configurations configs = new Configurations();
            builder = configs.xmlBuilder("config.xml");
            config = builder.getConfiguration();
        } catch (ConfigurationException cex) {
            cex.printStackTrace();
        }
    }

    /**
     * Singleton-Pattern -> Configmanager von überall aufrufbar
     * @return Die Instanz des ConfigManagers
     */
    public static ConfigManager getInstance(){
        if(null == _instance){
            _instance = new ConfigManager();
        }
        return _instance;
    }

    /**
     * Speichern der geänderten Configurationen
     * @throws ConfigurationException
     */
    public void saveConfig() throws ConfigurationException {
        builder.save();
    }

    private String getPropertyStr(String propertyName) {
        try {
            String tmp = config.getString(propertyName);
            return tmp;
        }
        catch(NoSuchElementException e){
            e.printStackTrace();
        }
        return null;
    }

    private int getPropertyInt(String propertyName){
        try {
            return config.getInt(propertyName);
        }
        catch(NoSuchElementException e){
            e.printStackTrace();
        }
        return -1;
    }

    private double getPropertyDouble(String propertyName){
        try {
            return config.getDouble(propertyName);
        }
        catch(NoSuchElementException e){
            e.printStackTrace();
        }
        return -1;
    }

    private void setPropertyString(String PropertyName, String str){
        config.setProperty(PropertyName,str);
    }

    private void setPropertyDouble(String PropertyName, double i){
        config.setProperty(PropertyName,i);
    }

    public String getConfDatabaseConn() throws Exception {
        return getPropertyStr("DataBaseConnection");
    }

    public String getImageBaseFolder() throws Exception {
        return getPropertyStr("ImageBase");
    }

    public double getStartWidth() throws Exception {
        return getPropertyDouble("StartWidth");
    }

    public double getStartHeight() throws Exception {
        return getPropertyDouble("StartHeight");
    }

    public void setStartWidth(double i){
        setPropertyDouble("StartWidth",i);
    }

    public void setStartHeight(double i){
        setPropertyDouble("StartHeight",i);
    }
}
