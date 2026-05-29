package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.domain.entities.uploaded_files.UploadedFile;
import giuseppetavella.zero_chiamate.domain.entities.uploaded_files.UploadedFilesService;
import giuseppetavella.zero_chiamate.domain.entities.uploaded_files.dto.to_send.UploadedFileToSendDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/uploaded-files")
public class UploadedFilesController {

    @Autowired
    private UploadedFilesService uploadedFilesService;


    /**
     * Upload a file.
     * Returns the uploaded file record.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UploadedFileToSendDTO upload(@RequestParam("file") MultipartFile file) {
        return uploadedFilesService.uploadDTO(file);
    }


    /**
     * Download a file by its public ID.
     */
    @GetMapping("/{fileIdInDB}")
    public ResponseEntity<byte[]> download(@PathVariable UUID fileIdInDB) {
        return uploadedFilesService.download(fileIdInDB);
    }


    /**
     * Delete a file by its public ID.
     */
    // @DeleteMapping("/{id}")
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    // public void delete(@PathVariable UUID id) {
    //     uploadedFilesService.delete(id);
    // }


}   