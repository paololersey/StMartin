package org.springframework.samples.hibernate.beans;


public class GlobalActivity {


	private Activity activity;
	
	private Person beneficiary;
	
	private Person personInCharge;
	
	private Note note;
	
	private String levelChange;

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	

	public Person getBeneficiary() {
		return beneficiary;
	}

	public void setBeneficiary(Person beneficiary) {
		this.beneficiary = beneficiary;
	}

	public Person getPersonInCharge() {
		return personInCharge;
	}

	public void setPersonInCharge(Person personInCharge) {
		this.personInCharge = personInCharge;
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}

	public String getLevelChange() {
		return levelChange;
	}

	public void setLevelChange(String levelChange) {
		this.levelChange = levelChange;
	}

}
