package xyz.ibudai.dailyword.oss.util;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import xyz.ibudai.dailyword.model.props.OssProps;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OssServer {

    private final OssProps ossProps;


    /**
     * Compress image.
     *
     * @param in  the in
     * @param out the out
     * @throws IOException the io exception
     */
    public void compress(InputStream in, OutputStream out) throws IOException {
        Thumbnails.of(in)
                // 压缩尺寸
                .scale(ossProps.getScale())
                // 压缩质量
                .outputQuality(ossProps.getQuality())
                // 输出格式
                .outputFormat("jpeg")
                .toOutputStream(out);
    }

    /**
     * Sign url.
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
        String key = filePath + expire + ossProps.getSecret();
        String secret = DigestUtils.md5DigestAsHex(key.getBytes());
        return ossProps.getHost()
                + "/" + bucket
                + "/" + filePath
                + "?" + String.format("expire=%s", expire)
                + "&" + String.format("secret=%s", secret);
    }

    /**
     * Sign avatar url.
     *
     * @param filePath the file path
     * @return the string
     */
    public String signAvatarUrl(String filePath) {
        return signUrl(filePath, ossProps.getAvatarDir(), TimeUnit.HOURS.toMillis(1));
    }
}
