package pt.um.lei.masb.blockchain;

import org.openjdk.jol.info.ClassLayout;
import pt.um.lei.masb.blockchain.data.SensorData;
import pt.um.lei.masb.blockchain.utils.Crypter;
import pt.um.lei.masb.blockchain.utils.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.concurrent.atomic.AtomicLong;

public class Transaction implements Sizeable {
    private static Crypter crypter = StringUtil.getDefaultCrypter();
    // a rough count of how many transactions have been generated.
    private static AtomicLong sequence = new AtomicLong(0);
    // this is also the hash of the transaction.
    private final String transactionId;
    // Agent's pub key.
    private final PublicKey publicKey;
    private final SensorData sd;

    // this is to identify unequivocally an agent.
    private byte[] signature;

    private transient long byteSize;


    public Transaction(PublicKey from, SensorData sd) {
        this.publicKey = from;
        this.sd = sd;
        this.transactionId = calculateHash();
        byteSize = ClassLayout.parseClass(this.getClass()).instanceSize() +
                sd.getApproximateSize();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public byte[] getSignature() {
        return signature;
    }

    public SensorData getSensorData() {
        return sd;
    }


    // This Calculates the transaction hash (which will be used as its Id)
    private String calculateHash() {
        //Increase the sequence to avoid 2 identical transactions having the same hash
        return crypter.applyHash(StringUtil.getStringFromKey(publicKey) +
                                 sd.toString() +
                                 sequence.incrementAndGet());
    }

    /**
     * Signs the sensor data using the public key.
     */
    public void generateSignature(PrivateKey privateKey) {
        var data = StringUtil.getStringFromKey(publicKey) + sd.toString();
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    /**
     * Verifies the data we signed hasn't been tampered with.
     *
     * @return whether the data was signed with the corresponding private key.
     */
    public boolean verifySignature() {
        var data = StringUtil.getStringFromKey(publicKey) + sd.toString();
        return StringUtil.verifyECDSASig(publicKey, data, signature);
    }

    /**
     * @return whether the transaction is valid.
     */
    public boolean processTransaction() {
        return true;//verifySignature();
    }

    /**
     * Calculate the approximate size of the transaction.
     *
     * @return the size of the transaction in bytes.
     */
    @Override
    public long getApproximateSize() {
        return byteSize;
    }

    /**
     * Recalculates the Transaction size if it's necessary,
     *
     * The size of the transaction is lost when storing it in database or serialization.
     */
    public void resetSize() {
        byteSize = ClassLayout.parseClass(this.getClass()).instanceSize() +
                sd.getApproximateSize();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Transaction {")
          .append(System.lineSeparator())
          .append("Transaction id: ")
          .append(transactionId)
          .append(System.lineSeparator())
          .append("Public Key: ")
          .append(publicKey.toString())
          .append(System.lineSeparator())
          .append("Data : {").append(getSensorData().toString())
          .append('}')
          .append(System.lineSeparator());
        return sb.toString();
    }
}
