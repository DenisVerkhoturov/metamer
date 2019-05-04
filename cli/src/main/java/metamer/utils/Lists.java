package metamer.utils;

import io.vavr.control.Option;

import java.util.List;

public class Lists {
    public static <T> Option<T> head(final List<T> list) {
        return list.isEmpty() ? Option.none() : Option.some(list.get(0));
    }
}
