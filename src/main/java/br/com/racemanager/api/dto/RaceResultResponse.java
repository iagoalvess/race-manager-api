package br.com.racemanager.api.dto;

import br.com.racemanager.api.model.RaceResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceResultResponse {
    private Long id;
    private String participantName;
    private String bibNumber;
    private String categoryName;
    private String startTime;
    private String finishTime;
    private String totalTime;
    private Integer position;
    private Integer categoryPosition;
    private String status;
    private String createdAt;
    private String updatedAt;

    public static RaceResultResponse fromEntity(RaceResult result) {
        RaceResultResponse response = new RaceResultResponse();
        response.setId(result.getId());
        response.setParticipantName(result.getParticipant().getFullName());
        response.setBibNumber(result.getParticipant().getBibNumber());
        response.setCategoryName(result.getParticipant().getCategory().getName());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        if (result.getStartTime() != null) {
            response.setStartTime(result.getStartTime().format(formatter));
        }
        if (result.getFinishTime() != null) {
            response.setFinishTime(result.getFinishTime().format(formatter));
        }
        
        if (result.getTotalTimeSeconds() != null) {
            long hours = result.getTotalTimeSeconds() / 3600;
            long minutes = (result.getTotalTimeSeconds() % 3600) / 60;
            long seconds = result.getTotalTimeSeconds() % 60;
            response.setTotalTime(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        }
        
        response.setPosition(result.getPosition());
        response.setCategoryPosition(result.getCategoryPosition());
        response.setStatus(result.getStatus().name());
        
        if (result.getCreatedAt() != null) {
            response.setCreatedAt(result.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
        if (result.getUpdatedAt() != null) {
            response.setUpdatedAt(result.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
        
        return response;
    }
}
