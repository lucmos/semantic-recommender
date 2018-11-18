import scipy.sparse
import numpy as np

from sklearn.cluster import MiniBatchKMeans
from sklearn.metrics import silhouette_score
from sklearn.metrics import silhouette_samples
from sklearn.metrics import calinski_harabaz_score
from sklearn.metrics import davies_bouldin_score
from sklearn.metrics.pairwise import cosine_distances

import utils
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

        self.index2users = {v: u for u,
                            v in self.decompositor.users2index.items()}
        self.index2categories = {v: u for u,
                                 v in self.decompositor.categories2index.items()}
        self.index2pages = {v: u for u,
                            v in self.decompositor.pages2index.items()}

        # for (row, label) in enumerate(clusterer.labels_):
        #     print("row {} has label {}".format(row, label))

    def clusterize(self, M):
        try:
            c = Chrono("Loading clusters...")
            clusterer = utils.load_joblib(ClustersPath.get_clusterer_path())
            c.millis("from cache (in {} millis)")
            return clusterer
        except IOError:
            c.millis("not present (in {} millis)")

        c = Chrono("Performing clusterizaion...")
        clusterer = self.get_algorithm()
        clusterer.fit(M)
        c.millis()

        c = Chrono("Saving clusterer...")
        utils.save_joblib(clusterer, ClustersPath.get_clusterer_path())
        c.millis()

        return clusterer

    def cluster2user(self):
        c2u = {}
        for user, cluster in enumerate(self.clusterer.labels_):
            if cluster not in c2u:
                c2u[cluster] = set()
            c2u[cluster].add(self.index2users[user])

        assert len(c2u) == config[N_CLUSTERS]
        assert sum(len(c2u[x]) for x in c2u) == len(self.clusterer.labels_)
        return c2u

    def measure_quality(self):
        calinski = calinski_harabaz_score(self.decompositor.matrix,
                                          self.clusterer.labels_)

        davies = self.DaviesBouldin(self.decompositor.matrix,
                                    self.clusterer.labels_)

        # davies = davies_bouldin_score(self.decompositor.matrix,
        #   self.clusterer.labels_)
        return calinski, davies

    def user_distances_to_pages(self, user_ind, pages_inds):
        user_vec = self.decompositor.matrix[user_ind, :]
        return cosine_distances([user_vec], self.decompositor.page_matrix[pages_inds, :])

    def reccomend(self, user, pages):
        dists = self.user_distances_to_pages(
            self.decompositor.users2index[user],
            [self.decompositor.pages2index[i] for i in pages])
        print("User {}\n {}".format(user, dists))
        min_page_index = np.argmin(dists)
        print(min_page_index)
        return pages[min_page_index]

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

    # def visualize(self):
    #     from sklearn.decomposition import PCA
    #     import matplotlib.pyplot as plt

    #     reduced_data = PCA(n_components=2).fit_transform(
    #         self.decompositor.matrix)

    #     # Step size of the mesh. Decrease to increase the quality of the VQ.
    #     h = .02     # point in the mesh [x_min, x_max]x[y_min, y_max].

    #     # Plot the decision boundary. For that, we will assign a color to each
    #     x_min, x_max = reduced_data[:, 0].min(
    #     ) - 1, reduced_data[:, 0].max() + 1
    #     y_min, y_max = reduced_data[:, 1].min(
    #     ) - 1, reduced_data[:, 1].max() + 1
    #     xx, yy = np.meshgrid(np.arange(x_min, x_max, h),
    #                          np.arange(y_min, y_max, h))

    #     # Obtain labels for each point in mesh. Use last trained model.
    #     Z = self.clusterer.labels_

    #     # Put the result into a color plot
    #     # Z = Z.reshape(xx.shape)
    #     plt.figure(1)
    #     plt.clf()
    #     plt.imshow(Z, interpolation='nearest',
    #                extent=(xx.min(), xx.max(), yy.min(), yy.max()),
    #                cmap=plt.cm.Paired,
    #                aspect='auto', origin='lower')

    #     plt.plot(reduced_data[:, 0], reduced_data[:, 1], 'k.', markersize=2)
    #     # Plot the centroids as a white X
    #     centroids = self.clusterer.cluster_centers_
    #     plt.scatter(centroids[:, 0], centroids[:, 1],
    #                 marker='x', s=169, linewidths=3,
    #                 color='w', zorder=10)
    #     plt.title('K-means clustering on the digits dataset (PCA-reduced data)\n'
    #               'Centroids are marked with white cross')
    #     plt.xlim(x_min, x_max)
    #     plt.ylim(y_min, y_max)
    #     plt.xticks(())
    #     plt.yticks(())
    #     plt.show()


if __name__ == "__main__":
    c = Clusterizator()

    u = "101935414"

    pages = ["WIKI:EN:Kyle_Lohse", "WIKI:EN:Gilles_Peterson", "WIKI:EN:Kevin_Davies",
             "WIKI:EN:Bonnie_Bernstein", "WIKI:EN:Shinichi_Osawa", "WIKI:EN:Peter_Barakan"]

    u = "103115352"
    pages = ["WIKI:EN:Real_Madrid_C.F.", "WIKI:EN:Buddy_Miller", "WIKI:EN:Teleamazonas",
             "WIKI:EN:EMILY's_List", "WIKI:EN:Once_Mekel", "WIKI:EN:Chris_Cornell"]
    # print(m[1:50, 1:50].todense())
    # # for x in range(c.decompositor.num_pages):
    # #     print(sum(m[x, :]))
    u = "100552039"

    pages = ["WIKI:EN:John_C._Dvorak",
             "WIKI:EN:Sergei_Bobrovsky",
             "WIKI:EN:Rainn_Wilson",
             "WIKI:EN:Guido_Henkel",
             "WIKI:EN:Conan_O'Brien",
             "WIKI:EN:The_All_American"]
    racc = c.reccomend(u, pages)
    print(racc)

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
