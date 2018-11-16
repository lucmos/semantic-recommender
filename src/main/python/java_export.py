from collections import Counter

import utils
from configuration import *
from chronometer import Chrono
from constants import JavaExportPath


class JavaExport:

    @staticmethod
    def read():
        path = JavaExportPath.get_path()

        path_cache = JavaExportPath.get_path_cache()

        c = Chrono("Reading {}...".format(path))

        try:
            obj = utils.load_pickle(path_cache)
            c.millis("from pickle (in {} millis)")
            return obj
        except IOError:
            pass

        try:
            data = utils.load_json(path)
            obj = JavaExport(*[data[x] for x in JavaExport.KEYS])
            utils.save_pickle(obj, path_cache)
            c.millis("from json, cached (in {} millis)")
            return obj

        except IOError:
            raise IOError("File not found: {}".format(path))

    ALL_USERS = "all_users"
    ALL_PAGES = "all_pages"
    ALL_CATDOM = "all_catdom"
    PAGES2CATDOM = "pages2catdom"
    USER2PERSONALPAGE_CATDOM = "user2personalPage_catdom"
    USER2LIKEDITEMS_WIKIPAGE = "user2likedItems_wikipage"
    USER2FOLLOWOUT = "user2followOut"
    USER2TWEET_CATDOM_COUNTER = "user2tweet_catdom_counter"

    KEYS = [ALL_USERS,
            ALL_PAGES,
            ALL_CATDOM,
            PAGES2CATDOM,
            USER2PERSONALPAGE_CATDOM,
            USER2LIKEDITEMS_WIKIPAGE,
            USER2FOLLOWOUT,
            USER2TWEET_CATDOM_COUNTER]

    def __init__(self,
                 all_users,
                 all_pages,
                 all_catdom,
                 pages2catdom,
                 user2personalPage_catdom,
                 user2likedItems_wikipage,
                 user2followOut,
                 user2tweet_catdom_counter):
        self.all_users = sorted(all_users)
        self.all_pages = sorted(all_pages)
        self.all_catdom = sorted(all_catdom)
        self.pages2catdom = pages2catdom
        self.user2personalPage_catdom = user2personalPage_catdom
        self.user2likedItems_wikipage = user2likedItems_wikipage
        self.user2followOut = user2followOut
        self.user2tweet_catdom_counter = {u: Counter(
            y) for u, y in user2tweet_catdom_counter.items()}


if __name__ == "__main__":
    a = JavaExport.read()
    # print(a)
    for i in JavaExport.KEYS:
        print(i, len(a.__getattribute__(i)))
