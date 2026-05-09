package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.Article;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.exceptions.InvalidUUIDStringException;
import giuseppetavella.demo_login_system.exceptions.NotFoundException;
import giuseppetavella.demo_login_system.exceptions.UnauthorizedException;
import giuseppetavella.demo_login_system.helpers.StringHelper;
import giuseppetavella.demo_login_system.payloads.in_request.NewArticleSentDTO;
import giuseppetavella.demo_login_system.payloads.in_request.UpdatedArticleSentDTO;
import giuseppetavella.demo_login_system.repositories.ArticlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ArticlesService {

    @Autowired
    private ArticlesRepository articlesRepository;
    
    @Autowired
    private UsersService usersService;

    /**
     * Is the user of the article the same as
     * the owner of the article?
     */
    public boolean isMyArticle(User owner, User maybeOwner) {
        return owner.getId().equals(maybeOwner.getId());
    } 

    /**
     * Find an article by ID.
     */
    public Article findById(UUID articleId) throws NotFoundException {
        return articlesRepository.findById(articleId).orElseThrow(() -> new NotFoundException(articleId, "article"));
    }

    public Article findById(String articleIdAsStr) throws NotFoundException, InvalidUUIDStringException {
        return this.findById(StringHelper.parseUUID(articleIdAsStr));
    }


    /**
     * Get all articles
     */
    public List<Article> findAll() {
        return this.articlesRepository.findAll();
    }

    /**
     * Find my article by ID.
     * The owner of the article must match the given user.
     */
    public Article findOwnArticleById(UUID articleId, User articleOwner) throws NotFoundException, 
                                                                                UnauthorizedException 
    {
        Article article = this.findById(articleId);
        if(!this.isMyArticle(articleOwner, article.getUser())) {
            throw new UnauthorizedException("This is not your article.");
        }
        return article;
    }
    
    public Article findOwnArticleById(String articleIdAsStr, User articleOwner) throws NotFoundException, 
                                                                                       UnauthorizedException, 
                                                                                        InvalidUUIDStringException 
    {
        return this.findOwnArticleById(StringHelper.parseUUID(articleIdAsStr), articleOwner);
    }


    /**
     * Get articles of the given user.
     */
    public Page<Article> findByUser(User user, int page, int size, String sortBy) {
        // / the size of each page (how many elements in each page)
        int finalSize = Math.min(10, size);
        // the page number
        int finalPage = Math.max(0, page);
        // page is the function that will get translated to SQL,
        // that will in turn filter the result set
        Pageable pageable = PageRequest.of(finalPage, finalSize, Sort.by(sortBy));
        return this.articlesRepository.findByUser(user, pageable);
    }

    
    /**
     * Add an article.
     * Checks if the user exists.
     */
    public Article addArticle(Article article) throws NotFoundException {
        boolean userExists = this.usersService.existsById(article.getUser().getId());
        if(!userExists) {
            throw new UnauthorizedException("The user associated with this article does not exist in DB.");
        }
        return this.articlesRepository.save(article);
    }
    
    public Article addOwnArticle(NewArticleSentDTO articleBody, User articleOwner) throws NotFoundException {
        Article article = new Article(
                articleOwner,
                articleBody.title(),
                articleBody.content()
        );
        return this.addArticle(article);
    }


    /**
     * Update my article, given the ID.
     */

    public Article updateOwnArticleById(UUID articleId, 
                                        UpdatedArticleSentDTO articleBody, 
                                        User articleOwner) throws NotFoundException, UnauthorizedException 
    {
        Article article = this.findOwnArticleById(articleId, articleOwner);
        article.setTitle(articleBody.title());
        article.setContent(articleBody.content());
        return this.articlesRepository.save(article);
    }

    public Article updateOwnArticleById(String articleIdAsStr, 
                                        UpdatedArticleSentDTO articleBody,
                                        User articleOwner) throws NotFoundException, 
                                                                    UnauthorizedException, 
                                                                    InvalidUUIDStringException
    {
        return this.updateOwnArticleById(StringHelper.parseUUID(articleIdAsStr), articleBody, articleOwner);
    }


    /**
     * Delete my article, given the ID.
     */

    public void deleteOwnArticleById(UUID articleId,
                                     User articleOwner) throws NotFoundException, UnauthorizedException
    {
        Article article = this.findOwnArticleById(articleId, articleOwner);
        this.articlesRepository.delete(article);
    }

    public void deleteOwnArticleById(String articleIdAsStr,
                                     User articleOwner) throws NotFoundException, 
                                                                UnauthorizedException,
                                                                InvalidUUIDStringException
    {
        this.deleteOwnArticleById(StringHelper.parseUUID(articleIdAsStr), articleOwner);
    }
    
}
