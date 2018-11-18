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
        MINIBATCH_KMEANS: MiniBatchKMeans(
            n_clusters=config[N_CLUSTERS],
            max_iter=config[MAX_ITER],
            batch_size=config[BATCH_SIZE],
            verbose=config[VERBOSE],
            max_no_improvement=config[MAX_NO_IMPROVEMENT],
            init_size=config[INIT_SIZE],
            n_init=config[N_INIT],
            reassignment_ratio=config[REASSIGNMENT_RATIO]
        )
    }

    def get_algorithm(self):
        return Clusterizator.CLUSTERIZATORS[config[CLUSTERER]]

    def __init__(self):
        self.decompositor = Decompositor.get_instance()
        self.clusterer = self.clusterize(self.decompositor.matrix)
        # for (row, label) in enumerate(clusterer.labels_):
        #     print("row {} has label {}".format(row, label))

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

        c = Chrono("Saving clusterer...")
        io_utils.save_joblib(clusterer, ClustersPath.get_clusterer_path())
        c.millis()

        return clusterer

    def cluster2user(self):
        c2u = {}
        for user, cluster in enumerate(self.clusterer.labels_):
            if cluster not in c2u:
                c2u[cluster] = set()
            c2u[cluster].add(self.decompositor.index2users[user])

        assert len(c2u) == config[N_CLUSTERS]
        assert sum(len(c2u[x]) for x in c2u) == len(self.clusterer.labels_)
        return c2u

    def measure_quality(self):
        calinski = calinski_harabaz_score(self.decompositor.matrix,
                                          self.clusterer.labels_)

        davies = davies_bouldin_score(
            self.decompositor.matrix, self.clusterer.labels_)

        d2 = self.DaviesBouldin(
            self.decompositor.matrix, self.clusterer.labels_)
        return calinski, davies, d2

    def DaviesBouldin(self, X, labels):
        n_cluster = len(np.bincount(labels))
        cluster_k = [X[labels == k] for k in range(n_cluster)]
        centroids = [np.mean(k, axis=0) for k in cluster_k]
        variances = [np.mean([euclidean(p, centroids[i]) for p in k])
                     for i, k in enumerate(cluster_k)]
        db = []

        for i in range(n_cluster):
            for j in range(n_cluster):
                if j != i:
                    db.append((variances[i] + variances[j]) /
                              euclidean(centroids[i], centroids[j]))

        return(np.max(db) / n_cluster)


if __name__ == "__main__":

    for x in [50, 100, 300, 500]:
        config[N_CLUSTERS] = x
        c = Clusterizator()
        calinski, davies, d = c.measure_quality()
        print("[QUALITY OF CLUSTERIZATION IN {}]".format(x))
        print("Calinski-Harabaz score [higher is better]: {}".format(calinski))
        print("Davies-Bouldin score [0 is best]: {}".format(davies))
        print("Custom Davies-Bouldin score [0 is best]: {}".format(d))
        print()


# np.set_printoptions(precision=100)
# r = c.decompositor.pipe_reducer.transform(m)
# print(r.shape)
# co = 0
# for x in np.linalg.norm(m, ord=2, axis=1):
#     if not x:
#         co += 1
#     print(x)
# print(co)

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
