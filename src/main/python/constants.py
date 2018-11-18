import os
from configuration import *


class MatrixPath:
    _FULL_MATRIX_CACHE = "cache/full/full_{}_{}_{}_dist_{}"
    _INDEXES_CACHE = "cache/full/indexes_{}"
    _REDUCED_MATRIX_CACHE = "cache/reduced/reduced_{}_dim_{}"
    _SVD_INSTANCE = "cache/reduced/{}_{}"

    @staticmethod
    def get_full_matrix_path():
        return MatrixPath._FULL_MATRIX_CACHE.format(config[DATASET], config[CLUSTER_OVER], config[DIMENSION], config[MAX_USER_DISTANCE])

    @staticmethod
    def get_full_matrix_name():
        return os.path.basename(MatrixPath.get_full_matrix_path())

    @staticmethod
    def get_indexes_path():
        return MatrixPath._INDEXES_CACHE.format(MatrixPath.get_full_matrix_name())

    @staticmethod
    def get_matrix_svd_path():
        full_matrix = MatrixPath.get_full_matrix_name()
        reduced_matrix = MatrixPath._REDUCED_MATRIX_CACHE.format(
            full_matrix, config[MATRIX_DIMENSIONALITY])
        return reduced_matrix, MatrixPath._SVD_INSTANCE.format(os.path.basename(reduced_matrix), config[REDUCER])


class JavaExportPath:
    _EXPORT_PATH = "results/java_export/export_{}_{}_{}"
    _EXPORT_CACHE = "cache/java_export/{}"

    @staticmethod
    def get_path():
        return JavaExportPath._EXPORT_PATH.format(config[DATASET], config[CLUSTER_OVER], config[DIMENSION])

    @staticmethod
    def get_path_cache():
        path = os.path.basename(JavaExportPath.get_path())
        return JavaExportPath._EXPORT_CACHE.format(path)


class DatasetPath:
    _S23_PATH = "datasets/S23.tsv"


class ClustersPath:
    _CLUSTERS_PATH = "results/clusters_of_{}_{}_{}"
    _CLUSTERER_PATH = "results/clusterer_of_{}_{}_{}"

    @staticmethod
    def get_clusterer_path():
        _, matrix_red_with_svd = MatrixPath.get_matrix_svd_path()
        clust = config[CLUSTERER]
        params = "_".join(str(config[x]) for x in CLUSTERER_PARAMETERS[clust])
        return ClustersPath._CLUSTERER_PATH.format(os.path.basename(matrix_red_with_svd), params, clust)


class RecommenderPath:
    _BEST_RECCOMENDATION = "results/recommendations/best_reccomendation"
    _RANKING_RECCOMENDATION = "results/recommendations/ranking_reccomendation"


if __name__ == "__main__":
    a = MatrixPath.get_full_matrix_path()
    b, c = MatrixPath.get_matrix_svd_path()
    o = MatrixPath.get_indexes_path()
    d = JavaExportPath.get_path()
    e = JavaExportPath.get_path_cache()
    f = ClustersPath.get_clusterer_path()

    print(o)
    print("{}\n{}\n{}\n{}\n{}\n{}\n".format(a, b, c, d, e, f))
