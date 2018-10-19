package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class TsvFileReader
{
    private TsvFileReader() {

    }

    /**
     * Methods that read a file text and returns it into an array which represents each line
     * with a string.
     * @param path the file path
     * @return an ArrayList of string, where each String is a line of the read file
     */
    public static List<List<String>>  readText(String path, int limit) {
        List<List<String>> splitted_lines = new LinkedList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));

            Stream<String> lines = reader.lines();
            lines = limit > 0 ? lines.limit(limit) : lines;

            lines.forEach(line -> splitted_lines.add(Arrays.asList(line.split("\t"))));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Done reading: " + path);
        return splitted_lines;
    }

    public static List<List<String>>  readText(String path) {
        return readText(path, -1);
    }


}
