package ru.com.m74.webapp.springtest.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.com.m74.webapp.springtest.dto.ExampleResult;
import ru.com.m74.webapp.springtest.dto.RemoteUserDTO;
import ru.com.m74.webapp.springtest.model.RemoteUser;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mixam
 * @since 23.03.16 15:49
 */
@RestController
@RequestMapping(path = "/test", method = RequestMethod.GET)
public class TestController {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ModelMapper mapper;

    @RequestMapping(path = "/users")
    public List<RemoteUserDTO> em() throws SQLException, ClassNotFoundException {
        List<RemoteUser> users = em.createQuery("select u from RemoteUser u", RemoteUser.class).getResultList();
        List<RemoteUserDTO> result = new ArrayList();

        for (RemoteUser user : users) {
            RemoteUserDTO dto = new RemoteUserDTO();
            mapper.map(user, dto);
            result.add(dto);
        }

        return result;
    }

    @RequestMapping(path = "/json")
    public ExampleResult json() {
        return new ExampleResult();
    }

    @RequestMapping(path = "/string")
    public String str() {
        return "string";
    }

}
