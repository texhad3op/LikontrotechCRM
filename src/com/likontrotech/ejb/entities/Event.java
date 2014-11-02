package com.likontrotech.ejb.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.likontrotech.ejb.Utils;
import com.likontrotech.ejb.base.BaseEntity;

@Entity
@Table(name = "events")
public class Event extends BaseEntity {

	@Id
	@GeneratedValue(generator = "eventsSeqName")
	@GenericGenerator(name = "eventsSeqName", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "events_id_seq") })
	private Long id;

	@ManyToOne
	@JoinColumn(name = "representative_id")
	private Representative representative;

	@ManyToOne
	@JoinColumn(name = "parent_event_id")
	private Event parentEvent;	
	
	@Column(name = "type")
	private Integer type = EventType.REPORT;

	public EventType getType() {
		return Utils.getEventType(type);
	}

	public void setType(EventType eventType) {
		this.type = eventType.getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getEventTime() {
		return eventTime;
	}

	public void setEventTime(Timestamp eventTime) {
		this.eventTime = eventTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "time")
	private Timestamp eventTime;

	@Column(name = "description", length = 4000, nullable = true)
	private String description;

	public Representative getRepresentative() {
		return representative;
	}

	public void setRepresentative(Representative representative) {
		this.representative = representative;
	}

	public Event getParentEvent() {
		return parentEvent;
	}

	public void setParentEvent(Event parentEvent) {
		this.parentEvent = parentEvent;
	}
	
	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private List<CommercialDocument> commercialDocuments = new ArrayList<CommercialDocument>();

	public List<CommercialDocument> getCommercialDocuments() {
		return commercialDocuments;
	}

	public void setCommercialDocuments(List<CommercialDocument> commercialDocuments) {
		this.commercialDocuments = commercialDocuments;
	}	

	@Lob
	@Column(name = "attachedFile")
	@Basic(fetch = FetchType.LAZY)
	private byte[] attachedFile;

	public byte[] getAttachedFile() {
		return attachedFile;
	}

	public void setAttachedFile(byte[] attachedFile) {
		this.attachedFile = attachedFile;
	}
	
	@Column(name = "mimeType", length = 200, nullable = true)
	private String mimeType;

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}	
	
}
