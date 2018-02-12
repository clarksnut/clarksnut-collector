package org.clarksnut.models.jpa;

import org.clarksnut.models.MessageModel;
import org.clarksnut.models.MessageProvider;
import org.clarksnut.models.UserModel;
import org.clarksnut.models.UserProvider;
import org.clarksnut.models.jpa.entity.MessageEntity;
import org.clarksnut.models.jpa.entity.UserEntity;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

@RequestScoped
public class JpaMessageProvider implements MessageProvider {

    @PersistenceContext
    private EntityManager em;

    @Override
    public MessageModel getMessage(String id) {
        MessageEntity entity = em.find(MessageEntity.class, id);
        if (entity == null) return null;
        return new MessageAdapter(em, entity);
    }

    @Override
    public MessageModel addMessage(String messageId, String provider) {
        MessageEntity entity = new MessageEntity();

        entity.setId(UUID.randomUUID().toString());
        entity.setMessageId(messageId);
        entity.setProvider(provider);

        em.persist(entity);
        return new MessageAdapter(em, entity);
    }

    @Override
    public MessageModel getUserByMessageIdAndProvider(String messageId, String provider) {
        TypedQuery<MessageEntity> query = em.createNamedQuery("getMessageByProviderAndMessageId", MessageEntity.class);
        query.setParameter("messageId", messageId);
        query.setParameter("provider", provider);
        List<MessageEntity> entities = query.getResultList();
        if (entities.size() == 0) return null;
        return new MessageAdapter(em, entities.get(0));
    }
}
