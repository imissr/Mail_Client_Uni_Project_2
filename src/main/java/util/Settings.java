package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public final class Settings implements Serializable{
    private static Settings instance;

    private Settings(){}

    public static Settings getInstance(){
        return instance == null ? (instance = new Settings()) : instance;
    }

    public String password;
    public boolean savePassword = false;
    public String username;

    public static void setInstance(Settings instance) {
        Settings.instance = instance;
    }


    public static void serializePassword(String password, boolean savePassword, String username) throws IOException{
        File file = new File("configurations.conf");
        if(!file.exists()){
            file.createNewFile();
        }

        Settings.getInstance().password = password;
        Settings.getInstance().savePassword = savePassword;
        Settings.getInstance().username = username;

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(Settings.getInstance());

        oos.close();
    }

    public static void deserializePassword() throws IOException, ClassNotFoundException{
        File file  = new File("configurations.conf");
        if(!file.exists()){
            return;
        }

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        setInstance((Settings)ois.readObject());
        ois.close();
    }
}