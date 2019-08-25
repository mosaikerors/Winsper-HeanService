package com.mosaiker.heanservice.service.serviceImple;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mosaiker.heanservice.entity.Hean;
import com.mosaiker.heanservice.entity.HeanComment;
import com.mosaiker.heanservice.repository.HeanCommentRepository;
import com.mosaiker.heanservice.repository.HeanRepository;
import com.mosaiker.heanservice.service.HeanCommentService;
import com.mosaiker.heanservice.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HeanCommentServiceImple implements HeanCommentService {

    @Autowired
    private HeanCommentRepository heanCommentRepository;
    @Autowired
    private HeanRepository heanRepository;
    @Autowired
    private UserInfoService userInfoService;

    @Override
    public HeanComment findHeanCommentByCId(String cId) {
        return heanCommentRepository.findHeanCommentByCommentId(cId);
    }

    //给一个HeanComment加一个子Comment
    @Override
    public Integer saveComment(HeanComment newComment) {
        heanCommentRepository.save(newComment);
        if (newComment.getTargetCommentId() != null) {
            HeanComment commented = heanCommentRepository
                    .findHeanCommentByCommentId(newComment.getTargetCommentId());
            commented.addReply(newComment.getCommentId());
            heanCommentRepository.save(commented);
        }
        Hean comed = heanRepository.findByHId(newComment.getHId());
        comed.addComment(newComment.getCommentId());
        heanRepository.save(comed);

        return 0;
    }

    @Override
    public JSONObject getComJSONObject(String cId) {
        HeanComment heanComment = findHeanCommentByCId(cId);
        JSONObject ret = new JSONObject();
        ret.put("commentId", heanComment.getCommentId());
        String commenter = userInfoService.getSimpleInfo(heanComment.getuId()).getString("username");
        ret.put("commenter", commenter);
        if (!heanComment.getTargetCommentId().equals("") && heanComment.getTargetCommentId() != null) {
            HeanComment target = findHeanCommentByCId(heanComment.getTargetCommentId());
            String commented = userInfoService.getSimpleInfo(target.getuId()).getString("username");
            ret.put("commented", commented);
        }
        ret.put("time", heanComment.getCommentTime());
        ret.put("content", heanComment.getContent());
        return ret;
    }

    @Override
    public JSONArray findAllByUId(Long owner) {
        List<HeanComment> commentList = heanCommentRepository.findAllByUId(owner);
        JSONArray array = new JSONArray();
        for (HeanComment com : commentList) {
            JSONObject onecom = new JSONObject();
            Boolean isCom = ((com.getTargetCommentId() == null) || (com.getTargetCommentId().isEmpty()));
            onecom.put("isComment", isCom);
            if (isCom) {
                Hean commed = heanRepository.findByHId(com.getHId());
                if (commed == null) {
                    //该hean已被删除
                    continue;
                }
                String username = userInfoService.getSimpleInfo(commed.getUId()).getString("username");
                onecom.put("username", username);
            } else {
                Long uId = heanCommentRepository.findHeanCommentByCommentId(com.getTargetCommentId())
                        .getuId();
                String username = userInfoService.getSimpleInfo(uId).getString("username");
                onecom.put("username", username);
            }
            onecom.put("content", com.getContent());
            onecom.put("time", com.getCommentTime());
            onecom.put("hId", com.getHId());
            array.add(onecom);
        }
        return array;
    }

}