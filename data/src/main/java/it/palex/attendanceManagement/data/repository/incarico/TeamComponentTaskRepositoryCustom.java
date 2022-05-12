package it.palex.attendanceManagement.data.repository.incarico;

import it.palex.attendanceManagement.data.entities.WorkTask;

import java.util.Date;

public interface TeamComponentTaskRepositoryCustom {

    public boolean isSpecialPartOfTheTeam(String username, Long taskId);

    public boolean isASpecialRole(String role);

    public boolean isPartOfTheTeam(String username, Long taskId);
}
