package kr.minimalest.core.domain.file;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public abstract class FileUtils {

    // png -> image/png
    public static String toImageContentType(String pureExt) {
        return "image/" + pureExt;
    }

    // http://example.com/file?key=image/my-key.png&position=abc.. -> image/my-key.png
    public static String extractKeyParameter(String fileUrl) {
        int keyStartIndex = fileUrl.indexOf("key=") + 4;
        int keyEndIndex = fileUrl.indexOf('&', keyStartIndex);

        return keyEndIndex == -1 ?
                fileUrl.substring(keyStartIndex) :
                fileUrl.substring(keyStartIndex, keyEndIndex);
    }

    // myImage.png -> image/{uuid}.png
    public static String createKey(String filename, String contentType) {
        String type = extractType(contentType);
        String ext = extractExt(filename);
        return keyResolver(type, ext);
    }

    // myImage.png -> image/{uuid}.png
    public static String createKey(MultipartFile multipartFile) {
        String type = extractType(multipartFile.getContentType());
        String ext = extractExt(multipartFile.getOriginalFilename());
        return keyResolver(type, ext);
    }

    // myImage.png -> png
    public static String extractPureExt(String filename) {
        int extStartIndex = filename.lastIndexOf('.');
        return filename.substring(extStartIndex + 1);
    }

    // myImage.png -> .png
    public static String extractExt(String filename) {
        int extStartIndex = filename.lastIndexOf('.');
        return filename.substring(extStartIndex);
    }

    // text/csv -> text, image/png -> image
    public static String extractType(String contentType) {
        int divIndex = contentType.indexOf('/');
        return contentType.substring(0, divIndex);
    }

    public static String getUuid() {
        return UUID.randomUUID().toString();
    }

    public static String keyResolver(String type, String ext) {
        String uuid = getUuid();
        return type + "/" + uuid + ext;
    }
}
