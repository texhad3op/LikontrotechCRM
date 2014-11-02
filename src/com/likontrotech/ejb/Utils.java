package com.likontrotech.ejb;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.likontrotech.ejb.entities.CommercialDocumentType;
import com.likontrotech.ejb.entities.CompanyType;
import com.likontrotech.ejb.entities.EventType;
import com.likontrotech.ejb.entities.PaymentType;
import com.likontrotech.ejb.entities.WarehouseOperationType;
import com.likontrotech.ejb.entities.WorkerType;

public class Utils {

	public static List<CompanyType> companyTypes;
	public static List<EventType> eventTypes;
	public static List<CommercialDocumentType> commercialDocumentTypes;
	public static List<Integer> daysTypes;
	public static List<PaymentType> paymentTypes;
	public static List<Integer> suggestionValidDays;
	public static List<WorkerType> workerTypes;
	public static List<WarehouseOperationType> warehouseOperationTypes;

	static {
		companyTypes = new ArrayList<CompanyType>() {
			{
				add(new CompanyType(0, "Pirkėjas"));
				add(new CompanyType(1, "Tiekėjas"));
				add(new CompanyType(2, "Tiekėjas/Pirkėjas"));
			}
		};
		eventTypes = new ArrayList<EventType>() {
			{
				add(new EventType(0, "Ataskaita"));
				add(new EventType(1, "Planuojamas"));
			}
		};
		daysTypes = new ArrayList<Integer>() {
			{
				add(5);
				add(12);
				add(18);
				add(30);
				add(45);
			}
		};

		suggestionValidDays = new ArrayList<Integer>() {
			{
				add(0);
				add(5);
				add(12);
			}
		};

		paymentTypes = new ArrayList<PaymentType>() {
			{
				add(new PaymentType(0, "100% avansinis mokėjimas"));
				add(new PaymentType(1, "pagal sutartį"));
				add(new PaymentType(2,
						"N dienų bėgyje po sąskaitos faktūros išrašymo datos"));
				add(new PaymentType(3, "abipusiu susitarimu"));
			}
		};

		warehouseOperationTypes = new ArrayList<WarehouseOperationType>() {
			{
				add(new WarehouseOperationType(0, "in"));
				add(new WarehouseOperationType(1, "out"));
			}
		};
		workerTypes = new ArrayList<WorkerType>() {
			{
				add(new WorkerType(0, "Darbuotojas"));
				add(new WorkerType(1, "Administratorius"));
				add(new WorkerType(2, "Direktorius"));
				add(new WorkerType(3, "Buhalterė"));
			}
		};

		commercialDocumentTypes = new ArrayList<CommercialDocumentType>() {
			{
				add(new CommercialDocumentType(
						CommercialDocumentType.COMMERCIAL_OFFER,
						"Komercinis pasiūlymas"));
				add(new CommercialDocumentType(
						CommercialDocumentType.PROFORMA_INVOICE,
						"Sąskaita išankstiniam apmokejimui"));
				add(new CommercialDocumentType(CommercialDocumentType.INVOICE,
						"Sąskaita-faktūra"));
			}
		};
	}

	public static CompanyType getCompanyType(int type) {
		return companyTypes.get(type);
	}

	public static CommercialDocumentType getCommercialDocumentType(int type) {
		return commercialDocumentTypes.get(type);
	}

	public static EventType getEventType(int type) {
		return eventTypes.get(type);
	}

	public static PaymentType getPaymentType(int type) {
		return paymentTypes.get(type);
	}

	public static Integer getDays(int type) {
		return daysTypes.get(type);
	}

	public static Integer getSuggestionValidDays(int type) {
		return suggestionValidDays.get(type);
	}

	public static WarehouseOperationType getWarehouseOperationType(int type) {
		return warehouseOperationTypes.get(type);
	}

	public static WorkerType getWorkerType(int type) {
		return workerTypes.get(type);
	}

	public static String getDateForDocument(Calendar calendar) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy MMMM dd",
				new Locale("LT", "lt"));
		String dateString = dateFormat.format(calendar.getTime());
		dateString = dateString.replaceFirst(" ", "-");
		dateString = dateString.replaceFirst(" ", "=");
		dateString = dateString.replaceFirst("-", " m. ");
		dateString = dateString.replaceFirst("=", " mėn. ");
		dateString += " d.";
		return dateString.toLowerCase();
	}
	
	public static String getDateForDocument(Timestamp ts) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy MMMM dd",
				new Locale("LT", "lt"));
		java.util.Date date = new java.util.Date(ts.getTime());
		String dateString = dateFormat.format(date);
		dateString = dateString.replaceFirst(" ", "-");
		dateString = dateString.replaceFirst(" ", "=");
		dateString = dateString.replaceFirst("-", " m. ");
		dateString = dateString.replaceFirst("=", " mėn. ");
		dateString += " d.";
		return dateString.toLowerCase();
	}	
}
