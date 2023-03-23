package com.example.calender.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE office SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Data
@Table(name = "office")
@Builder
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "location")
    private String location;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

}
