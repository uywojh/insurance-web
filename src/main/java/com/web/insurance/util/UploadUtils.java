package com.web.insurance.util;

import java.io.File;

public class UploadUtils {

    public final static String IMG_PATH_PREFIX = "static/upload/imgs";

    public static File getImgDirFile() {

        String fileDirPath = new String("src/main/resources/" + IMG_PATH_PREFIX);
        File fileDir = new File(fileDirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir;
    }
}