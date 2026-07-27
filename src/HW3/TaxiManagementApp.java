package HW3;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import javafx.scene.image.Image;

public class TaxiManagementApp extends Application {

	private Stage primaryStage;
	private systemDataBase ourSystem = new systemDataBase();
	private Scene loginScene, mainManagerScene, managerScene, subscriberScene;
	private Manager currentManager;
	private Subscription currentSubscriber;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		initializeData();
		createLoginScene();
		primaryStage.setTitle("Taxi Management System");
		primaryStage.setScene(loginScene);
		primaryStage.show();
	}

	private void initializeData() {
		// Initialize with sample data
		ourSystem.addManager(
				new MainManager("9001", "Maria", "Fahoum", "0500000000", "Central Perk", "system", "12345"));
		ourSystem.addManager(new Manager("M1", "Mike", "Hannigan", "0500000001", "NYC"));
		ourSystem.addManager(new Manager("M2", "Janice", "Hosenstein", "0500000002", "Brooklyn"));
		ourSystem.addManager(new Manager("M3", "Estelle", "Leonard", "0500000003", "Queens"));
		ourSystem.addManager(new Manager("M4", "Jack", "Geller", "0500000004", "Manhattan"));

		ourSystem.addSubscription(new Subscription("S1", "Rachel", "Green", "0501111111", "Soho"));
		ourSystem.addSubscription(new Subscription("S2", "Monica", "Geller", "0501111112", "West Village"));
		ourSystem.addSubscription(new Subscription("S3", "Phoebe", "Buffay", "0501111113", "Upper East"));
		ourSystem.addSubscription(new Subscription("S4", "Joey", "Tribbiani", "0501111114", "Queens"));
		ourSystem.addSubscription(new Subscription("S5", "Ross", "Geller", "0501111115", "Museum District"));

		Taxi t1 = new Taxi("T1", true, 35);
		Taxi t2 = new Taxi("T2", true, 40);
		Taxi t3 = new ExpressTaxi("T3", true, 45, true, 10);
		Taxi t4 = new ExpressTaxi("T4", true, 50, true, 12);
		Taxi t5 = new IntercityTaxi("T5", true, 60, 
				new ArrayList<>(Arrays.asList("Tel Aviv", "Haifa")), 15, 3);

		ourSystem.addTaxi(t1);
		ourSystem.addTaxi(t2);
		ourSystem.addTaxi(t3);
		ourSystem.addTaxi(t4);
		ourSystem.addTaxi(t5);

		ourSystem.addStation(new Station("Haifa", new ArrayList<>(Arrays.asList(t1, t2))));
		ourSystem.addStation(new Station("Tel Aviv", new ArrayList<>(Arrays.asList(t3, t4))));
	}

	private void createLoginScene() {
		VBox layout = new VBox(20);
		layout.setPadding(new Insets(50));
		layout.setAlignment(Pos.CENTER);

		Label titleLabel = new Label("Taxi Management System");
		titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2e86ab;");

		ComboBox<String> userTypeCombo = new ComboBox<>();
		userTypeCombo.getItems().addAll("Main Manager", "Manager", "Subscriber");
		userTypeCombo.setPromptText("Select user type");
		userTypeCombo.setStyle("-fx-font-size: 14px;");

		TextField usernameField = new TextField();
		usernameField.setPromptText("Username/ID");
		usernameField.setStyle("-fx-font-size: 14px;");

		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Password (Main Manager only)");
		passwordField.setStyle("-fx-font-size: 14px;");

		Button loginButton = new Button("Login");
		loginButton.setStyle("-fx-background-color: #2e86ab; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");

		TextArea messageArea = new TextArea();
		messageArea.setEditable(false);
		messageArea.setPrefRowCount(3);
		messageArea.setStyle("-fx-font-size: 12px;");

		loginButton.setOnAction(e -> {
			String userType = userTypeCombo.getValue();
			String username = usernameField.getText().trim();
			String password = passwordField.getText().trim();

			if (userType == null || username.isEmpty()) {
				messageArea.setText("Please select user type and enter username/ID");
				return;
			}

			switch (userType) {
			case "Main Manager":
				loginMainManager(username, password, messageArea);
				break;
			case "Manager":
				loginManager(username, messageArea);
				break;
			case "Subscriber":
				loginSubscriber(username, messageArea);
				break;
			}
		});

		layout.getChildren().addAll(titleLabel, userTypeCombo, usernameField, passwordField, 
				loginButton, messageArea);

		loginScene = new Scene(layout, 500, 500);
		
		ImageView logoImageView = null;
		try {
			// נתיב הקובץ הוא יחסי ל-Classpath (שורש תיקיית resources)
			InputStream logoStream = getClass().getResourceAsStream("/logo.png");
			if (logoStream != null) {
				Image logoImage = new Image(logoStream);
				logoImageView = new ImageView(logoImage);
				logoImageView.setFitHeight(100);
				logoImageView.setPreserveRatio(true);
			} else {
				System.err.println("קובץ לוגו לא נמצא בנתיב המשאבים (classpath)");
			}
		} catch (Exception e) {
			System.err.println("אירעה שגיאה בטעינת קובץ הלוגו: " + e.getMessage());
		}

		if (logoImageView != null) {
			layout.getChildren().add(0, logoImageView); // הוסף את הלוגו במיקום הראשון
		}
		
        // טעינת קובץ ה-CSS וחיבורו לסצנה
        String cssPath = "/style.css";
        String cssUrl = getClass().getResource(cssPath).toExternalForm();
        loginScene.getStylesheets().add(cssUrl);

        // הוספת Class עבור ה-VBox כדי שנוכל לעצב אותו ב-CSS
        layout.getStyleClass().add("root");
	}

	private void loginMainManager(String username, String password, TextArea messageArea) {
		if (username.isEmpty() || password.isEmpty()) {
			messageArea.setText("Main Manager requires both username and password");
			return;
		}

		for (Manager m : ourSystem.getManagers()) {
			if (m instanceof MainManager) {
				MainManager mm = (MainManager) m;
				if (mm.getUserName().equals(username) && mm.getPassword().equals(password)) {
					messageArea.setText("Login successful! Welcome " + mm.getFirstName());
					createMainManagerScene();
					primaryStage.setScene(mainManagerScene);
					return;
				}
			}
		}
		messageArea.setText("Invalid credentials for Main Manager");
	}

	private void loginManager(String managerId, TextArea messageArea) {
		for (Manager m : ourSystem.getManagers()) {
			if (!(m instanceof MainManager) && m.getId().equals(managerId)) {
				currentManager = m;
				messageArea.setText("Login successful! Welcome Manager " + m.getFirstName());
				createManagerScene();
				primaryStage.setScene(managerScene);
				return;
			}
		}
		messageArea.setText("Manager ID not found");
	}

	private void loginSubscriber(String subscriberId, TextArea messageArea) {
		for (Subscription s : ourSystem.getSubscriptions()) {
			if (s.getSubCode().equals(subscriberId)) {
				currentSubscriber = s;
				messageArea.setText("Login successful! Welcome " + s.getFirstName());
				createSubscriberScene();
				primaryStage.setScene(subscriberScene);
				return;
			}
		}
		messageArea.setText("Subscriber ID not found");
	}

	private void createMainManagerScene() {
		BorderPane layout = new BorderPane();
		layout.setStyle("-fx-background-color: #f5f5f5;");

		
		// הוספת לוגו מהתיקייה resources
				ImageView logoImageView = null;
				try {
					Image logoImage = new Image(new FileInputStream("resources/logo.png"));
					logoImageView = new ImageView(logoImage);
					logoImageView.setFitHeight(100); // קביעת גודל
					logoImageView.setPreserveRatio(true); // שמירה על יחס רוחב-גובה
				} catch (FileNotFoundException e) {
					System.err.println("קובץ לוגו לא נמצא: " + e.getMessage());
				}

				if (logoImageView != null) {
					layout.getChildren().add(logoImageView);
				}
				
				
		// Title
		Label titleLabel = new Label("Main Manager Dashboard");
		titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2e86ab;");

		VBox titleBox = new VBox(titleLabel);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setPadding(new Insets(20));
		layout.setTop(titleBox);

		// Buttons
		GridPane buttonGrid = new GridPane();



		buttonGrid.setHgap(10);
		buttonGrid.setVgap(10);
		buttonGrid.setPadding(new Insets(20));
		buttonGrid.setAlignment(Pos.CENTER);

		String buttonStyle = "-fx-background-color: #2e86ab; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 15; -fx-min-width: 200px;";

		Button showSubscribersBtn = new Button("Show Subscribers");
		Button showManagersBtn = new Button("Show Managers");
		Button showTaxisBtn = new Button("Show Taxis");
		Button addSubscriberBtn = new Button("Add Subscriber");
		Button addManagerBtn = new Button("Add Manager");
		Button addTaxiBtn = new Button("Add Taxi");
		Button assignTaxiBtn = new Button("Assign Taxi to Manager");
		Button addStationBtn = new Button("Add Station");
		Button loadManagersBtn = new Button("Load Managers from File");
		Button loadSubscribersBtn = new Button("Load Subscribers from File");
		Button saveManagersBtn = new Button("Save Managers to File");
		Button saveSubscribersBtn = new Button("Save Subscribers to File");
		Button saveOrdersBtn = new Button("Save Orders to File");
		Button saveTaxisBtn = new Button("Save Taxis to File");
		Button backBtn = new Button("Back to Login");

		Button[] buttons = {showSubscribersBtn, showManagersBtn, showTaxisBtn, addSubscriberBtn,
				addManagerBtn, addTaxiBtn, assignTaxiBtn, addStationBtn,
				loadManagersBtn, loadSubscribersBtn, saveManagersBtn, saveSubscribersBtn,
				saveOrdersBtn, saveTaxisBtn, backBtn};

		for (Button btn : buttons) {
			btn.setStyle(buttonStyle);
		}

		buttonGrid.add(showSubscribersBtn, 0, 0);
		buttonGrid.add(showManagersBtn, 1, 0);
		buttonGrid.add(showTaxisBtn, 2, 0);
		buttonGrid.add(addSubscriberBtn, 0, 1);
		buttonGrid.add(addManagerBtn, 1, 1);
		buttonGrid.add(addTaxiBtn, 2, 1);
		buttonGrid.add(assignTaxiBtn, 0, 2);
		buttonGrid.add(addStationBtn, 1, 2);
		buttonGrid.add(loadManagersBtn, 0, 3);
		buttonGrid.add(loadSubscribersBtn, 1, 3);
		buttonGrid.add(saveManagersBtn, 0, 4);
		buttonGrid.add(saveSubscribersBtn, 1, 4);
		buttonGrid.add(saveOrdersBtn, 2, 4);
		buttonGrid.add(saveTaxisBtn, 0, 5);
		buttonGrid.add(backBtn, 1, 5);

		layout.setCenter(buttonGrid);

		// Output area
		TextArea outputArea = new TextArea();
		outputArea.setEditable(false);
		outputArea.setPrefRowCount(10);
		outputArea.setStyle("-fx-font-family: monospaced; -fx-font-size: 12px;");

		ScrollPane scrollPane = new ScrollPane(outputArea);
		scrollPane.setPrefHeight(200);
		layout.setBottom(scrollPane);

		// Button actions
		showSubscribersBtn.setOnAction(e -> showSubscribers(outputArea));
		showManagersBtn.setOnAction(e -> showManagers(outputArea));
		showTaxisBtn.setOnAction(e -> showTaxis(outputArea));
		addSubscriberBtn.setOnAction(e -> showAddSubscriberForm(outputArea));
		addManagerBtn.setOnAction(e -> showAddManagerForm(outputArea));
		addTaxiBtn.setOnAction(e -> showAddTaxiForm(outputArea));
		assignTaxiBtn.setOnAction(e -> showAssignTaxiForm(outputArea));
		addStationBtn.setOnAction(e -> showAddStationForm(outputArea));
		loadManagersBtn.setOnAction(e -> loadManagersFromFile(outputArea));
		loadSubscribersBtn.setOnAction(e -> loadSubscribersFromFile(outputArea));
		saveManagersBtn.setOnAction(e -> saveManagersToFile(outputArea));
		saveSubscribersBtn.setOnAction(e -> saveSubscribersToFile(outputArea));
		saveOrdersBtn.setOnAction(e -> saveOrdersToFile(outputArea));
		saveTaxisBtn.setOnAction(e -> saveTaxisToFile(outputArea));
		backBtn.setOnAction(e -> primaryStage.setScene(loginScene));

		mainManagerScene = new Scene(layout, 900, 700);
	}

	private void createManagerScene() {
		BorderPane layout = new BorderPane();
		layout.setStyle("-fx-background-color: #f5f5f5;");

		Label titleLabel = new Label("Manager Dashboard - " + currentManager.getFirstName());
		titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2e86ab;");

		VBox titleBox = new VBox(titleLabel);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setPadding(new Insets(20));
		layout.setTop(titleBox);

		VBox buttonBox = new VBox(20);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setPadding(new Insets(50));

		String buttonStyle = "-fx-background-color: #2e86ab; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 15 30; -fx-min-width: 250px;";

		Button addOrderBtn = new Button("Add Order");
		Button changeOrderTaxiBtn = new Button("Change Taxi in Order");
		Button backBtn = new Button("Back to Login");

		addOrderBtn.setStyle(buttonStyle);
		changeOrderTaxiBtn.setStyle(buttonStyle);
		backBtn.setStyle(buttonStyle);

		buttonBox.getChildren().addAll(addOrderBtn, changeOrderTaxiBtn, backBtn);
		layout.setCenter(buttonBox);

		TextArea outputArea = new TextArea();
		outputArea.setEditable(false);
		outputArea.setPrefRowCount(8);
		layout.setBottom(new ScrollPane(outputArea));

		addOrderBtn.setOnAction(e -> showAddOrderForm(outputArea));
		changeOrderTaxiBtn.setOnAction(e -> showChangeOrderTaxiForm(outputArea));
		backBtn.setOnAction(e -> primaryStage.setScene(loginScene));

		managerScene = new Scene(layout, 800, 600);
	}

	private void createSubscriberScene() {
		BorderPane layout = new BorderPane();
		layout.setStyle("-fx-background-color: #f5f5f5;");

		Label titleLabel = new Label("Subscriber Dashboard - " + currentSubscriber.getFirstName());
		titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2e86ab;");

		VBox titleBox = new VBox(titleLabel);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setPadding(new Insets(20));
		layout.setTop(titleBox);

		VBox buttonBox = new VBox(20);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setPadding(new Insets(50));

		String buttonStyle = "-fx-background-color: #2e86ab; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 15 30; -fx-min-width: 250px;";

		Button showOrdersBtn = new Button("Show My Orders");
		Button updateDetailsBtn = new Button("Update Personal Details");
		Button showTaxiDetailsBtn = new Button("Show Taxi Details");
		Button backBtn = new Button("Back to Login");

		showOrdersBtn.setStyle(buttonStyle);
		updateDetailsBtn.setStyle(buttonStyle);
		showTaxiDetailsBtn.setStyle(buttonStyle);
		backBtn.setStyle(buttonStyle);

		buttonBox.getChildren().addAll(showOrdersBtn, updateDetailsBtn, showTaxiDetailsBtn, backBtn);
		layout.setCenter(buttonBox);

		TextArea outputArea = new TextArea();
		outputArea.setEditable(false);
		outputArea.setPrefRowCount(10);
		layout.setBottom(new ScrollPane(outputArea));

		showOrdersBtn.setOnAction(e -> showSubscriberOrders(outputArea));
		updateDetailsBtn.setOnAction(e -> showUpdateDetailsForm(outputArea));
		showTaxiDetailsBtn.setOnAction(e -> showTaxiDetailsForm(outputArea));
		backBtn.setOnAction(e -> primaryStage.setScene(loginScene));

		subscriberScene = new Scene(layout, 800, 600);
	}

	private void showSubscribers(TextArea outputArea) {
		StringBuilder sb = new StringBuilder();
		sb.append("=== SUBSCRIBERS (Sorted by Last Name) ===\n\n");

		List<Subscription> sortedSubs = ourSystem.getSubscriptions().stream()
				.sorted((s1, s2) -> s1.getLastName().compareToIgnoreCase(s2.getLastName()))
				.collect(Collectors.toList());

		for (Subscription sub : sortedSubs) {
			sb.append(sub.toString()).append("\n");
		}

		outputArea.setText(sb.toString());
	}

	private void showManagers(TextArea outputArea) {
		StringBuilder sb = new StringBuilder();
		sb.append("=== MANAGERS TABLE (Sorted by First Name) ===\n\n");
		sb.append(String.format("%-8s %-15s %-15s %-15s %-20s\n", 
				"ID", "First Name", "Last Name", "Phone", "Address"));
		sb.append("-".repeat(80)).append("\n");

		List<Manager> sortedManagers = ourSystem.getManagers().stream()
				.sorted((m1, m2) -> m1.getFirstName().compareToIgnoreCase(m2.getFirstName()))
				.collect(Collectors.toList());

		for (Manager manager : sortedManagers) {
			sb.append(String.format("%-8s %-15s %-15s %-15s %-20s\n",
					manager.getId(),
					manager.getFirstName(),
					manager.getLastName(),
					manager.getPhone(),
					manager.getAddress()));
		}

		outputArea.setText(sb.toString());
	}

	private void showTaxis(TextArea outputArea) {
		StringBuilder sb = new StringBuilder();
		sb.append("=== TAXIS TABLE (Sorted by Code) ===\n\n");
		sb.append(String.format("%-8s %-12s %-12s %-15s %-30s\n", 
				"Code", "Available", "Min Price", "Type", "Extra Details"));
		sb.append("-".repeat(90)).append("\n");

		List<Taxi> sortedTaxis = ourSystem.getTaxis().stream()
				.sorted((t1, t2) -> t1.getTaxiCode().compareToIgnoreCase(t2.getTaxiCode()))
				.collect(Collectors.toList());

		for (Taxi taxi : sortedTaxis) {
			String type = "Regular";
			String extraDetails = "";

			if (taxi instanceof IntercityTaxi) {
				type = "Intercity";
				IntercityTaxi it = (IntercityTaxi) taxi;
				extraDetails = "Cities: " + it.getCities() + ", Hours: " + it.getMaxHours();
			} else if (taxi instanceof ExpressTaxi) {
				type = "Express";
				ExpressTaxi et = (ExpressTaxi) taxi;
				extraDetails = "City: " + et.isCityTaxi() + ", Extra: " + et.getExtraPrice();
			}

			sb.append(String.format("%-8s %-12s %-12.2f %-15s %-30s\n",
					taxi.getTaxiCode(),
					taxi.isAvailable() ? "Yes" : "No",
							taxi.getMinPrice(),
							type,
							extraDetails));
		}

		outputArea.setText(sb.toString());
	}

	// File operations
	private void loadManagersFromFile(TextArea outputArea) {
		try {
			File file = new File("SystemManagers.txt");
			if (!file.exists()) {
				outputArea.setText("File SystemManagers.txt not found");
				return;
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			int loaded = 0;
			Set<String> existingIds = new HashSet<>();

			// Get existing manager IDs
			for (Manager m : ourSystem.getManagers()) {
				existingIds.add(m.getId());
			}

			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(" ");
				if (parts.length < 5) {
					outputArea.setText("Invalid file format - line has insufficient data: " + line);
					reader.close();
					return;
				}

				String id = parts[0];
				if (existingIds.contains(id)) {
					outputArea.setText("Duplicate manager ID found in file: " + id);
					reader.close();
					return;
				}

				String firstName = parts[1];
				String lastName = parts[2];
				String phone = parts[3];
				String address = parts[4];

				Manager manager = new Manager(id, firstName, lastName, phone, address);
				if (ourSystem.addManager(manager)) {
					existingIds.add(id);
					loaded++;
				}
			}

			reader.close();
			outputArea.setText("Successfully loaded " + loaded + " managers from file");

		} catch (IOException e) {
			outputArea.setText("Error reading file: " + e.getMessage());
		}
	}

	private void loadSubscribersFromFile(TextArea outputArea) {
		try {
			File file = new File("members.txt");
			if (!file.exists()) {
				outputArea.setText("File members.txt not found");
				return;
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			int loaded = 0;
			Set<String> existingCodes = new HashSet<>();

			// Get existing subscriber codes
			for (Subscription s : ourSystem.getSubscriptions()) {
				existingCodes.add(s.getSubCode());
			}

			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(" ");
				if (parts.length < 5) {
					outputArea.setText("Invalid file format - line has insufficient data: " + line);
					reader.close();
					return;
				}

				String code = parts[0];
				if (existingCodes.contains(code)) {
					outputArea.setText("Duplicate subscriber code found in file: " + code);
					reader.close();
					return;
				}

				String firstName = parts[1];
				String lastName = parts[2];
				String phone = parts[3];
				String address = parts[4];

				Subscription subscription = new Subscription(code, firstName, lastName, phone, address);
				if (ourSystem.addSubscription(subscription)) {
					existingCodes.add(code);
					loaded++;
				}
			}

			reader.close();
			outputArea.setText("Successfully loaded " + loaded + " subscribers from file");

		} catch (IOException e) {
			outputArea.setText("Error reading file: " + e.getMessage());
		}
	}

	private void saveManagersToFile(TextArea outputArea) {
		try {
			FileWriter writer = new FileWriter("SystemManagers.txt");

			List<Manager> sortedManagers = ourSystem.getManagers().stream()
					.filter(m -> !(m instanceof MainManager))
					.sorted((m1, m2) -> m1.getId().compareToIgnoreCase(m2.getId()))
					.collect(Collectors.toList());

			for (Manager manager : sortedManagers) {
				writer.write(manager.getId() + " " + manager.getFirstName() + " " + 
						manager.getLastName() + " " + manager.getPhone() + " " + 
						manager.getAddress() + "\n");
			}

			writer.close();
			outputArea.setText("Successfully saved " + sortedManagers.size() + " managers to file");

		} catch (IOException e) {
			outputArea.setText("Error writing file: " + e.getMessage());
		}
	}

	private void saveSubscribersToFile(TextArea outputArea) {
		try {
			FileWriter writer = new FileWriter("members.txt");

			List<Subscription> sortedSubs = ourSystem.getSubscriptions().stream()
					.sorted((s1, s2) -> s1.getLastName().compareToIgnoreCase(s2.getLastName()))
					.collect(Collectors.toList());

			for (Subscription sub : sortedSubs) {
				writer.write(sub.getSubCode() + " " + sub.getFirstName() + " " + 
						sub.getLastName() + " " + sub.getPhone() + " " + 
						sub.getAddress() + "\n");
			}

			writer.close();
			outputArea.setText("Successfully saved " + sortedSubs.size() + " subscribers to file");

		} catch (IOException e) {
			outputArea.setText("Error writing file: " + e.getMessage());
		}
	}

	private void saveOrdersToFile(TextArea outputArea) {
		try {
			FileWriter writer = new FileWriter("orders.txt");

			List<Order> sortedOrders = ourSystem.getOrders().stream()
					.sorted((o1, o2) -> o1.getOrderNum().compareToIgnoreCase(o2.getOrderNum()))
					.collect(Collectors.toList());

			for (Order order : sortedOrders) {
				writer.write(order.getOrderNum() + " " + order.getManagerId() + " " + 
						order.getDay() + " " + order.getMonth() + " " + order.getHour() + " " +
						order.getSubCode() + " " + order.getTaxi().getTaxiCode() + " " + 
						order.getOrderPrice() + "\n");
			}

			writer.close();
			outputArea.setText("Successfully saved " + sortedOrders.size() + " orders to file");

		} catch (IOException e) {
			outputArea.setText("Error writing file: " + e.getMessage());
		}
	}

	private void saveTaxisToFile(TextArea outputArea) {
		try {
			FileWriter writer = new FileWriter("Taxi.txt");

			List<Taxi> sortedTaxis = ourSystem.getTaxis().stream()
					.sorted((t1, t2) -> t1.getTaxiCode().compareToIgnoreCase(t2.getTaxiCode()))
					.collect(Collectors.toList());

			for (Taxi taxi : sortedTaxis) {
				writer.write("Taxi Code: " + taxi.getTaxiCode() + 
						", Available: " + taxi.isAvailable() + 
						", Min Price: " + taxi.getMinPrice());

				if (taxi instanceof IntercityTaxi) {
					IntercityTaxi it = (IntercityTaxi) taxi;
					writer.write(", Type: Intercity, Cities: " + it.getCities() + 
							", Max Hours: " + it.getMaxHours());
				} else if (taxi instanceof ExpressTaxi) {
					ExpressTaxi et = (ExpressTaxi) taxi;
					writer.write(", Type: Express, City Taxi: " + et.isCityTaxi() + 
							", Extra Price: " + et.getExtraPrice());
				} else {
					writer.write(", Type: Regular");
				}

				writer.write("\nResponsible Managers: ");
				boolean first = true;
				for (Manager manager : ourSystem.getManagers()) {
					if (manager.getTaxis().contains(taxi)) {
						if (!first) writer.write(", ");
						writer.write(manager.getId() + " (" + manager.getFirstName() + ")");
						first = false;
					}
				}
				writer.write("\n\n");
			}

			writer.close();
			outputArea.setText("Successfully saved " + sortedTaxis.size() + " taxis to file");

		} catch (IOException e) {
			outputArea.setText("Error writing file: " + e.getMessage());
		}
	}

	// Form methods will be implemented in the next parts...

	private void showAddSubscriberForm(TextArea outputArea) {
		Stage formStage = new Stage();
		formStage.setTitle("Add Subscriber");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		TextField idField = new TextField();
		TextField firstNameField = new TextField();
		TextField lastNameField = new TextField();
		TextField phoneField = new TextField();
		TextField addressField = new TextField();

		grid.add(new Label("ID:"), 0, 0);
		grid.add(idField, 1, 0);
		grid.add(new Label("First Name:"), 0, 1);
		grid.add(firstNameField, 1, 1);
		grid.add(new Label("Last Name:"), 0, 2);
		grid.add(lastNameField, 1, 2);
		grid.add(new Label("Phone:"), 0, 3);
		grid.add(phoneField, 1, 3);
		grid.add(new Label("Address:"), 0, 4);
		grid.add(addressField, 1, 4);

		Button addButton = new Button("Add Subscriber");
		Button cancelButton = new Button("Cancel");

		HBox buttonBox = new HBox(10, addButton, cancelButton);
		buttonBox.setAlignment(Pos.CENTER);
		grid.add(buttonBox, 0, 5, 2, 1);

		addButton.setOnAction(e -> {
			if (validateFields(idField, firstNameField, lastNameField, phoneField, addressField)) {
				Subscription newSub = new Subscription(idField.getText().trim(),
						firstNameField.getText().trim(), lastNameField.getText().trim(),
						phoneField.getText().trim(), addressField.getText().trim());

				if (ourSystem.addSubscription(newSub)) {
					outputArea.setText("Subscriber added successfully: " + newSub.getFirstName() + " " + newSub.getLastName());
					formStage.close();
				} else {
					outputArea.setText("Failed to add subscriber - ID may already exist");
				}
			} else {
				outputArea.setText("Please fill all fields");
			}
		});

		cancelButton.setOnAction(e -> formStage.close());

		Scene scene = new Scene(grid, 300, 200);
		formStage.setScene(scene);
		formStage.show();
	}

	private void showAddManagerForm(TextArea outputArea) {
		Stage formStage = new Stage();
		formStage.setTitle("Add Manager");

		VBox layout = new VBox(10);
		layout.setPadding(new Insets(20));

		ComboBox<String> typeCombo = new ComboBox<>();
		typeCombo.getItems().addAll("Regular Manager", "Main Manager");
		typeCombo.setPromptText("Select manager type");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField idField = new TextField();
		TextField firstNameField = new TextField();
		TextField lastNameField = new TextField();
		TextField phoneField = new TextField();
		TextField addressField = new TextField();
		TextField usernameField = new TextField();
		PasswordField passwordField = new PasswordField();

		grid.add(new Label("ID:"), 0, 0);
		grid.add(idField, 1, 0);
		grid.add(new Label("First Name:"), 0, 1);
		grid.add(firstNameField, 1, 1);
		grid.add(new Label("Last Name:"), 0, 2);
		grid.add(lastNameField, 1, 2);
		grid.add(new Label("Phone:"), 0, 3);
		grid.add(phoneField, 1, 3);
		grid.add(new Label("Address:"), 0, 4);
		grid.add(addressField, 1, 4);

		VBox additionalFields = new VBox(10);
		GridPane mainManagerGrid = new GridPane();
		mainManagerGrid.setHgap(10);
		mainManagerGrid.setVgap(10);
		mainManagerGrid.add(new Label("Username:"), 0, 0);
		mainManagerGrid.add(usernameField, 1, 0);
		mainManagerGrid.add(new Label("Password:"), 0, 1);
		mainManagerGrid.add(passwordField, 1, 1);
		additionalFields.getChildren().add(mainManagerGrid);
		additionalFields.setVisible(false);

		typeCombo.setOnAction(e -> {
			additionalFields.setVisible("Main Manager".equals(typeCombo.getValue()));
		});

		Button addButton = new Button("Add Manager");
		Button cancelButton = new Button("Cancel");
		HBox buttonBox = new HBox(10, addButton, cancelButton);
		buttonBox.setAlignment(Pos.CENTER);

		layout.getChildren().addAll(typeCombo, grid, additionalFields, buttonBox);

		addButton.setOnAction(e -> {
			if (typeCombo.getValue() == null) {
				outputArea.setText("Please select manager type");
				return;
			}

			if (!validateFields(idField, firstNameField, lastNameField, phoneField, addressField)) {
				outputArea.setText("Please fill all required fields");
				return;
			}

			Manager newManager;
			if ("Main Manager".equals(typeCombo.getValue())) {
				if (usernameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()) {
					outputArea.setText("Username and password required for Main Manager");
					return;
				}
				newManager = new MainManager(idField.getText().trim(), firstNameField.getText().trim(),
						lastNameField.getText().trim(), phoneField.getText().trim(),
						addressField.getText().trim(), usernameField.getText().trim(),
						passwordField.getText().trim());
			} else {
				newManager = new Manager(idField.getText().trim(), firstNameField.getText().trim(),
						lastNameField.getText().trim(), phoneField.getText().trim(),
						addressField.getText().trim());
			}

			if (ourSystem.addManager(newManager)) {
				outputArea.setText("Manager added successfully: " + newManager.getFirstName() + " " + newManager.getLastName());
				formStage.close();
			} else {
				outputArea.setText("Failed to add manager - ID may already exist");
			}
		});

		cancelButton.setOnAction(e -> formStage.close());

		Scene scene = new Scene(layout, 350, 400);
		formStage.setScene(scene);
		formStage.show();
	}

	private void showAddTaxiForm(TextArea outputArea) {
		Stage formStage = new Stage();
		formStage.setTitle("Add Taxi");

		VBox layout = new VBox(10);
		layout.setPadding(new Insets(20));

		ComboBox<String> typeCombo = new ComboBox<>();
		typeCombo.getItems().addAll("Regular", "Express", "Intercity");
		typeCombo.setPromptText("Select taxi type");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField codeField = new TextField();
		CheckBox availableBox = new CheckBox();
		availableBox.setSelected(true);
		TextField minPriceField = new TextField();

		grid.add(new Label("Code:"), 0, 0);
		grid.add(codeField, 1, 0);
		grid.add(new Label("Available:"), 0, 1);
		grid.add(availableBox, 1, 1);
		grid.add(new Label("Min Price:"), 0, 2);
		grid.add(minPriceField, 1, 2);

		// Express taxi fields
		VBox expressFields = new VBox(10);
		GridPane expressGrid = new GridPane();
		expressGrid.setHgap(10);
		expressGrid.setVgap(10);
		CheckBox cityTaxiBox = new CheckBox();
		TextField extraPriceField = new TextField();
		expressGrid.add(new Label("City Taxi:"), 0, 0);
		expressGrid.add(cityTaxiBox, 1, 0);
		expressGrid.add(new Label("Extra Price:"), 0, 1);
		expressGrid.add(extraPriceField, 1, 1);
		expressFields.getChildren().add(expressGrid);
		expressFields.setVisible(false);

		// Intercity taxi fields
		VBox intercityFields = new VBox(10);
		GridPane intercityGrid = new GridPane();
		intercityGrid.setHgap(10);
		intercityGrid.setVgap(10);
		TextField citiesField = new TextField();
		citiesField.setPromptText("Enter cities separated by commas");
		TextField intercityExtraPriceField = new TextField();
		TextField maxHoursField = new TextField();
		intercityGrid.add(new Label("Cities:"), 0, 0);
		intercityGrid.add(citiesField, 1, 0);
		intercityGrid.add(new Label("Extra Price:"), 0, 1);
		intercityGrid.add(intercityExtraPriceField, 1, 1);
		intercityGrid.add(new Label("Max Hours:"), 0, 2);
		intercityGrid.add(maxHoursField, 1, 2);
		intercityFields.getChildren().add(intercityGrid);
		intercityFields.setVisible(false);

		typeCombo.setOnAction(e -> {
			String selectedType = typeCombo.getValue();
			expressFields.setVisible("Express".equals(selectedType));
			intercityFields.setVisible("Intercity".equals(selectedType));
		});

		Button addButton = new Button("Add Taxi");
		Button cancelButton = new Button("Cancel");
		HBox buttonBox = new HBox(10, addButton, cancelButton);
		buttonBox.setAlignment(Pos.CENTER);

		layout.getChildren().addAll(typeCombo, grid, expressFields, intercityFields, buttonBox);

		addButton.setOnAction(e -> {
			try {
				if (typeCombo.getValue() == null || codeField.getText().trim().isEmpty() || 
						minPriceField.getText().trim().isEmpty()) {
					outputArea.setText("Please fill all required fields");
					return;
				}

				String code = codeField.getText().trim();
				boolean available = availableBox.isSelected();
				double minPrice = Double.parseDouble(minPriceField.getText().trim());

				if (minPrice <= 0) {
					outputArea.setText("Min price must be positive");
					return;
				}

				Taxi newTaxi;
				String type = typeCombo.getValue();

				switch (type) {
				case "Regular":
					newTaxi = new Taxi(code, available, minPrice);
					break;
				case "Express":
					if (extraPriceField.getText().trim().isEmpty()) {
						outputArea.setText("Please fill extra price for Express taxi");
						return;
					}
					double extraPrice = Double.parseDouble(extraPriceField.getText().trim());
					if (extraPrice <= 0) {
						outputArea.setText("Extra price must be positive");
						return;
					}
					newTaxi = new ExpressTaxi(code, available, minPrice, cityTaxiBox.isSelected(), extraPrice);
					break;
				case "Intercity":
					if (citiesField.getText().trim().isEmpty() || intercityExtraPriceField.getText().trim().isEmpty() ||
							maxHoursField.getText().trim().isEmpty()) {
						outputArea.setText("Please fill all fields for Intercity taxi");
						return;
					}
					ArrayList<String> cities = new ArrayList<>(Arrays.asList(
							citiesField.getText().trim().split("\\s*,\\s*")));
					double intercityExtraPrice = Double.parseDouble(intercityExtraPriceField.getText().trim());
					int maxHours = Integer.parseInt(maxHoursField.getText().trim());

					if (intercityExtraPrice <= 0 || maxHours <= 0) {
						outputArea.setText("Extra price and max hours must be positive");
						return;
					}

					newTaxi = new IntercityTaxi(code, available, minPrice, cities, intercityExtraPrice, maxHours);
					break;
				default:
					outputArea.setText("Invalid taxi type");
					return;
				}

				if (ourSystem.addTaxi(newTaxi)) {
					outputArea.setText("Taxi added successfully: " + code);
					formStage.close();
				} else {
					outputArea.setText("Failed to add taxi - code may already exist");
				}

			} catch (NumberFormatException ex) {
				outputArea.setText("Please enter valid numbers for price and hours");
			}
		});

		cancelButton.setOnAction(e -> formStage.close());

		Scene scene = new Scene(layout, 350, 500);
		formStage.setScene(scene);
		formStage.show();
	}

	private void showAssignTaxiForm(TextArea outputArea) {
		Stage formStage = new Stage();
		formStage.setTitle("Assign Taxi to Manager");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		ComboBox<String> taxiCombo = new ComboBox<>();
		ComboBox<String> managerCombo = new ComboBox<>();

		// Populate taxi combo
		for (Taxi taxi : ourSystem.getTaxis()) {
			taxiCombo.getItems().add(taxi.getTaxiCode() + " - " + taxi.getClass().getSimpleName());
		}

		// Populate manager combo
		for (Manager manager : ourSystem.getManagers()) {
			managerCombo.getItems().add(manager.getId() + " - " + manager.getFirstName() + " " + manager.getLastName());
		}

		grid.add(new Label("Select Taxi:"), 0, 0);
		grid.add(taxiCombo, 1, 0);
		grid.add(new Label("Select Manager:"), 0, 1);
		grid.add(managerCombo, 1, 1);

		Button assignButton = new Button("Assign");
		Button cancelButton = new Button("Cancel");
		HBox buttonBox = new HBox(10, assignButton, cancelButton);
		buttonBox.setAlignment(Pos.CENTER);
		grid.add(buttonBox, 0, 2, 2, 1);

		assignButton.setOnAction(e -> {
			if (taxiCombo.getValue() == null || managerCombo.getValue() == null) {
				outputArea.setText("Please select both taxi and manager");
				return;
			}

			String taxiCode = taxiCombo.getValue().split(" - ")[0];
			String managerId = managerCombo.getValue().split(" - ")[0];

			Taxi taxi = null;
			for (Taxi t : ourSystem.getTaxis()) {
				if (t.getTaxiCode().equals(taxiCode)) {
					taxi = t;
					break;
				}
			}

			Manager manager = null;
			for (Manager m : ourSystem.getManagers()) {
				if (m.getId().equals(managerId)) {
					manager = m;
					break;
				}
			}

			if (taxi != null && manager != null) {
				manager.addTaxi(taxi);
				outputArea.setText("Taxi " + taxiCode + " assigned to Manager " + manager.getFirstName());
				formStage.close();
			} else {
				outputArea.setText("Error: Taxi or Manager not found");
			}
		});

		cancelButton.setOnAction(e -> formStage.close());

		Scene scene = new Scene(grid, 400, 150);
		formStage.setScene(scene);
		formStage.show();
	}

	private void showAddStationForm(TextArea outputArea) {
		Stage formStage = new Stage();
		formStage.setTitle("Add Station");

		VBox layout = new VBox(10);
		layout.setPadding(new Insets(20));

		TextField nameField = new TextField();
		nameField.setPromptText("Station name");

		Label selectLabel = new Label("Select Taxis:");
		VBox taxiCheckBoxes = new VBox(5);
		List<CheckBox> checkBoxes = new ArrayList<>();

		for (Taxi taxi : ourSystem.getTaxis()) {
			CheckBox cb = new CheckBox(taxi.getTaxiCode() + " - " + taxi.getClass().getSimpleName());
			cb.setUserData(taxi);
			checkBoxes.add(cb);
			taxiCheckBoxes.getChildren().add(cb);
		}

		ScrollPane scrollPane = new ScrollPane(taxiCheckBoxes);
		scrollPane.setPrefHeight(150);

		Button addButton = new Button("Add Station");
		Button cancelButton = new Button("Cancel");
		HBox buttonBox = new HBox(10, addButton, cancelButton);
		buttonBox.setAlignment(Pos.CENTER);

		layout.getChildren().addAll(nameField, selectLabel, scrollPane, buttonBox);

		addButton.setOnAction(e -> {
			if (nameField.getText().trim().isEmpty()) {
				outputArea.setText("Please enter station name");
				return;
			}

			ArrayList<Taxi> selectedTaxis = new ArrayList<>();
			for (CheckBox cb : checkBoxes) {
				if (cb.isSelected()) {
					selectedTaxis.add((Taxi) cb.getUserData());
				}
			}

			if (selectedTaxis.isEmpty()) {
				outputArea.setText("Please select at least one taxi");
				return;
			}

			Station newStation = new Station(nameField.getText().trim(), selectedTaxis);
			if (ourSystem.addStation(newStation)) {
				outputArea.setText("Station added successfully: " + nameField.getText().trim());
				formStage.close();
			} else {
				outputArea.setText("Failed to add station - name may already exist");
			}
		});

		cancelButton.setOnAction(e -> formStage.close());

		Scene scene = new Scene(layout, 350, 350);
		formStage.setScene(scene);
		formStage.show();
	}

	private void showAddOrderForm(TextArea outputArea) {
		Stage formStage = new Stage();
		formStage.setTitle("Add Order");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		TextField orderIdField = new TextField();
		ComboBox<String> subscriberCombo = new ComboBox<>();
		ComboBox<String> taxiCombo = new ComboBox<>();
		TextField dayField = new TextField();
		TextField monthField = new TextField();
		TextField hourField = new TextField();

		// Populate subscriber combo
		for (Subscription sub : ourSystem.getSubscriptions()) {
			subscriberCombo.getItems().add(sub.getSubCode() + " - " + sub.getFirstName() + " " + sub.getLastName());
		}

		// Populate taxi combo with available taxis that current manager is responsible for
		for (Taxi taxi : currentManager.getTaxis()) {
			if (taxi.isAvailable()) {
				taxiCombo.getItems().add(taxi.getTaxiCode() + " - " + taxi.getClass().getSimpleName());
			}
		}

		grid.add(new Label("Order ID:"), 0, 0);
		grid.add(orderIdField, 1, 0);
		grid.add(new Label("Subscriber:"), 0, 1);
		grid.add(subscriberCombo, 1, 1);
		grid.add(new Label("Taxi:"), 0, 2);
		grid.add(taxiCombo, 1, 2);
		grid.add(new Label("Day (1-31):"), 0, 3);
		grid.add(dayField, 1, 3);
		grid.add(new Label("Month (1-12):"), 0, 4);
		grid.add(monthField, 1, 4);
		grid.add(new Label("Hour (0-23):"), 0, 5);
		grid.add(hourField, 1, 5);

		Button addButton = new Button("Add Order");
		Button cancelButton = new Button("Cancel");
		HBox buttonBox = new HBox(10, addButton, cancelButton);
		buttonBox.setAlignment(Pos.CENTER);
		grid.add(buttonBox, 0, 6, 2, 1);

		addButton.setOnAction(e -> {
			try {
				if (orderIdField.getText().trim().isEmpty() || subscriberCombo.getValue() == null ||
						taxiCombo.getValue() == null || dayField.getText().trim().isEmpty() ||
						monthField.getText().trim().isEmpty() || hourField.getText().trim().isEmpty()) {
					outputArea.setText("Please fill all fields");
					return;
				}

				String orderId = orderIdField.getText().trim();
				String subCode = subscriberCombo.getValue().split(" - ")[0];
				String taxiCode = taxiCombo.getValue().split(" - ")[0];
				int day = Integer.parseInt(dayField.getText().trim());
				int month = Integer.parseInt(monthField.getText().trim());
				int hour = Integer.parseInt(hourField.getText().trim());

				if (day < 1 || day > 31 || month < 1 || month > 12 || hour < 0 || hour > 23) {
					outputArea.setText("Please enter valid day (1-31), month (1-12), and hour (0-23)");
					return;
				}

				Taxi taxi = null;
				for (Taxi t : ourSystem.getTaxis()) {
					if (t.getTaxiCode().equals(taxiCode)) {
						taxi = t;
						break;
					}
				}

				if (taxi != null && taxi.isAvailable()) {
					Order newOrder = new Order(orderId, currentManager.getId(), day, month, hour, subCode, taxi, taxi.getMinPrice());
					if (ourSystem.addOrder(newOrder)) {
						taxi.setAvailable(false);
						outputArea.setText("Order added successfully: " + orderId);
						formStage.close();
					} else {
						outputArea.setText("Failed to add order - ID may already exist");
					}
				} else {
					outputArea.setText("Taxi not available or not found");
				}

			} catch (NumberFormatException ex) {
				outputArea.setText("Please enter valid numbers for day, month, and hour");
			}
		});

		cancelButton.setOnAction(e -> formStage.close());

		Scene scene = new Scene(grid, 400, 300);
		formStage.setScene(scene);
		formStage.show();
	}

	private void showChangeOrderTaxiForm(TextArea outputArea) {
		Stage formStage = new Stage();
		formStage.setTitle("Change Taxi in Order");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		ComboBox<String> orderCombo = new ComboBox<>();
		ComboBox<String> newTaxiCombo = new ComboBox<>();

		// Populate order combo with orders from current manager
		for (Order order : ourSystem.getOrders()) {
			if (order.getManagerId().equals(currentManager.getId())) {
				orderCombo.getItems().add(order.getOrderNum() + " - Subscriber: " + order.getSubCode());
			}
		}

		// Populate taxi combo with available taxis that current manager is responsible for
		for (Taxi taxi : currentManager.getTaxis()) {
			if (taxi.isAvailable()) {
				newTaxiCombo.getItems().add(taxi.getTaxiCode() + " - " + taxi.getClass().getSimpleName());
			}
		}

		grid.add(new Label("Select Order:"), 0, 0);
		grid.add(orderCombo, 1, 0);
		grid.add(new Label("New Taxi:"), 0, 1);
		grid.add(newTaxiCombo, 1, 1);

		Button changeButton = new Button("Change Taxi");
		Button cancelButton = new Button("Cancel");
		HBox buttonBox = new HBox(10, changeButton, cancelButton);
		buttonBox.setAlignment(Pos.CENTER);
		grid.add(buttonBox, 0, 2, 2, 1);

		changeButton.setOnAction(e -> {
			if (orderCombo.getValue() == null || newTaxiCombo.getValue() == null) {
				outputArea.setText("Please select both order and new taxi");
				return;
			}

			String orderNum = orderCombo.getValue().split(" - ")[0];
			String newTaxiCode = newTaxiCombo.getValue().split(" - ")[0];

			Order order = null;
			for (Order o : ourSystem.getOrders()) {
				if (o.getOrderNum().equals(orderNum)) {
					order = o;
					break;
				}
			}

			Taxi newTaxi = null;
			for (Taxi t : ourSystem.getTaxis()) {
				if (t.getTaxiCode().equals(newTaxiCode)) {
					newTaxi = t;
					break;
				}
			}

			if (order != null && newTaxi != null) {
				Taxi oldTaxi = order.getTaxi();
				if (!(oldTaxi instanceof Taxi) || oldTaxi instanceof ExpressTaxi || oldTaxi instanceof IntercityTaxi) {
					outputArea.setText("Can only change regular taxis");
					return;
				}

				oldTaxi.setAvailable(true);
				order.setTaxi(newTaxi);
				order.setOrderPrice(newTaxi.getMinPrice());
				newTaxi.setAvailable(false);

				outputArea.setText("Taxi changed successfully in order " + orderNum);
				formStage.close();
			} else {
				outputArea.setText("Error: Order or taxi not found");
			}
		});

		cancelButton.setOnAction(e -> formStage.close());

		Scene scene = new Scene(grid, 400, 150);
		formStage.setScene(scene);
		formStage.show();
	}

	private void showSubscriberOrders(TextArea outputArea) {
		StringBuilder sb = new StringBuilder();
		sb.append("=== MY ORDERS ===\n\n");

		ArrayList<Order> myOrders = ourSystem.getOrdersPerSub().get(currentSubscriber.getSubCode());
		if (myOrders != null && !myOrders.isEmpty()) {
			for (Order order : myOrders) {
				sb.append(order.toString()).append("\n");
			}
		} else {
			sb.append("No orders found.\n");
		}

		outputArea.setText(sb.toString());
	}

	private void showUpdateDetailsForm(TextArea outputArea) {
		Stage formStage = new Stage();
		formStage.setTitle("Update Personal Details");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		TextField phoneField = new TextField(currentSubscriber.getPhone());
		TextField addressField = new TextField(currentSubscriber.getAddress());

		grid.add(new Label("Phone:"), 0, 0);
		grid.add(phoneField, 1, 0);
		grid.add(new Label("Address:"), 0, 1);
		grid.add(addressField, 1, 1);

		Button updateButton = new Button("Update");
		Button cancelButton = new Button("Cancel");
		HBox buttonBox = new HBox(10, updateButton, cancelButton);
		buttonBox.setAlignment(Pos.CENTER);
		grid.add(buttonBox, 0, 2, 2, 1);

		updateButton.setOnAction(e -> {
			if (!phoneField.getText().trim().isEmpty() && !addressField.getText().trim().isEmpty()) {
				currentSubscriber.setPhone(phoneField.getText().trim());
				currentSubscriber.setAddress(addressField.getText().trim());
				outputArea.setText("Details updated successfully");
				formStage.close();
			} else {
				outputArea.setText("Please fill all fields");
			}
		});

		cancelButton.setOnAction(e -> formStage.close());

		Scene scene = new Scene(grid, 300, 150);
		formStage.setScene(scene);
		formStage.show();
	}

	private void showTaxiDetailsForm(TextArea outputArea) {
		Stage formStage = new Stage();
		formStage.setTitle("Show Taxi Details");

		VBox layout = new VBox(10);
		layout.setPadding(new Insets(20));

		TextField taxiCodeField = new TextField();
		taxiCodeField.setPromptText("Enter taxi code");

		Button showButton = new Button("Show Details");
		TextArea detailsArea = new TextArea();
		detailsArea.setEditable(false);
		detailsArea.setPrefRowCount(8);

		Button closeButton = new Button("Close");

		layout.getChildren().addAll(taxiCodeField, showButton, detailsArea, closeButton);

		showButton.setOnAction(e -> {
			String taxiCode = taxiCodeField.getText().trim();
			if (taxiCode.isEmpty()) {
				detailsArea.setText("Please enter a taxi code");
				return;
			}

			Taxi taxi = null;
			for (Taxi t : ourSystem.getTaxis()) {
				if (t.getTaxiCode().equals(taxiCode)) {
					taxi = t;
					break;
				}
			}

			if (taxi != null) {
				detailsArea.setText(taxi.toString());
			} else {
				detailsArea.setText("Taxi not found with code: " + taxiCode);
			}
		});

		closeButton.setOnAction(e -> formStage.close());

		Scene scene = new Scene(layout, 400, 350);
		formStage.setScene(scene);
		formStage.show();
	}

	private boolean validateFields(TextField... fields) {
		for (TextField field : fields) {
			if (field.getText().trim().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		launch(args);
	}
}