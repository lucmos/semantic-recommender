package babelnet;

import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;
import it.uniroma1.lcl.babelfy.core.Babelfy;
import it.uniroma1.lcl.jlt.util.Language;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BabelfyInterface {

    public static void main(String[] args) {
        String a = "son pocas de las cosas que puedo decirlo, pero de esto, en mi mente no hay otra opción.";
        System.out.println(disambiguate(a));
    }

    public static List<String> disambiguate(String text) {
        return new Babelfy().babelfy(text, Language.AGNOSTIC).stream()
                .map(SemanticAnnotation::getBabelSynsetID).collect(Collectors.toList());
    }
}
