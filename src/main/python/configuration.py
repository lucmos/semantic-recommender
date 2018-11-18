import javaproperties

# KEYS
# data
DATASET = "dataset"
DIMENSION = "dimension"
CLUSTER_OVER = "cluster_over"

# matrix building
MAX_USER_DISTANCE = "max_user_distance"
TWEET_IMPORTANCE = "tweet_importance"
PERSONAL_PAGE_IMPORTANCE = "personal_page_importance"
LIKED_ITEMS_IMPORTANCE = "liked_items_importance"
RATE_OF_DECAY = "rate_of_decay"
FOLLOW_OUT_TWEET_IMPORTANCE = "follow_out_tweet_importance"
FOLLOW_OUT_PERSONAL_PAGE_IMPORTANCE = "follow_out_personal_page_importance"
FOLLOW_OUT_LIKED_ITEMS_IMPORTANCE = "follow_out_liked_items_importance"

# decomposition
REDUCER = "reducer"
MATRIX_DIMENSIONALITY = "matrix_dimensionality"

# clusterization
CLUSTERER = "clusterer"

# minibatch
N_CLUSTERS = "n_clusters"
MAX_ITER = "max_iter"
BATCH_SIZE = "batch_size"
VERBOSE = "verbose"
MAX_NO_IMPROVEMENT = "max_no_improvement"
INIT_SIZE = "init_size"
N_INIT = "n_init"
REASSIGNMENT_RATIO = "reassignment_ratio"

# VALUES
WIKIMID = "WIKIMID"
S21 = "S21"
S22 = "S22"
S23 = "S23"
UNION = "UNION"

MINIBATCH_KMEANS = "minibatch_kmeans"

#  Group clusterer parameters togheter
CLUSTERER_PARAMETERS = {
    MINIBATCH_KMEANS: [
        N_CLUSTERS,
        MAX_ITER,
        BATCH_SIZE,
        VERBOSE,
        MAX_NO_IMPROVEMENT,
        INIT_SIZE,
        N_INIT,
        REASSIGNMENT_RATIO,
    ]
}


