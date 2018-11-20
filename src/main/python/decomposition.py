import scipy.sparse
import numpy as np

from sklearn.decomposition import TruncatedSVD
from sklearn.preprocessing import Normalizer
from sklearn.pipeline import make_pipeline

import io_utils
from constants import *
from chronometer import Chrono
from java_export import JavaExport
from configuration import *


class Decompositor:

    instance = None

    @staticmethod
    def get_instance():
        if not Decompositor.instance:
            Decompositor.instance = Decompositor()
        return Decompositor.instance

    def __init__(self):
        c = Chrono("Initializing decompositor...")
        ind = self._load_indexes()
        (self.categories, self.categories2index, self.index2categories, self.num_cat,
         self.pages, self.pages2index, self.index2pages, self.num_pages,
         self.users, self.users2index, self.index2users, self.num_users) = ind

        self.matrix, self.pipe_reducer = self._load_matrix()

        self.page_matrix = self.page2cat_matrix()
        c.millis()

    def page2cat_matrix(self):
        assert self.pipe_reducer is not None

        data = JavaExport.read()

        chrono = Chrono("Computing page2cat matrix...")
        M = scipy.sparse.lil_matrix(
            (self.num_pages, self.num_cat), dtype=np.float32)
        for page in data.pages2catdom:
            for c in data.pages2catdom[page]:
                p_index = self.pages2index[page]
                c_index = self.categories2index[c]
                M[p_index, c_index] = 1
        chrono.millis()

        chrono = Chrono("Transforming matrix...")
        out = self.pipe_reducer.transform(M.tocsr())
        chrono.millis()
        return out

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
            for page in data.user2likedItems_wikipage[u]:
                # print(u, page)
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
        tweet_imp = config[TWEET_IMPORTANCE]
        personal_page_imp = config[PERSONAL_PAGE_IMPORTANCE]
        liked_items_imp = config[LIKED_ITEMS_IMPORTANCE]
        f_tweet_imp = config[FOLLOW_OUT_TWEET_IMPORTANCE]
        f_personal_page_imp = config[FOLLOW_OUT_PERSONAL_PAGE_IMPORTANCE]
        f_liked_items_imp = config[FOLLOW_OUT_LIKED_ITEMS_IMPORTANCE]
        decay = config[RATE_OF_DECAY]

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
        for i in range(1, config[MAX_USER_DISTANCE] + 1):
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

            if (i == config[MAX_USER_DISTANCE]):
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
        red_matrix, red_svd = MatrixPath.get_reduced_matrix_path()

        c = Chrono("Computing decomposition...")

        normalizer_input = Normalizer(copy=False)
        pipe_reducer = TruncatedSVD(n_components=config[MATRIX_DIMENSIONALITY])
        normalizer_output = Normalizer(copy=False)
        pipeline = make_pipeline(
            normalizer_input, pipe_reducer, normalizer_output)
        # pipeline = make_pipeline(pipe_reducer)
        M_reduced, pipe_reducer = pipeline.fit_transform(M), pipeline
        assert M_reduced.shape[1] == config[MATRIX_DIMENSIONALITY]
        c.millis()

        c = Chrono("Saving decomposition...")
        io_utils.save_joblib(M_reduced, red_matrix)
        io_utils.save_joblib(pipe_reducer, red_svd)
        c.millis()

        return M_reduced, pipe_reducer

    def _load_matrix(self):
        full_matrix = MatrixPath.get_full_matrix_path()

        red_matrix, red_svd = MatrixPath.get_reduced_matrix_path()

        try:
            c = Chrono("Loading reduced matrix: {}".format(red_matrix))
            M_reduced = io_utils.load_joblib(red_matrix)
            pipe_reducer = io_utils.load_joblib(red_svd)
            assert M_reduced.shape[1] == config[MATRIX_DIMENSIONALITY]
            c.millis("from cache (in {} millis)")
            return (M_reduced, pipe_reducer)
        except IOError:
            c.millis("not present (in {} millis)")

        try:
            c = Chrono("Loading full matrix: {}".format(full_matrix))
            M = io_utils.load_npz(full_matrix)
            c.millis("from cache (in {} millis)")
            return self._compute_decomposition(M)
        except IOError:
            c.millis("not present (in {} millis)")

        c = Chrono("Computing full matrix...")
        data = JavaExport.read()
        M = self._compute_matrix(data)
        c.millis()
        c = Chrono("Saving matrix...")
        io_utils.save_npz(full_matrix, M, compressed=False)
        c.millis()
        return self._compute_decomposition(M)

    def _load_indexes(self):
        path = MatrixPath.get_indexes_path()

        try:
            c1 = Chrono("Loading indexes...")
            ind = io_utils.load_joblib(path)
            c1.millis("from cache (in {} millis)")
            return ind
        except IOError:
            c1.millis("not present (in {} millis)")

        c2 = Chrono("Computing indexes...")
        obj = JavaExport.read()
        categories = obj.all_catdom
        categories2index = {c: i for i, c in enumerate(categories)}
        num_cat = len(categories)

        pages = obj.all_pages
        pages2index = {p: i for i, p in enumerate(pages)}
        num_pages = len(pages)

        users = obj.all_users
        users2index = {u: i for i, u in enumerate(users)}
        num_users = len(users)
        c2.millis()

        index2categories = {v: u for u, v in categories2index.items()}
        index2pages = {v: u for u, v in pages2index.items()}
        index2users = {v: u for u, v in users2index.items()}

        c3 = Chrono("Saving indexes...")
        ind = (categories, categories2index, index2categories, num_cat,
               pages, pages2index, index2pages, num_pages,
               users, users2index, index2users, num_users)
        io_utils.save_joblib(ind, path)
        c3.millis()
        return ind


if __name__ == "__main__":
    d = Decompositor()
    print(d.matrix.shape)
