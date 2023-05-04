package com.example.kun_uz_.service;


import com.example.kun_uz_.dto.comment.CommentDTO;
import com.example.kun_uz_.entity.CommentEntity;
import com.example.kun_uz_.exps.ItemNotFoundException;
import com.example.kun_uz_.exps.MethodNotAllowedException;
import com.example.kun_uz_.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public CommentDTO create(CommentDTO dto, Integer creater_id) {
        CommentEntity entity = new CommentEntity();
        entity.setContent(dto.getContent());
        entity.setArticleId(dto.getArticleId());
        entity.setProfileId(creater_id);
        entity.setReplyId(dto.getReplyId());
        commentRepository.save(entity);
        dto.setId(commentRepository.save(entity).getId());
        return dto;
    }

    public CommentDTO update(CommentDTO dto, String commentId, Integer user_id) {
        CommentEntity entity = getById(commentId);
        if (!entity.getProfileId().equals(user_id)){
            throw new MethodNotAllowedException("Method not allowed");
        }
        entity.setContent(dto.getContent());
        entity.setUpdateDate(LocalDateTime.now());
        commentRepository.save(entity);
        return toDTO(entity);
    }

    private CommentEntity getById(String id) {
        Optional<CommentEntity> optional = commentRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Item not found");
        }
        if (optional.get().getVisible() == false) {
            throw new ItemNotFoundException("Item not found");
        }
        return optional.get();
    }

    private CommentDTO toDTO(CommentEntity entity) {
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setReplyId(entity.getReplyId());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setArticleId(entity.getArticleId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    private List<CommentDTO> toList(List<CommentEntity> list) {
        List<CommentDTO> dtoList = new ArrayList<>();
        list.forEach(commentEntity -> dtoList.add(toDTO(commentEntity)));
        return dtoList;
    }

    public Integer delete(String id) {
        int res= commentRepository.updateVisible(false, id);
        commentRepository.deleteReplyIdComment(id);
        return res;
    }

    public List<CommentDTO> list() {
        return toList(commentRepository.getByVisible(true));
    }

}
