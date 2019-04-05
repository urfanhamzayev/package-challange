package com.mobiquityinc.processor;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Package;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CalculatePackerTest {

    private File file;
    private String inputFile = "";
    private String message ="";
    private List<Package> packageList;
    private int maxWeight ;

    @Before
    public void setUp(){
        ClassLoader classLoader = getClass().getClassLoader();
        file  = new File(classLoader.getResource("testData.txt").getFile());
        inputFile = file.getAbsolutePath();
        message = "75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)";
        maxWeight = 7500;
        packageList = Arrays.asList(new Package(1,8531,29),
                new Package(2,1455,74),
                new Package(3,398,16),
                new Package(4,2624,55),
                new Package(5,6369,52),
                new Package(6,7625,75),
                new Package(7,6002,74),
                new Package(8,9318,35),
                new Package(9,8995,78));


    }

    @Test
    public void packTest() throws APIException {
       String result = CalculatePacker.pack(inputFile);
        String[] lines = result.split(System.getProperty("line.separator"));
        assertEquals(2, lines.length);
        assertEquals("2,7",lines[0]);
        assertEquals("8,9",lines[1]);
    }

    @Test
    public void parsePackerDetailsTest() throws APIException{

        Map<Integer, List<Package>> packerDetails = CalculatePacker.parsePackerDetails(message );
        List<Package> actualList = packerDetails.get(7500);
        assertEquals(1,packerDetails.size());
        assertEquals(9,actualList.size());
        Collections.sort(packageList, Comparator.comparing(Package::getIndex));
        Collections.sort(actualList, Comparator.comparing(Package::getIndex));

        assertTrue(packageList.stream().map(Package::getIndex).collect(Collectors.toList())
                .equals(actualList.stream().map(Package::getIndex).collect(Collectors.toList())));
        assertTrue(packageList.stream().map(Package::getWeight).collect(Collectors.toList())
                .equals(actualList.stream().map(Package::getWeight).collect(Collectors.toList())));
        assertTrue(packageList.stream().map(Package::getCost).collect(Collectors.toList())
                .equals(actualList.stream().map(Package::getCost).collect(Collectors.toList())));
    }

    @Test
    public void getBestCombinationOfPacker(){
    String result = CalculatePacker.getBestCombinationOfPacker(maxWeight,packageList);
    assertEquals("2,7",result);
    }

}
