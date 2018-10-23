package babelnet;

import it.uniroma1.lcl.babelnet.*;
import it.uniroma1.lcl.jlt.util.Language;

import java.util.List;
import java.util.stream.Collectors;

public class Test
{
	public static void main(String[] args)
	{
		BabelNet bn = BabelNet.getInstance();
		
		// get all the senses (i.e. words within synsets)
		// for the corresponding word and language
		for (BabelSense s : bn.getSensesContaining("house", Language.EN))
			System.out.println(s);

		// get all the synsets containing "house" in English
		// sorts them by relevance and find the first one
		BabelSynset syn = bn.getSynsets("house", Language.EN).stream()
				.sorted(new BabelSynsetComparator("house", Language.EN))
				.findFirst().get();
		// same for "piano" in Italian
		List<BabelSynset> syn2 = bn.getSynsets("piano", Language.IT).stream()
				.sorted(new BabelSynsetComparator("piano", Language.IT))
				.collect(Collectors.toList());
		
		// get the synset ID
		System.out.println(syn.getID());
		// get a standard printout of the synset
		System.out.println(syn2);
		// get the main sense of a synset
		System.out.println(syn.getMainSense());
		// get the domains of the synset
		System.out.println(syn.getDomains());
		
		// get a synset from its ID
		BabelSynset entitySynset = bn.getSynset(new BabelSynsetID("bn:00031027n"));
		System.out.println(entitySynset);
		// definitions
		System.out.println(entitySynset.getGlosses());
		// Wikipedia categories
		System.out.println(entitySynset.getCategories());
		// senses in Italian
		System.out.println(entitySynset.getSenses(Language.IT));
		
		// get all the WordNet synsets
		// and their neighbours
		// **NOTE**: takes tens of minutes to run (as it iterates over millions of nodes)!!!
		/*
		bn.stream()
			.filter(s -> s.getSenseSources().contains(BabelSenseSource.WN))
			.forEach(bs -> 
				{
					String neighbours = bs.getOutgoingEdges().stream().map(n -> n.getBabelSynsetIDTarget().getID()).collect(Collectors.joining());
					System.out.println(bs.getID()+" -> "+neighbours); 
				});
		 */
		
		// get all the WordNet synset offsets 
		// and their neighbours
		// **NOTE**: takes several minutes to run BUT FASTER THAN ITERATING ON SYNSETS AS ABOVE
		bn.offsetStream()
				.filter(id -> isFromWordNet(id))
				.forEach(id -> 
				{ 
					BabelSynset bs = bn.getSynset(new BabelSynsetID(id));
					String neighbours = bs.getOutgoingEdges().stream()
							.filter(e -> isFromWordNet(e.getBabelSynsetIDTarget().getID()))
							.map(n -> n.getBabelSynsetIDTarget().getID()).collect(Collectors.joining());
					System.out.println(bs.getID()+" -> "+neighbours); 
				});
	}
	
	static public boolean isFromWordNet(String id)
	{
		return id.startsWith("bn:000") || id.startsWith("bn:0010") || id.startsWith("bn:0011");
	}
}
