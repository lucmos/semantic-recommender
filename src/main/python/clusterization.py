import scipy.sparse
import numpy as np
# import hdbscan

import utils
from constants import *
from chronometer import Chrono
# from sklearn.cluster import *
from java_export import JavaExport
from config import Config


class Clusterizator:

    #   ! values in [0, 1]
    TWEET_IMPORTANCE = 0.30
    PERSONAL_PAGE_IMPORTANCE = 0.20
    LIKED_ITEMS_IMPORTANCE = 0.50

    RATE_OF_DECAY = 0.5
    FOLLOW_OUT_TWEET_IMPORTANCE = 0.15
    FOLLOW_OUT_PERSONAL_PAGE_IMPORTANCE = 0.55
    FOLLOW_OUT_LIKED_ITEMS_IMPORTANCE = 0.30

    MAX_USER_DISTANCE = 3

    #  #users x #cat
    def sparse_user2tweetcat(self, data):
        chrono = Chrono("Computing user2tweet matrix...")
        T = scipy.sparse.lil_matrix(
            (self.num_users, self.num_cat), dtype=np.float32)
        for u in data.user2tweet_catdom_counter:
            for c in data.user2tweet_catdom_counter[u]:
                u_index = self.users2index[u]
                c_index = self.categories2index[c]
                T[u_index, c_index] = data.user2tweet_catdom_counter[u][c]

        chrono.millis()

        del data.user2tweet_catdom_counter
        return T.tocsr()

    #  #users x #cat
    def sparse_user2personalcat(self, data):
        chrono = Chrono("Computing user2personal matrix...")
        P = scipy.sparse.lil_matrix(
            (self.num_users, self.num_cat), dtype=np.float32)
        for u in data.user2personalPage_catdom:
            for c in data.user2personalPage_catdom[u]:
                u_index = self.users2index[u]
                c_index = self.categories2index[c]
                P[u_index, c_index] = 1

        chrono.millis()

        del data.user2personalPage_catdom
        return P.tocsr()

    #  #users x #cat
    def sparse_user2likedcat(self, data):
        chrono = Chrono("Computing user2likedcat matrix...")
        L = scipy.sparse.lil_matrix(
            (self.num_users, self.num_cat), dtype=np.float32)
        for u in data.user2likedItems_wikipage:
            for page in data.user2likeItems_wikipage[u]:
                for c in data.pages2catdom[page]:
                    u_index = self.users2index[u]
                    c_index = self.categories2index[c]
                    L[u_index, c_index] += 1

        chrono.millis()

        del data.user2likedItems_wikipage
        return L.tocsr()

    def sparse_user2followout(self, data):
        chrono = Chrono("Computing followOut matrix...")
        F = scipy.sparse.lil_matrix(
            (self.num_users, self.num_users), dtype=np.bool)
        for u in data.user2followOut:
            for f in data.user2followOut[u]:
                u_index = self.users2index[u]
                f_index = self.users2index[f]
                F[u_index, f_index] = 1
        chrono.millis()

        del data.user2followOut
        return F.tocsr()

    def compute_matrix(self, data):
        chrono1 = Chrono("Computing M....")
        T = self.sparse_user2tweetcat(data)
        P = self.sparse_user2personalcat(data)
        L = self.sparse_user2likedcat(data)

        F = self.sparse_user2followout(data)
        del data

        chrono3 = Chrono("Performing M = T + P + L")
        M = (Clusterizator.TWEET_IMPORTANCE * T +
             Clusterizator.PERSONAL_PAGE_IMPORTANCE * P +
             Clusterizator.LIKED_ITEMS_IMPORTANCE * L)
        chrono3.millis()

        Fi = F.copy()
        for i in range(1, Clusterizator.MAX_USER_DISTANCE + 1):
            chrono2 = Chrono("Computing iteration {}...".format(i))

            for name_matrix, matrix, importance in zip(("T", "P", "L"),
                                                       (T, P, L),
                                                       (Clusterizator.FOLLOW_OUT_TWEET_IMPORTANCE,
                                                        Clusterizator.FOLLOW_OUT_PERSONAL_PAGE_IMPORTANCE,
                                                        Clusterizator.FOLLOW_OUT_LIKED_ITEMS_IMPORTANCE)):
                chrono3 = Chrono("Computing F^{} @ {}".format(i, name_matrix))
                coeff = (Clusterizator.RATE_OF_DECAY ** i) * importance
                M += (coeff * (Fi @ matrix))
                chrono3.millis()

            if (i == Clusterizator.MAX_USER_DISTANCE):
                break

            chorno4 = Chrono("Computing F^{} @ F...".format(i))

            Fi @= F
            Fi = Fi.tolil()
            Fi.setdiag(False)
            Fi = Fi.tocsr()

            chorno4.millis()
            chrono2.millis()

        chrono1.millis()
        del T, P, L, F
        return M

    def _get_opp_diagonal_matrix(self, F_i):
        D = scipy.sparse.lil_matrix((self.num_users, self.num_users))
        for i, v in enumerate(F_i.diagonal()):
            D[i, i] = -v
        return D.tocsr()

    def compute_matrix_cached(self, dataset_name, data):
        i = Config.get_instance()
        path = MatrixPath.get_path(
            dataset_name, i.cluster_over(), i.dimension(), Clusterizator.MAX_USER_DISTANCE)

        c = Chrono("Loading provider...")
        cache = None

        cache = utils.load_pickle(path)
        M = cache if cache else self.compute_matrix(data)

        if not cache:
            c.millis("generated")
            utils.save_pickle(M, path, override=True)
        else:
            c.millis("cached")

        return M

    # Data in the json export json:
    #
    # self.all_users
    # self.all_pages
    # self.all_catdom
    # self.pages2catdom
    # self.user2personalPage_catdom
    # self.user2likedItems_wikipage
    # self.user2followOut
    # self.user2tweet_catdom_counter

    def __init__(self, dataset_name):
        c = Chrono("Inizializing clusterizator...")
        obj = JavaExport.read(dataset_name)

        c2 = Chrono("Computing indexes...")
        self.categories = obj.all_catdom
        self.categories2index = {c: i for i, c in enumerate(self.categories)}
        self.num_cat = len(self.categories)

        self.pages = obj.all_pages
        self.pages2index = {p: i for i, p in enumerate(self.pages)}
        self.num_pages = len(self.pages)

        self.users = obj.all_users
        self.users2index = {u: i for i, u in enumerate(self.users)}
        self.num_users = len(self.users)
        c2.millis()

        M = self.compute_matrix_cached(dataset_name, obj)
        # P = self.sparse_user2personalcat(obj)
        # print("SOMAA: ", P.sum())
        # P = self.sparse_user2likedcat(obj)
        # print("SOMAA: ", P.sum())
        del obj
        c.millis()

    # def clusterize(self, k=1000):
    #     c = Chrono("Generate sparse matrix...")
    #     D = self.gen_sparse_matrix()
    #     c.millis()

    #     c = Chrono("Clusterize...")
    #     clusterer = hdbscan.HDBSCAN(
    #         min_cluster_size=100, algorithm='boruvka_kdtree')
    #     clusterer.fit(D.tocsr())
    #     c.millis()

    #     for (row, label) in enumerate(clusterer.labels_):
    #         print("row {} has label {}".format(row, label))


if __name__ == "__main__":
    c = scipy.sparse.lil_matrix((5, 5), dtype=np.bool)
    c[0, 4] = 1
    c[0, 3] = 1
    c[3, 1] = 1
    c[4, 2] = 1
    T = scipy.sparse.lil_matrix((5, 5))
    T[0, 4] = 23
    T[2, 4] = -23
    T[1, 1] = 10
    T[2, 1] = 20

    # # print("normale\n\n", d.todense())
    # # d.setdiag(0)
    # # print("diag a 0\n\n", d.todense())
    # # d = d.power(5)
    # # print("potenza\n\n", d.todense())
    # # d = (d > 0).astype(float)
    # # print("logical\n\n", d.todense())
    print(c.astype(float).todense())
    print(T.astype(float).todense())
    print()
    for i in range(5):
        print("C^{}".format(i))
        print((c ** i).astype(float).todense())
        print(((c ** i) @ T).astype(float).todense())
        print()

    # c = c * 5
    # print()
    # print(c.todense())
    # print()
    # print(d.todense())
    # print()
    # print(c @ d.todense())
    # print(c.todense())
    # print((c @ c).todense())
    # print((c @ c @ c).todense())
    # Clusterizator(Dataset.WIKIMID())
