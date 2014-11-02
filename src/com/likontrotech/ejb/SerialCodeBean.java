package com.likontrotech.ejb;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import com.likontrotech.ejb.base.BaseBean;
import com.likontrotech.ejb.entities.CommercialDocument;

@Stateless(name = "SerialCodeEJB")
public class SerialCodeBean extends BaseBean<CommercialDocument> implements
		SerialCodeLocal {
	public synchronized String getSerialCodeAndIncrement() {
		try {
			Integer value = (Integer) em
					.createNativeQuery(
							"select \"codeVal\" from \"SerialCode\" where code = 'LKT'")
					.getSingleResult();
			em.createNativeQuery(
					"update \"SerialCode\" set \"codeVal\" = \"codeVal\" + 1 where code = 'LKT'")
					.executeUpdate();

			return SumAsWords.fillZeros(String.valueOf(value.intValue()), 5);

		} catch (NoResultException nre) {
			return null;
		}
	}

	public String getSerialCodeString() {
		try {
			Integer value = (Integer) em
					.createNativeQuery(
							"select \"codeVal\" from \"SerialCode\" where code = 'LKT'")
					.getSingleResult();

			return SumAsWords.fillZeros(String.valueOf(value.intValue()), 5);

		} catch (NoResultException nre) {
			return null;
		}
	}	
	
	public Integer getSerialCode() {
		try {
			Integer value = (Integer) em
					.createNativeQuery(
							"select \"codeVal\" from \"SerialCode\" where code = 'LKT'")
					.getSingleResult();

			return value;

		} catch (NoResultException nre) {
			return null;
		}
	}

	public void setSerialCode(Integer value) {
		em.createNativeQuery(
				"update \"SerialCode\" set \"codeVal\" = :value where code = 'LKT'")
				.setParameter("value", value).executeUpdate();
	}
}
