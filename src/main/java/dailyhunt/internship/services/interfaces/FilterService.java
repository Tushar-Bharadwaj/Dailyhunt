package dailyhunt.internship.services.interfaces;

import dailyhunt.internship.clientmodels.request.DateFilter;
import dailyhunt.internship.clientmodels.request.FilterForm;
import dailyhunt.internship.entities.News;

import java.util.List;

public interface FilterService {

     List<News> filter(FilterForm filterForm);

     List<News> filterByKeyword(String keyword);

     List<News> filterByDateRange(DateFilter dateFilter);

     List<News> filterByTag(List<String> tags);

     List<News> filterByGenre(List<Long> genreIds);

     List<News> filterByLocality(List<Long> localityIds);
}
