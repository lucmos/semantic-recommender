package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public static ArrayList<String> readText(String path,int  c)
    { // TODO: 15/10/18 togli c 
        ArrayList<String> lines = new ArrayList<String>();
        try
        {
            File f = new File(path);
            BufferedReader bReader = new BufferedReader(new FileReader(f));
            String readLine;

            while (((readLine = bReader.readLine()) != null)&& c>0)
            {
                lines.add(readLine);
                c--;
            }
            return lines;
        }
        catch (IOException e) {
            e.printStackTrace();}
            return lines;
    }


    /**
     * Methods that split the lines of a file text by a specified character (the default is tab)
     * @param lines
     * @return
     */
    public static ArrayList<ArrayList<String>> splitByChar(ArrayList<String> lines, char specialChar)
    {
        ArrayList<ArrayList<String>>res = new ArrayList<>();

        for (int i=0; i<lines.size(); i++)
        {
            String sentence= lines.get(i);
            String[] splittedSentences = sentence.split(""+specialChar);
            ArrayList<String>  splitSent = new ArrayList<>(Arrays.asList(splittedSentences));
            res.add(splitSent);
        }
//        System.out.println(res);
        return res;
    }

    public static ArrayList<ArrayList<String>> splitByChar(ArrayList<String> lines)
    {
        return TsvFileReader.splitByChar(lines, '\t');
    }

    // TODO: 15/10/18 cancella main
    public static void main(String[] args)
    {
        ArrayList<String> lines = readText("D://università//cache//SocialExtraction//WSIEProject/provaTesto.txt", 10);
//        System.out.println(lines.size());
        ArrayList<ArrayList<String>> splittedLines = splitByChar(lines);
        System.out.println(splittedLines);
        System.out.println(splittedLines.size());
    }


}
