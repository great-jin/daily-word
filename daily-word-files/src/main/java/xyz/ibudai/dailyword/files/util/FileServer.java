package xyz.ibudai.dailyword.files.util;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import xyz.ibudai.dailyword.model.props.FilesProps;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileServer {

    private final FilesProps filesProps;


    /**
     * Compress.
     *
     * @param in  the in
     * @param out the out
     * @throws IOException the io exception
     */
    public void compress(InputStream in, OutputStream out) throws IOException {
        Thumbnails.of(in)
                // 压缩尺寸
                .scale(filesProps.getScale())
                // 压缩质量
                .outputQuality(filesProps.getQuality())
                // 输出格式
                .outputFormat("jpeg")
                .toOutputStream(out);
    }

    /**
     * Sign url string.
     *
     * @param filePath    the file path
     * @param bucket      the bucket
     * @param expireMills the expiry mills
     * @return the string
     */
    public String signUrl(String filePath, String bucket, Long expireMills) {
        if (Objects.isNull(expireMills)) {
            throw new IllegalArgumentException("expire mills must not be null");
        }

        long expire = System.currentTimeMillis() + expireMills;
        String key = filePath + expire + filesProps.getSecret();
        String secret = DigestUtils.md5DigestAsHex(key.getBytes());
        return filesProps.getHost()
                + "/" + filePath
                + "?" + String.format("bucket=%s", bucket)
                + "&" + String.format("expire=%s", expire)
                + "&" + String.format("secret=%s", secret);
    }
}
