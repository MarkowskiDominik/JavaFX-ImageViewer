package javafx.imageviewer.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.imageviewer.data.ImageVO;
import javafx.imageviewer.dataprovider.DataProvider;
import javafx.imageviewer.model.ImageViewerModel;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
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
	private ImageView imageView;
	@FXML
	private Button previousImageButton;
	@FXML
	private Button nextImageButton;

	private final ImageViewerModel model = new ImageViewerModel();
	private DataProvider dataProvider = new DataProvider();
	private Boolean slideshow = Boolean.FALSE;

	public ImageViewerController() {
		LOG.debug("ImageViewerController()");
	}

	@FXML
	private void initialize() {
		LOG.debug("initialize()");

		initializeImageTable();
		initializeScrollPane();

		imagesTable.itemsProperty().bind(model.imagesProperty());

	}

	private void initializeImageTable() {
		imageIdColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<Long>(cellData.getValue().getId()));
		imageNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
	}

	private void initializeScrollPane() {
		imageScrollPane.setScaleShape(Boolean.FALSE);
		imageScrollPane.setPannable(true);
		imageScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		imageScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		imageScrollPane.setContent(imageView);

		DoubleProperty zoomProperty = new SimpleDoubleProperty(200);
		zoomProperty.addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				imageView.setFitWidth(zoomProperty.get() * 4);
				imageView.setFitHeight(zoomProperty.get() * 3);				
			}
		});

		imageScrollPane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (event.getDeltaY() > 0) {
					zoomProperty.set(zoomProperty.get() * 1.1);
				} else if (event.getDeltaY() < 0) {
					zoomProperty.set(zoomProperty.get() / 1.1);
				}
			}
		});
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
		chooser.setTitle("JavaFX-ImageViewer");
		chooser.setInitialDirectory(new File("C:/Users/Public/Pictures/Sample Pictures"));
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
		if (!imagesTable.getSelectionModel().isEmpty()) {
			imageView.setImage(new Image(imagesTable.getSelectionModel().getSelectedItem().getPath()));
		}
	}

	@FXML
	public void keyActionChangeImage(KeyEvent event) {
		if (event.getCode() == KeyCode.LEFT) {
			getPreviousImage();
		}
		if (event.getCode() == KeyCode.RIGHT) {
			getNextImage();
		}
		if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
			displaySelectedImage();
		}
	}

	@FXML
	public void startSlideshow() {
		LOG.debug("startSlideshow()");
		
		if(!slideshow) {
			slideshow = Boolean.TRUE;
			slideshow();
		}
		else {
			slideshow = Boolean.FALSE;
		}
	}
	
	private void slideshow() {
		Task<Void> backgroundTask = new Task<Void>() {
			@Override
			protected Void call() throws InterruptedException {
				Thread.sleep(3000);
				return null;
			}

			@Override
			protected void succeeded() {
				getNextImage();

				if (slideshow) {
					slideshow();
				}
			}
		};
		
		new Thread(backgroundTask).start();
	}

	@FXML
	public void getPreviousImage() {
		LOG.debug("getPreviousImage()");

		imagesTable.getSelectionModel().select(imagesTable.getSelectionModel().getSelectedIndex() - 1);
		displaySelectedImage();
	}

	@FXML
	public void getNextImage() {
		LOG.debug("getNextImage()");

		imagesTable.getSelectionModel().select(imagesTable.getSelectionModel().getSelectedIndex() + 1);
		displaySelectedImage();
	}
}
