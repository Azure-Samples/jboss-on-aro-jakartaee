package org.jboss.eap.demo;

import javax.annotation.PostConstruct;
import javax.faces.annotation.FacesConfig;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import static javax.faces.annotation.FacesConfig.Version.JSF_2_3;

@Named
@ViewScoped
@FacesConfig(version = JSF_2_3)
public class ToDoView implements Serializable {
    private List<ToDo> todos;
    private ToDo todo = new ToDo();
    private String filter = "Pending";

    @Inject
    private ToDoController controller;

    @Inject
    private HttpServletRequest httpRequest;

    @Inject
    HttpSession httpSession;

    @PostConstruct
    public void init() {
        todos = controller.findByCompleted(false);
    }

    public void create() {
        controller.create(todo);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(String.format("'%s' created", todo.getTitle())));

        reloadTodos();
        todo = new ToDo();
    }

    public void reloadTodos() {
        switch (filter) {
            case "Done": {
                this.todos = controller.findByCompleted(true);
                break;
            }
            case "Pending": {
                this.todos = controller.findByCompleted(false);
                break;
            }
            default: {
                this.todos = controller.getAll();
            }
        }
    }

    public void delete(ToDo todo) {
        controller.delete(todo);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(String.format("'%s' deleted", todo.getTitle())));

        reloadTodos();
    }

    public void markCompleted(ToDo todo) {
        controller.update(todo);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(String.format("'%s' done", todo.getTitle())));

        reloadTodos();
    }

    public List<ToDo> getTodos() {
        return todos;
    }

    public void setTodos(List<ToDo> todos) {
        this.todos = todos;
    }

    public ToDo getTodo() {
        return todo;
    }

    public void setTodo(ToDo todo) {
        this.todo = todo;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getSessionId() {
        return httpSession.getId();
    }

    public String getHostName() {
       return System.getenv("KUBERNETES_NAMESPACE") != null ? System.getenv("HOSTNAME") : httpRequest.getLocalName();
    }

    public String getIpAddress() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getClientIpAddress() {
        return httpRequest.getRemoteAddr();
    }
}
