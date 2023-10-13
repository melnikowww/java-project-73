package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LabelServiceImpl implements LabelService{

    @Autowired
    LabelRepository labelRepository;
    @Autowired
    TaskRepository taskRepository;

    @Override
    public Label createLabel(LabelDto labelDto) {
        Label label = new Label();
        label.setName(labelDto.getName());
        return labelRepository.save(label);
    }

    @Override
    public Label updateLabel(LabelDto labelDto, Long id) {
        Label label = labelRepository.findById(id).orElseThrow();
        label.setName(labelDto.getName());
        return labelRepository.save(label);
    }

    @Override
    public void deleteLabel(Long id) {
        Label label = labelRepository.findById(id).orElseThrow();

    }
}
