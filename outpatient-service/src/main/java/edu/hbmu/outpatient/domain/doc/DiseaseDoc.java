package edu.hbmu.outpatient.domain.doc;

import edu.hbmu.outpatient.domain.entity.Disease;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-03-19
 */
@Getter
@Setter
public class DiseaseDoc implements Serializable {

    private Long diseaseId;

    private String diseaseName;

    private String diseaseDescription;

    private List<String> suggestion;

    public DiseaseDoc() {
    }

    public DiseaseDoc(Disease disease) {
        this.diseaseId = disease.getDiseaseId();
        this.diseaseName = disease.getDiseaseName();
        this.diseaseDescription = disease.getDiseaseDescription();
        this.suggestion = Collections.singletonList(this.diseaseName);
    }

}
