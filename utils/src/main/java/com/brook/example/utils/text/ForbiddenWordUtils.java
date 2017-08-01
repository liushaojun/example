package com.brook.example.utils.text;

import com.brook.example.utils.io.RemoteFileFetcher;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 屏蔽关键词 工具类
 * @author brook
 * @create 2017/7/7
 */
@Log4j2
@UtilityClass
public class ForbiddenWordUtils {

    /**
     * 默认的遮罩文字
     */
    private static final String DEFAULT_MASK = "***";
    /**
     * 屏蔽关键词抓取的url
     */
    private static String forbiddenWordFetchURL;

    /**
     * 屏蔽关键词抓取时间间隔 毫秒
     */
    private static int reloadInterval = 60000; //10分钟

    /**
     * 屏蔽关键词
     */
    private static List<Pattern> forbiddenWords;

    public static void setForbiddenWordFetchURL(String forbiddenWordFetchURL) {
        ForbiddenWordUtils.forbiddenWordFetchURL = forbiddenWordFetchURL;
    }

    public static void setReloadInterval(int reloadInterval) {
        ForbiddenWordUtils.reloadInterval = reloadInterval;
    }

    /**
     * 替换input中的屏蔽关键词为默认的掩码
     *
     * @param input
     * @return
     */
    public static String replace(String input) {
        return replace(input, DEFAULT_MASK);
    }

    /**
     * 将屏蔽关键词 替换为 mask
     *
     * @param input
     * @param mask
     * @return
     */
    public static String replace(String input, String mask) {
        for (int i = 0, l = forbiddenWords.size(); i < l; i++) {
            Pattern forbiddenWordPattern = forbiddenWords.get(i);
            input = forbiddenWordPattern.matcher(input).replaceAll(mask);
        }
        return input;
    }


    /**
     * 是否包含屏蔽关键词
     *
     * @param input
     * @return
     */
    public static boolean containsForbiddenWord(String input) {
        for (int i = 0, l = forbiddenWords.size(); i < l; i++) {
            Pattern forbiddenWordPattern = forbiddenWords.get(i);
            if (forbiddenWordPattern.matcher(input).find()) {
                return true;
            }
        }
        return false;
    }


    static {
        InputStream is = null;
        try {
            String fileName = "forbidden.txt";
            is = ForbiddenWordUtils.class.getClassLoader().getResourceAsStream(fileName);
            byte[] fileCBytes;
            fileCBytes = IOUtils.toByteArray(is);
            ForbiddenWordUtils.loadForbiddenWords(fileCBytes);
        } catch (IOException e) {
            log.error("read forbidden file failed", e);
        } finally {
            IOUtils.closeQuietly(is);
        }

    }

    /**
     * 初始化远程抓取配置
     */
    public static void initRemoteFetch() {
        RemoteFileFetcher.createPeriodFetcher(
                forbiddenWordFetchURL,
                reloadInterval,
                fileConent -> ForbiddenWordUtils.loadForbiddenWords(fileConent));
    }

    private static void loadForbiddenWords(byte[] fileCBytes) throws IOException {
        Reader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileCBytes), Charsets.UTF_8));
            List<String> forbiddenWordsStrList = IOUtils.readLines(reader);
            forbiddenWords = Lists.newArrayList();
            for (int i = forbiddenWordsStrList.size() - 1; i >= 0; i--) {
                String forbiddenWord = forbiddenWordsStrList.get(i).trim();
                if (forbiddenWord.length() == 0 || forbiddenWord.startsWith("#")) {
                    continue;
                } else {
                    forbiddenWords.add(Pattern.compile(forbiddenWord));
                }
            }
        } catch (Exception e) {
            log.error("load forbidden words failed", e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

}