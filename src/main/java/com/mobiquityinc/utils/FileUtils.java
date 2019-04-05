package com.mobiquityinc.utils;

import com.mobiquityinc.exception.APIException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.util.List;

import java.util.stream.Collectors;

public class FileUtils {

    public  List<String> loadFile(String fileName) throws APIException {
        try {
            File file = new File(fileName);
           return Files.lines(file.toPath()).collect(Collectors.toList());
                  } catch (IOException ioe) {
            throw new APIException("Failed to read data file " + fileName);
        }
    }

}
