package giuseppetavella.zero_chiamate.domain.entities.uploaded_files;

import giuseppetavella.zero_chiamate.api.AttachmentResponseBuilder;
import giuseppetavella.zero_chiamate.domain.entities.uploaded_files.dto.sent.DownloadedFileDTO;
import giuseppetavella.zero_chiamate.domain.entities.uploaded_files.dto.to_send.UploadedFileDTO;
import giuseppetavella.zero_chiamate.exceptions.NotFoundException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.storage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
public class UploadedFilesService {

    @Autowired
    private UploadedFilesRepository repository;

    @Autowired
    private FileStorageService fileStorageService;


    /**
     * Uploaded file -> Uploaded file DTO
     * 
     * @param uploadedFile
     * @return
     */
    public UploadedFileDTO toUploadedFileDTO(UploadedFile uploadedFile) {
        return new UploadedFileDTO(
                uploadedFile.getId(),
                uploadedFile.getMimeType(),
                uploadedFile.getOriginalFilename(),
                uploadedFile.getCreatedAt()
        );
    }


    public UploadedFileDTO uploadDTO(byte[] bytes, String originalFilename) {
        return toUploadedFileDTO(
                upload(bytes, originalFilename)
        );
    }
    
    
    /**
     * Use the DTO to avoid exposing public url of file.
     * @param file
     * @return
     */
    public UploadedFileDTO uploadDTO(MultipartFile file) {
       return uploadDTO(
               FileHelper.getBytes(file),
               file.getOriginalFilename()
       );
    }
    

    /**
     * 
     * 
     * @param bytes
     * @param originalFilename
     * @return
     */
    public UploadedFile upload(byte[] bytes, String originalFilename) {
        
        if(originalFilename == null) {
            originalFilename = "";
        }
        
        var mimeType = FileHelper.getMimeType(bytes, originalFilename);

        // upload to R2, get back the storage key
        var result = fileStorageService.upload(bytes, originalFilename);

        // save the record to the database
        var uploadedFile = new UploadedFile(
                // this is the storage key
                result.filename(),
                originalFilename,
                mimeType
        );

        return repository.save(uploadedFile);
        
    }
    

    /**
     * Note: this method is private because it exposes storage key.
     * Should use DTO wrapper, so that storage key is never exposed.
     * 
     * Upload a file to Cloudflare R2 and save the record to the database.
     * Returns the saved uploaded file record.
     */
    private UploadedFile upload(MultipartFile file) {
        return upload(FileHelper.getBytes(file), file.getOriginalFilename());
    }

    

    /**
     * Download raw bytes from storage by file ID.
     */
    public DownloadedFileDTO downloadDTO(UUID fileId) {
        // get the file from DB
        var uploadedFile = getById(fileId);
        // download file from cloud, using storage key found in DB
        var bytes        = fileStorageService.download(uploadedFile.getStorageKey());
        // wrap 
        return new DownloadedFileDTO(bytes, uploadedFile.getOriginalFilename());
    }
    

    /**
     * Download and wrap as an HTTP response ready to stream to the client.
     */
    public ResponseEntity<byte[]> downloadAsResponse(UUID fileId) {
        var file = downloadDTO(fileId);
        return AttachmentResponseBuilder.anyFile(file.bytes(), file.originalFilename());
    }
    

    /**
     * Delete a file from Cloudflare R2 and remove the record from the database.
     */
    // public void delete(UUID id) {
    //     var uploadedFile = getById(id);
    //
    //     // delete from R2 first, then from the database
    //     fileStorageService.delete(uploadedFile.getStorageKey());
    //     repository.deleteById(id);
    // }


    /**
     * Get an uploaded file by its public ID.
     * Throws if not found.
     */
    public UploadedFile getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, "uploaded file"));
    }


    /**
     * Find an uploaded file by its public ID.
     * Returns empty if not found.
     */
    public Optional<UploadedFile> findById(UUID id) {
        return repository.findById(id);
    }


}