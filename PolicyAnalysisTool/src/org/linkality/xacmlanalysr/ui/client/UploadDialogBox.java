package org.linkality.xacmlanalysr.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UploadDialogBox extends DialogBox {
	public UploadDialogBox() {
	    //Add an upload field
	    final FileUpload fileUpload = new FileUpload();
	    fileUpload.ensureDebugId("cwFileUpload");
	    fileUpload.setName("file");

	    // Create the dialog box
	    this.setText("Welcome to the Semantic Policy Analyser!");
	    this.setAnimationEnabled(true);
	    final FormPanel form = new FormPanel();
	    form.setAction(GWT.getModuleBaseURL() + "upload");
	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);
	    form.addFormHandler(new FormHandler() {
			public void onSubmit(FormSubmitEvent event) {	
				if (fileUpload.getFilename().length() == 0) {
			          Window.alert("You must select a file.");
			          event.setCancelled(true);
			        }
			}
			public void onSubmitComplete(FormSubmitCompleteEvent event) {
				Window.alert("Upload succeeded. " + event.getResults());
			}});
	        Hidden hiddenParam = new Hidden("action", "uploadXacml");

	    // Add a button to upload the file
	    Button uploadButton = new Button("Upload file!");    
	    
	    VerticalPanel dialogVPanel = new VerticalPanel();    
	    dialogVPanel.setWidth("100%");
	    dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
	    dialogVPanel.add(new HTML("<p/>At first, you need to upload a XACML file to work with:"));
	    dialogVPanel.add(hiddenParam);
	    dialogVPanel.add(fileUpload);
	    dialogVPanel.add(new HTML("<br>"));
	    dialogVPanel.add(uploadButton);

	    uploadButton.addClickListener(new ClickListener() {
	      public void onClick(Widget sender) {
	    	  try {
	    		  form.submit();
	        ((DialogBox) sender.getParent().getParent().getParent().getParent()).hide();
	    	  } catch (Exception e) {
	    		  e.printStackTrace();
	    	  }
	      }
	    });

	    // Set the contents of the Widget
	    form.add(dialogVPanel);
	    this.setWidget(form);

	}
}
