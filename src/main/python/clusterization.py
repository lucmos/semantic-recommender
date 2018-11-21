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
        self.baseline = self.generate_baseline()
        self.export_clusters()
        self.export_quality()

    def generate_baseline(self):
        c = Chrono("Computing latent baselines...")
        labels_latent = np.argmax(self.decompositor.matrix, axis=1).tolist()
        assert len(labels_latent) == self.decompositor.num_users, "Generated: {}, Expected: {}".format(
            len(labels_latent), self.decompositor.num_users)
        labels_latent = np.asarray(labels_latent)
        c.millis()
        return labels_latent

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

    def clusters2users(self, labels, unsafe=False):
        c2u = {}
        for user, cluster in enumerate(labels):
            clu = cluster.item()
            if clu not in c2u:
                c2u[clu] = set()
            c2u[clu].add(self.decompositor.index2users[user])

        c2u = {x: list(y) for x, y in c2u.items()}
        if not unsafe:
            assert len(c2u) == config[N_CLUSTERS], "Expected: {}, Generated: {}".format(
                config[N_CLUSTERS], len(c2u))
        assert sum(len(c2u[x]) for x in c2u) == len(labels)
        return c2u

    def users2clusters(self, labels):
        out = {self.decompositor.index2users[user]: cluster.item() for user, cluster in enumerate(
            labels)}
        assert len(out) == self.decompositor.num_users
        return out

    def measure_quality(self, labels=None):
        if labels is None:
            labels = self.clusterer.labels_

        calinski = calinski_harabaz_score(self.decompositor.matrix,
                                          labels)

        davies = davies_bouldin_score(
            self.decompositor.matrix, labels)

        return calinski, davies

    def export_clusters(self):
        c = Chrono("Saving clusterer...")
        io_utils.save_joblib(self.clusterer, ClustersPath.get_clusterer_path())
        c.millis()

        chrono = Chrono("Exporting clusters...")
        c2u = self.clusters2users(self.clusterer.labels_)
        io_utils.save_json(c2u, ClustersPath.get_clusters_2_users_path())

        u2c = self.users2clusters(self.clusterer.labels_)
        io_utils.save_json(u2c, ClustersPath.get_users_2_users_path())

        c2u_path, u2c_patch = ClustersPath.get_baselines_path()
        c2u = self.clusters2users(self.baseline, True)
        io_utils.save_json(c2u, c2u_path)

        u2c = self.users2clusters(self.baseline)
        io_utils.save_json(u2c, u2c_patch)
        chrono.millis()

        c = Chrono("Saving config...")
        config.save_config()
        c.millis()

    def export_quality(self):
        c = Chrono("Measuring quality...")
        D = "davies"
        C = "calinski"
        q = [
            {config.get_id(): {
                x: y for x, y in zip((C, D), self.measure_quality(self.clusterer.labels_))
            }},
            {"baseline_on_latent_categories": {
                x: y for x, y in zip((C, D), self.measure_quality(self.baseline))
            }},
            {"Config reminder": json.dumps(
                config.get_internal_properties(), sort_keys=True)}
        ]

        c.millis()

        chrono = Chrono("Exporting quality...")
        io_utils.save_json(q, ClustersPath.get_quality_path())
        chrono.millis()


if __name__ == "__main__":
    # Clusterizator()

    for x in [50, 100, 150, 300, 350, 400, 450, 500, 550, 600, 750, 800]:
        config[N_CLUSTERS] = x
        c = Clusterizator()
    #     calinski, davies = c.measure_quality()

    #     print("[QUALITY OF CLUSTERIZATION IN {}]".format(x))
    #     print("Calinski-Harabaz score [higher is better]: {}".format(calinski))
    #     print("Davies-Bouldin score [0 is best]: {}".format(davies))
    #     print()

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
