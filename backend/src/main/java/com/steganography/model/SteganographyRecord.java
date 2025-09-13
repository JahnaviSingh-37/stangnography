package com.steganography.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "steganography_records")
public class SteganographyRecord {
    @Id
    private String id;
    private String fileName;
    private String textHash;
    private String transactionHash;
    private String blockchainAddress;
    private LocalDateTime createdAt;
    private String status;
    private String originalFileName;

    // Constructors
    public SteganographyRecord() {}

    public SteganographyRecord(String fileName, String textHash, String transactionHash, 
                              String blockchainAddress, LocalDateTime createdAt, String status, 
                              String originalFileName) {
        this.fileName = fileName;
        this.textHash = textHash;
        this.transactionHash = transactionHash;
        this.blockchainAddress = blockchainAddress;
        this.createdAt = createdAt;
        this.status = status;
        this.originalFileName = originalFileName;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTextHash() {
        return textHash;
    }

    public void setTextHash(String textHash) {
        this.textHash = textHash;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getBlockchainAddress() {
        return blockchainAddress;
    }

    public void setBlockchainAddress(String blockchainAddress) {
        this.blockchainAddress = blockchainAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
}
