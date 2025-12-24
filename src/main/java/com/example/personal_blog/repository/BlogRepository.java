package com.example.personal_blog.repository;

import com.example.personal_blog.model.BlogPost;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogRepository extends MongoRepository<BlogPost, String> {

}
