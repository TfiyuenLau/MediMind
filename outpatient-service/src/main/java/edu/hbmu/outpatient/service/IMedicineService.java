package edu.hbmu.outpatient.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.hbmu.outpatient.domain.entity.Medicine;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-03-19
 */
public interface IMedicineService extends IService<Medicine> {

    Medicine getMedicineById(Long id);

    IPage<Medicine> getMedicineByPage(Long page);

    List<Medicine> findMedicineByName(String medicineName) throws IOException;

    List<String> getMedicineSuggestion(String prefix);
}
