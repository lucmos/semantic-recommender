import os
import pickle
import json

from sklearn.externals import joblib
from scipy.sparse import load_npz, save_npz

JOBLIB = ".joblib"
PICKLE = ".picke"
JSON = ".json"
NPZ = ".pnz"
TXT = ".txt"


def load_sparse_npz(filename):
    try:
        return load_npz(filename)
    except IOError:
        filename += NPZ
        return load_npz(filename)


def save_sparse_npz(filename, M):
    filename += NPZ
    save_npz(filename, M, compressed=False)


def load_joblib(filename, folder="."):
    filename = os.path.join(folder, filename + JOBLIB)
    if os.path.isfile(filename):
        with open(filename, 'rb') as handle:
            return joblib.load(handle)
    raise IOError("File not found: {}".format(filename))


def save_joblib(obj, filename, folder=".", override=False):
    filename = os.path.join(folder, filename + JOBLIB)
    if os.path.isfile(filename) and not override:
        filename = "avoid_overwriting_" + filename  # to not lose info for distraction

    os.makedirs(os.path.dirname(filename), exist_ok=True)
    with open(filename, 'wb') as handle:
        joblib.dump(obj, handle, protocol=pickle.HIGHEST_PROTOCOL)


def load_pickle(filename, folder="."):
    """
    Loads a given pickle file
    :param filename: the pickle file name
    :param folder: the default folder
    :return: the loaded data
    """
    filename = os.path.join(folder, filename + PICKLE)
    if os.path.isfile(filename):
        with open(filename, 'rb') as handle:
            return pickle.load(handle)
    raise IOError("File not found: {}".format(filename))


def save_pickle(obj, filename, folder=".", override=False):
    """
    Save a object to a pickle file with the highest protocol available
    :param obj: object to save
    :param folder: the default folder
    :param filename: pickle file name
    :param override: True if must replace existing file
    """
    filename = os.path.join(folder, filename + PICKLE)
    if os.path.isfile(filename) and not override:
        filename = "avoid_overwriting_" + filename  # to not lose info for distraction

    os.makedirs(os.path.dirname(filename), exist_ok=True)
    with open(filename, 'wb') as handle:
        pickle.dump(obj, handle, protocol=pickle.HIGHEST_PROTOCOL)


def load_json(filename):
    """
        Loads a given json file
        :param filename: the json file name
        :return: the loaded data
    """
    filename += JSON
    if os.path.isfile(filename):
        with open(filename, 'r') as handle:
            return json.load(handle)
    raise IOError("File not found: {}".format(filename))


def save_json(obj, filename, override=False):
    """
    Save a object to a json file
    :param obj: object to save
    :param filename: json file name
    :param override: True if must replace existing file
    """
    filename += JSON

    if os.path.isfile(filename) and not override:
        filename = "avoid_overwriting_" + filename  # to not lose info for distraction

    os.makedirs(os.path.dirname(filename), exist_ok=True)
    with open(filename, "w") as handle:
        json.dump(obj, handle, indent=4, sort_keys=True)


def save_string(string, filename, override=False):
    """
      Save a string to a plain file
      :param string: string to save
      :param filename: file name
      :param override: True if must replace existing file
      """
    filename += TXT
    if os.path.isfile(filename) and not override:
        filename = "avoid_overwriting_" + filename  # to not lose info for distraction

    os.makedirs(os.path.dirname(filename), exist_ok=True)
    with open(filename, "w") as handle:
        handle.write(string)


def min_max_normalization(array):
    """
    Performs a min-max normalization on the array
    :param array: the array to normalize
    :return: the normalized array
    """
    minx = min(array)
    maxx = max(array)
    return [(x - minx) / (maxx - minx) for x in array]
