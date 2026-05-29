package giuseppetavella.zero_chiamate.domain.entities.uploaded_files;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UploadedFilesRepository extends JpaRepository<UploadedFile, UUID> {
}
