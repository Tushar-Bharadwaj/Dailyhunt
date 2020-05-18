package dailyhunt.internship.services.interfaces;

import dailyhunt.internship.clientmodels.request.SourceDTO;
import dailyhunt.internship.clientmodels.response.AllSources;
import dailyhunt.internship.entities.Source;
import dailyhunt.internship.exceptions.ResourceNotFoundException;

import java.io.IOException;

public interface SourceService {

    AllSources getAllSources();

    Source findBySourceId(Long id) throws ResourceNotFoundException;

    Source save(SourceDTO sourceDTO) throws IOException;

    void saveSourceAtUserProfile(Source source);

    void delete(Long id) throws ResourceNotFoundException;

}
