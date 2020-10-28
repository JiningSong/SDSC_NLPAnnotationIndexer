package indexer.postingListGenerator.utils;

import java.util.Hashtable;

public class NormalizedAnnotationLables {
    public static Hashtable<String, String> uposDict = new Hashtable<String, String>() {
        {
            put("ADJ", "(ADJ\\s)");
            put("ADP", "(ADP\\s)");
            put("ADV", "(ADV\\s)");
            put("AUX", "(AUX\\s)");
            put("CCONJ", "(CCONJ\\s)");
            put("DET", "(DET\\s)");
            put("INTJ", "(INTJ\\s)");
            put("NOUN", "(NOUN\\s)");
            put("NUM", "(NUM\\s)");
            put("PART", "(PART\\s)");
            put("PRON", "(PRON\\s)");
            put("PROPN", "(PROPN\\s)");
            put("PUNCT", "(PUNCT\\s)");
            put("SCONJ", "(SCONJ\\s)");
            put("SYM", "(SYM\\s)");
            put("VERB", "(VERB\\s)");
            put("X", "(X\\s)");
        }
    };

    public static Hashtable<String, String> xposDict = new Hashtable<String, String>() {
        {
            put("CC", "(CC\\s)");
            put("CD", "(CD\\s)");
            put("DT", "(DT\\s)");
            put("EX", "(EX\\s)");
            put("FW", "(FW\\s)");
            put("IN", "(IN\\s)");
            put("JJ", "(JJ\\s)");
            put("JJR", "(JJR\\s)");
            put("JJS", "(JJS\\s)");
            put("LS", "(LS\\s)");
            put("MD", "(MD\\s)");
            put("NN", "(NN\\s)");
            put("NNS", "(NNS\\s)");
            put("NNP", "(NNP\\s)");
            put("NNPS", "(NNPS\\s)");
            put("PDT", "(PDT\\s)");
            put("POS", "(POS\\s)");
            put("PRP", "(PRP\\s)");
            put("RB", "(RB\\s)");
            put("RBR", "(RBR\\s)");
            put("RBS", "(RBS\\s)");
            put("RP", "(RP\\s)");
            put("SYM", "(SYM\\s)");
            put("TO", "(TO\\s)");
            put("UH", "(UH\\s)");
            put("VB", "(VB\\s)");
            put("VBD", "(VBD\\s)");
            put("VBG", "(VBG\\s)");
            put("VBP", "(VBP\\s)");
            put("VBZ", "(VBZ\\s)");
            put("WDT", "(WDT\\s)");
            put("WP", "(WP\\s)");
            put("WRB", "(WRB\\s)");
            put("VBN$", "(VBN$\\s)");
            put("WP$", "(WP$\\s)");
            put("PRP$", "(PRP$\\s)");
        }
    };

