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
@SQLDelete(sql = "UPDATE office SET is_active = false WHERE id=?")
@Data
@Table(name = "office")
@Builder
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ReadOnlyProperty
    private Long id;

    @Column(name = "location")
    private String location;

    @Column(name = "is_active")
    private boolean isActive = Boolean.TRUE;

}