class Config():
    CONFIG_FILE = "./config/wsie.properties"

    KEYS = [
        DATASET,
        DIMENSION,
        CLUSTER_OVER,

        MAX_USER_DISTANCE,
        TWEET_IMPORTANCE,
        PERSONAL_PAGE_IMPORTANCE,
        LIKED_ITEMS_IMPORTANCE,
        RATE_OF_DECAY,
        FOLLOW_OUT_TWEET_IMPORTANCE,
        FOLLOW_OUT_PERSONAL_PAGE_IMPORTANCE,
        FOLLOW_OUT_LIKED_ITEMS_IMPORTANCE,

        REDUCER,
        MATRIX_DIMENSIONALITY,

        CLUSTERER,

        N_CLUSTERS,
        MAX_ITER,
        BATCH_SIZE,
        VERBOSE,
        MAX_NO_IMPROVEMENT,
        INIT_SIZE,
        N_INIT,
        REASSIGNMENT_RATIO,
    ]

    TO_ENFORCE_VALUES = {
        DATASET: {WIKIMID, S21, S22, S23, UNION},
        DIMENSION: {"small", "complete"},
        CLUSTER_OVER: {"categories", "domains"},
        REDUCER: {"truncated_svd"},
        CLUSTERER: {MINIBATCH_KMEANS}
    }

    TO_ENFORCE_INT = {
        MAX_USER_DISTANCE,
        MATRIX_DIMENSIONALITY,
        N_CLUSTERS,
        MAX_ITER,
        BATCH_SIZE,
        MAX_NO_IMPROVEMENT,
        INIT_SIZE,
        N_INIT,
    }

    TO_ENFORCE_FLOAT = {
        TWEET_IMPORTANCE,
        PERSONAL_PAGE_IMPORTANCE,
        LIKED_ITEMS_IMPORTANCE,
        RATE_OF_DECAY,
        FOLLOW_OUT_TWEET_IMPORTANCE,
        FOLLOW_OUT_PERSONAL_PAGE_IMPORTANCE,
        FOLLOW_OUT_LIKED_ITEMS_IMPORTANCE,

        REASSIGNMENT_RATIO,
    }

    TO_ENFORCE_BOOL = {
        VERBOSE
    }

    instance = None

    @staticmethod
    def get_instance():
        if not Config.instance:
            Config.instance = Config()
        return Config.instance

    def __init__(self, config_file=CONFIG_FILE):
        assert Config.instance is None, "Use config gloabl variable instead"
        self.load(config_file)

    def load(self, config_file, verbose=True):
        with open(config_file, 'r') as fp:
            self.properties = javaproperties.load(fp)

        self.check_and_normalize()

        if(verbose):
            print(self)

    def _possible_values(self, key):
        if key in self.TO_ENFORCE_VALUES:
            return self.TO_ENFORCE_VALUES[key]

        if key in self.TO_ENFORCE_FLOAT:
            return "float"

        if key in self.TO_ENFORCE_INT:
            return "int"

    def check_and_normalize(self):
        assert len(self.KEYS) == sum(len(x) for x in (self.TO_ENFORCE_BOOL,
                                                      self.TO_ENFORCE_FLOAT,
                                                      self.TO_ENFORCE_INT,
                                                      self.TO_ENFORCE_VALUES))

        for prop in self.KEYS:
            assert prop in self.properties, "Missing: {} property. Possible values: {}. Change: {}!".format(
                prop, self._possible_values(prop), Config.CONFIG_FILE)

        for prop in self.properties:
            assert prop in self.KEYS, "Property: {} is defined but not managed. Change: {}!".format(
                prop, Config.CONFIG_FILE)

        assert len(self.properties) == len(
            self.KEYS), "Defined: {} properties, expected: {} properties. Change: {}!".format(
                len(self.properties), len(self.KEYS), Config.CONFIG_FILE)

        self._cast()

        for prop in self.TO_ENFORCE_VALUES:
            assert self.properties[prop] in self.TO_ENFORCE_VALUES[prop], "Invalid property: {} = {}, the value must be one of: {}".format(
                prop, self.properties[prop], self.TO_ENFORCE_VALUES[prop])

        if (self.properties[MAX_USER_DISTANCE] > 2):
            print("WARNING: with {} = {} the ram usage will be >32GB\n".format(
                MAX_USER_DISTANCE, self.properties[MAX_USER_DISTANCE]))

    def _cast(self):
        # allow enter the value in lowercase
        self.properties[DATASET] = self.properties[DATASET].upper()

        for value_type, value_name, enforce_list in zip((int, float, bool), ("int", "float in [0, 1]", "bool"),
                                                        (self.TO_ENFORCE_INT, self.TO_ENFORCE_FLOAT, self.TO_ENFORCE_BOOL)):
            try:
                for prop in enforce_list:
                    self.properties[prop] = value_type(self.properties[prop])
            except:
                raise Exception("Invalid property: {} = {}, the value must be a {}".format(
                    prop, self.properties[prop], value_name))

    def __str__(self):
        p = map(lambda x: "\t{}: {}\n".format(
            x.upper(), self.properties[x]), self.KEYS)
        return "[CONFIGURATION]\n" + "".join(p)

    def __setitem__(self, key, value):
        self.properties[key] = value
        self.check_and_normalize()

    def __getitem__(self, key):
        return self.properties[key]

    def debug(self):
        p = map(lambda x: "\t{}: {} {}\n".format(
            x.upper(), self.properties[x], type(self.properties[x])), self.KEYS)
        return "[DEBUG]\n" + "".join(p)


# global configuration variable!
config = Config.get_instance()

if __name__ == "__main__":
    print(config.debug())
