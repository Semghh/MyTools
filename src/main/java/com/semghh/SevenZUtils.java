package com.semghh;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class SevenZUtils {


    public static final String postfix = ".7z";

    public static final String separator = "/";

    public static void compress(String destPath, String destFileName, File... files) throws IOException {
        try (SevenZOutputFile out = new SevenZOutputFile(new File(getLegalDestName(destPath, destFileName)))) {
            for (File file : files) {
                recursion("", out, file);
            }
        }
    }

    /**
     * 解压缩
     * @param sourceSevenZFile 需要解压缩的7z源文件
     * @param dest 解压缩的目标位置。如果是文件夹，则会解压缩在文件夹内。如果是文件,则会在文件的同级目录下创建一个同名文件夹解压
     * @return 是否成功
     */
    public static boolean decompress(File sourceSevenZFile, File dest) {

        if (!checkLegalSourceSevenZFile(sourceSevenZFile)) {
            System.out.println("非法的源文件 : " + sourceSevenZFile);
            return false;
        }


        if (!dest.isDirectory()) {
            dest = new File(sourceSevenZFile.getParent(), getDecompressDirNameByFileName(sourceSevenZFile));
            dest.mkdir();
        }

        try (SevenZFile sevenZFile = new SevenZFile(sourceSevenZFile);) {
            SevenZArchiveEntry entry;
            while ((entry = sevenZFile.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                } else {
                    File cur = new File(dest, entry.getName());
                    File parent = cur.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    try (FileOutputStream out = new FileOutputStream(cur);) {
                        InputStream inputStream = sevenZFile.getInputStream(entry);
                        byte[] buff = new byte[8192];
                        int len;
                        while ((len = inputStream.read(buff)) != -1) {
                            out.write(buff, 0, len);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    /**
     * 查看解压缩的源文件是否合法
     * @param sourceSevenZFile 7z源文件
     * @return 是否合法
     */
    private static boolean checkLegalSourceSevenZFile(File sourceSevenZFile) {
        if (!sourceSevenZFile.exists()) {
            return false;
        }
        if (!sourceSevenZFile.isFile()) {
            return false;
        }
        return sourceSevenZFile.getName().endsWith(postfix);
    }

    /**
     * 获得同名文件夹的名称
     * @param sourceSevenZFile 7z源文件
     * @return get the same name of dir from source 7z file
     */
    private static String getDecompressDirNameByFileName(File sourceSevenZFile) {
        return sourceSevenZFile.getName().split(postfix)[0];
    }


    private static void recursion(String insidePath, SevenZOutputFile out, File file) throws IOException {
        String name = insidePath + SevenZUtils.separator + file.getName();//在7z中的路径+文件名字
        if (file.isFile()) {
            SevenZArchiveEntry entry = out.createArchiveEntry(file, name);
            out.putArchiveEntry(entry);
            out.write(Files.newInputStream(file.toPath()));
            out.closeArchiveEntry();
        } else if (file.isDirectory()) {
            for (File listFile : file.listFiles()) {
                recursion(name, out, listFile);
            }
        }
    }


    private static String getLegalDestName(String path, String fileName) {
        if (!fileName.endsWith(postfix)) {
            fileName += postfix;
        }
        return path + SevenZUtils.separator + fileName;
    }

    /**
     * 由于Windows分隔符的问题, File.separator 会出现Bug,所以需要把所有路径替换为 "/"
     * @param insidePath 文件内路径
     * @return Windows安全的路径
     */
    private static String makeSurePathSafe(String insidePath) {
        return insidePath.replaceAll("\\\\", "/");
    }


    public static SevenZCompressOption as7z(String destPath, String dest7zFileName) {
        return new SevenZCompressOption(destPath, dest7zFileName);
    }




    /**
     * not TheadSafe
     */
    public static class SevenZCompressOption {

        private SevenZOutputFile out;

        private boolean hasException = false;

        private Exception e;

        private SevenZCompressOption(String destPath, String destFileName) {
            try {
                out = new SevenZOutputFile(new File(getLegalDestName(destPath, destFileName)));
            } catch (IOException e) {
                hasException = true;
                this.e = e;
            }
        }

        public SevenZCompressOption addFile(File... files) {
            if (hasException) return this;
            addFile(".", files);
            return this;
        }

        public SevenZCompressOption addFile(String filePath) {
            if (hasException) return this;
            addFile(".", new File(filePath));
            return this;
        }

        public SevenZCompressOption addFile(String insidePath, File... files) {
            if (hasException) return this;
            insidePath = SevenZUtils.makeSurePathSafe(insidePath);
            try {
                for (File file : files) {
                    recursion(insidePath, out, file);
                }
            } catch (IOException e) {
                this.e = e;
                hasException = true;
            }
            return this;
        }

        public boolean isSuccess() {
            return !hasException;
        }

        public SevenZCompressOption done() {
            if (hasException) return this;
            try {
                out.close();
            } catch (IOException e) {
                this.e = e;
                this.hasException = true;
            }
            return this;
        }

        public Exception getException() {
            return e;
        }
    }

}
