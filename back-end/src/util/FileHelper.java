package util;

import java.io.*;

/**
 * 文件操作帮助类。
 */
public class FileHelper {
    /**
     * 将指定字符串写到指定文件，如果文件或目录不存在则尝试自动创建。
     * @param filePath 要写入的文件的路径。
     * @param content 要写入的字符串内容。
     * @throws IOException 写入或创建文件错误时抛出。
     */
    public static void saveStringToFile(String filePath, String content) throws IOException {
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.close();
    }

    /**
     * 从指定文件读取所有字符串。
     * @param filePath 文件所在路径。
     * @return 读取的字符串。
     * @throws IOException 当读取发生错误时抛出。
     */
    public static String readStringFromFile(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
            File file = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
            reader.close();
        return contentBuilder.toString();
    }
}
