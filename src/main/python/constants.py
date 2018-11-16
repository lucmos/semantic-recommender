import os
from config import *


class MatrixPath:
    _FULL_MATRIX_CACHE = "cache/full/full_matrix_{}_{}_{}_maxdistance_{}.npz"
    _INDEXES_CACHE = "cache/full/indexes_of_{}.joblib"
    _REDUCED_MATRIX_CACHE = "cache/reduced/reduced_{}_with_dimensionality_{}.joblib"
    _SVD_INSTANCE = "cache/reduced/reducer_of_{}_with_{}.joblib"

    @staticmethod
    def get_full_matrix_path():
        i = Config.get_instance()
        return MatrixPath._FULL_MATRIX_CACHE.format(i[DATASET], i[CLUSTER_OVER], i[DIMENSION], i[MAX_USER_DISTANCE])

    @staticmethod
    def get_full_matrix_name():
        return os.path.basename(MatrixPath.get_full_matrix_path())

    @staticmethod
    def get_indexes_path():
        return MatrixPath._INDEXES_CACHE.format(MatrixPath.get_full_matrix_name())

    @staticmethod
    def get_matrix_svd_path():
        i = Config.get_instance()

        full_matrix = MatrixPath.get_full_matrix_name()
        reduced_matrix = MatrixPath._REDUCED_MATRIX_CACHE.format(
            full_matrix, i[MATRIX_DIMENSIONALITY])
        return reduced_matrix, MatrixPath._SVD_INSTANCE.format(os.path.basename(reduced_matrix), i[REDUCER])


class JavaExportPath:
    _EXPORT_PATH = "results/java_export/export_{}_{}_{}.json"
    _EXPORT_CACHE = "cache/java_export/{}.pickle"

    @staticmethod
    def get_path():
        i = Config.get_instance()
        return JavaExportPath._EXPORT_PATH.format(i[DATASET], i[CLUSTER_OVER], i[DIMENSION])

    @staticmethod
    def get_path_cache():
        path = os.path.basename(JavaExportPath.get_path())
        return JavaExportPath._EXPORT_CACHE.format(path)


class Clusters:
    @staticmethod
    def CLUSTERS():
        return Clusters("results/clusters_{}_clusterized_with_{}.json")

    def __init__(self, name):
        self.name = name

    # todo: usa config
    def get_path(self):
        i = Config.get_instance()
        m, _ = MatrixPath.get_matrix_svd_path()
        # ! todo to change
        return self.name.format(os.path.basename(m), None)


if __name__ == "__main__":
    a = MatrixPath.get_full_matrix_path()
    b, c = MatrixPath.get_matrix_svd_path()
    d = JavaExportPath.get_path()
    e = JavaExportPath.get_path_cache()

    print("{}\n{}\n{}\n{}\n{}\n".format(a, b, c, d, e))
