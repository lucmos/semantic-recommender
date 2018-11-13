import scipy.sparse
import hdbscan

from cache import Cache
from constants import *
from chronometer import Chrono
from sklearn.cluster import *


class Clusterizator:

    def __init__(self):
        a, b, c = Cache.JavaExport.read(Dataset.WIKIMID())
        self.data = a
        self.user2catcounter = b

        self.categories = sorted(c)
        self.categories2index = {c: i for i, c in enumerate(self.categories)}
        self.num_cat = len(self.categories)

        self.users = sorted(self.user2catcounter.keys())
        self.users2index = {u: i for i, u in enumerate(self.users)}
        self.num_users = len(self.users)

    def gen_sparse_matrix(self):
        D = scipy.sparse.lil_matrix((self.num_users, self.num_cat))

        for u in self.user2catcounter:
            for c in self.user2catcounter[u]:
                u_in = self.users2index[u]
                c_in = self.categories2index[c]
                D[u_in, c_in] = self.user2catcounter[u][c]

        return D

    def clusterize(self, k=1000):
        c = Chrono("Generate sparse matrix...")
        D = self.gen_sparse_matrix()
        c.millis()

        c = Chrono("Clusterize...")
        clusterer = hdbscan.HDBSCAN(
            min_cluster_size=100, algorithm='boruvka_kdtree')
        clusterer.fit(D.tocsr())
        c.millis()

        for (row, label) in enumerate(clusterer.labels_):
            print("row {} has label {}".format(row, label))


Clusterizator().clusterize()
