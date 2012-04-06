package org.linkality.xacmlanalysr.ui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Analysr implements EntryPoint {
	public final String CONTROLLER_URL = GWT.getModuleBaseURL() + "controller";
	DecoratorPanel rightContent;
	HTML fileName = new HTML();
	HTML combiningAlgorithm = new HTML();
	HTML rulesCount = new HTML();
	HTML version = new HTML();
	HTML description = new HTML();
	ServerControllerAsync controllerService = (ServerControllerAsync) GWT.create(ServerController.class);
	ServiceDefTarget controllerServiceEndpoint = (ServiceDefTarget) controllerService;

	public Analysr() {
		super();
		controllerServiceEndpoint.setServiceEntryPoint(CONTROLLER_URL);
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		retrieveInfo();
		RootPanel.get("leftPart").add(getLeftMenu());
		VerticalPanel rightVPanel = new VerticalPanel();
		rightVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		rightVPanel.setWidth("500px");
		rightVPanel.setHeight("300px");
		rightVPanel.addStyleName("rightVPanel");
		rightVPanel.add(getInfoPanel());
		rightContent = new DecoratorPanel();
		rightContent.setWidget(rightVPanel);

		RootPanel.get("rightPart").add(rightContent);
		RootPanel.get("topPart").add(getTopPart());
	}
	
	private Widget getTopPart() {
		HorizontalPanel horizPanel = new HorizontalPanel();
		horizPanel.setWidth("750px");
		horizPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		horizPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

		HTML title = new HTML("<img src=\"analysr.png\"><br><b>semantic</b>xacml<b>analysis</b><br>&nbsp;");
		title.addStyleName("mainTitle");
		title.setWidth("100%");
		horizPanel.add(title);
		horizPanel.setCellWidth(title, "500px");
		horizPanel.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_LEFT);
		
//		Image hydra = new Image("hydralogo.gif");
//		hydra.setHeight("50px");
//		horizPanel.add(hydra);
//
//		Image sit = new Image("sit_logo.gif");
//		sit.setHeight("50px");
//		horizPanel.add(sit);
		
		return horizPanel;
	}

	private VerticalPanel getLeftMenu() {
		VerticalPanel leftVPanel = new VerticalPanel();
		leftVPanel.setWidth("100%");
		leftVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);

		// Create a new stack panel
		DecoratedStackPanel stackPanel = new DecoratedStackPanel();
		stackPanel.setWidth("200px");
		stackPanel.setTitle("Menu");
		stackPanel.setHeight("310px");

		// Add image and button to the RootPanel
		RootPanel.get("leftPart").add(leftVPanel);

		// Add the stack folders
		VerticalPanel analysisEntries = new VerticalPanel();
		analysisEntries.addStyleName("menu");
		HTML entryExplainOutcomes = new HTML("<a href=\"#\">Explain effects</a>");
		HTML entryGetRequiredAttributes = new HTML("<a href=\"#\">Get required attributes</a>");
		analysisEntries.add(entryExplainOutcomes);
		analysisEntries.add(entryGetRequiredAttributes);
		analysisEntries.add(new HTML("More to come ..."));

		VerticalPanel mainEntries = new VerticalPanel();
		mainEntries.addStyleName("menu");
		HTML entryExportToOWL = new HTML("<a href=\"#\">Export to OWL</a>");
		HTML entryExportToProlog = new HTML("<a href=\"#\">Export to Prolog</a>");
		HTML entryUploadXACML = new HTML("<a href=\"#\">Upload XACML file</a>");
		HTML entryCheckConsistency = new HTML("<a href=\"#\">Check consistency</a>");
		mainEntries.add(entryUploadXACML);
		mainEntries.add(entryExportToOWL);
		mainEntries.add(entryExportToProlog);


		VerticalPanel validateEntries = new VerticalPanel();
		validateEntries.addStyleName("menu");
		validateEntries.add(entryCheckConsistency);
		validateEntries.add(new HTML("More to come"));

		String mainHeader = "Transformation";
		String analysisHeader = "Analysis";
		String validationHeader = "Validation";
		stackPanel.add(mainEntries, mainHeader, true);
		stackPanel.add(validateEntries, validationHeader, true);
		stackPanel.add(analysisEntries, analysisHeader, true);

		leftVPanel.add(stackPanel);
		final UploadDialogBox dialogBox = new UploadDialogBox();

		entryGetRequiredAttributes.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				rightContent.setWidget(getRequiredAttributesPanel());
			}
		});

		entryExplainOutcomes.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				rightContent.setWidget(getExplanationPanel());
			}
		});

		entryExportToOWL.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				rightContent.setWidget(getPanelExportToOWL());
			}
		});

		entryExportToProlog.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				rightContent.setWidget(getPanelExportToProlog());
			}
		});

		entryCheckConsistency.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				try {
					AsyncCallback callback = new AsyncCallback() {
						public void onSuccess(Object result) {
							JSONObject resultObj = (JSONObject) JSONParser.parse((String) result);
							String res = ((JSONString) resultObj.get("result")).stringValue();
							if (res.equals("true")) {
								res = "Congratulations!\n\nThe consistency check passed. That means the structure of your policy is XACML compliant";
							}
							showMessageBox("Consistency check", res);
						}

						public void onFailure(Throwable caught) {
							showMessageBox("Server request failed", caught.getMessage());
							// Textarea displays any exception messages
							System.out.println("Server request raised an error; Java exception : "
									+ caught == null ? "An unknown exception"
									: caught.getMessage());
						}
					};
					// Call the service method, validating the form
					// values first.
					try {
						JSONObject req = new JSONObject();
						req.put("action", new JSONString("checkConsistency"));
						controllerService.handleRequest(req.toString(), callback);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		entryUploadXACML.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				dialogBox.center();
				dialogBox.show();
			}
		});

		return leftVPanel;
	}

	private DialogBox showMessageBox(String title, String text) {
		// Create a dialog box and set the caption text
		final DialogBox dialogBox = new DialogBox();
		dialogBox.ensureDebugId("cwDialogBox");
		dialogBox.setText(title);
		dialogBox.setAnimationEnabled(true);

		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setSpacing(4);
//		dialogContents.setSize("500px", "400px");
		dialogBox.setWidget(dialogContents);

		// Add some text to the top of the dialog

		HTML details = new HTML();
		dialogContents.ensureDebugId("dialogboxcontent");
		details.setText(text);
		details.setWordWrap(true);
//		dialogContents.setWidth("500px");
//		dialogContents.setHeight("400px");
		details.addStyleName("message_box_text");
		// details.setReadOnly(true);
		dialogContents.add(details);
		dialogContents.setCellHorizontalAlignment(details, HasHorizontalAlignment.ALIGN_CENTER);

		// Add a close button at the bottom of the dialog
		Button closeButton = new Button("Close", new ClickListener() {
			public void onClick(Widget sender) {
				dialogBox.hide();
			}
		});
		dialogContents.add(closeButton);
		if (LocaleInfo.getCurrentLocale().isRTL()) {
			dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_LEFT);

		} else {
			dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
		}
		dialogBox.center();
		dialogBox.show();

		// Return the dialog box
		return dialogBox;
	}

	private VerticalPanel getExplanationPanel() {
		VerticalPanel vertPanel = new VerticalPanel();
		vertPanel.setSize("450px", "300px");
		FlexTable layout = new FlexTable();
		layout.setCellSpacing(6);

		// Add a title to the form
		layout.setHTML(0, 0, "<b>Get explained the effects of your policy</b>");
		layout.getFlexCellFormatter().setColSpan(0, 0, 2);
		layout.getFlexCellFormatter().setHeight(0, 0, "30px");
		layout.setBorderWidth(0);
		layout.setWidth("450px");

		// Add some standard form options
		Hidden actionParam = new Hidden("action", "getExplanation");
		vertPanel.add(actionParam);
		// Make a new list box, adding a few items to it.
		final ListBox lbTargets = new ListBox();
		final ListBox lbEffects = new ListBox();
		lbEffects.setName("effect");
		lbTargets.setName("target");
		lbEffects.addItem("permit");
		lbEffects.addItem("deny");
	    getPolicyTargets(lbTargets);
	    lbTargets.setVisibleItemCount(1);
		layout.setHTML(1, 0, "<b>Select the target</b>");
		layout.setWidget(1, 1, lbTargets);
		layout.setHTML(2, 0, "<b>Select the effect</b>");
		layout.setWidget(2, 1, lbEffects);
		Button button = new Button();
		button.setText("Explain!");
		button.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				try {
					AsyncCallback callback = new AsyncCallback() {
						public void onSuccess(Object result) {
							JSONObject resultObj = (JSONObject) JSONParser.parse((String) result);
							JSONArray res = ((JSONArray) resultObj.get("result"));
							String messageText = "";
							for (int i=0;i<res.size();i++) {
								messageText += res.get(i) + "\n";
							}
							showMessageBox("Explanation", messageText);
						}

						public void onFailure(Throwable caught) {
							showMessageBox("Server request failed", caught.getMessage());
							System.out.println("Server request raised an error; Java exception : "
									+ caught == null ? "An unknown exception"
									: caught.getMessage());
						}
					};
					try {
						JSONObject req = new JSONObject();
						req.put("target", new JSONString(lbTargets.getItemText(lbTargets.getSelectedIndex())));
						req.put("effect", new JSONString(lbEffects.getItemText(lbEffects.getSelectedIndex())));
						req.put("action", new JSONString("getExplanation"));
						controllerService.handleRequest(req.toString(), callback);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}});
		vertPanel.add(layout);
		vertPanel.add(button);
	    return vertPanel;
	}	
	
	private VerticalPanel getRequiredAttributesPanel() {
		VerticalPanel vertPanel = new VerticalPanel();
		vertPanel.setSize("450px", "300px");
		FlexTable layout = new FlexTable();
		layout.setCellSpacing(6);

		// Add a title to the form
		layout.setHTML(0, 0, "<b>Find out which attributes are required to evaluate a request</b>");
		layout.getFlexCellFormatter().setColSpan(0, 0, 2);
		layout.getFlexCellFormatter().setHeight(0, 0, "30px");
		layout.setBorderWidth(0);
		layout.setWidth("450px");

		// Add some standard form options
		Hidden actionParam = new Hidden("action", "getRequiredAttributes");
		vertPanel.add(actionParam);
		// Make a new list box, adding a few items to it.
		final ListBox lbTargets = new ListBox();
		final ListBox lbEffects = new ListBox();
		lbEffects.setName("effect");
		lbTargets.setName("target");
		lbEffects.addItem("permit");
		lbEffects.addItem("deny");
	    getPolicyTargets(lbTargets);
	    lbTargets.setVisibleItemCount(1);
		layout.setHTML(1, 0, "<b>Select the target</b>");
		layout.setWidget(1, 1, lbTargets);
		Button button = new Button();
		button.setText("Find out!");
		button.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				try {
					AsyncCallback callback = new AsyncCallback() {
						public void onSuccess(Object result) {
							JSONObject resultObj = (JSONObject) JSONParser.parse((String) result);
							JSONArray res = ((JSONArray) resultObj.get("result"));
							String messageText = "";
							for (int i=0;i<res.size();i++) {
								messageText += res.get(i) + "\n";
							}
							showMessageBox("Required attributes", messageText);
						}

						public void onFailure(Throwable caught) {
							showMessageBox("Server request failed", caught.getMessage());
							System.out.println("Server request raised an error; Java exception : "
									+ caught == null ? "An unknown exception"
									: caught.getMessage());
						}
					};
					try {
						JSONObject req = new JSONObject();
						req.put("target", new JSONString(lbTargets.getItemText(lbTargets.getSelectedIndex())));
						req.put("effect", new JSONString(lbEffects.getItemText(lbEffects.getSelectedIndex())));
						req.put("action", new JSONString("getRequiredAttributes"));
						controllerService.handleRequest(req.toString(), callback);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}});
		layout.setWidget(2, 1, button);
		vertPanel.add(layout);
	    return vertPanel;
	}	

	private VerticalPanel getInfoPanel() {
		FlexTable layout = new FlexTable();
		layout.setCellSpacing(6);

		// Add a title to the form
		layout.setHTML(0, 0, "<b>Information about your policy</b>");
		layout.getFlexCellFormatter().setColSpan(0, 0, 2);
		layout.getFlexCellFormatter().setHeight(0, 0, "30px");
		layout.setBorderWidth(0);
		layout.setWidth("450px");

		// Add some standard form options
		layout.setHTML(1, 0, "<b>File name</b>");
		layout.setWidget(1, 1, fileName);
		layout.setHTML(2, 0, "<b>Combining Algorithm</b>");
		layout.setWidget(2, 1, combiningAlgorithm);
		layout.setHTML(3, 0, "<b>Version</b>");
		layout.setWidget(3, 1, version);
		layout.setHTML(4, 0, "<b>Description</b>");
		layout.setWidget(4, 1, description);

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		vPanel.add(layout);
		return vPanel;
	}

	public void getPolicyTargets(final ListBox lb) {
		try {
			AsyncCallback callback = new AsyncCallback() {
				public void onSuccess(Object result) {
					JSONArray resultObj = (JSONArray) JSONParser.parse((String) result);
					for (int i=0;i<resultObj.size();i++) {
						lb.addItem(((JSONString) resultObj.get(i)).stringValue());
					}
					System.out.println("Done. " + description);
				}

				public void onFailure(Throwable caught) {
					showMessageBox("Server request failed", caught.getMessage());
					System.out.println("Server request raised an error; Java exception : "
							+ caught == null ? "An unknown exception"
							: caught.getMessage());
				}
			};
			try {
				System.out.println("Get targets");
				JSONObject req = new JSONObject();
				req.put("action", new JSONString("getPolicyTargets"));
				controllerService.handleRequest(req.toString(), callback);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void retrieveInfo() {
		try {
			AsyncCallback callback = new AsyncCallback() {
				public void onSuccess(Object result) {
					JSONArray resultObj = (JSONArray) JSONParser.parse((String) result);
					String res = resultObj.toString();
					version.setText(resultObj.get(0).toString());
					fileName.setText(resultObj.get(1).toString());
					combiningAlgorithm.setText(resultObj.get(2).toString());
					description.setText(resultObj.get(3).toString());
					System.out.println("Done. " + description);
				}

				public void onFailure(Throwable caught) {
					showMessageBox("Server request failed", caught.getMessage());
					System.out.println("Server request raised an error; Java exception : "
							+ caught == null ? "An unknown exception"
							: caught.getMessage());
				}
			};
			try {
				JSONObject req = new JSONObject();
				req.put("action", new JSONString("getInfo"));
				controllerService.handleRequest(req.toString(), callback);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public final Widget getPanelExportToOWL() {
		VerticalPanel vertPanel = new VerticalPanel();
		vertPanel.setSize("500px","300px");
		Button button = new Button("Export to OWL!");
		button.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				try {
					AsyncCallback callback = new AsyncCallback() {
						public void onSuccess(Object result) {
							JSONObject resultObj = (JSONObject) JSONParser.parse((String) result);
							String res = ((JSONString) resultObj.get("result")).stringValue();
							showMessageBox("Policy in OWL format", res);
						}

						public void onFailure(Throwable caught) {
							showMessageBox("Server request failed", caught.getMessage());
							System.out.println("Server request raised an error; Java exception : "
									+ caught == null ? "An unknown exception"
									: caught.getMessage());
						}
					};
					try {
						JSONObject req = new JSONObject();
						req.put("action", new JSONString("getAsOWL"));
						controllerService.handleRequest(req.toString(), callback);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} });
		
		vertPanel.add(new HTML("Convert the policy to an ontology representation in OWL"));
		vertPanel.add(button);
		vertPanel.setCellHorizontalAlignment(button, HasHorizontalAlignment.ALIGN_CENTER);
		return vertPanel;
	}

	public final Widget getPanelExportToProlog() {
		VerticalPanel vertPanel = new VerticalPanel();
		vertPanel.setSize("500px","300px");
		Button button = new Button("Export to Prolog!");
		button.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				try {
					AsyncCallback callback = new AsyncCallback() {
						public void onSuccess(Object result) {
							JSONObject resultObj = (JSONObject) JSONParser.parse((String) result);
							String res = ((JSONString) resultObj.get("result")).stringValue();
							showMessageBox("Policy in Prolog format", res);
						}

						public void onFailure(Throwable caught) {
							showMessageBox("Server request failed", caught.getMessage());
							System.out.println("Server request raised an error; Java exception : "
									+ caught == null ? "An unknown exception"
									: caught.getMessage());
						}
					};
					try {
						JSONObject req = new JSONObject();
						req.put("action", new JSONString("getAsProlog"));
						controllerService.handleRequest(req.toString(), callback);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} });
		
		vertPanel.add(new HTML("Convert the policy to a first-order-logic representation in Prolog"));
		vertPanel.add(button);
		vertPanel.setCellHorizontalAlignment(button, HasHorizontalAlignment.ALIGN_CENTER);
		return vertPanel;
	}
}
