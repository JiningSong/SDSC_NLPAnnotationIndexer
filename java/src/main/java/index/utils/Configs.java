package index.utils;

public class Configs {

    /* Query Strings */
    public static final String SENT_QUERY = "SELECT * FROM sentence_segmentation_1000";
    public static final String POS_QUERY = "SELECT * FROM pos_annotations_1000 WHERE sent_id = cast (? as bigint)";
    public static final String NER_QUERY = "SELECT * FROM ner_annotations_1000 WHERE sent_id = cast (? as bigint)";
    public static final String DEPPARSE_QUERY = "SELECT * FROM depparse_annotations_1000 WHERE sent_id = cast (? as bigint)";

    /* Database configs */
    public static final String DB_DRIVER = "org.postgresql.Driver";
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/stanford_corenlp_results";
    public static final String DB_USERNAME = "postgres";
    public static final String DB_PASSWORD = "";
}

