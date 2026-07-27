module HW34 {
	requires javafx.controls;
	requires javafx.graphics;
	
	opens HW3 to javafx.graphics, javafx.fxml;
}
