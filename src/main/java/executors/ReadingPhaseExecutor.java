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
        DatasetReader dReader = new DatasetReader();
        TsvFileReader tsvReader = new TsvFileReader();
        ArrayList<String> lines = tsvReader.readText(DatasetConstants.WIKIMID_FRIEND_BASED_DATASET.getPath());
        ArrayList<ArrayList<String>> splittedLines = tsvReader.splitByChar(lines);


//        dReader.addDataInDataset(wikimid, readedData, DatasetReader.DatasetType.WIKI_MID_FRIENDBASED_DATASET);

    }
}
