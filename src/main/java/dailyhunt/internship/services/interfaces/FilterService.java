package dailyhunt.internship.services.interfaces;

import dailyhunt.internship.clientmodels.request.FilterForm;
import dailyhunt.internship.entities.News;

import java.util.List;

public interface FilterService {

     List<News> filter(FilterForm filterForm);
}
