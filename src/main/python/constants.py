import os
from configuration import *


class MatrixPath:
    _FULL_MATRIX_CACHE = "cache/full/full_{}"
    _INDEXES_CACHE = "cache/full/indexes_{}"

    _REDUCED_MATRIX_CACHE = "cache/decompositions/matrices/reduced_{}_{}"
    _REDUCER_INSTANCE = "cache/decompositions/reducers/reducer_{}_{}"

    @staticmethod
    def get_full_matrix_path():
        params = "_".join(str(config[x]) for x in PARAMETERS[MATRIX_BUILDING])
        return MatrixPath._FULL_MATRIX_CACHE.format(params)

    @staticmethod
    def get_full_matrix_name():
        return os.path.basename(MatrixPath.get_full_matrix_path())

    @staticmethod
    def get_indexes_path():
        return MatrixPath._INDEXES_CACHE.format(MatrixPath.get_full_matrix_name())

    @staticmethod
    def get_reduced_matrix_path():
        full_matrix = MatrixPath.get_full_matrix_name()
        params = "_".join(str(config[x]) for x in PARAMETERS[MATRIX_REDUCTION])

        reduced_matrix = MatrixPath._REDUCED_MATRIX_CACHE.format(
            full_matrix, params)

        svd_path = MatrixPath._REDUCER_INSTANCE.format(full_matrix, params)

        return reduced_matrix, svd_path

    @staticmethod
    def get_reduced_matrix_name():
        a, b = MatrixPath.get_reduced_matrix_path()
        return os.path.basename(a)


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
    _USERS_2_CLUSTERS_PATH = "results/clusterization/clusters/users2clusters_{}"
    _CLUSTERS_2_USERS_PATH = "results/clusterization/clusters/clusters2users_{}"
    _CLUSTERER_PATH = "results/clusterization/clusterizator/clusterer_of_{}"

    @staticmethod
    def get_clusterer_path():
        # _, matrix_red_with_svd = MatrixPath.get_reduced_matrix_path()
        # clust = config[CLUSTERER]
        # params = "_".join(str(config[x]) for x in PARAMETERS[clust])
        return ClustersPath._CLUSTERER_PATH.format(config.get_id())

    @staticmethod
    def get_clusters_2_users_path():
        # _, matrix_red_with_svd = MatrixPath.get_reduced_matrix_path()
        # clust = config[CLUSTERER]
        # params = "_".join(str(config[x]) for x in PARAMETERS[clust])
        return ClustersPath._CLUSTERS_2_USERS_PATH.format(config.get_id())

    @staticmethod
    def get_users_2_users_path():
        # _, matrix_red_with_svd = MatrixPath.get_reduced_matrix_path()
        # clust = config[CLUSTERER]
        # params = "_".join(str(config[x]) for x in PARAMETERS[clust])
        return ClustersPath._USERS_2_CLUSTERS_PATH.format(config.get_id())


class RecommenderPath:
    _BEST_RECOMMENDATION = "results/recommendations/best/best3_{}"
    _RANKING_RECCOMENDATION = "results/recommendations/ranking/ranking_{}"

    @staticmethod
    def get_best_recommendation_path():
        m = config.get_id()
        return RecommenderPath._BEST_RECOMMENDATION.format(m)

    @staticmethod
    def get_ranking_recommendation_path():
        m = config.get_id()
        return RecommenderPath._RANKING_RECCOMENDATION.format(m)


if __name__ == "__main__":
    a = MatrixPath.get_full_matrix_path()
    print(a)
    print()
    o, e = MatrixPath.get_reduced_matrix_path()
    print(o, "\n", e)
    print()
    d = JavaExportPath.get_path()
    e = JavaExportPath.get_path_cache()
    #
    f = ClustersPath.get_clusterer_path()

    print(o)

    a = ClustersPath.get_clusterer_path()
    print(a)
