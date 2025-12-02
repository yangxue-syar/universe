package com.universe.crawler;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.Instant;

/**
 * 简单的图片下载示例（同步）；生产环境建议异步并上传到对象存储
 */
public class ImageDownloadUtil {
    private static final OkHttpClient http = new OkHttpClient();

    public static String downloadToLocal(String url, String folder) throws Exception {
        Request req = new Request.Builder().url(url).build();
        try (Response res = http.newCall(req).execute()) {
            if (!res.isSuccessful()) throw new RuntimeException("failed to download " + url);
            InputStream in = res.body().byteStream();
            String filename = folder + "/" + Instant.now().toEpochMilli() + "-" + Math.abs(url.hashCode()) + ".jpg";
            try (FileOutputStream out = new FileOutputStream(filename)) {
                IOUtils.copy(in, out);
            }
            return filename;
        }
    }
}