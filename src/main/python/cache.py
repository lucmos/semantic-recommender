import utils
from constants import Dataset, ImportPath
from config import Config


class Cache:
    class Dataset:

        @staticmethod
        def read(dataset_name: Dataset):
            assert isinstance(dataset_name, Dataset)

            dimension = Config.get_instance().dimension()
            path = dataset_name.get_path(dimension)
            return utils.load_json(path)

    class JavaExport:

        @staticmethod
        def read(datasetName):
            i = Config.get_instance()

            path = ImportPath.get_path(
                datasetName.name, i.cluster_over(), i.dimension())
            print(path)
            return utils.load_json(path)


if __name__ == "__main__":
    a = Cache.JavaExport.read(Dataset.WIKIMID())
    # print(a)
    for i in a:
        print(i, len(a[i]))
