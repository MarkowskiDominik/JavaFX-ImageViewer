package javafx.imageviewer;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Startup extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		String langCode = getParameters().getNamed().get("lang");
		if (langCode != null && !langCode.isEmpty()) {
			Locale.setDefault(Locale.forLanguageTag(langCode));
		}

		Parent root = FXMLLoader.load(getClass().getResource("/javafx/imageviewer/view/image-viewer.fxml"),
				ResourceBundle.getBundle("javafx/imageviewer/bundle/base"));

		System.out.println("ImageViewer");
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/javafx/imageviewer/css/standard.css").toExternalForm());

		primaryStage.setTitle("JavaFX-ImageViewer");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
