package spellcheck;

import ngram.NgramDictionary;
import ngram.NgramList;

import java.util.*;

class CandidateList implements Iterable<Map.Entry<String, Integer>> {
    private Map<String, Integer> candidates;

    CandidateList(NgramDictionary dictionary, NgramList source) {
        Integer[] candidateIds = null;
        for (String ngram : source) {
            candidateIds = getMerged(candidateIds, dictionary.get(ngram));
        }

        candidates = new HashMap<String, Integer>();

        if (candidateIds == null) return;

        for (int candidate : candidateIds) {
            String candidateWord = dictionary.getString(candidate);
            candidates.put(candidateWord, getLevenshteinDistance(source.getSource(), candidateWord));
        }
    }

    @Override
    public Iterator<Map.Entry<String, Integer>> iterator() {
        return candidates.entrySet().iterator();
    }

    boolean isEmpty() {
        return candidates.isEmpty();
    }

    void sort() {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(candidates.entrySet());

        // Sort the list
        list.sort(new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // Put data from sorted list to HashMap
        candidates = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            candidates.put(aa.getKey(), aa.getValue());
        }
    }

    private static Integer[] getMerged(Integer[] arr1, Integer[] arr2) {
        if (arr1 == null && arr2 == null) return null;
        if (arr1 == null) return arr2.clone();
        if (arr2 == null) return arr1.clone();

        Integer[] result = new Integer[arr1.length + arr2.length];
        int i = 0, j = 0, r = 0;
        while (i < arr1.length && j < arr2.length) {
            result[r++] = arr1[i] < arr2[j] ? arr1[i++] : arr2[j++];
        }

        // Copy the remaining elements in array 1 to result
        if (i < arr1.length) {
            System.arraycopy(arr1, i, result, r, (arr1.length - i));
        }

        // Copy the remaining elements in array 2 to result
        if (j < arr2.length) {
            System.arraycopy(arr2, j, result, r, (arr2.length - j));
        }

        return result;
    }

    private static int getLevenshteinDistance(String s1, String s2) {
        int insertCost = 1;
        int deleteCost = 1;
        int replaceCost = 1;

        Integer[] prev = new Integer[s2.length() + 1];
        Integer[] curr = new Integer[s2.length() + 1];

        for (int it = 0; it <= s2.length(); ++it) {
            curr[it] = it * insertCost;
        }

        for (int i = 1; i <= s1.length(); ++i) {
            prev = curr.clone();

            curr[0] = i * deleteCost;
            for (int j = 1; j <= s2.length(); ++j) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    int insert = prev[j] + insertCost;
                    int delete = curr[j - 1] + deleteCost;
                    int replace = prev[j - 1] + replaceCost;
                    curr[j] = Math.min(Math.min(insert, delete), replace);
                }
            }
        }

        return curr[curr.length - 1];
    }
}
