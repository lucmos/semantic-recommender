import scipy.sparse
import numpy as np

from sklearn.cluster import MiniBatchKMeans
from sklearn.metrics import silhouette_score
from sklearn.metrics import silhouette_samples
from sklearn.metrics import calinski_harabaz_score
from sklearn.metrics import davies_bouldin_score

import io_utils
from decomposition import Decompositor
from chronometer import Chrono
from java_export import JavaExport
from configuration import *
from constants import *
from scipy.spatial.distance import euclidean
from collections import Counter


class Clusterizator:

    CLUSTERIZATORS = {
        MINIBATCH_KMEANS: MiniBatchKMeans
    }

    def get_algorithm(self):
        cluster = config[CLUSTERER]
        clusterer_class = Clusterizator.CLUSTERIZATORS[cluster]
        params = {x: config[x] for x in PARAMETERS[cluster]}
        return clusterer_class(**params)

    def __init__(self):
        self.decompositor = Decompositor.get_instance()
        self.clusterer = self.clusterize(self.decompositor.matrix)
        self.export()
        # for (row, label) in enumerate(clusterer.labels_):
        #     print("row {} has label {}".format(row, label))

    def export(self):
        c = Chrono("Saving clusterer...")
        io_utils.save_joblib(self.clusterer, ClustersPath.get_clusterer_path())
        c.millis()

        chrono = Chrono("Exporting clusters...")
        c2u = self.clusters2users()
        io_utils.save_json(c2u, ClustersPath.get_clusters_2_users_path())

        u2c = self.users2clusters()
        io_utils.save_json(u2c, ClustersPath.get_users_2_users_path())
        chrono.millis()

        c = Chrono("Saving config...")
        config.save_config()
        c.millis()

    def clusterize(self, M):
        try:
            c = Chrono("Loading clusters...")
            clusterer = io_utils.load_joblib(ClustersPath.get_clusterer_path())
            c.millis("from cache (in {} millis)")
            return clusterer
        except IOError:
            c.millis("not present (in {} millis)")

        c = Chrono("Performing clusterizaion...")
        clusterer = self.get_algorithm()
        clusterer.fit(M)
        c.millis()
        return clusterer

    def clusters2users(self):
        c2u = {}
        for user, cluster in enumerate(self.clusterer.labels_):
            clu = cluster.item()
            if clu not in c2u:
                c2u[clu] = set()
            c2u[clu].add(self.decompositor.index2users[user])

        c2u = {x: list(y) for x, y in c2u.items()}
        assert len(c2u) == config[N_CLUSTERS], "Expected: {}, Generated: {}".format(
            config[N_CLUSTERS], len(c2u))
        assert sum(len(c2u[x]) for x in c2u) == len(self.clusterer.labels_)
        return c2u

    def users2clusters(self):
        out = {self.decompositor.index2users[user]: cluster.item() for user, cluster in enumerate(
            self.clusterer.labels_)}
        assert len(out) == self.decompositor.num_users
        return out

    def measure_quality(self):
        calinski = calinski_harabaz_score(self.decompositor.matrix,
                                          self.clusterer.labels_)

        davies = davies_bouldin_score(
            self.decompositor.matrix, self.clusterer.labels_)

        return calinski, davies


if __name__ == "__main__":

    for x in [50, 100, 150, 300, 450, 500]:
        config[N_CLUSTERS] = x
        c = Clusterizator()
        calinski, davies = c.measure_quality()

        print("[QUALITY OF CLUSTERIZATION IN {}]".format(x))
        print("Calinski-Harabaz score [higher is better]: {}".format(calinski))
        print("Davies-Bouldin score [0 is best]: {}".format(davies))
        print()


# print("Perc", co, r.shape[0])
# import sklearn.preprocessing as preprocessing
# from sklearn.preprocessing import Normalizer
# normalizer_output = Normalizer(copy=False)

# X = [[1., -1.,  2.],   [.1,  .3,  0.],      [0.,  1., -1.]]

# X_normalized = normalizer_output.fit_transform(r)
# print(X_normalized)
# c2 = 0
# for x in (r.sum(1)):
#     if not x:
#         c2 += 1
#     print(x)
# print(co, c2)

# a = c.recommender(1, list(range(10)), m)
# print(a)
# print(m2.shape)
# print(m3)
# print(c.decompositor.matrix)
# print(c.decompositor.matrix[1, :])
# print(sum(c.decompositor.matrix[1, :]))
# print(c.decompositor.page2cat_matrix())
