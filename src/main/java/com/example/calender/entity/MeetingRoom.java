package com.example.calender.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE meeting_room SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Data
@Table(name = "meeting_room")
@Builder
public class MeetingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private long capacity;

    private boolean isOperational;

    @ManyToOne
    @JoinColumn(name = "office_id",nullable = false)
    private Office office;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

}
