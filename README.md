# Web and Social Information Extraction
In this work we're mainly dealing with the [WikiMID](http://wikimid.tweets.di.uniroma1.it/wikimid/) dataset.

We have been able to represent each Twitter user and each Wikipedia page in terms of their semantic categories, taken from [BabelNet](https://babelnet.org/) (disambiguating the text with [Babelfy](http://babelfy.org/index) when necessary).

The approach used to build the recommender system is very general: it makes possible to recommend, obtaining a score, whichever item that can be expressed in terms of [BabelNet](https://babelnet.org/) categories, without any further training (e.g. any page of Wikipedia or any synset in [BabelNet](https://babelnet.org/) that has categories associated), possibly without directly knowing the user preferences.

The key idea is the method used to represent the objects in terms of semantic categories:
![](./report/pics/formula.png)

A glimpse of the recommender performances:
![](./report/pics/recomm.png)

## Notes about efficiency
We are dealing with big matrices. The adjacency matrix has dimension 500𝑘×500𝑘, even if it’s a boolean matrix, storing it entirely in memory would mean to require 58GB of RAM (assuming no overhead). The situation is even worse when we consider the other matrices that have dimension 500𝑘×190𝑘, each one of them would require 353GB of RAM (assuming no overhead) since they contain float32 and not booleans.
It’s obvious that it is not feasible to store in memory those dense matrices. Fortunately, we can exploit one common property: they are sparse matrices.
We used SciPy. Sparse to work with these matrices, and we were able to fit them, and the computations, in a 32GB machine.
However, using this approach lead to some limitations: not all the functions made available by sklearn accept a sparse matrix, and not all the functions scale well enough memory wise. So, our choices  ere limited by the available options.
One of the main motivations that lead us to continue the project in python and not in java, was that java hasn’t a good enough, easy to use, library to handle sparse matrices, algebraic computations and machine learning tools. Instead in python we could leverage SciPy, NumPy and sklearn that, together, met all our needs.


### Adjacency matrix exponentiation
Must be noted, as already explained, that performing the 𝑖-power of the adjacency matrix gives us the nodes at distance 𝑖. Given the structure of the graph that one could expect from twitter users (some people have a lot of edges, following the Zipf law) it is straightforward to see that the initially sparse adjacency matrix would become exponentially less sparse at each exponentiation. On our 32GB machine we were able to compute only the square of the adjacency matrix.
Probably the operations could be optimized in some way, but we think that the importance of information decreases exponentially as it travels from friend to friend, and we enforced this behaviour setting an exponential decay in the formula. So, what we computed should give us a good enough approximation of an optimal computation (where higher powers of the adjacency matrix are computed).

___

More information in the full [report](https://github.com/LucaMoschella/WSIEProject/blob/master/report/tosend/WSIE_project_report_moschella_spini.pdf).
