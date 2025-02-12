import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {

    private int id;
    private int nonce;
    private long timeStamp;
    private String hash;
    private String previousHash;
    public List<Transaction> transactions;


    public Block(String previousHash ) {
        this.transactions=new ArrayList<>();
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        generateHash();
    }

    public void generateHash() {
        String dataToHash = Integer.toString(id)+previousHash+Long.toString(timeStamp)+transactions.toString()+Integer.toString(nonce);
        this.hash = CryptographyHelper.generateHash(dataToHash) ;
    }

    public boolean addTransaction(Transaction transaction) {
        if(transaction==null) return false;

        //if the block is not the genesis block do not process
        if((!previousHash.equals(Constants.GENESIS_PREV_HASH))){
            if((!transaction.verifyTransaction())){
                System.out.println("Transaction verification failed");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction added"+this);
        return true;
    }

    // the nonce is the only parameter the miners can tune (change)
    public void incrementNonce() {
        this.nonce++;
    }

    // this SHA-256 hash identifies the block
    public String getHash() {
        return this.hash;
    }
}
