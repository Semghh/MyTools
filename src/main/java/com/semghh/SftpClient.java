package com.semghh;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;
import java.util.Vector;

public class SftpClient {

    ChannelSftp sftp;
    JSch jSch;
    Session session;
    public SftpClient(String username, String password, String host, int port) throws JSchException {

        jSch = new JSch();
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session = jSch.getSession(username, host, port);
        session.setConfig(config);
        session.setPassword(password);
        session.connect();
        sftp = (ChannelSftp) session.openChannel("sftp");
        sftp.connect();
    }


    public boolean mkdir(String path) throws SftpException {
        return mkdir(path, false);
    }

    /**
     * 创建dir
     *
     * @param path              路径
     * @param ifCreateNestedDir 是否连续创建嵌套的dir
     * @return 创建是否成功
     */
    public boolean mkdir(String path, boolean ifCreateNestedDir) throws SftpException {

        if (!ifCreateNestedDir) {
            sftp.mkdir(path);
            return true;
        }

        path = path.replace("\\", "/");
        String[] split = path.split("/");
        StringBuilder sb = new StringBuilder("/");
        for (int i = 0; i < split.length; i++) {
            sb.append(split[i]);
            try {
                sftp.cd(sb.toString());
            } catch (SftpException e) {
                if (e.id == 2) {
                    sftp.mkdir(sb.toString());
                } else {
                    throw e;
                }
            }
            sb.append("/");
        }
        return true;
    }


    /**
     * 删除指定目录的文件夹。可选是否 rm -rf
     * @param path 指定的目录
     * @param ifDeleteAll true表示删除嵌套的全部文件 即 rm -rf
     */
    public boolean rmdir(String path, boolean ifDeleteAll) throws SftpException {
        if (!ifDeleteAll){
            sftp.rmdir(path);
            return true;
        }

        Vector<ChannelSftp.LsEntry> ls = ls(path);

        for (ChannelSftp.LsEntry l : ls) {
            if (!l.getAttrs().isDir()) {
                sftp.rm(path+"/"+l.getFilename());
            }else if (l.getAttrs().isDir() && !(l.getFilename().equals(".") || l.getFilename().equals(".."))){
                //filter dir . and ..
                rmdir(path+"/"+l.getFilename(),true);
            }
        }
        sftp.rmdir(path);
        return true;
    }


    /**
     * 遍历ls
     *
     * @param path 指定路径
     */
    public Vector<ChannelSftp.LsEntry> ls(String path) throws SftpException {
        return sftp.ls(path);
    }

    public String pwd() throws SftpException {
        return sftp.pwd();
    }

    /**
     * 从远端拉取文件。
     * 可以直接 getFile("./a.txt" , String "./")   将远端当前目录下的a.txt 输出到,本项目根目录下的a.txt
     * @param remoteFilePath  源文件(远端文件)的地址
     * @param localFileDest  输出文件 (get到本地)的地址
     * @throws SftpException
     */
    public void getFileFromRemote(String remoteFilePath , String localFileDest) throws SftpException {
        sftp.get(remoteFilePath,localFileDest);
    }

    /**
     * 将本地文件 push到远端
     *
     * @param localFilePath  本地文件的Path
     * @param remoteFileDest 远端文件的dest
     */

    public void pushFileToRemote(String localFilePath,String remoteFileDest) throws SftpException {
        sftp.put(localFilePath,remoteFileDest);
    }


    public void closeAll() {
        sftp.disconnect();
        session.disconnect();
    }
}
