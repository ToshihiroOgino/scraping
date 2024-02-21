package scraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;

public class ParserTest {
    @Test
    void parserCanConvertURL() {
        Method method;
        try {
            method = Parser.class.getDeclaredMethod("convertURLtoPath", String.class);
            method.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
        Document nullDoc = null;
        try {
            Parser instance1 = new Parser(nullDoc, "aaa");
            assertEquals(
                    (String) method.invoke(instance1,
                            "https://s.yimg.jp/images/edit/202401/1/aaa.jpg"),
                    "aaa/https/s.yimg.jp/images/edit/202401/1/aaa.jpg");
            assertEquals(
                    (String) method.invoke(instance1, "a///b"),
                    "aaa/a/b");
            assertEquals(
                    (String) method.invoke(instance1, "a////b"),
                    "aaa/a/b");

            Parser instance2 = new Parser(nullDoc, "./a");
            assertEquals(
                    (String) method.invoke(instance2, "b"),
                    "./a/b");

            Parser instance3 = new Parser(nullDoc, "./a/");
            assertEquals(
                    (String) method.invoke(instance3, "b"),
                    "./a/b");
            assertEquals(
                    (String) method.invoke(instance3, "https://aaa"),
                    "./a/https/aaa");

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void parseMultiDupeImg() {
        try {
            // 同一ファイルを同時に複数ダウンロードする
            final String testHTML = "./src/test/resources/test.html";
            final String testOut = "./out_test";
            File file = Paths.get(testHTML).toFile();
            Document doc = Jsoup.parse(file);
            Parser parser = new Parser(doc, testOut);
            parser.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
