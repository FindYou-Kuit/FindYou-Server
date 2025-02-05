package com.kuit.findyou.domain.report.model;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.global.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE report SET status = 'N' WHERE report_id = ?")
@SQLRestriction("status = 'Y'")
public class Report extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag", length = 50, nullable = false)
    private ReportTag tag;

    @Column(name = "event_location", length = 200, nullable = false)
    private String eventLocation;

    @Column(name = "event_date", nullable = false, columnDefinition = "DATE")
    private LocalDate eventDate;

    @Column(name = "additional_description")
    private String additionalDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // 신고 동물에 대해 CascadeType.ALL 및 orphanRemoval = true 적용
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "report_animal_id", nullable = false)
    private ReportAnimal reportAnimal;

    // 신고글 이미지에 대해 orphanRemoval = true 만 적용
    @OneToMany(mappedBy = "report", orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    // 최근 본 신고글 삭제를 위한 양방향 연관관계 설정
    // orphanRemoval = true 만 설정
    @OneToMany(mappedBy = "report", orphanRemoval = true)
    private List<ViewedReport> viewedReports = new ArrayList<>();

    // 관심 신고글 삭제를 위한 양방향 연관관계 설정
    // orphanRemoval = true 만 설정
    @OneToMany(mappedBy = "report", orphanRemoval = true)
    private List<InterestReport> interestReports = new ArrayList<>();

    //==생성 메서드==// -> 생성자 말고 생성 메서드를 별도로 만든 형태
    public static Report createReport(ReportTag tag, String eventLocation, LocalDate eventDate, String additionalDescription, User user, ReportAnimal reportAnimal, List<Image> images) {
        Report report = new Report();
        report.tag = tag;
        report.eventLocation = eventLocation;
        report.eventDate = eventDate;
        report.additionalDescription = additionalDescription;
        report.setUser(user);
        report.reportAnimal = reportAnimal;
        if(images != null && !images.isEmpty()) {
            images.forEach(report::addImage);
        }
        return report;
    }

    private void setUser(User user) {
        this.user = user;
        user.addReport(this);
    }

    public String getReportAnimalBreedName(){
        return this.reportAnimal.getBreedName();
    }
  
    /*public void addImage(Image image) {
        this.images.add(image);
        image.setReport(this);
    }*/
    public void addImage(Image image) {
        if (!this.images.contains(image)) {
            this.images.add(image);
            image.setReport(this);
        }
    }

    public void removeImage(Image image) {
        if(this.images.remove(image)) {
            image.setReport(null);
        }
    }

    // 이미지 리스트 반환 메서드
    public List<Image> getImages() {
        return Collections.unmodifiableList(images);
    }


    public void addViewedReport(ViewedReport viewedReport) {
        viewedReports.add(viewedReport);
    }

    public void addInterestReport(InterestReport interestReport) {
        interestReports.add(interestReport);
    }

    public String getThumbnailImage(){
        if(images == null || images.isEmpty()){
            return null;
        }
        return images.get(0).getFilePath();
    }
}
