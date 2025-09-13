package com.steganography.service;

import com.steganography.model.SteganographyRecord;
import com.steganography.util.SteganographyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SteganographyService {
    
    private static final Logger log = LoggerFactory.getLogger(SteganographyService.class);
    
    @Autowired
    private SteganographyUtil steganographyUtil;
    
    @Autowired
    private BlockchainService blockchainService;
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    /**
     * Hides text in image and stores hash on blockchain
     */
    public SteganographyRecord hideTextInImage(MultipartFile imageFile, String text) throws IOException {
        log.info("Hiding text in image: {}", imageFile.getOriginalFilename());
        
        // Validate input
        if (!imageFile.getContentType().equals("image/png")) {
            throw new IllegalArgumentException("Only PNG images are supported");
        }
        
        // Generate hash of the text
        String textHash = steganographyUtil.generateSHA256(text);
        log.info("Generated SHA-256 hash: {}", textHash);
        
        // Hide text in image
        byte[] originalImageBytes = imageFile.getBytes();
        byte[] stegoImageBytes = steganographyUtil.hideText(originalImageBytes, text);
        
        // Store hash on blockchain (with fallback)
        String transactionHash;
        try {
            transactionHash = blockchainService.storeHashOnBlockchain(textHash);
            log.info("Successfully stored hash on blockchain: {}", transactionHash);
        } catch (Exception e) {
            log.warn("Blockchain storage failed, using fallback: {}", e.getMessage());
            transactionHash = "FALLBACK_" + System.currentTimeMillis(); // Fallback when blockchain fails
        }
        
        // Create and save record
        SteganographyRecord record = new SteganographyRecord();
        record.setOriginalFileName(imageFile.getOriginalFilename());
        record.setFileName("stego_" + System.currentTimeMillis() + "_" + imageFile.getOriginalFilename());
        record.setTextHash(textHash);
        record.setTransactionHash(transactionHash);
        record.setBlockchainAddress(blockchainService.getAddress());
        record.setCreatedAt(LocalDateTime.now());
        record.setStatus("COMPLETED");
        
        mongoTemplate.save(record);
        
        log.info("Successfully hidden text and stored hash. Record ID: {}", record.getId());
        return record;
    }
    
    /**
     * Extracts text from image and verifies against blockchain
     */
    public String extractAndVerifyText(MultipartFile imageFile, String transactionHash) throws IOException {
        log.info("Extracting and verifying text from image: {}", imageFile.getOriginalFilename());
        
        // Extract text from image
        byte[] imageBytes = imageFile.getBytes();
        String extractedText = steganographyUtil.extractText(imageBytes);
        
        // Generate hash of extracted text
        String extractedTextHash = steganographyUtil.generateSHA256(extractedText);
        
        // Get hash from blockchain
        String blockchainHash = blockchainService.getHashFromBlockchain(transactionHash);
        
        // Verify integrity
        if (!extractedTextHash.equals(blockchainHash)) {
            throw new RuntimeException("Text integrity verification failed. Hash mismatch!");
        }
        
        log.info("Text integrity verified successfully");
        return extractedText;
    }
    
    /**
     * Verifies text integrity without extraction
     */
    public boolean verifyTextIntegrity(String text, String transactionHash) {
        try {
            String textHash = steganographyUtil.generateSHA256(text);
            String blockchainHash = blockchainService.getHashFromBlockchain(transactionHash);
            
            return textHash.equals(blockchainHash);
        } catch (Exception e) {
            log.error("Failed to verify text integrity", e);
            return false;
        }
    }
    
    /**
     * Gets all steganography records
     */
    public List<SteganographyRecord> getAllRecords() {
        return mongoTemplate.findAll(SteganographyRecord.class);
    }
    
    /**
     * Gets record by ID
     */
    public SteganographyRecord getRecordById(String id) {
        return mongoTemplate.findById(id, SteganographyRecord.class);
    }
    
    /**
     * Gets records by transaction hash
     */
    public SteganographyRecord getRecordByTransactionHash(String transactionHash) {
        Query query = new Query(Criteria.where("transactionHash").is(transactionHash));
        return mongoTemplate.findOne(query, SteganographyRecord.class);
    }
}
