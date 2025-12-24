package com.example.personal_blog.services;

import com.example.personal_blog.model.BlogPost;
import com.example.personal_blog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {

    @Autowired
    private BlogRepository blogRepository;
    public List<BlogPost> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Optional<BlogPost> getBlogById(String id) {
        return blogRepository.findById(id);
    }

    public BlogPost saveBlog(BlogPost blogPosts) {
        return blogRepository.save(blogPosts);
    }

    public BlogPost updateBlog(String id, BlogPost updatedBlog) {
        return blogRepository.findById(id)
                .map(existingBlog -> {
                    existingBlog.setTitle(updatedBlog.getTitle());
                    existingBlog.setContent(updatedBlog.getContent());
                    existingBlog.setAuthor(updatedBlog.getAuthor());
                    return blogRepository.save(existingBlog);
                })
                .orElseThrow(() -> new RuntimeException("Blog not found: " + id));
    }

    public void deleteById(String id) {
        if(!blogRepository.existsById(id)) {
            throw new IllegalArgumentException("Blog not found: " + id);
        }
        blogRepository.deleteById(id);
    }
}
