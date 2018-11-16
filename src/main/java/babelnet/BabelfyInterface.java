package babelnet;

import it.uniroma1.lcl.babelfy.commons.BabelfyConstraints;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;
import it.uniroma1.lcl.babelfy.core.Babelfy;
import it.uniroma1.lcl.jlt.util.Language;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BabelfyInterface {

    public static void main(String[] args) {
        System.out.println(getSynsets("Hello, how are you? Are you feeling well when you play football?"));
    }

    public static Set<String> getSynsets(String text) {
        return new Babelfy().babelfy(text, Language.ANG).stream()
                .map(SemanticAnnotation::getBabelSynsetID).collect(Collectors.toSet());
    }
}
