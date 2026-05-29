package giuseppetavella.zero_chiamate.domain.entities.uploaded_files;

import giuseppetavella.zero_chiamate.api.AttachmentResponseBuilder;
import giuseppetavella.zero_chiamate.domain.entities.uploaded_files.UploadedFile;
import giuseppetavella.zero_chiamate.domain.entities.uploaded_files.UploadedFilesRepository;
import giuseppetavella.zero_chiamate.domain.entities.uploaded_files.dto.to_send.UploadedFileToSendDTO;
import giuseppetavella.zero_chiamate.exceptions.NotFoundException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.storage.FileStorageService;
import giuseppetavella.zero_chiamate.integrations.cloudflare_r2.CloudflareR2APIService;
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
    public UploadedFileToSendDTO toUploadedFileDTO(UploadedFile uploadedFile) {
        return new UploadedFileToSendDTO(
                uploadedFile.getId(),
                uploadedFile.getMimeType(),
                uploadedFile.getOriginalFilename(),
                uploadedFile.getCreatedAt()
        );
    }
    

    /**
     * Upload a file to Cloudflare R2 and save the record to the database.
     * Returns the saved uploaded file record.
     */
    public UploadedFile upload(MultipartFile file) {

        var bytes        = FileHelper.getBytes(file);
        var originalName = file.getOriginalFilename();
        if(originalName == null) {
            originalName = "";
        }
        var mimeType     = FileHelper.getMimeType(bytes, originalName);

        // upload to R2, get back the storage key
        var result = fileStorageService.upload(bytes, originalName);

        // save the record to the database
        var uploadedFile = new UploadedFile(
                result.filename(),
                originalName,
                mimeType
        );

        return repository.save(uploadedFile);
    }

    
    public UploadedFileToSendDTO uploadDTO(MultipartFile file) {
        return toUploadedFileDTO(
                upload(file)
        );
    }
    

    /**
     * Download a file from Cloudflare R2 by its public ID (in database).
     * This is critical for security.
     */
    public ResponseEntity<byte[]> download(UUID fileIdInDB) {
        var uploadedFile = getById(fileIdInDB);
        var bytes        = fileStorageService.download(uploadedFile.getStorageKey());
        return AttachmentResponseBuilder.anyFile(bytes, uploadedFile.getOriginalFilename());
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