package ngram;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NgramList implements Iterable<String> {
    private String source;
    private List<String> ngrams;

    public NgramList(String word, int n) {
        source = word;
        ngrams = new ArrayList<String>();
        for (int it = 0; it < word.length() - n + 1; ++it) {
            ngrams.add(word.substring(it, it + n));
        }
    }

    @Override
    public Iterator<String> iterator() {
        return ngrams.iterator();
    }

    public String getSource() {
        return source;
    }
}
