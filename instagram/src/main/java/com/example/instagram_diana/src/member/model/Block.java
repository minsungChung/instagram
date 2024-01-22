package com.example.instagram_diana.src.member.model;

import com.example.instagram_diana.src.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Builder
@Table(name = "block")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Block extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fromUserId", nullable = false)
    private Member fromUser;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "toUserId", nullable = false)
    private Member toUser;

}