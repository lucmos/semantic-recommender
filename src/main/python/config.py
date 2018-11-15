import javaproperties

DIMENSION = "dimension"
CLUSTER_OVER = "cluster_over"

TWEET_IMPORTANCE = "tweet_importance"
PERSONAL_PAGE_IMPORTANCE = "personal_page_importance"
LIKED_ITEMS_IMPORTANCE = "liked_items_importance"
RATE_OF_DECAY = "rate_of_decay"
FOLLOW_OUT_TWEET_IMPORTANCE = "follow_out_tweet_importance"
FOLLOW_OUT_PERSONAL_PAGE_IMPORTANCE = "follow_out_personal_page_importance"
FOLLOW_OUT_LIKED_ITEMS_IMPORTANCE = "follow_out_liked_items_importance"

MAX_USER_DISTANCE = "max_user_distance"
MATRIX_DIMENSIONALITY = "dimensionality"


class Config():
    CONFIG_FILE = "./config/wsie.properties"

    KEYS = [
        DIMENSION,
        CLUSTER_OVER,

        TWEET_IMPORTANCE,
        PERSONAL_PAGE_IMPORTANCE,
        LIKED_ITEMS_IMPORTANCE,
        RATE_OF_DECAY,
        FOLLOW_OUT_TWEET_IMPORTANCE,
        FOLLOW_OUT_PERSONAL_PAGE_IMPORTANCE,
        FOLLOW_OUT_LIKED_ITEMS_IMPORTANCE,

        MAX_USER_DISTANCE,
        MATRIX_DIMENSIONALITY,
    ]

    TO_ENFORCE_VALUES = {
        DIMENSION: {"small", "complete"},
        CLUSTER_OVER: {"categories", "domains"},
    }

    TO_ENFORCE_INT = {
        MAX_USER_DISTANCE,
        MATRIX_DIMENSIONALITY,
    }

    TO_ENFORCE_FLOAT = {
        TWEET_IMPORTANCE,
        PERSONAL_PAGE_IMPORTANCE,
        LIKED_ITEMS_IMPORTANCE,
        RATE_OF_DECAY,
        FOLLOW_OUT_TWEET_IMPORTANCE,
        FOLLOW_OUT_PERSONAL_PAGE_IMPORTANCE,
        FOLLOW_OUT_LIKED_ITEMS_IMPORTANCE,
    }

    instance = None

    @staticmethod
    def get_instance():
        if not Config.instance:
            Config.instance = Config()
        return Config.instance

    def __init__(self, config_file=CONFIG_FILE):
        self.load(config_file)

    def load(self, config_file, verbose=True):
        with open(config_file, 'r') as fp:
            self.properties = javaproperties.load(fp)

        self.cast()
        self.check_validity()
        if(verbose):
            print(self)

    def cast(self):
        for prop in self.TO_ENFORCE_INT:
            self.properties[prop] = int(self.properties[prop])

        for prop in self.TO_ENFORCE_FLOAT:
            self.properties[prop] = float(self.properties[prop])

    def check_validity(self):
        assert len(self.properties) == len(self.KEYS)

        for prop in self.TO_ENFORCE_VALUES:
            assert self.properties[prop] in self.TO_ENFORCE_VALUES[prop]

        for prop in self.TO_ENFORCE_INT:
            assert isinstance(
                self.properties[prop], int), "{}: {}".format(prop, self.properties[prop])

        for prop in self.TO_ENFORCE_FLOAT:
            assert isinstance(
                self.properties[prop], float), "{}: {}".format(prop, self.properties[prop])

    def __str__(self):
        p = map(lambda x: "\t{}: {}\n".format(
            x.upper(), self.properties[x]), self.properties)
        return "[CONFIGURATION]\n" + "".join(p)

    def __setitem__(self, key, value):
        self.properties[key] = value
        self.check_validity()

    def __getitem__(self, key):
        return self.properties[key]


Config.get_instance()
