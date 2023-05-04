package com.example.kun_uz_.service;
import com.example.kun_uz_.dto.Tag.TagDTO;
import com.example.kun_uz_.entity.TagEntity;
import com.example.kun_uz_.exps.ItemAlreadyExistsException;
import com.example.kun_uz_.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;
    public TagDTO create( TagDTO dto, Integer moderId) {
        TagEntity old = tagRepository.findByName(dto.getName());
        if (old != null) {
            throw new ItemAlreadyExistsException("Item already exist");
        }
        TagEntity entity = new TagEntity();
        entity.setName(dto.getName());
        entity = tagRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public TagDTO update( TagDTO dto, String id) {
        if (dto.getName() == null){
            throw new RuntimeException("this tag name is null!");
        }
        TagEntity entity = new TagEntity();
        entity.setName(dto.getName());
        tagRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public boolean delete(Integer id) {
        TagEntity entity = tagRepository.findById(id).orElse(null);
        if (entity == null) {
            throw new RuntimeException("this article is null");
        }
        tagRepository.delete(entity);
        return true;
    }
    public List<TagDTO> getList(){
        List<TagEntity> list = tagRepository.findAll();
        List<TagDTO> tagDTOList = new LinkedList<>();
        for (TagEntity e : list){
            TagDTO dto = new TagDTO();
            dto.setId(e.getId());
            dto.setName(e.getName());
            tagDTOList.add(dto);
        }
        return tagDTOList;
    }

}
