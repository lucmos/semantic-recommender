package babelnet;

import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.data.BabelCategory;
import it.uniroma1.lcl.babelnet.data.BabelDomain;
import it.uniroma1.lcl.babelnet.resources.WikipediaID;
import it.uniroma1.lcl.jlt.util.Language;
import it.uniroma1.lcl.kb.Domain;
import twittermodel.WikiPageModel;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BabelNetInterface {

    private BabelNet babelnet;

    public BabelNetInterface() {
        this.babelnet = BabelNet.getInstance();
    }

    public BabelSynset getSynset(WikiPageModel wikiPage) {
        assert (wikiPage != null);

        BabelSynset synset = this.babelnet.getSynset(new WikipediaID(wikiPage.getSimpleName(), Language.EN));
        if (synset == null) {
            System.out.println(String.format("Synset not found: %s", wikiPage.getSimpleName()));
            return null;
        }

        assert synset != null;
        return synset;
    }

    public List<BabelCategory> getCategories(BabelSynset synset) {
        assert (synset != null);

        List<BabelCategory> l = synset.getCategories();

        assert l != null;
        assert !l.isEmpty();
        return l;
    }

    public Set<Domain> getDomains(BabelSynset synset) {
        assert synset != null;

        HashMap<Domain, Double> l = synset.getDomains();

        assert l != null;
        assert !l.isEmpty();
        return l.keySet();
    }


}
//System.out.println(BabelNet.getInstance().getSynset(new WikipediaID("Maxene_Magalona", Language.EN)).getCategories());