package com.kuit.findyou.domain.image.model;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.global.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


@Getter
@Setter
@Entity
@Table(name = "report_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE report_image SET status = 'N' WHERE image_id = ?")
@SQLRestriction("status = 'Y'")
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String filePath;

    @Column(name = "uuid", nullable = false)
    private String imageKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = true)
    private Report report;

    //생성메서드
    public static Image createImage(String filePath, String imageKey) {
        if (imageKey == null || imageKey.isEmpty()) {
            throw new IllegalStateException("UUID cannot be null");
        }
        Image image = new Image();
        image.filePath = filePath;
        image.imageKey = imageKey;
        return image;
    }
    public void setReport(Report report) {
        if (this.report != null && this.report != report) { //이미 다른 보고서가 등록돼있다면 이미지 제거 -> 다른 보고서로 할당되는 오류 방지
            //this.report.removeImage(this);
            this.report.getImages().remove(this);
        }
        this.report = report; //nullable
        if (report != null && !report.getImages().contains(this)) {
            //report.addImage(this);
            report.getImages().add(this);
        }
    }
    public void setImageKey(String imageKey) {
        if (imageKey == null || imageKey.trim().isEmpty()) {
            throw new IllegalArgumentException("UUID cannot be null or empty.");
        }
        this.imageKey = imageKey;
    }
}