package com.steganography.controller;

import com.steganography.model.SteganographyRecord;
import com.steganography.service.SteganographyService;
import com.steganography.util.SteganographyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/steganography")
public class SteganographyController {
    
    private static final Logger log = LoggerFactory.getLogger(SteganographyController.class);
    
    @Autowired
    private SteganographyService steganographyService;
    
    @Autowired
    private SteganographyUtil steganographyUtil;
    
    /**
     * Hide text in PNG image and store hash on blockchain
     */
    @PostMapping("/hide")
    public ResponseEntity<Map<String, Object>> hideText(
            @RequestParam("image") @NotNull MultipartFile imageFile,
            @RequestParam("text") @NotBlank String text) {
        
        try {
            log.info("Received request to hide text in image: {}", imageFile.getOriginalFilename());
            
            SteganographyRecord record = steganographyService.hideTextInImage(imageFile, text);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Text hidden successfully and hash stored on blockchain");
            response.put("record", record);
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process image");
        } catch (Exception e) {
            log.error("Unexpected error while hiding text", e);
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }
    
    /**
     * Extract text from PNG image and verify against blockchain
     */
    @PostMapping("/extract")
    public ResponseEntity<Map<String, Object>> extractText(
            @RequestParam("image") @NotNull MultipartFile imageFile,
            @RequestParam("transactionHash") @NotBlank String transactionHash) {
        
        try {
            log.info("Received request to extract text from image: {}", imageFile.getOriginalFilename());
            
            String extractedText = steganographyService.extractAndVerifyText(imageFile, transactionHash);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Text extracted and verified successfully");
            response.put("extractedText", extractedText);
            response.put("verified", true);
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("integrity verification failed")) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", e.getMessage());
                response.put("verified", false);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (IOException e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process image");
        } catch (Exception e) {
            log.error("Unexpected error while extracting text", e);
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }
    
    /**
     * Verify text integrity against blockchain without extraction
     */
    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyText(
            @RequestParam("text") @NotBlank String text,
            @RequestParam("transactionHash") @NotBlank String transactionHash) {
        
        try {
            boolean verified = steganographyService.verifyTextIntegrity(text, transactionHash);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("verified", verified);
            response.put("message", verified ? "Text integrity verified" : "Text integrity verification failed");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error while verifying text integrity", e);
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to verify text integrity");
        }
    }
    
    /**
     * Get all steganography records
     */
    @GetMapping("/records")
    public ResponseEntity<Map<String, Object>> getAllRecords() {
        try {
            List<SteganographyRecord> records = steganographyService.getAllRecords();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("records", records);
            response.put("count", records.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error while fetching records", e);
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch records");
        }
    }
    
    /**
     * Get record by ID
     */
    @GetMapping("/records/{id}")
    public ResponseEntity<Map<String, Object>> getRecordById(@PathVariable String id) {
        try {
            SteganographyRecord record = steganographyService.getRecordById(id);
            
            if (record == null) {
                return createErrorResponse(HttpStatus.NOT_FOUND, "Record not found");
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("record", record);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error while fetching record by ID", e);
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch record");
        }
    }
    
    /**
     * Get record by transaction hash
     */
    @GetMapping("/records/transaction/{transactionHash}")
    public ResponseEntity<Map<String, Object>> getRecordByTransactionHash(@PathVariable String transactionHash) {
        try {
            SteganographyRecord record = steganographyService.getRecordByTransactionHash(transactionHash);
            
            if (record == null) {
                return createErrorResponse(HttpStatus.NOT_FOUND, "Record not found for transaction hash");
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("record", record);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error while fetching record by transaction hash", e);
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch record");
        }
    }
    
    /**
     * Generate SHA-256 hash for text (utility endpoint)
     */
    @PostMapping("/hash")
    public ResponseEntity<Map<String, Object>> generateHash(@RequestParam("text") @NotBlank String text) {
        try {
            String hash = steganographyUtil.generateSHA256(text);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("text", text);
            response.put("hash", hash);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error while generating hash", e);
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate hash");
        }
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Steganography service is running");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
    
    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.status(status).body(response);
    }
}
