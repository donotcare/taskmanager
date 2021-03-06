package ru.techport.task.manager.ui.task.dialog.files;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.List;

public class FilesDialog extends Dialog {

    public FilesDialog(List<String> files) {

        setWidth("100%");
        setHeight("100%");
        Grid<String> grid = new Grid<>();
        grid.addColumn(new ComponentRenderer<>(fileName -> {
            StreamResource resource = new StreamResource(fileName, (InputStreamFactory) () -> new ByteArrayInputStream("grgrg".getBytes()));
            Anchor downloadLink = new Anchor(resource, "Скачать");
            downloadLink.getElement().setAttribute("download", true);
            return downloadLink;
        }));
        grid.setItems(files);
        add(grid);
    }
}
