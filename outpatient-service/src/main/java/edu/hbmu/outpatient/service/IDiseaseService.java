package edu.hbmu.outpatient.service;

import edu.hbmu.outpatient.domain.entity.Disease;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-03-19
 */
public interface IDiseaseService extends IService<Disease> {

    Disease getDiseaseById(Long id);

    List<Disease> findDiseasesByName(String name);

    List<String> getDiseaseSuggestion(String prefix);
}
