import numpy as np

from sklearn.metrics.pairwise import cosine_distances
from java_export import *
from constants import *
from decomposition import Decompositor
import io_utils


class Recommender:

    def __init__(self):
        self.decompositor = Decompositor.get_instance()
        self.users2liked_pages, self.users2to_recommend = self.read_data()

    def check_page_consistency(self, pages_inds):
        warns = 0
        for x in pages_inds:
            if not self.decompositor.page_matrix[x, :].any():
                warns += 1
                # print("WARNING: page {} is represented by a zero vector".format(
                #     self.decompositor.index2pages[x]))
        assert warns < len(pages_inds) - 1

    def convert_pages2indexes(self, pages):
        return [self.decompositor.pages2index[i] for i in pages]

    def user_distances_to_pages(self, user_ind, pages_inds):
        user_vec = self.decompositor.matrix[user_ind, :]
        self.check_page_consistency(pages_inds)
        return cosine_distances([user_vec], self.decompositor.page_matrix[pages_inds, :])

    def pages_distances_to_pages(self, pages_inds1, pages_inds2):
        pages1_vecs = self.decompositor.page_matrix[pages_inds1, :]
        pages2_vecs = self.decompositor.page_matrix[pages_inds2, :]
        self.check_page_consistency(pages_inds1)
        self.check_page_consistency(pages_inds2)
        return cosine_distances(pages1_vecs, pages2_vecs)

    def reccomend_user_based(self, user, to_recommed):
        dists = self.user_distances_to_pages(
            self.decompositor.users2index[user],
            self.convert_pages2indexes(to_recommed))[0]
        return sorted(zip(to_recommed, dists.tolist()), key=lambda x: x[1])

    def recommend_item_based(self, user, to_recommend, fun):
        dists = self.pages_distances_to_pages(
            self.convert_pages2indexes(self.users2liked_pages[user]),
            self.convert_pages2indexes(to_recommend))

        reduced_dists = fun(dists, axis=0)

        assert len(reduced_dists) == len(to_recommend)
        return sorted(zip(to_recommend, reduced_dists.tolist()), key=lambda x: x[1])

    def read_data(self):
        users2to_recommend = {}
        with open(DatasetPath._S23_PATH, 'r') as f:
            for x in f.readlines():
                user, page = x.split()
                user = user.strip()
                page = page.strip()

                assert user in self.decompositor.users
                assert page in self.decompositor.pages

                if user not in users2to_recommend:
                    users2to_recommend[user] = []

                users2to_recommend[user].append(page)

        users2liked_pages = {}
        data = JavaExport.read()
        for u in data.user2likedItems_wikipage:
            for page in data.user2likedItems_wikipage[u]:
                if u not in users2liked_pages:
                    users2liked_pages[u] = []
                users2liked_pages[u].append(page)

        return users2liked_pages, users2to_recommend

    def reccomend(self, recommend_function, reduce_fun):
        users2best_page = {}
        users2ranking = {}

        for user in self.users2to_recommend:
            pages_to_rec = self.users2to_recommend[user]

            if reduce_fun is None:
                ranking = recommend_function(user, pages_to_rec)
            else:
                ranking = recommend_function(user, pages_to_rec, reduce_fun)

            users2best_page[user] = [x for x, _ in ranking[:3]]
            users2ranking[user] = ranking

        return users2best_page, users2ranking

    def recommend_all(self):
        red_min = RecommenderPath.REDUCTION_MIN
        red_mean = RecommenderPath.REDUCTION_MEAN
        red_max = RecommenderPath.REDUCTION_MAX

        ub_path = RecommenderPath.get_userbased_recommendation_paths
        ib_path = RecommenderPath.get_itembased_recommendation_paths

        rec_ub = self.reccomend_user_based
        rec_ib = self.recommend_item_based

        for type, path_fun, reduce_fun, recommend_fun in zip(
            (red_min,   red_mean,   red_max,    None),
            (ib_path,   ib_path,    ib_path,    ub_path),
            (np.min,    np.mean,    np.max,     None),
            (rec_ib,    rec_ib,     rec_ib,     rec_ub)
        ):

            c = Chrono("Computing recommendations: {} {}...".format(
                recommend_fun.__name__, type))

            users2best_page, users2ranking = self.reccomend(
                recommend_fun, reduce_fun)

            best_path, ranking_path = path_fun(
                type) if type is not None else path_fun()
            c.millis()

            c2 = Chrono("Saving recommendations...")
            io_utils.save_json(users2best_page, best_path)
            io_utils.save_json(users2ranking, ranking_path)
            c2.millis()

        c3 = Chrono("Saving config...")
        config.save_config()
        c3.millis()


if __name__ == "__main__":

    c = Recommender()

    u = "100552039"
    pages = ["WIKI:EN:John_C._Dvorak",
             "WIKI:EN:Sergei_Bobrovsky",
             "WIKI:EN:Rainn_Wilson",
             "WIKI:EN:Guido_Henkel",
             "WIKI:EN:Conan_O'Brien",
             "WIKI:EN:The_All_American"]

    print("Liked pages: \n{}\n\n".format(c.users2liked_pages[u]))

    print("Possible pages: \n{}\n\n".format(c.users2to_recommend[u]))

    a = c.reccomend_user_based(u, pages)
    print("Recommendation user based: \n{}\n\n".format(a))

    a = c.recommend_item_based(u, pages, np.mean)
    print("Recommendation item based, mean: \n{}\n\n".format(a))

    a = c.recommend_item_based(u, pages, np.max)
    print("Recommendation item based, max: \n{}\n\n".format(a))

    a = c.recommend_item_based(u, pages, np.min)
    print("Recommendation item based, min: \n{}\n\n".format(a))
#
    # print(a)
    c.recommend_all()
