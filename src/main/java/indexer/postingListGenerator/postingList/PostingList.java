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

    PostingList() {
        this.list = new HashSet<PostingListItem>();
    }

    PostingList(HashSet<PostingListItem> list) {
        this.list = list;
    }

    public HashSet<PostingListItem> getList() {
        return this.list;
    }

    public static PostingList union(PostingList PostingListA, PostingList PostingListB) {
        HashSet<PostingListItem> setA = PostingListA.getList();
        HashSet<PostingListItem> setB = PostingListB.getList();
        setA.addAll(setB);
        return new PostingList(setA);
    }

    public static PostingList intersection(PostingList PostingListA, PostingList PostingListB) {
        HashSet<PostingListItem> setA = PostingListA.getList();
        HashSet<PostingListItem> setB = PostingListB.getList();
        setA.retainAll(setB);
        return new PostingList(setA);
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
