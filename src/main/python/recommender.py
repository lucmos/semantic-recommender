import numpy as np

from sklearn.metrics.pairwise import cosine_distances
from java_export import *
from constants import *
from decomposition import Decompositor
import io_utils


class Recommender:

    def __init__(self):
        self.decompositor = Decompositor.get_instance()

    def user_distances_to_pages(self, user_ind, pages_inds):
        user_vec = self.decompositor.matrix[user_ind, :]

        warns = 0
        for x in pages_inds:
            if not self.decompositor.page_matrix[x, :].any():
                warns += 1
                print("WARNING: page {} is represented by a zero vector".format(
                    self.decompositor.index2pages[x]))

        # if warns:
        #     print("{} WARNINGS".format(warns))

        assert warns < len(pages_inds) - 1

        return cosine_distances([user_vec], self.decompositor.page_matrix[pages_inds, :])

    def reccomend(self, user, pages):
        dists = self.user_distances_to_pages(
            self.decompositor.users2index[user],
            [self.decompositor.pages2index[i] for i in pages])[0]
        # min_page_index = np.argmin(dists)
        return sorted(zip(pages, dists.tolist()), key=lambda x: x[1])

    def reccomend_all(self):
        c = Chrono("Computing recommendations...")
        users2to_recommend = self.read_S23()
        users2best_page = {}
        users2ranking = {}

        for user in users2to_recommend:
            possible_pages = users2to_recommend[user]
            ranking = self.reccomend(user, possible_pages)
            users2best_page[user] = [x for x, _ in ranking[:3]]
            users2ranking[user] = ranking
        c.millis()

        c = Chrono("Saving reccomendations...")
        io_utils.save_json(
            users2best_page, RecommenderPath._BEST_RECCOMENDATION)
        io_utils.save_json(
            users2ranking, RecommenderPath._RANKING_RECCOMENDATION)
        c.millis()
        return users2best_page

    def read_S23(self):
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
        return users2to_recommend


if __name__ == "__main__":

    c = Recommender()

    u = "100552039"
    pages = ["WIKI:EN:John_C._Dvorak",
             "WIKI:EN:Sergei_Bobrovsky",
             "WIKI:EN:Rainn_Wilson",
             "WIKI:EN:Guido_Henkel",
             "WIKI:EN:Conan_O'Brien",
             "WIKI:EN:The_All_American"]
    a = c.reccomend(u, pages)

    print(a)

    c.reccomend_all()
