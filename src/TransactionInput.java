public class TransactionInput {
    private String transactionOutputId;
    private TransactionOutput UTX0; //unspent transaction output

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
    public String getTransactionOutputId() {
        return transactionOutputId;
    }

    public void setTransactionOutputId(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public TransactionOutput getUTX0() {
        return UTX0;
    }

    public void setUTX0(TransactionOutput UTX0) {
        this.UTX0 = UTX0;
    }
}
