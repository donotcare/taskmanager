package ru.techport.task.manager.ui.task.dialog;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import ru.techport.task.manager.backend.comment.CommentService;
import ru.techport.task.manager.backend.notification.Notification;
import ru.techport.task.manager.backend.task.Task;
import ru.techport.task.manager.backend.task.TaskService;
import ru.techport.task.manager.backend.user.User;
import ru.techport.task.manager.backend.user.UserService;
import ru.techport.task.manager.ui.system.DateUtils;
import ru.techport.task.manager.ui.task.dialog.files.FilesDialog;
import ru.techport.task.manager.ui.task.dialog.message.MessageForm;
import ru.techport.task.manager.ui.task.dialog.notification.NotificationDialog;

import java.util.List;

public class TaskEditDialog extends Dialog {
    private TaskService taskService;
    private UserService userService;
    private CommentService commentService;
    private Task task;
    private List<Notification> notifications;
    private final MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    private final TextArea text = new TextArea("Задача");
    private final TextField taskDate = new TextField("Дата исполнения");
    private final TextField notificationDate = new TextField("Напоминание");
    private final ComboBox<User> authorCombo = new ComboBox<>("Автор");
    private final ComboBox<User> recipientCombo = new ComboBox<>("Исполнитель");
    private final Button save = new Button("Сохранить", e -> {
        taskService.save(task, notifications, buffer);
        close();
    });
    private final Button cancel = new Button("Выйти", e -> close());
    private final Button files = new Button("Файлы", e -> new FilesDialog(task.getFiles()).open());

    private Binder<Task> binder = new Binder<>(Task.class);

    public TaskEditDialog(TaskService taskService, UserService userService, CommentService commentService, Task task) {
        this.taskService = taskService;
        this.userService = userService;
        this.commentService = commentService;
        this.task = task;
        init();
    }

    public TaskEditDialog(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
        this.task = new Task(userService.getCurrentUser());
        init();
    }

    private void init() {
        setWidth("100%");
        setHeight("100%");
        text.setWidth("100%");
        text.setHeight("100%");
        text.setSizeFull();
        authorCombo.setItems(task.getAuthor());
        authorCombo.setItemLabelGenerator(User::getName);
        authorCombo.setWidth("100%");
        recipientCombo.setItems(userService.getAll());
        recipientCombo.setItemLabelGenerator(User::getName);
        recipientCombo.setWidth("100%");

        HorizontalLayout firstLine = new HorizontalLayout(authorCombo, recipientCombo);
        firstLine.setWidth("100%");
        notifications = taskService.getNotificationsByTask(task);
        NotificationDialog notificationDialog = new NotificationDialog(task, notifications);
        Button notificationButton = new Button("Настроить", e -> notificationDialog.open());

        HorizontalLayout secondLine = new HorizontalLayout(taskDate, notificationDate, notificationButton);
        secondLine.setAlignItems(FlexComponent.Alignment.BASELINE);
        secondLine.setWidth("100%");

        VerticalLayout main = new VerticalLayout();
        main.add(firstLine, secondLine, text);
        main.setSizeFull();

        Upload upload = new Upload(buffer);
        upload.setMaxFileSize(5_000_000);
        upload.setMaxFiles(3);

        HorizontalLayout form = new HorizontalLayout();
        form.add(main, upload);

        HorizontalLayout buttons = new HorizontalLayout(save, cancel, files);
        add(form, new MessageForm(task, commentService), buttons);

        bindValues();
    }

    private void bindValues() {
        binder.setBean(task);
        binder.forField(authorCombo).bind(Task::getAuthor, null);
        binder.forField(recipientCombo).bind(Task::getRecipient, Task::setRecipient);
        binder.forField(taskDate).bind(t -> DateUtils.dateTimeToString(t.getTaskDate()), (t, v) -> t.setTaskDate(DateUtils.stringToDateTime(v)));
        binder.bindInstanceFields(this);
    }

}
