package org.javaproteam27.socialnetwork.service;

import org.javaproteam27.socialnetwork.model.dto.request.PostRq;
import org.javaproteam27.socialnetwork.model.dto.response.ListResponseRs;
import org.javaproteam27.socialnetwork.model.dto.response.PostRs;
import org.javaproteam27.socialnetwork.model.dto.response.ResponseRs;
import org.javaproteam27.socialnetwork.repository.PostRepository;
import org.javaproteam27.socialnetwork.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
class PostServiceTest {
    @MockBean
    private PostRepository postRepository;
    @MockBean
    private TagRepository tagRepository;
    @MockBean
    private NotificationsService notificationsService;

    @Autowired
    PostService postService;

    private void assertResponseRs(ResponseRs<PostRs> response){
        assertNotNull(response);
        assertEquals("", response.getError());
        assertTrue(response.getTimestamp() instanceof Long);
    }

    private void assertListResponseRs(ListResponseRs<PostRs> response, Integer offset, Integer itemPerPage){
        assertNotNull(response);
        assertEquals(offset, response.getOffset());
        assertEquals(itemPerPage, response.getPerPage());
        assertEquals("", response.getError());
        assertTrue(response.getTimestamp() instanceof Long);
    }

    @Test
    void publishPost() {
        PostRq testPost = PostRq.builder().postText("Test post").tags(Arrays.asList("test_post")).title("Test post").build();
        ResponseRs<PostRs> response = postService.publishPost(System.currentTimeMillis(), testPost, 1);

        assertResponseRs(response);
    }


    @Test
    void findAllPosts() {
        Integer offset = 0;
        Integer itemPerPage = 20;
        ListResponseRs<PostRs> response = postService.findAllPosts(offset, itemPerPage);

        assertListResponseRs(response, offset, itemPerPage);
    }

    @Test
    void findAllUserPosts() {
        Integer offset = 0;
        Integer itemPerPage = 20;
        Integer authorId = 1;
        ListResponseRs<PostRs> response = postService.findAllUserPosts(authorId, offset, itemPerPage);

        assertListResponseRs(response, offset, itemPerPage);
    }

    @Test
    void deletePost() {
        Integer postId = 1;
        ResponseRs<PostRs> response = postService.deletePost(postId);

        assertResponseRs(response);
    }

    @Test
    void updatePost() {
        Integer postId = 0;
        String title = "post title";
        String postText = "post text";
        List<String> tags = new ArrayList<>();
        ResponseRs<PostRs> response = postService.updatePost(postId, title, postText, tags);

        assertResponseRs(response);
    }

    @Test
    void findPost() {
        String text = "post text";
        Long dateFrom = System.currentTimeMillis() - 100_000;
        Long dateTo = System.currentTimeMillis();
        String authorName = "author name";
        List<String> tags = new ArrayList<>();
        Integer offset = 0;
        Integer itemPerPage = 20;
        ListResponseRs<PostRs> response = postService.findPost(text, dateFrom, dateTo, authorName, tags,
                offset, itemPerPage);

        assertListResponseRs(response, offset, itemPerPage);
    }

    @Test
    void getPost() {
        Integer postId = 0;
        ResponseRs<PostRs> response = postService.getPost(postId);

        assertResponseRs(response);
    }
}