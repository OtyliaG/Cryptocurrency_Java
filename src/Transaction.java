import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private String transactionId;   //id is a hash
    private PublicKey sender;
    private PublicKey receiver;
    private double amount;
    private byte[] signature;   //make sure the transaction is signed to prevent anyone else from spending the coins
    public List<TransactionInput> inputs;
    public List<TransactionOutput> outputs;

    public Transaction(PublicKey sender, PublicKey receiver, double amount, List<TransactionInput> inputs) {
        this.inputs= new ArrayList<TransactionInput>();
        this.outputs = new ArrayList<TransactionOutput>();
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.inputs = inputs;
        calculateHash();
    }
    public boolean verifyTransaction(){
        if(!verifySignature()){
            System.out.println("Signature verification failed");
            return false;
        }
        //gathering uspent amount
        for(TransactionInput transactionInput: inputs){
            transactionInput.setUTX0(Blockchain.UTX0s.get(transactionInput.getTransactionOutputId()));
        }
        //send value to recipient
        outputs.add(new TransactionOutput(this.receiver,amount,transactionId));
        //send the left over 'change' back to sender
        outputs.add(new TransactionOutput(this.sender,getInputsSum()-amount,transactionId));

        //outputs will be inputs for othr transactions- so put them in blockchain's UTX0
        for(TransactionOutput transactionOutput: outputs){
            Blockchain.UTX0s.put(transactionOutput.getId(), transactionOutput);
        }
        for(TransactionInput transactionInput : inputs)
            if(transactionInput.getUTX0() != null)
                Blockchain.UTX0s.remove(transactionInput.getUTX0().getId());

        return true;

    }
    //how much money sender has
    public double getInputsSum(){
        double sum=0;
        for(TransactionInput transactionInput: inputs)
            if(transactionInput.getUTX0()!=null)
                sum+= transactionInput.getUTX0().getAmount();

            return sum;
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = sender.toString()+receiver.toString()+Double.toString(amount);
        signature= CryptographyHelper.sign(privateKey,data);
    }
    public boolean verifySignature() {
        String data = sender.toString()+receiver.toString()+Double.toString(amount);
        return CryptographyHelper.verify(sender,data,signature);
    }


    private void calculateHash() {
        String hashData = sender.toString() + receiver.toString() + Double.toString(amount);
        this.transactionId = CryptographyHelper.generateHash(hashData); //generating transactionId based on zmiennych sender , transactionId - to identify transaction
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PublicKey getReceiver() {
        return receiver;
    }

    public void setReceiver(PublicKey receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public List<TransactionInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<TransactionInput> inputs) {
        this.inputs = inputs;
    }

    public List<TransactionOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TransactionOutput> outputs) {
        this.outputs = outputs;
    }

    public PublicKey getSender() {
        return sender;
    }

    public void setSender(PublicKey sender) {
        this.sender = sender;
    }
}
