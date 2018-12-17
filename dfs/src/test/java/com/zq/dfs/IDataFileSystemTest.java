package com.zq.dfs;

import com.zq.dfs.config.Configuration;
import com.zq.dfs.constants.FileSystemType;
import com.zq.dfs.constants.IFileSystemConstants;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: ZQDFS
 * @description:
 * @author: zhouqi1
 * @create: 2018-12-10 20:35
 **/
public class IDataFileSystemTest {

    @Test
    public void test1(){
        try{
            Configuration configuration = new Configuration();
            configuration.put(IFileSystemConstants.FILE_SYSTEM_TYPE, FileSystemType.MEMORY_PERSISTENCE.name());
            configuration.put(IFileSystemConstants.FS_STORAGE_ROOT_PATH, "E:\\dfs");
            IFileSystem fileSystem = IFileSystems.fileSystem(configuration);
            String path = "/user/zhouqi/test.txt";
            fileSystem.createFile(path);

            IFileOutputStream outputStream = fileSystem.append(path);
            outputStream.write("zhouqi is a good boy".getBytes());
            outputStream.close();

            IFileInputStream inputStream = fileSystem.open(path);
            int len = inputStream.available();
            byte[] context = new byte[len];
            inputStream.read(context);
            inputStream.close();

            System.out.println(new  String(context));

            String targetPath = "/user/zhouqi/test1.txt";
            fileSystem.rename(path, targetPath);

            fileSystem.delete(targetPath);
            System.out.println("asdasd-");

        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
