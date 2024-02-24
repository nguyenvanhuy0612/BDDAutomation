package org.huy;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.azure.storage.file.datalake.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZoneOffset;

public class Main {
    private static final String FORWARD_SLASH = "/";
    private static final String UNDER_SCORE = "_";

    public static void main(String[] args) {
        String url = "https://saobaasdummyenv.blob.core.windows.net/skuivu/Import/Contact?sp=rcw&st=2024-01-23T14:41:54Z&se=2024-01-23T22:41:54Z&skoid=ab25b8df-55b9-4170-9529-211ff7305365&sktid=04a2636c-326d-48ff-93f8-709875bd3aa9&skt=2024-01-23T14:41:54Z&ske=2024-01-23T22:41:54Z&sks=b&skv=2022-11-02&spr=https&sv=2022-11-02&sr=d&sig=lIoHYwxMTbQhHkRZ2%2F4a5cBzW9lOhRXTEriS2AX08LQ%3D&sdd=2";
        DataLakeDirectoryClient dataLakeDirectoryClient = new DataLakePathClientBuilder()
                .endpoint(url)
                .buildDirectoryClient();

        System.out.println("dataLakeDirectoryClient = " + dataLakeDirectoryClient);
    }

    public static void main2(String[] args) {

        BlobClient blobClient = new BlobClientBuilder()
                .endpoint("https://saobaasdummyenv.blob.core.windows.net/skuivu/Export/Test/file.text?sp=racwd&st=2024-01-22T15:08:10Z&se=2024-01-22T23:08:10Z&skoid=ab25b8df-55b9-4170-9529-211ff7305365&sktid=04a2636c-326d-48ff-93f8-709875bd3aa9&skt=2024-01-22T15:08:10Z&ske=2024-01-22T23:08:10Z&sks=b&skv=2022-11-02&spr=https&sv=2022-11-02&sr=d&sig=CAH1vAgf1a1gzvUiEC%2F03C2I0dRNHj8Hpm8HVhZV1%2B0%3D&sdd=2")
                .buildClient();
        System.out.println("blobClient = " + blobClient);

        BlockBlobClient blockBlobClient = blobClient.getBlockBlobClient();

        BlobAsyncClient blobAsyncClient = new BlobClientBuilder()
                .endpoint("https://saobaasdummyenv.blob.core.windows.net/skuivu/Export/Test/file.text?sp=racwd&st=2024-01-22T15:08:10Z&se=2024-01-22T23:08:10Z&skoid=ab25b8df-55b9-4170-9529-211ff7305365&sktid=04a2636c-326d-48ff-93f8-709875bd3aa9&skt=2024-01-22T15:08:10Z&ske=2024-01-22T23:08:10Z&sks=b&skv=2022-11-02&spr=https&sv=2022-11-02&sr=d&sig=CAH1vAgf1a1gzvUiEC%2F03C2I0dRNHj8Hpm8HVhZV1%2B0%3D&sdd=2")
                .buildAsyncClient();

        blobAsyncClient.getBlobUrl();

    }

    public static void main1(String[] args) throws Exception {

        DataLakeServiceClient dataLakeServiceClient = getDataLakeServiceClientWithSasToken(
                "saobaasci",
                "sv=2023-08-03&st=2024-01-22T14%3A39%3A37Z&se=2024-01-22T14%3A54%3A37Z&skoid=d8db1651-9e92-4d81-8cf9-1a49c5005565&sktid=04a2636c-326d-48ff-93f8-709875bd3aa9&skt=2024-01-22T14%3A39%3A37Z&ske=2024-01-22T14%3A54%3A37Z&sks=b&skv=2023-11-03&sr=d&sp=rl&sdd=2&sig=RLWSAy5pe%2BSHPUBkhp6wPxNjQJKvT6ghJVn0u4ik2Dc%3D"
        );

        Instant currentInstant = Instant.now();
        int year = currentInstant.atZone(ZoneOffset.UTC).getYear();

        DataLakeFileSystemClient dataLakeFileSystemClient = dataLakeServiceClient.getFileSystemClient("skuivu");

        StringBuilder adls2FilePath = new StringBuilder();
        adls2FilePath.append(year).append(FORWARD_SLASH).append("testdir");

        // Corrected: Use getDirectoryClient instead of createDirectory
        DataLakeDirectoryClient dataLakeDirectoryClient = dataLakeFileSystemClient.getDirectoryClient(adls2FilePath.toString());

        String fileNamePrefix = String.valueOf(Instant.now().toEpochMilli());
        byte[] uploadedFileBytes = "test".getBytes();
        InputStream inputStream = new ByteArrayInputStream(uploadedFileBytes);

        // Updated: Append UNDER_SCORE before fileNamePrefix
        DataLakeFileClient dataLakeFileClient = dataLakeDirectoryClient.createFile(UNDER_SCORE + fileNamePrefix + UNDER_SCORE + "test.txt");
        dataLakeFileClient.append(inputStream, 0, uploadedFileBytes.length);
        dataLakeFileClient.flush(uploadedFileBytes.length);

        // Note: Don't forget to close the inputStream after use
        inputStream.close();
    }

    static public DataLakeServiceClient getDataLakeServiceClientWithSasToken(String accountName, String sasToken) {
        String endpoint = String.format("https://%s.dfs.core.windows.net?%s", accountName, sasToken);

        DataLakeServiceClientBuilder builder = new DataLakeServiceClientBuilder();
        return builder.endpoint(endpoint).buildClient();
    }
}