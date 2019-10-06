package ngram;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NgramDictionary {
    private String[] dictionary;
    private Map<String, Integer[]> index;

    public NgramDictionary(String path, int n) throws IOException {
        dictionary = Files.lines(Paths.get(path)).sorted().toArray(String[]::new);

        Map<String, Integer> wordIndex = new HashMap<String, Integer>();
        for (int it = 0; it < dictionary.length; ++it){
            wordIndex.put(dictionary[it], it);
        }

        Map<String, List<Integer>> ngramIndex = new HashMap<>();

        for (String word : dictionary) {
            for (String ngram : new NgramList(word, n)) {
                ngramIndex.putIfAbsent(ngram, new ArrayList<Integer>());
                ngramIndex.get(ngram).add(wordIndex.get(word));
            }
        }

        index = new HashMap<String, Integer[]>();
        for (Map.Entry<String, List<Integer>> entry : ngramIndex.entrySet()) {
            index.put(entry.getKey(), entry.getValue().stream().distinct().sorted().toArray(Integer[]::new));
        }

    }

    public Integer[] get(String ngram) {
        return index.getOrDefault(ngram, new Integer[0]);
    }

    public String getString(int id) {
        return dictionary[id];
    }
}