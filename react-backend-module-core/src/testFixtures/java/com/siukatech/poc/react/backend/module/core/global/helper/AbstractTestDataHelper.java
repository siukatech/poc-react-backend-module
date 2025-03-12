package com.siukatech.poc.react.backend.module.core.global.helper;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractTestDataHelper {

    protected File getResourceFile(String subDir, String resourceName) {
        Path resourceFilePath = Paths.get("src", "test", "resources", subDir, resourceName);
        return resourceFilePath.toFile();
    }

    protected File getResourceFile(String resourceName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        return file;
    }

}
