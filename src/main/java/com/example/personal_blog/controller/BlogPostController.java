package com.example.personal_blog.controller;

import com.example.personal_blog.model.BlogPost;
import com.example.personal_blog.services.BlogPostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    @GetMapping
    public ResponseEntity<List<BlogPost>> getAllBlogs() {
        List<BlogPost> blogs = blogPostService.getAllBlogs();
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogById(@PathVariable String id) {
        try {
            Optional<BlogPost> blog = blogPostService.getBlogById(id);
            if(blog.isPresent()) {
                return new ResponseEntity<>(blog.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>("Blog not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("an error occured while fetching the blog", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createNewBlog(@Valid @RequestBody BlogPost blogPost) {
        try {
            String username = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();
            blogPost.setAuthor(username);

            BlogPost newBlog = blogPostService.saveBlog(blogPost);
            return new ResponseEntity<>(newBlog, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("error creating blog", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlogById(@PathVariable String id, @RequestBody BlogPost blogPost) {
        Optional<BlogPost> existingOpt = blogPostService.getBlogById(id);

        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found");
        }

        BlogPost existing = existingOpt.get();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!existing.getAuthor().equals(currentUser) && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not allowed to update this post");
        }

        existing.setTitle(blogPost.getTitle());
        existing.setContent(blogPost.getContent());

        BlogPost updated = blogPostService.saveBlog(existing);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable String id) {

        BlogPost post = blogPostService.getBlogById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if (post.getLikedBy().contains(username)) {
            post.getLikedBy().remove(username); // UNLIKE
        } else {
            post.getLikedBy().add(username);    // LIKE
        }

        blogPostService.saveBlog(post);
        return ResponseEntity.ok(post.getLikedBy().size());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        Optional<BlogPost> existingOpt = blogPostService.getBlogById(id);

        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found");
        }

        BlogPost existing = existingOpt.get();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!existing.getAuthor().equals(currentUser) && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not allowed to delete this post");
        }

        blogPostService.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }

}
