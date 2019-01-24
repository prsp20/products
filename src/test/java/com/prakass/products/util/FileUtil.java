package com.prakass.products.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.stream.Collectors;


public class FileUtil {

    public static String readFileFromClasspath(String path) {
        ClassLoader classLoader = FileUtil.class.getClassLoader();
        try {
            return IOUtils.readLines(classLoader.getResourceAsStream(path), Charset.defaultCharset())
                    .stream()
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
