package io;

import properties.PropReader;
import utils.Chrono;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class TsvFileReader
{
    private TsvFileReader() {

    }

    /**
     * Methods that read a file text and returns it into an array which represents each line
     * with a string.
     *
     * @param path the file path
     * @return an ArrayList of string, where each String is a line of the read file
     */
    public static List<List<String>> readText(String path) {
        PropReader.Dimension limit = PropReader.getInstance().dimension();

        Chrono c = new Chrono(String.format("Reading %s...", path));
        List<List<String>> splitted_lines = new LinkedList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));

            Stream<String> lines = reader.lines();
            lines = limit.getSize() > 0 ? lines.limit(limit.getSize()) : lines;

            lines.forEach(line -> splitted_lines.add(Arrays.asList(line.split("\t"))));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        c.millis();
        return splitted_lines;
    }
}
