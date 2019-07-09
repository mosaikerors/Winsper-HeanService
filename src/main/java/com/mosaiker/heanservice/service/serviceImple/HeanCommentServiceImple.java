package com.mosaiker.heanservice.service.serviceImple;

import com.mosaiker.heanservice.entity.Comment;
import com.mosaiker.heanservice.entity.HeanComment;
import com.mosaiker.heanservice.repository.HeanCommentRepository;
import com.mosaiker.heanservice.service.HeanCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class HeanCommentServiceImple implements HeanCommentService {

    @Autowired
    private HeanCommentRepository heanCommentRepository;

    @Override
    public HeanComment findHeanCommentsOrNew(String hId) {
        Optional<HeanComment> bookComment = heanCommentRepository.findHeanCommentByHId(hId);
        HeanComment bc = null;
        if (!bookComment.isPresent()) {
            bc = new HeanComment();
            bc.setHId(hId);
        } else {
            bc = bookComment.get();
        }
        return bc;
    }

    @Override
    public List<Comment> findComments(String hId) {
        HeanComment bookComment = findHeanCommentsOrNew(hId);
        return bookComment.getReplies();
    }

    //一级评论
    @Override
    public String saveHeanComments(String hId, List<Comment> comments) {
        HeanComment bc = findHeanCommentsOrNew(hId);
        bc.setReplies(comments);
        heanCommentRepository.save(bc);
        return "ok";
    }

    //给一个HeanComment加一个子Comment
    @Override
    public void saveComment(HeanComment oldComment, Long uId, String username, String content) {
        //新建评论
        Comment comment = new Comment();
        comment.setUserId(uId);
        comment.setUsername(username);
        comment.setContent(content);
        comment.setCommentTime(new Date().getTime());
        List<Comment> newReplies = oldComment.getReplies();
        if (newReplies == null) {
            newReplies = new LinkedList<>();
        }
        newReplies.add(comment);
        oldComment.setReplies(newReplies);
    }

    //任意一级的回复
    //有点难啊，要搜索树，级联保存
    @Override
    public String saveReply(String hId, List<Integer> commentIndex, Long uId, String username, String content) {
        //初始化curComment和curCommentLayer
        HeanComment bookComment = findHeanCommentsOrNew(hId);
        Comment curComment = null;
        List<Comment> curLayerComments = bookComment.getReplies();
        if (commentIndex.size() == 0) {
            saveComment(bookComment,uId,username,content);
        } else {
            //通过commentIndex找到最底层的curComment，新增评论
            for (int i = 0; i < commentIndex.size(); i++) {
                if (curLayerComments != null) {
                    curComment = curLayerComments.get(commentIndex.get(i));
                    curLayerComments = curComment.getReplies();
                } else {
                    return "error";
                }
            }
            saveComment(curComment,uId,username,content);
        }
        heanCommentRepository.save(bookComment);
        return "ok";
    }
}