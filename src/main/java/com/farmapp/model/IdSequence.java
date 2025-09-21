package com.farmapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "id_sequences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdSequence {

    @Id
    @Column(name = "entity_name", nullable = false, length = 100)
    private String entity; // tên entity: deposit, close, sell...

    @Column(name = "last_index")
    private int lastIndex; // chỉ số cuối cùng đã được sinh
}
