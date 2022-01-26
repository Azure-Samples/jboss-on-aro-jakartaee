package org.jboss.eap.demo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ToDoController {

    @PersistenceContext
    private EntityManager em;

    public ToDo create(ToDo todo) {
        todo.setTitle(todo.getTitle());
        todo.setCompleted(todo.isCompleted());

        em.persist(todo);

        return todo;
    }

    public List<ToDo> getAll() {
        return em.createQuery("SELECT t FROM ToDo t").getResultList();
    }

    public List<ToDo> findByCompleted(boolean completed) {
        return em.createQuery("SELECT t FROM ToDo t where t.completed = :completed")
                .setParameter("completed", completed).getResultList();
    }

    public ToDo getById(Long id) {
        return em.find(ToDo.class, id);
    }

    public ToDo update(ToDo todo) {
        return em.merge(todo);
    }

    public void delete(ToDo todo) {
        if (em.contains(todo)) {
            em.remove(todo);
        }
        else {
            todo = getById(todo.getId());

            if (todo != null) {
                em.remove(todo);
            }
        }
    }
}
