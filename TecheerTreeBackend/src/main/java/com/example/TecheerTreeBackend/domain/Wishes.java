package com.example.TecheerTreeBackend.domain;

import com.example.TecheerTreeBackend.dto.WishRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class Wishes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_id")
    private Long id;
    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private Category category; // 진로, 건강, 인간 관계, 돈, 목표, 학업/성적, 기타
    private Date created_at;
    @Enumerated(EnumType.STRING)
    private WishStatus is_confirm = WishStatus.PENDING; // 초기값 = "보류중"
    private Boolean is_deleted = Boolean.FALSE;

    public Wishes(String title, String content, Category category, Date createAt) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.created_at = createAt;
    }

    public static Wishes createWish(WishRequest wishRequest) {
        if (wishRequest.getTitle() == null || wishRequest.getContent() == null || wishRequest.getCategory() == null || wishRequest.getCreate_at() == null) {
            throw new IllegalArgumentException("Title, Content, Category, and Created_at cannot be null.");
        }

        // 클라이언트에서 받은 category 한글 문자열을 Category Enum으로 변환
        Category category = Category.fromKoreanName(wishRequest.getCategory());

        return new Wishes(
                wishRequest.getTitle(),
                wishRequest.getContent(),
                category,
                wishRequest.getCreate_at()
        );
    }

    public void softDelete() {
        this.is_deleted = Boolean.TRUE;
    }

    public String confirm(WishStatus wishStatus) {
        this.is_confirm = wishStatus;
        return this.is_confirm.getDescription(); // 상태의 설명을 반환
    }

    public Boolean checkConfirm() {
        return this.is_confirm == WishStatus.APPROVED;
    }
}
