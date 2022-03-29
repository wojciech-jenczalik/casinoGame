package pl.jenczalik.casinogame.domain.framework;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class SecureRandomStub extends SecureRandom {
    private Iterator<Integer> iterator;

    public void populateWithInts(Integer... ints) {
        List<Integer> list = Arrays.stream(ints).collect(Collectors.toList());
        iterator = list.iterator();
    }

    @Override
    public int nextInt(int bound) {
        if (isIteratorEmpty()) {
            throw new IllegalStateException("empty iterator in SecureRandomStub. Can not further generate integers");
        }
        return iterator.next();
    }

    private boolean isIteratorEmpty() {
        return !iterator.hasNext();
    }
}
