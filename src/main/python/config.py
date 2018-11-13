import javaproperties


class Config():
    CONFIG_FILE = "./config/wsie.properties"

    DIMENSION = "dimension"
    CLUSTER_METHOD = "cluster_method"
    CLUSTER_OVER = "cluster_over"
    DISTANCE = "distance"

    VALUES = {
        DIMENSION: {"small", "complete"},
        CLUSTER_METHOD: {"tf_idf", "most_common"},
        CLUSTER_OVER: {"categories", "domains"},
        DISTANCE: {"jaccard", "cosine"}
    }

    instance = None

    @staticmethod
    def get_instance():
        if not Config.instance:
            Config.instance = Config()
        return Config.instance

    def __init__(self, config_file=CONFIG_FILE):
        self.load(config_file)

    def current_config(self):
        p = map(lambda x: "\t{}: {}\n".format(
            x.upper(), self.properties[x]), self.properties)
        return "[CONFIGURATION]\n" + "".join(p)

    def load(self, config_file, verbose=True):
        with open(config_file, 'r') as fp:
            self.properties = javaproperties.load(fp)

        self.check_validity()
        if(verbose):
            print(self.current_config())

    def check_validity(self):
        assert len(self.properties) == len(self.VALUES)
        for prop in self.properties:
            assert prop in self.VALUES
            assert self.properties[prop] in self.VALUES[prop]

    def dimension(self):
        return self.properties[self.DIMENSION]

    def cluster_method(self):
        return self.properties[self.CLUSTER_METHOD]

    def cluster_over(self):
        return self.properties[self.CLUSTER_OVER]


if __name__ == "__main__":
    c = Config()
