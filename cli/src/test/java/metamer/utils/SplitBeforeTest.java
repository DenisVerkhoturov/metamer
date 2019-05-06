package metamer.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SplitBeforeTest {
    @Test
    @DisplayName("next() should return NoSuchElementException when there is empty  iterator")
    public void emptyListTest() {
        final Iterator<Object> iterator = Collections.emptyIterator();
        final SplitBefore<Object> chunks = new SplitBefore<>(iterator, symbol -> symbol.equals('|'));
        assertThat(chunks.hasNext(), is(false));
        assertThrows(NoSuchElementException.class, chunks::next);
    }

    @Test
    @DisplayName("next() should return \'a\' when list has only \'a\'")
    public void singleElementTest() {
        final Iterator<Character> iterator = List.of('a').iterator();
        final SplitBefore<Character> chunks = new SplitBefore<>(iterator, symbol -> symbol == '|');
        assertThat(chunks.next(), is(List.of('a')));
        assertThat(chunks.hasNext(), is(false));
    }

    @Test
    @DisplayName("next() should return \'|\' when list has only \'|\'")
    public void singleElementAsDelimiterTest() {
        final Iterator<Character> iterator = List.of('|').iterator();
        final SplitBefore<Character> chunks = new SplitBefore<>(iterator, symbol -> symbol == '|');
        assertThat(chunks.next(), is(List.of('|')));
        assertThat(chunks.hasNext(), is(false));
    }

    @Test
    @DisplayName("next() should return [\'|\', \'a\'] when list has\'|\' and \'a\'")
    public void delimiterAndCharacterTest() {
        final Iterator<Character> iterator = List.of('|', 'a').iterator();
        final SplitBefore<Character> chunks = new SplitBefore<>(iterator, symbol -> symbol == '|');
        assertThat(chunks.next(), is(List.of('|', 'a')));
        assertThat(chunks.hasNext(), is(false));
    }

    @Test
    @DisplayName("next() should return two lists when list has\'a\' and \'|\'")
    public void characterAndDelimiterTest() {
        final Iterator<Character> iterator = List.of('a', '|').iterator();
        final SplitBefore<Character> chunks = new SplitBefore<>(iterator, symbol -> symbol == '|');
        assertThat(chunks.next(), is(List.of('a')));
        assertThat(chunks.next(), is(List.of('|')));
        assertThat(chunks.hasNext(), is(false));
    }

    @Test
    @DisplayName("correct list should be returned when list consists of String")
    public void listOfStringTest() {
        final Iterator<String> iterator = List.of("|", "|abc", "abc").iterator();
        final SplitBefore<String> chunks = new SplitBefore<>(iterator, symbol -> symbol.startsWith("|"));
        assertThat(chunks.next(), is(List.of("|")));
        assertThat(chunks.next(), is(List.of("|abc", "abc")));
        assertThat(chunks.hasNext(), is(false));
    }

    @Test
    @DisplayName("correct list should be returned when list consists of Character")
    public void listOfCharacterTest() {
        final Iterator<Character> iterator = List.of('|', '|', 'a', '|', 'a', 'b', '|', 'a', 'b', 'c').iterator();
        final SplitBefore<Character> chunks = new SplitBefore<>(iterator, symbol -> symbol == '|');
        assertThat(chunks.next(), is(List.of('|')));
        assertThat(chunks.next(), is(List.of('|', 'a')));
        assertThat(chunks.next(), is(List.of('|', 'a', 'b')));
        assertThat(chunks.next(), is(List.of('|', 'a', 'b', 'c')));
        assertThat(chunks.hasNext(), is(false));
    }
}
