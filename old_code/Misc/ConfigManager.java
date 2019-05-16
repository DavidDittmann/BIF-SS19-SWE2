package Misc;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.BasicConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

public class ConfigManager{
    static ConfigManager _instance;
    static XMLConfiguration config;

    protected ConfigManager(){
        try {
            InputStream is = new FileInputStream("config.xml");
            config = new BasicConfigurationBuilder<>(XMLConfiguration.class).configure(new Parameters().xml()).getConfiguration();
            FileHandler fh = new FileHandler(config);
            fh.load(is);
        } catch (ConfigurationException cex) {
            cex.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigManager getInstance(){
        if(null == _instance){
            _instance = new ConfigManager();
        }
        return _instance;
    }

    private String getPropertyStr(String propertyName) {
        try {
            return config.getString(propertyName);
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
    public String getConfDatabaseConn() throws Exception {
        return getPropertyStr("DbConn");
    }


}
