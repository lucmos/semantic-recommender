import javaproperties
import json


class Config():

    CONFIG_FILE = "./config/wsie.properties"

    DIMENSION = "dimension"
    CLUSTER_METHOD = "cluster_method"
    CLUSTER_OVER = "cluster_over"

    VALUES = {
        DIMENSION: {"small", "complete"},
        CLUSTER_METHOD: {"tf_idf", "most_common"},
        CLUSTER_OVER: {"categories", "domains"}
    }

    instance = None

    @staticmethod
    def get_instance():
        if not Config.instance:
            Config.instance = Config()
        return Config.instance

    def __init__(self, config_file=CONFIG_FILE):
        self.load(config_file)
        print(self.current_config())

    def current_config(self):
        s = "[CONFIGURATION]\n"
        p = map(lambda x: "\t{}: {}\n".format(
            x, self.properties[x]), self.properties)

        return s + "".join(p)

    def load(self, config_file):
        with open(config_file, 'r') as fp:
            self.properties = javaproperties.load(fp)

        self.check_validity()

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
    print("ciao")
    print(c.properties)
