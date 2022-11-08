package ru.practicum.explorewithme.dto.compilation;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationInDto {
    @NotNull
    String title;
    @NotNull
    Boolean pinned;
    List<Long> events;
}
