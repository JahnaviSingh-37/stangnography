package com.steganography.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.crypto.Credentials;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

@Service
public class BlockchainService {
    
    private static final Logger log = LoggerFactory.getLogger(BlockchainService.class);
    
    private final Web3j web3j;
    private final Credentials credentials;
    
    @Value("${blockchain.contract.address}")
    private String contractAddress;
    
    public BlockchainService(@Value("${blockchain.rpc.url}") String rpcUrl,
                           @Value("${blockchain.private.key}") String privateKey) {
        this.web3j = Web3j.build(new HttpService(rpcUrl));
        
        // Handle private key with or without 0x prefix
        String cleanPrivateKey = privateKey;
        if (privateKey.startsWith("0x")) {
            cleanPrivateKey = privateKey.substring(2);
        }
        
        this.credentials = Credentials.create(cleanPrivateKey);
        log.info("Blockchain service initialized with address: {}", credentials.getAddress());
    }
    
    /**
     * Get the blockchain address for this service
     */
    public String getAddress() {
        return credentials.getAddress();
    }
    
    /**
     * Stores hash on the blockchain by sending a transaction with hash in data field
     */
    public String storeHashOnBlockchain(String hash) {
        try {
            log.info("Storing hash on blockchain: {}", hash);
            
            // Create transaction with hash in data field
            BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
            BigInteger gasLimit = BigInteger.valueOf(21000);
            BigInteger nonce = web3j.ethGetTransactionCount(
                credentials.getAddress(), 
                org.web3j.protocol.core.DefaultBlockParameterName.LATEST
            ).send().getTransactionCount();
            
            // Convert hash to hex bytes for transaction data
            String data = "0x" + hash;
            
            org.web3j.crypto.RawTransaction rawTransaction = 
                org.web3j.crypto.RawTransaction.createTransaction(
                    nonce, gasPrice, gasLimit, contractAddress, BigInteger.ZERO, data);
            
            byte[] signedMessage = org.web3j.crypto.TransactionEncoder.signMessage(
                rawTransaction, credentials);
            
            String hexValue = org.web3j.utils.Numeric.toHexString(signedMessage);
            
            org.web3j.protocol.core.methods.response.EthSendTransaction response = 
                web3j.ethSendRawTransaction(hexValue).send();
            
            if (response.hasError()) {
                throw new RuntimeException("Transaction failed: " + response.getError().getMessage());
            }
            
            String transactionHash = response.getTransactionHash();
            log.info("Transaction sent successfully. Hash: {}", transactionHash);
            
            return transactionHash;
            
        } catch (Exception e) {
            log.error("Failed to store hash on blockchain", e);
            throw new RuntimeException("Failed to store hash on blockchain", e);
        }
    }
    
    /**
     * Retrieves hash from blockchain transaction
     */
    public String getHashFromBlockchain(String transactionHash) {
        try {
            log.info("Retrieving hash from blockchain transaction: {}", transactionHash);
            
            EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(transactionHash).send();
            
            if (receipt.getTransactionReceipt().isEmpty()) {
                throw new RuntimeException("Transaction not found or still pending");
            }
            
            TransactionReceipt transactionReceipt = receipt.getTransactionReceipt().get();
            
            // Get the original transaction to access input data
            org.web3j.protocol.core.methods.response.EthTransaction ethTransaction = 
                web3j.ethGetTransactionByHash(transactionHash).send();
            
            if (ethTransaction.getTransaction().isEmpty()) {
                throw new RuntimeException("Transaction details not found");
            }
            
            String inputData = ethTransaction.getTransaction().get().getInput();
            
            // Remove '0x' prefix and return the hash
            if (inputData != null && inputData.startsWith("0x") && inputData.length() > 2) {
                return inputData.substring(2);
            }
            
            throw new RuntimeException("No hash data found in transaction");
            
        } catch (Exception e) {
            log.error("Failed to retrieve hash from blockchain", e);
            throw new RuntimeException("Failed to retrieve hash from blockchain", e);
        }
    }
    
    /**
     * Verifies if the transaction exists and is confirmed
     */
    public boolean verifyTransactionExists(String transactionHash) {
        try {
            EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(transactionHash).send();
            return receipt.getTransactionReceipt().isPresent();
        } catch (Exception e) {
            log.error("Failed to verify transaction existence", e);
            return false;
        }
    }
}
