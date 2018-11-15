import scipy.sparse
import numpy as np

from sklearn.decomposition import TruncatedSVD
from sklearn.preprocessing import Normalizer
from sklearn.pipeline import make_pipeline
# import hdbscan

import utils
from constants import *
from chronometer import Chrono
# from sklearn.cluster import *
from java_export import JavaExport
from config import *


class Clusterizator:

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
                F[u_index, f_index] = True
        chrono.millis()

        del data.user2followOut
        return F.tocsr()

    def _compute_matrix(self, data):
        conf = Config.get_instance()

        tweet_imp = conf[TWEET_IMPORTANCE]
        personal_page_imp = conf[PERSONAL_PAGE_IMPORTANCE]
        liked_items_imp = conf[LIKED_ITEMS_IMPORTANCE]
        f_tweet_imp = conf[FOLLOW_OUT_TWEET_IMPORTANCE]
        f_personal_page_imp = conf[FOLLOW_OUT_PERSONAL_PAGE_IMPORTANCE]
        f_liked_items_imp = conf[FOLLOW_OUT_LIKED_ITEMS_IMPORTANCE]
        decay = conf[RATE_OF_DECAY]

        chrono1 = Chrono("Computing M....")
        T = self.sparse_user2tweetcat(data)
        P = self.sparse_user2personalcat(data)
        L = self.sparse_user2likedcat(data)

        F = self.sparse_user2followout(data)
        del data

        chrono3 = Chrono("Performing M = T + P + L")
        M = (tweet_imp * T +
             personal_page_imp * P +
             liked_items_imp * L)
        chrono3.millis()

        Fi = F.copy()
        for i in range(1, conf[MAX_USER_DISTANCE] + 1):
            chrono2 = Chrono(
                "Computing iteration {} (number of ones in F: {})...".format(i, Fi.sum()))

            for name_matrix, matrix, importance in zip(("T", "P", "L"),
                                                       (T, P, L),
                                                       (f_tweet_imp, f_personal_page_imp, f_liked_items_imp)):

                chrono3 = Chrono("Computing F^{} @ {}".format(i, name_matrix))
                coeff = (decay ** i) * importance
                print(" (coeff: ", coeff, end=") ", flush=True)
                M += (coeff * (Fi @ matrix))
                chrono3.millis()

            if (i == conf[MAX_USER_DISTANCE]):
                break

            chorno4 = Chrono("Computing F^{} @ F...".format(i))

            Fi @= F
            chorno4.millis()

            chorno4 = Chrono("Setting diagonal to zero...")
            Fi = Fi.tolil()
            Fi.setdiag(False)
            Fi = Fi.tocsr()
            chorno4.millis()

            chrono2.millis()

        chrono1.millis()
        del T, P, L, F
        return M

    def _compute_decomposition(self, M):
        i = Config.get_instance()
        normalizer_input = Normalizer(copy=False)
        SVD = TruncatedSVD(n_components=i[MATRIX_DIMENSIONALITY])
        normalizer_output = Normalizer(copy=False)
        pipeline = make_pipeline(normalizer_input, SVD, normalizer_output)

        return pipeline.fit_transform(M), pipeline

    def _compute_decomposition_and_save(self, dataset_name, M):
        conf = Config.get_instance()
        red_matrix, red_svd = MatrixPath.get_matrix_svd_path(
            dataset_name, MatrixPath.SVD)

        c = Chrono("Computing decomposition...")
        M_reduced, SVD = self._compute_decomposition(M)
        assert M_reduced.shape()[1] == conf[MATRIX_DIMENSIONALITY]
        c.millis()

        c = Chrono("Saving decomposition...")
        scipy.sparse.save_npz(red_matrix, M_reduced)
        utils.save_joblib(SVD, red_svd)
        c.millis()

        return M_reduced, SVD

    def load_reduced_matrix_and_svd(self, dataset_name, data):
        i = Config.get_instance()
        full_matrix = MatrixPath.get_matrix_path(dataset_name)

        red_matrix, red_svd = MatrixPath.get_matrix_svd_path(
            dataset_name, MatrixPath.SVD)

        try:
            c = Chrono("Loading reduced matrix: {}".format(red_matrix))
            M_reduced = scipy.sparse.load_npz(red_matrix)
            SVD = utils.load_joblib(red_svd)
            assert M_reduced.shape()[1] == i[MATRIX_DIMENSIONALITY]
            return (M_reduced, SVD)
        except IOError:
            c.millis("not present (in {} millis)")

        try:
            c = Chrono("Loading full matrix: {}".format(full_matrix))
            M = scipy.sparse.load_npz(full_matrix)
            c.millis("from cache (in {} millis)")
            return self._compute_decomposition_and_save(dataset_name, M)
        except IOError:
            c.millis("not present (in {} millis)")

        c = Chrono("Computing full matrix...")
        M = self._compute_matrix(data)
        c.millis()
        c = Chrono("Saving matrix...")
        scipy.sparse.save_npz(full_matrix, M, compressed=False)
        c.millis()
        return self._compute_decomposition_and_save(dataset_name, M)

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

        M, SVD = self.load_reduced_matrix_and_svd(dataset_name, obj)
        print(M.sum())
        print(M.shape)
        print(M[0:20][0:20].todense())
        s = M.sum(1)
        print(s)
        print(len(s))
        # svd = TruncatedSVD(n_components=1000)
        # svd.fit(M)
        # print(M.shape)
        # M = svd.transform(M)
        # print(M.shape)
        # print(M[2, :])
        # print(M.sum())

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
    Clusterizator(Dataset.WIKIMID())
