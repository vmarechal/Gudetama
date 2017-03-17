package fr.crystalgems.gudetama.service;

import fr.crystalgems.gudetama.hibernate.HibernateUtil;
import fr.crystalgems.gudetama.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Antoine on 14/03/2017.
 */

@Path("user")
public class UserService {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public User getUserByIdInJSON(@PathParam("id") int id) {
        User user;
        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            user = HibernateUtil.getSessionFactory().getCurrentSession().get(User.class, id);
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (RuntimeException e) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw e;
        }
        return user;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response createUserInJSON(User user) {
        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            HibernateUtil.getSessionFactory().getCurrentSession().save(user);
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (RuntimeException e) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw e;
        }
        return Response.status(201).build();
    }

    @GET
    @Path("checkEmail")
    @Consumes(MediaType.TEXT_PLAIN + "; charset=UTF-8")
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Boolean checkEmail(String email) {

        boolean response = false;

        System.out.println(email);

        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            List result = HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from User E where E.email = " + email).list();
            System.out.println(result.size());
            response = result.size() == 0;

            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (RuntimeException e) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw e;
        }

        return response;
    }
}