import scipy.sparse
import numpy as np

from sklearn.cluster import *

import utils
from decomposition import Decompositor
from chronometer import Chrono
from java_export import JavaExport
from constants import *
from configuration import *


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
        self.clusterize(self.decompositor.matrix)

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

        for (row, label) in enumerate(clusterer.labels_):
            print("row {} has label {}".format(row, label))

        c = Chrono("Saving clusterer...")
        utils.save_joblib(clusterer, ClustersPath.get_clusterer_path())
        c.millis()

        return clusterer


if __name__ == "__main__":
    Clusterizator()
