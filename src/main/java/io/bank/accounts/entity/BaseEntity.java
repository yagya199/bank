package io.bank.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@ToString
public class BaseEntity {

   /* @Column(updatable = false): This means that the createdAt
    field is set only when a new record is created and cannot be change
            (updated) afterward. It typically holds the timestamp of when
    the record was created.

    */

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(updatable = false)
    private String createdBy;


   /*
   @Column(insertable = false): This indicates that the updatedAt field
    is not set during the initial insert of a record. It is typically
    updated automatically(for example, by a trigger or in your application logic)
    whenever the record is modified.

    */

    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @Column(insertable = false)
    private String updatedBy;
}
