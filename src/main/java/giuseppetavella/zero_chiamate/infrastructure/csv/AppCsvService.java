package giuseppetavella.zero_chiamate.infrastructure.csv;

import org.springframework.stereotype.Service;

@Service
public class AppCsvService extends CsvService {
    
    // @Autowired
    // private ArticlesService articlesService;
    //
    // // this should return a Csv instance
    // public Csv generateArticlesReport() {
    //    
    //     List<Article> articles = this.articlesService.findAll();
    //    
    //     String[] fields = {"Author", "Title", "Content"};
    //
    //     CsvGeneratorService csv = new CsvGeneratorService(fields);
    //    
    //     for (Article article : articles) {
    //         csv.addRow(
    //             article.getUser().getFirstname(),
    //             article.getTitle(),
    //             article.getContent()
    //         );
    //     }
    //    
    //     return new Csv(csv);
    //    
    // }
    
    
}
