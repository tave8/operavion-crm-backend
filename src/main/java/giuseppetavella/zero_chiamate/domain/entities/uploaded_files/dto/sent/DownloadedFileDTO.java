package giuseppetavella.zero_chiamate.domain.entities.uploaded_files.dto.sent;

public record DownloadedFileDTO(
        byte[] bytes,
        
        String originalFilename
) {
}
