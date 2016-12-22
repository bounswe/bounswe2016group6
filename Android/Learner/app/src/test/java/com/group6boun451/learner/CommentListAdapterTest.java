package com.group6boun451.learner;

import com.group6boun451.learner.model.Comment;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class CommentListAdapterTest {
    String testString = "test";
    Comment testComment = new Comment(10);

    CommentContainer testComments = new CommentContainer(null);
    CommentListAdapter testAdapter = new CommentListAdapter(null,testComments.getComments());

    @Test
    public void count_isCorrect() throws Exception {

        assertEquals(testAdapter.getCount(), testComments.getComments().size());
    }

    @Test
    public void getItem_isCorrect() throws Exception {

        assertEquals(testAdapter.getItem(0).getContent(), testComments.getComment(0).getContent());
    }

    @Test
    public void itemAddition_isCorrect() throws Exception {
        int size = testAdapter.getCount();
        testComment.setContent(testString);
        testAdapter.add(testComment);
        assertEquals(testAdapter.getItem(size).getContent(), testString);
    }
}