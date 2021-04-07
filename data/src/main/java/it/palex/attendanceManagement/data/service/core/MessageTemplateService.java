package it.palex.attendanceManagement.data.service.core;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.core.MessageTemplate;
import it.palex.attendanceManagement.data.entities.core.QMessageTemplate;
import it.palex.attendanceManagement.data.entities.enumTypes.MessageTypeEnum;
import it.palex.attendanceManagement.data.entities.enumTypes.SupportedLangsEnumI18N;
import it.palex.attendanceManagement.data.repository.core.MessageTemplateRepository;

@Service
public class MessageTemplateService {

	private final QMessageTemplate QMT =  QMessageTemplate.messageTemplate;
	
	@Autowired
	private MessageTemplateRepository messageTemplateRepo;
	
	
	public MessageTemplate findByKey(MessageTypeEnum messageType, SupportedLangsEnumI18N lang) {
		String mTypeStr = messageType==null ? null : messageType.name();
		String langSrt = lang==null ? null : lang.name();
		
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QMT.mType.mType.equalsIgnoreCase(mTypeStr));
		cond.and(QMT.lang.lang.equalsIgnoreCase(langSrt));
		
		Iterator<MessageTemplate> it = this.messageTemplateRepo.findAll(cond).iterator();
		
		if(it.hasNext()) {
			return it.next();
		}
		
		return null;
	}
	
	public String findOnlyMessageByKey(MessageTypeEnum messageType, SupportedLangsEnumI18N lang) {
		MessageTemplate park = this.findByKey(messageType, lang);
		if(park!=null) {
			return park.getMessage();
		}
		return null;
	}
	
	
}

