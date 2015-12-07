package ca.ualberta.t14.gametrader;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * This class generates a unique installation id that exist for this app's installation. This ID is written to
 * the apps folder and that very same file will be loaded to get the same ID. This ID only change to a new ID when that file "INSTALLATION" is removed.
 *
 * Ryan Satyabrata took this entire class from here
 * http://android-developers.blogspot.ca/2011/03/identifying-app-installations.html
 * as part of an answer from http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id/5626208#5626208
 */
public class InstallationIdGenerator {
    private static String sID = null;
    private static final String INSTALLATION = "INSTALLATION";

    public synchronized static String id(Context context) {
        if (sID == null) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists())
                    writeInstallationFile(installation);
                sID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }
}