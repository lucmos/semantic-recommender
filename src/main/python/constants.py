from enum import Enum


class MatrixPath:
    _MATRIX_CACHE = "cache/matrix_{}_{}_{}_maxdistance_{}.bin"

    @staticmethod
    def get_path(name, cluster_over, dimension, max_distance):
        return MatrixPath._MATRIX_CACHE.format(name, cluster_over, dimension, max_distance)


class JavaExportPath:
    _IMPORT_PATH = "results/java_export/export_{}_{}_{}.json"

    @staticmethod
    def get_path(name, cluster_over, dimension):
        return JavaExportPath._IMPORT_PATH.format(name, cluster_over, dimension)


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

    def get_path(self, dimension):
        return self.path.format(dimension)


class Clusters:
    @staticmethod
    def CLUSTERS():
        return Clusters("results/clusters_{}_{}_{}.json")

    def __init__(self, name):
        self.name = name

    def get_path(self, cluster_over, cluster_method, dimension):
        return self.name.format(cluster_over, cluster_method, dimension)


if __name__ == "__main__":
    print(Dataset.WIKIMID().get_path("small"))
