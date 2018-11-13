import time


class Chrono:
    _idd = 0
    _indent = []

    """
    A class that implements a simple timer for the elapsed time, measured in millis.
    """

    def __init__(self, initial_message="Starting...", indicator=True):
        """
        Creates the chronometer and starts it

        :param indicator: print guidelines
        :param initial_message: initial message to print
        :param new_line: True if the print must include a new line
        :param final_message: message to print end called by end
        """
        self.initial_message = initial_message
        self.current_milli_time = lambda: int(round(time.time() * 1000))
        self.start_time = self.current_milli_time()

        self._local_id = Chrono._idd
        Chrono._indent.append(self._local_id)
        self.local_indent = len(Chrono._indent) - 1
        Chrono._idd += 1

        print(("|\t" if indicator else "\t") * self.local_indent + initial_message)

    def millis(self, final_message=None, indicator=True, verbose=False):
        """
        Print the final message. Print other optional information and checks the elapsed time

        :param indicator: print guidelines
        :param verbose: print full information if true
        :param final_message: additional information whether to print a message or not
        :return: the elapsed time
        """
        elapsed = self.current_milli_time() - self.start_time
        print("{}{}done{} (in {} millis)".format(("|\t" if indicator else "\t") * self.local_indent,
                                                 self.initial_message + " " if verbose else "",
                                                 ", {}".format(final_message) if final_message else "",
                                                 elapsed))
        if Chrono._indent and Chrono._indent[-1] == self._local_id:
            Chrono._indent.pop()
        return elapsed
