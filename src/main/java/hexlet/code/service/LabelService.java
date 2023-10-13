package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import org.springframework.stereotype.Service;


public interface LabelService {

    Label createLabel(LabelDto labelDto);
    Label updateLabel(LabelDto labelDto, Long id);
    void deleteLabel(Long id);
}
