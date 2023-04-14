import java.util.ArrayList;


/**
 * This holds the values for a Block in the Blockchain, and it has methods to compute the Merkle Root and generate a hash.
 */
public class Block {


    private String sMerkleRoot;
    private final int iDifficulty = 5; // Mining seconds in testing 5: 6,10,15,17,20,32 | testing 6: 12,289,218
    private String sNonce;
    private String sMinerUsername;
    private String sHash;



    /**
     * This computes the Merkle Root. It either accepts 2 or 4 items, or if made to be dynamic, then accepts any
     * multiple of 2 (2,4,8.16.32,etc.) items
     * @param lstItems Strings
     * @return String as MerkleRoot
     */
    public synchronized String computeMerkleRoot(ArrayList<String> lstItems) {

        ArrayList<String> merkleTree = new ArrayList<>();
        int num = lstItems.size();

        //check for valid length of lstItems
        //loop checks for multiple of 2,  10 and 14 are examples of even#'s that are not accepted.
        while( num != 1 ){
            if((num % 2) != 0 || lstItems.size() < 2){
                System.out.println("[ERROR] lstItem's size is not multiple of 2...");
                System.out.println("...Exiting Program.");
                System.exit(0);
            }
            else{
                num = num /2 ;
            }
        }


        //hash listItems and add them to merkleTree<>
        for(int i = 0; i < lstItems.size(); i++){
            merkleTree.add(BlockchainUtil.generateHash(lstItems.get(i)));
        }

        //computes merkleRoot
        while(merkleTree.size() != 1){
            for(int i = 0; i < merkleTree.size(); i++){
                //concat & hash() pairs of two and store in larger index
                //ie: [0] = ([0]+[1]), then pop 1
                // [1] = ([1]+[2]) then pop2 ...
                // [2] = ([2]+[3]) ...and so on until size == 1
                String tempHash = (BlockchainUtil.generateHash(merkleTree.get(i) + merkleTree.get(i+1)));
                merkleTree.set(i, tempHash); //merkleTree[i] == tempHash
                merkleTree.remove(i+1);  //removes larger index
            }
        }


        return merkleTree.get(0); //merkleTree.size() == 1

    }



    /**
     * This method populates a Merkle node's left, right, and hash variables.
     * @param oNode MerkleNode
     * @param oLeftNode MerkleNode
     * @param oRightNode MerkleNode
     */
    //My approach didn't require using this method
    private void populateMerkleNode(MerkleNode oNode, MerkleNode oLeftNode, MerkleNode oRightNode){


        oNode.oLeft = oLeftNode;
        oNode.oRight = oRightNode;
        oNode.sHash = BlockchainUtil.generateHash(oNode.oLeft.sHash + oNode.oRight.sHash);
    }



    /**
     * This generates the hash for this block by combining the properties and hashing them.
     * @return String hash
     */
    public String computeHash() {

        return BlockchainUtil.generateHash(sMerkleRoot + iDifficulty + sMinerUsername + sNonce);
    }



    public int getDifficulty() {
        return iDifficulty;
    }


    public String getNonce() {
        return sNonce;
    }
    public void setNonce(String nonce) {
        this.sNonce = nonce;
    }

    public void setMinerUsername(String sMinerUsername) {
        this.sMinerUsername = sMinerUsername;
    }

    public String getHash() { return sHash; }
    public void setHash(String h) {
        this.sHash = h;
    }

    public synchronized void setMerkleRoot(String merkleRoot) { this.sMerkleRoot = merkleRoot; }




    /**
     * Run this to test your merkle tree logic.
     * @param args ..
     */
    public static void main(String[] args){

        ArrayList<String> lstItems = new ArrayList<>();
        Block oBlock = new Block();
        String sMerkleRoot;

        // These merkle root hashes based on "t1","t2" for two items, and then "t3","t4" added for four items.
        String sExpectedMerkleRoot_2Items = "3269f5f93615478d3d2b4a32023126ff1bf47ebc54c2c96651d2ac72e1c5e235";
        String sExpectedMerkleRoot_4Items = "e08f7b0331197112ff8aa7acdb4ecab1cfb9497cbfb84fb6d54f1cfdb0579d69";

        lstItems.add("t1");
        lstItems.add("t2");



        // *** BEGIN TEST 2 ITEMS ***

        sMerkleRoot = oBlock.computeMerkleRoot(lstItems);

        if(sMerkleRoot.equals(sExpectedMerkleRoot_2Items)){

            System.out.println("Merkle root method for 2 items worked!");
        }

        else{
            System.out.println("Merkle root method for 2 items failed!");
            System.out.println("Expected: " + sExpectedMerkleRoot_2Items);
            System.out.println("Received: " + sMerkleRoot);

        }


        // *** BEGIN TEST 4 ITEMS ***

        lstItems.add("t3");
        lstItems.add("t4");
        sMerkleRoot = oBlock.computeMerkleRoot(lstItems);

        if(sMerkleRoot.equals(sExpectedMerkleRoot_4Items)){

            System.out.println("Merkle root method for 4 items worked!");
        }

        else{
            System.out.println("Merkle root method for 4 items failed!");
            System.out.println("Expected: " + sExpectedMerkleRoot_4Items);
            System.out.println("Received: " + sMerkleRoot);

        }
    }
}