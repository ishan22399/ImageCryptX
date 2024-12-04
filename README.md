# Advanced AES Image Encryption
![56394](https://github.com/user-attachments/assets/958df903-0591-4dbb-99be-4b9ae12093ad)

## Overview
This project provides an advanced image encryption and decryption solution using AES (Advanced Encryption Standard) in GCM (Galois/Counter Mode). The tool allows users to securely encrypt and decrypt image files using AES-256 encryption with GCM, ensuring both confidentiality and integrity of the encrypted data.

## Features
- **Image Encryption**: Encrypt images securely using AES in GCM mode.
- **Image Decryption**: Decrypt images back to their original form using the corresponding key and initialization vector (IV).
- **IV Handling**: The IV is stored with the encrypted image for decryption.
- **GCM (Galois/Counter Mode)**: Ensures both encryption and authentication of the image file.

## Requirements
- Java 8 or higher
- A secure AES-256 key is generated automatically.

## Usage
1. **Encrypt Image**: 
   - Run the program.
   - Choose "1. Encrypt Image" from the menu.
   - Provide the path of the image to encrypt and specify the output path for the encrypted file.
   
2. **Decrypt Image**:
   - Run the program.
   - Choose "2. Decrypt Image" from the menu.
   - Provide the path of the encrypted image and specify the output path for the decrypted image.

## How to Run
1. Compile the `AdvancedAESImageEncryption.java` file.
2. Run the program.
   - It will present a menu to either encrypt or decrypt an image.

```bash
javac AdvancedAESImageEncryption.java
java AdvancedAESImageEncryption
```

## Code Walkthrough
- Key Generation: An AES-256 key is generated using Java's KeyGenerator.
- Encryption Process: The input image file is read into a byte array, encrypted using the AES algorithm in GCM mode, and then the IV and encrypted bytes are saved to a file.- 
- Decryption Process: The IV is extracted from the encrypted file, and the encrypted data is decrypted back to its original form using the same AES key and IV.
---
