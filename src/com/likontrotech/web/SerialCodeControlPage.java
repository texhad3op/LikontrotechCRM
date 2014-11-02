package com.likontrotech.web;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

import com.likontrotech.ejb.entities.Representative;

public class SerialCodeControlPage extends BasePage {

	public SerialCodeControlPage() {

		final TextField<String> serialNumber = new TextField<String>(
				"serialNumber", new Model<String>(serialCodeLocal
						.getSerialCode().toString()));

		Form<Representative> form = new Form<Representative>("form") {
			@Override
			protected void onSubmit() {
				if (serialNumber.isEnabled()) {
					serialCodeLocal.setSerialCode(Integer.parseInt(serialNumber
							.getConvertedInput()));
					serialNumber.setEnabled(false);
				}
			}

		};
		form.add(serialNumber);

		add(form);

	}
}
