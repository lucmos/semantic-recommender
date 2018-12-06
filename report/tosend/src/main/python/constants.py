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
    BASELINE_CATDOM_U2C = "results/clusterization/{}/baselines/{}_baseline_users2clusters"
    BASELINE_CATDOM_C2U = "results/clusterization/{}/baselines/{}_baseline_clusters2users"

    _USERS_2_CLUSTERS_PATH = "results/clusterization/{}/clusters/{}_users2clusters"
    _CLUSTERS_2_USERS_PATH = "results/clusterization/{}/clusters/{}_clusters2users"
    _CLUSTERER_PATH = "results/clusterization/{}/clusterizator/{}_clusterer"

    _QUALITY = "results/clusterization/{}/quality/{}_quality"

    @staticmethod
    def get_quality_path():
        return ClustersPath._QUALITY.format(config.get_id(), config.get_id())

    @staticmethod
    def get_baselines_path():
        return (ClustersPath.BASELINE_CATDOM_U2C.format(config.get_id(), config.get_id()),
                ClustersPath.BASELINE_CATDOM_C2U.format(config.get_id(), config.get_id()))

    @staticmethod
    def get_clusterer_path():
        return ClustersPath._CLUSTERER_PATH.format(config.get_id(), config.get_id())

    @staticmethod
    def get_clusters_2_users_path():
        return ClustersPath._CLUSTERS_2_USERS_PATH.format(config.get_id(), config.get_id())

    @staticmethod
    def get_users_2_users_path():
        return ClustersPath._USERS_2_CLUSTERS_PATH.format(config.get_id(), config.get_id())


class RecommenderPath:
    _BEST_USERBASED_RECOMMENDATION = "results/recommendations/{}/user_based/{}_best3"
    _RANKING_USERBASED_RECCOMENDATION = "results/recommendations/{}/user_based/{}_ranking"

    _BEST_ITEMBASED_RECOMMENDATION = "results/recommendations/{}/item_based/{}/{}_best3"
    _RANKING_ITEMBASED_RECCOMENDATION = "results/recommendations/{}/item_based/{}/{}_ranking"

    @staticmethod
    def get_userbased_recommendation_paths():
        m = config.get_id()
        return (RecommenderPath._BEST_USERBASED_RECOMMENDATION.format(m, m),
                RecommenderPath._RANKING_USERBASED_RECCOMENDATION.format(m, m))

    REDUCTION_MEAN = "min_of_mean_distances"
    REDUCTION_MAX = "min_of_max_distances"
    REDUCTION_MIN = "min_of_min_distances"

    @staticmethod
    def get_itembased_recommendation_paths(type):
        m = config.get_id()
        return (RecommenderPath._BEST_ITEMBASED_RECOMMENDATION.format(m, type, m),
                RecommenderPath._RANKING_ITEMBASED_RECCOMENDATION.format(m, type, m))


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
