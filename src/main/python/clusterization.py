import scipy.sparse
import numpy as np

from sklearn.decomposition import TruncatedSVD
from sklearn.preprocessing import Normalizer
from sklearn.pipeline import make_pipeline
import hdbscan

import utils
from constants import *
from chronometer import Chrono
# from sklearn.cluster import *
from java_export import JavaExport
from decomposition import Decompositor
from config import *


class Clusterizator:

    def __init__(self):
        self.decompositor = Decompositor.get_instance()

    def clusterize(self, M, minsize=100):
        c = Chrono("Clusterize...")
        # algorithm='boruvka_kdtree')
        clusterer = hdbscan.HDBSCAN(
            min_cluster_size=minsize, core_dist_n_jobs=-1)
        clusterer.fit(M)
        c.millis()

        for (row, label) in enumerate(clusterer.labels_):
            print("row {} has label {}".format(row, label))


if __name__ == "__main__":
    # c = scipy.sparse.lil_matrix((5, 5), dtype=np.bool)
    # c[0, 4] = 1
    # c[0, 3] = 1
    # c[3, 1] = 1
    # c[4, 2] = 1
    # T = scipy.sparse.lil_matrix((5, 5), dtype=np.float32)
    # T[0, 4] = 23
    #
    # i = Config.get_instance()
    # a = T * i[TWEET_IMPORTANCE]
    #
    # print(a)
    # T[2, 4] = -23
    # T[1, 1] = 10
    # T[2, 1] = 20

    # # # print("normale\n\n", d.todense())
    # # # d.setdiag(0)
    # # # print("diag a 0\n\n", d.todense())
    # # # d = d.power(5)
    # # # print("potenza\n\n", d.todense())
    # # # d = (d > 0).astype(float)
    # # # print("logical\n\n", d.todense())
    # print(c.astype(float).todense())
    # print(T.astype(float).todense())
    # print()
    # for i in range(5):
    #     print("C^{}".format(i))
    #     print((c ** i).astype(float).todense())
    #     print(((c ** i) @ T).astype(float).todense())
    #     print()

    # # c = c * 5
    # # print()
    # # print(c.todense())
    # # print()
    # # print(d.todense())
    # # print()
    # # print(c @ d.todense())
    # # print(c.todense())
    # # print((c @ c).todense())
    # # print((c @ c @ c).todense())
    i = Config.get_instance()
    Clusterizator()
