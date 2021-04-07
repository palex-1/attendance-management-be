package it.palex.attendanceManagement.data.service.core;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.core.QSupportedLangI18n;
import it.palex.attendanceManagement.data.entities.core.SupportedLangI18n;
import it.palex.attendanceManagement.data.entities.enumTypes.SupportedLangsEnumI18N;
import it.palex.attendanceManagement.data.repository.core.SupportedLangI18nRepository;

@Service
public class SupportedLangI18nService {

	private QSupportedLangI18n QSLI18N = QSupportedLangI18n.supportedLangI18n;
	
	@Autowired
	private SupportedLangI18nRepository supportedLangI18nRepository;
	
	
	public SupportedLangI18n findByKey(SupportedLangsEnumI18N lang) {

		String langSrt = lang == null ? null : lang.name();

		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QSLI18N.lang.equalsIgnoreCase(langSrt));

		Iterator<SupportedLangI18n> it = this.supportedLangI18nRepository.findAll(cond).iterator();

		if (it.hasNext()) {
			return it.next();
		}

		return null;
	}

	
	
	
}
