import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blockchain {
    // this is the public ledger - everyone can access
    // all the blocks (previous transactions) on the blockchain
    public static List<Block> blockChain;
    // we store every unspent transactions
    public static Map<String, TransactionOutput> UTX0s;

    public Blockchain() {
        Blockchain.UTX0s = new HashMap<String, TransactionOutput>();
        Blockchain.blockChain = new ArrayList<>();
    }

    public void addBlock(Block block) {
        Blockchain.blockChain.add(block);
    }

public int size() {
        return Blockchain.blockChain.size();
}
    @Override
    public String toString() {

        String blockChain = "";

        for(Block block : Blockchain.blockChain)
            blockChain+=block.toString()+"\n";

        return blockChain;
    }
}
