package edu.hbmu.outpatient.domain.doc;

import edu.hbmu.outpatient.domain.entity.Medicine;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Medicine ES文档对象
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-17
 */
@Getter
@Setter
public class MedicineDoc {

    private Long medicineId;

    private String medicineName;

    private String medicineDescription;

    private List<String> suggestion;

    public MedicineDoc() {
    }

    public MedicineDoc(Medicine medicine) {
        this.medicineId = medicine.getMedicineId();
        this.medicineName = medicine.getMedicineName();
        this.medicineDescription = medicine.getMedicineDescription();
        this.suggestion = Collections.singletonList(this.medicineName);
    }

}
