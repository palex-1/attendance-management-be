package it.palex.attendanceManagement.batch.deleteChangePasswordHistory;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.data.service.user.UserPasswordChangeHistoryService;

@Component
public class DeleteChangePasswordHistoryTransactionalService {

	
	@Autowired
	private UserPasswordChangeHistoryService userPasswordChangeHistorySrv;

	
	@Transactional(rollbackFor = Exception.class )
	public int deleteAllSuccLoginLogsMadeBefore(Calendar date) {
		return this.userPasswordChangeHistorySrv.deleteAllSuccLoginLogsMadeBefore(date);
	}
	
	
	
	
}
