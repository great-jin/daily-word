package xyz.ibudai.dailyword.files.resouces;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.dailyword.model.base.ResponseData;
import xyz.ibudai.dailyword.model.props.FilesProps;

import java.io.File;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileResources {

    private final FilesProps filesProps;


    /**
     * Gets temp file.
     *
     * @param fileName the file name
     * @param bucket   the bucket
     * @param expire   the expiry
     * @param secret   the secret
     * @return the temp file
     */
    @GetMapping(value = "/{fileName:.+}")
    public ResponseEntity<?> getFile(@PathVariable String fileName,
                                     @RequestParam("bucket") String bucket,
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
        String key = fileName + expire + filesProps.getSecret();
        String receiveSecret = DigestUtils.md5DigestAsHex(key.getBytes());
        if (!secret.equalsIgnoreCase(receiveSecret)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ResponseData.denied("Permission denied"));
        }

        // 返回文件内容
        File file = new File(filesProps.getHome() + File.separator + bucket, fileName);
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
