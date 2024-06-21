package com.rbc.zfe0.road.services.transferitem.dao;

import com.rbc.zfe0.road.services.transferitem.entity.Notes;
import lombok.SneakyThrows;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotesDao {

    @Autowired
    private SessionFactory sessionFactory;

    @SneakyThrows
    public void saveNotes(Notes notes) {
        sessionFactory.getCurrentSession().saveOrUpdate(notes);
    }
}
