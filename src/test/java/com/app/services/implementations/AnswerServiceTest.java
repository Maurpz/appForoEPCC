package com.app.services.implementations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.app.domain.post.Answer;
import com.app.domain.post.Entry;
import com.app.domain.post.Post;
import com.app.domain.user.ForoUser;
import com.app.repositories.AnswerRepositoryImp;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {

  @Mock
  private UserService userService;
  @Mock
  private PostService postService;
  @Mock
  private EntryService entryService;
  @Mock
  private AnswerRepositoryImp answerRepository;

  @InjectMocks
  private AnswerService answerService;

  @Mock
  private Authentication authentication;

  @Mock
  private SecurityContext securityContext;

  @BeforeEach
  public void setUp () {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void testCreateAnswer () {
    //given que es lo que necesito tener para testear el metodo
    String username = "testerUser";
    Long postIdToReply = 1L;
    String content = "Content de respuesta";

    ForoUser user  = new ForoUser();
    user.setUsername(username);

    Entry entry = new Entry();
    entry.setContent(content);

    Post post = new Post();
    post.setId(postIdToReply);

    Answer answer = new Answer();
    answer.setEntry(entry);
    answer.setPost(post);

    //when

    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(username);
    when(userService.getUserByUsername(username)).thenReturn(user);
    when(entryService.createEntry(user, content)).thenReturn(entry);
    when(postService.getPostById(postIdToReply)).thenReturn(post);
    when(answerRepository.save(any(Answer.class))).thenReturn(answer);//provamos que hac

    //then

    //Act
    Answer answerCreated = answerService.createAnswer(postIdToReply, content);

    //Assert 

    assertNotNull(answerCreated);
  }

}