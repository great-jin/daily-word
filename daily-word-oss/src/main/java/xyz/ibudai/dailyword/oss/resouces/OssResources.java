package xyz.ibudai.dailyword.oss.resouces;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.base.ResponseData;
import xyz.ibudai.dailyword.model.props.OssProps;

import java.io.File;

@RestController
@RequestMapping("/api/oss")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OssResources {

    private final OssProps ossProps;


    /**
     * Gets temp file.
     *
     * @param bucket   the bucket
     * @param fileName the file name
     * @param expire   the expiry
     * @param secret   the secret
     * @return the temp file
     */
    @GetMapping(value = "/{bucket}/{fileName:.+}")
    public ResponseEntity<?> getFile(@PathVariable String bucket,
                                     @PathVariable String fileName,
                                     @RequestParam(required = false) Long expire,
                                     @RequestParam(required = false) String secret) {
        if (expire == null || secret == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ResponseData.denied("Permission denied"));
        }
        // 过期校验
        if (System.currentTimeMillis() > expire) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ResponseData.denied("Resource expired"));
        }
        // 签名校验
        String key = fileName + expire + ossProps.getSecret();
        String receiveSecret = DigestUtils.md5DigestAsHex(key.getBytes());
        if (!secret.equalsIgnoreCase(receiveSecret)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ResponseData.denied("Permission denied"));
        }

        // 返回文件内容
        File file = new File(ossProps.getHome() + File.separator + bucket, fileName);
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseData.failed("Resource not exists"));
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + fileName + "\"")
                .contentType(MediaTypeFactory.getMediaType(fileName).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(new FileSystemResource(file));
    }
}
