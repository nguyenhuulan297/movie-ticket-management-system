package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Tiện ích đọc/ghi file JSON.
 * Mọi ngoại lệ đều được ném dưới dạng RuntimeException để tiện xử lý.
 */
public class FileUtils {

    /**
     * Đọc toàn bộ nội dung file thành chuỗi.
     * Nếu file chưa tồn tại, tự động tạo file với nội dung mảng rỗng "[]"
     * thay vì ném lỗi ngay lúc khởi động app.
     */
    public static String readJsonFile(String path) {
        try {
            Path filePath = Paths.get(path);
            if (!Files.exists(filePath)) {
                ensureParentDirExists(filePath);
                Files.write(filePath, "[]".getBytes());
            }
            return new String(Files.readAllBytes(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Lỗi đọc file: " + path, e);
        }
    }

    public static void writeJsonFile(String path, String content) {
        try {
            Path filePath = Paths.get(path);
            ensureParentDirExists(filePath);
            Files.write(filePath, content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Lỗi ghi file: " + path, e);
        }
    }

    private static void ensureParentDirExists(Path filePath) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }
}