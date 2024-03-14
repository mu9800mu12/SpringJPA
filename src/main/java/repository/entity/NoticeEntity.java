package repository.entity;


import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "NOTICE")
@DynamicInsert
@DynamicUpdate
@Builder
@Entity
@Cacheable //2차캐시 어노테이션




public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="notice_seq")
    private Long noticeSeq;

    @NonNull
    @Column(name = "title", length = 500, nullable = false)
    private String title;

    @NonNull
    @Column(name = "notice_yn", length = 1, nullable = false)
    private String noticeYn;


    @NonNull
    @Column(name = "contents", nullable = false)
    private String contents;

    @NonNull
    @Column(name = "user_id", nullable = false)
    private String userId;

    @NonNull
    @Column(name = "read_cnt", nullable = false)
    private Long readCnt;

    @NonNull
    @Column(name = "reg_id", updatable = false)
    private String regId;

    @NonNull
    @Column(name = "reg_dt", updatable = false)
    private String regDt;
    @NonNull
    @Column(name = "chg_id")
    private String chgId;

    @NonNull
    @Column(name = "chg_dt")
    private String chgDt;









}