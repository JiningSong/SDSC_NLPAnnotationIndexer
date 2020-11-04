package indexer.postingListGenerator.postingList;

import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;

/*
 * Class Name:          PostingListItem
 * 
 * Member variables:    value   Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> 
 * 
 * Member functions:    public boolean equals(Object obj)
 *                      public int hashCode()
 *                      public String toString()
 * 
 * Description:         The PostingListItem class wraps a javatuples.Pair object inside representing 
 *                      an entry in the posting list. 'equals' and 'hashCode' methods are overrided 
 *                      in order for the PostingListItems to be able to correctly stored in a HashSet
 */
public class PostingListItem {
    private Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> value;

    public PostingListItem(Pair<Triplet<String, String, List<Triplet<String, String, Integer>>>, String> value) {
        this.value = value;
    }

    // Override equals which now compares two PostingListItem by the string
    // Pair stored in it (in raw string formmat)
    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        PostingListItem test = (PostingListItem) obj;
        return (this.value.toString().equals(test.value.toString()));
    }

    // Override hashCode which now is generatted by concatenating the docId,
    // sentId, and tokenIds stored in the Pair inside of PostingListItem. then
    // return concatenatedString.hashCode()
    @Override
    public int hashCode() {
        Triplet<String, String, List<Triplet<String, String, Integer>>> PostingList = this.value.getValue0();
        String docId = PostingList.getValue0();
        String sentId = PostingList.getValue1();
        List<Triplet<String, String, Integer>> tokens = PostingList.getValue2();
        int tokensCount = tokens.size();
        String appendedIdString = docId + sentId;
        for (int i = 0; i < tokensCount; i++) {
            appendedIdString += String.valueOf(tokens.get(i).getValue2());
        }
        return appendedIdString.hashCode();

    }

    public String toString() {
        return this.value.toString();
    }
}
