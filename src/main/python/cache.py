import utils
from constants import Dataset
from config import Config


class Cache:
    class Dataset:

        @staticmethod
        def read(dataset_name: Dataset):
            assert isinstance(dataset_name, Dataset)

            dimension = Config.get_instance().dimension()
            path = dataset_name.get_path(dimension)
            return utils.load_json(path)


if __name__ == "__main__":
    a = Cache.Dataset.read(Dataset.S21())

    for i in a:
        print(i)