    public static Hashtable<String, String> NERDict = new Hashtable<String, String>() {
        {
            put("B-CARDINAL", "(B-CARDINAL\\s)");
            put("B-DATE", "(B-DATE\\s)");
            put("B-EVENT", "(B-EVENT\\s)");
            put("B-FAC", "(B-FAC\\s)");
            put("B-GPE", "(B-GPE\\s)");
            put("B-LANGUAGE", "(B-LANGUAGE\\s)");
            put("B-LAW", "(B-LAW\\s)");
            put("B-LOC", "(B-LOC\\s)");
            put("B-MONEY", "(B-MONEY\\s)");
            put("B-NORP", "(B-NORP\\s)");
            put("B-ORG", "(B-ORG\\s)");
            put("B-PERCENT", "(B-PERCENT\\s)");
            put("B-PERSON", "(B-PERSON\\s)");
            put("B-PRODUCT", "(B-PRODUCT\\s)");
            put("B-QUANTITY", "(B-QUANTITY\\s)");
            put("B-TIME", "(B-TIME\\s)");
            put("B-WORK_OF_ART", "(B-WORK_OF_ART\\s)");
            put("E-CARDINAL", "(E-CARDINAL\\s)");
            put("E-DATE", "(E-DATE\\s)");
            put("E-EVENT", "(E-EVENT\\s)");
            put("E-FAC", "(E-FAC\\s)");
            put("E-GPE", "(E-GPE\\s)");
            put("E-LANGUAGE", "(E-LANGUAGE\\s)");
            put("E-LAW", "(E-LAW\\s)");
            put("E-LOC", "(E-LOC\\s)");
            put("E-MONEY", "(E-MONEY\\s)");
            put("E-NORP", "(E-NORP\\s)");
            put("E-ORG", "(E-ORG\\s)");
            put("E-PERCENT", "(E-PERCENT\\s)");
            put("E-PERSON", "(E-PERSON\\s)");
            put("E-PRODUCT", "(E-PRODUCT\\s)");
            put("E-QUANTITY", "(E-QUANTITY\\s)");
            put("E-TIME", "(E-TIME\\s)");
            put("E-WORK_OF_ART", "(E-WORK_OF_ART\\s)");
            put("I-CARDINAL", "(I-CARDINAL\\s)");
            put("I-DATE", "(I-DATE\\s)");
            put("I-EVENT", "(I-EVENT\\s)");
            put("I-FAC", "(I-FAC\\s)");
            put("I-GPE", "(I-GPE\\s)");
            put("I-LANGUAGE", "(I-LANGUAGE\\s)");
            put("I-LAW", "(I-LAW\\s)");
            put("I-LOC", "(I-LOC\\s)");
            put("I-MONEY", "(I-MONEY\\s)");
            put("I-NORP", "(I-NORP\\s)");
            put("I-ORG", "(I-ORG\\s)");
            put("I-PERCENT", "(I-PERCENT\\s)");
            put("I-PERSON", "(I-PERSON\\s)");
            put("I-PRODUCT", "(I-PRODUCT\\s)");
            put("I-QUANTITY", "(I-QUANTITY\\s)");
            put("I-TIME", "(I-TIME\\s)");
            put("I-WORK_OF_ART", "(I-WORK_OF_ART\\s)");
            put("O>", "(O\\s)");
            put("S-CARDINAL", "(S-CARDINAL\\s)");
            put("S-DATE", "(S-DATE\\s)");
            put("S-EVENT", "(S-EVENT\\s)");
            put("S-FAC", "(S-FAC\\s)");
            put("S-GPE", "(S-GPE\\s)");
            put("S-LANGUAGE", "(S-LANGUAGE\\s)");
            put("S-LAW", "(S-LAW\\s)");
            put("S-LOC", "(S-LOC\\s)");
            put("S-MONEY", "(S-MONEY\\s)");
            put("S-NORP", "(S-NORP\\s)");
            put("S-ORDINAL", "(S-ORDINAL\\s)");
            put("S-ORG", "(S-ORG\\s)");
            put("S-PERSON", "(S-PERSON\\s)");
            put("S-PRODUCT", "(S-PRODUCT\\s)");
            put("S-QUANTITY", "(S-QUANTITY\\s)");
            put("S-TIME", "(S-TIME\\s)");
            put("S-WORK_OF_ART", "(S-WORK_OF_ART\\s)");
        }

    };

    public static Hashtable<String, String> depparseDict = new Hashtable<String, String>() {
        {
            put("acl", "(acl\\s)");
            put("advcl", "(advcl\\s)");
            put("advmod", "(advmod\\s)");
            put("amod", "(amod\\s)");
            put("appos", "(appos\\s)");
            put("aux", "(aux\\s)");
            put("case", "(case\\s)");
            put("cc", "(cc\\s)");
            put("ccomp", "(ccomp\\s)");
            put("compound", "(compound\\s)");
            put("conj", "(conj\\s)");
            put("cop", "(cop\\s)");
            put("csubj", "(csubj\\s)");
            put("dep", "(dep\\s)");
            put("det", "(det\\s)");
            put("discourse", "(discourse\\s)");
            put("dislocated", "(dislocated\\s)");
            put("expl", "(expl\\s)");
            put("fixed", "(fixed\\s)");
            put("flat", "(flat\\s)");
            put("goeswith", "(goeswith\\s)");
            put("iobj", "(iobj\\s)");
            put("list", "(list\\s)");
            put("mark", "(mark\\s)");
            put("nmod", "(nmod\\s)");
            put("nsubj", "(nsubj\\s)");
            put("nummod", "(nummod\\s)");
            put("obj", "(obj\\s)");
            put("obl", "(obl\\s)");
            put("orphan", "(orphan\\s)");
            put("parataxis", "(parataxis\\s)");
            put("punct", "(punct\\s)");
            put("reparandum", "(reparandum\\s)");
            put("root", "(root\\s)");
            put("vocative", "(vocative\\s)");
            put("xcomp", "(xcomp\\s)");
            put("acl:relcl", "(acl:relcl\\s)");
            put("cc:preconj", "(cc:preconj\\s)");
            put("compound:prt", "(compound:prt\\s)");
            put("csubj:pass", "(csubj:pass\\s)");
            put("aux:pass", "(aux:pass\\s)");
            put("det:predet", "(det:predet\\s)");
            put("flat:foreign", "(flat:foreign\\s)");
            put("nmod:npmod", "(nmod:npmod\\s)");
            put("nmod:poss", "(nmod:poss\\s)");
            put("nmod:tmod", "(nmod:tmod\\s)");
            put("nsubj:pass", "(nsubj:pass\\s)");
            put("obl:npmod", "(obl:npmod\\s)");
            put("obl:tmod", "(obl:tmod\\s)");

        }
    };
}