package info.ajaxplorer.synchro.gui;
import info.ajaxplorer.client.http.AjxpAPI;
import info.ajaxplorer.client.http.RestRequest;
import info.ajaxplorer.client.http.RestStateHolder;
import info.ajaxplorer.client.model.Node;
import info.ajaxplorer.client.model.Server;
import info.ajaxplorer.synchro.Manager;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import com.cloudgarden.resource.SWTResourceManager;
import com.j256.ormlite.dao.Dao;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class JobEditor extends org.eclipse.swt.widgets.Composite {

	{
		//Register as a resource user - SWTResourceManager will
		//handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}
	
	private Label hostLabel;
	private Text tfHost;
	private Label labelLogin;
	private CTabFolder cTabFolder;
	private CTabItem cTabItem2;
	private Composite composite1;
	private CTabItem cTabItem1;
	private Button buttonFileChooser;
	private Text tfTarget;
	private Label labelTarget;
	private Text tfPassword;
	private Label labelPass;
	private Button radioDirection2;
	private Label localLabel;
	private Label remoteTitle;
	private Group group2;
	private Group group1;
	private Label Title;
	private Button saveButton;
	private Button buttonLoadRepositories;
	private Button radioSyncInterval2;
	private Combo comboRepository;
	private Label labelRepository;
	private Button radioSyncInterval3;
	private Button radioSyncInterval1;
	private Button radioDirection3;
	private Button radioDirection;
	private Button checkboxActive;
	private Composite composite2;
	private Text tfLogin;
	
	private HashMap<String, String> repoItems;
	private Node currentSynchroNode;
	

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
		
	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/	
	protected void checkSubclass() {
	}
	
	/**
	* Auto-generated method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	/*
	public static void showGUI() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		JobEditor inst = new JobEditor(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		if(size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}*/

	protected void saveConfig(){
		try {
			Manager.getInstance().updateSynchroNode(getFormData(), currentSynchroNode);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public JobEditor(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
		int baseNodeId = -1;
		try {
			Dao<Node, String> nodeDao = Manager.getInstance().getNodeDao();
			baseNodeId = nodeDao.queryForEq("resourceType", Node.NODE_TYPE_REPOSITORY).get(0).id;
			Node baseNode = Manager.getInstance().getSynchroNode(String.valueOf(baseNodeId));
			Server s = new Server(baseNode.getParent());
			HashMap<String, String> values = new HashMap<String, String>();
			values.put("HOST", s.getUrl());
			values.put("LOGIN", s.getUser());
			values.put("PASSWORD", s.getPassword());
			values.put("REPOSITORY_LABEL", baseNode.getLabel());
			values.put("REPOSITORY_ID", baseNode.getPropertyValue("repository_id"));
			values.put("TARGET", baseNode.getPropertyValue("target_folder"));
			this.loadFormData(values);
			currentSynchroNode = baseNode;
		} catch (Exception e) {
		}

	}
	
	public Map<String, String> getFormData() throws Exception{
		HashMap<String, String> values = new HashMap<String, String>();
		if(repoItems == null) throw new Exception("Please select a repository first!");
		
		values.put("HOST", tfHost.getText());		
		values.put("LOGIN", tfLogin.getText());		
		values.put("PASSWORD", tfPassword.getText());		
		values.put("REPOSITORY_LABEL", comboRepository.getText());
		values.put("REPOSITORY_ID", repoItems.get(comboRepository.getText()));
		values.put("TARGET", tfTarget.getText());		
		
		return values;
	}
	
	public void loadFormData(Map<String, String> values){
		tfHost.setText(values.get("HOST"));
		tfLogin.setText(values.get("LOGIN"));
		tfPassword.setText(values.get("PASSWORD"));
		comboRepository.setText(values.get("REPOSITORY_LABEL"));
		comboRepository.setEnabled(true);
		repoItems = new HashMap<String, String>();
		repoItems.put(values.get("REPOSITORY_LABEL"), values.get("REPOSITORY_ID"));
		tfTarget.setText(values.get("TARGET"));
	}

	private void loadRepositories(){
		
		if(tfHost.getText().equals("") || tfLogin.getText().equals("") || tfPassword.getText().equals("") ){
			return;
		}
		String host = tfHost.getText();
		String login = tfLogin.getText();
		String pass = tfPassword.getText();
		
		Server s;
		try {
			s = new Server("Test", host, login, pass, true, false);
			RestStateHolder.getInstance().setServer(s);
			AjxpAPI.getInstance().setServer(s);
			RestRequest rest = new RestRequest();
			Document doc = rest.getDocumentContent(AjxpAPI.getInstance().getGetXmlRegistryUri());
			Dao<Node, String> nodeDao = Manager.getInstance().getNodeDao();
			
			NodeList mainTag = doc.getElementsByTagName("repositories");
			if(mainTag.getLength() == 0){
				throw new Exception("No repositories found");
			}			
			final NodeList repos = mainTag.item(0).getChildNodes();
			repoItems = new HashMap<String, String>();
			if (repos!=null && repos.getLength() > 0){
				for (int i = 0; i < repos.getLength(); i++) {
					org.w3c.dom.Node xmlNode = repos.item(i);
					Node repository = new Node(Node.NODE_TYPE_REPOSITORY, "", null);
					repository.properties = nodeDao.getEmptyForeignCollection("properties");
					repository.initFromXmlNode(xmlNode);
					boolean excluded = false;
					for(int p =0;p<Manager.EXCLUDED_ACCESS_TYPES.length;p++){
						if(repository.getPropertyValue("access_type").equalsIgnoreCase(Manager.EXCLUDED_ACCESS_TYPES[p])){
							excluded = true; break;
						}
					}
					repoItems.put(repository.getLabel(), repository.getPropertyValue("repository_id"));
					if(excluded) {
						continue;
					}
				}
			}
			
			comboRepository.setEnabled(true);
			comboRepository.setItems(repoItems.keySet().toArray(new String[0]));
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void initGUI() {
		try {
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			this.setSize(401, 325);
			this.setBackground(SWTResourceManager.getColor(94, 124, 144));
			this.setBackgroundMode(1);
			{
				Title = new Label(this, SWT.NONE);
				FormData TitleLData = new FormData();
				TitleLData.left =  new FormAttachment(0, 1000, 18);
				TitleLData.top =  new FormAttachment(0, 1000, 20);
				TitleLData.width = 320;
				TitleLData.height = 26;
				TitleLData.right =  new FormAttachment(1000, 1000, -63);
				Title.setLayoutData(TitleLData);
				Title.setText("AjaXplorer Synchronizer");
				Title.setForeground(SWTResourceManager.getColor(255, 255, 255));
				Title.setBackground(SWTResourceManager.getColor(94, 124, 144));
				Title.setFont(SWTResourceManager.getFont("Arial", 18, 0, false, false));
			}
			{
				cTabFolder = new CTabFolder(this, SWT.NONE);
				{
					cTabItem1 = new CTabItem(cTabFolder, SWT.NONE);
					cTabItem1.setText("Connexion data");
					{
						composite1 = new Composite(cTabFolder, SWT.NONE);
						FormLayout composite1Layout = new FormLayout();
						composite1.setLayout(composite1Layout);
						cTabItem1.setControl(composite1);
						composite1.setBackground(SWTResourceManager.getColor(255, 255, 255));
						{
							localLabel = new Label(composite1, SWT.NONE);
							FormData localLabelLData = new FormData();
							localLabelLData.left =  new FormAttachment(0, 1000, 12);
							localLabelLData.top =  new FormAttachment(0, 1000, 174);
							localLabelLData.width = 349;
							localLabelLData.right =  new FormAttachment(1000, 1000, -12);
							localLabelLData.height = 22;
							localLabel.setLayoutData(localLabelLData);
							localLabel.setText("Local folder");
							localLabel.setFont(SWTResourceManager.getFont("Arial", 12, 1, false, false));
							localLabel.setForeground(SWTResourceManager.getColor(94, 124, 144));
						}
						{
							remoteTitle = new Label(composite1, SWT.NONE);
							FormData remoteTitleLData = new FormData();
							remoteTitleLData.left =  new FormAttachment(0, 1000, 12);
							remoteTitleLData.top =  new FormAttachment(0, 1000, 10);
							remoteTitleLData.width = 349;
							remoteTitleLData.right =  new FormAttachment(1000, 1000, -12);
							remoteTitleLData.height = 22;
							remoteTitle.setLayoutData(remoteTitleLData);
							remoteTitle.setText("Remote Server");
							remoteTitle.setFont(SWTResourceManager.getFont("Arial", 12, 1, false, false));
							remoteTitle.setForeground(SWTResourceManager.getColor(94, 124, 144));
						}
						{
							buttonLoadRepositories = new Button(composite1, SWT.PUSH | SWT.CENTER);
							FormData buttonLoadRepositoriesLData = new FormData(36, 24);
							buttonLoadRepositoriesLData.top =  new FormAttachment(0, 1000, 138);
							buttonLoadRepositoriesLData.width = 36;
							buttonLoadRepositoriesLData.height = 24;
							buttonLoadRepositoriesLData.left =  new FormAttachment(0, 1000, 298);
							buttonLoadRepositories.setLayoutData(buttonLoadRepositoriesLData);
							buttonLoadRepositories.setImage(SWTResourceManager.getImage("info/ajaxplorer/synchro/resources/images/reload.png"));
							buttonLoadRepositories.setSize(36, 24);
							buttonLoadRepositories.addListener(SWT.Selection, new Listener() {
								@Override
								public void handleEvent(Event arg0) {
									loadRepositories();
								}
							});
						}
						{
							FormData comboRepositoryLData = new FormData();
							comboRepositoryLData.left =  new FormAttachment(0, 1000, 111);
							comboRepositoryLData.top =  new FormAttachment(0, 1000, 139);
							comboRepositoryLData.width = 147;
							comboRepositoryLData.height = 23;
							comboRepositoryLData.right =  new FormAttachment(1000, 1000, -87);
							comboRepository = new Combo(composite1, SWT.NONE);
							comboRepository.setLayoutData(comboRepositoryLData);
							comboRepository.setToolTipText("Click on the load button to get the repositories list from the server");
							comboRepository.setFont(SWTResourceManager.getFont("Arial", 9, 0, false, false));
							comboRepository.setEnabled(false);
						}
						{
							labelRepository = new Label(composite1, SWT.NONE);
							FormData labelRepositoryLData = new FormData();
							labelRepositoryLData.left =  new FormAttachment(0, 1000, 30);
							labelRepositoryLData.top =  new FormAttachment(0, 1000, 142);
							labelRepositoryLData.width = 69;
							labelRepositoryLData.height = 19;
							labelRepository.setLayoutData(labelRepositoryLData);
							labelRepository.setText("Repository");
							labelRepository.setAlignment(SWT.RIGHT);
							labelRepository.setBackground(SWTResourceManager.getColor(255, 255, 255));
						}
						{
							hostLabel = new Label(composite1, SWT.NONE);
							FormData hostLabelLData = new FormData();
							hostLabelLData.left =  new FormAttachment(0, 1000, 30);
							hostLabelLData.top =  new FormAttachment(0, 1000, 41);
							hostLabelLData.width = 69;
							hostLabelLData.height = 19;
							hostLabel.setLayoutData(hostLabelLData);
							hostLabel.setText("Host URL");
							hostLabel.setAlignment(SWT.RIGHT);
							hostLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
						}
						{
							FormData hostTextLData = new FormData();
							hostTextLData.left =  new FormAttachment(0, 1000, 111);
							hostTextLData.top =  new FormAttachment(0, 1000, 40);
							hostTextLData.width = 211;
							tfHost = new Text(composite1, SWT.BORDER);
							tfHost.setLayoutData(hostTextLData);
							tfHost.setOrientation(SWT.HORIZONTAL);
							tfHost.setToolTipText("Full path to ajaxplorer, e.g http://domain.tld/ajaxplorer");
							tfHost.setFont(SWTResourceManager.getFont("Arial", 9, 0, false, false));
						}
						{
							labelLogin = new Label(composite1, SWT.NONE);
							FormData labelLoginLData = new FormData();
							labelLoginLData.left =  new FormAttachment(0, 1000, 30);
							labelLoginLData.top =  new FormAttachment(0, 1000, 73);
							labelLoginLData.width = 69;
							labelLoginLData.height = 19;
							labelLogin.setLayoutData(labelLoginLData);
							labelLogin.setText("Login");
							labelLogin.setAlignment(SWT.RIGHT);
							labelLogin.setBackground(SWTResourceManager.getColor(255, 255, 255));
						}
						{
							FormData tfLoginLData = new FormData();
							tfLoginLData.left =  new FormAttachment(0, 1000, 111);
							tfLoginLData.top =  new FormAttachment(0, 1000, 72);
							tfLoginLData.width = 211;
							tfLoginLData.height = 15;
							tfLogin = new Text(composite1, SWT.BORDER);
							tfLogin.setLayoutData(tfLoginLData);
							tfLogin.setToolTipText("User identifier");
							tfLogin.setFont(SWTResourceManager.getFont("Arial", 9, 0, false, false));
						}
						{
							labelPass = new Label(composite1, SWT.NONE);
							FormData labelPassLData = new FormData();
							labelPassLData.left =  new FormAttachment(0, 1000, 30);
							labelPassLData.top =  new FormAttachment(0, 1000, 106);
							labelPassLData.width = 69;
							labelPassLData.height = 19;
							labelPass.setLayoutData(labelPassLData);
							labelPass.setText("Password");
							labelPass.setAlignment(SWT.RIGHT);
							labelPass.setBackground(SWTResourceManager.getColor(255, 255, 255));
						}
						{
							FormData tfPasswordLData = new FormData();
							tfPasswordLData.left =  new FormAttachment(0, 1000, 111);
							tfPasswordLData.top =  new FormAttachment(0, 1000, 105);
							tfPasswordLData.width = 211;
							tfPasswordLData.height = 15;
							tfPassword = new Text(composite1, SWT.BORDER|SWT.PASSWORD);
							tfPassword.setLayoutData(tfPasswordLData);
							tfPassword.setToolTipText("User password");
							tfPassword.setFont(SWTResourceManager.getFont("Arial", 9, 0, false, false));
						}
						{
							labelTarget = new Label(composite1, SWT.NONE);
							FormData labelTargetLData = new FormData();
							labelTargetLData.left =  new FormAttachment(0, 1000, 30);
							labelTargetLData.top =  new FormAttachment(0, 1000, 202);
							labelTargetLData.width = 69;
							labelTargetLData.height = 19;
							labelTarget.setLayoutData(labelTargetLData);
							labelTarget.setText("Target Folder");
							labelTarget.setAlignment(SWT.RIGHT);
							labelTarget.setBackground(SWTResourceManager.getColor(255, 255, 255));
						}
						{
							FormData tfTargetLData = new FormData();
							tfTargetLData.left =  new FormAttachment(0, 1000, 111);
							tfTargetLData.top =  new FormAttachment(0, 1000, 200);
							tfTargetLData.width = 163;
							tfTargetLData.height = 15;
							tfTargetLData.right =  new FormAttachment(1000, 1000, -87);
							tfTarget = new Text(composite1, SWT.BORDER);
							tfTarget.setLayoutData(tfTargetLData);
							tfTarget.setEditable(false);
							tfTarget.setToolTipText("Choose a local folder for synchronization");
							tfTarget.setFont(SWTResourceManager.getFont("Arial", 9, 0, false, false));
						}
						{
							buttonFileChooser = new Button(composite1, SWT.PUSH | SWT.CENTER);
							FormData buttonFileChooserLData = new FormData();
							buttonFileChooserLData.top =  new FormAttachment(0, 1000, 198);
							buttonFileChooserLData.width = 36;
							buttonFileChooserLData.height = 24;
							buttonFileChooserLData.left =  new FormAttachment(0, 1000, 298);
							buttonFileChooser.setLayoutData(buttonFileChooserLData);
							buttonFileChooser.setImage(SWTResourceManager.getImage("info/ajaxplorer/synchro/resources/images/view_tree.png"));
							buttonFileChooser.addListener(SWT.Selection, new Listener() {
								
								@Override
								public void handleEvent(Event arg0) {									
									DirectoryDialog dialog = new DirectoryDialog(JobEditor.this.getShell());
									String res = dialog.open();
									if(res != null){
										tfTarget.setText(res);
									}
								}
							});
						}
					}

				}
				{
					cTabItem2 = new CTabItem(cTabFolder, SWT.NONE);
					cTabItem2.setText("Job Execution");
					{
						composite2 = new Composite(cTabFolder, SWT.NONE);
						FormLayout composite2Layout = new FormLayout();
						composite2.setLayout(composite2Layout);
						cTabItem2.setControl(composite2);
						composite2.setBackground(SWTResourceManager.getColor(255, 255, 255));
						{
							group2 = new Group(composite2, SWT.NONE);
							GridLayout group2Layout = new GridLayout();
							group2Layout.makeColumnsEqualWidth = true;
							group2.setLayout(group2Layout);
							FormData group2LData = new FormData();
							group2LData.width = 346;
							group2LData.left =  new FormAttachment(0, 1000, 12);
							group2LData.top =  new FormAttachment(0, 1000, 134);
							group2LData.height = 75;
							group2.setLayoutData(group2LData);
							group2.setText("Run every...");
							group2.setBackground(SWTResourceManager.getColor(255, 255, 255));
							{
								radioSyncInterval1 = new Button(group2, SWT.RADIO | SWT.LEFT);
								GridData radioSyncInterval1LData = new GridData();
								radioSyncInterval1LData.widthHint = 327;
								radioSyncInterval1LData.heightHint = 16;
								radioSyncInterval1.setLayoutData(radioSyncInterval1LData);
								radioSyncInterval1.setText("Minute");
								radioSyncInterval1.setBackground(SWTResourceManager.getColor(255, 255, 255));
							}
							{
								radioSyncInterval2 = new Button(group2, SWT.RADIO | SWT.LEFT);
								GridData radioSyncInterval2LData = new GridData();
								radioSyncInterval2LData.widthHint = 327;
								radioSyncInterval2LData.heightHint = 16;
								radioSyncInterval2.setLayoutData(radioSyncInterval2LData);
								radioSyncInterval2.setText("Hour");
								radioSyncInterval2.setBackground(SWTResourceManager.getColor(255,255,255));
								radioSyncInterval2.setSelection(true);
							}
							{
								radioSyncInterval3 = new Button(group2, SWT.RADIO | SWT.LEFT);
								GridData radioSyncInterval3LData = new GridData();
								radioSyncInterval3LData.widthHint = 327;
								radioSyncInterval3LData.heightHint = 16;
								radioSyncInterval3.setLayoutData(radioSyncInterval3LData);
								radioSyncInterval3.setText("Day");
								radioSyncInterval3.setBackground(SWTResourceManager.getColor(255,255,255));
							}
						}
						{
							group1 = new Group(composite2, SWT.NONE);
							GridLayout group1Layout = new GridLayout();
							group1Layout.makeColumnsEqualWidth = true;
							group1.setLayout(group1Layout);
							FormData group1LData = new FormData();
							group1LData.width = 346;
							group1LData.left =  new FormAttachment(0, 1000, 12);
							group1LData.top =  new FormAttachment(0, 1000, 34);
							group1LData.height = 71;
							group1.setLayoutData(group1LData);
							group1.setText("Synchronisation Direction");
							group1.setBackground(SWTResourceManager.getColor(255, 255, 255));
							{
								radioDirection = new Button(group1, SWT.RADIO | SWT.LEFT);
								GridData radioDirectionLData = new GridData();
								radioDirectionLData.widthHint = 327;
								radioDirectionLData.heightHint = 16;
								radioDirection.setLayoutData(radioDirectionLData);
								radioDirection.setText("Bidirectionnal");
								radioDirection.setBackground(SWTResourceManager.getColor(255,255,255));
								radioDirection.setSelection(true);
							}
							{
								radioDirection2 = new Button(group1, SWT.RADIO | SWT.LEFT);
								GridData radioDirection2LData = new GridData();
								radioDirection2LData.widthHint = 327;
								radioDirection2LData.heightHint = 16;
								radioDirection2.setLayoutData(radioDirection2LData);
								radioDirection2.setText("Download only");
								radioDirection2.setBackground(SWTResourceManager.getColor(255, 255, 255));
							}
							{
								radioDirection3 = new Button(group1, SWT.RADIO | SWT.LEFT);
								GridData radioDirection3LData = new GridData();
								radioDirection3LData.widthHint = 327;
								radioDirection3LData.heightHint = 16;
								radioDirection3.setLayoutData(radioDirection3LData);
								radioDirection3.setText("Upload Only");
								radioDirection3.setBackground(SWTResourceManager.getColor(255,255,255));
							}
						}
						{
							checkboxActive = new Button(composite2, SWT.CHECK | SWT.LEFT);
							FormData checkboxActiveLData = new FormData();
							checkboxActiveLData.left =  new FormAttachment(0, 1000, 20);
							checkboxActiveLData.top =  new FormAttachment(0, 1000, 12);
							checkboxActiveLData.width = 273;
							checkboxActiveLData.height = 16;
							checkboxActive.setLayoutData(checkboxActiveLData);
							checkboxActive.setText("This job is active");
							checkboxActive.setBackground(SWTResourceManager.getColor(255, 255, 255));
							checkboxActive.setSelection(true);
						}
					}
				}
				FormData cTabFolderLData = new FormData();
				cTabFolderLData.left =  new FormAttachment(0, 1000, 12);
				cTabFolderLData.top =  new FormAttachment(0, 1000, 57);
				cTabFolderLData.width = 373;
				cTabFolderLData.height = 234;
				cTabFolderLData.right =  new FormAttachment(1000, 1000, -12);
				cTabFolderLData.bottom =  new FormAttachment(1000, 1000, -12);
				cTabFolder.setLayoutData(cTabFolderLData);
				cTabFolder.setSelection(0);
				cTabFolder.setBackground(SWTResourceManager.getColor(255, 255, 255));
				cTabFolder.setSimple(false);
			}
			{
				saveButton = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData saveButtonLData = new FormData();
				saveButtonLData.left =  new FormAttachment(0, 1000, 350);
				saveButtonLData.top =  new FormAttachment(0, 1000, 17);
				saveButtonLData.right =  new FormAttachment(1000, 1000, -17);
				saveButtonLData.bottom =  new FormAttachment(1000, 1000, -274);
				saveButtonLData.width = 34;
				saveButtonLData.height = 34;
				saveButton.setLayoutData(saveButtonLData);
				saveButton.setImage(SWTResourceManager.getImage("info/ajaxplorer/synchro/resources/images/filesave.png"));
				saveButton.setAlignment(SWT.CENTER);
				saveButton.setBackground(SWTResourceManager.getColor(255, 255, 255));
				saveButton.addListener(SWT.PUSH, new Listener() {
					
					@Override
					public void handleEvent(Event arg0) {
						saveConfig();						
					}
				});
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}