package com.finance.util.myutil;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件工具
 *
 * @author s1507112
 */
public final class FileUtil {

    /**
     * 保存文件
     *
     * @param file     文件
     * @param fileName 文件名
     */
    public static void saveFile(File file, String fileName) throws IOException {
        File savefile = new File(new File(BaseConstants.FILESAVEPATH), fileName);
        if (!savefile.getParentFile().exists()) {
            boolean isSuccess = savefile.getParentFile().mkdirs();
            if (isSuccess) {
                FileUtils.copyFile(file, savefile);
            }
        }
    }

    /**
     * 读文件
     *
     * @param fileName 文件名
     */
    public static InputStream readFile(String fileName) throws IOException {

        return new FileInputStream(new File(BaseConstants.FILESAVEPATH + fileName));
    }
}
