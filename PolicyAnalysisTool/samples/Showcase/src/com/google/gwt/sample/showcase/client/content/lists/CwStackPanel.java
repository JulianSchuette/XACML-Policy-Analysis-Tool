/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.sample.showcase.client.content.lists;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.sample.showcase.client.ContentWidget;
import com.google.gwt.sample.showcase.client.ShowcaseAnnotations.ShowcaseData;
import com.google.gwt.sample.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import com.google.gwt.sample.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeImages;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 */
@ShowcaseStyle({".gwt-DecoratedStackPanel", "html>body .gwt-DecoratedStackPanel",
    "* html .gwt-DecoratedStackPanel", ".cw-StackPanelHeader"})
public class CwStackPanel extends ContentWidget {
  /**
   * The constants used in this Content Widget.
   */
  @ShowcaseSource
  public static interface CwConstants extends Constants,
      ContentWidget.CwConstants {
    String[] cwStackPanelContacts();

    String[] cwStackPanelContactsEmails();

    String cwStackPanelContactsHeader();

    String cwStackPanelDescription();

    String[] cwStackPanelFilters();

    String cwStackPanelFiltersHeader();

    String[] cwStackPanelMailFolders();

    String cwStackPanelMailHeader();

    String cwStackPanelName();
  }

  /**
   * Specifies the images that will be bundled for this example.
   * 
   * We will override the leaf image used in the tree. Instead of using a blank
   * 16x16 image, we will use a blank 1x1 image so it does not take up any
   * space. Each TreeItem will use its own custom image.
   */
  @ShowcaseSource
  public interface Images extends TreeImages {
    AbstractImagePrototype contactsgroup();

    AbstractImagePrototype defaultContact();

    AbstractImagePrototype drafts();

    AbstractImagePrototype filtersgroup();

    AbstractImagePrototype inbox();

    AbstractImagePrototype mailgroup();

    AbstractImagePrototype sent();

    AbstractImagePrototype templates();

    AbstractImagePrototype trash();

    /**
     * Use noimage.png, which is a blank 1x1 image.
     */
    @Resource("noimage.png")
    AbstractImagePrototype treeLeaf();
  }

  /**
   * An instance of the constants.
   */
  @ShowcaseData
  private CwConstants constants;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwStackPanel(CwConstants constants) {
    super(constants);
    this.constants = constants;
  }

  @Override
  public String getDescription() {
    return constants.cwStackPanelDescription();
  }

  @Override
  public String getName() {
    return constants.cwStackPanelName();
  }

  @Override
  public boolean hasStyle() {
    return true;
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  public Widget onInitialize() {
    // Get the images
    Images images = (Images) GWT.create(Images.class);

    // Create a new stack panel
    DecoratedStackPanel stackPanel = new DecoratedStackPanel();
    stackPanel.setWidth("200px");

    // Add the Mail folders
    String mailHeader = getHeaderString(constants.cwStackPanelMailHeader(),
        images.mailgroup());
    stackPanel.add(createMailItem(images), mailHeader, true);

    // Add a list of filters
    String filtersHeader = getHeaderString(
        constants.cwStackPanelFiltersHeader(), images.filtersgroup());
    stackPanel.add(createFiltersItem(), filtersHeader, true);

    // Add a list of contacts
    String contactsHeader = getHeaderString(
        constants.cwStackPanelContactsHeader(), images.contactsgroup());
    stackPanel.add(createContactsItem(images), contactsHeader, true);

    // Return the stack panel
    stackPanel.ensureDebugId("cwStackPanel");
    return stackPanel;
  }

  /**
   * Create the list of Contacts.
   * 
   * @param images the {@link Images} used in the Contacts
   * @return the list of contacts
   */
  @ShowcaseSource
  private VerticalPanel createContactsItem(Images images) {
    // Create a popup to show the contact info when a contact is clicked
    HorizontalPanel contactPopupContainer = new HorizontalPanel();
    contactPopupContainer.setSpacing(5);
    contactPopupContainer.add(images.defaultContact().createImage());
    final HTML contactInfo = new HTML();
    contactPopupContainer.add(contactInfo);
    final PopupPanel contactPopup = new PopupPanel(true, false);
    contactPopup.setWidget(contactPopupContainer);

    // Create the list of contacts
    VerticalPanel contactsPanel = new VerticalPanel();
    contactsPanel.setSpacing(4);
    String[] contactNames = constants.cwStackPanelContacts();
    String[] contactEmails = constants.cwStackPanelContactsEmails();
    for (int i = 0; i < contactNames.length; i++) {
      final String contactName = contactNames[i];
      final String contactEmail = contactEmails[i];
      final HTML contactLink = new HTML("<a href=\"javascript:undefined;\">"
          + contactName + "</a>");
      contactsPanel.add(contactLink);

      // Open the contact info popup when the user clicks a contact
      contactLink.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          // Set the info about the contact
          contactInfo.setHTML(contactName + "<br><i>" + contactEmail + "</i>");

          // Show the popup of contact info
          int left = contactLink.getAbsoluteLeft() + 14;
          int top = contactLink.getAbsoluteTop() + 14;
          contactPopup.setPopupPosition(left, top);
          contactPopup.show();
        }
      });
    }
    return contactsPanel;
  }

  /**
   * Create the list of filters for the Filters item.
   * 
   * @return the list of filters
   */
  @ShowcaseSource
  private VerticalPanel createFiltersItem() {
    VerticalPanel filtersPanel = new VerticalPanel();
    filtersPanel.setSpacing(4);
    for (String filter : constants.cwStackPanelFilters()) {
      filtersPanel.add(new CheckBox(filter));
    }
    return filtersPanel;
  }

  /**
   * Create the {@link Tree} of Mail options.
   * 
   * @param images the {@link Images} used in the Mail options
   * @return the {@link Tree} of mail options
   */
  @ShowcaseSource
  private Tree createMailItem(Images images) {
    Tree mailPanel = new Tree(images);
    TreeItem mailPanelRoot = mailPanel.addItem("foo@example.com");
    String[] mailFolders = constants.cwStackPanelMailFolders();
    mailPanelRoot.addItem(images.inbox().getHTML() + " " + mailFolders[0]);
    mailPanelRoot.addItem(images.drafts().getHTML() + " " + mailFolders[1]);
    mailPanelRoot.addItem(images.templates().getHTML() + " " + mailFolders[2]);
    mailPanelRoot.addItem(images.sent().getHTML() + " " + mailFolders[3]);
    mailPanelRoot.addItem(images.trash().getHTML() + " " + mailFolders[4]);
    mailPanelRoot.setState(true);
    return mailPanel;
  }

  /**
   * Get a string representation of the header that includes an image and some
   * text.
   * 
   * @param text the header text
   * @param image the {@link AbstractImagePrototype} to add next to the header
   * @return the header as a string
   */
  @ShowcaseSource
  private String getHeaderString(String text, AbstractImagePrototype image) {
    // Add the image and text to a horizontal panel
    HorizontalPanel hPanel = new HorizontalPanel();
    hPanel.setSpacing(0);
    hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    hPanel.add(image.createImage());
    HTML headerText = new HTML(text);
    headerText.setStyleName("cw-StackPanelHeader");
    hPanel.add(headerText);

    // Return the HTML string for the panel
    return hPanel.getElement().getString();
  }
}
