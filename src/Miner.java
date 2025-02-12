public class Miner {
    // every miner gets 6.25 BTC after the mining
    private double reward;

    public void mine(Block block, Blockchain blockChain) {
        // this is the proof of work (PoW) - PoS
        // it is not energy efficient
        while(!isGoldenHash(block)) {
            block.incrementNonce();
            block.generateHash();
        }

        System.out.println(block+" has just mined...");
        System.out.println("Hash is: "+block.getHash());
        blockChain.addBlock(block);
        reward+=Constants.MINER_REWARD;
    }

    public boolean isGoldenHash(Block block) {
        String leadingZeros = new String(new char[Constants.DIFFICULTY]).replace('\0', '0');
        return block.getHash().substring(0,Constants.DIFFICULTY).equals(leadingZeros);
    }

    public double getReward() {
        return this.reward;
    }
}
