package com.brook.example.utils.io;

import com.brook.example.utils.lang.Console;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时抓取远程文件
 * @author brook
 * @create 2017/7/7
 */
public class RemoteFileFetcher {

    private static final ScheduledExecutorService scheduledExecutorService = Executors
            .newSingleThreadScheduledExecutor(r -> new Thread(r, "RemoteFileFetcher_Schedule_Thread"));
    private byte[] fileConent;
    private String url;
    private long lastModified;
    private int connectTimeout;
    private int readTimeout;
    private FileChangeListener listener;

    private RemoteFileFetcher(String url, int reloadInterval, FileChangeListener listener) {
        this.connectTimeout = 1000;
        this.readTimeout = 1000;

        this.url = url;
        this.listener = listener;
        if (reloadInterval > 0) {
            scheduledExecutorService.scheduleWithFixedDelay(
                    RemoteFileFetcher.this::doFetch,
                    reloadInterval, reloadInterval, TimeUnit.MILLISECONDS);
        }
        doFetch();
    }

    private void doFetch() {
        if (url == null) {
            return;
        }
        Console.log("Begin fetch remote file... url = {}", this.url);
        try {
            URL target = new URL(this.url);
            this.fileConent = IOUtils.toByteArray((InputStream) target.getContent());
            this.lastModified = System.currentTimeMillis();
            if (this.listener != null && this.fileConent != null) {
                this.listener.fileReloaded(this.fileConent);
            }
        } catch (Exception e) {
            Console.error("read from url failed", e);
        }
    }

    public static RemoteFileFetcher createPeriodFetcher(String url,
            int reloadInterval,
            FileChangeListener listener) {

        return new RemoteFileFetcher(url, reloadInterval, listener);

    }

    public long getLastModified() {
        return this.lastModified;
    }

    public byte[] getFileByteArray() {
        return this.fileConent;
    }

    public interface FileChangeListener {
        void fileReloaded(byte[] contentBytes) throws IOException;
    }
}