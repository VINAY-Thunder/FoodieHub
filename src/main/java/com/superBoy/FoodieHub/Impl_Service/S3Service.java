package com.superBoy.FoodieHub.Impl_Service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.superBoy.FoodieHub.ExceptionHandling.FileStorageException;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

	private final S3Client s3Client;

	@Value("${aws.s3.bucket}")
	private String bucketName;

	@Autowired
	public S3Service(S3Client s3Client) {
		this.s3Client = s3Client;
	}

	public String uploadFile(String fileName, MultipartFile file) {

		try {
			String key = UUID.randomUUID() + "_" + fileName;

			PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(key)
					.contentType(file.getContentType()).build();

			s3Client.putObject(putObjectRequest,
					software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

			return "https://" + bucketName + ".s3.amazonaws.com/" + key;

		} catch (IOException e) {
			throw new RuntimeException("Error uploading file", e);
		}
	}

	public void deleteFile(String fileUrl) {

		if (fileUrl == null || fileUrl.isBlank()) {
			return ;
		}
		try {
			String key = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

			DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(key).build();

			s3Client.deleteObject(deleteObjectRequest);
		} catch (Exception e) {
			throw new FileStorageException("Failed to delete file from S3", e);
		}
	}

}
