package com.steganography.util;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class SteganographyUtil {
    
    private static final String DELIMITER = "###END###";
    
    /**
     * Hides text in a PNG image using LSB steganography
     */
    public byte[] hideText(byte[] imageBytes, String text) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
        String textWithDelimiter = text + DELIMITER;
        byte[] textBytes = textWithDelimiter.getBytes();
        
        int width = image.getWidth();
        int height = image.getHeight();
        
        // Check if image can hold the text
        int maxCapacity = (width * height * 3) / 8; // 3 channels, 1 bit per channel
        if (textBytes.length > maxCapacity) {
            throw new IllegalArgumentException("Text too long for image capacity");
        }
        
        int textIndex = 0;
        int bitIndex = 0;
        
        outerLoop:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (textIndex >= textBytes.length) {
                    break outerLoop;
                }
                
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;
                
                // Modify LSB of red channel
                if (textIndex < textBytes.length) {
                    int bit = (textBytes[textIndex] >> (7 - bitIndex)) & 1;
                    red = (red & 0xFE) | bit;
                    bitIndex++;
                    
                    if (bitIndex == 8) {
                        bitIndex = 0;
                        textIndex++;
                    }
                }
                
                // Modify LSB of green channel
                if (textIndex < textBytes.length && bitIndex < 8) {
                    int bit = (textBytes[textIndex] >> (7 - bitIndex)) & 1;
                    green = (green & 0xFE) | bit;
                    bitIndex++;
                    
                    if (bitIndex == 8) {
                        bitIndex = 0;
                        textIndex++;
                    }
                }
                
                // Modify LSB of blue channel
                if (textIndex < textBytes.length && bitIndex < 8) {
                    int bit = (textBytes[textIndex] >> (7 - bitIndex)) & 1;
                    blue = (blue & 0xFE) | bit;
                    bitIndex++;
                    
                    if (bitIndex == 8) {
                        bitIndex = 0;
                        textIndex++;
                    }
                }
                
                int newPixel = (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, newPixel);
            }
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        return baos.toByteArray();
    }
    
    /**
     * Extracts hidden text from a PNG image
     */
    public String extractText(byte[] imageBytes) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
        
        int width = image.getWidth();
        int height = image.getHeight();
        
        StringBuilder textBuilder = new StringBuilder();
        int bitIndex = 0;
        int currentByte = 0;
        
        outerLoop:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;
                
                // Extract bit from red channel
                int bit = red & 1;
                currentByte = (currentByte << 1) | bit;
                bitIndex++;
                
                if (bitIndex == 8) {
                    textBuilder.append((char) currentByte);
                    if (textBuilder.toString().endsWith(DELIMITER)) {
                        break outerLoop;
                    }
                    currentByte = 0;
                    bitIndex = 0;
                }
                
                // Extract bit from green channel
                if (bitIndex < 8) {
                    bit = green & 1;
                    currentByte = (currentByte << 1) | bit;
                    bitIndex++;
                    
                    if (bitIndex == 8) {
                        textBuilder.append((char) currentByte);
                        if (textBuilder.toString().endsWith(DELIMITER)) {
                            break outerLoop;
                        }
                        currentByte = 0;
                        bitIndex = 0;
                    }
                }
                
                // Extract bit from blue channel
                if (bitIndex < 8) {
                    bit = blue & 1;
                    currentByte = (currentByte << 1) | bit;
                    bitIndex++;
                    
                    if (bitIndex == 8) {
                        textBuilder.append((char) currentByte);
                        if (textBuilder.toString().endsWith(DELIMITER)) {
                            break outerLoop;
                        }
                        currentByte = 0;
                        bitIndex = 0;
                    }
                }
            }
        }
        
        String extractedText = textBuilder.toString();
        if (extractedText.endsWith(DELIMITER)) {
            return extractedText.substring(0, extractedText.length() - DELIMITER.length());
        }
        
        throw new IllegalArgumentException("No hidden text found or image corrupted");
    }
    
    /**
     * Generates SHA-256 hash of the text
     */
    public String generateSHA256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
