package javafx.imageviewer.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.imageviewer.data.ImageVO;
import javafx.imageviewer.dataprovider.DataProvider;
import javafx.imageviewer.model.ImageViewerModel;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;

public class ImageViewerController {

	private static final Logger LOG = Logger.getLogger(ImageViewerController.class);

	@FXML
	private Label formTitleLabel;
	@FXML
	private TableView<ImageVO> imagesTable;
	@FXML
	private TableColumn<ImageVO, Long> imageIdColumn;
	@FXML
	private TableColumn<ImageVO, String> imageNameColumn;
	@FXML
	private ScrollPane imageScrollPane;
	@FXML
	private ImageView imageViewer;
	@FXML
	private Button previousImageButton;
	@FXML
	private Button nextImageButton;

	private final ImageViewerModel model = new ImageViewerModel();
	private DataProvider dataProvider = new DataProvider();

	public ImageViewerController() {
		LOG.debug("ImageViewerController()");
	}

	@FXML
	private void initialize() {
		LOG.debug("initialize()");

		initializeImageTable();

		imagesTable.itemsProperty().bind(model.imagesProperty());

	}

	private void initializeImageTable() {
		imageIdColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<Long>(cellData.getValue().getId()));
		imageNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
	}

	@FXML
	public void setDirectory() {
		LOG.debug("setDirectory()");
		
		File chooser = getDirectory();
		if (chooser != null) {
			loadImage(chooser.getPath());
		}
	}

	private File getDirectory() {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("JavaFX Projects");
		File defaultDirectory = new File("C:/Users/Public/Pictures/Sample Pictures");
		chooser.setInitialDirectory(defaultDirectory);
		return chooser.showDialog(null);
	}

	@FXML
	private void loadImage(String path) {
		LOG.debug("loadImage(path)");

		Task<Collection<ImageVO>> backgroundTask = new Task<Collection<ImageVO>>() {
			@Override
			protected Collection<ImageVO> call() throws Exception {
				return dataProvider.getImagesFromDirectory(path);
			}

			@Override
			protected void succeeded() {
				model.setImages(new ArrayList<ImageVO>(getValue()));
				imagesTable.getSortOrder().clear();

				LOG.debug("Search completed");
			}
		};

		new Thread(backgroundTask).start();
	}

	@FXML
	public void displaySelectedImage() {
		if (model.imagesProperty().size() != 0) {
			imageViewer.setImage(new Image(imagesTable.getSelectionModel().getSelectedItem().getPath()));
		}
	}
	
	@FXML
	public void keyActionChangeImage(KeyEvent event) {
		if (!model.imagesProperty().isEmpty()) {
			if ((event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.UP)) {
				getPreviousImage();
			}
			if ((event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.DOWN)) {
				getNextImage();
			}

		}
	}
	
	@FXML
	public void startSlideshow() {
		LOG.debug("startSlideshow()");
	}

	@FXML
	public void getPreviousImage() {
		LOG.debug("getPreviousImage()");
		
		displaySelectedImage();
	}

	@FXML
	public void getNextImage() {
		LOG.debug("getNextImage()");
		
		displaySelectedImage();
	}
}
