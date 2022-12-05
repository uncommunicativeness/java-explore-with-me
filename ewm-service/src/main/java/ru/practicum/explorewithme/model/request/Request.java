package ru.practicum.explorewithme.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.model.event.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "requests",
        uniqueConstraints = {@UniqueConstraint(name = "uc_request_user_id_event_id", columnNames = {"user_id", "event_id"})})
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request_seq")
    Long id;

    @Column(name = "created")
    LocalDateTime created;

    @Enumerated(EnumType.STRING)
    State state;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    User requester;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    private Event event;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        return id.equals(request.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @PrePersist
    void onCreate() {
        this.created = LocalDateTime.now();
    }
}
