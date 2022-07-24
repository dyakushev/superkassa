import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Combinator {

    public static List<List<String>> findAllPossibleRowCombinations(List<List<String>> incomingLists, int arrayDimension) {
        //1. Group by binary combination
        Map<Integer, List<Integer>> combinationGroups = Combinator.groupByBinaryRepresentation(incomingLists);

        //2. Find all possible sum combinations between groups
        List<List<Integer>> combinations = Combinator.findAllPossibleSumCombinationsByGroup(combinationGroups, Combinator.countMaxSumNumber(arrayDimension));

        //3. Join lists according to combinations found
        return combinations.stream()
                //4. get indexes from groups and find all possible combinations between them
                .flatMap(l -> Combinator.findListsToJoinByGroup(l, combinationGroups).stream())
                //5. combine rows according to appropriate indexes
                .map(l -> Combinator.combineLists(l, incomingLists)).toList();
    }

    /**
     * 1. Represent each row in binary, 0 for null, and 1 for not null
     * 2. Transform binary into int and use it as key in a map
     * 3. If a key exists just add the value into existing list
     * <p>
     * Rows
     * a1 null a3 null --> row 0
     * ...
     * b1 null b3 null --> row 5
     * Should be in a same group, and Entry in a hashmap should be counted as:
     * 1010 = 10
     * Group:
     * [10] = [0,5]
     *
     * @return Map - groups by binary key
     */
    private static Map<Integer, List<Integer>> groupByBinaryRepresentation(List<List<String>> incomingLists) {
        Map<Integer, List<Integer>> groups = new HashMap<>();
        for (int i = 0; i < incomingLists.size(); i++) {
            //List of strings to String with 0 and 1 replacement accordingly
            String binaryString = incomingLists.get(i).stream().map(s -> Objects.isNull(s) ? "0" : "1").collect(Collectors.joining());

            //binary to decimal
            Integer binaryToInt = Integer.parseInt(binaryString, 2);
            if (groups.containsKey(binaryToInt)) {
                groups.get(binaryToInt).add(i);
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(i);
                groups.put(binaryToInt, list);
            }
        }
        return groups;
    }

    /**
     * Finds max value for a given dimension
     */
    private static int countMaxSumNumber(int arrayDimension) {
        return ((int) Math.pow(2, arrayDimension)) - 1;
    }


    /**
     * Gets array of ints and finds all the combinations of sums equal to desiredNumber
     * For [15]
     * it will be
     * <p>
     * [15],[8,4,2,1] and so on
     * Elements should not be duplicated
     * [4,4,4,2,1] is not allowed
     */
    private static List<List<Integer>> findAllPossibleSumCombinationsByGroup(Map<Integer, List<Integer>> groups, int desiredNumber) {
        List<Integer> groupsList = new ArrayList<>(groups.keySet());
        List<List<Integer>> allPossibleSumCombinationsList = new ArrayList<>();
        Collections.sort(groupsList);
        Combinator.findAllPossibleSumCombinationsByGroupBacktrack(allPossibleSumCombinationsList, new ArrayList<>(), groupsList, desiredNumber, 0);
        return allPossibleSumCombinationsList;
    }

    /**
     * Finds all possible combinations of sums
     */
    private static void findAllPossibleSumCombinationsByGroupBacktrack(List<List<Integer>> allPossibleSumCombinationsList, List<Integer> tempList, List<Integer> groupsList, int remain, int start) {
       if (remain == 0) allPossibleSumCombinationsList.add(new ArrayList<>(tempList));
        else if (remain>0){
            for (int i = start; i < groupsList.size(); i++) {
                if (!tempList.contains(groupsList.get(i))) {
                    tempList.add(groupsList.get(i));
                    Combinator.findAllPossibleSumCombinationsByGroupBacktrack(allPossibleSumCombinationsList, tempList, groupsList, remain - groupsList.get(i), i);
                    tempList.remove(tempList.size() - 1);
                }
            }
        }
    }

    /**
     * Finds all possible combinations between elements
     * <p>
     * For
     * [1,2], [3,4]
     * it will be
     * [1,3], [1,4], [2,3], [2,4]
     */
    private static List<List<Integer>> findListsToJoinByGroup(List<Integer> groupList, Map<Integer, List<Integer>> groups) {
        return Lists.cartesianProduct(groupList.stream().map(groups::get).toList());
    }

    /**
     * Combines lists, replaces nulls with values
     * For
     * a1   null a3    null
     * null b2   null  b4
     * it will be
     * a1   b2   c3    b4
     */
    private static List<String> combineLists(List<Integer> indexCombinations, List<List<String>> incomingLists) {
        //construct list of lists to combine
        List<List<String>> listsToCombine = indexCombinations.stream().map(incomingLists::get).toList();
        //if an incoming list is empty no need to continue
        if (listsToCombine.isEmpty()) return Collections.emptyList();
        //if we have 1 list we don't need to do anything
        if (listsToCombine.size() == 1) return listsToCombine.get(0);

        //construct list of iterators to combine
        List<Iterator<String>> iteratorList = new ArrayList<>();
        for (List<String> listToCombine : listsToCombine) {
            iteratorList.add(listToCombine.iterator());
        }

        List<String> combined = new ArrayList<>();
        boolean shouldIterate = true;
        StringBuilder sb = new StringBuilder();

        while (shouldIterate) {
            sb.setLength(0);
            for (Iterator<String> iterator : iteratorList) {
                if (!iterator.hasNext()) {
                    shouldIterate = false;
                } else {
                    String next = iterator.next();
                    if (!Strings.isNullOrEmpty(next)) sb.append(next);
                }
            }
            if (shouldIterate) combined.add(sb.toString());
        }
        return combined;
    }


}


