package com.test.task.videostream.binarystorage;

import com.test.task.videostream.config.MinioConfig;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

import static com.test.task.videostream.config.AsyncConfig.ASYNC_TASK_EXECUTOR;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioStorageService {
    private final MinioClient minioClient;

    @Value("${minio.put-object-part-size}")
    private Long putObjectPartSize;

    @Async(ASYNC_TASK_EXECUTOR)
    public void save(MultipartFile file, UUID uuid) throws Exception {
        minioClient.putObject(
                PutObjectArgs
                        .builder()
                        .bucket(MinioConfig.COMMON_BUCKET_NAME)
                        .object(uuid.toString())
                        .stream(file.getInputStream(), file.getSize(), putObjectPartSize)
                        .build()
        );
    }

    public InputStream getInputStream(UUID uuid, long offset, long length) throws Exception {
        return minioClient.getObject(
                GetObjectArgs
                        .builder()
                        .bucket(MinioConfig.COMMON_BUCKET_NAME)
                        .offset(offset)
                        .length(length)
                        .object(uuid.toString())
                        .build());
    }

}
