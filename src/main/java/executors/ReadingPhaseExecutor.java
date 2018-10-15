package executors;

import constants.DatasetConstants;
import datasetsreader.Dataset;
import datasetsreader.DatasetReader;
import utils.TsvFileReader;

import java.util.ArrayList;

public class ReadingPhaseExecutor
{
    public static void main(String[] args){
        Dataset wikimid = new Dataset("wikimid");
        System.out.println(wikimid.toString());

        DatasetReader dReader = new DatasetReader();
        System.out.println(dReader.toString());

        TsvFileReader tsvReader = new TsvFileReader();
        System.out.println(tsvReader.toString());
        System.out.println(DatasetConstants.WIKIMID_FRIEND_BASED_DATASET.getPath());

        ArrayList<String> lines = tsvReader.readText(DatasetConstants.WIKIMID_FRIEND_BASED_DATASET.getPath(), 40000000);
//        ArrayList<String> lines = tsvReader.readText("prova_friend_based_dataset.txt", 10);
        System.out.println(lines.size());
        System.out.println(lines.get(0));


//        ArrayList<ArrayList<String>> splittedLines = tsvReader.splitByChar(lines);


//        dReader.addDataInDataset(wikimid, readedData, DatasetReader.DatasetType.WIKI_MID_FRIENDBASED_DATASET);

    }
}
