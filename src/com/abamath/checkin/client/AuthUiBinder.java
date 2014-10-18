package com.abamath.checkin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AuthUiBinder extends Composite {

	private static AuthUiBinderUiBinder uiBinder = GWT
			.create(AuthUiBinderUiBinder.class);
	
	

	interface AuthUiBinderUiBinder extends UiBinder<Widget, AuthUiBinder> {
	}

	public AuthUiBinder() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	  @UiField SpanElement nameSpan;
	  public void setName(String name) { nameSpan.setInnerText(name); }
	  @UiField VerticalPanel loginPanel;
	  public void addLoginPanel (Panel panel) { loginPanel.add(panel);}
	  @UiField VerticalPanel createPanel;
	  public void addCreatePanel (Panel panel) { createPanel.add(panel);}  

}
