import utils
from constants import Dataset, JavaExportPath
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

        USER_CAT_COUNTER = "user2catdom_counter"
        CATEGORIES = "all_catdom"

        @staticmethod
        def read(datasetName):
            i = Config.get_instance()

            path = JavaExportPath.get_path(
                datasetName.name, i.cluster_over(), i.dimension())
            print(path)
            data = utils.load_json(path)

            key = Cache.JavaExport.USER_CAT_COUNTER
            data[key] = {x: y["counts"] for x, y in data[key].items()}
            return data, data[Cache.JavaExport.USER_CAT_COUNTER], data[Cache.JavaExport.CATEGORIES]


if __name__ == "__main__":
    a = Cache.JavaExport.read(Dataset.WIKIMID())
    # print(a)
    for i in a:
        print(i, len(a[i]))
