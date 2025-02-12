import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Wallet {
    //users of the network (but in real there are no users)
    //used for signature
    private PrivateKey privateKey;
    //verification
    //address: RIPMD public key (160 bits)
    private PublicKey publicKey;

    public Wallet() {
        KeyPair keyPair = CryptographyHelper.ellipticCurveCrypto();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }
    //miners of the blockchain will put this transaction into the blockchain
    public Transaction transferMoney(PublicKey receiver, double amount) {
        if(calculateBalance()<amount){
            System.out.println("Insufficient funds");
            return null;
        }
        //store inputs for the transaction in this array
        List<TransactionInput> inputs= new ArrayList<TransactionInput>();

        //find unspent transactions (because the Blockchain stores all UTX0s
        for (Map.Entry<String,TransactionOutput>item: Blockchain.UTX0s.entrySet()){
            TransactionOutput UTX0 = item.getValue();
            if(UTX0.isMine(this.publicKey))
                inputs.add(new TransactionInput(UTX0.getId()));
        }
        Transaction newTransaction= new Transaction(publicKey,receiver, amount, inputs);
        //sender signs the transaction
        newTransaction.generateSignature(privateKey);
        return newTransaction;
    }
    //there is no balance associated with the users
    //UTX0s and consider all the transaction in the past with PrivateKEy (user)
    public double calculateBalance(){
        double balance = 0;
        //iteracja po hashmapie
        for (Map.Entry<String,TransactionOutput>item: Blockchain.UTX0s.entrySet()){
            TransactionOutput transactionOutput = item.getValue();
            //czy transaction należy do danego usera
            if (transactionOutput.isMine(publicKey))
                balance += transactionOutput.getAmount();
        }
        return balance;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
