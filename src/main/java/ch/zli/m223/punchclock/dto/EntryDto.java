package ch.zli.m223.punchclock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EntryDto {

    private Long id;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private ReturnUserDto user;

}
