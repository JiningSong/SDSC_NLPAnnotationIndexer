package indexer.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.javatuples.Triplet;

import indexer.postingListGenerator.postingList.PostingList;
import indexer.postingListGenerator.postingList.PostingListItem;

import java.util.List;
import java.util.Hashtable;
import java.util.Iterator;

public class Utils {
    /**
     * Function Name: createFileWriter
     * 
     * Description: Construct a FileWriter object for 'fileName' in 'append' mode
     * 
     * @param fileName name of the file that FW opens
     * @param append   mode (r or w)
     * 
     * @return FileWriter for the according file or null if exception occurs
     */
    public static FileWriter createFileWriter(String fileName, boolean append) {
        try {
            File myObj = new File(fileName);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter fw = new FileWriter(myObj, append);
            if (fw != null) {
                return fw;
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    public static int savePostingListOnDisk(String query, PostingList results) throws IOException {

        String POSTING_LIST_PATH_PATTERN = "%s/src/main/java/indexer/out/PL-%s.out";
        String POSTING_LIST_TOKENS_PATH_PATTERN = "%s/src/main/java/indexer/out/PLT-%s.out";
        String CURRENT_DIR = System.getProperty("user.dir");

        FileWriter postingListWriter = Utils
                .createFileWriter(String.format(POSTING_LIST_PATH_PATTERN, CURRENT_DIR, query), false);

        if (postingListWriter == null)
            return 0;

        FileWriter postingListTokensWriter = Utils
                .createFileWriter(String.format(POSTING_LIST_TOKENS_PATH_PATTERN, CURRENT_DIR, query), false);

        if (postingListTokensWriter == null) {
            postingListWriter.close();
            return 0;
        }

        Iterator<PostingListItem> iterator = results.iterator();

        while (iterator.hasNext()) {

            PostingListItem postingListItem = iterator.next();

            String docId = postingListItem.getDocId();
            String sentId = postingListItem.getSentId();
            postingListTokensWriter.write(String.format("%s\n%s\n", docId, sentId));

            List<Triplet<String, String, Integer>> tokens = postingListItem.getTokensList();
            int tokensCount = tokens.size();
            postingListTokensWriter.write(String.format("%d\n", tokensCount));

            for (int i = 0; i < tokensCount; i++) {
                postingListTokensWriter.write(String.format("%s\n%s\n%d\n", tokens.get(i).getValue0(),
                        tokens.get(i).getValue1(), tokens.get(i).getValue2()));
            }

            postingListTokensWriter.write("\n");

            String tokensText = postingListItem.getTokensText();
            postingListWriter.write(String.format("%s\n", tokensText));
        }

        postingListTokensWriter.close();
        postingListWriter.close();
        System.out.println();
        return 1;
    }
}
