package executors;

import constants.DatasetInfo;
import constants.DatasetName;
import datasetsreader.Dataset;
import datasetsreader.DatasetReader;
import io.CacheManager;

import javax.xml.crypto.Data;

public class ReadingPhaseExecutor
{
    public static void main(String[] args) {

        for (DatasetName name : DatasetName.values()) {
            Dataset d = DatasetReader.readDataset(name);
            System.out.println(d);
            System.out.println();
        }

//        CacheManager.writeToCache(name);
//
//        Dataset d = CacheManager.readFromCache(name);
//        System.out.println(d.getUsers().size());

//        Dataset d = DatasetReader.readDataset(DatasetName.S23);
//        Dataset wikimid = new Dataset("wikimid");
//        System.out.println(wikimid.toString());
//
//        DatasetReader dReader = new DatasetReader();
//        System.out.println(dReader.toString());
//
//        TsvFileReader tsvReader = new TsvFileReader();
//        System.out.println(tsvReader.toString());
//        System.out.println(DatasetInfo.WIKIMID_FRIEND_BASED_DATASET.getPath());
//
//        ArrayList<String> lines = tsvReader.readText(DatasetInfo.WIKIMID_FRIEND_BASED_DATASET.getPath(), 400000000);
        //        ArrayList<String> lines = tsvReader.readText("prova_friend_based_dataset.txt", 10);
//        System.out.println(lines.size());
//        System.out.println(lines.get(0));


//        ArrayList<ArrayList<String>> splittedLines = tsvReader.splitByChar(lines);


//        dReader.addDataInDataset(wikimid, readedData, DatasetReader.DatasetType.WIKI_MID_FRIENDBASED_DATASET);

    }
}
