package com.zq.dfs.data.storage;

import com.zq.dfs.IDirectory;
import com.zq.dfs.IFile;
import com.zq.dfs.INode;
import com.zq.dfs.IndexTables;
import com.zq.dfs.data.DataIndexTables;
import com.zq.dfs.data.IDataBlockFile;
import com.zq.dfs.data.IDataDirectory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.zq.dfs.data.storage.EditLog.EditLogType.*;

/**
 * @program: ZQDFS
 * @description: 存储目录
 * @author: zhouqi1
 * @create: 2018-12-12 19:39
 **/
public class LocatedDataIndexTables extends DataIndexTables implements IndexTables {

    private static final String INDEX_TABLES_STORAGE_DIRECTORY = "/tables";

    /**
     * 变更日志存储服务
     */
    private volatile EditLogFileStorage editLogStorage;

    /**
     * 目录镜像存储服务
     */
    private MirrorImageStorage mirrorImageStorage;

    private volatile boolean stop = false;

    private long lastCheckpointTime;

    private ScheduledExecutorService scheduledExecutorService;

    public LocatedDataIndexTables(String fsStorageRootPath) {
        editLogStorage = new EditLogFileStorage(fsStorageRootPath + INDEX_TABLES_STORAGE_DIRECTORY);
        mirrorImageStorage = new MirrorImageStorage(fsStorageRootPath + INDEX_TABLES_STORAGE_DIRECTORY);
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        checkpoint();
    }

    @Override
    public void put(String path, INode node) {
        //添加到变更日志文件中
        editLogStorage.storage(new EditLog(node.isDirectory() ? EDIT_DIRECTORY.value() : EDIT_FILE.value(), node.serialize()));

        //添加到目录
        super.put(path, node);
    }

    @Override
    public void remove(String path) {
        //添加到变更日志文件中
        editLogStorage.storage(new EditLog(DEL_PATH.value(), path.getBytes()));

        super.remove(path);
    }

    @Override
    public void close() {
        super.close();
        this.stop = true;
        editLogStorage.close();
        mirrorImageStorage.close();
        scheduledExecutorService.shutdownNow();
    }

    private void checkpoint(){
        lastCheckpointTime = System.currentTimeMillis();
        scheduledExecutorService.scheduleWithFixedDelay(()->{
            if(!stop && (editLogStorage.isStartCheckpoint()|| isCheckpointTime())){
                editLogStorage.startCheckpoint();
                DataIndexTables indexTables = mirrorImageStorage.load2IndexTables();
                while(editLogStorage.readable() > 0){
                    EditLog editLog = editLogStorage.acquire();
                    EditLog.EditLogType type = EditLog.EditLogType.toEnum(editLog.getType());
                    byte[] value = editLog.getValue();
                    switch (type){
                        case EDIT_DIRECTORY:
                            IDirectory directory = new IDataDirectory();
                            directory.deserialize(value);
                            indexTables.put(directory.path(), directory);
                            break;
                        case EDIT_FILE:
                            IFile file = new IDataBlockFile();
                            file.deserialize(value);
                            indexTables.put(file.path(), file);
                            break;
                        case DEL_PATH:
                            String path = new String(value);
                            indexTables.remove(path);
                            break;
                        default:
                            break;
                    }
                }
                mirrorImageStorage.storageIndexTables(indexTables);
                editLogStorage.endCheckpoint();
                lastCheckpointTime = System.currentTimeMillis();
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    private boolean isCheckpointTime() {
        return lastCheckpointTime - System.currentTimeMillis() > 5 * 60 * 1000;
    }
}
