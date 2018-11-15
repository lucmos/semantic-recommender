from config import *


class MatrixPath:
    _FULL_MATRIX_CACHE = "cache/matrix_{}_{}_{}_maxdistance_{}_full.npz"
    _REDUCED_MATRIX_CACHE = "cache/matrix_{}_{}_{}_maxdistance_{}_dimensionality_{}.npz"
    _SVD_INSTANCE = "cache/reducer_of_{}_with_{}.joblib"

    SVD = "truncated_svd"

    @staticmethod
    def get_matrix_path(dataset):
        i = Config.get_instance()
        return MatrixPath._FULL_MATRIX_CACHE.format(dataset.name, i[CLUSTER_OVER], i[DIMENSION], i[MAX_USER_DISTANCE])

    @staticmethod
    def get_matrix_svd_path(dataset, reducer):
        i = Config.get_instance()
        reduced_matrix = MatrixPath._REDUCED_MATRIX_CACHE.format(
            dataset.name, i[CLUSTER_OVER], i[DIMENSION], i[MAX_USER_DISTANCE], i[MATRIX_DIMENSIONALITY])
        return reduced_matrix, MatrixPath._SVD_INSTANCE.format(reduced_matrix, reducer)


class JavaExportPath:
    _EXPORT_PATH = "results/java_export/export_{}_{}_{}.json"
    _EXPORT_CACHE = "cache/java_export/export_{}_{}_{}.pickle"

    @staticmethod
    def get_path(name, cluster_over, dimension):
        return JavaExportPath._EXPORT_PATH.format(name, cluster_over, dimension)

    @staticmethod
    def get_path_cache(name, cluster_over, dimension):
        return JavaExportPath._EXPORT_CACHE.format(name, cluster_over, dimension)


class Dimension:
    @staticmethod
    def COMPLETE():
        return Dimension("complete")

    @staticmethod
    def SMALL():
        return Dimension("small")

    def __init__(self, name):
        self.name = name


class Dataset:
    @staticmethod
    def WIKIMID():
        return Dataset("WIKIMID", "datasets/bin/wikimid_{}.bin")

    @staticmethod
    def S21():
        return Dataset("S21", "datasets/bin/S21_{}.bin")

    @staticmethod
    def S22():
        return Dataset("S22", "datasets/bin/S22_preferences_{}.bin")

    @staticmethod
    def S23():
        return Dataset("S23", "datasets/bin/S23_{}.bin")

    def __init__(self, name, path):
        self.name = name
        self.path = path

    # todo: usa config
    def get_path(self, dimension):
        return self.path.format(dimension)


class Clusters:
    @staticmethod
    def CLUSTERS():
        return Clusters("results/clusters_{}_{}_{}.json")

    def __init__(self, name):
        self.name = name

    # todo: usa config
    def get_path(self, cluster_over, cluster_method, dimension):
        return self.name.format(cluster_over, cluster_method, dimension)


if __name__ == "__main__":
    print(Dataset.WIKIMID().get_path("small"))
