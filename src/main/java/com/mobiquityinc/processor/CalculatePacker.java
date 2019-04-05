package com.mobiquityinc.processor;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Package;
import com.mobiquityinc.utils.FileUtils;

import static com.mobiquityinc.constant.Constant.*;


public class CalculatePacker {

    private static Logger logger = Logger.getLogger(CalculatePacker.class.getName());

    public static String pack(String inputPath) throws APIException {

        StringBuilder result = new StringBuilder();

        new FileUtils().loadFile(inputPath)
                .stream().forEach(line -> {
            try {
                parsePackerDetails(line).entrySet().stream().
                        forEach(t -> {
                            result
                                    .append(getBestCombinationOfPacker(t.getKey(), t.getValue()))
                                    .append(System.getProperty("line.separator"));
                        });
            } catch (APIException e) {
                logger.log(Level.SEVERE, "Packing exception was thrown", e);
            }
        });

        return result.toString();
    }

    public static Map<Integer, List<Package>> parsePackerDetails(String line) throws APIException {

        Map<Integer, List<Package>> packageList = new HashMap<>();
        String[] splitted = line.split(":");
        final int maxWeight;
        if (splitted.length != 2) throw new APIException("Line must contain exactly one `:`", line);

        try {
            maxWeight = (int) (Double.parseDouble(splitted[0]) * 100);
        } catch (NumberFormatException e) {
            throw new APIException("Left side of `:` must be a number", e, line);
        }
        Pattern pattern = Pattern.compile(PACKAGE_REGEX);
        Matcher matcher = pattern.matcher(splitted[1]);
        int lastEnd = 0;
        List<Package> packages = new ArrayList<>();

        while (matcher.find()) {
            if (matcher.start() != lastEnd + 1 || splitted[1].charAt(lastEnd) != ' ') {
                throw new APIException(String.format("Right side of `:` must be in the following pattern (%s) separated by space", PACKAGE_REGEX), line);
            }

            try {
                Integer index = Integer.valueOf(matcher.group(INDEX));
                int weight = (int) (Double.valueOf(matcher.group(WEIGHT)) * 100);
                int cost = Integer.valueOf(matcher.group(COST));

                if (index > MAX_ITEMS_IN_LINE || index < 0) {
                    throw new APIException(String.format("Index mas be in range (1, %d)", MAX_ITEMS_IN_LINE), line);
                }

                if (weight > MAX_WEIGHT || weight < 0) {
                    throw new APIException(String.format("Weight mas be in range (0, %f)", MAX_WEIGHT), line);
                }

                if (cost > MAX_COST || cost < 0) {
                    throw new APIException(String.format("Cost mas be in range (0, %f)", MAX_COST), line);
                }

                packages.add(new Package(index, weight, cost));
            } catch (NumberFormatException | IllegalFormatConversionException e) {
                throw new APIException(e, line);
            }

            lastEnd = matcher.end();
        }

        if (lastEnd != splitted[1].length()) {
            throw new APIException("Unexpected characters in the end of the line", line);
        }


        long[] indexes = packages.stream()
                .mapToLong(Package::getIndex)
                .toArray();

        for (int i = 0; i < indexes.length; i++) {
            if (indexes[i] != i + 1) {
                throw new APIException("The indexes in not order well, or some index is missing", line);
            }
        }
        packageList.put(maxWeight, packages);
        return packageList;
    }


    public static String getBestCombinationOfPacker(int maxWeight, List<Package> packageList) {

        List<Package> packageOpt = packageList.stream()
                .filter(x -> x.getWeight() <= maxWeight)
                .collect(Collectors.toList());
        Collections.sort(packageOpt, Comparator.comparing(Package::getWeight));

        ArrayList<ArrayList<Package>> mainList = new ArrayList<ArrayList<Package>>();

        for (int i = 1, max = 1 << packageOpt.size(); i < max; ++i) {
            int totalWeight = 0;
            ArrayList<Package> arrayList = new ArrayList<>();

            for (int j = 0, k = 1; j < packageOpt.size(); ++j, k <<= 1)
                if ((k & i) != 0) {
                    totalWeight += packageOpt.get(j).getWeight();
                    if (totalWeight < maxWeight) {
                        arrayList.add(packageOpt.get(j));
                    }
                }
            mainList.add(arrayList);
        }


        HashMap<Integer, List<Package>> integerIntegerHashMap = new HashMap<>();

        mainList.stream().forEach(
                arrayList -> {
                    int sumWeight = arrayList.stream()
                            .mapToInt(Package::getWeight)
                            .sum();
                    if (sumWeight <= maxWeight) {
                        int sum = arrayList.stream()
                                .mapToInt(Package::getCost)
                                .sum();
                        integerIntegerHashMap.put(sum, arrayList);
                    }
                }
        );


        Map map = integerIntegerHashMap.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        Optional<Map.Entry<Integer, List<Package>>> optionalEntry = map.entrySet().stream().findFirst();

        List<Package> keyOfTheFirst = optionalEntry.isPresent() ? optionalEntry.get().getValue() : Collections.EMPTY_LIST;

        Collections.sort(keyOfTheFirst, Comparator.comparing(Package::getIndex));
        String result = keyOfTheFirst.stream()
                .map(t -> t.getIndex())
                .map(n -> n.toString())
                .collect(Collectors.joining(","));

        return result.isEmpty() ? "-" : result;
    }
}
