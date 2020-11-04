package indexer.postingListGenerator.postingList;

import java.util.HashSet;
import java.util.Iterator;

/*
 * Class Name:          PostingList
 * 
 * Member variables:    list   HashSet<PostingListItem>
 * 
 * Member functions:    public HashSet<PostingListItem> getList()
 *                      public static PostingList union(PostingList A, posingList B)
 *                      public static PostingList intersection(PostingList A, PostingList B)
 *                      public String toString()
 * 
 * Description:         The PostingList wraps a HashSet<PostingListItem> (stored as a member variable called list),
 *                      representing a posing list containing tokens from database. Two static methods, union and intersection
 *                      are defined on this class which perform union and intersection operation on two input PostingLists
 *                      and return the result. These methods are used by the evaluator in queryParser to perform bool operation
 *                      upon multiple sub posting lists.
 */
public class PostingList {
    HashSet<PostingListItem> list;

    public PostingList() {
        this.list = new HashSet<PostingListItem>();
    }

    public PostingList(HashSet<PostingListItem> list) {
        this.list = list;
    }

    public HashSet<PostingListItem> getList() {
        return this.list;
    }

    public void appendItem(PostingListItem item) {
        this.list.add(item);
        return;
    }

    public int size() {
        return this.list.size();
    }

    public static PostingList union(PostingList PostingListA, PostingList PostingListB) {

        // clone setA, so that it's original value won't be changed by union operation
        HashSet<PostingListItem> copySetA = new HashSet<>();
        HashSet<PostingListItem> setA = PostingListA.getList();
        copySetA = (HashSet) setA.clone();
        HashSet<PostingListItem> setB = PostingListB.getList();
        copySetA.addAll(setB);
        return new PostingList(copySetA);
    }

    public static PostingList intersection(PostingList PostingListA, PostingList PostingListB) {

        // clone setA, so that it's original value won't be changed by intersection
        // peration
        HashSet<PostingListItem> copySetA = new HashSet<>();
        HashSet<PostingListItem> setA = PostingListA.getList();
        HashSet<PostingListItem> setB = PostingListB.getList();
        copySetA = (HashSet) setA.clone();
        copySetA.retainAll(setB);
        return new PostingList(copySetA);
    }

    public String toString() {
        String outString = "";
        Iterator<PostingListItem> it = this.list.iterator();
        while (it.hasNext()) {
            outString += it.next().toString() + '\n';
        }
        return outString;
    }
}
