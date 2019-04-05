package com.mobiquityinc.utils;

import com.mobiquityinc.exception.APIException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FileUtilsTest {


    private File file;

    @Before
    public void setUp() {
        ClassLoader classLoader = getClass().getClassLoader();
        file  = new File(classLoader.getResource("testData.txt").getFile());
    }

    @Test
    public void loadFileTest() throws  APIException {
       List<String> result = new FileUtils().loadFile(file.getAbsolutePath());
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

}
