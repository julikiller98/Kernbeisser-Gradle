package kernbeisser.StartUp.DataImport;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import kernbeisser.DBConnection.DBConnection;
import kernbeisser.DBEntities.Article;
import kernbeisser.Windows.MVC.IModel;
import lombok.Cleanup;

public class DataImportModel implements IModel<DataImportController> {
  <T> void batchMergeAll(Collection<T> v) {
    @Cleanup EntityManager em = DBConnection.getEntityManager();
    EntityTransaction et = em.getTransaction();
    et.begin();
    int c = 0;
    for (T t : v) {
      em.merge(t);
      c++;
      if (c % 20 == 0) {
        em.flush();
        em.clear();
      }
    }
    em.flush();
    et.commit();
    em.close();
  }

  <T> void saveAll(Collection<T> v) {
    if (v.size() == 0) {
      return;
    }
    @Cleanup EntityManager em = DBConnection.getEntityManager();
    EntityTransaction et = em.getTransaction();
    et.begin();
    for (T t : v) {
      em.persist(t);
    }
    em.flush();
    et.commit();
    em.close();
  }

  void saveAllItems(Collection<Article> articles) {
    if (articles.size() == 0) {
      return;
    }
    @Cleanup EntityManager em = DBConnection.getEntityManager();
    EntityTransaction et = em.getTransaction();
    et.begin();
    int c = 0;
    for (Article t : articles) {
      t.getOffers().forEach(em::persist);
      em.persist(t);
      c++;
      if (c % 20 == 0) {
        em.flush();
        em.clear();
      }
    }
    em.flush();
    et.commit();
    em.close();
  }
}
