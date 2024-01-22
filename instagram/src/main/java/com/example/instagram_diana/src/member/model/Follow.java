package com.example.instagram_diana.src.member.model;

import com.example.instagram_diana.src.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Builder
@Table(name = "follow")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Follow extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fromUserId", nullable = false)
    private Member fromUser;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "toUserId", nullable = false)
    private Member toUser;


}