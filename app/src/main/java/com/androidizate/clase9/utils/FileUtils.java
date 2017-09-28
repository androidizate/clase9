package com.androidizate.clase9.utils;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author Andres Oller
 */
public class FileUtils {

    Context context;

    public FileUtils(Context context) {
        this.context = context;
    }

    public void createTxtFile(String filename, String fileData) {
        try {
            FileOutputStream fileout = context.openFileOutput(filename, MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(fileData);
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
