package spellcheck;

import ngram.NgramDictionary;
import ngram.NgramList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpellChecker {
    private int N;
    private NgramDictionary dictionary;

    public SpellChecker(String path, int n) throws IOException {
        N = n;
        dictionary = new NgramDictionary(path, n);
    }

    public List<String> search(String word) {
        CandidateList candidates = new CandidateList(dictionary, new NgramList(word, N));
        List<String> bestCandidates = new ArrayList<String>();
        if (candidates.isEmpty()) return bestCandidates;

        int bestDist = -1;
        candidates.sort();
        for (Map.Entry<String, Integer> candidate : candidates) {
            String candidateWord = candidate.getKey();
            int candidateDist = candidate.getValue();

            if (bestCandidates.isEmpty()) {
                bestDist = candidateDist;
                bestCandidates.add(candidateWord);
                continue;
            }

            if (bestDist != candidateDist) {
                break;
            }

            bestCandidates.add(candidateWord);
        }

        return bestCandidates;
    }
}
