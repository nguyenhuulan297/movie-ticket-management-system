package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Tiện ích đọc/ghi file JSON.
 * Mọi ngoại lệ đều được ném dưới dạng RuntimeException để tiện xử lý.
 */

public class FileUtils {

    /**
     * Đọc toàn bộ nội dung file thành chuỗi.
     * @param path đường dẫn file
     * @return nội dung file dạng String
     * @throws RuntimeException nếu có lỗi IO
     */
    public static String readJsonFile(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException("Lỗi đọc file: " + path, e);
        }
    }

    /**
     * Ghi chuỗi JSON vào file.
     * @param path đường dẫn file
     * @param content nội dung JSON
     * @throws RuntimeException nếu có lỗi IO
     */
    public static void writeJsonFile(String path, String content) {
        try {
            Files.write(Paths.get(path), content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Lỗi ghi file: " + path, e);
        }
    }
}
