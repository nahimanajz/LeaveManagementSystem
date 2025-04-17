package com.leave.dto;

import com.leave.model.Team;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TeamResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TeamResponse() {}

    public TeamResponse(Team team) {
        if (team != null) {
            this.id = team.getId();
            this.name = team.getName();
            this.createdAt = team.getCreatedAt();
            this.updatedAt = team.getUpdatedAt();
        }
    }
} 


 