package ca.ualberta.t14.gametrader;

import java.io.FileOutputStream;

/**
 * Created by dubsy on 11/1/2015.
 */
public class FileIo {
    public FileIo (){}

    public void saveJson(Object obj, String filename){
        FileOutputStream fos = new FileOutputStream();
    }

    public Object loadJson(Class<?> objectClass, String filename){
        return new Object();
    }
}
