package kr.minimalest.core.domain.file.storage;

import kr.minimalest.core.domain.file.StorageType;

public interface StorageResourceUploader {

   String uploadAndGetKey(UploadFile uploadFile);

   StorageType getStorageType();
}
